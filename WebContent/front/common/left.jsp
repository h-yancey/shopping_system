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

        var cateIds = [];

        function getCateId(cateId) {
            $("ul li[fid=" + cateId + "]").each(function (index, el) {
                id = $(el).attr('cate-id');
                cateIds.push(id);
                getCateId(id);
            });
        }


        $(function () {
            $("ul.x-cate li[fid!='0']").hide();
            // 栏目多级显示效果
            $('.x-show').click(function () {
                if ($(this).attr('status') == 'true') {
                    $(this).html('&#xe625;');
                    $(this).attr('status', 'false');
                    cateId = $(this).parents('li').attr('cate-id');
                    $("ul li[fid=" + cateId + "]").show();
                } else {
                    cateIds = [];
                    $(this).html('&#xe623;');
                    $(this).attr('status', 'true');
                    cateId = $(this).parents('li').attr('cate-id');
                    getCateId(cateId);
                    for (var i in cateIds) {
                        $("ul li[cate-id=" + cateIds[i] + "]").hide().find('.x-show').html('&#xe623;').attr('status', 'true');
                    }
                }
            });

            $("ul.x-cate li[fid='0']").mouseover(function () {
                $(this).find('.x-show').html('&#xe625;');
                // $(this).find('.x-show').attr('status', 'false');
                cateId = $(this).attr('cate-id');
                $("ul li[fid=" + cateId + "]").show();
            }).mouseout(function () {
                if ($(this).find('.x-show').attr('status') == 'true') {
                    cateIds = [];
                    $(this).find('.x-show').html('&#xe623;');

                    cateId = $(this).attr('cate-id');
                    getCateId(cateId);
                    for (var i in cateIds) {
                        $("ul li[cate-id=" + cateIds[i] + "]").hide().find('.x-show').html('&#xe623;').attr('status', 'true');
                    }
                }
            })

        })
    </script>
    <style>
        dl.cart-dl {
            text-align: left;
        }

        .cart-dl dt {
            font-size: 23px;
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
                    <dt>购物车</dt>
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
                <ul class="x-cate" style="font-size:13px;">
                    <c:forEach items="${typeList}" var="typeBean">
                        <li cate-id='${typeBean.typeId}' fid='${typeBean.parentId}'>
                            <i class="layui-icon x-show" status='true'>&#xe623;</i>
                            <a href="${contextPath}?itemType=${typeBean.typeId}">
                                    ${typeBean.typeName}
                            </a>
                            <ul>
                                <c:forEach items="${typeBean.childTypeList}" var="childTypeBean">
                                    <li cate-id='${childTypeBean.typeId}' fid='${childTypeBean.parentId}'>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        ├&nbsp;
                                        <a href="${contextPath}?itemType=${childTypeBean.typeId}">
                                                ${childTypeBean.typeName}
                                        </a>
                                    </li>
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
