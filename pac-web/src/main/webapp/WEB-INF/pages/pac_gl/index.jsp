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
    <title>大屏定制</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/media/image/favicon.ico"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/pac_gl/style/main.css"/>
</head>
<body>
<div class="header">
    <div class="logo"><h4>大屏定制</h4></div>
    <div class="user-info">
        <a href="javascript:void(0)" class="quit link-white" onclick="loginCasOut()"></a>
        <div class="user-txt">
            <span class="user-mame txt-white">${sessionUserInfo.userName }</span><br/> <a
                class="user-role link-white no-decoration"
                href="javascript:void(0)" id="user">${sessionUserInfo.orgName }<i
                class="fa fa-chevron-down"></i></a>
        </div>
        <div class="user-avatar"></div>
    </div>
</div>
<div class="side-bar">
    <h2>管理菜单</h2>
    <div class="main-nav" id="mainNav">
        <ul>
            <c:forEach items="${functionList}" var="f" varStatus="status">
                <c:choose>
                    <c:when test='${fn:contains(f.target,"blank")}'>
                        <li><a target="${f.target}" href="${pageContext.request.contextPath}${f.function_url}"
                               id="menu_${f.sort}"><i class="fa fa-area-chart"></i>
                            <span>${f.function_name}</span></a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a target="${f.target}" href="#${f.sign}" id="menu_${f.sort}"
                               url="${pageContext.request.contextPath}${f.function_url}"><i
                                class="fa fa-area-chart"></i> ${f.function_name}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </ul>
    </div>
</div>
<div class="main-container">
    <div class="main-container-title" id="titleContainer"></div>
    <div class="main-container-body" id="iframeContainer"></div>
</div>
</body>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/pac_gl/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/pac_gl/js/layer/layer.js"></script>
<script type="text/javascript">
    var flag = "${flag}";

    $(document).ready(function () {
        $("#mainNav").find("a").on("click", function () {
            if ($(this).attr("newWin")) {
                return;
            }
            var id = ($(this).attr("id")).split("_")[1];
            if (1 == id ) {
                return;
            }
            if ($("#tabTitle_" + id).length > 0) {  //存在
                //刷新子页面
                $("#frame_" + id).attr("src", ($("#frame_" + id).attr("src")).split("?_=")[0] + "?_=" + (new Date).getTime());
                if ($("#tabTitle_" + id).hasClass("tab-title-seled")) {	//已经处于激活状态，不做处理
                    return;
                } else {	//激活
                    $("#mainNav>ul>.nav-seled").removeClass("nav-seled");
                    $(this).parent().addClass("nav-seled");
                    var oldId = $("#titleContainer>.tab-title-seled").attr("id").split("_")[1];
                    $("#titleContainer>.tab-title-seled").removeClass("tab-title-seled");
                    $("#tabTitle_" + id).addClass("tab-title-seled");
                    $("#frame_" + oldId).hide();
                    $("#frame_" + id).show();
                }
            } else {	//未创建
                $("#mainNav>ul>.nav-seled").removeClass("nav-seled");
                $(this).parent().addClass("nav-seled");
                if ($("#titleContainer>.tab-title-seled").length > 0) {
                    var oldId = $("#titleContainer>.tab-title-seled").attr("id").split("_")[1];
                    $("#frame_" + oldId).hide();
                    $("#titleContainer>.tab-title-seled").removeClass("tab-title-seled");
                }
                var txt = $(this).text();
                $("#titleContainer").append("<span id='tabTitle_" + id + "' class='tab-title tab-title-seled'>" + txt + "<a href='javascript:void(0)'></a></span>");
                $("#iframeContainer").append("<iframe frameborder='0' id='frame_" + id + "' src='" + $(this).attr("url") + "'></iframe>");

                $("#titleContainer>span").last().on("click", function () {
                    if ($(this).hasClass("tab-title-seled")) {
                        return;
                    }
                    var id = $(this).attr("id").split("_")[1];
                    var oldItem = $("#titleContainer>.tab-title-seled");
                    var oldId = oldItem.attr("id").split("_")[1];
                    $("#mainNav>ul>.nav-seled").removeClass("nav-seled");
                    $("#menu_" + id).parent().addClass("nav-seled");
                    $("#tabTitle_" + id).addClass("tab-title-seled");
                    $("#frame_" + id).show();
                    oldItem.removeClass("tab-title-seled");
                    $("#frame_" + oldId).hide();
                });
                $("#titleContainer>span>a").last().on("click", function () {
                    var id = $(this).parent().attr("id").split("_")[1];
                    if (!$(this).parent().hasClass("tab-title-seled")) {
                        $("#frame_" + id).attr("src", "");
                        $("#frame_" + id).remove();
                        $(this).parent().remove();
                    } else {  //必然是当前的是选中项
                        if ($("#titleContainer>span").length > 1) {
                            var itemShow = $(this).parent().index() == 0 ? $(this).parent().next() : $(this).parent().prev();
                            var newId = itemShow.attr("id").split("_")[1];
                            itemShow.addClass("tab-title-seled");
                            $("#frame_" + newId).show();
                            $("#menu_" + newId).parent().addClass("nav-seled");
                        }
                        $("#frame_" + id).attr("src", "");
                        $("#frame_" + id).remove();
                        $(this).parent().remove();
                        $("#menu_" + id).parent().removeClass("nav-seled");
                    }
                });
            }
        });

        var linkFlag = (window.location.href).split("#").length == 2 ? (window.location.href).split("#")[1] : "";
        var linkObj = {};
        var str = '${functionList}';
        var json = JSON.parse(str);
        for (var i = 0; i < json.length; i++) {
            var obj = json[i];
            linkObj[obj.sign] = obj.id;
        }
        if (linkObj[linkFlag]) {
            $("#menu_" + linkObj[linkFlag]).trigger("click");
        }
        if (flag) {
            $("#menu_4").trigger("click");
        }

    });
    function loginCasOut() {
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            type: 'POST',
            dataType: "json",
            url: "${pageContext.request.contextPath}" + "/manager/loginAction/loginCasOut",
            error: function () {
                alert('请求失败');
            },
            success: function (res) { //请求成功后处理函数。
                if (res.success) {
                    window.location.href = "${casServerUrlPrefix}/logout?service=<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()%>/manager/loginAction/loginCas";
                } else {
                    alert('退出失败！');
                }
            }
        });
    }
    var linkObj = {};
    function menuClick(type) {
        if ("market" == type) {//市场
//            $("#menu_1").trigger('click');
            $("#menu_1 span").trigger('click');
        }
        if ("appView" == type) {//已组装应用预览
            $("#menu_2").trigger('click');
        }
        if ("viewList" == type) {//查看组装清单
            $("#menu_4").trigger('click');
        }
        if ("notice" == type) {//查看组装清单
            $("#menu_5").trigger('click');
        }
    }

    function myAlert(title, content) {
        layer.open({
            type: 1,
            title: title,
            shade: 0.3,
            zIndex: layer.zIndex, //用于选中置顶
            content: '<div style="padding: 20px; line-height: 20px;">' + content + '</div>',
            btn: ['确定'],
            btn1: function (index, layero) {
                layer.close(index);
            },
            success: function (layero, index) {
                layer.setTop(layero);	//用于选中置顶
            }
        });
    }
</script>
</html>
