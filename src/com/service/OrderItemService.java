package com.service;

import com.bean.OrderItemBean;
import com.dao.OrderItemDao;

import java.util.List;
import java.util.Map;


public class OrderItemService {
    private OrderItemDao orderItemDao = new OrderItemDao();

    public int getMaxId() {
        return orderItemDao.getMaxId();
    }

    public int getOrderItemCount(Map<String, String> paramMap) {
        return orderItemDao.getOrderItemCount(paramMap);
    }

    public List<OrderItemBean> getOrderItemList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        return orderItemDao.getOrderItemList(beginIndex, pageSize, paramMap);
    }

    public List<OrderItemBean> getOrderItemListByOrderId(int orderId) {
        return orderItemDao.getOrderItemListByOrderId(orderId);
    }

    public void saveOrderItem(OrderItemBean orderItemBean) throws Exception {
        orderItemDao.saveOrderItem(orderItemBean);
    }

}
