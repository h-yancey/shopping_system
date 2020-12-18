package com.service;


import com.bean.OrderBean;
import com.bean.OrderItemBean;
import com.dao.OrderDao;
import com.dao.OrderItemDao;

import java.util.List;
import java.util.Map;


public class OrderItemService {
    private OrderItemDao orderItemDao = new OrderItemDao();

//    public List<OrderBean> getOrderList(int beginIndex, int pageSize, Map<String, String> paramMap) {
//        return orderDao.getOrderList(beginIndex, pageSize, paramMap);
//    }

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
//
//    public void deleteItem(int itemId) throws Exception {
//        boolean isExist = itemDao.isExistItemId(itemId);
//        if (isExist) {
//            itemDao.deleteItem(itemId);
//        } else {
//            throw new Exception("您要删除的商品不存在");
//        }
//    }
//
//    public ItemBean getItem(int itemId) throws Exception {
//        ItemBean itemBean = itemDao.getItem(itemId);
//
//        if (itemBean == null) {
//            throw new Exception("您要修改的商品不存在");
//        }
//        return itemBean;
//    }
//
//    public void updateItem(int itemId, ItemBean itemBean) throws SQLException {
//        itemDao.updateItem(itemId, itemBean);
//    }
//
//    public int getItemCount(Map<String, String> paramMap) {
//        return itemDao.getItemCount(paramMap);
//    }


}
