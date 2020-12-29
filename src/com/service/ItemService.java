package com.service;

import com.bean.ItemBean;
import com.dao.ItemDao;
import com.util.GlobalUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ItemService {
    private ItemDao itemDao = new ItemDao();

    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return 商品数
     * @description 获取数据库中满足查询条件的商品的数量
     */
    public int getItemCount(Map<String, String> paramMap) {
        return itemDao.getItemCount(paramMap);
    }

    /**
     * @param beginIndex 开始的索引位
     * @param pageSize   获取的个数
     * @param paramMap   存放查询条件的一系列键值对
     * @return 商品列表
     * @description 从数据库中获取指定条件的商品
     */
    public List<ItemBean> getItemList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        return itemDao.getItemList(beginIndex, pageSize, paramMap);
    }

    /**
     * @param itemId 商品编号
     * @return 一个商品对象
     * @description 通过商品编号获取商品
     */
    public ItemBean getItem(int itemId) throws Exception {
        ItemBean itemBean = itemDao.getItem(itemId);
        if (itemBean == null) {
            throw new Exception("不存在商品编号为" + itemId + "的商品");
        }
        return itemBean;
    }

    /**
     * @return 商品编号
     * @description 获取下一个可用的商品编号（即数据库中最大itemId加1后的值）
     */
    public int getMaxItemId() {
        return itemDao.getMaxItemId();
    }

    /**
     * @param itemBean 商品对象
     * @description 保存商品
     */
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

    /**
     * @param itemId 商品编号
     * @description 通过商品编号删除商品
     */
    public void deleteItem(int itemId) throws Exception {
        boolean isExist = itemDao.isExistItemId(itemId);//先判断是否存在
        if (isExist) {//存在才能删除
            itemDao.deleteItemById(itemId);
        } else {
            throw new Exception("不存在商品编号为" + itemId + "的商品");
        }
    }


    /**
     * @param itemBean 商品对象
     * @description 更新商品
     */
    public void updateItem(ItemBean itemBean) throws SQLException {
        itemDao.updateItem(itemBean);
    }


    /**
     * @param typeId 商品所属类别的编号
     * @description 通过商品所属类别（大类或小类都行）的编号删除商品
     */
    public void deleteItemsByTypeId(int typeId) {
        itemDao.deleteItemsByTypeId(typeId);
    }

}
