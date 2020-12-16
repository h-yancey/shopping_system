<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<head>
    <title>Title</title>
    <script>
        function addCartItem(itemId) {
            var url = "${contextPath}/cart?task=add";
            $.post(url, {itemId: itemId}, function (jsonData) {
                layer.msg("成功添加到购物车", {icon: 1, time: 1000}, function () {
                    var cartItemCount = jsonData.cartItemCount;
                    var typeCount = jsonData.typeCount;
                    var cartTotalPrice = jsonData.cartTotalPrice;
                    $("#cartItemCount").html(cartItemCount);
                    $("#typeCount").html(typeCount);
                    $("#cartTotalPrice").html(cartTotalPrice);
                });
            }, "json");
        }
    </script>
    <style>
        dl.cart-dl {
            text-align: left;
        }

        .cart-dl dt {
            font-size: 28px;
            font-weight: bold;
            line-height: 50px;
            color: #009688;
        }

        .cart-dl dd {
            font-size: 13px;
            line-height: 25px;
        }
    </style>
</head>
<body>
<div class="left-nav" style="overflow-y:auto;">
    <div class="layui-card">
        <div class="layui-card-body">
            <div class="layui-show">
                <dl class="cart-dl">
                    <dt> 购物车</dt>
                    <dd> 商品总个数：<span id="cartItemCount">${empty cartItemCount?0:cartItemCount}</span></dd>
                    <dd> 商品种类总数：<span id="typeCount">${empty typeSet?0:typeSet.size()}</span></dd>
                    <dd> 总金额：￥<span id="cartTotalPrice"><fmt:formatNumber value="${empty cartTotalPrice?0:cartTotalPrice}" pattern="#,##0.00"></fmt:formatNumber></span></dd>
                </dl>
            </div>
        </div>
    </div>
    <div class="layui-card">
        <div class="layui-card-body">
            <div id="side-nav">

                <ul>
                    <c:forEach items="${typeList}" var="typeBean">
                        <li>
                            <a class="" href="${contextPath}?itemType=${typeBean.typeId}">${typeBean.typeName}</a>
                            <ul>
                                <c:forEach items="${typeBean.childTypeList}" var="childTypeBean">
                                    <li><a href="${contextPath}?itemType=${childTypeBean.typeId}">&nbsp;&nbsp;-${childTypeBean.typeName}</a></li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</div>
</body>
</html>
