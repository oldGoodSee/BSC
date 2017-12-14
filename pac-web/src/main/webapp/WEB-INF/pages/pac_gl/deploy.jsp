<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/11/17
  Time: 15:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css"/>
</head>
<body>
<div class="layout-main" style="margin-top: 20px;position: relative;left: 30px;">
    <br>
    <label>Step&nbsp;4&nbsp;:</label>
    <label>发布你的应用</label><br><br>
    <label style="font-size: x-large">点击下方部署按钮即可完成此次配置！</label><br>
    <br><br>
    <form class="form-inline" name="form" action="${pageContext.request.contextPath}/manager/basket/makeNewApp"
          onkeydown="if(event.keyCode==13)return false;">
        <button type="button" class="btn btn-lg btn-success" style="position: fixed;top: 30%;left: 30%"
                onclick="deploy()">部署应用
        </button>
        <br><br><br>
        <input type="button" class="btn btn-lg btn-default" style="position: fixed;left: 30%"
               onclick="release()" value="预览">
        <br>
        <br>
        <label id="urlLabel" style="position: fixed;left: 15%;top: 40%;"></label>
        <%--<input type="hidden" name="appId" value="${appId}">--%>
    </form>
</div>
</body>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/pac_gl/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
    function release() {
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            type: 'get',
            dataType: "json",
            data: {
                appId: appId
            },
            url: "${pageContext.request.contextPath}/manager/basket/previewApp",
            error: function () {//请求失败处理函数
                window.parent.layer.alert('请求失败');
            },
            success: function (res) { //请求成功后处理函数。
                if ("success" == res.result) {
                    $("#urlLabel").html("用户端访问的地址&nbsp;：&nbsp;" + res.url);
                    window.parent.layer.msg("部署成功", {icon: 1})
                } else {
                    window.parent.layer.msg(res.reason, {icon: 5})
                }
            }
        });

    }
    function deploy() {
        var appId = ${appId};
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            type: 'get',
            dataType: "json",
            data: {
                appId: appId
            },
            url: "${pageContext.request.contextPath}/manager/basket/makeNewApp",
            error: function () {//请求失败处理函数
                window.parent.layer.alert('请求失败');
            },
            success: function (res) { //请求成功后处理函数。
                if ("success" == res.result) {
                    $("#urlLabel").html("用户端访问的地址&nbsp;：&nbsp;" + res.url);
                    window.parent.layer.msg("部署成功", {icon: 1})
//                    window.parent.layer.confirm('部署成功，是否跳转到组装应用浏览？', {
//                        btn: ['是', '否']
//                    }, function (index, layero) {
//                        window.parent.menuClick("appView");
//                        window.parent.layer.closeAll();
//                    }, function (index) {
////                        window.parent.layer.msg('成功!', {icon: 1});
//                    });
                } else {
                    window.parent.layer.msg(res.reason, {icon: 5})
                }
            }
        });
    }
</script>
</html>
