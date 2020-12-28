<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>商品信息管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" href="${contextPath}/css/font.css">
    <link rel="stylesheet" href="${contextPath}/css/xadmin.css">
    <script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>
    <script src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script>
        layui.use(['form']);

        function deleteItem(itemId, imgName) {
            layer.confirm("是否删除该商品？", {
                btn: ['是', '否']
            }, function () {
                var deleteUrl = "${contextPath}/servlet/ItemServlet?task=delete";
                var data = {
                    itemId: itemId,
                    imgName: imgName
                }
                $.post(deleteUrl, data, function (jsonData) {
                    var flag = jsonData.flag;
                    var message = jsonData.message;
                    if (flag) {
                        layer.alert('已删除!', {
                                icon: 1,
                                btn1: function () {
                                    window.location.reload();
                                }
                            }
                        );
                    } else {
                        layer.alert("删除失败，原因：" + message, {
                            icon: 2
                        });
                    }
                }, "json")
            })
        }

        function submitSearchForm() {
            var priceMin = $("#search_form input[name='priceMin']").val();
            var priceMax = $("#search_form input[name='priceMax']").val();
            if (priceMin != "" && isNaN(priceMin) || priceMax != "" && isNaN(priceMax)) {
                layer.msg("价格区间需为数字", {icon: 0, time: 1000});
            } else {
                $("#search_form").submit();
            }
        }

        function clearSearchForm() {
            $("#search_form input").val("");
            $("#search_form select").val("");
            $("#search_form").submit();
        }
    </script>
</head>

<body>
<%--<div class="x-nav">--%>
<%--            <span class="layui-breadcrumb">--%>
<%--                <a href="">首页</a>--%>
<%--                <a href="">演示</a>--%>
<%--                <a>--%>
<%--                    <cite>导航元素</cite></a>--%>
<%--            </span>--%>
<%--    <a class="layui-btn layui-btn-small" style="line-height:1.6em;margin-top:3px;float:right"--%>
<%--       onclick="location.reload()" title="刷新">--%>
<%--        <i class="layui-icon layui-icon-refresh" style="line-height:30px"></i>--%>
<%--    </a>--%>
<%--</div>--%>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body ">
                    <form id="search_form" class="layui-form layui-col-space5" action="${contextPath}/servlet/ItemServlet?task=list" method="post"
                          autocomplete="off">
                        <div class="layui-input-inline layui-show-xs-block">
                            <select name="itemType">
                                <option value="">请选择分类</option>
                                <c:forEach items="${typeList}" var="typeBean">
                                    <option value="${typeBean.typeId}" ${paramMap.itemTypeId == typeBean.typeId?"selected":""}>${typeBean.typeName}</option>
                                    <c:forEach items="${typeBean.childTypeList}" var="childTypeBean">
                                        <option value="${childTypeBean.typeId}" ${paramMap.itemTypeId == childTypeBean.typeId?"selected":""}>&nbsp;&nbsp;&nbsp;&nbsp;${childTypeBean.typeName}</option>
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
                            <button type="button" class="layui-btn" onclick="submitSearchForm()"><i class="layui-icon">&#xe615;</i></button>
                            <button type="button" class="layui-btn" onclick="clearSearchForm()">清空条件</button>
                        </div>
                        <div class="layui-input-inline layui-show-xs-block" style="float:right;">
                            <%--                                                <button class="layui-btn layui-btn-danger" onclick="delAll()">--%>
                            <%--                                                    <i class="layui-icon"></i>批量删除--%>
                            <%--                                                </button>--%>
                            <a class="layui-btn" onclick="xadmin.open('增加商品','${contextPath}/servlet/ItemServlet?task=add')">
                                <i class="layui-icon"></i>增加商品
                            </a>
                            <a class="layui-btn layui-btn-small" style="line-height:1.6em;float:right"
                               onclick="location.reload()" title="刷新">
                                <i class="layui-icon layui-icon-refresh" style="line-height:30px"></i>
                            </a>
                        </div>
                    </form>
                    <hr>
                </div>
                <div class="layui-card-body">
                    <table class="layui-table layui-form" width="100%">
                        <thead>
                        <tr>
                            <%--                            <th width="20"><input type="checkbox" name="" lay-skin="primary"></th>--%>
                            <th width="2.5%">商品编号</th>
                            <th width="20%">商品名称</th>
                            <th width="25%">商品描述</th>
                            <th width="5%">商品单价</th>
                            <th width="20%">商品图片</th>
                            <th width="5%">商品类别</th>
                            <th width="2.5%">是否缺货</th>
                            <th width="20%">操作</th>
                        </thead>
                        <tbody class="x-cate">
                        <c:if test="${empty itemList}">
                            <tr>
                                <td colspan="8" align="center" style="font-size: 20px; color: #FFB800;"><i class="layui-icon-tips layui-icon"></i>
                                    查无商品
                                </td>
                            </tr>
                        </c:if>
                        <c:forEach items="${itemList}" var="itemBean">
                            <tr>
                                    <%--                                <td><input type="checkbox" name="" lay-skin="primary"></td>--%>
                                <td>${itemBean.itemId}</td>
                                <td>${itemBean.itemName} </td>
                                <td>${itemBean.itemDesc}</td>
                                <td align="right"><fmt:formatNumber value="${itemBean.itemPrice}" pattern="#,###.00"></fmt:formatNumber></td>
                                <td><img src="${contextPath}/upload/${itemBean.imgName}"></td>
                                <td>${itemBean.bigTypeName}/${itemBean.smallTypeName}</td>
                                <td>${itemBean.shortageTag}</td>
                                <td class="td-manage">
                                    <button class="layui-btn layui-btn layui-btn-xs"
                                            onclick="xadmin.open('编辑商品','${contextPath}/servlet/ItemServlet?task=edit&itemId=${itemBean.itemId}')">
                                        <i class="layui-icon">&#xe642;</i>编辑
                                    </button>
                                    <button class="layui-btn-danger layui-btn layui-btn-xs"
                                            onclick="deleteItem(${itemBean.itemId},'${itemBean.imgName}')"
                                            href="javascript:;"><i class="layui-icon">&#xe640;</i>删除
                                    </button>
                                </td>
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