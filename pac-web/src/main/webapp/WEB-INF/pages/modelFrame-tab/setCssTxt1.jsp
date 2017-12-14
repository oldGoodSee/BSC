<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta charset="UTF-8">
	<title></title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/css/bootstrap.min.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/widgetFrame/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/init/resources/xhEditor-master/xheditor.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/init/resources/xhEditor-master/xheditor_lang/zh-cn.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
	<!--[if lt IE 9]>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/html5shiv.min.js"></script>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/respond.min.js"></script>
	<![endif]-->
</head>

<body>
	<div id="main"></div>
	<form class="form-horizontal" id="form" style="margin: 20px;">
		<div class="form-group" id="add">
			<div class="col-xs-12">
				<button type="button" class="btn btn-default">新增</button>
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
  var editorList=[];
	$(document).on("ready",function() {
		$.extend(XHEDITOR.settings);
		for(var i=0;i<data.length;i++){
			var innerStr = '<div class="form-group">'+
												'<div class="col-xs-10">'+
													'<textarea id="elm'+i+'" name="elm'+i+'" rows="8" cols="80" style="width: 100%"></textarea>'+
												'</div>'+
												'<div class="col-xs-2">'+
													'<button class="btn btn-default">删除</button>'+
												'</div>'+
											'</div>';
			$("#add").before(innerStr);
			var editor = $('#elm'+i).xheditor({skin:'o2007blue',background:'#006699',tools:'Cut,Copy,Paste,Pastetext,|,Fontface,FontSize,Bold,Italic,Underline,|,FontColor,BackColor,|,Align,List,Outdent,Indent,Removeformat'});
			editor.setSource(data[i]);
			editorList.push(editor)
		}
		
		$("#form").on("click","button",function(e){
			var btn = $(e.currentTarget);
			switch(btn.html()){
				case "删除":
					var index = btn.parent().parent().index();
					editorList.splice(index,1);
					btn.parent().parent().remove();
					break;
				case "新增":
					var index = editorList.length;
					var innerStr = '<div class="form-group">'+
												'<div class="col-xs-10">'+
													'<textarea id="elm'+index+'" name="elm'+index+'" rows="8" cols="80" style="width: 100%"></textarea>'+
												'</div>'+
												'<div class="col-xs-2">'+
													'<button class="btn btn-default">删除</button>'+
												'</div>'+
											'</div>';
					$("#add").before(innerStr);
					var editor = $('#elm'+index).xheditor({skin:'o2007blue',background:'#006699',tools:'Cut,Copy,Paste,Pastetext,|,Fontface,FontSize,Bold,Italic,Underline,|,FontColor,BackColor,|,Align,List,Outdent,Indent,Removeformat'});
					editorList.push(editor)
					break;
				default:
					break;
			}
		});
		
		$("#submit").on("click",function(){
			var newData = [];
			for(var i = 0;i<editorList.length;i++){
				newData.push(editorList[i].getSource());
			}
			
			editingFrame.data = newData;
			editingFrame.useUserData = true;
			p.$("#"+editingFrame.frameId+"_tools").find("li").eq(0).find("a").addClass("menu-active");
			var frame = p.$("#"+editingFrame.frameId)[0].contentWindow;
			frame.res = newData;
			if(frame.showIndex > newData.length-1){
				frame.showIndex = newData.length-1;
			}
			frame.createText();
			p.editingWidget = null;
			p.editingWidgetIndex = null;
			p.layer.close(p.editingLayer);
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