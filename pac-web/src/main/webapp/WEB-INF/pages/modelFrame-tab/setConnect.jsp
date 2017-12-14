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
	<link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/js/layui/css/layui.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/layui/layui.js" ></script>
</head>

<body>
	<form class="layui-form" action="" id="form">
		<div class="layui-form-item">
			<div class="layui-input-block">
	      <select name="connect" id="connect"></select>
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
	var activeTab = p.activeTab;
	var list = [];
	
	var connectList = deepClone(editingFrame.connectList);
	var connectIndex = 0;
  
	layui.use(['form','jquery'], function(){
	  var form = layui.form;
	  var $ = layui.jquery;
		for(var i = 0;i<activeTab.container.length;i++){
			for(var j=0;j<activeTab.container[i].frame.length;j++){
				if(activeTab.container[i].frame[j].connectFn){
					list.push(activeTab.container[i].frame[j]);
				}
			}
		}
		for(var i = 0;i<activeTab.layer.length;i++){
			for(var j=0;j<activeTab.layer[i].frame.length;j++){
				if(activeTab.layer[i].frame[j].connectFn){
					list.push(activeTab.layer[i].frame[j]);
				}
			}
		}
		var innerStr = '';
		
		for(var i=0;i<editingFrame.connectList.length;i++){
			$("#connect").append('<option value="'+i+'">'+editingFrame.connectList[i].title+'</option>');
		}
		for(var i = 0;i<list.length;i++){
			var btnList = '';
			for(var j=0;j<list[i].connectFn.length;j++){
				btnList += '<button type="button" class="layui-btn layui-btn-primary">'+list[i].connectFn[j].text+'</button>';
			}
			innerStr += '<fieldset id="'+list[i].id+'"><legend>'+list[i].title+'</legend></fieldset>'+
									'<div class="layui-form-item" id="btnList">'+
										btnList+
									'</div>';
		}
		$("#form").append(innerStr);
		form.render();
		
		$("#btnList").find(".layui-btn").on("click",function(){
			if(!$(this).hasClass("layui-btn-primary")){
				editingFrame.connectList[connectIndex].fn = false;
				$(this).addClass("layui-btn-primary");
			}else{
				var btns = $(this).parent().find("button");
				for(var i=0;i<btns.length;i++){
					if(!btns.eq(i).hasClass("layui-btn-primary")){
						btns.eq(i).removeClass("layui-btn-primary");
						break;
					}
				}
				$(this).addClass("layui-btn-primary");
				var targetId = parseInt($(this).parent().prev().attr("id"));
				var targetObj = p.getObjById(targetId,"frame");
				var connectIndex = $(this).index();
				editingFrame.connectList[connectIndex].fn = targetObj.connectFn[connectIndex].fn;
			}
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
</script>
</html>