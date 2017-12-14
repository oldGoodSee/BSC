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
		<div class="form-group">
			<textarea id="elm" name="elm" rows="12" cols="80" style="width: 100%"></textarea>
		</div>
		<div class="form-group">
			<label for="missionName" class="col-xs-2 control-label" style="padding-top:7px;">任务名称</label>
			<div class="col-xs-10">
				<input type="text" class="form-control" id="missionName" />
			</div>
		</div>
		<div class="form-group">
			<label for="beginTime" class="col-xs-2 control-label" style="padding-top:7px;">开始时间</label>
			<div class="col-xs-4">
				<input type="text" class="form-control inp-date" id="beginTime" />
			</div>
			<label for="endTime" class="col-xs-2 control-label" style="padding-top:7px;">结束时间</label>
			<div class="col-xs-4">
				<input type="text" class="form-control inp-date" id="endTime" />
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
  var editor;
	$(document).on("ready",function() {
		$.extend(XHEDITOR.settings);
		editor = $('#elm').xheditor({skin:'o2007blue',background:'#006699',tools:'Cut,Copy,Paste,Pastetext,|,Fontface,FontSize,Bold,Italic,Underline,|,FontColor,BackColor,|,Align,List,Outdent,Indent,Removeformat'});
		editor.setSource(data.content);
		
		$(".inp-date").datetimepicker({
			pickerPosition:"top-left",
			format: 'yyyy-mm-dd',
			language:"zh",
			minView: "month",
			autoclose:true
		});
		
		var beginTime = formatDate(data.beginTime);
		var endTime = formatDate(data.endTime);
		$("#missionName").val(data.appName);
		$("#beginTime").val(beginTime);
		$("#endTime").val(endTime);
		
		$("#submit").on("click",function(){
			data.content = editor.getSource();
			data.appName = $("#missionName").val();
			if($("#beginTime").val()>=$("#endTime").val()){
				p.layer.alert("开始时间必须小于结束时间");
				return;
			}
			data.beginTime = $("#beginTime").val();
			data.endTime = $("#endTime").val();
			var saveObj = {
				useUserData:true,
				data:data
			}
			p.widgetSave(saveObj,frameId,"data");
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
	
	function formatDate(str){
		var arr = str.split("-");
		if(arr[1].length==1){
			arr[1] = "0"+arr[1];
		}
		if(arr[2].length==1){
			arr[2] = "0"+arr[1];
		}
		return arr.join("-");
	}
</script>
</html>