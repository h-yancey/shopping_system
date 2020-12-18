<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="x-admin-sm">

<head>
    <meta charset="UTF-8">
    <title>欢迎页面-X-admin2.2</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" href="${contextPath}/css/font.css">
    <link rel="stylesheet" href="${contextPath}/css/xadmin.css">
    <script src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <script type="text/javascript" src="${contextPath}/js/My97DatePicker/WdatePicker.js"></script>
    <script>
        layui.use(['form']);

        function submitSearchForm() {
            $("#search_form").submit();
        }

        function clearSearchForm() {
            $("#search_form input").val("");
            $("#search_form select").val("");
            $("#search_form").submit();
        }

    </script>
</head>

<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body">
                    <form id="search_form" class="layui-form layui-col-space5"
                          action="${contextPath}/servlet/OrderServlet?task=list" method="post" autocomplete="off">
                        <div class="layui-input-inline layui-show-xs-block">
                            <input type="text" class="layui-input Wdate" placeholder="开始日" name="startDate"
                                   value="${paramMap.startDate}" onclick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd',firstDayOfWeek: 1})">
                        </div>
                        <div class="layui-input-inline layui-show-xs-block">
                            <input type="text" class="layui-input Wdate" placeholder="截止日" name="endDate"
                                   value="${paramMap.endDate}" onclick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd',firstDayOfWeek: 1})">
                        </div>
                        <div class="layui-input-inline layui-show-xs-block">
                            <input type="text" name="orderUser" placeholder="请输入用户名" value="${paramMap.orderUser}" class="layui-input">
                        </div>
                        <div class="layui-input-inline layui-show-xs-block">
                            <input type="text" name="itemName" placeholder="请输入商品名称" value="${paramMap.itemName}" class="layui-input">
                        </div>
                        <div class="layui-input-inline layui-show-xs-block">
                            <select name="auditStatus">
                                <option value="">订单状态</option>
                                <option value="1" ${paramMap.auditStatus == "1"?"selected":""}>未审核</option>
                                <option value="2" ${paramMap.auditStatus == "2"?"selected":""}>通过</option>
                                <option value="3" ${paramMap.auditStatus == "3"?"selected":""}>不通过</option>
                            </select>
                        </div>
                        <div class="layui-input-inline layui-show-xs-block">
                            <button type="button" class="layui-btn" onclick="submitSearchForm()"><i class="layui-icon">&#xe615;</i></button>
                            <button type="button" class="layui-btn" onclick="clearSearchForm()">清空条件</button>
                        </div>
                        <div class="layui-input-inline layui-show-xs-block" style="float:right;">
                            <a class="layui-btn layui-btn-small" style="line-height:1.6em;float:right"
                               onclick="location.reload()" title="刷新">
                                <i class="layui-icon layui-icon-refresh" style="line-height:30px"></i>
                            </a>
                        </div>
                    </form>
                    <hr>
                </div>

                <div class="layui-card-body ">
                    <table class="layui-table layui-form">
                        <thead>
                        <tr>
                            <%--                            <th width="20"><input type="checkbox" name="" lay-skin="primary"></th>--%>
                            <th style="text-align: center">订单号</th>
                            <th style="text-align: center">订单用户名</th>
                            <th style="text-align: center">下单时间</th>
                            <th style="text-align: center">付款方式</th>
                            <th style="text-align: center">发货方式</th>
                            <th style="text-align: center">商品总个数</th>
                            <th style="text-align: center">订单总金额</th>
                            <th style="text-align: center">订单状态</th>
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
                                <td align="center">${orderBean.orderId}</td>
                                <td align="center">${orderBean.orderUser}</td>
                                <td align="center"><fmt:formatDate value="${orderBean.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                <td align="center">${orderBean.payType}</td>
                                <td align="center">${orderBean.sendType}</td>
                                <td align="center">${orderBean.itemSize}</td>
                                <td align="right">￥${orderBean.totalPrice}</td>
                                <td align="center">
                                        ${orderBean.auditStatus == "1"?"未审核":""}
                                        ${orderBean.auditStatus == "2"?"通过":""}
                                        ${orderBean.auditStatus == "3"?"不通过":""}
                                </td>
                                <td class="td-manage" align="center">
                                    <a title="查看详细" href="javascript:;"
                                       onclick="xadmin.open('查看详细','${contextPath}/servlet/OrderServlet?task=orderInfo&orderId=${orderBean.orderId}')">
                                        <i class="layui-icon layui-icon-form"></i>
                                    </a>
                                    <a title="订单审核" href="javascript:;"
                                       onclick="xadmin.open('订单审核','${contextPath}/servlet/OrderServlet?task=editOrderAudit&orderId=${orderBean.orderId}',600,320)">
                                        <i class="layui-icon layui-icon-auz"></i>
                                    </a>
                                    <a title="订单修改" href="javascript:;"
                                       onclick="xadmin.open('订单修改','${contextPath}/servlet/OrderServlet?task=editOrder&orderId=${orderBean.orderId}')">
                                        <i class="layui-icon layui-icon-edit"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>


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