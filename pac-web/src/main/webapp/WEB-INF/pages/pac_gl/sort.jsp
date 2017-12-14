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
    <label style="margin-left: 7px;">Step&nbsp;2&nbsp;:</label>
    <label>请设置页面布局风格</label>
    <br><br>
    <label style="margin-left: 7px;color: orangered">注意&nbsp;:</label>
    <label>当选择布局风格是tab样式的时候，排序值可重复，最多可允许出现两个相同的排序值， 排序值相同的页面会在同一个tab页中展示;</label>
    <br><label style="margin-left: 50px;">浮动样式在选择完分辨率之后，请选择属于哪一个页面。</label>
    <hr style="margin:0px;height:1px;border:0px;background-color:#D5D5D5;color:#D5D5D5;"/>
    <br>
    <form class="form-inline" name="form" action="${pageContext.request.contextPath}/manager/basket/cashNext"
          onkeydown="if(event.keyCode==13)return false;">
        <br><br>
        <table class="table">
            <c:if test="${not empty packagingDtos}">
                <c:forEach var="dto" items="${packagingDtos}" varStatus="pack">
                    <tr>
                        <td><label style="width: 25%;">${dto.oappname}</label>
                                <%--<label style="width: 10%;font-weight: 100">布局风格&nbsp;</label>--%>
                                <%--<label style="width: 20%;font-weight: 100">分辨率&nbsp;/位置&nbsp;</label>--%>
                            <br><br>
                            <c:if test="${not empty dto.packagingInfos}">
                                <c:forEach items="${dto.packagingInfos}" var="p" varStatus="go">
                                    <c:if test="${!go.first}">
                                        <br>
                                    </c:if>
                                    <label style="width: 20%;font-weight: 100">${p.pagename}</label>
                                    <select id="select_${p.pageid}" multiple class="form-control"
                                            style="width: 10%;overflow-y: hidden"
                                            size="2"
                                            onchange="changeImg(this.value,${p.pageid})">
                                        <option value="-2">浮动样式</option>
                                        <option value="-1" selected>Tab样式</option>
                                    </select>
                                    <select id="${p.pageid}" style="width: 5%;" sort="sort" name="${p.pageid}"
                                            class="form-control" onchange="sortSelect()">
                                    </select>
                                    <label id="label_${p.pageid}" style="display: none;" class="radio-inline">
                                        <label class="radio-inline">
                                            <input type="radio" name="inlineRadioOptions_${p.pageid}" id="inlineRadio1"
                                                   value="1"
                                                   onclick="radioSelect(this.value)"> 300*400
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" name="inlineRadioOptions_${p.pageid}" id="inlineRadio2"
                                                   value="2"
                                                   onclick="radioSelect(this.value)"> 400*600
                                        </label>
                                        <label class="radio-inline">
                                            <input type="radio" name="inlineRadioOptions_${p.pageid}" id="inlineRadio3"
                                                   value="3"
                                                   onclick="radioSelect(this.value)"> 600*800
                                        </label>
                                    </label>
                                    <select id="readySelect_${p.pageid}" style="display: none" class="form-control"
                                            onclick="show(id)"
                                            data-toggle="popover"
                                            data-trigger="focus"
                                            title="提示"
                                            data-content="">
                                    </select>
                                    <br>
                                </c:forEach>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            <c:if test="${empty packagingDtos}">
                <br>无记录<br>
            </c:if>
        </table>
        <input type="hidden" name="pageInfo" value="">
    </form>
    <input type="button" class="btn btn-primary" style="float: right;position: absolute;right: 100px;"
           onclick="release()" value="下一步">
    <input type="hidden" class="btn btn-primary" style="float: left"
           onclick="window.history.back(-1);" value="上一步">
    <br><br><br><br>
    <label> 第&nbsp;2&nbsp;步/共&nbsp;4&nbsp;步</label>
</div>
</body>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/pac_gl/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
    var pageIdList = [];
    var jsonIDName = {};
    $(document).ready(function () {
        var packingInfo = ${packagingDtos};
        var length = 0;
        for (var i = 0; i < packingInfo.length; i++) {
            length += parseInt(packingInfo[i].packagingInfos.length);
            for (var j = 0; j < packingInfo[i].packagingInfos.length; j++) {
                var obj = packingInfo[i].packagingInfos[j];
                jsonIDName[obj.pageid] = obj.pagename;
                pageIdList.push(obj.pageid);
            }
        }
        var str = "";
        for (var j = 0; j < length; j++) {
            var int = parseInt(j) + 1;
            str += "<option value='" + int + "'>" + int +
                    "</option>";
        }
        $("select[sort='sort']").append(str);
        $("select[sort='sort']").each(function (j) {
            $("select[sort='sort']")[j][j].selected = true;
        })
    });
    function sortSelect() {
        for (var i = 0; i < pageIdList.length; i++) {
            var id = pageIdList[i];
            var valueList = [];
            var idList = [];
            $("select[sort='sort']").each(function (i) {
                var objSort = $("select[sort='sort']")[i];
                idList.push(objSort.id);
                if (id != objSort.id) {
                    valueList.push(objSort.value);
                }
            })
            var typeList = [];
            var str = "";
            var typeValue = $("#select_" + id).val();
            if (typeValue == -1) {
                typeList = getWinList();
                str = "请选择它的上下排序";
                var optionsStr = splitStr(typeList, typeValue);
                var flag = getRepeatFlag($("#" + id).val());
                if ($.inArray($("#" + id).val(), valueList) != -1 && flag) {
                    $("#readySelect_" + id).attr("style", "");
                    $("#readySelect_" + id).attr("data-content", str);
                    $("#readySelect_" + id).empty();
                    $("#readySelect_" + id).append(optionsStr);
                } else {
                    $("#readySelect_" + id).attr("style", "display:none");
                    $("#readySelect_" + id).empty();
                }
            } else {
                typeList = getTabList();
                str = "请选择它的父页面";
                var optionsStr = splitStr(typeList, typeValue);
                $("#readySelect_" + id).attr("style", "");
                $("#readySelect_" + id).attr("data-content", str);
                $("#readySelect_" + id).empty();
                $("#readySelect_" + id).append(optionsStr);
            }
        }
    }
    function getRepeatFlag(value) {
        var oldValue = 0;
        var flag = false;
        $("select[sort='sort']").each(function (i) {
            var objSort = $("select[sort='sort']")[i];
            if (value == objSort.value) {
                if (oldValue == "") {
                    oldValue = parseInt($("#select_" + objSort.id).val());
                } else {
                    if ($("#select_" + objSort.id).val() == oldValue) {
                        flag = true;
                    } else {
                        oldValue = parseInt($("#select_" + objSort.id).val());
                    }
                }
            }
        })
        return flag;
    }
    function splitStr(list, type) {
        var str = "";
        if (type == -1) {
            str += "<option name='option_" + list[0] + "' value='" + 999 + "' selected>上</option>";
            str += "<option name='option_" + list[0] + "' value='" + 888 + "' selected>下</option>";
            return str;
        }
        for (var i = 0; i < list.length; i++) {
            var pageId = list[i];
            str += "<option id='option_" + pageId + "' value='" + pageId + "' selected>" + jsonIDName[pageId] + "</option>";
        }
        return str;
    }
    function getTabList() {
        var tabList = [];
        $("select[multiple]").each(function (i) {
            var obj = $("select[multiple]")[i];
            if (obj.value == -1) {
                tabList.push(obj.id.split("_")[1]);
            }
        })
        return tabList;
    }
    function getWinList() {
        var winList = [];
        $("select[multiple]").each(function (i) {
            var obj = $("select[multiple]")[i];
            if (obj.value == -2) {
                winList.push(obj.id.split("_")[1]);
            }
        })
        return winList;
    }
    function show(id) {
        $("#" + id).popover("show");
        setTimeout($("#" + id).popover("hidden"), 1000);
    }
    function changeImg(value, pageId) {
        if (-1 == value) {
            $("#label_" + pageId).prop("style", "display:none");
        } else {
            $("#label_" + pageId).prop("style", "");
        }
        sortSelect();
    }
    function release() {
        var pageInfo = {
            appId: "",
            functionList: []
        }
        pageInfo.appId = ${appId};
        var sortList = [];
        var repeatList = [];
        var sortAndTypeList = [];
        var positionList = [];
        var repeatFlag = false;
        var positionFlag = false;
        var flag = false;
        var flag2 = false;

        $("select[sort='sort']").each(function (j) {
            var sort = $("select[sort='sort']")[j].value;
            var pageId = $("select[sort='sort']")[j].name;
            var selectValue = $("#select_" + pageId).val();
            var type = "";
            var resolution = "null";
            var position = "null";
            var fatherid = "null";
            //判断选的页面顺序是否有重复
            if ($.inArray(sort, sortList) != -1) {
                //判断页面布局类型是不是和顺序值是否一起重复
                if ($.inArray(sort + "_" + selectValue, sortAndTypeList) == -1) {
                    flag2 = true;
                    return;
                }
                //判断重复是否超过2个
                if ($.inArray(sort, repeatList) != -1) {
                    repeatFlag = true;
                    return;
                }
                repeatList.push(sort);
                sortList.push(sort);
            } else {
                sortList.push(sort);
                sortAndTypeList.push(sort + "_" + selectValue);
            }
            if (selectValue == -2) {
                resolution = $("input[name='inlineRadioOptions_" + pageId + "']:checked").val();
                if (undefined == resolution) {
                    flag = true;
                }
                type = "win";
            } else {
                type = "tab";
            }
            if (flag) {
                return;
            }
            var readyValue = $("#readySelect_" + pageId).val();
            if (999 == readyValue) {
                position = "up";
            } else if (888 == readyValue) {
                position = "down";
            } else {
                fatherid = readyValue;
            }
            var idSort = pageId + "," + sort + "," + type + "," + resolution + "," + position + "," + fatherid;

            if (type == "tab" && $.inArray(sort + "," + position, positionList) != -1) {
                positionFlag = true;
            } else {
                positionList.push(sort + "," + position);
            }
            pageInfo.functionList.push(idSort);
        })
        if (positionFlag) {
            window.parent.layer.alert("页面上下排序有重复！");
            return;
        }
        if (flag2) {
            window.parent.layer.alert("页面排序值有重复！");
            return;
        }
        if (flag) {
            window.parent.layer.alert("请选择分辨率!");
            return;
        }
        if (repeatFlag) {
            window.parent.layer.alert("页面顺序重复值数量大于2个，请重新设置！");
            return;
        }
        $("input[name='pageInfo']").val(JSON.stringify(pageInfo));
        form.submit();
    }
</script>
</html>
