package com.test;

import com.bean.ItemBean;
import com.service.ItemService;
import com.util.GlobalUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class TestService {
    public static void main(String[] args) throws Exception {
//        TypeService typeService = new TypeService();
//        List<TypeBean> typeList = typeService.getTypeList();
//        for (TypeBean type : typeList) {
//            System.out.println(type.getNid());
//            System.out.println(type.getSname());
//        }
//        ItemService itemService= new ItemService();
//        List<ItemBean> list = itemService.getItemList(0,1, paramMap);
//        for (ItemBean bean : list) {
//            System.out.println(bean.getNid());
//            System.out.println(bean.getSname());
//        }
//
//        itemService.deleteItem(3);

//        String str="2020-11-29 22:55:53";
//        Date date = GlobalUtil.formatDate(str);
//        System.out.println(date);
//        int i = (Integer)null;
//        System.out.println(i);
        BigDecimal bigDecimal = new BigDecimal(5000.59);
        String s = GlobalUtil.formatBigDecimal(bigDecimal);
        System.out.println(s);
    }
}
