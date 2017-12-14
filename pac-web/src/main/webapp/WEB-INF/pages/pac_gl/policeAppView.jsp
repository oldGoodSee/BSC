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
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/pac_gl/style/sub.css"/>
    <style type="text/css">
        #appInfoBody td {
            vertical-align: middle;
        }

        #functionTable td {
            vertical-align: middle;
        }

        .tableHeight {
            height: 80%;
        }

        .trDisplay {
            display: none;
        }

        .trColor {
            background-color: #ddd;
        }

        .btnBgImgIn {
            width: 40px;
            height: 40px;
            background-image: url(${pageContext.request.contextPath}/pac_gl/images/in.png);
            background-size: 100%;
        }

        .btnBgImgout {
            width: 40px;
            height: 40px;
            background-image: url(${pageContext.request.contextPath}/pac_gl/images/out.png);
            background-size: 100%;
        }

        .newLabel {
            color: #0c91e5;
            font-size: 14px;
        }

    </style>
</head>
<body>
<div>
    <div class="layout-main" style="margin-top: 20px;">
        <div class="table-toolbar" style="margin-left: 10px;float: left">
            <form class="form-inline" onkeydown="if(event.keyCode==13)return false;">
                <%--<div style="position: relative;left: 100px;">--%>
                <%--<label class="control-label"> 已存在的组装应用&nbsp;：</label>--%>
                <%--<label style="display: none" id="displayLabel"> 无 </label>--%>
                <%--<select id="PacAppSelect" class="form-control">--%>
                <%--</select>--%>
                <%--</div>--%>
            </form>
            <form class="form-inline text-left" onkeydown="if(event.keyCode==13)return false;">
                <select id="statusSelect" class="form-control">
                    <option id="1" value="1" selected="selected">应用</option>
                    <option id="2" value="2">功能</option>
                </select>
                <input id="appNameInput" class="form-control" onkeydown="if(event.keyCode==13)return query();">
                <input type="button" id="queryButton" class="btn btn-primary" onclick="query()" value="查&nbsp;询">
            </form>
        </div>
        <br>
        <div class="layout-table-container">
            <input type="button" value="加入组装清单" class="btn-link"
                   style="float: right" onclick="addText()">
            <table class="table">
                <thead>
                <tr>
                    <th width="10%">查看功能页面</th>
                    <th width="20%">应用名称</th>
                    <th width="30%">应用描述</th>
                    <th width="20%">版本</th>
                    <th width="20%">运行状态</th>
                </tr>
                </thead>
                <tbody id="appInfoBody">
                </tbody>
            </table>
        </div>
        <ul class="pagination layout-page" id="pagination" style="float: right;"></ul>
        <input type="button" value="加入组装清单" class="btn-link"
               style="position: fixed;right: 30px;bottom: 80px;" onclick="addText()">
    </div>
    <%--modal--%>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
         aria-labelledby="mySmallModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="form-inline">
                        <h2>您已选择的功能页面&nbsp;：</h2>
                        <div id="pageDivView">
                        </div>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" style="float: left" data-dismiss="modal"
                            onclick="goAddFuncList()">确认
                    </button>
                    <button type="button" class="btn btn-default" style="float: right" data-dismiss="modal">取消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
</body>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/pac_gl/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/pac_gl/js/jqPaginator.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
    var appId, appName, status, appData, length, pages, currentPage, funcName;
    var msg = "${appInfoJson.msg}";
    appData = ${appInfoJson.data};
    var functionList = [];
    $(document).on("ready", function () {
        if (msg == "error") {
            window.parent.layer.alert("${appInfoJson.reason}");
            return;
        }
        pagination();
        appSelect();
    });
    function isIE() {
        if (!!window.ActiveXObject || "ActiveXObject" in window)
            return true;
        else
            return false;
    }
    function addText() {

        if (null == functionList || functionList.length == 0) {
            window.parent.layer.alert("必须选择一个或者多个功能页面!");
            return false;
        }
//        var obj = document.getElementById('PacAppSelect');

        var pageStr = "";
        var pageDivView = $("#pageDivView");
        pageDivView.empty();
        for (var i = 0; i < functionList.length; i++) {
            pageStr += "<label class='newLabel'>" + functionList[i].split(",")[1] + "</label><br>";
        }
        if (functionList.length > 10) {
            pageDivView.attr("style", "height:40%;overflow-y:auto;");
        }
        pageDivView.append(pageStr);
        $('#myModal').modal({backdrop: 'static'});
    }
    function goAddFuncList() {
        var NewPacApp = {
            appId: appId,
            funcList: functionList
        }
        var test = JSON.stringify(NewPacApp);
        test = encodeURI(test);
        $.ajax({
            async: true,
            contentType: "application/json",
            type: 'get',
            dataType: "json",
            data: {pageInfo: test},
            url: "${pageContext.request.contextPath}"
            + "/manager/basket/addToBasket",
            error: function () {//请求失败处理函数
                window.parent.layer.alert("请求失败！");
            },
            success: function (res) { //请求成功后处理函数。
                if ("success" != res.result) {
                    window.parent.layer.msg('加入失败!', {icon: 5});
                } else {
                    window.parent.layer.confirm('加入成功，是否去查看组装清单？', {
                        btn: ['是', '否']
                    }, function (index, layero) {
                        window.parent.layer.closeAll();
                        window.parent.menuClick("market");
                        window.parent.menuClick("viewList");
                    }, function (index) {
                        window.parent.layer.msg('成功!', {icon: 1});
                        window.parent.menuClick("market");
                    });
                }
            }
        });
    }
    function appSelect() {
        var pacAppData = ${appBaseInfoJsonArray};
        var PacAppSelectDocM = $("#PacAppSelect");
        if (pacAppData.length == 0) {
            PacAppSelectDocM.attr("style", "display:none");
            PacAppSelectDocM.attr("style", "");
        }
        PacAppSelectDocM.empty();
        var pacAppSelect = "";
        for (var i = 0; i < pacAppData.length; i++) {
            if (i == 0) {
                pacAppSelect = "<option id='" + i + "' value='" + pacAppData[i].appId + "' selected>" + pacAppData[i].appName + "</option>";
            } else {
                pacAppSelect += "<option id='" + i + "' value='" + pacAppData[i].appId + "'>" + pacAppData[i].appName + "</option>";
            }
        }
        PacAppSelectDocM.append(pacAppSelect);
    }
    //分页
    function pagination() {
        length = appData.length;
        if (length % 10 == 0) {
            pages = parseInt(length / 10);
        } else {
            pages = parseInt(length / 10) + 1;
        }
        if (0 == pages) {
            pages = 1;
        }
        $.jqPaginator('#pagination', {
            totalPages: pages,
            visiblePages: 10,
            currentPage: 1,
            first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
            prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
            next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
            last: '<li class="last"><a href="javascript:void(0);">末页</a></li>',
            page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
            onPageChange: function (num) {
                dealPage(num);
            }
        });
    }
    //分页数据拼接处理
    function dealPage(num) {
        var pageNum;
        currentPage = num;
        if (num < pages) {
            pageNum = 10;
        } else {
            pageNum = length - (num - 1) * 10;
        }
        var appInfoBody = $("#appInfoBody");
        appInfoBody.html("");
        if (length == 0) {
            var str = "<tr><td colspan='6' style='text-align: center;'> 无记录 </td></tr>";
            appInfoBody.append(str);
        }
        var trString = "";
        for (var i = 0; i < pageNum; i++) {
            var currentLength = (num - 1) * 10 + i;
            var funcList = appData[currentLength].pages;
            if (funcList == 'null' || funcList == undefined) {
                funcList = [];
            }
            var linkList = "<button id='"
                + i + "_" + funcList.length
                + "' class='btn-link btnBgImgIn' onclick='funcList(id)'></button>"
            var appStatus = getStatus(appData[currentLength].status);
            var OappName = appData[currentLength].appName;
            var OappId = appData[currentLength].appId;
            var OappDesc = appData[currentLength].appDesc;
            var version = appData[currentLength].version;
            trString += "<tr><td>"
                + linkList + "</td><td>"
                + OappName + "</td><td>"
                + OappDesc + "</td><td>"
                + version + "</td><td>"
                + appStatus + "</td></tr>";
            var url = "";
            var deployments = appData[currentLength].deployments;
            if (undefined != deployments && "" != deployments && deployments.length > 0) {
                url = "http://" + deployments[0].ipAddr + ":" + deployments[0].port;
                if (isIE()) {
                    if (typeof String.prototype.startsWith != 'function') {
                        String.prototype.startsWith = function (prefix) {
                            return this.slice(0, prefix.length) === prefix;
                        };
                    }
                }
                if (deployments[0].initPath.startsWith("/")) {
                    url += deployments[0].initPath;
                } else {
                    url += "/" + deployments[0].initPath;
                }
            }
            var str = "";
            var pageIdList = '${pageIdList}';
            for (var j = 0; j < funcList.length; j++) {
                var disabled = "";
                var pageId = parseInt(funcList[j].id);
                if ($.inArray(pageId, pageIdList) != -1) {
                    disabled = "disabled checked";
                }
                var value = pageId + "," + funcList[j].pageName + "," + url + funcList[j].pageUrl + "," + OappId + "," + OappName;
                str += "<tr id='" + i + "tr_" + j + "' class='trDisplay trColor'><td></td><td></td><td>"
                    + funcList[j].pageName + "</td><td>"
                    + funcList[j].pageUrl + "</td><td>"
                    + "<input type='checkbox' id='" + "checkBox_" + funcList[j].id + "' value='" + value + "' onchange='checkBox(id,this.value)' " + disabled + "></td>";
            }
            trString += str;
        }
        appInfoBody.append(trString);
    }
    function checkBox(id, value) {
        //window.parent.layer.alert("必须选择一个级以上的功能！");

        if ($("#" + id)[0].checked) {
            functionList.push(value);
        } else if ($.inArray(value, functionList) != -1) {
            functionList.splice($.inArray(value, functionList), 1);
        }
    }
    function query() {
        status = $("#statusSelect").val();
        if (1 == status) {
            appName = $("#appNameInput").val();
            funcName = "";
        } else {
            funcName = $("#appNameInput").val();
            appName = "";
        }
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            type: 'get',
            dataType: "json",
            data: {
                "appName": encodeURI(appName),
                "funcName": encodeURI(funcName)
            },
            url: "${pageContext.request.contextPath}"
            + "/manager/arcm/getAppInfoData",
            error: function () {//请求失败处理函数
                window.parent.layer.alert('请求失败');
            },
            success: function (res) { //请求成功后处理函数。
                if ("success" == res.result) {
                    appData = res.data.data;
                    pagination();
                    functionList = [];
                } else {
                    window.parent.layer.alert(res.reason);
                }

            }
        });
    }
    function funcList(idStr) {
        if ($("#" + idStr).hasClass("btnBgImgIn")) {
            $("#" + idStr).removeClass("btnBgImgIn");
            $("#" + idStr).addClass("btnBgImgOut");
        } else {
            $("#" + idStr).removeClass("btnBgImgOut");
            $("#" + idStr).addClass("btnBgImgIn");
        }
        var str = idStr.split("_");
        if (parseInt(str[1]) > -1) {
            for (var i = -1; i < parseInt(str[1]); i++) {
                var j = i + 1;
                if ($("#" + str[0] + "tr_" + j).hasClass("trDisplay")) {
                    $("#" + str[0] + "tr_" + j).removeClass("trDisplay");
                } else {
                    $("#" + str[0] + "tr_" + j).addClass("trDisplay");
                }
            }
        }
    }
    function getStatus(status) {
        if (1 == status) {
            return "运行中";
        }
        if (2 == status) {
            return "卸载中";
        }
        if (3 == status) {
            return "已卸载";
        }
        if (4 == status) {
            return "切换中";
        }
        if (5 == status) {
            return "已关闭";
        } else {
            return "未知";
        }
    }
</script>
</html>
