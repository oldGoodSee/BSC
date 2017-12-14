<%--
  Created by IntelliJ IDEA.
  User: bocom-qy
  Date: 2017-1-16
  Time: 10:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>notice</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css"/>
</head>
<style type="text/css">
    textarea {
        resize: none;
    }
</style>
<body>

<div>
    <div class="layout-main" style="margin-top: 20px;">

        <input type="button" id="addNewApp" class="btn btn-primary" onclick="addNotice()" value="新建通知"
               style="float: right;margin-right: 20px">

        <form class="form-inline" onkeydown="if(event.keyCode==13)return false;" name="form">
            <div>

                <table class="table table-responsive" style="width: 98%;margin-left: 2%;">
                    <thead>
                    <tr>
                        <th width="10%">序号</th>
                        <th width="40%">通知内容</th>
                        <th width="10%">关联应用</th>
                        <%--<th width="50%">通知内容</th>--%>
                        <th width="20%">添加时间</th>
                        <th width="20%">操作</th>
                    </tr>
                    </thead>
                    <tbody id="appInfoBody">
                    <c:if test="${not empty noticeList}">
                        <c:forEach var="notice" items="${noticeList}">
                            <tr>
                                <td>${notice.sort}</td>
                                <td>${notice.content}</td>
                                <c:if test="${notice.noticeType=='public'}">
                                    <td style="color: #00aa91">全局通知</td>
                                </c:if>
                                <c:if test="${notice.noticeType=='private'}">
                                    <td>${notice.appName}</td>
                                </c:if>
                                <td>
                                    <fmt:formatDate value="${notice.createTime}" type="both"/>
                                </td>
                                <td>
                                    <input type="button" value="编辑" class="btn btn-link"
                                           onclick="updateNotice(${notice.id},'${notice.content}')"/>
                                    <input type="button" value="删除" class="btn btn-link"
                                           onclick='deleteNotice(${notice.id})' style="color: red"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty noticeList}">
                    <tr>
                        <td colspan="4" style="text-align: center">无记录</td>
                    <tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </form>
    </div>

    <div class="modal fade bs-example-modal-lg" id="myModal">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span
                            aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title">新建通知</h4>
                </div>
                <div class="modal-body">
                    <form role="form">
                        <br>
                        <label>序号：</label>
                        <input type="text" id="sort" class="form-control">
                        <br>
                        <label>内容：</label>
                        <textarea class="form-control" id="content" rows="6"></textarea>
                        <br>
                        <label style="display: none">频率：</label>
                        <input type="text" id="frequency" class="form-control" value="1" style="display: none">
                        <br>
                        <label>类型：</label>
                        <select id="statusSelect" class="form-control" onchange="selectChange(this.value)">
                            <option id="-1" value="-1">请选择</option>
                            <option id="1" value="public" selected="selected">公共</option>
                            <option id="2" value="private">私有</option>
                        </select>
                        <br>
                        <c:if test="${not empty appNameIDList}">
                            <div id="appDiv" style="display: none">
                                <label>选择应用：</label>
                                <select id="appIdSelect" class="form-control">
                                    <c:forEach var="app" items="${appNameIDList}">
                                        <option value="${app.appId}">${app.appName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </c:if>
                        <c:if test="${ empty appNameIDList}">
                            <div id="appDiv" style="display: none">
                                <label>选择应用：</label>
                                <br>
                                <label>无记录！请先发布应用 ！ </label>
                            </div>
                        </c:if>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" style="float: left" onclick="submit()">确认
                    </button>
                    <button type="button" class="btn btn-default" style="float: right" onclick="cancel()">取消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div class="modal fade" id="updateModal">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span
                            aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title">编辑通知</h4>
                </div>
                <div class="modal-body">
                    <form role="form">
                        <input type="hidden" id="id">
                        <br>
                        <label>内容：</label>
                        <textarea class="form-control" id="contentNew" rows="6"></textarea>
                        <br>
                        <input type="button" value="清空" class="btn btn-default" onclick="empty()">
                        <br>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" style="float: left" onclick="submit2()">确认
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
<script type="text/javascript"
        src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript">
    function empty() {
        $("#contentNew").val("");
    }
    $(document).ready(function () {

    });
    function deleteNotice(id) {
        var noticeInfo = {
            id: id
        };
        var type = "delete";
        ajaxSubmit(noticeInfo, type);
    }

    function submit2() {
        var content = encodeURI($("#contentNew").val());
        var length = content.length;
        if (length == 0) {
            window.parent.layer.alert("请输入内容！");
            return false;
        }
        var id = $("#id").val();
        var noticeInfo = {
            content: content,
            id: id
        };
        var type = "update";
        ajaxSubmit(noticeInfo, type);
        $('#updateModal').modal('hide');
    }
    function updateNotice(id, content) {
        $('#id').val(id);
        $('#contentNew').val(content);
        $('#updateModal').modal({backdrop: 'static'});
    }
    function addNotice() {
        $('#myModal').modal({backdrop: 'static'});
    }
    function cancel() {
        $("#sort").val("");
        $("#frequency").val("");
        $("#content").val("");
        $("#statusSelect").val("public");
        $("#appDiv").attr("style", "display:none");
        $('#myModal').modal('hide');
    }
    function submit() {
        var sort = $("#sort").val();

        var reg = new RegExp("^[0-9]*$");

        if (sort.length == 0) {
            window.parent.layer.alert("请输入数字类型的序号！");
            return false;
            //TODO
        } else if (!reg.test(sort)) {
            window.parent.layer.alert("请输入数字类型的序号！");
            return false;
        }
        var frequency = $("#frequency").val();
        if (frequency.length == 0) {
            window.parent.layer.alert("请输入数字类型的频率！");
            return false;
        } else if (!reg.test(frequency)) {
            window.parent.layer.alert("请输入数字类型的频率！");
            return false;
        }
        var content = encodeURI($("#content").val());
        if (content.length == 0) {
            window.parent.layer.alert("请输入内容！");
            return false;
        }
        var noticeType = $("#statusSelect").val();
        var appId = $("#appIdSelect").val();
        var appName = encodeURI($("#appIdSelect option:checked").text());
        if(parseInt(noticeType) == -1){
            window.parent.layer.alert("请选择通知类型 !");
            return false;
        }
        if ("private" == noticeType) {
            if (appId == undefined) {
                window.parent.layer.alert("请先发布应用 ！");
                return false;
            }
        }
        var noticeInfo = {
            sort: sort,
            content: content,
            noticeType: noticeType,
            frequency: frequency,
            appId: appId,
            appName: appName
        };
        var type = "add";
        var result = ajaxSubmit(noticeInfo, type);
        if (result == "success") {
            $('#myModal').modal('hide');
            $('#myModal').modal();
        }

    }
    function selectChange(value) {
        if ("private" == value) {
            $("#appDiv").attr("style", "display:false");
        } else {
            $("#appDiv").attr("style", "display:none");
        }
    }

    function ajaxSubmit(noticeInfo, type) {
        var result;
        var data = {
            noticeInfo: noticeInfo,
            type: type
        };
        $.ajax({
            contentType: "application/json",
            type: 'post',
            dataType: "json",
            url: "${pageContext.request.contextPath}/manager/notice/configNotice",
            data: JSON.stringify(data),
            error: function (XMLHttpRequest) {//请求失败处理函数
                var status = XMLHttpRequest.status;
                if (status == 400) {
                    window.parent.layer.alert("请求失败！");
                } else {
                    window.parent.layer.alert("请求失败！");
                }

            },
            success: function (res) { //请求成功后处理函数。
                if ("success" != res.result) {
                    window.parent.layer.msg(res.reason, {icon: 5});
                    result = "error";
                } else {
                    var msg = "";
                    if ("add" == type) {
                        msg = "新建通知成功";
                    } else if ("update" == type) {
                        msg = "修改通知成功";
                    } else {
                        msg = "删除通知成功";
                    }
                    window.parent.layer.msg('&nbsp&nbsp&nbsp&nbsp&nbsp' + msg, {icon: 1});
                    window.parent.menuClick("notice");
                    result = "success";
                }
            }
        });
        return result;
    }
</script>
</html>
