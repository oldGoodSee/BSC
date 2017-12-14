<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>警务微应用市场</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/market/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/market/css/main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/market/css/reset.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/market/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/market/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/market/js/laypage-v1.3/laypage/laypage.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/market/js/layer/layer.js"></script>

</head>

<body>
<!--个人信息-->
<div class="topbar">
    <div class="w">
        <ul class="fr topbar-r-list">
            <li>你好，<span id="userName">${sessionUserInfo.userName }</span>&nbsp;</li>
            <li class="spacer"></li>
            <%--<li>--%>
            <%--<a target="_blank" href="javascript:void(0)">我的微应用篮子</a>--%>
            <%--</li>--%>
            <li class="spacer"></li>
            <li>
                <a href="javascript:void(0);" onclick="loginCasOut()">退出</a>
            </li>
        </ul>
    </div>
</div>
<!--search-->
<div class="w clear">
    <div class="logo">
        <a href="javascript:void(0)"></a>
    </div>
    <div class="searchbar">
        <!--搜索-->
        <form name="form_search" action="" onkeydown="if(event.keyCode==13)return false;">
            <input type="text" id="keyWord" value="${keyWord}"/>
            <div class="dropdown" style="display:inline-block;">
                <button id="search" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                        onclick="getData('',2)">
                    <span class="glyphicon glyphicon-search"></span>&nbsp;
                    搜索
                </button>
            </div>
        </form>
    </div>
    <!--应用篮子-->
    <div class="my-basket" id="myBasket">
        <a class="basket-container" id="myBasketC" target="_blank"
           href="${pageContext.request.contextPath}/manager/menuAction/toIndex?flag=basket">
            <i class="icon-l"></i>
            <i class="icon-r">&gt;</i>
            <i class="count" id="shopping-amount">0</i>
            <span>我的微应用篮子</span>
        </a>
        <!--应用篮子下拉-->
        <div class="dorpdown-layer" id="dorpdownLayer">
            <div class="spacer"></div>
            <div id="settleup-content"><span class="loading"></span></div>
        </div>
    </div>
    <div class="hotwords" id="hotSearch">
        <c:forEach var="page" items="${bestPagesList}">
            <a href="javascript:void(0);" onclick="javascript:commonSearch('${page.mAppName}');">${page.mAppName}</a>
        </c:forEach>
    </div>
</div>
<!--main-->
<div class="w clear">
    <!-- 应用筛选 -->
    <form action="${pageContext.request.contextPath}/manager/menuAction/getMarketData"
          name="form" method="post">
        <input type="hidden" name="localAppName" value="">
        <input type="hidden" name="command" value="">
        <input type="hidden" name="pageSize" value="">
        <input type="hidden" name="pageNum" value="">
        <div class="selector">
            <div class="filter-title">
                <h3><span class="txt-blue00">警务微应用市场</span>&nbsp;&nbsp;微应用筛选</h3>
                <div class="st-ext">共&nbsp;<span id="appCount">${total}</span>个警务微应用</div>
            </div>
            <!--应用分类-->
            <div class="selector-row">
                <div class="sel-key">
                    <span>微应用分类：</span>
                </div>
                <div class="sel-value">
                    <div class="sel-v-list">
                        <ul class="J_valueList">
                            <c:forEach items="${appNameList}" var="appName">
                                <c:if test="${not empty appName}">
                                    <li>
                                        <a href="javascript:void(0)"
                                           onclick="javascript:getData('${appName.localAppName}',3);">${appName.localAppName}</a>
                                    </li>
                                </c:if>
                            </c:forEach>

                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <div class="main-container clear">
        <div class="list">
            <div class="ml-wrap">
                <div class="filter">
                    <div class="f-line top">
                        <!--排序-->
                        <div class="f-sort" id="sortBy">
                            <a href="javascript:void(0)" class="curr">组装次数 &nbsp<i><span
                                    class="glyphicon glyphicon-arrow-down"></span></i></a>
                            <%--<a href="javascript:void(0)">上架时间<i></i></a>--%>
                            <%--<a href="javascript:void(0)">评论数<i></i></a>--%>
                        </div>
                        <!--上方分页-->
                        <div class="f-page">
                            <span class="fp-text" id="pageTop"></span>
                            <a class="fp-prev" id="topPrev" href="javascript:void(0)"> &lt; </a>
                            <a class="fp-next" id="topNext" href="javascript:void(0)"> &gt; </a>
                        </div>
                    </div>
                </div>
                <!--右侧应用列表-->
                <div class="app-list clear">
                    <ul id="appList">
                        <c:forEach var="page" items="${pagesList}">
                            <c:forEach var="img" items="${page.mAppImg}">
                                <li appid="001">
                                    <div class="p-img">

                                        <a href="javascript:void(0);" title="${page.mAppName}"
                                           onclick="javascript:pageDetail('${page.mAppId}','${page.localAppName}','${page.mAppAssembleTimes}'
                                                   ,'${page.mAppDesc}','${page.mAppName}','${page.deployUrl}${page.visitUrl}','${page.localAppId}','${page.createTime}','${img.mAppImg}')">
                                            <c:choose>
                                            <c:when test='${fn:contains(img.mAppImg,"http")}'>
                                            <img width="190" height="190" data-img="1"
                                                 src='${img.mAppImg}'>
                                            </c:when>
                                            <c:otherwise>
                                            <img width="190" height="190" data-img="1"
                                                 src='${fastDFSUrl}${img.mAppImg}'>
                                            </c:otherwise>
                                            </c:choose>
                                    </div>
                                    <div class="p-score">
                                        <img src="${pageContext.request.contextPath}/market/img/star.png"><img
                                            src="${pageContext.request.contextPath}/market/img/star.png">
                                        <img src="${pageContext.request.contextPath}/market/img/star.png"><img
                                            src="${pageContext.request.contextPath}/market/img/star.png">
                                        <img src="${pageContext.request.contextPath}/market/img/star.png">
                                            <%--<img src="/market/img/half-star.png">--%>
                                    </div>
                                    <div class="p-name">
                                        <a href="javascript:void(0);" title="${page.mAppName}"
                                           onclick="javascript:pageDetail('${page.mAppId}','${page.localAppName}','${page.mAppAssembleTimes}'
                                                   ,'${page.mAppDesc}','${page.mAppName}','${page.deployUrl}${page.visitUrl}','${page.localAppId}','${page.createTime}','${img.mAppImg}')">
                                            <em>${page.mAppName}</em> </a><br>
                                        <cite>${page.mAppDesc}</cite></div>
                                    <div class="p-use"> 已有<span class="txt-blue0">${page.mAppAssembleTimes}</span>人组装
                                    </div>
                                    <div class="p-tool">
                                        <button type="button" class="btn btn-sm btn-primary"
                                                title="加入微应用篮子"
                                                onclick="addToBasket('${page.id}','${page.mAppId}','${page.mAppName}','${page.deployUrl}${page.visitUrl}','${page.localAppId}','${page.localAppName}');">
                                            加入微应用篮子
                                        </button>
                                    </div>
                                </li>
                            </c:forEach>
                        </c:forEach>
                    </ul>
                </div>
                <!--分页-->
                <div class="clear">
                    <div class="fr" style="margin-top: 20px;" id="pageContainer"></div>
                </div>
            </div>
        </div>
        <!--精选应用-->
        <div class="sidebar" style="background-color: #D7ECF9; border: 1px solid #A8D7F2;">
            <h3>微精选应用</h3>
            <ul id="appGood">
                <c:forEach var="page" items="${bestPagesList}">
                    <c:forEach var="img" items="${page.mAppImg}">
                        <li appid="001">
                            <div class="p-img">
                                <a href="javascript:void(0);"
                                   onclick="javascript:pageDetail('${page.mAppId}','${page.localAppName}','${page.mAppAssembleTimes}'
                                           ,'${page.mAppDesc}','${page.mAppName}','${page.deployUrl}${page.visitUrl}','${page.localAppId}','${page.createTime}','${img.mAppImg}')"
                                   title="${page.mAppName}">
                                    <c:choose>
                                        <c:when test="${fn:contains(img.mAppImg,'http')}">
                                            <img width="190" height="190" data-img="1"
                                                 src='${img.mAppImg}'>
                                        </c:when>
                                        <c:otherwise>
                                            <img width="190" height="190" data-img="1"
                                                 src='${fastDFSUrl}${img.mAppImg}'>
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </div>
                            <div class="p-score"><img src="${pageContext.request.contextPath}/market/img/star.png">
                                <img src="${pageContext.request.contextPath}/market/img/star.png">
                                <img src="${pageContext.request.contextPath}/market/img/star.png">
                                <img src="${pageContext.request.contextPath}/market/img/star.png">
                                <img src="${pageContext.request.contextPath}/market/img/star.png"></div>
                            <div class="p-name">
                                <a href="javascript:void(0);"
                                   onclick="javascript:pageDetail('${page.mAppId}','${page.localAppName}','${page.mAppAssembleTimes}'
                                           ,'${page.mAppDesc}','${page.mAppName}','${page.deployUrl}${page.visitUrl}','${page.localAppId}','${page.createTime}','${img.mAppImg}')"
                                   title="${page.mAppName}">
                                    <em>${page.mAppName}</em> </a><br> <cite>${page.mAppDesc}</cite></div>
                            <div class="p-use"> 已有<span class="txt-blue0">${page.mAppAssembleTimes}</span>人组装</div>
                            <div class="p-tool">
                                <button
                                        onclick="addToBasket('${page.id}','${page.mAppId}','${page.mAppName}','${page.deployUrl}${page.visitUrl}','${page.localAppId}','${page.localAppName}');"
                                        type="button" class="btn btn-sm btn-primary" title="加入微应用篮子">加入微应用篮子
                                </button>
                            </div>
                        </li>
                    </c:forEach>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
<div class="clear" style="margin-bottom: 40px;"></div>
</body>
<script type="text/javascript">
    var pageNum = ${appInfoJson.page.pageNum};
    var pageSize = ${appInfoJson.page.pageSize};
    var pages = ${appInfoJson.page.pages};
    var sortBy = 0;  //0,1,2分别对应组装次数，上架时间，评论数
    $(document).on("ready", function () {

        var command = '${command}';
        if ('' != command && command != null && command != 'null') {
            $("#keyWord").val(command);
        }
        getCountForBasket();
        if (pages > 1) {
            if (pageNum == 1) {
                $("#topPrev").addClass("disabled");
                $("#topNext").find(".disabled").removeClass("disabled");
            }
            if (pageNum != 1) {
                $("#topPrev").find(".disabled").removeClass("disabled");
            }
            if (pages == pageNum) {
                $("#topNext").addClass("disabled");
            }
            //上方翻页
            $("#topPrev").on("click", function (e) {
                if (pageNum == 1) {
                    return;
                }
                pageNum--;
                query(pageNum);
            });
            $("#topNext").on("click", function (e) {
                if (pageNum == pageSize) {
                    return;
                }
                pageNum++;
                query(pageNum);
            });
        } else {
            $("#topPrev").addClass("disabled");
            $("#topNext").addClass("disabled");
        }

        //切换排序方式
        $("#sortBy").find("a").on("click", function () {
            if ($(this).hasClass("curr")) {
                return;
            }
            sortBy = $(this).index();
            $("#sortBy").find(".curr").removeClass("curr");
            $(this).addClass("curr");
            query(1);
        });

        laypage({
            cont: 'pageContainer', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
            pages: pages, //通过后台拿到的总页数
            curr: pageNum, //当前页
            skin: '#428bca',
            groups: 2,
            first: 1,
            last: pages,
            prev: '<', //若不显示，设置false即可
            next: '>',
            jump: function (obj, first) { //触发分页后的回调
                if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                    query(obj.curr);
                }
            }
        });
    });

    function query(pageNum) {
        $("input[name='pageSize']").val(pageSize);
        $("input[name='pageNum']").val(pageNum);
        form.submit();
    }
    function getData(searchValue, searchType) {

        if (parseInt(searchType) === 3) {
            $("input[name='localAppName']").val(encodeURI(searchValue));
        }
        if (parseInt(searchType) === 1) {
            $("input[name='command']").val(searchValue);
        }
        if (parseInt(searchType) === 2) {
            $("input[name='command']").val(encodeURI($("#keyWord").val()));
        }

        form.submit();
    }

    //常用搜索
    function commonSearch(keyWord) {
        $("#keyWord").val(keyWord);
        getData('', 2);
    }

    function addToBasket(appuuid, pageid, pagename, pageurl, appid, appname) {

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
                    getCountForBasket();
                }
            }
        });
    }

    function getCountForBasket() {
        $.ajax({
            async: true,
            contentType: "application/json",
            type: 'get',
            url: "${pageContext.request.contextPath}/manager/basket/getCountForBasket",
            error: function () {//请求失败处理函数
                window.parent.layer.alert("请求失败！");
            },
            success: function (res) { //请求成功后处理函数。
                var count = res.basketCount;
                $('#shopping-amount').html(count);
            }
        });
    }

    function pageDetail(pageId, appName, composeNum, pageDesc, pageName, pageUrl, appId, createTime, imgUrl) {
        debugger;
        if (!(imgUrl.indexOf("http") >= 0)) {
            imgUrl = '${fastDFSUrl}' + imgUrl;
        }
        window.open('${pageContext.request.contextPath}/manager/menuAction/pageDetail?pageId=' + pageId + '&appName=' + encodeURI(encodeURI(appName)) + '&composeNum=' + composeNum + '&pageDesc=' + encodeURI(encodeURI(pageDesc)) + '&pageName=' + encodeURI(encodeURI(pageName)) + '&pageUrl=' + pageUrl + '&appId=' + appId + '&createTime=' + createTime + '&imgUrl=' + imgUrl, '_self');
    }

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


</script>

</html>