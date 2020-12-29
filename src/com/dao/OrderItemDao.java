package com.dao;


import com.bean.OrderItemBean;
import com.util.GlobalUtil;
import com.util.JdbcUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class OrderItemDao {
    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return where子句
     * @description 拼接sql语句的查询条件参数
     */
    private String getSearchSql(Map<String, String> paramMap) {
        StringBuffer searchSql = new StringBuffer();
        String orderId = paramMap.get("orderId");

        if (!GlobalUtil.isEmpty(orderId)) {
            searchSql.append(" and orderId = '" + orderId + "'");
        }
        return searchSql.toString();
    }

    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return 订单条目数
     * @description 获取数据库中满足查询条件的订单条目的数量
     */
    public int getOrderItemCount(Map<String, String> paramMap) {
        String searchSql = this.getSearchSql(paramMap);
        String sql = "SELECT IFNULL(COUNT(id),0) AS order_item_count FROM t_order_item";
        if (searchSql != null && searchSql.length() > 0) {
            sql = sql + " WHERE 1=1 " + searchSql;
        }

        Long orderItemCount = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            orderItemCount = (Long) runner.query(conn, sql, new ScalarHandler("order_item_count"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return orderItemCount.intValue();
    }

    /**
     * @param beginIndex 开始的索引位
     * @param pageSize   获取的个数
     * @param paramMap   存放查询条件的一系列键值对
     * @return 订单条目列表
     * @description 从数据库中获取指定条件的订单条目
     */
    public List<OrderItemBean> getOrderItemList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        String searchSql = this.getSearchSql(paramMap);
        String sql = "SELECT * FROM t_order_item";
        if (searchSql != null && searchSql.length() > 0) {
            sql = sql + " WHERE 1=1 " + searchSql;
        }
        sql += " ORDER BY id ASC LIMIT " + beginIndex + "," + pageSize;
        List<OrderItemBean> orderItemList = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            orderItemList = runner.query(conn, sql, new BeanListHandler<>(OrderItemBean.class));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return orderItemList;
    }

    /**
     * @param orderId 订单编号
     * @return 订单条目列表
     * @description 获取指定订单编号的订单中的所有订单条目
     */
    public List<OrderItemBean> getOrderItemListByOrderId(int orderId) {
        String sql = "SELECT * FROM t_order_item WHERE orderId = ?";
        List<OrderItemBean> orderItemList = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            orderItemList = runner.query(conn, sql, new BeanListHandler<>(OrderItemBean.class), orderId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return orderItemList;
    }

    /**
     * @return 最大id加1后的值
     * @description 获取数据库中最大id加1后的值
     */
    public int getMaxId() {
        String sql = "SELECT IFNULL(MAX(id),0)+1 AS max_id FROM t_order_item";
        Long maxId = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            maxId = (Long) runner.query(conn, sql, new ScalarHandler("max_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return maxId.intValue();
    }

    /**
     * @param orderItemBean 订单条目对象
     * @description 将订单条目保存到数据库
     */
    public void saveOrderItem(OrderItemBean orderItemBean) throws SQLException {
        int id = orderItemBean.getId();
        int orderId = orderItemBean.getOrderId();
        int itemId = orderItemBean.getItemId();
        String itemName = orderItemBean.getItemName();
        String itemDesc = orderItemBean.getItemDesc();
        String imgName = orderItemBean.getImgName();
        int itemCount = orderItemBean.getItemCount();
        BigDecimal itemPrice = orderItemBean.getItemPrice();
        BigDecimal totalPrice = orderItemBean.getTotalPrice();

        String sql = "INSERT INTO t_order_item VALUES(?,?,?,?,?,?,?,?,?)";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        Object[] params = {id, orderId, itemId, itemName, itemDesc, imgName, itemCount, itemPrice, totalPrice};
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
