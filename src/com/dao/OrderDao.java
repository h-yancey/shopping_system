package com.dao;


import com.bean.ItemBean;
import com.bean.OrderBean;
import com.bean.TypeBean;
import com.util.GlobalUtil;
import com.util.JdbcUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class OrderDao {
    public int getMaxOrderId() {
        String sql = "SELECT IFNULL(MAX(orderId),0)+1 AS max_orderid FROM t_order";
        Long maxOrderId = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            maxOrderId = (Long) runner.query(conn, sql, new ScalarHandler("max_orderid"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return maxOrderId.intValue();
    }

    private String getSearchSql(Map<String, String> paramMap) {
        StringBuffer searchSql = new StringBuffer();
        String orderDate = paramMap.get("orderDate");
        String auditStatus = paramMap.get("auditStatus");
        String orderUser = paramMap.get("orderUser");

        if (!GlobalUtil.isEmpty(orderDate)) {
            searchSql.append(" and orderDate like '%" + orderDate + "%'");
        }
        if (!GlobalUtil.isEmpty(auditStatus)) {
            searchSql.append(" and auditStatus = '" + auditStatus + "'");
        }
        if (!GlobalUtil.isEmpty(orderUser)) {
            searchSql.append(" and orderUser = '" + orderUser + "'");
        }
        return searchSql.toString();
    }

    public int getOrderCount(Map<String, String> paramMap) {
        String searchSql = this.getSearchSql(paramMap);
        String sql = "SELECT IFNULL(COUNT(orderId),0) AS order_count FROM t_order";
        if (searchSql != null && searchSql.length() > 0) {
            sql = sql + " WHERE 1=1 " + searchSql;
        }

        Long orderCount = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            orderCount = (Long) runner.query(conn, sql, new ScalarHandler("order_count"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return orderCount.intValue();
    }

    public List<OrderBean> getOrderList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        String searchSql = this.getSearchSql(paramMap);
        String sql = "SELECT * FROM t_order";
        if (searchSql != null && searchSql.length() > 0) {
            sql = sql + " WHERE 1=1 " + searchSql;
        }
        sql += " ORDER BY orderDate ASC LIMIT " + beginIndex + "," + pageSize;
        List<OrderBean> orderist = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            orderist = runner.query(conn, sql, new BeanListHandler<>(OrderBean.class));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return orderist;
    }

    public void saveOrder(OrderBean orderBean) throws SQLException {
        int orderId = orderBean.getOrderId();
        String orderUser = orderBean.getOrderUser();
        Date orderDate = orderBean.getOrderDate();
        String payType = orderBean.getPayType();
        String sendType = orderBean.getSendType();
        int itemTypeSize = orderBean.getItemTypeSize();
        int itemSize = orderBean.getItemSize();
        BigDecimal totalPrice = orderBean.getTotalPrice();
        String auditStatus = orderBean.getAuditStatus();
        String consignee = orderBean.getConsignee();
        String address = orderBean.getAddress();
        String postcode = orderBean.getPostcode();
        String phone = orderBean.getPhone();
        String email = orderBean.getEmail();

        String sql =
                "INSERT INTO t_order(orderId,orderUser,orderDate,payType,sendType,itemTypeSize,itemSize,totalPrice,auditStatus,consignee,address,postcode,phone,email) " + "VALUES(?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?)";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        Object[] params = {orderId, orderUser, orderDate, payType, sendType, itemTypeSize, itemSize, totalPrice, auditStatus, consignee, address, postcode, phone, email};
        try {
            runner.update(conn, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    //    public boolean isExistItemId(int itemId) {
//        String sql = "SELECT IFNULL(COUNT(itemId),0) AS is_exist_itemid FROM t_mc WHERE itemId = ?";
//        Long cntItemId = null;
//        Connection conn = JdbcUtil.getConnection();
//        QueryRunner runner = new QueryRunner();
//        try {
//            cntItemId = (Long) runner.query(conn, sql, new ScalarHandler("is_exist_itemid"), itemId);
//            if (cntItemId > 0) {
//                return true;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            DbUtils.closeQuietly(conn);
//        }
//        return false;
//    }
//

//
//    public void deleteItem(int itemId) throws SQLException {
//        String sql = "DELETE FROM t_mc WHERE itemId = ?";
//        Connection conn = JdbcUtil.getConnection();
//        QueryRunner runner = new QueryRunner();
//        try {
//            runner.update(conn, sql, itemId);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            DbUtils.closeQuietly(conn);
//        }
//    }
//
//    public ItemBean getItem(int itemId) throws SQLException {
//        String sql = "SELECT * FROM t_mc WHERE itemId = ?";
//        ItemBean itemBean = null;
//        Connection conn = JdbcUtil.getConnection();
//        QueryRunner runner = new QueryRunner();
//        try {
//            itemBean = runner.query(conn, sql, new BeanHandler<>(ItemBean.class), itemId);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            DbUtils.closeQuietly(conn);
//        }
//        return itemBean;
//    }
//
//    public void updateItem(int itemId, ItemBean itemBean) throws SQLException {
//        String sql = "UPDATE t_mc SET itemId = ?, itemName = ?, itemDesc = ?, itemPrice = ?, imgName = ?, shortageTag = ?, addDate = ?, bigTypeId " + "=" + " ?, smallTypeId = " + "? WHERE itemId
//        = ?";
//        Connection conn = JdbcUtil.getConnection();
//        QueryRunner runner = new QueryRunner();
//        int itemIdNew = itemBean.getItemId();
//        String itemName = itemBean.getItemName();
//        String itemDesc = itemBean.getItemDesc();
//        BigDecimal itemPrice = itemBean.getItemPrice();
//        String imgName = itemBean.getImgName();
//        String shortageTag = itemBean.getShortageTag();
//        Date addDate = itemBean.getAddDate();
//        int bigTypeId = itemBean.getBigTypeId();
//        int smallTypeId = itemBean.getSmallTypeId();
//        Object[] params = {itemId, itemName, itemDesc, itemPrice, imgName, shortageTag, addDate, bigTypeId, smallTypeId, itemIdNew};
//        try {
//            runner.update(conn, sql, params);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            DbUtils.closeQuietly(conn);
//        }
//    }


}
