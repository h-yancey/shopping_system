<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html class="x-admin-sm">

<head>
    <meta charset="UTF-8">
    <title>商品类别管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" href="${contextPath}/css/font.css">
    <link rel="stylesheet" href="${contextPath}/css/xadmin.css">
    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <script src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script>
        layui.use(['form']);


        // 分类展开收起的分类的逻辑
        $(function () {
            $("tbody.x-cate tr[fid!='0']").hide();
            // 栏目多级显示效果
            $('.x-show').click(function () {
                if ($(this).attr('status') == 'true') {
                    $(this).html('&#xe625;');
                    $(this).attr('status', 'false');
                    cateId = $(this).parents('tr').attr('cate-id');
                    $("tbody tr[fid=" + cateId + "]").show();
                } else {
                    cateIds = [];
                    $(this).html('&#xe623;');
                    $(this).attr('status', 'true');
                    cateId = $(this).parents('tr').attr('cate-id');
                    getCateId(cateId);
                    for (var i in cateIds) {
                        $("tbody tr[cate-id=" + cateIds[i] + "]").hide().find('.x-show').html('&#xe623;').attr('status', 'true');
                    }
                }
            });

            $("tbody.x-cate tr[fid='0']").mouseover(function () {
                $(this).find('.x-show').html('&#xe625;');
                // $(this).find('.x-show').attr('status', 'false');
                cateId = $(this).attr('cate-id');
                $("tbody tr[fid=" + cateId + "]").show();
            }).mouseout(function () {
                if ($(this).find('.x-show').attr('status') == 'true') {
                    cateIds = [];
                    $(this).find('.x-show').html('&#xe623;');

                    cateId = $(this).attr('cate-id');
                    getCateId(cateId);
                    for (var i in cateIds) {
                        $("tbody tr[cate-id=" + cateIds[i] + "]").hide().find('.x-show').html('&#xe623;').attr('status', 'true');
                    }
                }
            })

        })

        var cateIds = [];

        function getCateId(cateId) {
            $("tbody tr[fid=" + cateId + "]").each(function (index, el) {
                id = $(el).attr('cate-id');
                cateIds.push(id);
                getCateId(id);
            });
        }

        function deleteType(typeId, parentId) {
            layer.confirm("是否删除该类别？", {btn: ['是', '否']}, function () {
                var deleteUrl = "${contextPath}/servlet/TypeServlet?task=delete";
                var data = {
                    typeId: typeId,
                    parentId: parentId
                }
                $.post(deleteUrl, data, function (jsonData) {
                    var flag = jsonData.flag;
                    var message = jsonData.message;
                    if (flag) {
                        layer.msg('已删除', {icon: 1, time: 1000,}, function () {
                            window.location.reload();
                        });
                    } else {
                        layer.alert("删除失败，原因：" + message, {icon: 2});
                    }
                }, "json")
            });
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
<%--                    <div class="layui-input-inline layui-show-xs-block">--%>
<%--                        <button class="layui-btn layui-btn-danger" onclick="delAll()">--%>
<%--                            <i class="layui-icon"></i>批量删除--%>
<%--                        </button>--%>
<%--                    </div>--%>
                    <div class="layui-input-inline layui-show-xs-block" style="float:right">
                        <a class="layui-btn" onclick="xadmin.open('增加分类','${contextPath}/servlet/TypeServlet?task=add')">
                            <i class="layui-icon"></i>增加分类
                        </a>
                        <a class="layui-btn layui-btn-small" style="line-height:1.6em;float:right"
                           onclick="location.reload()" title="刷新">
                            <i class="layui-icon layui-icon-refresh" style="line-height:30px"></i>
                        </a>
                    </div>
                    <hr>
                </div>

                <div class="layui-card-body">
                    <table class="layui-table layui-form">
                        <thead>
                        <tr>
                            <th width="10%"><input type="checkbox" name="" lay-skin="primary"></th>
                            <th width="20%">类别编号</th>
                            <th width="25%">类别名称</th>
                            <th width="20%">父类编号</th>
                            <th width="25%">操作</th>
                        </thead>
                        <tbody class="x-cate">

                        <c:forEach items="${requestScope.typeList}" var="typeBean">
                            <tr cate-id='${typeBean.typeId}' fid='${typeBean.parentId}'>
<%--                            <td><input type="checkbox" name="" lay-skin="primary"></td>--%>
                            <td>${typeBean.typeId}</td>
                            <td>
                                <i class="layui-icon x-show" status='true'>&#xe623;</i>${typeBean.typeName}
                            </td>
                            <td>${typeBean.parentId}</td>
                            <%--                                <td><input type="text" class="layui-input x-sort" name="order" value="1"></td>--%>
                            <%--                                <td>--%>
                            <%--                                    <input type="checkbox" name="switch"  lay-text="开启|停用"  checked="" lay-skin="switch">--%>
                            <%--                                </td>--%>
                            <td class="td-manage">
                                <button class="layui-btn layui-btn layui-btn-xs"
                                        onclick="xadmin.open('编辑类别','${contextPath}/servlet/TypeServlet?task=edit&typeId=${typeBean.typeId}')">
                                    <i class="layui-icon">&#xe642;</i>编辑
                                </button>
                                    <%--                                    <button class="layui-btn layui-btn-warm layui-btn-xs"  onclick="xadmin.open('编辑','admin-edit.html')" ><i class="layui-icon">&#xe642;</i>添加子栏目</button>--%>
                                <button class="layui-btn-danger layui-btn layui-btn-xs"
                                        onclick="deleteType(${typeBean.typeId},${typeBean.parentId})"
                                        href="javascript:;"><i
                                        class="layui-icon">&#xe640;</i>删除
                                </button>
                                    <%--                                    <button class="layui-btn-danger layui-btn layui-btn-xs"  onclick="member_del(this,'要删除的id')" href="javascript:;" ><i class="layui-icon">&#xe640;</i>删除</button>--%>
                            </td>


                            <c:forEach items="${typeBean.childTypeList}" var="childTypeBean">
                                <tr cate-id='${childTypeBean.typeId}' fid='${childTypeBean.parentId}'>
<%--                                    <td>--%>
<%--                                        <input type="checkbox" name="" lay-skin="primary">--%>
<%--                                    </td>--%>
                                    <td>${childTypeBean.typeId}</td>
                                    <td>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        ├&nbsp;${childTypeBean.typeName}
                                    </td>
                                    <td>${childTypeBean.parentId}</td>
                                    <td class="td-manage">
                                        <button class="layui-btn layui-btn layui-btn-xs"
                                                onclick="xadmin.open('编辑类别','${contextPath}/servlet/TypeServlet?task=edit&typeId=${childTypeBean.typeId}')">
                                            <i class="layui-icon">&#xe642;</i>编辑
                                        </button>
                                        <button class="layui-btn-danger layui-btn layui-btn-xs"
                                                onclick="deleteType(${childTypeBean.typeId},${childTypeBean.parentId})"
                                                href="javascript:;"><i class="layui-icon">&#xe640;</i>删除
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="layui-card-body ">
                    <div class="page" id="page">
                        <%--                        <div>--%>
                        <%--                            <a class="prev" href="">&lt;&lt;</a>--%>
                        <%--                            <a class="num" href="">1</a>--%>
                        <%--                            <span class="current">2</span>--%>
                        <%--                            <a class="num" href="">3</a>--%>
                        <%--                            <a class="num" href="">489</a>--%>
                        <%--                            <a class="next" href="">&gt;&gt;</a>--%>
                        <%--                        </div>--%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
