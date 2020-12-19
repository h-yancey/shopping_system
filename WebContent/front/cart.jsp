<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>购物车</title>
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

        function deleteCartItem(itemId) {
            layer.confirm("是否从购物车中删除该商品？", {btn: ['是', '否']}, function () {
                var deleteUrl = "${contextPath}/cart?task=delete";
                var data = {
                    itemId: itemId,
                };
                $.post(deleteUrl, data, function () {
                    layer.msg('已从购物车中删除', {icon: 1, time: 1000}, function () {
                        location.reload();
                    });
                });
            });
        }

        function clearCart() {
            layer.confirm("是否清空购物车？", {btn: ['是', '否']}, function () {
                var clearUrl = "${contextPath}/cart?task=clear";
                $.post(clearUrl, function () {
                    layer.msg('已清空购物车', {icon: 1, time: 1000}, function () {
                        location.reload();
                    });
                });
            });
        }

        function updateCartItemCount(object, itemId, prevItemCount) {
            var itemCount = object.value;
            if (itemCount.match('^[0-9]*$') && itemCount > 0) {
                var url = "${contextPath}/cart?task=updateCartItemCount";
                var data = {
                    itemId: itemId,
                    itemCount: itemCount
                }
                $.post(url, data, function () {
                    location.reload();
                    // layer.msg("数量修改成功", {icon: 1, time: 1000}, function () {
                    //     location.reload();
                    // });
                });
            } else {
                layer.msg("商品数量必须是非负整数", {icon: 2, time: 1000}, function () {
                    object.value = prevItemCount;
                });
            }
        }

        function decCartItemCount(object, itemId) {
            var input = $(object).siblings("input");
            var prevItemCount = input.val();
            var itemCount = Number(prevItemCount) - 1;
            input.val(itemCount);
            updateCartItemCount(input[0], itemId, prevItemCount);
        }

        function incCartItemCount(object, itemId) {
            var input = $(object).siblings("input");
            var prevItemCount = input.val();
            var itemCount = Number(prevItemCount) + 1;
            input.val(itemCount);
            updateCartItemCount(input[0], itemId, prevItemCount);
        }

        function keyDownEvent(event, object, itemId, prevItemCount) {
            if (event.keyCode == 13) {
                updateCartItemCount(object, itemId, prevItemCount);
                $(object).blur();
            }
        }
    </script>
</head>
<body>
<c:import url="common/header.jsp"></c:import>

<div class="layui-fluid">
    <div class="layui-card" style="margin-left: 200px;margin-right: 200px">
        <div class="layui-card-body">
            <div class="layui-row">
                <table class="layui-table layui-form">
                    <c:if test="${empty cartItemSet}">
                        <tr>
                            <td colspan="8" align="center" style="font-size: 20px;"><i class="layui-icon-tips layui-icon"></i>
                                您的购物车空空如也，去<a href="${contextPath}" style="color: #FFB800;">购物</a>吧
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty cartItemSet}">
                        <thead>
                        <tr>
                                <%--                            <th width="20"><input type="checkbox" name="" lay-skin="primary"></th>--%>
                            <th width="25%" style="text-align: center">商品名称</th>
                            <th width="15%" style="text-align: center">商品缩略图</th>
                            <th width="15%" style="text-align: center">商品单价</th>
                            <th width="20%" style="text-align: center">商品数量</th>
                            <th width="15%" style="text-align: center">商品小计</th>
                            <th width="10%" style="text-align: center">操作</th>
                        </thead>
                        <tbody>

                        <c:forEach items="${cartItemSet}" var="orderItemBean">
                            <tr>
                                    <%--                                <td><input type="checkbox" name="" lay-skin="primary"></td>--%>
                                <td>${orderItemBean.itemName}</td>
                                <td align="center"><img src="${contextPath}/upload/${orderItemBean.imgName}"></td>
                                <td align="right">￥<fmt:formatNumber value="${orderItemBean.itemPrice}" pattern="#,###.00"></fmt:formatNumber></td>
                                <td align="center">
                                    <div>
                                        <a href="javascript:;" class="layui-btn layui-btn-xs" style="width: 30px;height: 30px" onclick="decCartItemCount(this,${orderItemBean.itemId})">
                                            <i class="layui-icon">-</i>
                                        </a>
                                        <input type="text" class="layui-input layui-input-inline" style="text-align: center;height: 30px;width: 40px;padding: 0px"
                                               onblur="updateCartItemCount(this,${orderItemBean.itemId},${orderItemBean.itemCount})"
                                               onkeydown="keyDownEvent(event,this,${orderItemBean.itemId},${orderItemBean.itemCount})"
                                               value="${orderItemBean.itemCount}">
                                        <a href="javascript:;" class="layui-btn layui-btn-xs" style="width: 30px;height: 30px" onclick="incCartItemCount(this,${orderItemBean.itemId})">
                                            <i class="layui-icon">+</i>
                                        </a>
                                    </div>

                                </td>
                                <td align="right">￥<fmt:formatNumber value="${orderItemBean.totalPrice}" pattern="#,##0.00"></fmt:formatNumber></td>
                                <td class="td-manage" align="center">
                                    <a class="layui-btn-danger layui-btn layui-btn-xs" onclick="deleteCartItem(${orderItemBean.itemId})" href="javascript:;">
                                        <i class="layui-icon layui-icon-delete"></i>删除
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td colspan="6" align="right">
                                <span style="margin-left: 50px">商品总个数：${cartItemCount}</span>
                                <span style="margin-left: 50px">商品种类总数：${typeSet.size()}</span>
                                <span style="margin-left: 50px">商品总价：￥${cartTotalPrice}</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="6" align="right">
                                <a href="${contextPath}" class="layui-btn layui-btn-normal">
                                    <i class="layui-icon layui-icon-return"></i>继续购物
                                </a>
                                <a href="javascript:;" class="layui-btn layui-btn-danger" onclick="clearCart()">
                                    <i class="layui-icon layui-icon-delete"></i>清空购物车
                                </a>
                                <a href="${contextPath}/order" class="layui-btn layui-btn-warm">
                                    结算并下订单<i class="layui-icon layui-icon-triangle-r"></i>
                                </a>
                            </td>
                        </tr>
                        </tbody>
                    </c:if>
                </table>
            </div>
        </div>
    </div>
</div>
</body>

</html>