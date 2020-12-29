package com.service;


import com.bean.OrderBean;
import com.dao.OrderDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class OrderService {
    private OrderDao orderDao = new OrderDao();

    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return 订单数
     * @description 获取满足查询条件的订单的数量
     */
    public int getOrderCount(Map<String, String> paramMap) {
        return orderDao.getOrderCount(paramMap);
    }

    /**
     * @param beginIndex 开始的索引位
     * @param pageSize   获取的个数
     * @param paramMap   存放查询条件的一系列键值对
     * @return 订单列表
     * @description 获取指定条件的订单
     */
    public List<OrderBean> getOrderList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        return orderDao.getOrderList(beginIndex, pageSize, paramMap);
    }

    /**
     * @param orderId 订单编号
     * @return 存在返回一个订单对象，不存在返回null
     * @description 通过订单编号获取订单
     */
    public OrderBean getOrder(int orderId) throws Exception {
        OrderBean orderBean = orderDao.getOrder(orderId);
        if (orderBean == null) {
            throw new Exception("不存在订单编号为" + orderId + "的订单");
        }
        return orderBean;
    }

    /**
     * @return 订单编号
     * @description 获取下一个可用的订单编号
     */
    public int getMaxOrderId() {
        return orderDao.getMaxOrderId();
    }

    /**
     * @param orderBean 订单对象
     * @description 保存订单
     */
    public void saveOrder(OrderBean orderBean) throws Exception {
        orderDao.saveOrder(orderBean);
    }

    /**
     * @param orderBean 订单对象
     * @description 修改订单信息中的审核相关的字段，有审核（订单）状态auditStatus、订单反馈msg、审核用户auditUser、审核日期auditDate
     */
    public void updateOrderAudit(OrderBean orderBean) throws SQLException {
        orderDao.updateOrderAudit(orderBean);
    }

    /**
     * @param orderBean 订单对象
     * @description 修改订单信息中的付款方式payType、发货方式sendType、收货人consignee、地址address、邮编postcode、电话phone、邮箱email
     */
    public void updateOrder(OrderBean orderBean) throws SQLException {
        orderDao.updateOrder(orderBean);
    }
}
