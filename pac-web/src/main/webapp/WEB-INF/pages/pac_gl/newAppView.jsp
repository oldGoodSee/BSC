<%--
  Created by IntelliJ IDEA.
  User: bocom-qy
  Date: 2016/11/17
  Time: 16:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>已组装应用预览</title>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/pac_gl/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/pac_gl/js/jqPaginator.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/bootstrap/datetimepicker/js/moment-with-locales.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/bootstrap/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/pac_gl/style/sub.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/bootstrap/datetimepicker/css/bootstrap-datetimepicker.min.css"/>
    <style type="text/css">
        #mainTable td {
            vertical-align: middle;
        }
    </style>
</head>
<body>
<div class="layout-main" style="margin-top: 20px;">
    <div class="table-toolbar" style="margin-left: 10px;float: left">
        <form class="form-inline text-left" onkeydown="if(event.keyCode==13)return false;" name="form1">
            <input type="hidden" name="appId" id="appId">
            <label class="control-label"> 应用名称&nbsp;：</label>
            <input id="appNameInput" class="form-control" placeholder="输入应用名称">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <label class="control-label"> 运行状态&nbsp;：</label>
            <select id="statusSelect" class="form-control">
                <option id="-1" value="-1">请选择</option>
                <option id="1" value="1">正常访问中</option>
                <option id="2" value="0">屏蔽中</option>
                <option id="3" value="6">编辑中</option>
            </select>
            &nbsp;&nbsp;
            <input type="button" id="queryButton" class="btn btn-primary" onclick="query()" value="查&nbsp;询"
                   style="position: relative;left: 30px;">
        </form>
    </div>
    <input type="button" id="addNewApp" class="btn btn-primary" onclick="addApp()" value="新建组装应用"
           style="float: right">
    <br>
    <div class="layout-table-container">
        <table class="table table-striped">
            <thead>
            <tr class="bg-tr-even">
                <%--<th width="10%">应用ID</th>--%>
                <th width="10%">应用名称</th>
                <th width="20%">应用描述</th>
                <th width="15%">创建时间</th>
                <th width="10%">创建人员</th>
                <th width="10%" style="text-align: center">失效时间</th>
                <th width="10%">运行状态</th>
                <th width="25%">操作</th>
            </tr>
            </thead>
            <tbody id="mainTable">
            </tbody>
        </table>
    </div>
    <ul class="pagination layout-page" id="pagination" style="float: right;"></ul>

    <div class="modal fade" id="updateAppBaseInfo">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span
                            aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title">失效延期</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="appId" value="">
                    <input type="hidden" name="appName" value="">
                    <div style="overflow:hidden;">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-8" style="margin-left: 10%">
                                    <div id="uninstallTime"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" style="float: left" onclick="updateAppBaseInfo()">确认
                    </button>
                    <button type="button" class="btn btn-default" style="float: right" data-dismiss="modal">取消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
</body>
<script type="text/javascript">
    //用于存放当前页码
    var pageNum = 1;
    //用于时存放数据编码数据
    var appInfoList = [];
    var appName, status, appId;
    $(document).on("ready", function () {
        query();
    });

    $(document).ready(function () {
        var setright = '${SETRIGHT}';
        if (setright == 'FINISH') {
            window.parent.layer.msg('授权成功', {icon: 1});
        }
    });

    function addRight() {
        window.parent.layer.open({
            type: 2,
            title: "授权",
            area: ['1000px', '600px'],
            offset: 'auto',
            content: ['http://10.37.149.105:8899/pap/a/sys/manager/roleAction/pacRole'],
            end: function () {
                window.parent.layer.closeAll();
            }
        });
    }

    function query(curr) {
        appName = $("#appNameInput").val();
        status = $("#statusSelect").val();
        if (-1 == status) {
            status = "";
        }
        var pageObject = {
            pageNum: curr || 1,
            pageSize: 10,
            appId: appId,
            appName: encodeURI(appName),
            status: status
        };
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            type: 'get',
            dataType: "json",
            data: pageObject,
            url: "${pageContext.request.contextPath}"
            + "/manager/app/getAppBaseInfo",
            error: function () {//请求失败处理函数
                window.parent.layer.alert('请求失败');
            },
            success: function (res) { //请求成功后处理函数。
                pagination(res, curr);
            }
        });
    }

    function pagination(res, curr) {
        $("#mainTable").empty();
        var innerStr = "";
        pageNum = res.page.pageNum;
        appInfoList = res.page.list;
        if (appInfoList.length != 0) {
            for (var i = 0; i < appInfoList.length; i++) {
                var trCls = "";
                var createTime = appInfoList[i].createTime ? appInfoList[i].createTime : "";
                var appId = appInfoList[i].appId;
                var status = appInfoList[i].status;
                var linkList = "";
                if (appInfoList[i].isAuthorize == 'true') {
                    linkList += "<button class='btn btn-link' onclick='funcList(" + appId + ",4,\""
                        + appInfoList[i].appName + "\",\"" + status + "\")'>查看/编辑布局</button>";
                } else {
                    var link = "";
                    if (6 != parseInt(status)) {
                        if (0 == parseInt(status)) {
                            link = "<button class='btn btn-link' onclick='funcList(" + appId + ",5)'>恢复</button>";
                        } else {
                            link = "<button class='btn btn-link' onclick='funcList(" + appId + ",0)'>屏蔽</button>";
                        }
                    }
                    var status = appInfoList[i].status;
                    var msg = "卸载";
                    if (parseInt(status) != 1 && parseInt(status) != 0) {
                        msg = "删除";
                    }
                    linkList += "<button class='btn btn-link' onclick='funcList(" + appId + ",99)'>修改授权</button>"
                        + "<button class='btn btn-link' onclick='funcList(" + appId + ",4,\"" +
                        appInfoList[i].appName + "\",\"" + status + "\")'>查看/编辑布局</button>"
                        + link
                        + "<button class='btn btn-link' style='color: red;' onclick='funcList(" + appId + ",3,\""
                        + appInfoList[i].appName + "\",\"" + status + "\" )'>" + msg + "</button>";
                }
                var btnUnistall;
                if (appInfoList[i].uninstallTime == null) {
                    appInfoList[i].uninstallTime = "无";
                    btnUnistall =
                        "<input class='btn disabled' value='" + appInfoList[i].uninstallTime + "'>";
                } else {
                    btnUnistall = "<input class='btn btn-link' onclick='updateAppBaseInfoShow(" + appId + ","
                        + '"' + appInfoList[i].uninstallTime + '"' + ","
                        + '"' + appInfoList[i].appName + '"' + ")' value='" + appInfoList[i].uninstallTime + "'>";
                }

                innerStr += "<tr>" +
                    "<td>" + "<button class='btn btn-link' onclick='funcList(" + appId + ",4,\"" +
                    appInfoList[i].appName + "\",\"" + status + "\")'>" + appInfoList[i].appName + "</button></td>" +
                    "<td>" + appInfoList[i].appDesc + "</td>" +
                    "<td>" + createTime + "</td>" +
                    "<td>" + appInfoList[i].userName + "</td>" +
                    "<td>" + btnUnistall + "</td>" +
                    "<td>" + getStatus(status) + "</td>" +
                    "<td>" + linkList + "</td>" +
                    "</tr>";
            }
        } else {
            innerStr = "<tr>"
                + "<td colspan='6' align='center'> 无记录 </td>" +
                "</tr>";
            res.page.pages = 1;
        }

        $("#mainTable").append(innerStr);
        //显示分页
        var first = true;
        $.jqPaginator('#pagination', {
            totalPages: res.page.pages,
            visiblePages: 10,
            currentPage: 1,
            first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
            prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
            next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
            last: '<li class="last"><a href="javascript:void(0);">末页</a></li>',
            page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
            onPageChange: function (num) {
                if (!first) {
                    query(num);
                }
                first = false;
            }
        });
    }

    function updateAppBaseInfoShow(appId, uninstallTime, appName) {
        $("input[name='appId']").val(appId);
        $("input[name='appName']").val(appName);
        $('#uninstallTime').datetimepicker({
            format: 'YYYY-MM-DD',
            minDate: uninstallTime,
            locale: moment.locale('zh-cn'),
            inline: true,
            sideBySide: true
        });
        $('#updateAppBaseInfo').modal({backdrop: 'static'});
    }

    function updateAppBaseInfo() {
        var uninstallTime = $('#uninstallTime').datetimepicker().data().date;
        var appId = $("input[name='appId']").val();
        var appName = $("input[name='appName']").val();
        var date = {
            appId: appId,
            appName: encodeURI(appName),
            uninstallTime: uninstallTime
        };
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            type: 'POST',
            dataType: "json",
            data: JSON.stringify(date),
            url: "${pageContext.request.contextPath}" + "/manager/app/updateAppBaseInfo",
            error: function () {
                window.parent.layer.msg("请求失败", {icon: 2});
            },
            success: function (res) {
                if (res.success == true) {
                    $('#updateAppBaseInfo').modal('hide');
                    window.parent.layer.msg("延期 成功", {icon: 1});
                    window.parent.menuClick("appView");
                } else {
                    window.parent.layer.msg("延期 失败", {icon: 5});
                }
            }
        });
    }

    function funcList(appId, funcId, appName, status) {
        if (funcId == 99) {
            $("#appId").val(appId);
            form1.action = '${pageContext.request.contextPath}/manager/basket/toSetRight';
            form1.submit();
        }
        if (funcId == 4) {
            window.open("${pageContext.request.contextPath}/manager/menuAction/qingdan?appName=" + encodeURI(encodeURI(appName)) + "&appId=" + appId + "&status=" + status);
        }
        if (funcId == 3) {
            var msg = "删除";
            if (parseInt(status) == 1 || parseInt(status) == 0) {
                msg = "卸载";
            }
            window.parent.layer.open({
                type: 1,
                title: '是否确定' + msg,
                shade: 0.3,
                zIndex: window.parent.layer.zIndex, //用于选中置顶
                content: '<div style="padding: 20px; line-height: 20px;width: 150px;text-align:center">' + appName + '  ?</div>',
                btn: ['确定'],
                btn1: function (index, layero) {
                    $.ajax({
                        async: false,
                        cache: false,
                        contentType: "application/json",
                        type: 'get',
                        dataType: "json",
                        data: {
                            appId: appId,
                            status: status
                        },
                        url: "${pageContext.request.contextPath}/manager/app/uninstallApp",
                        error: function () {//请求失败处理函数
                            window.parent.layer.alert('请求失败');
                        },
                        success: function (res) { //请求成功后处理函数。
                            if (res.result == "success") {
                                window.parent.layer.msg(msg + "成功", {icon: 1});
                                window.parent.menuClick("appView");
                            } else {
                                window.parent.layer.alert(res.reason)
                            }

                        }
                    });
                    window.parent.layer.close(index);
                },
                success: function (layero, index) {
                    window.parent.layer.setTop(layero);	//用于选中置顶
                }
            });
        }
        if (funcId == 0) {
            window.parent.layer.confirm(' 是否 确定屏蔽 ？ ', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    async: false,
                    cache: false,
                    contentType: "application/json",
                    type: 'get',
                    dataType: "json",
                    data: {
                        appId: appId,
                        status: 0
                    },
                    url: "${pageContext.request.contextPath}/manager/basket/updateAppStatus",
                    error: function () {//请求失败处理函数
                        window.parent.layer.alert('请求失败');
                    },
                    success: function (res) { //请求成功后处理函数。
                        if (res.result == "success") {
                            window.parent.layer.msg("屏蔽成功", {icon: 1});
                            window.parent.menuClick("appView");

                        } else {
                            window.parent.layer.alert(res.reason)
                        }

                    }
                });
                layer.close(index);
            });
        }
        if (funcId == 5) {
            window.parent.layer.confirm(' 是否 确定恢复 ？ ', {icon: 3, title: '提示'}, function (index) {
                $.ajax({
                    async: false,
                    cache: false,
                    contentType: "application/json",
                    type: 'get',
                    dataType: "json",
                    data: {
                        appId: appId,
                        status: 1
                    },
                    url: "${pageContext.request.contextPath}/manager/basket/updateAppStatus",
                    error: function () {//请求失败处理函数
                        window.parent.layer.alert('请求失败');
                    },
                    success: function (res) { //请求成功后处理函数。
                        if (res.result == "success") {
                            window.parent.layer.msg("恢复成功", {icon: 1});
                            window.parent.menuClick("appView");

                        } else {
                            window.parent.layer.alert(res.reason)
                        }

                    }
                });
                layer.close(index);
            });

        }
    }

    function getStatus(status) {
        if (1 == status) {
            return "正常访问中";
        }
        if (0 == status) {
            return "屏蔽中";
        }
        if (3 == status) {
            return "已卸载";
        }
        if (6 == status) {
            return "编辑中";
        } else {
            return "未知";
        }
    }

    function addApp() {
        window.parent.layer.open({
            type: 2,
            title: "新建组装应用",
            area: ['900px', '600px'],
            offset: 'auto',
            content: ['${pageContext.request.contextPath}/manager/menuAction/buildNewApp?resource=1'],
            end: function () {
                window.parent.layer.closeAll();
                window.location.href = "${pageContext.request.contextPath}/manager/menuAction/newAppView";
            }
        });
    }
</script>
</html>
