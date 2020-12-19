package com.bean;

import java.math.BigDecimal;
import java.util.Date;

public class ItemBean {
    private int itemId;//商品编号
    private String itemName;//商品名称
    private String itemDesc;//商品描述
    private BigDecimal itemPrice;//商品价格
    private String imgName;//商品图片的名称
    private String shortageTag;//是否缺贷
    private Date addDate;//添加时间
    private int bigTypeId;//所属大类编号
    private int smallTypeId;//所属小类编号
    private String bigTypeName;//所属大类名称
    private String smallTypeName;//所属小类名称

    public ItemBean() {
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getShortageTag() {
        return shortageTag;
    }

    public void setShortageTag(String shortageTag) {
        this.shortageTag = shortageTag;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public int getBigTypeId() {
        return bigTypeId;
    }

    public void setBigTypeId(int bigTypeId) {
        this.bigTypeId = bigTypeId;
    }

    public int getSmallTypeId() {
        return smallTypeId;
    }

    public void setSmallTypeId(int smallTypeId) {
        this.smallTypeId = smallTypeId;
    }

    public String getBigTypeName() {
        return bigTypeName;
    }

    public void setBigTypeName(String bigTypeName) {
        this.bigTypeName = bigTypeName;
    }

    public String getSmallTypeName() {
        return smallTypeName;
    }

    public void setSmallTypeName(String smallTypeName) {
        this.smallTypeName = smallTypeName;
    }

}
