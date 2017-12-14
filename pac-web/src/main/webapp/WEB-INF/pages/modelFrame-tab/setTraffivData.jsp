<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta charset="UTF-8">
	<title></title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/css/bootstrap.min.css" />
	<style type="text/css">
		.active{
			background-color: #f6f6f6 !important;
		}
		table,td,th{
			border:0 none;
		}
		td,th{
			padding: 5px;
		}
		.inp{
			border:1px solid #eee;
			padding:4px;
		}
	</style>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/bootstrap.min.js"></script>
	<!--[if lt IE 9]>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/html5shiv.min.js"></script>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/respond.min.js"></script>
	<![endif]-->
</head>

<body>
	<div style="margin:10px;">
		<div style="text-align:center;">
			<input type="text" class="form-control" style="width:150px;margin-left: auto;
    margin-right: auto;" id="title" />
		</div>
		<div id="groupList" style="width:90%; margin-left:5%;">
		</div>
		<form class="form-horizontal" id="form" style="margin: 20px;">
		</form>
		<div class="form-group">
			<div class="col-xs-12">
				<button class="btn btn-default" type="button" id="addGroup">添加分组</button>
			</div>
		</div>
		<button class="btn btn-default" style="float:right;" id="submit">确认修改</button>
	</div>
</body>
<script type="text/javascript">
	var p = window.parent;
	var editingFrame = p.editingWidget.frame[p.editingWidgetIndex];
  var data = deepClone(p.editingWidget.frame[p.editingWidgetIndex].data);
	
	var selIndexList = [];
	var editImg;
	
	var oldThs = [];
	
	var old;
	$(document).on("ready",function(){
		$("#title").val(data.title);
		$("#colMax").val(data.colMax);
		
		createTable();
		
		$("#groupList").on("change","input",function(e){
			var inp = $(e.currentTarget);
			if(inp.attr("type") == "checkbox"){
				if(inp.prop("checked")){
					inp.parent().parent().addClass('active');
					selIndexList.push(inp.parent().parent().index());
				}else{
					inp.parent().parent().removeClass('active');
					selIndexList.splice(inp.parent().parent().index(),1);
				}
			}
		});
		
		$("#groupList").on("click","img",function(e){
			editImg = $(e.currentTarget);
			p.iconsLayer = p.layer.open({
				type: 2,
				title: '图片选择',
				shade: 0.3,
				area: ['400px', '300px'],
				content: '../../widgetFrame/init/resources/color_icons/icons.html#1', //iframe的url
				cancel: function(index, layero){
					p.iconsLayer = null;
				}
			});
		});
		var innerStrYun = '<div class="points">'+
								'<div class="row" style="margin-bottom:10px;">'+
								'<div class="col-xs-2">'+
									'<img src="${pageContext.request.contextPath}/widgetFrame/init/resources/color_icons/add.png" style="width:47px; height: 47px;" />'+
								'</div>'+
								'<div class="col-xs-4">'+
									'<input type="text" class="form-control" placeholder="拥堵情况" value="" />'+
								'</div>'+
								'<div class="col-xs-4">'+
									'<input type="text" class="form-control" placeholder="数量" value="" />'+
								'</div>'+
								'<div class="col-xs-1"><button type="button" class="btn btn-default"><span class="glyphicon glyphicon-minus"></span></button></div>'+
							'</div>'+
						'</div>';
		$("#addRow").on("click",function(){						   
			$("#groupList").append(innerStrYun);
		});
		$("#addGroup").on("click",function(){
			//添加项
			var innerStr = '<fieldset style="margin-top: 10px;">' +			
					'<div class="form-group">'+
						'<div class="row" style="margin-bottom:10px;">'+
							'<label for="title" class="col-xs-2 control-label" style="padding-top:7px;">标题</label>'+
							'<div class="col-xs-9">'+
								'<input type="text" id="title" class="form-control" value="">'+
							'</div>'+
						'</div>'+
					'</div>'+
					'<div class="form-group">'+
						'<div class="row" style="margin-bottom:10px;">'+
							'<label for="title" class="col-xs-2 control-label" style="padding-top:7px;">路段</label>'+
							'<div class="col-xs-9">'+
								'<input type="text" id="title" class="form-control" value="">'+
							'</div>'+
						'</div>'+					
						innerStrYun+
					 '<button class="btn btn-default" type="button">增加路段</button> <button class="btn btn-default" type="button">删除该分组</button>'+
					'</div>'+
				'</fieldset>';
			$("#groupList").append(innerStr);

		});
		$("#groupList").on("click","fieldset button",function(e){
			var btn = $(e.currentTarget);
			switch(btn.html()){
				case "增加路段":
			    var innerStr = '<div class="points">'+
								'<div class="row" style="margin-bottom:10px;">'+
								'<div class="col-xs-2">'+
									'<img src="${pageContext.request.contextPath}/widgetFrame/init/resources/color_icons/add.png" style="width:47px; height: 47px;" />'+
								'</div>'+
								'<div class="col-xs-4">'+
									'<input type="text" class="form-control" placeholder="拥堵情况" value="" />'+
								'</div>'+
								'<div class="col-xs-4">'+
									'<input type="text" class="form-control" placeholder="数量" value="" />'+
								'</div>'+
								'<div class="col-xs-1"><button type="button" class="btn btn-default"><span class="glyphicon glyphicon-minus"></span></button></div>'+
							'</div>'+
						'</div>';
					btn.before(innerStr);
					break;
				case "<span class='glyphicon glyphicon-minus'></span>":
					btn.parent().parent().parent().remove();
					break;
				case "删除该分组":
					var container = btn.parent().parent();
					container.remove();
					break;
				default:
				btn.parent().parent().parent().remove();
					break;
			}
		});		

		$("#submit").on("click",function(){
			var obj = {lists:[]};
			obj.title = $("#title").val();
			obj.colMax = parseInt($("#colMax").val());
			//field
			var fieldsetList = $("#groupList").find("fieldset");
			for(var i = 0;i<fieldsetList.length;i++){
				var traffficObj = {points:[]};
				traffficObj.name = fieldsetList.eq(i).find('input').eq(0).val();
				traffficObj.value = fieldsetList.eq(i).find('input').eq(1).val();
				var points = fieldsetList.eq(i).find('.points');
				for(var j=0;j<points.length;j++){
					var inps = points.eq(j).find("input");
					traffficObj.points.push({
						name:inps.eq(0).val(),
						icon:points.eq(j).find("img").eq(0).attr("src"),
						value:inps.eq(1).val()
					});
				}
				obj.lists.push(traffficObj);		
			}
			editingFrame.data = obj;
			editingFrame.useUserData = true;
			p.$("#"+editingFrame.frameId+"_tools").find("li").eq(0).find("a").addClass("menu-active");
			var frame = p.$("#"+editingFrame.frameId)[0].contentWindow;
			frame.res = obj;
			frame.colMax = obj.colMax;
			frame.createTable();
			p.editingWidget = null;
			p.editingWidgetIndex = null;
			p.layer.close(p.editingLayer);
		});
	});
	
	function createTable(){
		var innerStr = '';

		for(var i=0;i<data.lists.length;i++){
			var pointStr="";
			for(var j =0;j<data.lists[i].points.length;j++){
					var icon = data.lists[i].points[j].icon?'<strong><img src="'+data.lists[i].points[j].icon+'" /></strong>':'<strong><img src="${pageContext.request.contextPath}/widgetFrame/init/resources/color_icons/add.png" style="width:47px; height: 47px;" /></strong>';
					pointStr += '<div class="points">'+
										'<div class="row" style="margin-bottom:10px;">'+
										'<div class="col-xs-2">'+icon+
										'</div>'+
										'<div class="col-xs-4">'+
											'<input type="text" class="form-control" placeholder="拥堵情况" value="'+data.lists[i].points[j].name+'" />'+
										'</div>'+
										'<div class="col-xs-4">'+
											'<input type="text" class="form-control" placeholder="数量" value="'+data.lists[i].points[j].value+'" />'+
										'</div>'+
										'<div class="col-xs-1"><button type="button" class="btn btn-default"><span class="glyphicon glyphicon-minus"></span></button></div>'+
									'</div>'+
								'</div>';

				}
			innerStr += '<fieldset style="margin-top: 10px;">' +			
				'<div class="form-group">'+
					'<div class="row" style="margin-bottom:10px;">'+
						'<label for="title" class="col-xs-2 control-label" style="padding-top:7px;">标题</label>'+
						'<div class="col-xs-9">'+
							'<input type="text" id="title" class="form-control" value="'+data.lists[i].name+'">'+
						'</div>'+
					'</div>'+
				'</div>'+
				'<div class="form-group">'+
					'<div class="row" style="margin-bottom:10px;">'+
						'<label for="title" class="col-xs-2 control-label" style="padding-top:7px;">路段</label>'+
						'<div class="col-xs-9">'+
							'<input type="text" id="title" class="form-control" value="'+data.lists[i].value+'">'+
						'</div>'+
					'</div>'+				
					pointStr+
					'<button class="btn btn-default" type="button">增加路段</button> <button class="btn btn-default" type="button">删除该分组</button>'+
				'</div>'+
			'</fieldset>';
		}

		$("#groupList").html(innerStr);
	}
	
	function getUrl(url){
		editImg.attr("src","${pageContext.request.contextPath}/widgetFrame/init/resources/color_icons/"+url);
	}
	
	//深度克隆
	function deepClone(obj){
	    var result,oClass=isClass(obj);
	    //确定result的类型
	    if(oClass==="Object"){
	      result={};
	    }else if(oClass==="Array"){
	      result=[];
	    }else{
	      return obj;
	    }
	    for(key in obj){
				var copy=obj[key];
				if(isClass(copy)=="Object"){
					result[key]=arguments.callee(copy);//递归调用
				}else if(isClass(copy)=="Array"){
					result[key]=arguments.callee(copy);
				}else{
					result[key]=obj[key];
				}
	    }
	    return result;
	}
	//返回传递给他的任意对象的类
	function isClass(o){
		if(o===null) return "Null";
		if(o===undefined) return "Undefined";
		return Object.prototype.toString.call(o).slice(8,-1);
	}
</script>
</html>