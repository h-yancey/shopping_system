package com.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 */
public class OrderBean {
    private int orderId;//订单编号
    private String orderUser;//订单用户名
    private Date orderDate;//下单时间
    private String payType;//付款方式
    private String sendType;//发贷方式
    private int itemTypeSize;//商品种类数
    private int itemSize;//商品总个数
    private BigDecimal totalPrice;//订单总金额
    private String auditStatus;//审核状态，1未审核，2通过，3不通过
    private String msg;//订单反馈
    private String auditUser;//审核人
    private Date auditDate;//审核时间
    private String consignee;//收贷人
    private String address;//收贷人地址
    private String postcode;//收贷人邮编
    private String phone;//收贷人联系电话
    private String email;//收贷人email

    public OrderBean() {

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public int getItemTypeSize() {
        return itemTypeSize;
    }

    public void setItemTypeSize(int itemTypeSize) {
        this.itemTypeSize = itemTypeSize;
    }

    public int getItemSize() {
        return itemSize;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
