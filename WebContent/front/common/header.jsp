<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>header</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/lib/layui/css/layui.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/font.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/xadmin.css">
    <!-- <link rel="stylesheet" href="./css/theme5.css"> -->
    <script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery-validation/jquery.validate.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery-validation/localization/messages_zh.js"></script>
    <script type="text/javascript" src="${contextPath}/js/My97DatePicker/WdatePicker.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">
    <div class="logo">
        <a href="${contextPath}">购 物 商 城</a>
    </div>
    <ul class="layui-nav left">
        <li class="layui-nav-item">
            <a href="${contextPath}"><i class="layui-icon layui-icon-home">&nbsp;首页</i></a>
        </li>
        <li class="layui-nav-item">
            <a href="${contextPath}/member"><i class="layui-icon layui-icon-user">&nbsp;会员中心</i></a>
        </li>
        <li class="layui-nav-item">
            <a href="${contextPath}/cart"><i class="layui-icon layui-icon-cart">&nbsp;购物车</i></a>
        </li>
    </ul>
    <ul class="layui-nav right">
        <c:if test="${empty frontUserBean}">
            <li class="layui-nav-item">
                <a href="${contextPath}/login.jsp">登录</a>
            </li>
            <li class="layui-nav-item">
                <a href="${contextPath}/register.jsp">注册</a>
            </li>
        </c:if>
        <c:if test="${not empty frontUserBean}">
            <li class="layui-nav-item">
                <a href="javascript:;">${frontUserBean.username}</a>
                <dl class="layui-nav-child">
                    <!-- 二级菜单 -->
                    <dd><a href="${contextPath}/member">个人资料</a></dd>
                    <dd><a href="${contextPath}/member?task=editPwd">密码修改</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item">
                <a href="${contextPath}/servlet/FrontLoginServlet?task=logout">退出</a>
            </li>
        </c:if>
    </ul>
</div>
</body>
</html>
