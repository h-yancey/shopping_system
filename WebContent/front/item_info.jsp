<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html class="x-admin-sm">
<head>
    <meta charset="UTF-8">
    <title>${itemBean.itemName}</title>
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
                , anim: 0 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
            })
        }


    </script>
    <style>
        .item-info-box {
            height: 600px;
            margin: 0px auto;
            padding: 10px;
        }

        .item-info-pic {
            width: 400px;
            height: 400px;
            margin: 10px;
        }

        .item-info-text {
            margin: 10px;

        }
    </style>
</head>
<body>
<!-- 顶部开始 -->
<c:import url="common/header.jsp"></c:import>
<!-- 顶部结束 -->

<!-- 左侧菜单开始 -->
<c:import url="common/left.jsp"></c:import>
<!-- 左侧菜单结束 -->

<!-- 内容主体区域 -->
<div class="page-content" style="overflow-y:auto;">
    <div class="layui-fluid">
        <div class="layui-card layui-col-space10">
            <div class="item-info-box">
                <div class="layui-row">
                    <div class="layui-col-md3">
                        <a href="${contextPath}" class="layui-btn layui-btn-normal" style="width: 70px;float:left;margin-top: 10px;margin-left: 20px">
                            <i class="layui-icon layui-icon-return"></i>返回
                        </a>
                    </div>
                    <div class="layui-col-md4">
                        <div class="item-info-pic">
                            <img src="${contextPath}/upload/${itemBean.imgName}" height="100%" width="100%" onclick="showLargeImg(this.src)" title="点击查看大图">
                        </div>
                    </div>
                    <div class="layui-col-md4">
                        <div class="layui-text item-info-text" style="line-height: 70px;">

                            <div class="layui-show">
                                <p>
                                    <label> 商品名称</label> <strong>${itemBean.itemName}</strong>
                                </p>
                            </div>

                            <div class="layui-show">
                                <p>
                                    <label> 商品价格</label> ￥<strong>${itemBean.itemPrice}</strong>
                                </p>
                            </div>

                            <div class="layui-show">
                                <p>
                                    <label> 商品类别</label> <strong>${itemBean.bigTypeName}/ ${itemBean.smallTypeName} </strong>
                                </p>
                            </div>


                            <div class="layui-show">
                                <p>
                                    <label> 是否缺货</label> <strong>${itemBean.shortageTag}</strong>
                                </p>
                            </div>
                            <div class="layui-show">
                                <p>
                                    <label> 商品描述</label> <strong>${itemBean.itemDesc}</strong>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md1">
                        &nbsp;
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md12">
                        <div style="text-align: center;margin-top:30px">
                            <c:if test="${itemBean.shortageTag == '否'}">
                                <a href="javascript:;" class="layui-btn layui-btn-warm layui-btn-lg" onclick="addCartItem(${itemBean.itemId})" style="width: 150px">
                                    <i class="layui-icon layui-icon-cart-simple"></i> 购 买
                                </a>
                            </c:if>
                            <c:if test="${itemBean.shortageTag == '是'}">
                                <a class="layui-btn layui-btn-disabled layui-btn-lg layui-disabled" style="width: 150px" title="缺货" disabled>
                                    <i class="layui-icon layui-icon-cart-simple"></i> 购 买
                                </a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
