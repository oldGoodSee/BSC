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
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/spectrum/spectrum.js"></script>
	<!--[if lt IE 9]>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/html5shiv.min.js"></script>
	<script src="${pageContext.request.contextPath}/modelFrame-tab/js/respond.min.js"></script>
	<![endif]-->
</head>

<body>
	<table id="table" style="width:100%; text-align:center; background-color: #069;">
		<tr>
			<td>
				<img src="${pageContext.request.contextPath}/widgetFrame/init/resources/icons/default.png" /><br />
				<span id="keyStyle">项名称</span><br /><span id="valueStyle">1000</span>
			<td>
		</tr>
	</table>
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
  var styleObj = frame.styleObj;
  
	$(document).on("ready",function() {
		for(var i in styleObj){
			addCss(styleObj[i],i);
		}
		
		$("#submit").on("click",function(){
			p.widgetSave(styleObj,editingFrame.id,"css");
		});
	});
	
	function addCss(obj,key){
		var weightFlag = obj.fontWeight == "bold"?true:false;
		var str = '<fieldset id="'+key+'">' + 
								'<legend>'+obj.title+'</legend>' +
								'<div class="form-group">' +
									'<label for="fontSize_'+key+'" class="col-xs-2 control-label" style="padding-top:7px;">文本大小</label>' +
									'<div class="col-xs-4">' +
										'<select type="text" id="fontSize_'+key+'" class="form-control">' +
							      	'<option value="12px">12像素</option>' +
							      	'<option value="14px">14像素</option>' +
							      	'<option value="16px">16像素</option>' +
							      	'<option value="18px">18像素</option>' +
							      	'<option value="20px">20像素</option>' +
							      	'<option value="22px">22像素</option>' +
							      	'<option value="24px">24像素</option>' +
							      	'<option value="26px">26像素</option>' +
							      	'<option value="28px">28像素</option>' +
							      	'<option value="30px">30像素</option>' +
							      	'<option value="32px">32像素</option>' +
							      	'<option value="34px">34像素</option>' +
							      '</select>' +
									'</div>' +
									'<label for="fontColor_'+key+'" class="col-xs-2 control-label" style="padding-top:7px;">文本颜色</label>' +
									'<div class="col-xs-4">' +
							      '<input type="text" id="fontColor_'+key+'" />' +
							    '</div>' +
								'</div>' +
								'<div class="form-group">' +
									'<label for="fontFamily_'+key+'" class="col-xs-2 control-label" style="padding-top:7px;">字体</label>' +
									'<div class="col-xs-4">' +
										'<select type="text" id="fontFamily_'+key+'" class="form-control">' +
							      	'<option value="微软雅黑">微软雅黑</option>' +
							      	'<option value="黑体">黑体</option>' +
							      	'<option value="宋体">宋体</option>' +
							      	'<option value="仿宋体">仿宋体</option>' +
							      	'<option value="楷体">楷体</option>' +
							      '</select>' +
									'</div>' +
									'<div class="col-xs-6"><input type="checkbox" id="fontWeight_'+key+'" checked='+weightFlag+' /> 粗体</div>' +
								'</div>' +
							'</fieldset>';
		$("#btn").before(str);
		
		$("#"+key).css(obj);
		
		$("#fontSize_"+key).val(obj.fontSize);
		
	  $("#fontColor_"+key).spectrum({
	    color: obj.color,
	    change: function(color) {
	    	var key = $(this).attr("id").split("_")[1];
		    styleObj[key].color = color.toHexString(); // #ff0000
				$("#"+key).css({
					"color":color.toHexString()
				});
			}
		});
		
		$('#fontSize_'+key).on("change",function(){
			var key = $(this).attr("id").split("_")[1];
			styleObj[key].fontSize = $(this).val();
			$("#"+key).css({
				"fontSize":$(this).val()
			});
		});
		
		$('#fontWeight_'+key).on("change",function(){
			var key = $(this).attr("id").split("_")[1];
			styleObj[key].fontWeight = $(this).prop("checked")?"bold":"normal";
			var weight = $(this).prop("checked")?"bold":"normal";
			$("#"+key).css({
				"fontWeight":weight
			});
		});
		
		$('#fontFamily_'+key).on("change",function(){
			var key = $(this).attr("id").split("_")[1];
			styleObj[key].fontFamily = $(this).val();
			$("#"+key).css({
				"fontFamily":$(this).val()
			});
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