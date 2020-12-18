<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>我的订单</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/font.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/xadmin.css">
    <script src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${contextPath}/js/My97DatePicker/WdatePicker.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script>
        layui.use(['form', 'layer']);

        function submitSearchForm() {
            var url = "${contextPath}/member?task=myOrder&"
            var formData = $("#search_form").serializeArray();
            $.each(formData, function () {
                if (this.value != "") {
                    url += this.name + "=" + this.value + "&";
                }
            });

            //$("#search_form").submit();
            window.location.href = url.substring(0, url.length - 1);
        }

        function clearSearchForm() {
            window.location.href = "${contextPath}/member?task=myOrder";
        }

        function orderInfo(orderId) {
            var url = "${contextPath}/member?task=orderInfo&orderId=" + orderId;
            xadmin.open('查看订单', url);
            // var data={
            //     orderId:orderId
            // };
            // $.post(url,data,function(){
            //
            // })
        }

    </script>
    <style>
        dl.member-dl {
            text-align: left;
            padding-left: 80px;
        }

        .member-dl dt {
            font-size: 28px;
            font-weight: bold;
            line-height: 50px;
            color: #FFB800;
        }

        .member-dl dd {
            font-size: 13px;
            line-height: 25px;
        }

        .member-dl a:hover {
            color: #FFB800;
        }
    </style>
</head>
<body>
<c:import url="../common/header.jsp"></c:import>

<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">
            <div class="layui-row">
                <div class="layui-col-md2">
                    <div class="layui-show">
                        <dl class="member-dl">
                            <dt>会员中心</dt>
                            <dd>
                                <a href="${contextPath}/member">基本资料</a>
                            </dd>
                            <dd>
                                <a href="${contextPath}/member?task=editPwd">密码修改</a>
                            </dd>
                            <dd>
                                <a href="${contextPath}/member?task=myOrder">我的订单</a>
                            </dd>
                        </dl>
                    </div>
                </div>
                <div class="layui-col-md10" style="padding:10px;border: 1px solid #e6e6e6">
                    <h2>我的订单</h2>
                    <hr>
                    <div class="layui-card" style="height: 500px">
                        <div class="layui-card-body ">
                            <form id="search_form" class="layui-form layui-col-space5" action="${contextPath}/member?task=myOrder" method="post" autocomplete="off">
                                <div class="layui-input-inline layui-show-xs-block">
                                    <input type="text" class="layui-input Wdate" name="orderDate" placeholder="请输入下单日期"
                                           value="${paramMap.orderDate}" onclick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd',firstDayOfWeek: 1})">
                                </div>

                                <div class="layui-input-inline layui-show-xs-block">
                                    <select name="auditStatus">
                                        <option value="">请选择审核状态</option>
                                        <option value="1" ${paramMap.auditStatus == "1" ? "selected":""}>未审核</option>
                                        <option value="2" ${paramMap.auditStatus == "2" ? "selected":""}>通过</option>
                                        <option value="3" ${paramMap.auditStatus == "3" ? "selected":""}>不通过</option>
                                    </select>
                                </div>

                                <div class="layui-input-inline layui-show-xs-block">
                                    <button type="button" class="layui-btn" onclick="submitSearchForm()"><i class="layui-icon">&#xe615;</i></button>
                                    <button type="button" class="layui-btn" onclick="clearSearchForm()">清空条件</button>
                                </div>
                            </form>
                            <hr>
                        </div>
                        <div class="layui-card-body">
                            <table class="layui-table">
                                <thead>
                                <tr>
                                    <%--                            <th width="20"><input type="checkbox" name="" lay-skin="primary"></th>--%>
                                    <th style="text-align: center">订单编号</th>
                                    <th style="text-align: center">下单时间</th>
                                    <th style="text-align: center">订单状况</th>
                                    <th style="text-align: center">订单金额</th>
                                    <th style="text-align: center">商品总数</th>
                                    <th style="text-align: center">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:if test="${empty orderList}">
                                    <tr>
                                        <td colspan="8" align="center" style="font-size: 20px; color: #FFB800;"><i class="layui-icon-tips layui-icon"></i>
                                            查无订单
                                        </td>
                                    </tr>
                                </c:if>
                                <c:forEach items="${orderList}" var="orderBean">
                                    <tr>
                                            <%--                                <td><input type="checkbox" name="" lay-skin="primary"></td>--%>
                                        <td align="center">${orderBean.orderId}</td>
                                        <td align="center"><fmt:formatDate value="${orderBean.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                        <td align="center">
                                                ${orderBean.auditStatus == "1"?"未审核":""}
                                                ${orderBean.auditStatus == "2"?"通过":""}
                                                ${orderBean.auditStatus == "3"?"不通过":""}
                                        </td>
                                        <td align="right">￥${orderBean.totalPrice}</td>
                                        <td align="center">${orderBean.itemSize}</td>
                                        <td class="td-manage" align="center">
                                            <button class="layui-btn layui-btn-normal layui-btn-xs" onclick="orderInfo(${orderBean.orderId})">
                                                <i class="layui-icon layui-icon-list"></i>查看订单
                                            </button>
                                                <%--                                            <button class="layui-btn-danger layui-btn layui-btn-xs"--%>
                                                <%--                                                    onclick=""><i class="layui-icon">&#xe640;</i>删除--%>
                                                <%--                                            </button>--%>
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
    </div>
</div>
</body>

</html>