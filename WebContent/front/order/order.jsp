<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>下订单</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/font.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/xadmin.css">
    <script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
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

            var validator = $("#order_form").validate({
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

            $("#order_btn").click(function () {
                if (validator.form()) {
                    layer.confirm("是否已确认订单信息填写正确？", {btn: ["是，确认下单", "否，再检查看看"]}, function () {
                        var url = "${contextPath}/order?task=placeOrder";
                        var formData = $("#order_form").serializeArray();
                        $.post(url, formData, function (jsonData) {
                            var flag = jsonData.flag;
                            var message = jsonData.message;
                            if (flag) {
                                layer.alert("您的订单已经提交成功<br>" + message, {icon: 1}, function () {
                                    window.location.href = "${contextPath}/member?task=myOrder";
                                });
                            } else {
                                layer.alert("您的订单提交失败，原因：" + message, {icon: 2});
                            }
                        }, "json");
                    })
                }
            });

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
<c:import url="../common/header.jsp"></c:import>
<div class="layui-fluid">
    <div class="layui-card" style="margin-left: 350px;margin-right: 350px">
        <div class="layui-card-body">
            <div class="layui-row">
                <div style="width: 600px;margin: 0px 200px">
                    <form class="layui-form" id="order_form" autocomplete="off">
                        <div class="layui-form-item">
                            <label class="layui-form-label"></label>
                            <h2>下订单</h2>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 订单号 </label>
                            <div class="layui-input-inline">
                                <input class="layui-input" name="orderId" value="${paramMap.maxOrderId}" readonly>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">商品种类数</label>
                            <div class="layui-input-inline">
                                <input class="layui-input" name="itemTypeSize" value="${typeSet.size()}" readonly>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">商品总件数</label>
                            <div class="layui-input-inline">
                                <input class="layui-input" name="itemSize" value="${cartItemCount}" readonly>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 价格总计(￥) </label>
                            <div class="layui-input-inline">
                                <input class="layui-input" name="totalPrice" value="${cartTotalPrice}" readonly>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 付款方式 </label>
                            <div class="layui-input-inline">
                                <input class="layui-input" name="payType" value=" ${paramMap.payType}" readonly>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货方式 </label>
                            <div class="layui-input-inline">
                                <input class="layui-input" name="sendType" value="${paramMap.sendType}" readonly>
                            </div>
                        </div>
                        <div class="layui-form-item"></div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货人姓名 </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="consignee" id="consignee" value="${frontUserBean.truename}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="consignee" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货人地址 </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="address" id="address" value="${frontUserBean.address}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="address" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货人邮编 </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="postcode" id="postcode" value="${frontUserBean.postcode}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="postcode" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货人电话 </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="phone" id="phone" value="${frontUserBean.phone}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="phone" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"> 收货人邮箱 </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="email" id="email" value="${frontUserBean.email}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="email" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"></label>
                            <button type="button" class="layui-btn layui-btn-warm" id="order_btn">下订单</button>
                            <button type="reset" class="layui-btn layui-btn-primary" id="reset_btn"> 重置</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
