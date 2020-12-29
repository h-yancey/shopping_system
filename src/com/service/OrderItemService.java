package com.service;

import com.bean.OrderItemBean;
import com.dao.OrderItemDao;

import java.util.List;
import java.util.Map;


public class OrderItemService {
    private OrderItemDao orderItemDao = new OrderItemDao();

    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return 订单条目数
     * @description 获取满足查询条件的订单条目的数量
     */
    public int getOrderItemCount(Map<String, String> paramMap) {
        return orderItemDao.getOrderItemCount(paramMap);
    }

    /**
     * @param beginIndex 开始的索引位
     * @param pageSize   获取的个数
     * @param paramMap   存放查询条件的一系列键值对
     * @return 订单条目列表
     * @description 获取指定条件的订单条目
     */
    public List<OrderItemBean> getOrderItemList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        return orderItemDao.getOrderItemList(beginIndex, pageSize, paramMap);
    }

    /**
     * @param orderId 订单编号
     * @return 订单条目列表
     * @description 获取指定订单编号的订单中的所有订单条目
     */
    public List<OrderItemBean> getOrderItemListByOrderId(int orderId) {
        return orderItemDao.getOrderItemListByOrderId(orderId);
    }

    /**
     * @return 订单条目编号
     * @description 获取下一个可用的订单条目编号
     */
    public int getMaxId() {
        return orderItemDao.getMaxId();
    }

    /**
     * @param orderItemBean 订单条目对象
     * @description 保存订单条目
     */
    public void saveOrderItem(OrderItemBean orderItemBean) throws Exception {
        orderItemDao.saveOrderItem(orderItemBean);
    }

}
