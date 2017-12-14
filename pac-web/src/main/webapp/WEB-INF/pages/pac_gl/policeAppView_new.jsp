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
        <form name="form_search" action="">
            <input type="text" id="keyWord" value="${keyWord}"/>
            <div class="dropdown" style="display:inline-block;">
                <button id="search" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span class="glyphicon glyphicon-search"></span>&nbsp;
                    搜索
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" aria-labelledby="search">
                    <li><a href="javascript:getData('',1);" id="byApp">微应用</a></li>
                    <li><a href="javascript:getData('',2)" id="byfunction">功能</a></li>
                </ul>
            </div>
        </form>
    </div>
    <!--应用篮子-->
    <div class="my-basket" id="myBasket">
        <a class="basket-container" id="myBasketC" target="_blank" href="${pageContext.request.contextPath}/manager/menuAction/toIndex?flag=basket">
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
        <a href="javascript:void(0);" onclick="javascript:commonSearch('数据编码状态统计');">数据编码状态统计 </a>
        <a href="javascript:void(0);" onclick="javascript:commonSearch('数据表状态统计');">数据表状态统计 </a>
        <a href="javascript:void(0);" onclick="javascript:commonSearch('在用数据项统计');">在用数据项统计 </a>
        <a href="javascript:void(0);" onclick="javascript:commonSearch('数据项状态统计');">数据项状态统计 </a>
    </div>
</div>
<!--main-->
<div class="w clear">
    <!-- 应用筛选 -->
    <form action="${pageContext.request.contextPath}/manager/basket/getData"
          name="form1" method="post">
        <input type="hidden" name="appId" value="">
        <input type="hidden" name="searchType" value="${searchType}">
        <input type="hidden" name="keyWord" value="">
        <input type="hidden" name="appName" value="">
        <input type="hidden" name="funcName" value="">
        <div class="selector">
            <div class="filter-title">
                <h3><span class="txt-blue00">警务微应用市场</span>&nbsp;&nbsp;微应用筛选</h3>
                <div class="st-ext">共&nbsp;<span id="appCount">${fn:length(appName)}</span>个警务微应用</div>
            </div>
            <!--应用分类-->
            <div class="selector-row">
                <div class="sel-key">
                    <span>微应用分类：</span>
                </div>
                <div class="sel-value">
                    <div class="sel-v-list">
                        <ul class="J_valueList">
                            <c:forEach items="${appName}" var="a">
                                <li>
                                    <a href="javascript:void(0)"
                                       onclick="javascript:getData('${a.appId}',3);">${a.appName}</a>
                                </li>
                            </c:forEach>

                        </ul>
                    </div>
                </div>
            </div>
            <!--面向用户-->
            <%--<div class="selector-row">--%>
            <%--<div class="sel-key">--%>
            <%--<span>面向用户：</span>--%>
            <%--</div>--%>
            <%--<div class="sel-value">--%>
            <%--<div class="sel-v-list">--%>
            <%--<ul class="J_valueList">--%>
            <%--<li>--%>
            <%--<a href="javascript:void(0)">消防 </a>--%>
            <%--</li>--%>
            <%--<li>--%>
            <%--<a href="javascript:void(0)">刑警 </a>--%>
            <%--</li>--%>
            <%--<li>--%>
            <%--<a href="javascript:void(0)">交通 </a>--%>
            <%--</li>--%>
            <%--<li>--%>
            <%--<a href="javascript:void(0)">民政 </a>--%>
            <%--</li>--%>
            <%--</ul>--%>
            <%--</div>--%>
            <%--</div>--%>
            <%--</div>--%>
        </div>
    </form>

    <div class="main-container clear">
        <div class="list">
            <div class="ml-wrap">
                <div class="filter">
                    <div class="f-line top">
                        <!--排序-->
                        <div class="f-sort" id="sortBy">
                            <a href="javascript:void(0)" class="curr">组装次数<i></i></a>
                            <%--<a href="javascript:void(0)">上架时间<i></i></a>--%>
                            <%--<a href="javascript:void(0)">评论数<i></i></a>--%>
                        </div>
                        <!--上方分页-->
                        <div class="f-page">
                            <span class="fp-text" id="pageTop"></span>
                            <%--<a class="fp-prev disabled" id="topPrev" href="javascript:void(0)"> &lt; </a>--%>
                            <%--<a class="fp-next" id="topNext" href="javascript:void(0)"> &gt; </a>--%>
                        </div>
                    </div>
                </div>
                <!--右侧应用列表-->
                <div class="app-list clear">
                    <ul id="appList">
                        <c:forEach var="page" items="${pagesList}">
                            <li appid="001">
                                <div class="p-img">
                                    <a href="javascript:void(0);" title="${page.pagename}"
                                       onclick="javascript:pageDetail('${page.id}','${page.appname}','${page.composenum}','${page.pagedesc}','${page.pagename}','${page.pageurl}','${page.appid}')">
                                        <%--<c:if test="${empty page.imgUrl}">--%>
                                        <%--<img width="190" height="190" data-img="1"--%>
                                             <%--src="${pageContext.request.contextPath}/market/img/moren.jpg"></a>--%>
                                    <%--</c:if>--%>
                                    <%--<c:if test="${not empty page.imgUrl}">--%>
                                        <%--<img width="190" height="190" data-img="1"--%>
                                             <%--src="${page.imgUrl}"></a>--%>
                                    <%--</c:if>--%>

                                    <img width="190" height="190" data-img="1"
                                         src="${pageContext.request.contextPath}/market/img/moren.png"></a>

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
                                    <a href="javascript:void(0);" title="${page.pagename}"
                                       onclick="javascript:pageDetail('${page.id}','${page.appname}','${page.composenum}','${page.pagedesc}','${page.appname}','${page.pageurl}','${page.appid}')">
                                        <em>${page.pagename}</em> </a><br>
                                    <cite>${page.pagedesc}</cite></div>
                                <div class="p-use"> 已有<span class="txt-blue0">${page.composenum}</span>人组装</div>
                                <div class="p-tool">
                                    <button type="button" class="btn btn-sm btn-primary"
                                            title="加入微应用篮子"
                                            onclick="addToBasket('${page.appuuid}','${page.id}','${page.pagename}','${page.pageurl}','${page.appid}','${page.appname}');">
                                        加入微应用篮子
                                    </button>
                                </div>
                            </li>
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
                    <li appid="001">
                        <div class="p-img">
                            <a href="javascript:void(0);"
                               onclick="javascript:pageDetail('${page.id}','${page.appname}','${page.composenum}','${page.pagedesc}','${page.pagename}','${page.pageurl}','${page.appid}')"
                               title="${page.pagename}">
                                <img width="190" height="190" data-img="1"
                                     src="${pageContext.request.contextPath}/market/img/moren.png"></a></div>
                        <div class="p-score"><img src="${pageContext.request.contextPath}/market/img/star.png">
                            <img src="${pageContext.request.contextPath}/market/img/star.png">
                            <img src="${pageContext.request.contextPath}/market/img/star.png">
                            <img src="${pageContext.request.contextPath}/market/img/star.png">
                            <img src="${pageContext.request.contextPath}/market/img/star.png"></div>
                        <div class="p-name">
                            <a href="javascript:void(0);"
                               onclick="javascript:pageDetail('${page.id}','${page.appname}','${page.composenum}','${page.pagedesc}','${page.pagename}','${page.pageurl}','${page.appid}')"
                               title="${page.pagename}">
                                <em>${page.pagename}</em> </a><br> <cite>${page.pagedesc}</cite></div>
                        <div class="p-use"> 已有<span class="txt-blue0">${page.composenum}</span>人组装</div>
                        <div class="p-tool">
                            <button
                                    onclick="addToBasket('${page.appuuid}','${page.id}','${page.appname}','${page.pageurl}','${page.appid}','${page.pagename}');"
                                    type="button" class="btn btn-sm btn-primary" title="加入微应用篮子">加入微应用篮子
                            </button>
                        </div>
                    </li>
                </c:forEach>

            </ul>
        </div>
    </div>
</div>
<div class="clear" style="margin-bottom: 40px;"></div>
</body>
<script type="text/javascript">
    var keyword = "";
    var pageNum = 1;
    var pageCount;
    var sortBy = 0;  //0,1,2分别对应组装次数，上架时间，评论数
    $(document).on("ready", function () {
        getCountForBasket();
        //获取热门搜索关键字
        <%--$.ajax({--%>
        <%--dataType: "json",--%>
        <%--//type: "POST",--%>
        <%--url: "${pageContext.request.contextPath}/market/data/hotsearch.json",--%>
        <%--cache: false,--%>
        <%--success: function(res) {--%>
        <%--if(res.success){--%>
        <%--var innerStr = "";--%>
        <%--for(var i=0;i<res.data.length;i++){--%>
        <%--innerStr += '<a href="javascript:void(0)">'+res.data[i].keyword+'</a>';--%>
        <%--}--%>
        <%--$("#hotSearch").html(innerStr);--%>
        <%--}else{--%>
        <%--console.log("获取应用列表失败！");--%>
        <%--}--%>
        <%--},--%>
        <%--error: function() {--%>
        <%--layer.alert("获取应用列表失败！");--%>
        <%--}--%>
        <%--});--%>

        //获取精选应用
        <%--$.ajax({--%>
        <%--dataType: "json",--%>
        <%--//type: "POST",--%>
        <%--url: "${pageContext.request.contextPath}/market/data/appGood.json",--%>
        <%--cache: false,--%>
        <%--success: function(res) {--%>
        <%--if(res.success){--%>
        <%--var innerStr = "";--%>
        <%--for(var i=0;i<res.data.length;i++){--%>
        <%--var starNum = Math.floor(res.data[i].score);--%>
        <%--var stars = "";--%>
        <%--var halfStar = "";--%>
        <%--if(starNum<res.data[i].score){--%>
        <%--halfStar = '<img src="img/half-star.png" />';--%>
        <%--}--%>
        <%--for(var j=0;j<starNum;j++){--%>
        <%--stars += '<img src="img/star.png" />';--%>
        <%--}--%>
        <%--innerStr += '<li appId="'+res.data[i].id+'">' +--%>
        <%--'	 <div class="p-img">' +--%>
        <%--'		 <a href="appDetail.html#'+res.data[i].id+'" target="_blank" title="'+res.data[i].name+'"><img width="190" height="190" data-img="1" src="'+res.data[i].img+'"></a>' +--%>
        <%--'	 </div>' +--%>
        <%--'  <div class="p-score">'+stars+halfStar+'</div>' +--%>
        <%--'  <div class="p-name">' +--%>
        <%--'  	 <a href="appDetail.html#'+res.data[i].id+'" target="_blank" title="'+res.data[i].name+'"> <em>'+res.data[i].name+'</em> </a><br />' +--%>
        <%--'  	 <cite>'+res.data[i].intro+'</cite>' +--%>
        <%--'  </div>' +--%>
        <%--'  <div class="p-use"> 已有<span class="txt-blue0">'+res.data[i].use+'</span>人组装 </div>' +--%>
        <%--'  <div class="p-tool"><button type="button" class="btn btn-sm btn-primary">加入应用篮子</button></div>' +--%>
        <%--'</li>';--%>
        <%--}--%>
        <%--$("#appGood").html(innerStr);--%>
        <%--}else{--%>
        <%--console.log("获取精选应用失败！");--%>
        <%--}--%>
        <%--},--%>
        <%--error: function() {--%>
        <%--layer.alert("获取精选应用失败！");--%>
        <%--}--%>
        <%--});--%>

        //query(1);

//        //搜索
//        $("#search").on("click",function(){
//            keyword = encodeURI($.trim($("#keyword").val()));
//            query(1);
//        });

        //热门搜索
//        $("#hotSearch").on("click","a",function(e){
//            var a = $(e.currentTarget);
//            keyword = encodeURI(a.html());
//            query(1);
//        });

        //我的应用篮子
//        $("#myBasket").on("mouseenter",function(){
//            $("#myBasketC").css({
//                backgroundColor: "#fff",
//                borderBottomWidth:0
//            });
//            $("#dorpdownLayer").show();
//        });
//
//        $("#myBasket").on("mouseleave",function(){
//            $("#myBasketC").css({
//                backgroundColor: "#f9f9f9",
//                borderBottomWidth:"1px"
//            });
//            $("#dorpdownLayer").hide();
//        });

        //上方翻页
        $("#topPrev").on("click", function (e) {
            if (pageNum == 1) {
                return;
            }
            pageNum--;
            query(pageNum);
        });
        $("#topNext").on("click", function (e) {
            if (pageNum == pageCount) {
                return;
            }
            pageNum++;
            query(pageNum);
        });

        //切换排序方式
        $("#sortBy").find("a").on("click", function () {
            if ($(this).hasClass("curr")) {
                return;
            }
            sortBy = $(this).index();
            $("#sortBy").find(".curr").removeClass("curr");
            $(this).addClass("curr");
            query(1);
        })
    });

    <%--function query(curr){--%>
    <%--var paramObj = {--%>
    <%--pageSize:10,--%>
    <%--pageNum:curr,--%>
    <%--sortBy:sortBy--%>
    <%--};--%>
    <%--if(keyword != ""){--%>
    <%--paramObj.keyword = keyword;--%>
    <%--}--%>
    <%--&lt;%&ndash;$.ajax({&ndash;%&gt;--%>
    <%--&lt;%&ndash;dataType: "json",&ndash;%&gt;--%>
    <%--&lt;%&ndash;//type: "POST",&ndash;%&gt;--%>
    <%--&lt;%&ndash;url: "${pageContext.request.contextPath}/market/data/appList.json",&ndash;%&gt;--%>
    <%--&lt;%&ndash;data:paramObj,&ndash;%&gt;--%>
    <%--&lt;%&ndash;cache: false,&ndash;%&gt;--%>
    <%--&lt;%&ndash;success: function(res) {&ndash;%&gt;--%>
    <%--&lt;%&ndash;if(res.success){&ndash;%&gt;--%>
    <%--&lt;%&ndash;var innerStr = "";&ndash;%&gt;--%>
    <%--&lt;%&ndash;//填充上方页数&ndash;%&gt;--%>
    <%--&lt;%&ndash;$("#pageTop").html('<b>'+res.pageNum+'</b><em>/</em><i>'+res.pages+'</i>');&ndash;%&gt;--%>
    <%--&lt;%&ndash;pageNum = res.pageNum;&ndash;%&gt;--%>
    <%--&lt;%&ndash;pageCount = res.pages;&ndash;%&gt;--%>
    <%--&lt;%&ndash;//设置上方分页按钮disabled&ndash;%&gt;--%>
    <%--&lt;%&ndash;if(pageNum == 1 || pageNum == pageCount){&ndash;%&gt;--%>
    <%--&lt;%&ndash;$("#topPrev").addClass("disabled");&ndash;%&gt;--%>
    <%--&lt;%&ndash;}else{&ndash;%&gt;--%>
    <%--&lt;%&ndash;$("#topPrev").removeClass("disabled");&ndash;%&gt;--%>
    <%--&lt;%&ndash;}&ndash;%&gt;--%>
    <%--&lt;%&ndash;if(pageNum == pageCount){&ndash;%&gt;--%>
    <%--&lt;%&ndash;$("#topNext").addClass("disabled");&ndash;%&gt;--%>
    <%--&lt;%&ndash;}else{&ndash;%&gt;--%>
    <%--&lt;%&ndash;$("#topNext").removeClass("disabled");&ndash;%&gt;--%>
    <%--&lt;%&ndash;}&ndash;%&gt;--%>
    <%--&lt;%&ndash;//填充&ndash;%&gt;--%>
    <%--&lt;%&ndash;for(var i=0;i<res.data.length;i++){&ndash;%&gt;--%>
    <%--&lt;%&ndash;var starNum = Math.floor(res.data[i].score);&ndash;%&gt;--%>
    <%--&lt;%&ndash;var stars = "";&ndash;%&gt;--%>
    <%--&lt;%&ndash;var halfStar = "";&ndash;%&gt;--%>
    <%--&lt;%&ndash;if(starNum<res.data[i].score){&ndash;%&gt;--%>
    <%--&lt;%&ndash;halfStar = '<img src="img/half-star.png" />';&ndash;%&gt;--%>
    <%--&lt;%&ndash;}&ndash;%&gt;--%>
    <%--&lt;%&ndash;for(var j=0;j<starNum;j++){&ndash;%&gt;--%>
    <%--&lt;%&ndash;stars += '<img src="img/star.png" />';&ndash;%&gt;--%>
    <%--&lt;%&ndash;}&ndash;%&gt;--%>
    <%--&lt;%&ndash;innerStr += '<li appId="'+res.data[i].id+'">' +&ndash;%&gt;--%>
    <%--&lt;%&ndash;'	 <div class="p-img">' +&ndash;%&gt;--%>
    <%--&lt;%&ndash;'		 <a href="appDetail.html#'+res.data[i].id+'" target="_blank" title="'+res.data[i].name+'"><img width="190" height="190" data-img="1" src="'+res.data[i].img+'"></a>' +&ndash;%&gt;--%>
    <%--&lt;%&ndash;'	 </div>' +&ndash;%&gt;--%>
    <%--&lt;%&ndash;'  <div class="p-score">'+stars+halfStar+'</div>' +&ndash;%&gt;--%>
    <%--&lt;%&ndash;'  <div class="p-name">' +&ndash;%&gt;--%>
    <%--&lt;%&ndash;'  	 <a href="appDetail.html#'+res.data[i].id+'" target="_blank" title="'+res.data[i].name+'"> <em>'+res.data[i].name+'</em> </a><br />' +&ndash;%&gt;--%>
    <%--&lt;%&ndash;'  	 <cite>'+res.data[i].intro+'</cite>' +&ndash;%&gt;--%>
    <%--&lt;%&ndash;'  </div>' +&ndash;%&gt;--%>
    <%--&lt;%&ndash;'  <div class="p-use"> 已有<span class="txt-blue0">'+res.data[i].use+'</span>人组装 </div>' +&ndash;%&gt;--%>
    <%--&lt;%&ndash;'  <div class="p-tool"><button type="button" class="btn btn-sm btn-primary" title="加入应用篮子">加入应用篮子</button></div>' +&ndash;%&gt;--%>
    <%--&lt;%&ndash;'</li>';&ndash;%&gt;--%>
    <%--&lt;%&ndash;}&ndash;%&gt;--%>
    <%--&lt;%&ndash;$("#appList").html(innerStr);&ndash;%&gt;--%>
    <%--&lt;%&ndash;//分页&ndash;%&gt;--%>
    <%--&lt;%&ndash;laypage({&ndash;%&gt;--%>
    <%--&lt;%&ndash;cont: 'pageContainer', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>&ndash;%&gt;--%>
    <%--&lt;%&ndash;pages: res.pages, //通过后台拿到的总页数&ndash;%&gt;--%>
    <%--&lt;%&ndash;curr: curr || 1, //当前页&ndash;%&gt;--%>
    <%--&lt;%&ndash;skip: true, //是否开启跳页&ndash;%&gt;--%>
    <%--&lt;%&ndash;first: 1, //将首页显示为数字1,。若不显示，设置false即可&ndash;%&gt;--%>
    <%--&lt;%&ndash;last: res.pages, //将尾页显示为总页数。若不显示，设置false即可&ndash;%&gt;--%>
    <%--&lt;%&ndash;skin: '#428bca',&ndash;%&gt;--%>
    <%--&lt;%&ndash;jump: function(obj, first) { //触发分页后的回调&ndash;%&gt;--%>
    <%--&lt;%&ndash;if(!first) { //点击跳页触发函数自身，并传递当前页：obj.curr&ndash;%&gt;--%>
    <%--&lt;%&ndash;query(obj.curr);&ndash;%&gt;--%>
    <%--&lt;%&ndash;}&ndash;%&gt;--%>
    <%--&lt;%&ndash;}&ndash;%&gt;--%>
    <%--&lt;%&ndash;});&ndash;%&gt;--%>
    <%--&lt;%&ndash;}else{&ndash;%&gt;--%>
    <%--&lt;%&ndash;console.log("获取热门搜索失败！");&ndash;%&gt;--%>
    <%--&lt;%&ndash;}&ndash;%&gt;--%>
    <%--&lt;%&ndash;},&ndash;%&gt;--%>
    <%--&lt;%&ndash;error: function() {&ndash;%&gt;--%>
    <%--&lt;%&ndash;layer.alert("获取热门搜索失败！");&ndash;%&gt;--%>
    <%--&lt;%&ndash;}&ndash;%&gt;--%>
    <%--&lt;%&ndash;});&ndash;%&gt;--%>
    <%--}--%>

    function getData(appId, searchType) {
        //all param include:keyword,search_type,appname(appid),
        $("input[name='appId']").val(appId);
        if (searchType != 3) {
            $("input[name='searchType']").val(searchType);
        }
        $("input[name='keyWord']").val(encodeURI($("#keyWord").val()));

        form1.submit();
    }

    //常用搜索
    function commonSearch(keyWord) {
//        alert(keyWord);
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

    function pageDetail(pageId, appName, composeNum, pageDesc, pageName, pageUrl, appId) {
//            alert(pageId+' '+appName+' '+composeNum+' '+pageDesc+' '+pageName+' '+pageUrl);
        window.open('${pageContext.request.contextPath}/manager/menuAction/pageDetail?pageId=' + pageId + '&appName=' + encodeURI(encodeURI(appName)) + '&composeNum=' + composeNum + '&pageDesc=' + encodeURI(encodeURI(pageDesc)) + '&pageName=' + encodeURI(encodeURI(pageName)) + '&pageUrl=' + pageUrl + '&appId=' + appId, '_blank');
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