<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<head>
  <meta charset="UTF-8">
  <title>微应用生成</title>
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/media/image/favicon.ico" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/css/bootstrap.min.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/widgetFrame/css/bootstrap-select.min.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/widgetFrame/css/main_component_manage.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/widgetFrame/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" />
  <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/jquery-1.9.1.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/js/bootstrap-select.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/layer/layer.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/js/laypage/laypage.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/js/ajaxfileupload.js"></script>
  <!--[if lt IE 9]>
  <script src="${pageContext.request.contextPath}/modelFrame-tab/js/html5shiv.min.js"></script>
  <script src="${pageContext.request.contextPath}/modelFrame-tab/js/respond.min.js"></script>

  <![endif]-->
</head>

<body id="body" oncontextmenu="return false;">
<%--<div class="header">--%>
<%--<div class="logo">微应用生成</div>--%>
<%--<div class="user-info">--%>
<%--<a href="javascript:void(0)" class="quit link-white" onclick="loginCasOut()"></a>--%>
<%--<div class="user-txt">--%>
<%--<span class="user-mame txt-white">${sessionUserInfo.userName }</span><br/>--%>
<%--<a class="user-role link-white no-decoration" href="javascript:void(0)" id="user">${sessionUserInfo.orgName }<i class="fa fa-chevron-down"></i></a>--%>
<%--</div>--%>
<%--<div class="user-avatar"></div>--%>
<%--</div>--%>
<%--</div>--%>

<div class="sidebar" id="sideBar">
  <div style="margin: 10px;">
    <select class="selectpicker" id="type1"></select>
  </div>
  <div style="margin: 10px;">
    <select class="selectpicker" id="type2">
      <option value="">全部</option>
    </select>
  </div>
  <div class="sidebar-content" id="sidebarContent">
    <ul id="defaultList"></ul>
  </div>
  <div class="sidebar-page" id="defaultListPage"></div>
</div>

<div class="main-content" id="mainContent">
  <iframe class="layout-iframe" frameborder="0" name="mainFrame" id="mainFrame" src=""></iframe>
</div>
<%--<div class="form-group">--%>
<%--<label class="col-sm-2 control-label"><span class="red-font">*</span>样例图</label>--%>
<%--<div class="col-sm-10" id="">--%>
<%--<div class="add-block">--%>
<%--< img  class="fileImg" width="200"--%>
<%--style="display: inline-block">--%>
<%--<div class="img-reday" id="show-img">--%>
<%--选择图片 <input type="file" class="upi file" id="logoWeb"--%>
<%--name="logoWeb" onchange="imgShow(this)">--%>
<%--</div>--%>
<%--<div class="img-reday" id="upload-img" style="display: none">开始上传--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<div class="sidebar-r" id="sidebarR">
  <form id="editContent" style="visibility: hidden;"></form>
  <button class="btn btn-primary" id="save" style="visibility: hidden; float: right; margin: 15px 15px 0 0;">保存</button>
  <%--style="display: inline-block">--%>
  <!--input type="file" class="upi file" id="logoWeb"
         name="logoWeb"  style="display: block" onchange="imgShow(this)"-->
  <%--<div class="img-reday" id="upload-img" onclick="uploadImg1()">开始上传--%>
	<div id="saveForm" style="display:none;">
		<form class="form-horizontal" id="myUploadForm" style="overflow:hidden; margin:10px;">
			 <div class="form-group">
				<label for="inpWidgetName" class="col-xs-3 control-label">组件名称</label>
				<div class="col-xs-9">
					<input type="text" class="form-control" id="inpWidgetName" placeholder="组件名称">
				</div>
			</div>
			<div class="form-group">
				<div class="col-xs-3"></div>
				<div class="col-xs-9">
					<input type="file" class="upi file" id="logoWeb" name="logoWeb" style="border:0 none;" onchange="imgShow(this)" />
				</div>
			</div>
			<img class="fileImg" style="width:320px; margin-bottom:10px; display:none;">
			<!--div style="border:1px solid #ccc; padding: 10px; margin-bottom: 15px; background-color:#eee;">
				<div class="form-group">
					<label>图片上传</label>
				</div>
				<div class="form-group">
					<input type="file" class="upi file form-control" id="logoWeb" name="logoWeb"  style="display: block" onchange="imgShow(this)" />
				</div>
				<img class="fileImg" style="width:200px; margin-bottom:10px; display:none;">
				<div class="form-group" style="text-align:right;">
					<button class="btn btn-default" type="button" id="uploadImg">上传</button>
				</div>
			</div-->
		</form>
	</div>
</div>

<div id="hiddenContainer" style="display:none;"></div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/js/fnList-1.0.js"></script>
<script type="text/javascript">
    //存储接口列表信息
    var interfaceList;
    //按接口类型存储options
    var interfaceOptions = {};
    //正在编辑的接口(interfaceList中的)
    var editingInterface;
    //正在编辑的接口(子页面传入的)
    var editingUrl;
    //正在编辑的参数的dom
    var editingParamDom;
    //用于创建子页面的congfig
    var config = {};
    //全局存储子页面传过来的urlList
    var urlList;
    //正在选择url的btn
    var urlBtn;
    //sql设置使用的btn
    var sqlBtn;
    //正在选择参数的btn
    var paramBtn;
    //子页面内容
    var childPage;
    //正在编辑的title
    var editingTitle;
    //通过type和参数数量过滤接口
    var urlFliter = [];
    //这里三个分类都有自己单独的页码
    var widgetType = 1;

    var baseUrl = "${pageContext.request.contextPath}";

    var tableList;

    var typeCode1,typeCode2,typeCode2Name;
		
		var imgPath = '';

    function loginCasOut() {
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            type: 'POST',
            dataType: "json",
            url: "${pageContext.request.contextPath}" + "/manager/loginAction/loginCasOut",
            error: function() {
                alert('请求失败');
            },
            success: function(res) { //请求成功后处理函数。
                if(res.success) {
                    window.location.href = "${casServerUrlPrefix}/logout?service=<%=request.getScheme()+": //"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()%>/manager/loginAction/loginCas";
                } else {
                    alert('退出失败！');
                }
            }
        });
    }

    //    function imgShow(dom) {
    //        var fileImg = $(".fileImg");
    //        var explorer = navigator.userAgent;
    //        var imgSrc = $(dom)[0].value;
    //        if (explorer.indexOf('MSIE') >= 0) {
    //            if (!/^([a-zA-Z0-9_]+)(.gif|.jpg|.jpeg|.GIF|.JPG|.JPEG|.png)/.test(imgSrc)) {
    //                imgSrc = "";
    //                fileImg.attr("src","/img/default.png");
    //                return false;
    //            }else{
    //                fileImg.attr("src",imgSrc);
    //                /* $("#show-img").hide();
    //                 $("#upload-img").show()*/
    //
    //            }
    //        }else{
    //            if (!/\.(jpg|jpeg|png|JPG|PNG|JPEG|gif|GIF)$/.test(imgSrc)) {
    //                imgSrc = "";
    //                fileImg.attr("src","/img/default.png");
    //                return false;
    //            }else{
    //                var file = $(dom)[0].files[0];
    //                var url = URL.createObjectURL(file);
    //                fileImg.attr("src",url);
    //                /*$("#show-img").hide();
    //                 $("#upload-img").show()*/
    //            }
    //        }
    //    }




    $(document).on('ready', function() {
        //获取初始控件列表类型
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            type: 'get',
            dataType: "json",
            url: "${pageContext.request.contextPath}/manager/menuAction/queryCategory",
            error: function() {
                layer.alert('获取控件类型失败');
            },
            success: function(res) { //请求成功后处理函数。
                if(res.success) {
                    var typeList = res.data
                    if(!typeList){
                        layer.alert('未获取到控件类型');
                        return;
                    }
                    if(typeList.length==0){
                        layer.alert('未获取到控件类型');
                        return;
                    }
                    var innerStr1 = '';
                    var innerStr2 = '<option value="-1">全部</option>';
                    for(var i=0;i<typeList.length;i++){
                        if(typeList[i].categoryType == "1"){
                            innerStr1 += '<option value="'+typeList[i].id+'">'+typeList[i].categoryName+'</option>';
                        }else{
                            innerStr2 += '<option value="'+typeList[i].id+'">'+typeList[i].categoryName+'</option>';
                        }
                    }
                    $("#type1").html(innerStr1);
                    $("#type2").html(innerStr2);
                    $('.selectpicker').selectpicker('refresh');
                    getInitWidget(1,typeList[0].id);
                } else {
                    layer.alert('获取控件类型失败');
                }
            }
        });
        // $("#uploadImg").click(function () {
            // var formData=new FormData();
            // formData.append('logoWeb', $('#logoWeb')[0].files[0]);
            // $.ajax({
                // type: "POST",
                // url: "${pageContext.request.contextPath}/manager/widget/uploadPhoto1",
                // data: formData,
                // processData : false,
                // contentType : false ,

                // success:function (res) {
                    // res=JSON.parse(res);
                    ////alert(res.data);
										// imgPath = res.data;
										// window.parent.layer.alert("上传成功");
                // },
                // error: function (data, status, e) {
                    // try{
                        // window.parent.layer.alert("上传失败");
                    // }catch(e){
                        // layer.alert("上传失败");
                    // }
                // }
            // })
        // })

        $('#type1').on('hidden.bs.select', function (e) {
            getInitWidget(1,$("#type1").selectpicker('val'),$("#type2").selectpicker('val'));
        });

        $("#type2").on("hidden.bs.select",function(){
            getInitWidget(1,$("#type1").selectpicker('val'),$("#type2").selectpicker('val'));
        });

        //通过名称搜搜
        $("body").on("click", "#interfaceSearch", function(e) {
            var btn = $(e.currentTarget);
            searchInterface(1,btn.attr("selBy"),$.trim($("#interfaceName").val()));
        });

        //弹出接口选择窗口
        $("#editContent").on("click", ".interface-btn", function(e) {
            urlBtn = $(e.currentTarget);
            for(var i = 0; i < urlList.length; i++) {
                if(urlList[i].id == urlBtn.attr("urlid")) {
                    editingUrl = urlList[i];
                    break;
                }
            }
            createSelWin(editingUrl,urlBtn.attr("selBy"));
        });

        //弹出区域选择窗口
        $("#editContent").on("click", ".area-sel", function(e) {
            var btn = $(e.currentTarget);
            var dbId = btn.parent().attr("dbId");
            sqlBtn = btn;
            createDbSetWin(dbId,"area");
        });

        //弹出数据库选择窗口
        $("#editContent").on("click", ".db-sel", function(e) {
            var btn = $(e.currentTarget);
            var dbId = btn.parent().attr("dbId");
            sqlBtn = btn;
            if(!config.dbSet[dbId].area){
                layer.alert("请先选择区域！");
                return;
            }
            createDbSetWin(dbId,"db");
        });

        //弹出绑定参数窗口
        $("#editContent").on("click", ".col-set>.paramset>button", function(e) {
            var btn = $(e.currentTarget);
            paramBtn = $(e.currentTarget);
            var params = config.paramsReady[btn.attr("urlId")];
            var paramBtns = "";
            for(var i=0;i<params.length;i++){
                //自身也显示，以便解除绑定
                var cls = "btn-default";
                if(params[i].key == btn.attr("key")){
                    cls = "btn-success";
                }
                //这里index不用-1，因为按钮放在一个单独的容器中
                paramBtns += '<button class="btn '+cls+' param-sel-btn" urlId="'+btn.attr("urlId")+'" key="'+params[i].key+'" paramIndex="'+btn.index()+'">'+params[i].name+'</button>';
            }
            layer.open({
                type: 1,
                title: "请选择需要绑定的参数", //不显示标题
                area: ['600px', '400px'],
                content: paramBtns
            });
        });

        //搜索表
        $("#editContent").on("click", ".table-list-search", function(e) {
            if(!tableList){
                layer.alert("尚未获得数据表信息！");
                return;
            }
            var innerStr = "";
            if(!$.trim($("#tableSearchInp").val())){
                for(var i=0;i<tableList.length;i++){
                    innerStr += '<li>'+tableList[i].tableName+'<span class="txt-gray">('+tableList[i].dataName+')</span></li>';
                }
            }else{
                for(var i=0;i<tableList.length;i++){
                    if(tableList[i].dataName){
                        if(tableList[i].dataName.indexOf($.trim($("#tableSearchInp").val())) != -1){
                            innerStr += '<li>'+tableList[i].tableName+'<span class="txt-gray">('+tableList[i].dataName+')</span></li>';
                        }
                    }
                }
            }
            $("#tableList").html(innerStr);
        });

        //接口选择
        $("body").on("click", ".layui-layer .layui-layer-content .interface-sel-btn", function(e) {
            //清除之前设置的config，因为弹窗有可能不选，所以放到这里
            delete config.url[editingUrl.id];
            delete config.params[editingUrl.id];
            delete config.colNames[editingUrl.id];
            delete config.title[editingUrl.id];
            if(editingUrl.selCol) { //如selCol为false，则表示该需要调用的url仅仅是为了获取数据
                var btn = $(e.currentTarget);
                for(var i = 0; i < interfaceList.length; i++) {
                    if("${pageContext.request.contextPath}/"+interfaceList[i].url == btn.attr("url")) {
                        //设置子页面的url
                        config.url[editingUrl.id] = "${pageContext.request.contextPath}/"+interfaceList[i].url;
                        config.urlName[editingUrl.id] = btn.html();
                        //设置页面元素
                        editingInterface = interfaceList[i];
                        urlBtn.html(interfaceList[i].name);
                        urlBtn.attr("title",interfaceList[i].name);

                        $("#explain").html(interfaceList[i].explain);
                        window.frames["mainFrame"].config = config;
                        window.frames["mainFrame"].init.urlFresh(editingUrl.id);
                        break;
                    }
                }
            }
            if(urlBtn.hasClass("btn-default")) {
                urlBtn.addClass("btn-success");
                urlBtn.removeClass("btn-default");
            }
            layer.closeAll();
        });

        //区域选择
        $("body").on("click", ".layui-layer .layui-layer-content .area-sel-btn", function(e) {
            var btn = $(e.currentTarget);
            if(btn.hasClass("btn-success")){
                return;
            }
            var dbId = btn.parent().attr("dbId");
            config.dbSet[dbId].area = btn.attr("dbId");
            delete config.dbSet[dbId].db;
            window.frames["mainFrame"].config = config;
            window.frames["mainFrame"].init.init();
            sqlBtn.addClass("btn-success");
            sqlBtn.removeClass("btn-default");
            sqlBtn.html(btn.html());
            sqlBtn.next().addClass("btn-default");
            sqlBtn.next().removeClass("btn-success");
            sqlBtn.next().html("请选择数据库");
            layer.closeAll();
            tableList = null;
            $("#tableList").empty();
            $("#tablePage").empty();
            $("#tableSearchInp").val("");
        });

        //数据库选择
        $("body").on("click", ".layui-layer .layui-layer-content .db-sel-btn", function(e) {
            var btn = $(e.currentTarget);
            if(btn.hasClass("btn-success")){
                return;
            }
            var dbId = btn.parent().attr("dbId");
            config.dbSet[dbId].db = btn.attr("dbId");
            config.dbSet[dbId].dbType = btn.attr("dbType").toLowerCase();
            window.frames["mainFrame"].config = config;
            window.frames["mainFrame"].init.init();
            sqlBtn.addClass("btn-success");
            sqlBtn.removeClass("btn-default");
            sqlBtn.html(btn.html());
            layer.closeAll();
            //获得数据库下所有的表
            getTable(1,config.dbSet[dbId].db);
        });

        //以控件为主绑定参数
        $("body").on("click", ".layui-layer .layui-layer-content .param-sel-btn", function(e) {
            var btn = $(e.currentTarget);
            if(btn.hasClass("btn-success")){
                //解除绑定
                delete config.params[paramBtn.attr("urlId")][paramBtn.index()].key;
                btn.addClass("btn-default");
                btn.removeClass("btn-success");
                paramBtn.addClass("btn-default");
                paramBtn.removeClass("btn-success");
                paramBtn.removeAttr("key");
            }else{
                //绑定
                config.params[paramBtn.attr("urlId")][paramBtn.index()].key = btn.attr("key");
                config.params[paramBtn.attr("urlId")][paramBtn.index()].name = btn.html();
                btn.parent().find(".btn-success").eq(0).removeClass("btn-success");
                btn.addClass("btn-success");
                btn.removeClass("btn-default");
                paramBtn.addClass("btn-success");
                paramBtn.removeClass("btn-default");
                paramBtn.attr("key",btn.attr("key"));
            }
        });

        //设置chart标题
        $("#editContent").on("click", ".btn-title", function(e) {
            var btn = $(e.currentTarget);
            editingTitle = btn.prev();
            setChartTitle(btn);
        });

        //接口内列的绑定控制
        $("#editContent").on("click", ".col-set>button", function(e) {
            var btn = $(e.currentTarget);
            selCol(btn, true);
        });

        //解除接口内列的绑定
        $("#editContent").on("dblclick", ".col-set>.result>span", function(e) {
            var span = $(e.currentTarget);
            unSelCol(span);
        });

        //参数改名
        $("#editContent").on("click", ".col-set>.interface-params-panel>a", function(e) {
            editingParamDom = $(e.currentTarget).parent();
            layer.prompt({ title: '请填写参数名称', formType: 3 }, function(text, index) {
                editingParamDom.children().eq(0).html(text);
                config.params[editingParamDom.attr("urlId")][editingParamDom.index() - 1].name = text;
                window.frames["mainFrame"].config = config;
                window.frames["mainFrame"].init.urlFresh(editingUrl.id);
                layer.close(index);
            });
        });

        //设置接口参数是否启用，必选参数无法设置
        //设置接口参数使用默认值
        $("#editContent").on("change", ".interface-params-panel>input", function(e) {
            var checkInp = $(e.currentTarget);
            var inps = checkInp.parent().find("input");
            switch(checkInp.attr("use")) {
                case "setUse":
                    //启用该参数
                    paramChange(checkInp);
                    break;
                case "useSettledValue":
                    //使用固定值
                    useSettled(checkInp);
                    break;
                case "useDefaultValue":
                    //使用默认值
                    useDefault(checkInp);
                    break;
                default:
                    //设置固定值，日期格式
                    if(!checkInp.hasClass("inp-date")) {
                        if(checkInp[0] == inps.last()[0]) {
                            setDefault(checkInp);
                        } else {
                            setSettled(checkInp);
                        }
                    }
                    break;
            }
        });

        //保存发布
        $("#save").on("click", function() {
            var extraParam = {};
            var list = [];
            extraParam.widgetType = 1;
            for(var i = 0;i<urlList.length;i++){
                if(!config.url[urlList[i].id]){
                    layer.alert("接口尚未配置完成");
                    return;
                }
            }
            extraParam.list = JSON.stringify(list);
            //window.frames["mainFrame"].$("#configJs").html("var config=" + JSON.stringify(config));
            $.ajax({
                dataType: "text",
                url: $("#mainFrame").attr("src"),
                cache: false,
                success: function(htmlStr) {
                    htmlStrList = htmlStr.split('<script type="text/javascript" id="configJs">');
                    save(htmlStrList[0]+'<script type="text/javascript" id="configJs">var config=' + JSON.stringify(config)+htmlStrList[1], extraParam);
                },
                error:function(){
                    layer.alert("读取信息失败！");
                }
            });
        });




        //切换tab
        $("#widgetTypeSel").find("button").on("click", function() {
            if($(this).hasClass("btn-primary")){
                return;
            }
            var oldBtn = $("#widgetTypeSel").find(".btn-primary").eq(0);
            oldBtn.removeClass("btn-primary");
            oldBtn.addClass("btn-default");
            $(this).removeClass("btn-default");
            $(this).addClass("btn-primary");
            widgetType = $(this).index();
            getInitWidget(pageObj[$(this).index()],$(this).index()+1);
        });
    });

    //获取初始微应用
    function getInitWidget(curr,type1,type2) {
        var dataObj = {
            pageNum: curr,
            pageSize: 20,
            typeCode1:type1,
            widgetTypeId:1
        }
        if(type2 != "-1"){
            dataObj.typeCode2 = type2;
        }
        $.ajax({
            dataType: "json",
            //type: "POST",
            url: "${pageContext.request.contextPath}/manager/widget/getWidgetInitList",
            data: dataObj,
            cache: false,
            success: function(result) {
                var innerStr = "";
                if(result.reason){
                    layer.alert(result.reason);
                    return;
                }
                for(var i = 0; i < result.data.length; i++) {
                    innerStr += '<li><a href="javascript:void(0)" initId="' + result.data[i].id + '" widgetName="' +
                        result.data[i].widget_name_show + '" type="initId" version="' + result.data[i].widget_version +
                        '" storagePath="'+result.data[i].storage_path+'" typeCode1="'+result.data[i].first_category_id+'" typeCode2="'+result.data[i].sencond_category_id+'" typeCode2Name="'+result.data[i].sencond_category_name+'">' + result.data[i].widget_name_show + '</a></li>';
                }
                $("#defaultList").html(innerStr);
                $("#defaultList").find("a").on("click", function() {
                    initId = $(this).attr("initId");
                    widgetName = $(this).attr("widgetName");
                    storagePath = $(this).attr("storagePath");
                    innerPage($(this).attr("initId"), "initId", $(this),widgetName,storagePath);
                    typeCode1 = $(this).attr("typeCode1");
                    typeCode2 = $(this).attr("typeCode2");
                    typeCode2Name = $(this).attr("typeCode2Name");
										$("#imgContent").show();
										imgPath = '';
                    $("#save").html("发布");
                    $("#mainContent").animate({
                        right: "310px"
                    }, 300);
                    $("#sidebarR").animate({
                        width: "300px"
                    }, 300);
                });
                //显示分页
                laypage({
                    cont: 'defaultListPage', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
                    pages: Math.ceil(result.count / 20), //通过后台拿到的总页数
                    curr: curr || 1, //当前页
                    skin: '#428bca',
                    groups: 2,
                    first: 1,
                    last: Math.ceil(result.count / 20),
                    prev: '<', //若不显示，设置false即可
                    next: '>',
                    jump: function(obj, first) { //触发分页后的回调
                        if(!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                            getInitWidget(obj.curr,type1,type2);
                        }
                    }
                });
            },
            error: function() {
                layer.alert("获取微应用信息失败！");
            }
        });
    }

    //根据左侧选择直接加载iframe或者根据id读取src,并重置editContent
    function innerPage(param, type, jqDom,widgetName,storagePath) {
        $("#editContent").empty();
        if(type == "id") {
            $.ajax({
                data: {
                    widgetId: param
                },
                url: '${pageContext.request.contextPath}/manager/widget/downloadWidget',
                cache: false,
                success: function(result) {
                    if(result.success) {
                        var now = new Date();
                        var url = "${pageContext.request.contextPath}/widgetFrame/app/" + result.data;
                        $("#mainContent").find("iframe").attr("src", url + "?_=" + now.getTime());
                        $("#editContent").css("visibility", "visible");
                        $("#save").css("visibility", "visible");
                    } else {
                        layer.alert(result.reason);
                    }
                },
                error: function() {
                    layer.alert("获取微应用路径失败");
                }
            });
        } else {
            $.ajax({
                data: {
                    initId: param,
                    widgetName: encodeURI(widgetName),
                    version: jqDom.attr("version"),
                    dfsUrl:storagePath
                },
                url: '${pageContext.request.contextPath}/manager/widget/initWidget',
                cache: false,
                success: function(result) {
                    if(result.success) {
                        var url = "${pageContext.request.contextPath}/widgetFrame/" + result.data
                        var now = new Date();
                        $("#mainContent").find("iframe").attr("src", url + "?_=" + now.getTime());
                        $("#editContent").css("visibility", "visible");
                        $("#save").css("visibility", "visible");
                    } else {
                        layer.alert(result.reason);
                    }
                },
                error: function() {
                    layer.alert("获取控件路径失败");
                }
            });
        }
    }

    //在页面进行操作前存储
    function writeChildPage(str) {
        childPage = str;
    }

    //获取返回的数据
    function getHtmlStr() {
        //window.frames["mainFrame"].$("#configJs").html("var config="+JSON.stringify(config));
        var page = $("#mainFrame").prop('contentWindow').document;
        var pageStr = $(page)[0].documentElement.innerHTML;
        var arr = childPage.split('<script type="text/javascript" id="configJs">');

        var headStr = pageStr.split("<body>")[0];
        var bodyStr = '<body>' + arr[0] + '</body>';
        var scriptStr = '<script type="text/javascript" id="configJs">var config=' + JSON.stringify(config) + ';' + arr[1];
        var returnStr = headStr + bodyStr + scriptStr;
        return returnStr;
    }

    //保存文件
    function save(htmlStr,extraParam) {
				layer.open({
					type: 1,
					shade: 0.3,
					title: "请填写标题及上传图片",
					area:['400px','300px'],
					content: $('#saveForm'),
					btn: ['确认', '取消'],
					yes: function(index, layero){
						if(!$("#inpWidgetName").val()){
							layer.alert("请填写标题");
							return;
						}
						if($('#logoWeb')[0].files.length==0){
							layer.alert("请选择图片");
							return;
						}
						var formData=new FormData();
            formData.append('logoWeb', $('#logoWeb')[0].files[0]);
            $.ajax({
                type: "POST",
                url: "${pageContext.request.contextPath}/manager/widget/uploadPhoto1",
                data: formData,
                processData : false,
                contentType : false ,

                success:function (res) {
                    res=JSON.parse(res);
                    //alert(res.data);
										var paramObj = {
												doc: htmlStr,
												widgetName: $("#inpWidgetName").val(),
												initId:parseInt(initId),
												typeCode1:typeCode1,
												typeCode2:typeCode2,
                                                typeCode2Name:typeCode2Name,
												appImg:res.data
										}
										$.extend(paramObj,extraParam);
										$.ajax({
												type: "POST",
												url: '${pageContext.request.contextPath}/manager/widget/addWidget',
												data: paramObj,
												cache: false,
												success: function(result) {
														if(result.success) {
																layer.alert("发布成功", function(i) {
																		document.getElementById("myUploadForm").reset();
																		$(".fileImg").hide();
																		layer.closeAll();
																});
														} else {
																layer.alert(result.reason, { icon: 2 });
														}
												},
												error: function() {
														layer.alert("发布失败");
												}
										});
										//数据控件，置为false，数据控件可继续编辑
										if(config.publish){
												config.publish = false;
										}
                },
                error: function (data, status, e) {
                    try{
                        window.parent.layer.alert("上传失败");
                    }catch(e){
                        layer.alert("上传失败");
                    }
                }
            })
					},
					btn2: function(index, layero){
						document.getElementById("myUploadForm").reset();
						$(".fileImg").hide();
					},
					cancel: function(){
						document.getElementById("myUploadForm").reset();
						$(".fileImg").hide();
					}
				});
        // layer.prompt({ title: '请填写标题，并确认', formType: 3, closeBtn:0 }, function(text, index) {
            // layer.close(index);
            // var paramObj = {
                // doc: htmlStr,
                // widgetName: text,
                // initId:parseInt(initId),
                // typeCode1:typeCode1,
                // typeCode2:typeCode2,
								// appImg:imgPath
            // }
            // $.extend(paramObj,extraParam);
            // $.ajax({
                // type: "POST",
                // url: '${pageContext.request.contextPath}/manager/widget/addWidget',
                // data: paramObj,
                // cache: false,
                // success: function(result) {
                    // if(result.success) {
                        // layer.alert("发布成功", function(i) {
                            // layer.close(i);
                        // });
                    // } else {
                        // layer.alert(result.reason, { icon: 2 });
                    // }
                // },
                // error: function() {
                    // layer.alert("发布失败");
                // }
            // });
            ////数据控件，置为false，数据控件可继续编辑
            // if(config.publish){
                // config.publish = false;
            // }
        // });
    }

    //通过名称搜索来查找接口
    function searchInterface(pageNum,selBy,interfaceName) {
        var paramsObj = {
            category:editingUrl.category
        };
        if(interfaceName){
            paramsObj.interfaceName = encodeURI(interfaceName);
        }
        //获取接口配置信息
        $.ajax({
            dataType: "json",
            //type: "POST",
            url: "${pageContext.request.contextPath}/manager/screen/category/list",
            data: paramsObj,
            cache: false,
            success: function(result) {
                interfaceList = result;
                var innerStr = "";
                for(var i = 0; i < result.length; i++) {
                    innerStr += '<button class="btn btn-default interface-sel-btn" type="button" url="${pageContext.request.contextPath}/' + result[i].url + '">' + result[i].name + '</button>';
                }
                $("#interfaceContainer").html(innerStr);
//          //显示分页
//          laypage({
//            cont: 'microAppListPage', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
//            pages: result.page.pages, //通过后台拿到的总页数
//            curr: curr || 1, //当前页
//            skin: '#428bca',
//            groups: 2,
//            first: 1,
//            last: result.page.pages,
//            prev: '<', //若不显示，设置false即可
//            next: '>',
//            jump: function(obj, first) { //触发分页后的回调
//              if(!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
//                getPubWidget(obj.curr);
//              }
//            }
//          });
            },
            error: function() {
                layer.alert("获取接口列表失败！");
            }
        });
    }

    function getDbInfo(dbId,type,pageNum){
        if(type == "area"){
            $.ajax({
                dataType: "json",
                //type: "POST",
                url: "${pageContext.request.contextPath}/server/widget/getDBInfo",
                data: {
                    pageNum:1,
                    pageSize: 100000
                },
                cache: false,
                success: function(result) {
                    if(result.success) {
                        dbList = result.data;
                        var innerStr = "";
                        for(var i = 0; i < dbList.length; i++) {
                            if(dbList[i].id == config.dbSet[dbId].area){
                                innerStr += '<button class="btn btn-success '+type+'-sel-btn" type="button" dbId="' + dbList[i].id + '" title="'+dbList[i].description+'" style="margin: 10px 0 0 10px;">' + dbList[i].areaName + '</button>';
                            }else{
                                innerStr += '<button class="btn btn-default '+type+'-sel-btn" type="button" dbId="' + dbList[i].id + '" title="'+dbList[i].description+'" style="margin: 10px 0 0 10px;">' + dbList[i].areaName + '</button>';
                            }
                        }
                        $("#dbInfo").html(innerStr);
                    } else {
                        layer.alert("获取区域列表失败！"+result.message);
                    }
                },
                error: function() {
                    layer.alert("获取区域列表失败！");
                }
            });
        }else{
            $.ajax({
                dataType: "json",
                //type: "POST",
                url: "${pageContext.request.contextPath}/server/widget/getDBInfo",
                data: {
                    areaId:config.dbSet[dbId].area,
                    pageNum:pageNum,
                    pageSize: 20
                },
                cache: false,
                success: function(result) {
                    if(result.success) {
                        dbList = result.data.list;
                        var innerStr = "";
                        for(var i = 0; i < dbList.length; i++) {
                            if(dbList[i].id == config.dbSet[dbId].db){
                                innerStr += '<button class="btn btn-success '+type+'-sel-btn" type="button" dbId="' + dbList[i].id + '" dbType="'+dbList[i].dbCategoryName+'" title="'+dbList[i].description+'" style="margin: 10px 0 0 10px;">' + dbList[i].instanceName + '</button>';
                            }else if(dbList[i].dbCategoryName == "mysql" || dbList[i].dbCategoryName == "hana"){
                                innerStr += '<button class="btn btn-default '+type+'-sel-btn" type="button" dbId="' + dbList[i].id + '" dbType="'+dbList[i].dbCategoryName+'" title="'+dbList[i].description+'" style="margin: 10px 0 0 10px;">' + dbList[i].instanceName + '</button>';
                            }else{
                                innerStr += '<button class="btn btn-danger" style="margin: 10px 0 0 10px;" disabled="disabled">' + dbList[i].instanceName + '</button>';
                            }
                        }
                        $("#dbInfo").html(innerStr);
                        //显示分页
                        laypage({
                            cont: 'dbInfoPage', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
                            pages: result.data.pages, //通过后台拿到的总页数
                            curr: pageNum || 1, //当前页
                            skin: '#428bca',
                            groups: 2,
                            first: 1,
                            last: result.data.pages,
                            prev: '<', //若不显示，设置false即可
                            next: '>',
                            jump: function(obj, first) { //触发分页后的回调
                                if(!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                                    getDbInfo(dbId,type,obj.curr);
                                }
                            }
                        });
                    } else {
                        layer.alert("获取数据库列表失败！"+result.message);
                    }
                },
                error: function() {
                    layer.alert("获取数据库列表失败！");
                }
            });
        }
    }

    function getTable(curr,id){
        $.ajax({
            dataType: "json",
            //type: "POST",
            url: "${pageContext.request.contextPath}/server/widget/getDBInfo",
            data: {
                pageNum: curr,
                pageSize: 20,
                serverId:id
            },
            cache: false,
            success: function(result) {
                if(result.success){
                    var innerStr = "";
                    tableList = result.data.list;
                    if(tableList.length>0){
                        for(var i=0;i<tableList.length;i++){
                            innerStr += '<li>'+tableList[i].tableName+'<span class="txt-gray">('+tableList[i].dataName+')</span></li>';
                        }
                    }else{
                        innerStr += '<p>没有数据！</p>';
                    }
                    $("#tableList").html(innerStr);
                    //显示分页
                    laypage({
                        cont: 'tablePage', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
                        pages: result.data.pages, //通过后台拿到的总页数
                        curr: curr || 1, //当前页
                        skin: '#428bca',
                        groups: 2,
                        first: 1,
                        last: result.data.pages,
                        prev: '<', //若不显示，设置false即可
                        next: '>',
                        jump: function(obj, first) { //触发分页后的回调
                            if(!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                                getTable(obj.curr,id);
                            }
                        }
                    });
                }else{
                    layer.alert("获取该数据库下的表信息失败！"+result.message);
                }
            },
            error:function(){
                layer.alert("获取该数据库下的表信息失败！");
            }
        });
    }

    //20171201 upload pic new!!!!////////////////////
    //    var imgJudge = /^((.*)+)(.gif|.jpg|.jpeg|.GIF|.JPG|.JPEG|.png|.PNG|.bmp|.BMP)/;
    //    var a =  new RegExp(imgJudge);

    function uploadPhoto(){
        var formData = new FormData();
        formData.append("logoWeb", $('#logoWeb')[0].files[0]);
        console.log("formData: "+formData);
        $.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/manager/widget/uploadPhoto1",
            data: formData,
            processData: false,
            contentType: false,
            success: function(res) {
                var data = $.parseJSON(res);
                if (data.success == true) {
                    layer.alert("上传成功");
                } else {
                    layer.alert("上传失败");
                }

            },
            error: function() {
                layer.alert("服务器正忙,请稍后再试");
            }
        });
    }

    //图片预览
    function imgShow(dom) {
			// console.log('in imgshow');
			var fileImg = $(".fileImg");
			var explorer = navigator.userAgent;
			var imgSrc = $(dom)[0].value;
			if (explorer.indexOf('MSIE') >= 0) {
				if (!/^([a-zA-Z0-9_]+)(.gif|.jpg|.jpeg|.GIF|.JPG|.JPEG|.png)/.test(imgSrc)) {
					imgSrc = "";
					fileImg.attr("src","/img/default.png");
					return false;
				}else{
					fileImg.attr("src",imgSrc);
					/* $("#show-img").hide();
					 $("#upload-img").show()*/

				}
			}else{
				if (!/\.(jpg|jpeg|png|JPG|PNG|JPEG|gif|GIF)$/.test(imgSrc)) {
					imgSrc = "";
					fileImg.attr("src","/img/default.png");
						return false;
				}else{
					var file = $(dom)[0].files[0];
					var url = URL.createObjectURL(file);
					fileImg.attr("src",url);
					/*$("#show-img").hide();
					 $("#upload-img").show()*/
				}
			}
			fileImg.show();
    }


</script>

</html>