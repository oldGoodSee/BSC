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
	</style>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/bootstrap.min.js"></script>
	<!--[if lt IE 9]>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/html5shiv.min.js"></script>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/respond.min.js"></script>
	<![endif]-->
</head>

<body>
	<div id="main"></div>
	<form class="form-horizontal" id="form" style="margin: 20px;">
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
  var pId = p.$("#"+frameId).parent().parent().attr("id");
  var arr = p.$("#"+frameId).parent().parent().hasClass("container")?p.activeTab.container:p.activeTab.layer;
  var containerObj = p.getObjInArr(pId,arr,"id");
  var editingFrame = p.getObjInArr(frameId,containerObj.frame,"id");
  p.editingFrameObj = editingFrame;
	var frame = p.$("#"+frameId)[0].contentWindow;
  var dataObj;
	
	$(document).on("ready",function() {
		//第一此创建
		dataObj	= deepClone(frame.res);
		
//		lastEditTime = 0;
		createEdit();
		
		function createEdit(){
			var innerStr = '<fieldset>';
			for(var j in dataObj){
				var inp = '<input type="number" class="form-control inp-value" value="'+dataObj[j]+'" />';
				innerStr += '<div class="form-group">' +
											'<div class="col-xs-5">' +
												'<input id="'+j+'" class="form-control inp-key" value="'+j+'" />'+  //这里的id用于寻找旧的值及判断是否是新增属性
											'</div>' +
											'<div class="col-xs-5">' +
												inp +
											'</div>' +
											'<div class="col-xs-1">' +
												'<button type="button" class="btn btn-default"><span class="glyphicon glyphicon-minus"></span></button>' +
											'</div>' +
										'</div>';
			}
			innerStr += '<div class="form-group">' +
										'<div class="col-xs-12">' +
											'<button type="button" class="btn btn-default" id="addItem"><span class="glyphicon glyphicon-plus"></span></button>' +
										'</div>' +
									'</div>';
			innerStr += '</fieldset>';
			$("#btn").before(innerStr);
		}
		
		$("#addItem").on("click",function(){
			//添加项
			innerStr = '<div class="form-group">' +
										'<div class="col-xs-5">' +
											'<input id="" class="form-control inp-key" value="" />'+  //这里的id用于寻找旧的值及判断是否是新增属性
										'</div>' +
										'<div class="col-xs-5">' +
											'<input type="number" class="form-control inp-value" value="0" />' +
										'</div>' +
										'<div class="col-xs-1">' +
											'<button type="button" class="btn btn-default"><span class="glyphicon glyphicon-minus"></span></button>' +
										'</div>' +
									'</div>';
			$(this).parent().parent().before(innerStr);
		});
		
		//动态添加修改title事件
		$("#form").on("change",".inp-title",function(e){
			var inp = $(e.currentTarget);
			dataObj.title = inp.val();
		});
		
		//动态添加删除事件
		$("#form").on("click","button",function(e){
			var span = $(e.currentTarget).find("span").eq(0);
			if(span.hasClass("glyphicon-minus")){
				var key = $(this).parent().parent().find(".inp-key").eq(0).val();
				if(key == ""){
					$(this).parent().parent().remove();
				}else{
					delete dataObj[key];
					$(this).parent().parent().remove();
				}
			}
		});
		
		//动态添加新增属性事件
		$("#form").on("change",".inp-key",function(e){
			var inp = $(e.currentTarget);
			var v = parseInt(inp.parent().next().children().eq(0).val());
			if(inp.attr("id") != ""){	//修改
				var key = inp.attr("id");
				delete dataObj[key];
				dataObj[inp.val()] = v;
				inp.attr("id",inp.val());
			}else{	//新增
				if(dataObj[inp.val()] != undefined){
					p.myAlert("新增项和已有的数据重复！");
					return;
				}
				dataObj[inp.val()] = v;
				inp.attr("id",inp.val());
			}
		});
		
		//动态添加为属性赋值事件
		$("#form").on("change",".inp-value",function(e){
			var inp = $(e.currentTarget);
			var keyInp = $(this).parent().prev().children().eq(0);
			if(keyInp.attr("id") == ""){	//未设置key值，无操作
				return;
			}
			if(inp.val()<0){
				inp.val(0-inp.val());
			}
			var v = parseInt(inp.val())?parseInt(inp.val()):0;
			dataObj[keyInp.val()] = v;
		});
	  
		//提交
		$("#submit").on("click",function(){
			var saveObj = {
				data:dataObj,
//				lastEditTime:lastEditTime,
				useUserData:true
			}
			p.widgetSave(saveObj,editingFrame.id,"data");
		});
	});
	
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
	
	//保存成功后调用的方法，关闭自身及刷新对应iframe，主要是因为每个控件需要
	function sumitCallback(obj){
		frame.showY = obj.showY;
		frame.fixLen = obj.fixLen;
		frame.res = obj.data;
		frame.lastEditTimeData = obj.lastEditTime;
		frame.createChart()
		p.layer.close(p.editingLayer);
	}
</script>
</html>