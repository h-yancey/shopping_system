<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>查看订单</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" href="${contextPath}/css/font.css">
    <link rel="stylesheet" href="${contextPath}/css/xadmin.css">
    <script type="text/javascript" src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script>
        layui.use(['form', 'layer']);

        function submitForm() {
            var url = "${contextPath}/servlet/OrderServlet?task=updateOrderAudit";
            var formData = $("#audit_form").serializeArray();
            $.post(url, formData, function (jsonData) {
                var flag = jsonData.flag;
                if (flag) {
                    layer.msg("保存成功", {icon: 1,time: 1000}, function () {
                        //关闭当前frame
                        xadmin.close();

                        // 可以对父窗口进行刷新
                        xadmin.father_reload();
                    });
                } else {
                    layer.alert("保存失败", {icon: 2});
                }
            }, "json");
        }
    </script>

</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body ">
                    <form class="layui-form" id="audit_form">
                        <input type="hidden" value="${orderBean.orderId}">
                        <div class="layui-form-item">
                            <label class="layui-form-label">审核状态</label>
                            <div class="layui-input-inline">
                                <select name="auditStatus">
                                    <option value="">订单状态</option>
                                    <option value="1" ${orderBean.auditStatus == "1"?"selected":""}>未审核</option>
                                    <option value="2" ${orderBean.auditStatus == "2"?"selected":""}>通过</option>
                                    <option value="3" ${orderBean.auditStatus == "3"?"selected":""}>不通过</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">订单反馈</label>
                            <div class="layui-input-inline" style="width: 400px">
                                <textarea placeholder="请输入内容" name="msg"  class="layui-textarea">${orderBean.msg}</textarea>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"></label>
                            <button type="button" class="layui-btn" onclick="submitForm()">保存</button>
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>