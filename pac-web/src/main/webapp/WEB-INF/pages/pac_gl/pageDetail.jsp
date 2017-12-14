<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>警务应用市场</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/market/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/market/css/sub.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/market/css/reset.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/market/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/market/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/market/js/laypage-v1.3/laypage/laypage.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/market/js/layer/layer.js"></script>
</head>

<body>
<div class="main">
    <div class="topbar">
        <a href="${pageContext.request.contextPath}/manager/menuAction/toIndex" target="_blank">首页</a>
        >
        <a href="javascript:void(0);" onclick="javascript:history.go(-1);" target="_self">警务微应用市场</a>
        >
        <a href="javascript:void(0)" >挑选微应用</a>
    </div>

    <div class="app-info">
        <div class="app-img" style="max-width:30%; max-width:70%">
            <img src="${param.imgUrl}" id="appImg" style="width:100%; height:100%; "/>
            <br/>
            <span id="appId"></span>
        </div>
        <div class="app-intro">
            <h1 id="appName">${pageName}</h1>
            <p id="otherInfo">
                所属应用：${appName}
                <br/>
                组装次数：${composeNum}次
            </p>
            <button class="base-btn" type="button" onclick="javascript:addToBasket();">加入应用篮子</button>
        </div>
        <div class="clear"></div>
    </div>

    <div class="step-container">
        <ul class="step">
            <ol style="background-position-y:0;">
                <span>将微应用加入应用篮子</span>
            </ol>
            <ol style="background-position-y:-36px;">
                <span>前往编辑布局界面组装</span>
            </ol>
            <ol style="background-position-y:-72px;">
                <span>预览组装应用</span></ol>
            <ol style="background-position-y:-114px;">
                <span>发布组装应用</span>
            </ol>
        </ul>
    </div>
    <div class="more-info clear">
        <h2>应用详情</h2>
    </div>
    <p class="more-info-p" id="appMoreInfo">
        应用编号:${pageId}&nbsp;&nbsp;&nbsp;商品类型：微应用&nbsp;&nbsp;&nbsp;发布时间：
        <label style="font-weight:100;" id="createTime"></label>
    </p>

    <div class="blue-container clear">
        <h3>微应用功能描述：</h3>
        <p id="appFunction">
        </p>
    </div>
</div>
</body>

<script type="text/javascript">
    var pageDesc = decodeURI('${pageDesc}');
    $(document).on("ready", function () {
        $("#appFunction").html(pageDesc);
        Date.prototype.Format = function (fmt) { //author: meizz
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        };

        $("#appFunction").html(pageDesc);
        var createTime = new Date(${param.createTime}).Format("yyyy-MM-dd hh:mm:ss");
        $("#createTime").html(createTime);
    });
    function addToBasket() {
        var appuuid = '';
        var pageid = '${pageId}';
        var pagename = '${pageName}';
        var pageurl = '${pageUrl}';
        var appname = '${appName}';
        var appid = '${appId}';

        $.ajax({
            async: true,
            contentType: "application/json",
            type: 'get',
            dataType: "json",
            data: {
                appuuid: appuuid,
                pageid: pageid,
                pagename: encodeURI(pagename),
                pageurl: pageurl,
                appid: appid,
                appname: encodeURI(appname)
            },
            url: "${pageContext.request.contextPath}/manager/basket/addToBasketNew",
            error: function () {//请求失败处理函数
                window.parent.layer.alert("请求失败！");
            },
            success: function (res) { //请求成功后处理函数。
                if ("success" != res.result) {
                    layer.msg(res.reason, {icon: 5});
                } else {
                    layer.msg('加入篮子成功！', {icon: 1});
                }
            }
        });
    }

</script>
</html>
