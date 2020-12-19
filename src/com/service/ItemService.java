package com.service;

import com.bean.ItemBean;
import com.dao.ItemDao;
import com.util.GlobalUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ItemService {
    private ItemDao itemDao = new ItemDao();

    public List<ItemBean> getItemList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        return itemDao.getItemList(beginIndex, pageSize, paramMap);
    }

    public int getMaxItemId() {
        return itemDao.getMaxItemId();
    }

    public void saveItem(ItemBean itemBean) throws Exception {
        String itemName = itemBean.getItemName();
        if (GlobalUtil.isEmpty(itemName)) {
            throw new Exception("类别名称不能为空");
        } else {
            int itemId = itemBean.getItemId();
            boolean isExistItemId = itemDao.isExistItemId(itemId);
            if (isExistItemId) {
                throw new Exception("添加的商品编号已存在，请更改商品编号");
            }
            //商品编号未重复，添加商品
            itemDao.saveItem(itemBean);
        }
    }

    public void deleteItem(int itemId) throws Exception {
        boolean isExist = itemDao.isExistItemId(itemId);
        if (isExist) {
            itemDao.deleteItemById(itemId);
        } else {
            throw new Exception("您要删除的商品不存在");
        }
    }

    public ItemBean getItem(int itemId) throws Exception {
        ItemBean itemBean = itemDao.getItem(itemId);
        if (itemBean == null) {
            throw new Exception("您要修改的商品不存在");
        }
        return itemBean;
    }

    public void updateItem(ItemBean itemBean) throws SQLException {
        itemDao.updateItem(itemBean);
    }

    public int getItemCount(Map<String, String> paramMap) {
        return itemDao.getItemCount(paramMap);
    }

    public void deleteItemsByTypeId(int typeId) {
        itemDao.deleteItemsByTypeId(typeId);
    }


}
