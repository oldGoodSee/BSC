<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String frameId = request.getParameter("frameId");
%>
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
	<!--[if lt IE 9]>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/html5shiv.min.js"></script>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/respond.min.js"></script>
	<![endif]-->
</head>

<body>
	<div id="main"></div>
	<form class="form-horizontal" id="form" style="margin: 20px;">
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
  var frameId = "<%=frameId%>";
  var editingFrame = p.getObjById(frameId,"frame");
  p.editingFrameObj = editingFrame;
	var frame = p.$("#"+frameId)[0].contentWindow;
	
  var data = deepClone(frame.res);
  var tm = deepClone(frame.tm);
	
	var cityList = [];
	var firstLoad = true;
	
	var colorList = [];
	var checkedList = [];
	
	for(var i=0;i<data.points.length;i++){
		colorList.push(data.points[i].color);
	}
	
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
		
		$("#provinceList").val(data.titleEN);
		$("#tm").prop("checked",tm);
		
		provinceSel();
		
		$("#provinceList").on("change",function(){
			cityList = [];
			colorList = [];
			checkedList = [];
			provinceSel();
			$("#groupList").empty();
		});
		
		$("#addGroup").on("click",function(){
			var citysStr="";
			for(var i=0;i<cityList.length;i++){
				var disabled = $.inArray(cityList[i].name,checkedList)!=-1?"disabled":"";
				citysStr += '<div style="display:inline-block; margin:0 5px; line-height:26px; padding: 8px 0;">'+
											'<input type="checkbox" '+disabled+' /><span> '+cityList[i].name+': </span><input type="text" style="width:80px; border:1px solid #ccc;" />'+
										'</div>';
			}
			//添加项
			var innerStr = '<fieldset>' +
												'标题：<input type="text" class="group-title" style="border:1px solid #ccc;" /> '+
												'颜色：<input type="text" class="color-inp" /> <input type="text" class="color-value" value="#ffffff" style="border:1px solid #ccc;" />'+
												'<div class="citys">'+citysStr +'</div>'+
												'<button class="btn btn-default" type="button">删除该分组</button>'+
											'</fieldset>';
			$("#groupList").append(innerStr);
			colorList.push("#ffffff");
			$("#groupList").find(".color-inp").last().spectrum({
				color: '#ffffff',
				change: function(color) {
					var fieldsetIndex = $(this).parent().index();
					colorList[fieldsetIndex] = color.toHexString();
					$(this).parent().find(".color-value").val(color.toHexString());
				}
			});
		});
		
		$("#form").on("change",".color-value",function(e){
			var reg1 = new RegExp(/^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$/);
			var colorValue = $(this).val().match(reg1)?$(this).val():"#ffffff";
			$(this).val(colorValue);
			$(this).parent().find(".color-inp").last().spectrum("set",colorValue);
			var index = $(this).parent().index();
			colorList[index] = colorValue;
		});
		
		$("#form").on("change","input[type='checkbox']",function(e){
			changeCheck($(this));
		});
		
		$("#form").on("click","fieldset button",function(e){
			var btn = $(e.currentTarget);
			var checkInps = btn.parent().find('input[type="checkbox"]');
			var fieldsetIndex = btn.parent().index();
			var fieldsetList = $("#groupList").find("fieldset");
			for(var i=0;i<checkInps.length;i++){
				if(checkInps.eq(i).prop("checked")){
					var index = $.inArray(checkInps.eq(i).next().html().replace(" ","").replace(": ",""),checkedList);
					if(index != -1){
						checkedList[index] = "-"+checkedList[index];
					}
				}
			}
			for(var i=checkedList.length-1;i>=0;i--){
				if(checkedList[i].substring(0,1) == "-"){
					for(var j=0;j<fieldsetList.length;j++){
						var spanList = fieldsetList.eq(j).find("span");
						for(var k=0;k<spanList.length;k++){
							if(spanList.eq(k).html() == " "+checkedList[i].replace("-","")+": "){
								spanList.eq(k).prev().prop("disabled",false);
								break;
							}
						}
					}
					checkedList.splice(i,1);
					
				}
			}
			btn.parent().remove();
		});
	  
		$("#submit").on("click",function(){
			var obj = {points:[]};
			obj.titleEN = $("#provinceList").val();
			var fieldsetList = $("#groupList").find("fieldset");
			for(var i=0;i<fieldsetList.length;i++){
				var provinceObj = {points:[]};
				provinceObj.name = fieldsetList.eq(i).find("input").eq(0).val();
				provinceObj.color = colorList[i];
				var citys = fieldsetList.eq(i).find('input[type="checkbox"]');
				for(var j=0;j<citys.length;j++){
					if(citys.eq(j).prop("checked")){
						var v = getPosition(citys.eq(j).next().html());
						var value = parseInt(citys.eq(j).next().next().val())?parseInt(citys.eq(j).next().next().val()):0;
						v.push(value);
						var name = citys.eq(j).next().html()
						name = name.replace(/ /g,"");
						name = name.replace(/:/g,"");
						provinceObj.points.push({
							name:name,
							value:value
						});
					}
				}
				obj.points.push(provinceObj);
			}
			var saveObj = {
				useUserData:true,
				data:obj,
				tm:$("#tm").prop("checked")
			}
			p.widgetSave(saveObj,editingFrame.id,"data");
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
				for(var i=0;i<result.features.length;i++){
					cityList.push({
						name:result.features[i].properties.name,
						position:result.features[i].properties.cp
					});
				}
				if(firstLoad){
					for(var i=0;i<data.points.length;i++){
						addGroup(data.points[i],i);
					}
					var fieldsetList = $("#groupList").find("fieldset");
					for(var i=0;i<data.points.length;i++){
						for(var j=0;j<data.points[i].points.length;j++){
							var checkInp = fieldsetList.eq(i).find("."+data.points[i].points[j].name).find("input").eq(0);
							checkInp.prop("checked",true);
							changeCheck(checkInp);
						}
					}
					firstLoad = false;
				}
			}
		});
	}
	
	function addGroup(obj,index){
		var citysStr="";
		for(var i=0;i<cityList.length;i++){
			citysStr += '<div style="width:30%; float:left; line-height:26px; padding: 8px 0;" class="'+cityList[i].name+'">'+
										'<input type="checkbox" /><span> '+cityList[i].name+': </span><input type="text" style="width:80px; border:1px solid #ccc;" />'+
									'</div>';
		}
		//添加项
		var innerStr = '<fieldset>' +
											'标题：<input type="text" class="group-title" style="border:1px solid #ccc;" value="'+obj.name+'" />'+
											'颜色：<input type="text" class="color-inp" /> <input type="text" class="color-value" value="'+obj.color+'" style="border:1px solid #ccc;" />'+
											'<div class="citys">'+citysStr +'</div>'+
											'<button class="btn btn-default" type="button">删除该分组</button>'+
										'</fieldset>';
		$("#groupList").append(innerStr);
		$("#groupList").find(".color-inp").eq(index).spectrum({
			color: obj.color,
			change: function(color) {
				// console.log(color.toHexString()); // #ff0000
				var fieldsetIndex = $(this).parent().index();
				colorList[fieldsetIndex] = color.toHexString();
			}
		});
		for(var i=0;i<obj.points.length;i++){
			var item = $("#groupList").find("fieldset").eq(index).find("."+obj.points[i].name);
			//item.find("input").eq(0).prop("checked", true);
			item.find("input").eq(1).val(obj.points[i].value[2]);
		}
	}
	
	function getPosition(name){
		for(var i = 0;i<cityList.length;i++){
			name = name.replace(/ /g,"");
			name = name.replace(/:/g,"");
			if(name == cityList[i].name){
				return cityList[i].position;
			}
		}
	}
	
	function changeCheck(obj){
		var index = obj.parent().index();
		var fieldsetIndex = obj.parent().parent().parent().index();
		var fieldsetList = $("#groupList").find("fieldset");
		if(obj.prop("checked")){
			for(var i=0;i<fieldsetList.length;i++){
				if(i != fieldsetIndex){
					fieldsetList.eq(i).find(".citys").children().eq(index).children().eq(0).prop("disabled",true);
				}
			}
			checkedList.push(obj.next().html().replace(" ","").replace(": ",""));
		}else{
			for(var i=0;i<fieldsetList.length;i++){
				if(i != fieldsetIndex){
					fieldsetList.eq(i).find(".citys").children().eq(index).children().eq(0).prop("disabled",false);
				}
			}
			var checkedIndex = $.inArray(obj.next().html(),checkedList);
			checkedList.splice(checkedIndex,1);
		}
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