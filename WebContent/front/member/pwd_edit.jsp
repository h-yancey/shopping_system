<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>密码修改</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/font.css">
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/xadmin.css">
    <script src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
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
            var validator = $("#update_pwd_form").validate({
                rules: {
                    oldPwd: {
                        required: true,
                        rangelength: [3, 15]
                    },
                    pwd: {
                        required: true,
                        rangelength: [3, 15]
                    },
                    repwd: {
                        required: true,
                        equalTo: '#pwd'
                    },
                },
                messages: {
                    oldPwd: {
                        required: "旧密码必填",
                        rangelength: "密码长度需为3-15个字符"
                    },
                    pwd: {
                        required: "新密码必填",
                        rangelength: "密码长度需为3-15个字符"
                    },
                    repwd: {
                        required: "确认密码必填",
                        equalTo: "两次密码不一致"
                    }
                }
            });

            $("#update_pwd_btn").click(function () {
                if (validator.form()) {
                    var formData = $("#update_pwd_form").serializeArray();
                    var saveUrl = "${contextPath}/member?task=updatePwd";
                    $.post(saveUrl, formData, function (jsonData) {
                        var flag = jsonData.flag;
                        var message = jsonData.message;
                        if (flag) {
                            layer.msg("密码修改成功", {icon: 1, time: 1000}, function () {
                                location.reload();
                            });
                        } else {
                            layer.alert("修改失败，原因：" + message, {icon: 2});
                        }
                    }, "json");
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
                    <h2>密码修改</h2>
                    <hr>
                    <form class="layui-form" autocomplete="off" id="update_pwd_form">
                        <div class="layui-form-item">
                            <label for="oldPwd" class="layui-form-label">
                                旧密码
                            </label>
                            <div class="layui-input-inline">
                                <input type="password" id="oldPwd" name="oldPwd" class="layui-input">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="oldPwd" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label for="pwd" class="layui-form-label">
                                新密码
                            </label>
                            <div class="layui-input-inline">
                                <input type="password" id="pwd" name="pwd" class="layui-input">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="pwd" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label for="repwd" class="layui-form-label">
                                确认密码
                            </label>
                            <div class="layui-input-inline">
                                <input type="password" id="repwd" name="repwd" class="layui-input">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="repwd" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"></label>
                            <button type="button" class="layui-btn" id="update_pwd_btn">修改</button>
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