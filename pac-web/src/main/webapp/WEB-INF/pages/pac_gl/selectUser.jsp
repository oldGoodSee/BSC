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
<style type="text/css">
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

    .trDisplay {
        display: none;
    }

    #userListTable td {
        vertical-align: middle;
    }
</style>
<body>
<div class="layout-main" style="margin-top: 20px;position: relative;left: 30px;width: 95%;">
    <br>
    <%--<label>Step&nbsp;3&nbsp;:</label>--%>
    <label>请选择需要授权的用户</label>
    <br><br>
    <form id="myForm" class="form-inline" name="form" action="${pageContext.request.contextPath}/manager/basket/setRight"
          onkeydown="if(event.keyCode==13)return false;">
        <table class="table" id="userListTable">
            <thead>
            <tr>
                <th style="width: 25%"></th>
                <th style="width: 45%"></th>
                <th style="width: 15%"></th>
                <th style="width: 15%"></th>
            </tr>
            </thead>
            <c:if test="${not empty userListAll}">
                <c:forEach var="userList" items="${userListAll}">
                    <tr>
                        <td><label>组织名称&nbsp;：</label>${userList.orgName}
                        </td>
                        <td></td>
                        <td><input type="checkbox" zz="zz" onclick="checkBoxF(name)" name="${userList.orgCode}"></td>
                        <td><input type="button" class='btn-link btnBgImgIn' id="${userList.orgCode}"
                                   onclick="playOrDis(id)">
                        </td>
                    </tr>
                    <c:if test="${not empty userList.userRoleOrgs}">

                        <tr class="trDisplay" name="${userList.orgCode}">
                            <td colspan="4">
                                <c:forEach items="${userList.userRoleOrgs}" var="user" varStatus="go">
                                    <label style="width: 10%;">
                                            ${user.realName}&nbsp;
                                    </label><input style="width: 8%;" type="checkbox" xx="xx"
                                                   userId="${user.realName}"
                                                   name="td_${userList.orgCode}"
                                                   onclick="checkAllBox(name)">
                                    <c:if test="${!go.first&&(go.index+1)%5==0}">
                                        &nbsp;<br>
                                    </c:if>
                                </c:forEach>
                            </td>
                        </tr>

                    </c:if>
                    <c:if test="${empty userList.userRoleOrgs}">
                        <tr class="trDisplay" name="${userList.orgCode}">
                            <td></td>
                            <td>
                                无记录
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                    </c:if>
                </c:forEach>
            </c:if>
            <c:if test="${empty userListAll}">
                <br>无记录<br>
            </c:if>
        </table>
        <input type="hidden" name="pageInfo" value="">
    </form>
    <%--<button type="button" class="btn btn-primary" style="float: left"--%>
            <%--onclick="window.history.back(-1);">--%>
    <%--</button>--%>
    <button type="submit" class="btn btn-primary" style="float: right;position: absolute;right: 100px;"
            onclick="release()">授权
    </button>
    <br><br><br><br>
    <%--<label> 第&nbsp;3&nbsp;步/共&nbsp;4&nbsp;步</label>--%>
</div>
</body>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/pac_gl/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
    var appId='';
    $(document).ready(function () {
        appId = '${appId}';
        var appUsers='${appUsers}';
        var userArray=appUsers.split(",");
        var checks=$("input[type='checkbox']");//get all the checkbox
        $(checks).each(function(){
            if(validateHasRight($(this).attr("userId"), userArray)){
                $(this).attr("checked","checked");
            }
        });
    });

    function validateHasRight(userName,userArray){
        var result=false;
        for(var i=0;i<userArray.length;i++){
            if(userName==userArray[i]){
                result=true;
                break;
            }
        }
        return result;
    }
    function playOrDis(orgID) {
        var btnObj = $("#" + orgID);
        if (btnObj.hasClass("btnBgImgIn")) {
            btnObj.removeClass("btnBgImgIn");
            btnObj.addClass("btnBgImgOut");
        } else {
            btnObj.removeClass("btnBgImgOut");
            btnObj.addClass("btnBgImgIn");
        }
        var obj = $("tr[name='" + orgID + "']");
        if (obj.hasClass("trDisplay")) {
            obj.removeClass("trDisplay");
        } else {
            obj.addClass("trDisplay");
        }
    }
    var fromManage = false;
    function release() {
        var userInfoList = {
            appId:'${appId}',
            userList: []
        };
        var objParent = $("input[zz=zz]");
        objParent.each(function (i) {
            var userInfo = {
                orgId: "",
                userId: []
            };
//            if (objParent[i].checked) {
//                userInfo.orgId = objParent[i].name;
//            } else {
            var obj = $("input[name=td_" + objParent[i].name + "]:checked");
            obj.each(function (i) {
                userInfo.userId.push($(obj[i]).attr("userId"));
            });
//            }
            var flag = (userInfo.orgId != "" && userInfo.userId.length == 0) || (userInfo.orgId == "" && userInfo.userId.length != 0)
            if (flag) {
                userInfoList.userList.push(userInfo);
            }
        });
        if (userInfoList.userList.length == 0) {
            window.parent.layer.alert("请选择可以访问的用户！");
            return;
        }
        try{
            fromManage = window.parent.fromManage();
            $.ajax({
                dataType: "text",
                type: "POST",
                url: "${pageContext.request.contextPath}/manager/basket/setRight",
                data:{
                    pageInfo:JSON.stringify(userInfoList)
                },
                cache: false,
                success: function (result) {
                    window.parent.publishAjax();
                },
                error: function () {
                    window.parent.myAlert("权限设置失败！");
                }
            });
        }catch(e){
            $("input[name='pageInfo']").val(JSON.stringify(userInfoList));
            form.submit();
        }
    }
    function checkBoxF(name) {
        var flag = $("input[name=" + name + "]")[0].checked;
        $("input[name=td_" + name + "]").each(function (i) {
            $("input[name=td_" + name + "]")[i].checked = flag;
        })
    }
    function checkAllBox(name) {
        var obj = $("input[name=" + name + "]")
        var length = obj.length;
        var checkLength = 0;
        obj.each(function (i) {
            if (obj[i].checked) {
                checkLength += 1;
            }
        })
        if (checkLength == length) {
            $("input[name=" + name.split("_")[1] + "]").prop("checked", true);
        } else {
            $("input[name=" + name.split("_")[1] + "]").prop("checked", false);
        }
    }
</script>
</html>
