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
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body ">
                    <table class="layui-table">
                        <thead>
                        <th colspan="2">订单基本信息</th>
                        </thead>
                        <tbody>
                        <tr>
                            <td>订单号</td>
                            <td>${orderBean.orderId}</td>
                        </tr>
                        <tr>
                            <td>用户</td>
                            <td>${orderBean.orderUser}</td>
                        </tr>
                        <tr>
                            <td>收货人</td>
                            <td>${orderBean.consignee}</td>
                        </tr>
                        <tr>
                            <td>收货人地址</td>
                            <td>${orderBean.address}</td>
                        </tr>
                        <tr>
                            <td>收货人邮编</td>
                            <td>${orderBean.postcode}</td>
                        </tr>
                        <tr>
                            <td>收货人电话</td>
                            <td>${orderBean.phone}</td>
                        </tr>
                        <tr>
                            <td>收货人邮箱</td>
                            <td>${orderBean.email}</td>
                        </tr>
                        <tr>
                            <td>下单时间</td>
                            <td><fmt:formatDate value="${orderBean.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        </tr>
                        <tr>
                            <td>付款方式</td>
                            <td>${orderBean.payType}</td>
                        </tr>
                        <tr>
                            <td>发货方式</td>
                            <td>${orderBean.sendType}</td>
                        </tr>
                        <tr>
                            <td>审核状态</td>
                            <td>
                                ${orderBean.auditStatus == "1"?"未审核":""}
                                ${orderBean.auditStatus == "2"?"通过":""}
                                ${orderBean.auditStatus == "3"?"不通过":""}
                            </td>
                        </tr>
                        <tr>
                            <td>审核人</td>
                            <td>${orderBean.auditUser}</td>
                        </tr>
                        <tr>
                            <td>审核时间</td>
                            <td><fmt:formatDate value="${orderBean.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        </tr>
                        <tr>
                            <td>不通过原因</td>
                            <td>${orderBean.msg}</td>
                        </tr>
                        </tbody>
                    </table>
                    <table class="layui-table">
                        <thead>
                        <tr>
                            <th colspan="6">订单商品信息</th>
                        </tr>
                        <tr>
                            <th style="text-align: center;width: 20%">商品名称</th>
                            <th style="text-align: center;width: 30%">商品描述</th>
                            <th style="text-align: center;width: 20%">商品图片</th>
                            <th style="text-align: center;width: 10%">商品数量</th>
                            <th style="text-align: center;width: 10%">商品单价</th>
                            <th style="text-align: center;width: 10%">小计</th>
                        </tr>
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
                        <tfoot>
                        <tr>
                            <td colspan="6" align="right">
                                商品种类数：${orderBean.itemTypeSize}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                商品总个数：${orderBean.itemSize}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                商品总金额：￥${orderBean.totalPrice}
                            </td>
                        </tr>
                        </tfoot>
                    </table>
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
>