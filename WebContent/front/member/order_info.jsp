<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>查看订单</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" href="${contextPath}/css/font.css">
    <link rel="stylesheet" href="${contextPath}/css/xadmin.css">
    <script type="text/javascript" src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
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
<div class="layui-fluid">
    <div class="layui-row" style="height: 800px">
        <div class="layui-col-md3">
            <div class="layui-card" style="height: 100%">
                <div class="layui-form">
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            订单编号
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.orderId}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            订单用户名
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.orderUser}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            下单时间
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            <fmt:formatDate value="${orderBean.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            付款方式
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.payType}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            发货方式
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.sendType}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            商品种类数
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.itemTypeSize}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            商品总个数
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.itemSize}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            订单总金额
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ￥${orderBean.totalPrice}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            审核状态
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.auditStatus == "1"?"未审核":""}
                            ${orderBean.auditStatus == "2"?"通过":""}
                            ${orderBean.auditStatus == "3"?"不通过":""}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            订单反馈
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.msg}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            收贷人
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.consignee}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            收贷人地址
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.address}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            收贷人邮编
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.postcode}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            收贷人电话
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.phone}
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">
                            收贷人邮箱
                        </label>
                        <div class="layui-form-label" style="width: 150px;text-align: left">
                            ${orderBean.email}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-col-md9">
            <div class="layui-card" style="height: 100%">
                <div class="layui-card-body">
                    <table class="layui-table">
                        <thead>
                        <tr>
                            <th style="text-align: center;width: 20%">商品名称</th>
                            <th style="text-align: center;width: 30%">商品描述</th>
                            <th style="text-align: center;width: 20%">商品图片</th>
                            <th style="text-align: center;width: 10%">商品数量</th>
                            <th style="text-align: center;width: 10%">商品单价</th>
                            <th style="text-align: center;width: 10%">总计</th>
                        </thead>
                        <tbody>
                        <c:forEach items="${orderItemList}" var="orderItemBean">
                            <tr>
                                <td>${orderItemBean.itemName}</td>
                                <td>${orderItemBean.itemDesc}</td>
                                <td align="center"><img src="${contextPath}/upload/${orderItemBean.imgName}"></td>
                                <td align="center">${orderItemBean.itemCount}</td>
                                <td align="right">￥${orderItemBean.itemPrice}</td>
                                <td align="right">￥${orderItemBean.totalPrice}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="layui-card-body ">
                    <div class="page">
                        ${pageTool}
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
</body>
</html>