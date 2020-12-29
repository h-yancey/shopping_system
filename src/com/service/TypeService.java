package com.service;

import java.util.List;

import com.bean.TypeBean;
import com.dao.TypeDao;
import com.util.GlobalUtil;

public class TypeService {
    private TypeDao typeDao = new TypeDao();

    /**
     * @return 类别列表
     * @description 获取带有小类的所有大类
     */
    public List<TypeBean> getTypeList() {
        List<TypeBean> parentTypeList = typeDao.getParentTypeList();
        for (TypeBean typeBean : parentTypeList) {
            //获取每个大类下的所有小类
            int typeId = typeBean.getTypeId();
            List<TypeBean> childTypeList = typeDao.getChildTypeList(typeId);
            typeBean.setChildTypeList(childTypeList);
        }
        return parentTypeList;
    }

    /**
     * @return 类别列表
     * @description 仅获取所有大类
     */
    public List<TypeBean> getParentTypeList() {
        return typeDao.getParentTypeList();
    }

    /**
     * @param typeId
     * @return 一个商品类别对象
     * @description 通过类别编号获取类别
     */
    public TypeBean getType(int typeId) throws Exception {
        TypeBean typeBean = typeDao.getType(typeId);
        if (typeBean == null) {
            throw new Exception("不存在类别编号为"+typeId+"的商品类别");
        }
        return typeBean;
    }

    /**
     * @return 类别编号
     * @description 获取下一个可用类别编号
     */
    public int getMaxTypeId() {
        return typeDao.getMaxTypeId();
    }

    /**
     * @param typeBean 商品类别对象
     * @description 保存商品类别
     */
    public void saveType(TypeBean typeBean) throws Exception {
        String typeName = typeBean.getTypeName();
        if (GlobalUtil.isEmpty(typeName)) {
            throw new Exception("类别名称不能为空");
        } else {
            int typeId = typeBean.getTypeId();
            //判断类别编号是否已存在
            boolean isExistTypeId = typeDao.isExistTypeId(typeId);
            if (isExistTypeId) {
                throw new Exception("添加的类别编号已存在，请更改类别编号");
            }

            int parentId = typeBean.getParentId();
            //判断同一分类下，是否已存在该类别名称
            boolean isExistTypeName = typeDao.isExistTypeName(typeName, parentId);
            if (isExistTypeName) {
                throw new Exception("同一分类下，类别名称不能重复");
            }

            //保存
            typeDao.saveType(typeBean);
        }
    }

    /**
     * @param typeId       类别编号
     * @param parentTypeId 该类别对应的父类编号
     * @description 删除类别
     */
    public void deleteType(int typeId, int parentTypeId) throws Exception {
        boolean isExist = typeDao.isExistTypeId(typeId);
        if (isExist) {
            //删除该类别
            typeDao.deleteTypeById(typeId);

            //删除的是如果是大类，其下的所有小类也删除
            if (parentTypeId == 0) {
                typeDao.deleteChildType(typeId);
            }
        } else {
            throw new Exception("不存在类别编号为"+typeId+"的商品类别");
        }
    }

    /**
     * @description 修改商品类别
     * @param typeBean 商品类别对象
     */
    public void updateType(TypeBean typeBean) throws Exception {
//        if (typeBean.getTypeId() == typeBean.getParentId()) {
//            throw new Exception("同一分类下，类别名称不能重复");
//        }

        //修改的类别名称在同一大类分类下不能重复
        boolean isExistTypeName = typeDao.isExistTypeName(typeBean.getTypeId(), typeBean.getTypeName(), typeBean.getParentId());
        if (isExistTypeName) {
            throw new Exception("同一分类下，类别名称不能重复");
        }
        typeDao.updateType(typeBean);
    }
}
