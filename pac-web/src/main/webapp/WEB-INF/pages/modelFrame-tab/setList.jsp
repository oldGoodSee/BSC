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
		<div style=" height:40px; margin: 10px 0;">
			<button class="btn btn-default" id="addRow">新增行</button>
			<button class="btn btn-default" id="del">删除</button>
		</div>
		<table id="table" style="width:80%; margin-left:5%;">
		</table>
		<form class="form-horizontal" id="form" style="margin: 20px;">
			<div class="form-group" id="add">
				<div class="col-xs-5">每行显示列数</div>
				<div class="col-xs-7">
					<input type="number" class="form-control" id="colMax" />
				</div>
			</div>
		</form>
		<button class="btn btn-default" style="float:right;" id="submit">确认修改</button>
	</div>
</body>
<script type="text/javascript">
	var p = window.parent;
	var frameId = "<%=frameId%>";
  var pId = p.$("#"+frameId).parent().parent().attr("id");
  var arr = p.$("#"+frameId).parent().parent().hasClass("container")?p.activeTab.container:p.activeTab.layer;
  var containerObj = p.getObjInArr(pId,arr,"id");
  var editingFrame = p.getObjInArr(frameId,containerObj.frame,"id");
  p.editingFrameObj = editingFrame;
	var frame = p.$("#"+frameId)[0].contentWindow;
	
  var dataObj = deepClone(frame.res);
	
	var selIndexList = [];
	var editImg;
	
	var oldThs = [];
	
	var old;
	$(document).on("ready",function(){
		$("#colMax").val(frame.colMax);
		
		createTable();
		
		$("#table").on("change","input",function(e){
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
		
		$("#table").on("click","img",function(e){
			editImg = $(e.currentTarget);
			p.iconsLayer = p.layer.open({
				type: 2,
				title: '图片选择',
				shade: 0.3,
				area: ['400px', '300px'],
				content: '../../widgetFrame/init/resources/icons/icons.html#1', //iframe的url
				cancel: function(index, layero){
					p.iconsLayer = null;
				}
			});
		});
		
		$("#table").on("click","a",function(e){
			editImg = $(e.currentTarget).parent().next();
			p.iconsLayer = p.layer.open({
				type: 2,
				title: '图片选择',
				shade: 0.3,
				area: ['400px', '300px'],
				content: '../../widgetFrame/init/resources/icons/icons.html#1', //iframe的url
				cancel: function(index, layero){
					p.iconsLayer = null;
				}
			});
		});
		
		$("#addRow").on("click",function(){
			var innerStr = '<tr>'+
												'<td style="width:10%;"><input type="checkbox" /></td>' +
												'<td style="width:10%;"><img src="${pageContext.request.contextPath}/widgetFrame/init/resources/icons/add.png" style="width:47px; height: 47px;" /></td>' +
												'<th style="width:40%;"><input class="inp" type="text" value="" /></th>' +
												'<td style="width:40%;"><input class="inp" type="text" value="" /></td>' +
											'</tr>';
			$("#table").append(innerStr);
		});
		
		$("#del").on("click",function(){
			var trs = $("#table").find("tr");
			for(var i=0;i<selIndexList.length;i++){
				trs.eq(selIndexList[i]).remove();
			}
		});
		
		$("#submit").on("click",function(){
			var saveObj = {data:[]};
			var trs = $('#table').find("tr");
			for(var i=0;i<trs.length;i++){
				var inps = trs.eq(i).find(".inp");
				if(inps.eq(0).val() != ""){
					var src = trs.eq(i).find("img").eq(0).attr("src")!="${pageContext.request.contextPath}/widgetFrame/init/resources/icons/add.png"?trs.eq(i).find("img").eq(0).attr("src"):"";
					saveObj.data.push({
						name:inps.eq(0).val(),
						icon:src,
						value:inps.eq(1).val()
					});
				}
			}
			saveObj.colMax = parseInt($("#colMax").val());
			saveObj.useUserData = true;
			saveObj.showType = frame.showType;
			p.widgetSave(saveObj,editingFrame.id,"data");
		});
	});
	
	function createTable(){
		var innerStr = '';
		for(var i=0;i<dataObj.length;i++){
				var icon = dataObj[i].icon?'<img src="'+dataObj[i].icon+'" style="display:inline-block;" />':'<nobr><a href="javascript:void(0)">无图片</a></nobr><img src="" style="display:none;" />';
				innerStr += '<tr>'+
											'<td style="width:10%;"><input type="checkbox" /></td>' +
											'<td style="width:30%;">'+icon+'</td>' +
											'<th style="width:30%;"><input type="text" class="inp" value="'+dataObj[i].name+'" /></th>' +
											'<td style="width:30%;"><input type="text" class="inp" value="'+dataObj[i].value+'" /></td>'+
										'</tr>';
		}
		$("#table").html(innerStr);
	}
	
	function getUrl(url){
		if(url){
			editImg.attr("src","${pageContext.request.contextPath}/widgetFrame/init/resources/icons/"+url);
			editImg.show();
			if(editImg.prev().length>0){
				editImg.prev().hide();
			}
		}else{
			editImg.attr("src",'');
			if(editImg.prev().length>0){
				editImg.prev().show();
			}else{
				editImg.before('<nobr><a href="javascript:void(0)">无图片</a></nobr>');
			}
			editImg.hide();
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