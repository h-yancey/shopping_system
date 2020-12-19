<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>结账</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/font.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/xadmin.css">
    <script src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script>
        layui.use(['form', 'layer']);
    </script>
</head>
<body>
<c:import url="../common/header.jsp"></c:import>
<div class="layui-fluid">
    <div class="layui-card" style="margin-left: 350px;margin-right: 350px">
        <div class="layui-card-body">
            <div class="layui-row">
                <div style="width: 350px;margin: 0px auto">

                    <form class="layui-form" action="${contextPath}/order?task=checkout" method="post">
                        <div class="layui-form-item">
                            <div style="text-align: center"><h2>结账</h2></div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 付款方式 </label>
                            <div class="layui-input-inline">
                                <select name="payType">
                                    <option value="在线支付">在线支付</option>
                                    <option value="货到付款">货到付款</option>
                                    <option value="汇款支付">汇款支付</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货方式 </label>
                            <div class="layui-input-inline">
                                <select name="sendType">
                                    <option value="快递">快递</option>
                                    <option value="邮寄">邮寄</option>
                                    <option value="自提">自提</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"></label>
                            <a href="${contextPath}/cart" class="layui-btn layui-btn-normal">
                                <i class="layui-icon layui-icon-left"></i>返回购物车
                            </a>
                            <button type="submit" class="layui-btn layui-btn-warm">
                                确认结算<i class="layui-icon layui-icon-right"></i>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
