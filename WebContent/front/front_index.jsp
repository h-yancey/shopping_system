<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>购物商城首页</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="stylesheet" href="${contextPath}/lib/layui/css/layui.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/font.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/xadmin.css">
    <!-- <link rel="stylesheet" href="./css/theme5.css"> -->
    <script src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <!-- <script type="text/javascript" src="./js/xadmin.js"></script> -->
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script>
        layui.use(['form'], function () {
        });

        function submitSearchForm() {
            var priceMin = $("#search_form input[name='priceMin']").val();
            var priceMax = $("#search_form input[name='priceMax']").val();
            if (priceMin != "" && isNaN(priceMin) || priceMax != "" && isNaN(priceMax)) {
                layer.msg("价格区间需为数字", {
                    icon: 0,
                    time: 1000
                });
            } else {
                var url = "${contextPath}?"
                var formData = $("#search_form").serializeArray();
                $.each(formData, function () {
                    if (this.value != "") {
                        url += this.name + "=" + this.value + "&";
                    }
                });

                //$("#search_form").submit();
                window.location.href = url.substring(0, url.length - 1);
            }
        }

        function clearSearchForm() {
            $("#search_form input").val("");
            $("#search_form select").val("");
            window.location.href = "${contextPath}";
        }

    </script>
    <style>
        .left-nav::-webkit-scrollbar { /*隐藏滚轮*/
            display: none;
        }

        div.item {
            height: 320px;
            width: 100%;
        }

        div.item-pic {
            margin: 0px auto;
            width: 230px;
            height: 230px;
        }

        img.item-img {
            width: 100%;
            height: 100%;
        }

        /*删格化5等份*/
        .layui-col-lg2-4, .layui-col-md2-4, .layui-col-sm2-4, .layui-col-xs2-4 {
            position: relative;
            display: block;
            box-sizing: border-box
        }

        .layui-col-xs2-4 {
            float: left
        }

        .layui-col-xs2-4 {
            width: 19.9999992%
        }

        .layui-col-xs-offset2-4 {
            margin-left: 19.9999992%
        }

        @media screen and (min-width: 768px) {
            .layui-col-sm2-4 {
                float: left
            }

            .layui-col-sm2-4 {
                width: 19.9999992%
            }

            .layui-col-sm-offset2-4 {
                margin-left: 19.9999992%
            }
        }

        @media screen and (min-width: 992px) {
            .layui-col-md2-4 {
                float: left
            }

            .layui-col-md2-4 {
                width: 19.9999992%
            }

            .layui-col-md-offset2-4 {
                margin-left: 19.9999992%
            }
        }

        @media screen and (min-width: 1200px) {
            .layui-col-lg2-4 {
                float: left
            }

            .layui-col-lg2-4 {
                width: 19.9999992%
            }

            .layui-col-lg-offset2-4 {
                margin-left: 19.9999992%
            }
        }
    </style>
</head>

<body>
<!-- 顶部开始 -->
<c:import url="common/header.jsp"></c:import>
<!-- 顶部结束 -->

<!-- 左侧菜单开始 -->
<c:import url="common/left.jsp"></c:import>
<!-- 左侧菜单结束 -->

<!-- 内容主体区域 -->
<div class="page-content" style="overflow-y:auto;">
    <div class="layui-fluid">
        <div class="layui-card">
            <div class="layui-card-body">
                <div class="layui-row layui-col-space15">
                    <div class="layui-col-md12">
                        <div class="layui-card">
                            <div class="layui-card-body ">
                                <form id="search_form" class="layui-form layui-col-space5" autocomplete="off">
                                    <div class="layui-input-inline layui-show-xs-block">
                                        <select name="itemType">
                                            <option value="">请选择分类</option>
                                            <c:forEach items="${typeList}" var="typeBean">
                                                <option value="${typeBean.typeId}" ${paramMap.itemTypeId == typeBean.typeId?"selected":""}>${typeBean.typeName}</option>
                                                <c:forEach items="${typeBean.childTypeList}" var="childTypeBean">
                                                    <option value="${childTypeBean.typeId}"
                                                        ${paramMap.itemTypeId == childTypeBean.typeId?"selected":""}>&nbsp;&nbsp;&nbsp;&nbsp;${childTypeBean.typeName}
                                                    </option>
                                                </c:forEach>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="layui-input-inline layui-show-xs-block">
                                        <input class="layui-input" placeholder="请输入关键字" name="keyword" value="${paramMap.keyword}">
                                    </div>

                                    <div class="layui-input-inline" style="width: 100px;">
                                        <input type="text" name="priceMin" placeholder="￥" autocomplete="off" class="layui-input" value="${paramMap.priceMin}">
                                    </div>
                                    <div class="layui-input-inline">-</div>
                                    <div class="layui-input-inline" style="width: 100px;">
                                        <input type="text" name="priceMax" placeholder="￥" autocomplete="off" class="layui-input" value="${paramMap.priceMax}">
                                    </div>
                                    <div class="layui-input-inline layui-show-xs-block">
                                        <button type="button" class="layui-btn" onclick="submitSearchForm()">
                                            <i class="layui-icon">&#xe615;</i></button>
                                        <button type="button" class="layui-btn" onclick="clearSearchForm()">清空条件</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <c:set var="row" value="2" scope="page"></c:set>
                <c:set var="col" value="5" scope="page"></c:set>
                <c:forEach var="i" begin="0" end="${row-1}">
                    <div class="layui-row layui-col-space25">
                        <c:forEach var="j" begin="0" end="${col-1}">
                            <c:if test="${i*col+j < itemList.size()}">
                                <c:set var="itemBean" value="${itemList.get(i*col+j)}" scope="page"></c:set>
                                <div class="layui-col-md2-4">
                                    <div class="item">
                                        <div class="item-pic">
                                            <img src="${contextPath}/upload/${itemBean.imgName}" class="item-img">
                                        </div>
                                        <div style="border: 1px solid #C9C9C9;">
                                            <div style="margin: 2px 0px 2px 2px;color: red;font-size: 18px">
                                                ￥<strong>${itemBean.itemPrice}</strong>
                                            </div>
                                            <div style="margin: 2px 0px 2px 2px">
                                                    ${itemBean.itemName}
                                            </div>

                                            <div style="height:40px;text-align: center">
                                                <a href="${contextPath}?task=info&itemId=${itemBean.itemId}" class="layui-btn layui-btn-normal" style="width: 70px">详细信息</a>
                                                <c:if test="${itemBean.shortageTag == '否'}">
                                                    <a href="javascript:;" class="layui-btn layui-btn-danger" onclick="addCartItem(${itemBean.itemId})" style="width: 70px">
                                                        <i class="layui-icon layui-icon-cart-simple"></i>购买
                                                    </a>
                                                </c:if>
                                                <c:if test="${itemBean.shortageTag == '是'}">
                                                    <a class="layui-btn layui-btn-disabled layui-disabled" style="width: 70px" title="缺货" disabled>
                                                        <i class="layui-icon layui-icon-cart-simple"></i>购买
                                                    </a>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:forEach>

            </div>
            <div class="layui-card-body ">
                <div class="page">
                    ${pageTool}
                </div>
            </div>
        </div>

    </div>
</div>
<!-- 中部开始 -->


<!-- 右侧主体开始 -->


<!-- 右侧主体结束 -->
<!-- 中部结束 -->
</body>


</html>