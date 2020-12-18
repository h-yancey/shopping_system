<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>注册用户管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" href="${contextPath}/css/font.css">
    <link rel="stylesheet" href="${contextPath}/css/xadmin.css">
    <script src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script>
        function submitSearchForm() {
            $("#search_form").submit();
        }

        function clearSearchForm() {
            $("#search_form input").val("");
            $("#search_form select").val("");
            $("#search_form").submit();
        }

        function updateLock(obj, userid) {
            var updateLockUrl = "${contextPath}/servlet/UserServlet?task=updateLock";
            var data = {
                userid: userid,
            }
            if ($(obj).attr('title') == '解冻') {
                layer.confirm('确认要解冻吗？', function () {
                    $.post(updateLockUrl, data, function (jsonData) {
                        var flag = jsonData.flag;
                        var message = jsonData.message;
                        if (flag) {
                            layer.msg('已解冻!', {icon: 1, time: 700}, function () {
                                location.reload();
                            });
                        } else {
                            layer.alert("解冻失败，原因：" + message, {
                                icon: 2
                            });
                        }
                    }, "json")

                    // $(obj).attr('title', '冻结')
                    // $(obj).find('i').html('&#xe601;');
                    // $(obj).parents("tr").find(".td-status").find('span').removeClass('layui-btn-disabled').html('未冻结');
                })

            } else {
                layer.confirm('确认要冻结吗？', function () {
                    $.post(updateLockUrl, data, function (jsonData) {
                        var flag = jsonData.flag;
                        var message = jsonData.message;
                        if (flag) {
                            layer.msg('已冻结!', {icon: 1, time: 700}, function () {
                                location.reload();
                            });
                        } else {
                            layer.alert("冻结失败，原因：" + message, {
                                icon: 2
                            });
                        }
                    }, "json")

                    // $(obj).attr('title', '解冻')
                    // $(obj).find('i').html('&#xe62f;');
                    // $(obj).parents("tr").find(".td-status").find('span').addClass('layui-btn-disabled').html('已冻结');
                })

            }
        }

        layui.use(['form'], function () {
        });
    </script>
</head>
<body>
<%--<div class="x-nav">--%>
<%--          <span class="layui-breadcrumb">--%>
<%--            <a href="">首页</a>--%>
<%--            <a href="">演示</a>--%>
<%--            <a>--%>
<%--              <cite>导航元素</cite></a>--%>
<%--          </span>--%>
<%--    <a class="layui-btn layui-btn-small" style="line-height:1.6em;margin-top:3px;float:right" onclick="location.reload()" title="刷新">--%>
<%--        <i class="layui-icon layui-icon-refresh" style="line-height:30px"></i></a>--%>
<%--</div>--%>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body ">
                    <form id="search_form" class="layui-form layui-col-space5" autocomplete="off" method="post"
                          action="${contextPath}/servlet/UserServlet?task=userList">
                        <div class="layui-inline layui-show-xs-block">
                            <input type="text" name="username" placeholder="请输入用户名" class="layui-input" value="${paramMap.username}">
                        </div>
                        <div class="layui-inline layui-show-xs-block">
                            <select name="sex">
                                <option value="">请选择性别</option>
                                <option value="男" ${paramMap.sex == "男"?"selected":""}>男</option>
                                <option value="女" ${paramMap.sex == "女"?"selected":""}>女</option>
                            </select>
                        </div>
                        <div class="layui-inline layui-show-xs-block">
                            <select name="lockTag">
                                <option value="">请选择冻结状态</option>
                                <option value="0" ${paramMap.lockTag == "0"?"selected":""}>未冻结</option>
                                <option value="1" ${paramMap.lockTag == "1"?"selected":""}>已冻结</option>
                            </select>
                        </div>
                        <div class="layui-inline layui-show-xs-block">
                            <button type="button" class="layui-btn" onclick="submitSearchForm()"><i class="layui-icon">&#xe615;</i></button>
                            <button type="button" class="layui-btn" onclick="clearSearchForm()">清空条件</button>
                        </div>
                        <a class="layui-btn layui-btn-small" style="line-height:1.6em;float:right" onclick="location.reload()"
                           title="刷新">
                            <i class="layui-icon layui-icon-refresh" style="line-height:30px"></i>
                        </a>
                    </form>
                    <hr>
                </div>
                <%--                <div class="layui-card-header">--%>
                <%--                    <button class="layui-btn layui-btn-danger" onclick="delAll()"><i class="layui-icon"></i>批量删除</button>--%>
                <%--                    <button class="layui-btn" onclick="xadmin.open('添加用户','./member-add.html',600,400)"><i class="layui-icon"></i>添加</button>--%>
                <%--                </div>--%>
                <div class="layui-card-body layui-table-body layui-table-main">
                    <table class="layui-table layui-form">
                        <thead>
                        <tr>
                            <%--                            <th>--%>
                            <%--                                <input type="checkbox" lay-filter="checkall" name="" lay-skin="primary">--%>
                            <%--                            </th>--%>
                            <th>ID</th>
                            <th>用户名</th>
                            <th>真实姓名</th>
                            <th>性别</th>
                            <%--                            <th>出生日期</th>--%>
                            <%--                            <th>email</th>--%>
                            <%--                            <th>电话号码</th>--%>
                            <%--                            <th>地址</th>--%>
                            <%--                            <th>邮编</th>--%>
                            <th>注册日期</th>
                            <th>状态</th>
                            <th>最后登录时间</th>
                            <th>登录次数</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty userList}">
                            <tr>
                                <td colspan="9" align="center" style="font-size: 20px; color: #FFB800;"><i class="layui-icon-tips layui-icon"></i>
                                    查无用户
                                </td>
                            </tr>
                        </c:if>
                        <c:forEach items="${userList}" var="userBean">
                            <tr>
                                    <%--                                <td>--%>
                                    <%--                                    <input type="checkbox" name="id" value="1" lay-skin="primary">--%>
                                    <%--                                </td>--%>
                                <td>${userBean.userid}</td>
                                <td>${userBean.username}</td>
                                <td>${userBean.truename}</td>
                                <td>${userBean.sex}</td>
                                    <%--                                <td>${userBean.birth}</td>--%>
                                    <%--                                <td>${userBean.email}</td>--%>
                                    <%--                                <td>${userBean.phone}</td>--%>
                                    <%--                                <td>${userBean.address}</td>--%>
                                    <%--                                <td>${userBean.code}</td>--%>
                                <td>
                                    <fmt:formatDate value="${userBean.regDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                                </td>
                                <td class="td-status">
                                    <span class="layui-btn layui-btn-normal layui-btn-mini ${userBean.lockTag == '0'?'':'layui-btn-disabled'}">
                                            ${userBean.lockTag == "0"?"未冻结":"已冻结"}</span>
                                </td>
                                <td>
                                    <fmt:formatDate value="${userBean.lastDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                                </td>
                                <td>${userBean.loginNum}</td>
                                <td class="td-manage">
                                    <a onclick="updateLock(this,${userBean.userid})" href="javascript:;" title="${userBean.lockTag == '0'?'冻结':'解冻'}">
                                        <i class="layui-icon">${userBean.lockTag == "0"?"&#xe601;":"&#xe62f;"}</i>
                                    </a>
                                        <%--                                    <a title="编辑" onclick="xadmin.open('编辑','member-edit.html',600,400)" href="javascript:;">--%>
                                        <%--                                        <i class="layui-icon">&#xe642;</i>--%>
                                        <%--                                    </a>--%>
                                        <%--                                    <a onclick="xadmin.open('修改密码','member-password.html',600,400)" title="修改密码" href="javascript:;">--%>
                                        <%--                                        <i class="layui-icon">&#xe631;</i>--%>
                                        <%--                                    </a>--%>
                                        <%--                                    <a title="删除" onclick="member_del(this,'要删除的id')" href="javascript:;">--%>
                                        <%--                                        <i class="layui-icon">&#xe640;</i>--%>
                                        <%--                                    </a>--%>
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
<script>
    function delAll(argument) {
        var ids = [];

        // 获取选中的id
        $('tbody input').each(function (index, el) {
            if ($(this).prop('checked')) {
                ids.push($(this).val())
            }
        });

        layer.confirm('确认要删除吗？' + ids.toString(), function (index) {
            //捉到所有被选中的，发异步进行删除
            layer.msg('删除成功', {icon: 1});
            $(".layui-form-checked").not('.header').parents('tr').remove();
        });
    }
</script>
</html>