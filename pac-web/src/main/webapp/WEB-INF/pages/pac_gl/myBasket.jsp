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
<div class="layout-main" style="margin-top: 20px;position: relative;left: 30px;width: 95%;">
    <br>
    <form class="form-inline" onkeydown="if(event.keyCode==13)return false;" name="form1">
        <input type="hidden" name="appId" value="">
        <input type="hidden" name="pageInfo" value="">

        <div>
            <label> 已选的功能页面&nbsp;：</label>
            <br><br>
            <table class="table">
                <c:if test="${not empty packagingDtos}">
                    <c:forEach var="dto" items="${packagingDtos}">
                        <tr>
                            <td><label>${dto.oappname}</label>
                                <br><br>
                                <c:if test="${not empty dto.packagingInfos}">
                                    <c:forEach items="${dto.packagingInfos}" var="p" varStatus="go">
                                        <c:if test="${!go.first}">
                                            <br>
                                        </c:if>
                                        <label style="width: 200px;font-weight: 100">${p.pageName}</label>
                                        <input type="checkbox" value="${p.pageid}">
                                        <br>
                                    </c:forEach>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                <c:if test="${empty packagingDtos}">
                    <br>
                    无记录
                    <br>
                </c:if>
            </table>
        </div>
    </form>
    <input type="button" class="btn btn-danger" value="删除所选" onclick="delFunc()">
    <input type="button" class="btn btn-success" style="float: right;position: absolute;right: 100px;" value="返回市场"
           onclick="goRelease()">
</div>
<%--    <input type="button" class="btn-primary" onclick="checkAll(2)" value="全不选">
    <input type="button" class="btn-primary" onclick="checkAll(1)" value="全选">--%>
</body>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/pac_gl/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
    var appId, basketId;
    var basket = [];
    var pageIdJson = {
        basketId: "",
        pageId: [],
        appId: ""
    };
    $(document).ready(function () {
    });
    function delFunc() {
        pageIdJson.pageId = [];
        var i = 0;
        $("input[type='checkbox']:checked").each(function () {
            var value = $(this).val();
            if ($.inArray(value, pageIdJson.pageId) == -1) {
                pageIdJson.pageId.push($(this).val());
            }
            i++;
        });
        if (i == 0) {
            window.parent.layer.alert('请选择要删除的页面');
            return;
        }
        pageIdJson.basketId = basketId;
        pageIdJson.appId = '';
        $("form").attr("action", "${pageContext.request.contextPath}/manager/basket/deleteFromBasket");
        $("input[name='pageInfo']").val(JSON.stringify(pageIdJson));
        form1.submit();
    }

    function goRelease() {
        window.parent.menuClick("market");
    }
</script>
</html>
