<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: bocom-qy
  Date: 2017/6/16
  Time: 17:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bocom.domain.pac.AppOperationLogInfo,com.bocom.domain.pac.TDictionaryInfo" %>
<html>
<head>
    <title>日志查询</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/bootstrap/datetimepicker/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/market/css/main.css"/>
    <style type="text/css">
        #logBody td {
            vertical-align: middle;
        }

        .table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td {
            padding: 0px;
            line-height: 1.42857143;
            vertical-align: top;
            border-top: 1px solid #ddd;
        }

        .container {
            padding-right: 15px;
            padding-left: 15px;
            margin-right: inherit;
            margin-left: inherit;
        }
    </style>
</head>
<body>
<div>
    <form class="form-inline" action="${pageContext.request.contextPath}/manager/menuAction/queryLog" name="form">
        <div style="margin-left: 30px;margin-top: 15px">
            <label class="col-md-1 control-label">日志内容 ：</label>
            <input type="text" id="content" name="content" class="form-control" style="margin-left: 15px"
                   placeholder="输入要筛选的日志内容">
        </div>
        <div style="margin-left: 30px;margin-top: 15px">
            <label class="col-md-1 control-label">动作类型 ：</label>
            <c:forEach var="dic" items="${dicInfoListMap}">
                <c:if test="${dic.typeId == 1}">
                    <div class="checkbox-inline">
                        <label>
                            <input type="radio" name="operationType" id="${dic.id}" value="${dic.id}">
                                ${dic.name}
                        </label>
                    </div>
                </c:if>
            </c:forEach>
            <div class="checkbox-inline">
                <label>
                    <input type="radio" name="operationType" id="oAll" value="oAll">全部
                </label>
            </div>
        </div>
        <div style="margin-left: 30px;margin-top: 15px">
            <label class="col-md-1 control-label">业务类型 ：</label>
            <c:forEach var="dic" items="${dicInfoListMap}">
                <c:if test="${dic.typeId == 2}">
                    <div class="checkbox-inline">
                        <label>
                            <input type="radio" name="businessType" id="${dic.id}" value="${dic.id}">
                                ${dic.name}
                        </label>
                    </div>
                </c:if>
            </c:forEach>
            <div class="checkbox-inline">
                <label>
                    <input type="radio" name="businessType" id="bAll" value="bAll">全部
                </label>
            </div>
        </div>
        <div style="margin-left: 30px;margin-top: 15px">
            <label class="col-md-1">时间段选择 ： </label>
        </div>
        <div class="container">
            <div class='col-md-3'>
                <div class="form-group">
                    <div class='input-group date' id='cong'>
                        <input type='text' class="form-control" name="startTime" id="startTime"/>
                        <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
                    </div>
                </div>
            </div>
            <div class='col-md-3'>
                <div class="form-group">
                    <div class='input-group date' id='dao'>
                        <input name="endTime" id="endTime" type='text' class="form-control"/>
                        <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
                    </div>
                </div>
            </div>
        </div>
        <div style="margin-left: 35px;margin-top: 15px">
            <input type="button" id="sumbit" class="btn btn-primary" value="查询" onclick="query(1)"
                   style="margin-left: 10px">

            <input type="reset" id="reset" class="btn btn-primary" value="重置" style="margin-left: 30px">
        </div>
        <div style="margin-left: 10px;margin-top: 15px">
            <input type="hidden" name="pageSize" value="">
            <input type="hidden" name="pageNum" value="">
            <table class="table table-hover" style="width: 98%;margin-left: 2%;">
                <thead>
                <tr>
                    <th width="5%">序号</th>
                    <th width="10%">操作</th>
                    <th width="10%">业务类型</th>
                    <th width="30%">日志内容</th>
                    <th width="10%">操作人员</th>
                    <th width="15%">人员组织</th>
                    <th width="30%">记录时间</th>
                </tr>
                </thead>
                <tbody id="logBody">
                <c:if test="${not empty logPage}">
                    <c:forEach var="log" items="${logPage}">
                        <tr>
                            <td>${log.id}</td>
                            <td>${log.operationType}</td>
                            <td>${log.businessType}</td>
                            <td>${log.content}</td>
                            <td>${log.userName}</td>
                            <td>${log.orgName}</td>
                            <td>${log.createTime}</td>
                        </tr>
                    </c:forEach>
                </c:if>
                <c:if test="${empty logPage}">
                <tr>
                    <td colspan="5" style="text-align: center">无记录</td>
                <tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        <!--分页-->
        <div class="clear">
            <div class="fr" style="margin-top: 20px;" id="pageContainer"></div>
        </div>
    </form>
</div>
</body>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/market/js/laypage-v1.3/laypage/laypage.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/market/js/layer/layer.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/pac_gl/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/bootstrap/datetimepicker/js/moment-with-locales.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/bootstrap/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>

<script type="text/javascript">

    $(function () {
        $('#cong').datetimepicker({
            format: 'YYYY-MM-DD HH:mm',
            locale: moment.locale('zh-cn')
        });
        $('#dao').datetimepicker({
            format: 'YYYY-MM-DD HH:mm',
            locale: moment.locale('zh-cn'),
            useCurrent: false //Important! See issue #1075
        });
        $("#cong").on("dp.change", function (e) {
            $('#dao').data("DateTimePicker").minDate(e.date);
        });
        $("#dao").on("dp.change", function (e) {
            $('#cong').data("DateTimePicker").maxDate(e.date);
        });
    });

</script>

<script type="text/javascript">

    var pageNum = parseInt('${pageInfo.pageNum}');
    var pageSize = parseInt('${pageInfo.pageSize}');
    var pages = parseInt('${pageInfo.pages}');

    $(document).ready(function () {

        var operationType = '${operationType}';
        var businessType = '${businessType}';
        var startTime = '${startTime}';
        var endTime = '${endTime}';
        var content = '${content}';
        if (operationType == null || operationType == "" || operationType == 'null') {
            $("#oAll").attr("checked", true);
        } else {
            $("#" + operationType + "").attr("checked", true);
        }
        if (businessType == null || businessType == "" || businessType == 'null') {
            $("#bAll").attr("checked", true);
        } else {
            $("#" + businessType + "").attr("checked", true);
        }
        $("#startTime").val(startTime);
        $("#endTime").val(endTime);
        $("#content").val(decodeURI(content));
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

    $("#reset").on('click',function () {
        $("#bAll").attr("checked", true);
        $("#oAll").attr("checked", true);
    })
    });
    function query(pageNum) {

        var businessType = $("input[name='businessType']:checked").val();
        var operationType = $("input[name='operationType']:checked").val();
        if (businessType == "bAll") {
            $("input[name='businessType']").val("");
        }
        if (operationType == "oAll") {
            $("input[name='operationType']").val("");
        }
        $("input[name='pageSize']").val(pageSize);
        $("input[name='pageNum']").val(pageNum);

        form.submit();
    }
</script>
</html>
