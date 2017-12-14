<%--
  Created by IntelliJ IDEA.
  User: bocom-qy
  Date: 2016/12/2
  Time: 10:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css"/>
</head>
<body>

<div class="table-toolbar" style="margin-left: 10px;float: left">
    <form class="form-inline text-left" name="form" onkeydown="if(event.keyCode==13)return false;">
        <br><br>
        <label class="control-label" style="width: 100px"> 应用名称&nbsp;：</label>
        <input id="appNameInput" name="appName" class="form-control" placeholder="输入应用名称"
               maxlength="50">&nbsp;&nbsp;<label id="alert"></label>
        <br><br>
        <label class="control-label" style="width: 100px"> 应用描述&nbsp;：</label>
        <textarea class="form-control" id="appDesc" name="appDesc" rows="6" cols="60"></textarea>
        <br><br>
        <label class="control-label" style="width: 100px"> 有效期&nbsp;：</label>
        <select id="statusSelect" class="form-control" onchange="selectChange(this.value)">
            <option id="-1" value="-1">请选择</option>
            <option id="1" value="0" selected="selected">永久</option>
            <option id="2" value="30">30天</option>
            <option id="3" value="15">15天</option>
            <option id="4" value="7">7天</option>
            <option id="5" value="5">其他</option>
        </select>
        <input id="validityPeriod" class="form-control" placeholder="输入整数的有效天数" style="display: none">
        <br>
        <br>
        <br>
        <input type="button" style="margin-left: 50px;" id="saveBack" class="btn btn-primary" onclick="save()"
               value="新建">
        <%--        <input type="button" id="saveContinue" class="btn btn-primary" onclick="addFunction('addFunction')"
                       value="继续添加功能">--%>
        <input type="hidden" id="appInfo" name="appInfo" value="">
    </form>
</div>
</body>
<script type="text/javascript"
        src="${pageContext.request.contextPath}/pac_gl/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
    $(function () {
        $("#appNameInput").bind('input porpertychange', function () {
            var length = $("#appNameInput").val().length;
            var html = (length) + "/50";
            $("#alert").empty().append(html);
        });
    });

    var commonParam = [];
    var resource = ${resource};

    function selectChange(value) {
        if (5 == parseInt(value)) {
            $("#validityPeriod").attr("style", "display:false");
        } else {
            $("#validityPeriod").attr("style", "display:none");
        }
    }

    function save() {
        var appName = $("#appNameInput").val();
        var appDesc = $("#appDesc").val();
        if ("" == appName) {
            window.parent.layer.alert("请输入应用名称!", {icon: 2});
            return;
        }
        if (checkName(appName)) {
            window.parent.layer.alert("应用名称不能包含特殊字符以及引号", {icon: 2});
            return;
        }
        if ("" == appDesc) {
            window.parent.layer.alert("请输入应用描述!", {icon: 2});
            return;
        }
        if ((appDesc + '').length > 150) {
            window.parent.layer.alert("应用描述不能超过150个字!", {icon: 2});
            return;
        }
        //有效期 暂时未做
        var autoUninstall = $("#statusSelect").val();
        var validityTime = 0;
        if (-1 == autoUninstall) {
            window.parent.layer.alert("请选择有效期天数!", {icon: 2});
            return;
        }
        if (5 != autoUninstall && 0 != autoUninstall) {
            validityTime = autoUninstall;
        } else if (5 == autoUninstall) {
//            var value = parseFloat($("#validityPeriod").val());
            var value = $("#validityPeriod").val();
            console.log("value   " + value + "  isNaN " + isNaN(value));
            if (isNaN(value)) {
                window.parent.layer.alert("请输入正确的有效期天数!", {icon: 2});
                return;
            }
            if (isNaN(value) && !isInteger(value)) {
                window.parent.layer.alert("请输入正确的有效期天数!", {icon: 2});
                return;
            } else {
                validityTime = value;
            }
        }

        var appInfo = {
            appName: appName,
            appDesc: appDesc,
            autoUninstall: autoUninstall,
            validityTime: validityTime
        };
//        $("#appInfo").val();
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            type: 'get',
            dataType: "json",
            data: {
                appInfo: encodeURI(JSON.stringify(appInfo))
            },
            url: "${pageContext.request.contextPath}"
            + "/manager/app/addAppBaseInfo",
            error: function () {//请求失败处理函数
                alert('请求失败');
            },
            success: function (res) { //请求成功后处理函数。
                if ("success" != res.result) {
                    window.parent.layer.msg(res.reason, {icon: 5});
                } else {
                    if ("1" != resource) {
                        window.parent.layer.confirm('新建成功，是否为该应用添加功能？', {
                            btn: ['是', '否']
                        }, function (index, layero) {
                            window.parent.menuClick("market");
                            window.parent.layer.closeAll();
                        }, function (index) {
                            window.parent.layer.msg('成功!', {icon: 1});
                        });
                    } else {
                        var appInfo = JSON.parse(res.data);
                        parent.$(".layui-layer-setwin>a", parent.doucment).trigger('click');
                    }
                }
            }
        });
    }

    function checkName(val) {
        var patrn = /[`"~!@#$%^&*()_\-+=<>?:"{}|\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’、]/im;
        return patrn.test(val);
    }

    function isInteger(obj) {
        return Math.floor(obj) === obj
    }

    function addFunction(url) {
        commonParam = {
            appName: encodeURI(encodeURI($("#appNameInput").val())),
            status: $("#statusSelect").val()
        }
    }
</script>
</html>
