<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>增加类别</title>
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
        jQuery(function () {

            var validator = $("#save_form").validate({
                rules: {
                    typeId: {
                        required: true,
                        digits: true,
                        min: 1

                    },
                    typeName: {
                        required: true,
                        //  rangelength: [3, 15],
                    },
                },
                messages: {
                    typeId: {
                        required: "类别编号不能为空",
                        digits: "请输入正整数",
                        min: "请输入正整数"
                    },
                    typeName: {
                        required: "类别名称不能为空",
                        // rangelength: "类别名称长度允许3-15个字符",
                    },
                }
            })

            $("#save_btn").click(function () {
                if (validator.form()) {
                    var formData = $("#save_form").serializeArray();
                    var saveUrl = "${contextPath}/servlet/TypeServlet?task=save";
                    jQuery.post(saveUrl, formData, function (jsonData) {
                        var flag = jsonData.flag;
                        var message = jsonData.message;
                        if (flag) {
                            layer.alert("增加成功", {
                                    icon: 1
                                },
                                function () {
                                    //关闭当前frame
                                    xadmin.close();

                                    // 可以对父窗口进行刷新
                                    xadmin.father_reload();
                                })
                        } else {
                            layer.alert("添加失败，原因：" + message, {
                                icon: 2
                            });
                        }
                    }, "json");
                }
            });

            $("#reset_btn").click(function () {
                validator.resetForm();
            });

        });
        layui.use(['form', 'layer']);
    </script>
    <style>
        input.error {
            border: 1px dotted red;
        }

        label.error {
            clear: left;
            margin-left: 10px;
            color: red;
        }
    </style>
</head>
<body>
<%--<div class="layui-row">--%>
<%--    <a class="layui-btn-small" style="line-height:1.6em;margin-right:15px;float:right" onclick="location.reload()" title="刷新">--%>
<%--        <i class="layui-icon layui-icon-refresh" style="line-height:30px"></i>--%>
<%--    </a>--%>
<%--</div>--%>
<div class="layui-fluid">
    <div class="layui-row">
        <form class="layui-form" id="save_form" autocomplete="off">
            <div class="layui-form-item">
                <label class="layui-form-label"> <span class="x-red">*</span>类别编号</label>
                <div class="layui-input-inline">
                    <input type="text" name="typeId" id="typeId" class="layui-input" value="${maxTypeId}"/>
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <label for="typeId" class="error"></label>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"> <span class="x-red">*</span>类别名称</label>
                <div class="layui-input-inline">
                    <input type="text" name="typeName" id="typeName" class="layui-input"/>
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <label for="typeName" class="error"></label>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label"> <span class="x-red">*</span>上级分类</label>
                <div class="layui-input-inline">
                    <select name="parentId">
                        <option value="0">无</option>
                        <c:forEach items="${parentTypeList}" var="typeBean">
                            <option value="${typeBean.typeId}">${typeBean.typeName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"></label>
                <button type="button" class="layui-btn" id="save_btn">保存类别</button>
                <button type="reset" class="layui-btn layui-btn-primary" id="reset_btn">重置</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>