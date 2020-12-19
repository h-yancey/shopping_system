<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!doctype html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>用户登录</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="stylesheet" href="${contextPath}/css/font.css">
    <link rel="stylesheet" href="${contextPath}/css/login.css">
    <link rel="stylesheet" href="${contextPath}/css/xadmin.css">
    <script type="text/javascript" src="${contextPath}/js/jquery.min.js"></script>
    <script src="${contextPath}/lib/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery-validation/jquery.validate.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery-validation/localization/messages_zh.js"></script>
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script>
        layui.use('form');

        function isAlphaNum(value) {
            if (!value.match('^[0-9a-zA-Z]*$')) {
                return false;
            }
            return true;
        }

        function draw(show_num) {
            var canvas_width = $('#canvas').width();
            var canvas_height = $('#canvas').height();
            var canvas = document.getElementById("canvas");//获取到canvas的对象，演员
            var context = canvas.getContext("2d");//获取到canvas画图的环境，演员表演的舞台
            canvas.width = canvas_width;
            canvas.height = canvas_height;
            var sCode = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0";
            var aCode = sCode.split(",");
            var aLength = aCode.length;//获取到数组的长度

            for (var i = 0; i <= 3; i++) {
                var j = Math.floor(Math.random() * aLength);//获取到随机的索引值
                var deg = Math.random() * 30 * Math.PI / 180;//产生0~30之间的随机弧度
                var txt = aCode[j];//得到随机的一个内容
                show_num[i] = txt.toLowerCase();
                var x = 10 + i * 20;//文字在canvas上的x坐标
                var y = 20 + Math.random() * 8;//文字在canvas上的y坐标
                context.font = "bold 23px 微软雅黑";

                context.translate(x, y);
                context.rotate(deg);

                context.fillStyle = randomColor();
                context.fillText(txt, 0, 0);

                context.rotate(-deg);
                context.translate(-x, -y);
            }
            for (var i = 0; i <= 5; i++) { //验证码上显示线条
                context.strokeStyle = randomColor();
                context.beginPath();
                context.moveTo(Math.random() * canvas_width, Math.random() * canvas_height);
                context.lineTo(Math.random() * canvas_width, Math.random() * canvas_height);
                context.stroke();
            }
            for (var i = 0; i <= 30; i++) { //验证码上显示小点
                context.strokeStyle = randomColor();
                context.beginPath();
                var x = Math.random() * canvas_width;
                var y = Math.random() * canvas_height;
                context.moveTo(x, y);
                context.lineTo(x + 1, y + 1);
                context.stroke();
            }
        }

        //得到随机的颜色值
        function randomColor() {
            var r = Math.floor(Math.random() * 256);
            var g = Math.floor(Math.random() * 256);
            var b = Math.floor(Math.random() * 256);
            return "rgb(" + r + "," + g + "," + b + ")";
        }

        $(function () {
            jQuery.validator.addMethod("alphaNum", function (value, element) {
                return isAlphaNum(value);
            }, '必须是英文字母或数字');

            var validator = $("#login_form").validate({
                rules: {
                    username: {
                        required: true,
                        alphaNum: true,
                        rangelength: [3, 15]
                    },
                    pwd: {
                        required: true,
                        rangelength: [3, 15]
                    }
                },
                messages: {
                    username: {
                        required: "用户名必填",
                        alphaNum: "用户名必须是字母或数字",
                        rangelength: "用户名长度需为3-15个字符"
                    },
                    pwd: {
                        required: "密码必填",
                        rangelength: "密码长度需为3-15个字符"
                    }
                }
            });

            var show_num = [];
            draw(show_num);
            $("#canvas").click(function () {
                draw(show_num);
            });

            $("#login_btn").click(function () {
                var inputCode = $(".input-val").val().toLowerCase();
                var code = show_num.join("");
                if (validator.form()) {
                    if (inputCode == "") {
                        layer.msg("请输入验证码", {icon: 7, time: 1000});
                    } else if (inputCode == code) {
                        var formData = $("#login_form").serializeArray();
                        var loginUrl = "${contextPath}/servlet/FrontLoginServlet?task=login";
                        $.post(loginUrl, formData, function (jsonData) {
                            var flag = jsonData.flag;
                            var message = jsonData.message;
                            if (flag) {
                                window.location.href = message;
                            } else {
                                layer.alert(message, {icon: 2});
                            }
                        }, "json");
                    } else {
                        layer.msg("验证码有错，请重新输入", {icon: 5, time: 1000});
                        $(".input-val").val("");
                        draw(show_num);
                    }
                }
            });

            $("input").keydown(function (event) {
                if (event.keyCode == 13) {
                    var inputCode = $(".input-val").val().toLowerCase();
                    var code = show_num.join("");
                    if (validator.form()) {
                        if (inputCode == "") {
                            layer.msg("请输入验证码", {icon: 7, time: 1000});
                        } else if (inputCode == code) {
                            var formData = $("#login_form").serializeArray();
                            var loginUrl = "${contextPath}/servlet/FrontLoginServlet?task=login";
                            $.post(loginUrl, formData, function (jsonData) {
                                var flag = jsonData.flag;
                                var message = jsonData.message;
                                if (flag) {
                                    window.location.href = message;
                                } else {
                                    layer.alert(message, {icon: 2});
                                }
                            }, "json");
                        } else {
                            layer.msg("验证码有错，请重新输入", {icon: 5, time: 1000});
                            $(".input-val").val("");
                            draw(show_num);
                        }
                    }
                }
            });

        });
    </script>
    <style>
        .code {
            width: 100%;
            margin: 0 auto;
        }

        .input-val {
            background: #ffffff;
            padding: 0 2%;
            border-radius: 5px;
            border: 1px solid rgba(0, 0, 0, .2);
            font-size: 0.9rem;
        }

        #canvas {
            float: right;
            display: inline-block;
            border: 1px solid #ccc;
            border-radius: 5px;
            cursor: pointer;
        }

        input.error {
            border: 1px dotted red;
        }

        label.error {
            color: red;
        }
    </style>
</head>
<body>
<!-- 顶部开始 -->
<div class="container">
    <div class="logo">
        <a href="${contextPath}" style="font-size: 28px">购 物 商 城</a>
    </div>
    <ul class="layui-nav left">
        <li class="layui-nav-item">
            <a href="${contextPath}"><i class="layui-icon layui-icon-home">&nbsp;首页</i></a>
        </li>
        <li class="layui-nav-item">
            <a href="${contextPath}/member"><i class="layui-icon layui-icon-user">&nbsp;会员中心</i></a>
        </li>
        <li class="layui-nav-item">
            <a href="${contextPath}/cart"><i class="layui-icon layui-icon-cart">&nbsp;购物车</i></a>
        </li>
    </ul>
</div>
<!-- 顶部结束 -->
<div class="login layui-anim layui-anim-up">
    <div class="message">用户登录</div>
    <div id="darkbannerwrap"></div>
    <form class="layui-form layui-form-pane" id="login_form">
        <input type="hidden" name="referer" value="${header.referer}">
        <div class="layui-form-item">
            <label class="layui-form-label" style="height: 50px;width: 15%;">
                <i class="layui-icon layui-icon-username" style="line-height:30px;font-size:18px"></i>
            </label>
            <input style="width: 85%" type="text" name="username" id="username" placeholder="用户名" class="layui-input">
            <div style="height: 12px;width: 100%"><label for="username" class="error"></label></div>
        </div>


        <div class="layui-form-item">
            <label class="layui-form-label" style="height: 50px;width: 15%;">
                <i class="layui-icon layui-icon-password" style="line-height:30px;font-size:18px"></i>
            </label>
            <input style="width: 85%" type="password" name="pwd" id="pwd" placeholder="密码" class="layui-input">
            <div style="height: 12px;width: 100%"><label for="pwd" class="error"></label></div>
        </div>


        <div class="code">
            <label class="layui-form-label" style="height: 50px;width: 15%;">
                <i class="layui-icon layui-icon-vercode" style="line-height:30px;font-size:18px"></i>
            </label>
            <input type="text" placeholder="验证码（不区分大小写）" class="input-val" style="width: 50%; height: 50px">
            <canvas id="canvas" width="100" height="50"></canvas>
        </div>
        <hr class="hr20">
        <input type="button" id="login_btn" value="登录">
        <hr class="hr20">
        <a href="${contextPath}/register.jsp" style="float: right">免费注册</a>
    </form>
</div>
</body>
</html>