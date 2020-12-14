<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>购物商城后台管理</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/font.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/xadmin.css">
    <!-- <link rel="stylesheet" href="./css/theme5.css"> -->
    <script src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>

    <script src="https://cdn.staticfile.org/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script>
        // 是否开启刷新记忆tab功能
        //var is_remember = false;

        layui.use('element', function () {
            var $ = layui.jquery;
            var element = layui.element;//Tab的切换功能，切换事件监听等，需要依赖element模块
            var active = {
                //在这里给active绑定事件，后面可通过active调用这些事件
                tabDeleteAll: function (ids) {//删除所有
                    $.each(ids, function (i, item) {
                        element.tabDelete("xbs_tab", item);//ids是一个数组，里面存放了多个id，调用tabDelete方法分别删除
                    })
                }
            };

            $(".close-all").click(function () {
                if ($(this).attr("data-type") == "closeall") {
                    // $("#nav li").removeClass("open");
                    // $("#nav a").removeClass("active");
                    // $("ul.sub-menu").css("display", "none");

                    var tabtitle = $(".layui-tab-title li");
                    var ids = new Array();
                    $.each(tabtitle, function (i) {
                        ids[i] = $(this).attr("lay-id");
                    });
                    active.tabDeleteAll(ids);
                }
            });

        });

        function logout() {
            var logoutUrl = "${contextPath}/servlet/LoginServlet?task=logout";
            $.get(logoutUrl, function () {
                layer.msg("您已经安全退出", {icon: 6, time: 1000}, function () {
                    var loginUrl = "${contextPath}/admin_login.jsp";
                    window.location.href = loginUrl;
                });
            });
        }

    </script>
</head>
<body class="index">
<!-- 顶部开始 -->
<div class="container">
    <div class="logo">
        <a href="javascript:location.reload();">后台管理</a></div>
    <div class="left_open">
        <a><i title="展开左侧栏" class="iconfont">&#xe699;</i></a>
    </div>
<%--    <ul class="layui-nav left fast-add">--%>
<%--        <li class="layui-nav-item">--%>
<%--            <a href="javascript:;">+新增</a>--%>
<%--            <dl class="layui-nav-child">--%>
<%--                <!-- 二级菜单 -->--%>
<%--                <dd>--%>
<%--                    <a onclick="xadmin.open('最大化','http://www.baidu.com','','',true)">--%>
<%--                        <i class="iconfont">&#xe6a2;</i>弹出最大化</a>--%>
<%--                </dd>--%>
<%--                <dd>--%>
<%--                    <a onclick="xadmin.open('弹出自动宽高','http://www.baidu.com')">--%>
<%--                        <i class="iconfont">&#xe6a8;</i>弹出自动宽高</a>--%>
<%--                </dd>--%>
<%--                <dd>--%>
<%--                    <a onclick="xadmin.open('弹出指定宽高','http://www.baidu.com',500,300)">--%>
<%--                        <i class="iconfont">&#xe6a8;</i>弹出指定宽高</a>--%>
<%--                </dd>--%>
<%--                <dd>--%>
<%--                    <a onclick="xadmin.add_tab('在tab打开','member-list.html')">--%>
<%--                        <i class="iconfont">&#xe6b8;</i>在tab打开</a>--%>
<%--                </dd>--%>
<%--                <dd>--%>
<%--                    <a onclick="xadmin.add_tab('在tab打开刷新','member-del.html',true)">--%>
<%--                        <i class="iconfont">&#xe6b8;</i>在tab打开刷新</a>--%>
<%--                </dd>--%>
<%--            </dl>--%>
<%--        </li>--%>
<%--    </ul>--%>
    <ul class="layui-nav right" lay-filter="">
        <li class="layui-nav-item">
            <a href="javascript:;">${userBean.username}</a>
            <dl class="layui-nav-child">
                <!-- 二级菜单 -->
                <dd><a onclick="xadmin.open('个人资料','${contextPath}/servlet/ProfileServlet?task=editProfile')">个人资料</a></dd>
                <dd><a onclick="xadmin.open('密码修改','${contextPath}/servlet/ProfileServlet?task=editPwd')">密码修改</a></dd>
                <dd class="close-all" data-type="closeall"><a href="javascript:;" onclick="logout()">退出</a></dd>
            </dl>
        </li>
        <li class="layui-nav-item to-index">
            <a href="${contextPath}">前台首页</a>
        </li>
    </ul>
</div>
<!-- 顶部结束 -->
<!-- 中部开始 -->
<!-- 左侧菜单开始 -->
<div class="left-nav">
    <div id="side-nav">
        <ul id="nav">
            <li>
                <a href="javascript:;">
                    <i class="iconfont left-nav-li" lay-tips="商品管理">&#xe6f6;</i>
                    <cite>商品管理</cite>
                    <i class="iconfont nav_right">&#xe697;</i></a>
                <ul class="sub-menu">
                    <li>
                        <a onclick="xadmin.add_tab('商品类别管理','${contextPath}/servlet/TypeServlet?task=list')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>商品类别管理</cite></a>
                    </li>
                    <li>
                        <a onclick="xadmin.add_tab('商品信息管理','${contextPath}/servlet/ItemServlet?task=list')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>商品信息管理</cite></a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="javascript:;">
                    <i class="iconfont left-nav-li" lay-tips="用户管理">&#xe6b8;</i>
                    <cite>用户管理</cite>
                    <i class="iconfont nav_right">&#xe697;</i></a>
                <ul class="sub-menu">
                    <li>
                        <a onclick="xadmin.add_tab('注册用户管理','${contextPath}/servlet/UserServlet?task=userList')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>注册用户管理</cite>
                        </a>
                    </li>
                    <li>
                        <a onclick="xadmin.add_tab('管理员管理','${contextPath}/servlet/UserServlet?task=adminList')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>管理员管理</cite>
                        </a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="javascript:;">
                    <i class="iconfont left-nav-li" lay-tips="订单管理">&#xe723;</i>
                    <cite>订单管理</cite>
                    <i class="iconfont nav_right">&#xe697;</i></a>
                <ul class="sub-menu">
                    <li>
                        <a onclick="xadmin.add_tab('订单列表','order-list.html')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>订单列表</cite></a>
                    </li>
                    <li>
                        <a onclick="xadmin.add_tab('订单列表1','order-list1.html')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>订单列表1</cite></a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="javascript:;">
                    <i class="iconfont left-nav-li" lay-tips="个人资料管理">&#xe726;</i>
                    <cite>个人资料管理</cite>
                    <i class="iconfont nav_right">&#xe697;</i></a>
                <ul class="sub-menu">
                    <li>
                        <a onclick="xadmin.add_tab('个人资料','${contextPath}/servlet/ProfileServlet?task=editProfile')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>个人资料</cite></a>
                    </li>
                    <li>
                        <a onclick="xadmin.add_tab('密码修改','${contextPath}/servlet/ProfileServlet?task=editPwd')">
                            <i class="iconfont">&#xe6a7;</i>
                            <cite>密码修改</cite></a>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</div>
<!-- <div class="x-slide_left"></div> -->
<!-- 左侧菜单结束 -->
<!-- 右侧主体开始 -->
<div class="page-content">
    <div class="layui-tab tab" lay-filter="xbs_tab" lay-allowclose="false">
        <ul class="layui-tab-title">
            <li class="home">
                <i class="layui-icon">&#xe68e;</i>我的桌面
            </li>
        </ul>
        <div class="layui-unselect layui-form-select layui-form-selected" id="tab_right">
            <dl>
                <dd data-type="this">关闭当前</dd>
                <dd data-type="other">关闭其它</dd>
                <dd data-type="all">关闭全部</dd>
            </dl>
        </div>
        <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">
                <iframe src='${contextPath}/admin/welcome.html' frameborder="0" scrolling="yes" class="x-iframe"></iframe>
            </div>
        </div>
        <div id="tab_show"></div>
    </div>
</div>
<div class="page-content-bg"></div>
<style id="theme_style"></style>
<!-- 右侧主体结束 -->
<!-- 中部结束 -->
</body>

</html>