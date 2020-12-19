<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>基本资料</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <link rel="stylesheet" href="${contextPath}/css/font.css">
    <link rel="stylesheet" href="${contextPath}/css/xadmin.css">
    <script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/xadmin.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery-validation/jquery.validate.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery-validation/localization/messages_zh.js"></script>
    <script type="text/javascript" src="${contextPath}/js/My97DatePicker/WdatePicker.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script>
        layui.use(['form']);

        $(function () {
            function isAlphaNum(value) {
                if (!value.match('^[0-9a-zA-Z]*$')) {
                    return false;
                }
                return true;
            }

            jQuery.validator.addMethod("alphaNum", function (value, element) {
                return isAlphaNum(value);
            }, '必须是英文字母或数字');

            jQuery.validator.addMethod("chinese", function (value, element) {
                if (!value.match('^([\u4E00-\uFA29]|[\uE7C7-\uE7F3])*$')) {
                    return false;
                }
                return true;
            }, '必须是中文');

            jQuery.validator.addMethod("yearDiffMin", function (value, element, params) {
                var birth = new Date(value);
                var now = new Date();
                var birthYear = birth.getFullYear();
                var nowYear = now.getFullYear();
                var diffYear = nowYear - birthYear;
                if (diffYear < params) {
                    return false;
                }
                return true;
            }, '年数差必须不能小于设定值');

            var validator = $("#save_form").validate({
                rules: {
                    username: {
                        required: true,
                        alphaNum: true,
                        rangelength: [3, 15]
                    },
                    password: {
                        required: true,
                        rangelength: [3, 15]
                    },
                    repassword: {
                        required: true,
                        equalTo: '#password'
                    },
                    sex: "required",
                    truename: {
                        required: true,
                        chinese: true,
                        rangelength: [2, 10]
                    },
                    birth: {
                        required: true,
                        date: true,
                        yearDiffMin: 10
                    },
                    email: {
                        required: true,
                        email: true
                    },
                    phone: {
                        required: true,
                        digits: true
                    },
                    address: {
                        required: true,
                        maxlength: 100
                    },
                    postcode: {
                        required: true,
                        digits: true,
                        rangelength: [6, 6]
                    }
                },
                messages: {
                    username: {
                        required: "用户名必填",
                        alphaNum: "用户名必须是字母或数字",
                        rangelength: "用户名长度需为3-15个字符"
                    },
                    password: {
                        required: "密码必填",
                        rangelength: "密码长度需为3-15个字符"
                    },
                    repassword: {
                        required: "确认密码必填",
                        equalTo: "两次密码不一致"
                    },
                    truename: {
                        required: "真实姓名必填",
                        chinese: '真实姓名必须是中文',
                        rangelength: "真实姓名长度需为2-10个字符"
                    },
                    sex: "性别必填",
                    birth: {
                        required: "出生日期必填",
                        date: "请填写正确的日期格式",
                        yearDiffMin: "年龄需大于等于10岁"
                    },
                    email: {
                        required: "邮箱必填",
                        email: "请填写正确的邮箱格式"
                    },
                    phone: {
                        required: "电话号码必填",
                        digits: "电话号码必须是数字"
                    },
                    address: {
                        required: "地址必填",
                        maxlength: "地址长度不能大于100个字符"
                    },
                    postcode: {
                        required: "邮编必填",
                        digits: "邮编必须是6位数字",
                        rangelength: "邮编必须是6位数字"
                    }
                }
            })

            $("#save_btn").click(function () {
                if (validator.form()) {
                    layer.confirm("确认修改？", {btn: ['是', '否']}, function () {
                        var formData = $("#save_form").serializeArray();
                        var saveUrl = "${contextPath}/member?task=updateProfile";
                        $.post(saveUrl, formData, function (jsonData) {
                            var flag = jsonData.flag;
                            var message = jsonData.message;
                            if (flag) {
                                layer.msg("修改成功", {icon: 1, time: 1000}, function () {
                                    location.reload();
                                });
                            } else {
                                layer.alert("修改失败，原因：" + message, {icon: 2});
                            }
                        }, "json");
                    });
                }
            });


            $("#reset_btn").click(function () {
                validator.resetForm();
            });
        })

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
    <div class="layui-card">
        <div class="layui-card-body">
            <div class="layui-row">
                <div class="layui-col-md2">
                    <c:import url="./common/member_left.jsp"></c:import>
                </div>
                <div class="layui-col-md10" style="padding:10px;border: 1px solid #e6e6e6">
                    <h2>基本资料</h2>
                    <hr>
                    <form id="save_form" class="layui-form" autocomplete="off">
                        <div class="layui-form-item">
                            <label class="layui-form-label">
                                用户名
                            </label>
                            <div class="layui-form-label" style="width: 150px;text-align: left">
                                <label>${frontUserBean.username}</label>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">
                                真实姓名
                            </label>
                            <div class="layui-input-inline">
                                <input type="text" name="truename" id="truename" class="layui-input" value="${frontUserBean.truename}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="truename" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">
                                性别
                            </label>
                            <div class="layui-input-inline">
                                <input type="radio" name="sex" id="sex" value="男" title="男" ${frontUserBean.sex == '男'?'checked':''} >
                                <input type="radio" name="sex" value="女" title="女" ${frontUserBean.sex == '女'?'checked':''} >
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="sex" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">
                                出生日期
                            </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input Wdate" name="birth" id="birth" value="${frontUserBean.birth}"
                                       onclick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd',firstDayOfWeek: 1})">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="birth" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">
                                电子邮箱
                            </label>
                            <div class="layui-input-inline">
                                <input type="email" class="layui-input" name="email" id="email" value="${frontUserBean.email}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="email" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">
                                电话号码
                            </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="phone" id="phone" value="${frontUserBean.phone}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="phone" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">
                                地址
                            </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="address" id="address" value="${frontUserBean.address}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="address" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">
                                邮编
                            </label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" name="postcode" id="postcode" value="${frontUserBean.postcode}">
                            </div>
                            <div class="layui-form-mid layui-word-aux">
                                <label for="postcode" class="error"></label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">
                                注册日期
                            </label>
                            <div class="layui-form-label" style="width: 150px;text-align: left">
                                <fmt:formatDate value="${frontUserBean.regDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">
                                最后登录时间
                            </label>
                            <div class="layui-form-label" style="width: 150px;text-align: left">
                                <fmt:formatDate value="${frontUserBean.lastDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">
                                登录次数
                            </label>
                            <div class="layui-form-label" style="width: 150px;text-align: left">
                                <label>${frontUserBean.loginNum}</label>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label"></label>
                            <button type="button" class="layui-btn" id="save_btn">修改</button>
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
