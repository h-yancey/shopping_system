package com.bean;

import java.util.List;

public class TypeBean {
    private int typeId;// 类别编号
    private String typeName;// 类别名称
    private int parentId;// 父类编号，0表示该类型是父类
    private List<TypeBean> childTypeList;//子类别

    public TypeBean() {
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<TypeBean> getChildTypeList() {
        return childTypeList;
    }

    public void setChildTypeList(List<TypeBean> childTypeList) {
        this.childTypeList = childTypeList;
    }
}
