<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>修改商品</title>
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
    <script type="text/javascript" src="${contextPath}/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${contextPath}/js/jquery.form.min.js"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script>
        function showLargeImg(src) {
            layer.photos({
                photos: {
                    "title": "", //相册标题
                    "id": 0, //相册id
                    "start": 0, //初始显示的图片序号，默认0
                    "data": [   //相册包含的图片，数组格式
                        {
                            "alt": "",
                            "pid": 0, //图片id
                            "src": src, //原图地址
                            "thumb": "" //缩略图地址
                        },
                    ]
                }
                ,anim: 0 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
            })
        }

        function isImg(value) {
            if (value == "") {
                return true;
            }
            var filepath = value;
            //获得上传文件名
            var fileArr = filepath.split("\\");
            var fileTArr = fileArr[fileArr.length - 1].toLowerCase().split(".");
            var filetype = fileTArr[fileTArr.length - 1];
            //切割出后缀文件名
            if (filetype == "jpg" || filetype == "gif" || filetype === "bmp") {
                return true;
            } else {
                return false;
            }
        }

        function getObjectURL(file) {
            var url = null;
            if (window.createObjectURL != undefined) {
                url = window.createObjectURL(file);
            } else if (window.URL != undefined) {
                url = window.URL.createObjectURL(file);
            } else if (window.webkitURL != undefined) {
                url = window.webkitURL.createObjectURL(file);
            }
            return url;
        }


        jQuery(function () {
            jQuery.validator.addMethod("twoDecimalPlace", function (value, element) {
                inputZ = value;
                var arr = inputZ.split(".");    //截取字符串
                if (arr.length == 2) {
                    if (arr[1].length > 2) {    //判断小数点后面的字符串长度
                        return false;
                    }
                }
                return true;
            }, "小数点后最多为两位");         //验证错误信息

            jQuery.validator.addMethod("checkImg", function (value, element) {
                return isImg(value);
            }, "上传的文件必须为图片");

            var validator = $("#save_form").validate({
                rules: {
                    itemId: {
                        required: true,
                        digits: true,
                        min: 1
                    },
                    itemName: {
                        required: true,
                        //  rangelength: [3, 15],
                    },
                    itemPrice: {
                        required: true,
                        number: true,
                        min: 0,
                        twoDecimalPlace: true,
                        // $("input[name='itemPrice']").val()
                    },
                    smallTypeId: {
                        required: true
                    },
                    addDate: {
                        required: true,
                        date: true
                    },
                    imgName: {
                        checkImg: true
                    },
                    itemDesc: "required",
                },
                messages: {
                    itemId: {
                        required: "商品编号不能为空",
                        digits: "请输入正整数",
                        min: "请输入正整数"
                    },
                    itemName: {
                        required: "商品名称不能为空",
                        // rangelength: "类别名称长度允许3-15个字符",

                    },
                    itemPrice: {
                        required: "商品价格不能为空",
                        number: "商品价格必须是非负数",
                        min: "商品价格必须是非负数"
                    },
                    smallTypeId: "请选择一个类别",
                    addDate: {
                        required: "添加时间不能为空",
                        date: "请填写正确的日期格式"
                    }
                    ,
                    itemDesc: "商品描述不能为空",
                }
            })

            $("#save_btn").click(function () {
                if (validator.form()) {
                    var saveUrl = "${contextPath}/servlet/ItemServlet?task=update";
                    $("#save_form").ajaxSubmit({
                        url: saveUrl,
                        type: "post",
                        dataType: "json",
                        async: true,
                        success: function (jsonData) {
                            var flag = jsonData.flag;
                            var message = jsonData.message;
                            if (flag) {
                                layer.alert("修改成功", {
                                        icon: 1
                                    },
                                    function () {
                                        //关闭当前frame
                                        xadmin.close();

                                        // 可以对父窗口进行刷新
                                        xadmin.father_reload();
                                    })
                            } else {
                                layer.alert("修改失败，原因：" + message, {
                                    icon: 2
                                });
                            }
                        },
                        error: function () {
                            layer.alert("数据加载失败", {
                                icon: 2
                            });
                        }
                    })
                }
            });

            $("#reset_btn").click(function () {
                validator.resetForm();
                $("#img_show").attr("src", "${contextPath}/upload/${itemBean.imgName}").show();
                $("#span_imgname").html("${itemBean.imgName}");
            });

            $("#imgName").change(function () {
                var file = this.files[0];
                if (!file) {
                    $("#img_show").attr("src", "${contextPath}/upload/${itemBean.imgName}").show();
                    $("#span_imgname").html("${itemBean.imgName}");
                } else {
                    var fileName = file.name;
                    var fileSrc = getObjectURL(file);
                    if (fileSrc && isImg(fileName)) {
                        $("#img_show").attr("src", fileSrc).show();
                        $("#span_imgname").html(fileName);
                    } else {
                        $("#img_show").hide();
                        $("#span_imgname").html("");
                    }
                }
            })
        });

        layui.use(['form', 'layer']);
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
    <div class="layui-row">
        <form class="layui-form" id="save_form" autocomplete="off" enctype="multipart/form-data">
            <div class="layui-form-item">
                <label class="layui-form-label"> <span class="x-red">*</span>商品编号</label>
                <div class="layui-input-inline">
                    <input type="text" name="itemId" class="layui-input" value="${itemBean.itemId}" disabled>
                    <input type="hidden" name="itemId" class="layui-input" value="${itemBean.itemId}">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"> <span class="x-red">*</span>商品名称</label>
                <div class="layui-input-inline">
                    <input type="text" name="itemName" id="itemName" class="layui-input" value="${itemBean.itemName}">
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <label for="itemName" class="error"></label>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"> <span class="x-red">*</span>商品价格</label>
                <div class="layui-input-inline">
                    <input type="text" name="itemPrice" id="itemPrice" class="layui-input" value="${itemBean.itemPrice}">
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <label for="itemPrice" class="error"></label>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="x-red">*</span>商品类别</label>
                <div class="layui-input-inline">
                    <select name="smallTypeId" id="smallTypeId">
                        <option>请选择</option>
                        <c:forEach items="${typeList}" var="typeBean">
                            <optgroup label="${typeBean.typeName}">
                                <c:forEach items="${typeBean.childTypeList}" var="childTypeBean">
                                    <option value="${childTypeBean.typeId}" ${itemBean.smallTypeId == childTypeBean.typeId?"selected":""}>${childTypeBean.typeName}</option>
                                </c:forEach>
                            </optgroup>
                        </c:forEach>
                    </select>
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <label for="smallTypeId" class="error"></label>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="x-red">*</span>添加时间</label>
                <div class="layui-input-inline">
                    <input type="text" class="layui-input Wdate" name="addDate" id="addDate" value="${itemBean.addDate}"
                           onclick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd HH:mm:ss',firstDayOfWeek: 1})">
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <label for="addDate" class="error"></label>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="x-red">*</span>商品图片</label>
                <div class="layui-input-inline">
                    <div style="width: 100px;height: 100px;background: white">
                        <img src="${contextPath}/upload/${itemBean.imgName}" height="100" width="100" id="img_show" onclick="showLargeImg(this.src)">
                    </div>
                    <input type="hidden" name="oldImgName" value="${itemBean.imgName}">
                    <input type="file" class="layui-input" name="imgName" id="imgName">
                </div>
                <div class="layui-form-mid">
                    <span id="span_imgname" style="float: bottom;color: black">${itemBean.imgName}</span>
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <label for="imgName" class="error"></label>
                </div>
            </div>

            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label"><span class="x-red">*</span>商品描述</label>
                <div class="layui-input-inline" style="width: 800px">
                    <textarea placeholder="请输入内容" name="itemDesc" id="itemDesc" class="layui-textarea">${itemBean.itemDesc}</textarea>
                </div>
                <div class="layui-form-mid layui-word-aux">
                    <label for="itemDesc" class="error"></label>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"><span class="x-red">*</span>是否缺货</label>
                <div class="layui-input-inline">
                    <select name="shortageTag">
                        <option value="否" ${itemBean.shortageTag == "否"?"selected":""}>否</option>
                        <option value="是" ${itemBean.shortageTag == "是"?"selected":""}>是</option>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label"></label>
                <button type="button" class="layui-btn" id="save_btn">保存修改</button>
                <button type="reset" class="layui-btn layui-btn-primary" id="reset_btn">重置</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
