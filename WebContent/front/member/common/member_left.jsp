<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>会员中心左边导航栏</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/font.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/xadmin.css">
    <script src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <style>
        dl.member-dl {
            text-align: left;
            padding-left: 60px;
        }

        .member-dl dt {
            font-size: 28px;
            font-weight: bold;
            line-height: 50px;
            color: #009688;
        }

        .member-dl dd {
            font-size: 13px;
            line-height: 25px;
        }

        .member-dl a:hover {
            color: #009688;
        }
    </style>
</head>
<body>
<div class="layui-show">
    <dl class="member-dl">
        <dt>会员中心</dt>
        <dd>
            <a href="${contextPath}/member">基本资料</a>
        </dd>
        <dd>
            <a href="${contextPath}/member?task=editPwd">密码修改</a>
        </dd>
        <dd>
            <a href="${contextPath}/member?task=myOrder">我的订单</a>
        </dd>
    </dl>
</div>
</body>
</html>
