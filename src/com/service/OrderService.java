package com.service;


import com.bean.ItemBean;
import com.bean.OrderBean;
import com.dao.OrderDao;
import com.util.GlobalUtil;

import java.util.List;
import java.util.Map;


public class OrderService {
    private OrderDao orderDao = new OrderDao();

    public List<OrderBean> getOrderList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        return orderDao.getOrderList(beginIndex, pageSize, paramMap);
    }

    public int getMaxOrderId() {
        return orderDao.getMaxOrderId();
    }

    public void saveOrder(OrderBean orderBean) throws Exception {
        orderDao.saveOrder(orderBean);
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
