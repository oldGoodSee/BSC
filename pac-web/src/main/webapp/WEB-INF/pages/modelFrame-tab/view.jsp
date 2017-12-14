<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta charset="UTF-8">
	<title></title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/css/bootstrap.min.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/css/view.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/layer/layer.js"></script>
	<!--[if lt IE 9]>
		<style type="text/css">
			.layui-layer{
				filter: progid:DXImageTransform.Microsoft.Shadow(Strength=1,Direction=0,Color='#999999')
						progid:DXImageTransform.Microsoft.Shadow(Strength=1,Direction=90,Color='#999999')
						progid:DXImageTransform.Microsoft.Shadow(Strength=1,Direction=180,Color='#999999')
						progid:DXImageTransform.Microsoft.Shadow(Strength=1,Direction=270,Color='#999999');
			}
		</style>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/html5shiv.min.js"></script>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/respond.min.js"></script>
	<![endif]-->
</head>

<body>
<div class="header">
	<div class="logo" id="logo">警务应用组装</div>
	<div class="user-info">
		<a href="javascript:void(0)" class="quit link-white" onclick="loginCasOut()"></a>
		<div class="user-txt">
			<span class="user-mame txt-white">${sessionUserInfo.userName }</span><br/> <a
				class="user-role link-white no-decoration"
				href="javascript:void(0)" id="user">${sessionUserInfo.orgName }<i
				class="fa fa-chevron-down"></i></a>
		</div>
		<div class="user-avatar"></div>
	</div>
</div>

<a href="javascript:void(0)" id="hideShowTab"  class="tab-show">
	<span class="glyphicon glyphicon-chevron-down"></span>
</a>

<div id="notice" class="notice"></div>

<!-- Nav tabs -->
<div class="tab-title" id="tabTitleContainer">
	<ul class="nav nav-tabs" role="tablist" id="tabTitle">
		<!--<li role="presentation" class="active">
            <a href="#home" role="tab" data-toggle="tab">Home</a>
        </li>-->
	</ul>
</div>
<!-- Tab panes -->
<div class="tab-content my-tab-content" id="tabContent">
	<!--<div role="tabpanel" class="tab-pane active" id="home">home...</div>-->
</div>
<div id="tabMore" class="dropdown" style="position:absolute; z-index: 100; right:10px; top:90px; display:none;">
	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown">
		<span class="glyphicon glyphicon-list" style="font-size: 20px;"></span>
	</button>
	<ul id="tabMoreList" class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="dropdownMenu1">
		
	</ul>
</div>
</body>
<script type="text/javascript">
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
	var userName = '${sessionUserInfo.userName }';
	
	var publishObj,activeIndex;
	//用于存储布局的html，是否正在递归调用中
	var layoutStr = "",beginLayout = false;
	//用于滚动播放
	var flagNotice,marginT = 0;
	//tab的总长度
	var tabWidth = 0;
	//tab标题下拉或收起进行中的标志
	var tabShowing = false;
	
	$(document).on("ready",function() {

		//获取已有的布局模板
		$.ajax({
			dataType: "json",
			//type: "POST",
			url: "${pageContext.request.contextPath}/modelFrame-tab/data/preview.json",
			cache:false,
			success: function(result) {
				publishObj = result;
				createAppAll(publishObj);
			},
			error:function(){
				layer.alert("获取应用布局信息失败！");
			}
		});
		
		//获取通知
		$.ajax({
			dataType: "json",
			//type: "POST",
			url: "data/notice.json",
			cache:false,
			success: function(result) {
				if(result.success){
					var innerStr = "<ul>";
					if(result.data){
						for(var i=0;i<result.data.length;i++){
							innerStr += '<li><a href="'+result.data[i].url+'" target="_blank" title="'+result.data[i].title+'"><span class="glyphicon glyphicon-link"></span>&nbsp;&nbsp;'+result.data[i].title+'</a></li>';
						}
						innerStr += '<li><a href="'+result.data[0].url+'" target="_blank" title="'+result.data[i].title+'"><span class="glyphicon glyphicon-link"></span>&nbsp;&nbsp;'+result.data[0].title+'</a></li>';
						innerStr += "</ul>"
					}
					$("#notice").html(innerStr);
					scrollList();
				}else{
					//myAlert("获取应用通知信息失败！");
				}
			},
			error:function(){
				//layer.alert("获取应用通知信息失败！");
			}
		});
		
		//resize事件
		$(window).on("resize",function(){
			checkShowMore();
			var containerW = $("#tabTitleContainer").width();
			var marginL = $("#tabTitle").css("marginLeft")?parseInt($("#tabTitle").css("marginLeft")):0;
			var positionL = $("#tabTitle").find(".active").eq(0).position().left;
			var liW = $("#tabTitle").find(".active").eq(0).width();
			if(tabWidth <= containerW || activeIndex == 0){
				$("#tabTitle").css({
					"marginLeft":0
				});
			}else if(positionL + liW > containerW ){
				$("#tabTitle").css({
					"marginLeft":marginL - (positionL + liW - containerW) +"px"
				});
			}else if(activeIndex == $("#tabTitle").children().length-1){
				$("#tabTitle").css({
					"marginLeft":(marginL + (containerW - positionL - liW)) +"px"
				});
			}
		});

		//增加tab切换事件
		$("#tabTitle").on('click','a',function(e){
			if($(e.currentTarget).parent().hasClass("active")){
				return;
			}
			hideAllWin()
			//切换window，先关闭全部window
			e.preventDefault();
			$(e.currentTarget).tab('show');
			activeIndex = $(e.currentTarget).parent().index();
			showAllWin();
			setFrameShow($(e.currentTarget).parent().index());
		});
		
		//鼠标进入通知栏，停止滚动
		$("#notice").on('mouseenter','a',function(e){
			clearInterval(flagNotice);
		});
		//鼠标移除通知栏，恢复滚动
		$("#notice").on('mouseleave','a',function(e){
			scrollList();
		});
		
		//显示或隐藏tab的title
		$("#hideShowTab").on("click",function(){
			if(tabShowing){
				return;
			}
			tabShowing = true;
			var icon = $(this).children().eq(0);
			var h = icon.hasClass("glyphicon-chevron-down")?"42px":"0";
			$("#tabTitleContainer").animate({ 
				height: h
			}, 500 ,function(){
				icon.toggleClass("glyphicon-chevron-down");
				icon.toggleClass("glyphicon-chevron-up");
				checkShowMore();
				tabShowing = false;
			});
		});
	});
	
	//这里重写这个是因为这里publishObj不需要改动过了
	function createAppAll(list){
		if(list.length==0){
			return;
		}
		activeIndex = 0;
		var title = "",content = "",li="";
		for(var i=0;i<list.length;i++){
			var active = i == 0?"active":"";
			title += '<li role="presentation" class="'+active+'">'+
					 '	  <a href="#tab'+i+'" role="tab" data-toggle="tab">'+publishObj[i].pageName+'</a>'+
					 '</li>';
			content += '<div role="tabpanel" class="tab-pane '+active+'" id="tab'+i+'" templateIndex="'+i+'"></div>';
			li += '<li><a href="javascript:void(0)">'+publishObj[i].pageName+'</a></li>'
		}
		$("#tabTitle").append(title);
		$("#tabContent").append(content);
		showAllWin();
		for(var j=0;j<list.length;j++){
			//创建各个tab的布局，因为可能有多个tab，只有第一个tab是显示的，所以长宽使用第一个tab的长宽
			var container = $("#tabContent").find(".tab-pane").eq(j);
			var container0 = $("#tabContent").find(".tab-pane").eq(0);
			if(publishObj[j].panel.length==0){
				container.html('<div class="container-tip">尚未设置布局模板</div>');
			}else{
				container.html(getLayout(publishObj[j].panel,container0.width(),container0.height(),j));
			}
			beginLayout = false;
			layoutStr = "";
		}
		setFrameShow(0);
		//添加tab到下拉菜单.并添加切换事件
		$("#tabMoreList").append(li);
		$("#tabMoreList").find("a").on("click",function(){
			var index = $(this).parent().index();
			var ul = $("#tabTitle");
			var tabs  = ul.find("a");
			var positionL = tabs.eq(index).parent().position().left;
			var marginL = parseInt(ul.css("marginLeft"));
			var containerW = $("#tabTitleContainer").width();
			var liW = tabs.eq(index).parent().width();
			tabs.eq(index).trigger("click");
			if(positionL + liW> containerW){
				$("#tabTitle").css({
					"marginLeft":marginL - (positionL + liW- containerW) + "px"
				});
			}else if(positionL< 0){
				$("#tabTitle").css({
					"marginLeft":0
				});
			}
		});
	}
	
	//根据选择的模板或读取内容创建页面及resize对象
	//参数分别是模板json数组，容器宽，容器长，容器ID
	function getLayout(list,containerW,containerH,id){
		if(list.length==0){
			return;
		}
		if(!beginLayout){
			layoutStr="";
			beginLayout = true;
		}
		for(var i=0;i<list.length;i++){
			if(list[i].type != "container"){
				var inner = list[i].pageName?'<iframe  urlData="'+list[i].url+'?source=pac&userName='+userName+'" frameborder="0"></iframe>':'<div class="container-tip">尚未添加应用</div>';
				layoutStr += '<div class="float-l item" style="width:'+list[i].width+'; height:'+list[i].height+';" id="'+id+'_'+i+'">'+inner+'</div>';
			}else{
				layoutStr += '<div class="float-l" style="width:'+list[i].width+'; height:'+list[i].height+';" id="'+id+'_'+i+'">';
				arguments.callee(list[i].panel,list[i].width,list[i].Height,id+'_'+i);
				layoutStr += '</div>';
			}
		}
		return layoutStr;
	}
	
	//判断是否要显示更多的按钮
	function checkShowMore(){
		tabWidth = 0;
		var tabs = $("#tabTitle").children();
		for(var i=0;i<tabs.length;i++){
			tabWidth += tabs.eq(i).width();
		}
		if(tabWidth>$("#tabTitleContainer").width() && $("#hideShowTab").children().eq(0).hasClass("glyphicon-chevron-up")){
			$("#tabMore").fadeIn();
			$("#tabTitleContainer").css("right","60px");
		}else{
			$("#tabMore").fadeOut();
			$("#tabTitleContainer").css("right","10px");
		}
	}
	
	//显示tab的frame链接
	function setFrameShow(index){
		var frames = $("#tabContent").children().eq(index).find("iframe");
		for(var i=0;i<frames.length;i++){
			frames.eq(i).attr("src",frames.eq(i).attr("urlData"));
		}
	}
	
	//用于tab切换时隐藏对应win
	function hideAllWin(){
		for(var i=0;i<publishObj[activeIndex].win.length;i++){
			$("#"+publishObj[activeIndex].win[i].id).parent().hide();
		}
	}
	
	//用于tab切换时显示对应win
	function showAllWin(){
		var topWin;
		for(var i=0;i<publishObj[activeIndex].win.length;i++){
			if($("#"+publishObj[activeIndex].win[i].id).length != 0){  //已创建，显示
				$("#"+publishObj[activeIndex].win[i].id).parent().show();
			}else{
				if(publishObj[activeIndex].win.length>=5){  //超过5个已最小化形式显示
					createWin(publishObj[activeIndex].win[i],true);
				}else{
					if(publishObj[activeIndex].win[i].id == publishObj[activeIndex].topWinIndex){
						topWin = publishObj[activeIndex].win[i];
					}else{	//需要动态创建
						createWin(publishObj[activeIndex].win[i],false);
					}
				}
			}
		}
		if(topWin){  //强制把topWin置顶
			createWin(topWin,false);
		}
	}
	
	//自定义封装了创建win的方法
	function createWin(obj,flag){
		var area = obj.area?obj.area:['600px','400px'];
		var content = obj.pageName?'<iframe src="'+obj.url+'?source=pac&userName='+userName+'" frameborder="0"></iframe>':'<div class="item" style="width:100%; height:100%;"><div class="container-tip">尚未添加应用</div></div>';
		var config = {
		  	type: 1,
		  	title:obj.winTitle,
		  	area:area,
		  	closeBtn: 0,
		  	maxmin: true,
		  	offset:obj.offset,
		  	shade:0,
		  	id:obj.id,
		  	zIndex: layer.zIndex, //用于选中置顶
		  	content: content,
		  	success: function(layero, index){
				layer.setTop(layero);	//用于选中置顶
		   	},
		    min:function(DOM){
	   			DOM.eq(0).find(".layui-layer-max").eq(0).addClass("max-show");
		   	},
		   	restore:function(DOM){
		   		DOM.eq(0).find(".layui-layer-max").eq(0).removeClass("max-show");
		   	}
		};
	  	if(obj.offset){
	  		config.offset = obj.offset
	  	}
			if(flag){
				config.success = function(layero, index){
					setTimeout(function(){
						$("#layui-layer"+index).find(".layui-layer-min").eq(0).trigger("click");
						// layer.min(index);
					},50);
				};
			}
			layer.open(config);
	}
	
	//自定义的alert，因为这里的layer都置顶了，普通的alert会被覆盖
	function myAlert(title){
		var area = title.replace(/[\u4e00-\u9fa5]/g,"aa").length>57?['400px']:['260px'];
		layer.open({
		  	type: 1,
		  	title:'提示',
		  	area:area,
		  	shade:0.3,
		  	zIndex: layer.zIndex, //用于选中置顶
		  	content: '<div style="padding: 20px; line-height: 20px;">'+title+'</div>',
		  	btn: ['确定'],
		    btn1: function(index, layero){
		    	layer.close(index);
		  	},
		  	success: function(layero, index){
				layer.setTop(layero);	//用于选中置顶
		    }
		});
	}
	
	function scrollList(){
		flagNotice = setInterval(function(){
			var list = $("#notice").find("li");
			marginT--;
			list.eq(0).animate({
				"marginTop":(40*marginT)+"px"
			},300,"linear",function(){
				if(marginT <= (1-list.length)){
					list.eq(0).css({
						"marginTop":0
					});
					marginT = 0;
				}
			});
		},5000);
	}
</script>
</html>