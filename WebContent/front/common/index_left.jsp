<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<head>
    <title>Title</title>
</head>
<body>
<div class="left-nav" style="overflow-y:auto;">
    <div id="side-nav">
        <div class="layui-card">
            <div class="layui-card-body">
                <div style="width: 100%;height: 200px;">
                    <span>购物车 </span>
                    <span>数量：</span>
                    <span>金额：</span>
                </div>
            </div>
        </div>
        <div class="layui-card">
            <div class="layui-card-body">
                <ul class="">
                    <c:forEach items="${typeList}" var="typeBean">
                        <li>
                            <a class="" href="${contextPath}?itemType=${typeBean.typeId}">${typeBean.typeName}</a>
                            <ul>
                                <c:forEach items="${typeBean.childTypeList}" var="childTypeBean">
                                    <li><a href="${contextPath}?itemType=${childTypeBean.typeId}">&nbsp;&nbsp;-${childTypeBean.typeName}</a></li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</div>
</body>
</html>
