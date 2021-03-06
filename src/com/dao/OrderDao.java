package com.dao;


import com.bean.OrderBean;
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


public class OrderDao {
    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return where子句
     * @description 拼接sql语句的查询条件参数
     */
    private String getSearchSql(Map<String, String> paramMap) {
        StringBuffer searchSql = new StringBuffer();
        String orderDate = paramMap.get("orderDate");//下单日期
        String auditStatus = paramMap.get("auditStatus");//订单（审核）状态
        String orderUser = paramMap.get("orderUser");//审核用户的用户名
        String startDate = paramMap.get("startDate");//日期区间的开始日期
        String endDate = paramMap.get("endDate");//日期区间的结束日期
        String itemName = paramMap.get("itemName");//商品名称

        if (!GlobalUtil.isEmpty(orderDate)) {
            searchSql.append(" and orderDate like '%" + orderDate + "%'");
        }
        if (!GlobalUtil.isEmpty(auditStatus)) {
            searchSql.append(" and auditStatus = '" + auditStatus + "'");
        }
        if (!GlobalUtil.isEmpty(orderUser)) {
            searchSql.append(" and orderUser = '" + orderUser + "'");
        }
        if (!GlobalUtil.isEmpty(startDate)) {
            searchSql.append(" and orderDate >= '" + startDate + "'");
        }
        if (!GlobalUtil.isEmpty(endDate)) {
            searchSql.append(" and orderDate <= '" + endDate + " 23:59:59'");
        }
        if (!GlobalUtil.isEmpty(itemName)) {
            //拼接子查询
            searchSql.append(" and orderId in (select orderId from t_order_item where itemName like '%" + itemName + "%')");
        }
        return searchSql.toString();
    }

    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return 订单数
     * @description 获取数据库中满足查询条件的订单的数量
     */
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

    /**
     * @param beginIndex 开始的索引位
     * @param pageSize   获取的个数
     * @param paramMap   存放查询条件的一系列键值对
     * @return 订单列表
     * @description 从数据库中获取指定条件的订单
     */
    public List<OrderBean> getOrderList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        String searchSql = this.getSearchSql(paramMap);
        String sql = "SELECT * FROM t_order";
        if (searchSql != null && searchSql.length() > 0) {
            sql = sql + " WHERE 1=1 " + searchSql;
        }
        sql += " ORDER BY orderDate ASC LIMIT " + beginIndex + "," + pageSize;
        List<OrderBean> orderList = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            orderList = runner.query(conn, sql, new BeanListHandler<>(OrderBean.class));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return orderList;
    }

    /**
     * @param orderId 订单编号
     * @return 存在返回一个订单对象，不存在返回null
     * @description 通过订单编号从数据库中获取订单
     */
    public OrderBean getOrder(int orderId) throws SQLException {
        String sql = "SELECT * FROM t_order WHERE orderId = ?";
        OrderBean orderBean = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            orderBean = runner.query(conn, sql, new BeanHandler<>(OrderBean.class), orderId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return orderBean;
    }

    /**
     * @return 最大orderId加1后的值
     * @description 获取数据库中最大orderId加1后的值
     */
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

    /**
     * @param orderBean 订单对象
     * @description 保存订单（除了审核相关的字段以外的其他字段）到数据库
     */
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

        String sql = "INSERT INTO t_order(orderId,orderUser,orderDate,payType,sendType,itemTypeSize,itemSize,totalPrice,auditStatus,consignee,address,postcode,phone,email) VALUES(?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?)";
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

    /**
     * @param orderBean 订单对象
     * @description 修改订单信息中的审核相关的字段，有审核（订单）状态auditStatus、订单反馈msg、审核用户auditUser、审核日期auditDate
     */
    public void updateOrderAudit(OrderBean orderBean) throws SQLException {
        String sql = "UPDATE t_order SET auditStatus = ?, msg = ?, auditUser = ?, auditDate = ? WHERE orderId = ?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        String auditStatus = orderBean.getAuditStatus();
        String msg = orderBean.getMsg();
        String auditUser = orderBean.getAuditUser();
        Date auditDate = orderBean.getAuditDate();
        int orderId = orderBean.getOrderId();
        Object[] params = {auditStatus, msg, auditUser, auditDate, orderId};
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
     * @param orderBean 订单对象
     * @description 修改订单信息中的付款方式payType、发货方式sendType、收货人consignee、地址address、邮编postcode、电话phone、邮箱email
     */
    public void updateOrder(OrderBean orderBean) throws SQLException {
        String sql = "UPDATE t_order SET payType = ?, sendType = ?, consignee = ?, address = ?, postcode = ?, phone = ?, email = ? WHERE orderId = ?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        String payType = orderBean.getPayType();
        String sendType = orderBean.getSendType();
        String consignee = orderBean.getConsignee();
        String address = orderBean.getAddress();
        String postcode = orderBean.getPostcode();
        String phone = orderBean.getPhone();
        String email = orderBean.getEmail();
        int orderId = orderBean.getOrderId();
        Object[] params = {payType, sendType, consignee, address, postcode, phone, email, orderId};
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
