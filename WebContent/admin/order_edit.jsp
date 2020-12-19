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
    <script type="text/javascript" src="${contextPath}/js/jquery-validation/jquery.validate.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery-validation/localization/messages_zh.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script>
        layui.use(['form', 'layer']);

        $(function () {
            jQuery.validator.addMethod("chinese", function (value) {
                if (!value.match('^([\u4E00-\uFA29]|[\uE7C7-\uE7F3])*$')) {
                    return false;
                }
                return true;
            }, '必须是中文');

            var validator = $("#save_form").validate({
                rules: {
                    consignee: {
                        required: true,
                        chinese: true,
                        rangelength: [2, 10]
                    },
                    address: {
                        required: true,
                        rangelength: [3, 100]
                    },
                    postcode: {
                        required: true,
                        digits: true,
                        rangelength: [6, 6]
                    },
                    phone: {
                        required: true,
                        digits: true
                    },
                    email: {
                        required: true,
                        email: true
                    },

                },
                messages: {
                    truename: {
                        required: "收货人姓名必填",
                        chinese: '收货人姓名必须是中文',
                        rangelength: "收货人姓名长度需为2-10个字符"
                    },
                    address: {
                        required: "地址必填",
                        maxlength: "地址长度需为3-100个字符"
                    },
                    postcode: {
                        required: "邮编必填",
                        digits: "邮编必须是6位数字",
                        rangelength: "邮编必须是6位数字"
                    },
                    phone: {
                        required: "电话号码必填",
                        digits: "电话号码必须是数字"
                    },
                    email: {
                        required: "邮箱必填",
                        email: "请填写正确的邮箱格式"
                    }
                }
            });

            $("#save_btn").click(function () {
                if (validator.form()) {
                    var formData = $("#save_form").serializeArray();
                    var saveUrl = "${contextPath}/servlet/OrderServlet?task=updateOrder";
                    $.post(saveUrl, formData, function (jsonData) {
                        var flag = jsonData.flag;
                        var message = jsonData.message;
                        if (flag) {
                            layer.msg("修改成功", {icon: 1, time: 1000}, function () {
                                //location.reload();
                                xadmin.father_reload();
                            });
                        } else {
                            layer.alert("修改失败，原因：" + message, {icon: 2});
                        }
                    }, "json");
                }
            })


            $("#reset_btn").click(function () {
                validator.resetForm();
            });
        });
    </script>
    <style>
        input.error {
            border: 1px dotted red;
        }

        label.error {
            margin-left: 10px;
            color: red;
        }
    </style>

</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body ">
                    <form class="layui-form" id="save_form">
                        <div class="layui-form-item">
                            <label class="layui-form-label">订单号</label>
                            <div class="layui-input-inline">
                                <input type="hidden" name="orderId" value="${orderBean.orderId}">
                                <input type="text" class="layui-input" name="orderId" value="${orderBean.orderId}" disabled>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 付款方式 </label>
                            <div class="layui-input-inline">
                                <select name="payType">
                                    <option value="在线支付" ${orderBean.payType == "在线支付"?"selected":""}>在线支付</option>
                                    <option value="货到付款" ${orderBean.payType == "货到付款"?"selected":""}>货到付款</option>
                                    <option value="汇款支付" ${orderBean.payType == "汇款支付"?"selected":""}>汇款支付</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货方式 </label>
                            <div class="layui-input-inline">
                                <select name="sendType">
                                    <option value="快递" ${orderBean.sendType == "快递"?"selected":""}>快递</option>
                                    <option value="邮寄" ${orderBean.sendType == "邮寄"?"selected":""}>邮寄</option>
                                    <option value="自提" ${orderBean.sendType == "自提"?"selected":""}>自提</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货人姓名 </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="consignee" id="consignee" value="${orderBean.consignee}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="consignee" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货人地址 </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="address" id="address" value="${orderBean.address}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="address" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货人邮编 </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="postcode" id="postcode" value="${orderBean.postcode}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="postcode" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货人电话 </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="phone" id="phone" value="${orderBean.phone}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="phone" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货人邮箱 </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="email" id="email" value="${orderBean.email}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="email" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"></label>
                            <button type="button" class="layui-btn" id="save_btn">保存</button>
                            <button type="reset" class="layui-btn layui-btn-primary" id="reset_btn">重置</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>