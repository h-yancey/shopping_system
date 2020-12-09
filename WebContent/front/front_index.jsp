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
        function searchByType(typeId) {
            var url = "${contextPath}/servlet/FrontItemServlet?task=list&itemType=" + typeId;
            $("#body_iframe").attr("src", url);
        }
    </script>
    <style>
        .left-nav::-webkit-scrollbar { /*隐藏滚轮*/
            display: none;
        }
    </style>

</head>

<body>
<!-- 顶部开始 -->
<div class="container">
    <div class="logo">
        <a href="./index.html">购 物 商 城</a></div>

    <ul class="layui-nav left" lay-filter="">
        <li class="layui-nav-item">
            <a href="javascript:;"><i class="layui-icon layui-icon-home">&nbsp;首页</i></a>
        </li>
        <li class="layui-nav-item">
            <a href="javascript:;"><i class="layui-icon layui-icon-user">&nbsp;会员中心</i></a>
        </li>
        <li class="layui-nav-item">
            <a href="javascript:;"><i class="layui-icon layui-icon-cart">&nbsp;购物车</i></a>
        </li>
    </ul>
    <ul class="layui-nav right" lay-filter="">
        <li class="layui-nav-item">
            <a href="javascript:;">登录</a>
        </li>
        <li class="layui-nav-item">
            <a href="/">注册</a>
        </li>
    </ul>
</div>
<!-- 顶部结束 -->

<!-- 左侧菜单开始 -->
<div class="left-nav" style="overflow-y:auto;">
    <div id="side-nav">
        <div class="layui-card">
            <div class="layui-card-body">
                <div style="width: 100%;height: 200px;">
                    <span>购物车 </span>
                    <span>数量：</span>
                    <span>金额：</span>
                </div>
            </div>
        </div>
        <div class="layui-card">
            <div class="layui-card-body">
                <ul class="">
                    <c:forEach items="${typeList}" var="typeBean">
                        <li>
                            <a class="" href="javascript:;" onclick="searchByType(${typeBean.typeId})">${typeBean.typeName}</a>
                            <ul>
                                <c:forEach items="${typeBean.childTypeList}" var="childTypeBean">
                                    <li><a href="javascript:;" onclick="searchByType(${childTypeBean.typeId})">&nbsp;&nbsp;-${childTypeBean.typeName}</a></li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>


        <%--        <div class="layui-side-scroll">--%>
        <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
        <%--        <ul class="layui-nav layui-nav-tree" id="nav">--%>
        <%--            <c:forEach items="${typeList}" var="typeBean">--%>
        <%--                <li class="layui-nav-item layui-nav-itemed">--%>
        <%--                    <a class="" href="javascript:;">${typeBean.typeName}</a>--%>
        <%--                    <dl class="layui-nav-child">--%>
        <%--                        <c:forEach items="${typeBean.childTypeList}" var="childTypeBean">--%>
        <%--                            <dd><a href="javascript:;">${childTypeBean.typeName}</a></dd>--%>
        <%--                        </c:forEach>--%>
        <%--                    </dl>--%>
        <%--                </li>--%>
        <%--            </c:forEach>--%>
        <%--        </ul>--%>
        <%--        </div>--%>
    </div>
</div>
<!-- 左侧菜单结束 -->

<div class="page-content">
    <iframe src="${contextPath}/servlet/FrontItemServlet?task=list" frameborder="0" scrolling="yes" id="body_iframe" width="100%" height="100%"></iframe>
</div>

<!-- 中部开始 -->


<!-- 右侧主体开始 -->


<!-- 右侧主体结束 -->
<!-- 中部结束 -->
</body>


</html>