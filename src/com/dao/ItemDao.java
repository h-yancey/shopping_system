package com.dao;

import com.bean.ItemBean;
import com.bean.TypeBean;
import com.util.GlobalUtil;
import com.util.JdbcUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ItemDao {
    private TypeDao typeDao = new TypeDao();

    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return where子句
     * @description 拼接sql语句的查询条件参数
     */
    private String getSearchSql(Map<String, String> paramMap) {
        StringBuffer searchSql = new StringBuffer();
        String itemTypeId = paramMap.get("itemTypeId");//商品所属类别的编号
        String keyword = paramMap.get("keyword");//关键字
        String priceMin = paramMap.get("priceMin");//最小价格
        String priceMax = paramMap.get("priceMax");//最大价格

        if (!GlobalUtil.isEmpty(itemTypeId)) {
            try {
                TypeBean typeBean = typeDao.getType(Integer.parseInt(itemTypeId));
                if (typeBean != null) {
                    int parentId = typeBean.getParentId();
                    //判断按大类还是小类查找
                    if (parentId == 0) {
                        //查找的是大类
                        searchSql.append(" and bigTypeId = " + itemTypeId);
                    } else if (parentId > 0) {
                        //查找的是小类
                        searchSql.append(" and smallTypeId = " + itemTypeId);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (!GlobalUtil.isEmpty(keyword)) {
            //关键字用于商品名称或商品描述的模糊查询
            searchSql.append(" and (itemName like '%" + keyword + "%'");
            searchSql.append(" or itemDesc like '%" + keyword + "%')");
        }
        if (!GlobalUtil.isEmpty(priceMin)) {
            searchSql.append(" and itemPrice >= " + priceMin);
        }
        if (!GlobalUtil.isEmpty(priceMax)) {
            searchSql.append(" and itemPrice <= " + priceMax);
        }
        return searchSql.toString();
    }

    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return 商品数
     * @description 获取数据库中满足查询条件的商品的数量
     */
    public int getItemCount(Map<String, String> paramMap) {
        String searchSql = this.getSearchSql(paramMap);
        String sql = "SELECT IFNULL(COUNT(itemId),0) AS item_count FROM t_mc";
        if (searchSql != null && searchSql.length() > 0) {
            sql = sql + " WHERE 1=1 " + searchSql;
        }

        Long itemCount = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            itemCount = (Long) runner.query(conn, sql, new ScalarHandler("item_count"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return itemCount.intValue();
    }

    /**
     * @param beginIndex 开始的索引位
     * @param pageSize   获取的个数
     * @param paramMap   存放查询条件的一系列键值对
     * @return 商品列表
     * @description 从数据库中获取指定条件的商品
     */
    public List<ItemBean> getItemList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        String searchSql = this.getSearchSql(paramMap);
        String sql = "SELECT * FROM t_mc";
        if (searchSql != null && searchSql.length() > 0) {
            sql = sql + " WHERE 1=1 " + searchSql;
        }
        sql += " ORDER BY itemId ASC LIMIT " + beginIndex + "," + pageSize;
        List<ItemBean> itemList = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            itemList = runner.query(conn, sql, new BeanListHandler<>(ItemBean.class));
            Map<Integer, String> typeMap = typeDao.getTypeMap();
            for (ItemBean itemBean : itemList) {
                int bigTypeId = itemBean.getBigTypeId();
                int smallTypeId = itemBean.getSmallTypeId();
                String bigTypeName = typeMap.get(bigTypeId);
                String smallTypeName = typeMap.get(smallTypeId);
                itemBean.setBigTypeName(bigTypeName);
                itemBean.setSmallTypeName(smallTypeName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return itemList;
    }

    /**
     * @param itemId 商品编号
     * @return 存在返回一个商品对象，不存在返回null
     * @description 通过商品编号从数据库中获取商品
     */
    public ItemBean getItem(int itemId) throws SQLException {
        String sql = "SELECT * FROM t_mc WHERE itemId = ?";
        ItemBean itemBean = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            itemBean = runner.query(conn, sql, new BeanHandler<>(ItemBean.class), itemId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return itemBean;
    }

    /**
     * @return 最大itemId加1后的值
     * @description 获取数据库中最大itemId加1后的值
     */
    public int getMaxItemId() {
        String sql = "SELECT IFNULL(MAX(itemId),0)+1 AS max_itemid FROM t_mc";
        Long maxItemId = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            maxItemId = (Long) runner.query(conn, sql, new ScalarHandler("max_itemid"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return maxItemId.intValue();
    }

    /**
     * @param itemId 商品编号
     * @return 存放返回true, 不存在返回false
     * @description 判断商品编号是否在数据库中已存在
     */
    public boolean isExistItemId(int itemId) {
        String sql = "SELECT IFNULL(COUNT(itemId),0) AS is_exist_itemid FROM t_mc WHERE itemId = ?";
        Long cntItemId = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            cntItemId = (Long) runner.query(conn, sql, new ScalarHandler("is_exist_itemid"), itemId);
            if (cntItemId > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return false;
    }

    /**
     * @param itemBean 商品对象
     * @description 将商品保存到数据库
     */
    public void saveItem(ItemBean itemBean) throws SQLException {
        int itemId = itemBean.getItemId();
        String itemName = itemBean.getItemName();
        String itemDesc = itemBean.getItemDesc();
        BigDecimal itemPrice = itemBean.getItemPrice();
        String imgName = itemBean.getImgName();
        String shortageTag = itemBean.getShortageTag();
        Date addDate = itemBean.getAddDate();
        int bigTypeId = itemBean.getBigTypeId();
        int smallTypeId = itemBean.getSmallTypeId();
        String sql = "INSERT INTO t_mc VALUES(?,?,?,?,?,?,?,?,?)";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        Object[] params = {itemId, itemName, itemDesc, itemPrice, imgName, shortageTag, addDate, bigTypeId, smallTypeId};
        try {
            runner.update(conn, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * @param itemId 商品编号
     * @description 通过商品编号从数据库中删除商品
     */
    public void deleteItemById(int itemId) throws SQLException {
        String sql = "DELETE FROM t_mc WHERE itemId = ?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            runner.update(conn, sql, itemId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * @param typeId 商品所属类别的编号
     * @description 通过商品所属类别（大类或小类都行）的编号删除商品
     */
    public void deleteItemsByTypeId(int typeId) {
        String sql = "DELETE FROM t_mc WHERE smallTypeId = ? OR bigTypeId = ?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            runner.update(conn, sql, typeId, typeId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * @param itemBean 商品对象
     * @description 更新商品
     */
    public void updateItem(ItemBean itemBean) throws SQLException {
        String sql = "UPDATE t_mc SET itemName = ?, itemDesc = ?, itemPrice = ?, imgName = ?, shortageTag = ?, addDate = ?, bigTypeId =  ?, smallTypeId = ? WHERE itemId = ?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        String itemName = itemBean.getItemName();
        String itemDesc = itemBean.getItemDesc();
        BigDecimal itemPrice = itemBean.getItemPrice();
        String imgName = itemBean.getImgName();
        String shortageTag = itemBean.getShortageTag();
        Date addDate = itemBean.getAddDate();
        int bigTypeId = itemBean.getBigTypeId();
        int smallTypeId = itemBean.getSmallTypeId();
        int itemId = itemBean.getItemId();
        Object[] params = {itemName, itemDesc, itemPrice, imgName, shortageTag, addDate, bigTypeId, smallTypeId, itemId};
        try {
            runner.update(conn, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }


}
