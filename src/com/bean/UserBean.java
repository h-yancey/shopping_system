package com.bean;

import java.util.Date;

public class UserBean {
    private int userid;//用户Id
    private String username;//用户名
    private String pwd;//密码
    private String truename;//真实姓名
    private String sex;//性别
    private Date birth;//出生日期
    private String email;//电子邮箱
    private String phone;//电话号码
    private String address;//地址
    private String postcode;//邮编
    private int authLevel;//权限级别，1表示超级管理员，5表示普通管理员，9表示注册用户
    private Date regDate;//注册日期
    private String lockTag;//是否冻结，1表示冻结，0表示解冻
    private Date lastDate;//最后登录时间
    private int loginNum;//登录次数

    public UserBean() {
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(int authLevel) {
        this.authLevel = authLevel;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getLockTag() {
        return lockTag;
    }

    public void setLockTag(String lockTag) {
        this.lockTag = lockTag;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public int getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }
}
