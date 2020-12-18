package com.service;


import com.bean.OrderBean;
import com.dao.OrderDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class OrderService {
    private OrderDao orderDao = new OrderDao();

    public int getMaxOrderId() {
        return orderDao.getMaxOrderId();
    }

    public int getOrderCount(Map<String, String> paramMap) {
        return orderDao.getOrderCount(paramMap);
    }

    public List<OrderBean> getOrderList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        return orderDao.getOrderList(beginIndex, pageSize, paramMap);
    }


    public void saveOrder(OrderBean orderBean) throws Exception {
        orderDao.saveOrder(orderBean);
    }

    public OrderBean getOrder(int orderId) throws Exception {
        OrderBean orderBean = orderDao.getOrder(orderId);

        if (orderBean == null) {
            throw new Exception("订单不存在");
        }
        return orderBean;
    }

    public void updateOrderAudit(OrderBean orderBean) throws SQLException {
        orderDao.updateOrderAudit(orderBean);
    }

//    public void deleteItem(int itemId) throws Exception {
//        boolean isExist = itemDao.isExistItemId(itemId);
//        if (isExist) {
//            itemDao.deleteItem(itemId);
//        } else {
//            throw new Exception("您要删除的商品不存在");
//        }
//    }
//


}
