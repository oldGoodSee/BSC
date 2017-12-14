<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta charset="UTF-8">
	<title></title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/css/bootstrap.min.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/js/spectrum/spectrum.css" />
	<style>
		fieldset{
			margin-bottom:10px;
			padding: 15px;
			border: 1px solid #ccc;
			background-color: #eee;
		}
		hr{
			border-color:#ccc;
			margin-top: 10px;
			margin-bottom: 15px;
		}
		fieldset{
			background-color:#fafafa;
			border: 1px solid #eee;
		}
		.citys{
			margin-top:10px;
		}
	</style>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/spectrum/spectrum.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/init/resources/echart/data/city/china-main-city-map.js"></script>
	<!--[if lt IE 9]>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/html5shiv.min.js"></script>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/respond.min.js"></script>
	<![endif]-->
</head>

<body>
	<div id="main"></div>
	<form class="form-horizontal" id="form" style="margin: 20px;">
		<div class="form-group">
			<label for="title" class="col-xs-2 control-label" style="padding-top:7px;">标题</label>
			<div class="col-xs-8">
				<input type="text" id="title" class="form-control" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<input type="checkbox" id="tm" /> 是否透明
			</div>
		</div>
		<div class="form-group">
			<label for="provinceList" class="col-xs-2 control-label" style="padding-top:7px;">选择省份</label>
			<div class="col-xs-8">
				<select type="text" id="provinceList" class="form-control"></select>
			</div>
		</div>
		<div class="form-group" id="citySel" style="display:none;">
			<label for="cityList" class="col-xs-2 control-label" style="padding-top:7px;">选择城市</label>
			<div class="col-xs-8">
				<select type="text" id="cityList" class="form-control"></select>
			</div>
		</div>
		<div class="form-group" id="groupList">
			
		</div>
		<div class="form-group">
			<div class="col-xs-12">
				<button class="btn btn-default" type="button" id="addGroup">添加分组</button>
			</div>
		</div>
		
   	<div class="form-group" id="btn">
	    <div style="float:right; margin: 0 20px;">
	      <button type="button" class="btn btn-default" id="submit">确认修改</button>
	    </div>
	  </div>
	</form>
</body>
<script type="text/javascript">
  var p = window.parent;
  var editingFrame = p.editingWidget.frame[p.editingWidgetIndex];
  var data = deepClone(p.editingWidget.frame[p.editingWidgetIndex].data);
  var tm = p.editingWidget.frame[p.editingWidgetIndex].tm;
	
	var firstLoad = true;
	
	$(document).on("ready",function() {
		var provinceList = [
			{
				"name":"北京",
				"code":"beijing",
				"citys":[]
			},
			{
				"name":"天津",
				"code":"tianjin",
				"citys":[]
			},
			{
				"name":"河北",
				"code":"hebei",
				"citys":[]
			},
			{
				"name":"山西",
				"code":"shanxi",
				"citys":[]
			},
			{
				"name":"内蒙古",
				"code":"neimenggu",
				"citys":[]
			},
			{
				"name":"辽宁",
				"code":"liaoning",
				"citys":[]
			},
			{
				"name":"吉林",
				"code":"jilin",
				"citys":[]
			},
			{
				"name":"黑龙江",
				"code":"heilongjiang",
				"citys":[]
			},
			{
				"name":"上海",
				"code":"shanghai",
				"citys":[]
			},
			{
				"name":"江苏",
				"code":"jiangsu",
				"citys":[]
			},
			{
				"name":"浙江",
				"code":"zhejiang",
				"citys":[]
			},
			{
				"name":"安徽",
				"code":"anhui",
				"citys":[]
			},
			{
				"name":"福建",
				"code":"fujian",
				"citys":[]
			},
			{
				"name":"江西",
				"code":"jiangxi",
				"citys":[]
			},
			{
				"name":"山东",
				"code":"shandong",
				"citys":[]
			},
			{
				"name":"河南",
				"code":"henan",
				"citys":[]
			},
			{
				"name":"湖北",
				"code":"hubei",
				"citys":[]
			},
			{
				"name":"湖南",
				"code":"hunan",
				"citys":[]
			},
			{
				"name":"广东",
				"code":"guangdong",
				"citys":[]
			},
			{
				"name":"广西",
				"code":"guangxi",
				"citys":[]
			},
			{
				"name":"海南",
				"code":"hainan",
				"citys":[]
			},
			{
				"name":"重庆",
				"code":"chongqing",
				"citys":[]
			},
			{
				"name":"四川",
				"code":"sichuan",
				"citys":[]
			},
			{
				"name":"贵州",
				"code":"guizhou",
				"citys":[]
			},
			{
				"name":"云南",
				"code":"yunnan",
				"citys":[]
			},
			{
				"name":"西藏",
				"code":"xizang",
				"citys":[]
			},
			{
				"name":"陕西",
				"code":"shanxi1",
				"citys":[]
			},
			{
				"name":"甘肃",
				"code":"gansu",
				"citys":[]
			},
			{
				"name":"青海",
				"code":"qinghai",
				"citys":[]
			},
			{
				"name":"宁夏",
				"code":"ningxia",
				"citys":[]
			},
			{
				"name":"新疆",
				"code":"xinjiang",
				"citys":[]
			},
			{
				"name":"台湾",
				"code":"taiwan",
				"citys":[]
			},
			{
				"name":"香港",
				"code":"xianggang",
				"citys":[]
			},
			{
				"name":"澳门",
				"code":"aomen",
				"citys":[]
			}
		];
		
		var innerList = "";
		for(var i=0;i<provinceList.length;i++){
			innerList += '<option value="'+provinceList[i].code+'">'+provinceList[i].name+'</option>';
		}
		$("#provinceList").html(innerList);
		
		$("#provinceList").val(data.titleENP);
		
		$("#title").val(data.title);
		$("#tm").prop("checked",tm);
		
		provinceSel();
		
		$("#provinceList").on("change",function(){
			provinceSel();
		});
		
		$("#cityList").on("change",function(){
			$("#groupList").empty();
		});
		
		$("#addGroup").on("click",function(){
			var pointStr =  '<div class="points">'+
												'<div class="row" style="margin-bottom:10px;">' +
													'<label class="col-xs-2 control-label" style="padding-top:7px;">点位名称</label>'+
													'<div class="col-xs-4">'+
														'<input type="text" class="form-control" placeholder="点位名称" value="" />'+
													'</div>'+
													'<label class="col-xs-2 control-label" style="padding-top:7px;">数量</label>'+
													'<div class="col-xs-4">'+
														'<input type="text" class="form-control" placeholder="数量" value="" />'+
													'</div>'+
												'</div>'+
												'<div class="row" style="margin-bottom:10px;">'+
													'<label class="col-xs-2 control-label" style="padding-top:7px;">经度</label>'+
													'<div class="col-xs-4">'+
														'<input type="text" class="form-control" placeholder="经度" value=""/>'+
													'</div>'+
													'<label class="col-xs-2 control-label" style="padding-top:7px;">纬度</label>'+
													'<div class="col-xs-4">'+
														'<input type="text" class="form-control" placeholder="纬度" value=""/>'+
													'</div>'+
												'</div>'+
												'<div class="row" style="margin-bottom:10px;">'+
													'<div class="col-xs-2">'+
														'<button class="btn btn-default" type="button">删除该标记点</button>'+
													'</div>'+
												'</div>'+
											'</div>';
			//添加项
			var innerStr = '<fieldset>' +
												'标题：<input type="text" class="group-title" style="border:1px solid #ccc;" /> '+
												'颜色：<input type="text" class="color-inp" /> <input type="text" class="color-code" value="#ffffff" /> '+
												'<hr />'+
												pointStr+
												'<button class="btn btn-default" type="button">增加标记点</button> <button class="btn btn-default" type="button">删除该分组</button>'+
											'</fieldset>';
			$("#groupList").append(innerStr);
			$("#groupList").find(".color-inp").last().spectrum({
				color: '#ffffff',
				change: function(color) {
					$(this).next().next().val(color.toHexString());
				}
			});
		});
		
		$("#form").on("click","fieldset button",function(e){
			var btn = $(e.currentTarget);
			switch(btn.html()){
				case "增加标记点":
					var innerStr =  '<div class="points">'+
														'<div class="row" style="margin-bottom:10px;">' +
															'<label class="col-xs-2 control-label" style="padding-top:7px;">点位名称</label>'+
															'<div class="col-xs-4">'+
																'<input type="text" class="form-control" placeholder="点位名称" value="" />'+
															'</div>'+
															'<label class="col-xs-2 control-label" style="padding-top:7px;">数量</label>'+
															'<div class="col-xs-4">'+
																'<input type="text" class="form-control" placeholder="数量" value="" />'+
															'</div>'+
														'</div>'+
														'<div class="row" style="margin-bottom:10px;">'+
															'<label class="col-xs-2 control-label" style="padding-top:7px;">经度</label>'+
															'<div class="col-xs-4">'+
																'<input type="text" class="form-control" placeholder="经度" value=""/>'+
															'</div>'+
															'<label class="col-xs-2 control-label" style="padding-top:7px;">纬度</label>'+
															'<div class="col-xs-4">'+
																'<input type="text" class="form-control" placeholder="纬度" value=""/>'+
															'</div>'+
														'</div>'+
														'<div class="row" style="margin-bottom:10px;">'+
															'<div class="col-xs-2">'+
																'<button class="btn btn-default" type="button">删除该标记点</button>'+
															'</div>'+
														'</div>'+
													'</div>';
					btn.before(innerStr);
					break;
				case "删除该标记点":
					btn.parent().parent().parent().remove();
					break;
				case "删除该分组":
					var container = btn.parent();
					container.remove();
					break;
				default:
					break;
			}
		});
	  
		$("#submit").on("click",function(){
			var obj = {points:[]};
			obj.title = $("#title").val();
			obj.titleEN = $("#cityList").val();
			obj.titleENP = $("#provinceList").val();
			var fieldsetList = $("#groupList").find("fieldset");
			for(var i=0;i<fieldsetList.length;i++){
				var provinceObj = {points:[]};
				provinceObj.name = fieldsetList.eq(i).find("input").eq(0).val();
				provinceObj.color = fieldsetList.eq(i).find("input").eq(2).val();
				var points = fieldsetList.eq(i).find('.points');
				for(var j=0;j<points.length;j++){
					var inps = points.eq(j).find("input");
					var v = [parseFloat(inps.eq(2).val()),parseFloat(inps.eq(3).val()),parseInt(inps.eq(1).val())];
					provinceObj.points.push({
						name:inps.eq(0).val(),
						value:v
					});
				}
				obj.points.push(provinceObj);
			}
			p.editingWidget.frame[p.editingWidgetIndex].data = obj;
			p.editingWidget.frame[p.editingWidgetIndex].tm = $("#tm").prop("checked");
			p.editingWidget.frame[p.editingWidgetIndex].useUserData = true;
			p.$("#"+editingFrame.frameId+"_tools").find("li").eq(0).find("a").addClass("menu-active");
			var frame = p.$("#"+editingFrame.frameId)[0].contentWindow;
			frame.useUserData = true;
			frame.tm = $("#tm").prop("checked");
			frame.getData();
			frame.bgtm();
			p.editingWidget = null;
			p.editingWidgetIndex = null;
			p.layer.close(p.editingLayer);
			
		});
	});
	
	function provinceSel(){
		var url = "${pageContext.request.contextPath}/widgetFrame/init/resources/echart/data/"+$("#provinceList").val()+".json";
		$.ajax({
			dataType: "json",
			//type: "POST",
			url: url,
			cache: false,
			success: function(result) {
				//如果是省，载入市
				if($("#provinceList").val() != "beijing" && $("#provinceList").val() != "tianjing" && $("#provinceList").val() != "shanghai" && $("#provinceList").val() != "chongqing" && $("#provinceList").val() != "xianggang" && $("#provinceList").val() != "aomen"){
					var innerStr = "";
					for(var i=0;i<result.features.length;i++){
						innerStr += '<option value="'+result.features[i].id+'">'+result.features[i].properties.name+'</option>';
					}
					$("#cityList").html(innerStr);
					$("#citySel").show();
					if(firstLoad){
						$("#cityList").val(data.titleEN);
						for(var i=0;i<data.points.length;i++){
							addGroup(data.points[i],i);
						}
						firstLoad = false;
					}else{
						$("#groupList").empty();
					}
				}else{
					var txt = $("#provinceList").find("option:selected").text();
					for(var i in cityMap){
						if(i.indexOf(txt) != -1){
							$("#cityList").append('<option value="'+cityMap[i]+'">'+i+'</option>');
							$("#cityList").val(cityMap[i]);
							break;
						}
					}
					$("#citySel").hide();
					if(firstLoad){
						for(var i=0;i<data.points.length;i++){
							addGroup(data.points[i],i);
						}
						firstLoad = false;
					}else{
						$("#groupList").empty();
					}
				}
			}
		});
	}
	
	function addGroup(obj,index){
		var pointStr="";
		for(var i=0;i<obj.points.length;i++){
			pointStr += '<div class="points">'+
										'<div class="row" style="margin-bottom:10px;">'+
										'<label class="col-xs-2 control-label" style="padding-top:7px;">点位名称</label>'+
										'<div class="col-xs-4">'+
											'<input type="text" class="form-control" placeholder="点位名称" value="'+obj.points[i].name+'" />'+
										'</div>'+
										'<label class="col-xs-2 control-label" style="padding-top:7px;">数量</label>'+
										'<div class="col-xs-4">'+
											'<input type="text" class="form-control" placeholder="数量" value="'+obj.points[i].value[2]+'" />'+
										'</div>'+
									'</div>'+
									'<div class="row" style="margin-bottom:10px;">'+
										'<label class="col-xs-2 control-label" style="padding-top:7px;">经度</label>'+
										'<div class="col-xs-4">'+
											'<input type="text" class="form-control" placeholder="经度" value="'+obj.points[i].value[0]+'"/>'+
										'</div>'+
										'<label class="col-xs-2 control-label" style="padding-top:7px;">纬度</label>'+
										'<div class="col-xs-4">'+
											'<input type="text" class="form-control" placeholder="纬度" value="'+obj.points[i].value[1]+'"/>'+
										'</div>'+
									'</div>'+
									'<div class="row" style="margin-bottom:10px;">'+
										'<div class="col-xs-2">'+
											'<button class="btn btn-default" type="button">删除该标记点</button>'+
										'</div>'+
									'</div>'+
								'</div>';
		}
		//添加项
		var innerStr = '<fieldset>' +
											'标题：<input type="text" class="group-title" style="border:1px solid #ccc;" value="'+obj.name+'" /> '+
											'颜色：<input type="text" class="color-inp" /> <input type="text" class="color-code" value="'+obj.color+'" /> '+
											'<hr />'+
												pointStr+
											'<button class="btn btn-default" type="button">增加标记点</button> <button class="btn btn-default" type="button">删除该分组</button>'+
										'</fieldset>';
		$("#groupList").append(innerStr);
		$("#groupList").find(".color-inp").eq(index).spectrum({
			color: obj.color,
			change: function(color) {
				// console.log(color.toHexString()); // #ff0000
				// var fieldsetIndex = $(this).parent().index();
				// colorList[fieldsetIndex] = color.toHexString();
				$(this).next().next().val(color.toHexString());
			}
		});
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