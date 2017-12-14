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
		table input{
			display:block;
			width:100%;
			height:100%;
			border:0 none;
		}
		.active{
			background-color: #f90 !important;
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
		<div style="text-align:center; margin-bottom:10px;">
			<input type="text" class="form-control" style="width:150px;" id="title" />
		</div>
		<div style=" height:40px;">
			<button class="btn btn-default" id="addRow">新增行</button>
			<button class="btn btn-default" id="addCol">新增列</button>
			<button class="btn btn-default" id="del">删除</button>
		</div>
		<table id="table" class="table">
			<thead id="tHead"></thead>
			<tbody id="tBody"></tbody>
		</table>
		<form class="form-horizontal" id="form" style="margin: 20px;">
			<div class="form-group">
				<label for="speed" class="col-xs-2 control-label" style="padding-top:7px;">滚动速度</label>
				<div class="col-xs-4">
					<input type="text" class="form-control" id="speed" />
				</div>
			</div>
		</form>
		<button class="btn btn-default" style="float:right;" id="submit">确认修改</button>
	</div>
</body>
<script type="text/javascript">
	var addFlag = false;
  var p = window.parent;
	var editingFrame = p.editingWidget.frame[p.editingWidgetIndex];
  var data = deepClone(p.editingWidget.frame[p.editingWidgetIndex].data);
  var editor;
	var selType;
	var selIndex;
	var editing;
	var speed = p.editingWidget.frame[p.editingWidgetIndex].speed?p.editingWidget.frame[p.editingWidgetIndex].speed:6;
	
	var old;
	$(document).on("ready",function(){
		$("#title").val(data.title);
		$("#title").on("change",function(){
			data.title = $(this).val();
		});
		
		createTable();
		
		$("#speed").val(speed)
		
		$("#addRow").on("click",function(){
			var innerStr = "<tr>";
			var obj = {};
			for(i in data.data[0]){
				innerStr +="<td></td>";
				obj[i] = "";
			}
			innerStr +="</tr>";
			$("#tBody").append(innerStr);
			data.data.push(obj);
		});
		
		$("#addCol").on("click",function(){
			var innerStr = "";
			var ths = $("#tHead").find("th");
			var trs = $("tBody").find("tr");
			var w = (100/(ths.length+1))+"%";
			ths.css({
				width:w
			});
			$("#tHead").find("tr").eq(0).append('<th style="width:'+w+'"></th>');
			for(var i=0;i<data.data.length;i++){
				trs.eq(i).append("<td></td>");
			}
		});
		
		$("#tHead").on("click","th",function(e){
			var th = $(e.currentTarget);
			if(th.hasClass("active")){
				return;
			}
			selType = "col";
			selIndex = $(e.currentTarget).index();
			$("#table").find(".active").removeClass("active");
			th.addClass("active");
			var trs = $("#tBody").find("tr");
			for(var i=0;i<data.data.length;i++){
				var tds = trs.eq(i).find("td");
				tds.eq(selIndex).addClass("active");
			}
		});
		
		$("#tHead").on("dblclick","th",function(e){
			old = $(e.currentTarget).html();
			innerInp($(e.currentTarget));
		});
		
		$("#tHead").on("blur","input",function(e){
			innerDom($(e.currentTarget));
		});
		
		$("#tBody").on("click","td",function(e){
			var td = $(e.currentTarget);
			if(td.hasClass("active") && selType === "row"){
				return;
			}
			selType = "row";
			selIndex = $(e.currentTarget).parent().index();
			$("#table").find(".active").removeClass("active");
			$(e.currentTarget).parent().find("td").addClass("active");
		});
		
		$("#tBody").on("dblclick","td",function(e){
			innerInp($(e.currentTarget));
		});
		
		$("#tBody").on("blur","input",function(e){
			innerDom($(e.currentTarget));
		});
		
		
		$("#del").on("click",function(){
			if(selType == "row"){
				data.data.splice(selIndex,1);
				$("tBody").find("tr").eq(selIndex).remove();
			}else{
				data.ths.splice(selIndex,1);
				var name = $("tHead").find("th").eq(selIndex).html();
				var trs = $("#tBody").find("tr");
				for(var i=0;i<data.data.length;i++){
					delete data.data[i][name];
					trs.eq(i).find("td").eq(selIndex).remove();
				}
				$("tHead").find("th").eq(selIndex).remove();
			}
		});
		
		$("html").on("click",function(e){
			if($(e.target).prop("tagName")!="TD" && $(e.target).prop("tagName")!="TH" && $(e.target).prop("tagName")!="INPUT"){
				$("#table").find(".active").removeClass("active");
			}
		});
		
		$("#submit").on("click",function(){
			p.editingWidget.frame[p.editingWidgetIndex].data = data;
			p.editingWidget.frame[p.editingWidgetIndex].speed = parseInt($("#speed").val());
			p.editingWidget.frame[p.editingWidgetIndex].useUserData = true;
			p.$("#"+editingFrame.frameId+"_tools").find("li").eq(0).find("a").addClass("menu-active");
			var frame = p.$("#"+editingFrame.frameId)[0].contentWindow;
			frame.res = data;
			frame.useUserData = true;
			frame.speed = parseInt($("#speed").val());
			frame.createTable();
			p.editingWidget = null;
			p.editingWidgetIndex = null;
			p.layer.close(p.editingLayer);
		});
	});
	
	function createTable(){
  		var thStr = "<tr>";
			var tdStr = "";
			for(var i=0;i<data.ths.length;i++){
				thStr += '<th style="width:'+(100/data.ths.length)+'%">'+data.ths[i]+'</th>';
			}
			thStr += "</tr>";
			for(var i=0;i<data.data.length;i++){
				tdStr += "<tr>";
				for(var j=0;j<data.ths.length;j++){
					var str = data.data[i][data.ths[j]]?data.data[i][data.ths[j]]:"";
					tdStr += '<td>'+str+'</td>';
				}
				tdStr += "</tr>";
			}
			$("#tHead").html(thStr);
			$("#tBody").html(tdStr);
		}
	
	function innerInp(obj){
		obj.html('<input type="text" value="'+obj.html()+'" />');
		obj.children().eq(0).focus();
		editing = obj;
	}
	
	function innerDom(obj){
		if(obj.parent().prop("tagName")=="TH"){
			var index = obj.parent().index();
			var trs = $("tBody").find("tr");
			data.ths[index] = obj.val();
			for(var i=0;i<data.data.length;i++){
				data.data[i][obj.val()] = trs.eq(i).find("td").eq(index).html();
				if(old){
					delete data.data[i][old];
				}
			}
		}else{
			var rowIndex = obj.parent().parent().index();
			var index = obj.parent().index();
			var ths = $("tHead").find("th");
			data.data[rowIndex][data.ths[index]] = obj.val();
		}
		obj.parent().html(obj.val());
		obj.remove();
		editing=null;
		console.log(data.data);
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