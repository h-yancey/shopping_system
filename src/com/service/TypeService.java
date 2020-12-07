package com.service;

import java.sql.SQLException;
import java.util.List;

import com.bean.TypeBean;
import com.dao.TypeDao;
import com.util.GlobalUtil;

public class TypeService {
    private TypeDao typeDao = new TypeDao();

    public List<TypeBean> getTypeList() {
        List<TypeBean> parentTypeList = typeDao.getParentTypeList();
        for (TypeBean typeBean : parentTypeList) {
            int id = typeBean.getTypeId();
            List<TypeBean> childTypeList = typeDao.getChildTypeList(id);
            typeBean.setChildTypeList(childTypeList);
        }
        return parentTypeList;
    }

    public List<TypeBean> getParentTypeList() {
        return typeDao.getParentTypeList();
    }

    public int getMaxTypeId() {
        return typeDao.getMaxTypeId();
    }

    public void saveType(TypeBean typeBean) throws Exception {
        String name = typeBean.getTypeName();
        if (GlobalUtil.isEmpty(name)) {
            throw new Exception("类别名称不能为空");
        } else {
            int id = typeBean.getTypeId();
            boolean isExistTypeId = typeDao.isExistTypeId(id);
            if (isExistTypeId) {
                throw new Exception("添加的类别编号已存在，请更改类别编号");
            }
            int npid = typeBean.getParentId();
            boolean isExistTypeName = typeDao.isExistTypeName(name, npid);
            if (isExistTypeName) {
                throw new Exception("同一分类下，类别名称不能重复");
            }
            typeDao.saveType(typeBean);
        }
    }

    public void deleteType(int typeId, int parentTypeId) throws Exception {
        boolean isExist = typeDao.isExistTypeId(typeId);
        if (isExist) {
            typeDao.deleteType(typeId);
            if (parentTypeId == 0) {
                typeDao.deleteChildType(typeId);
            }
        } else {
            throw new Exception("您要删除的类别不存在");
        }
    }


    public TypeBean getTypeById(int typeId) throws Exception {
        TypeBean typeBean = typeDao.getTypeById(typeId);
        if (typeBean == null) {
            throw new Exception("商品类别不存在");
        }
        return typeBean;
    }

    public void updateType(int typeId, TypeBean typeBean) throws Exception {
        if(typeBean.getTypeId() == typeBean.getParentId()){
            throw new Exception("同一分类下，类别名称不能重复");
        }
        boolean isExistTypeName = typeDao.isExistTypeName(typeBean.getTypeId(), typeBean.getTypeName(), typeBean.getParentId());
        if (isExistTypeName) {
            throw new Exception("同一分类下，类别名称不能重复");
        }
        typeDao.updateType(typeId, typeBean);
    }

//    public int getTypeCount() {
//        return  typeDao.getTypeCount();
//    }
}
