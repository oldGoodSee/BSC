<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta charset="UTF-8">
	<title></title>
	<style>
		.H1{
			display:inline-block;
			height:45px;
			line-height:45px;
			background:url(/PAC-M/widgetFrame/init/resources/img/title-r.png) right top no-repeat;
			padding-right: 40px;
			cursor:normal;
			color: #fff;
		}
		.H1 span{
			display:inline-block;
			height: 45px;
			line-height:45px;
			min-width: 80px;
			padding-right: 0;
			padding-left: 40px;
			background:url(/PAC-M/widgetFrame/init/resources/img/title-l.png) left top no-repeat;
		}
	</style>
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

<body style="background-color:#006699;">
	<div style="text-align:center; height:45px;">
			<b class="H1" style="margin-top:0;">
				<span id="H1">标题</span>
			</b>
		</div>
	<form class="form-horizontal" id="form" style="margin: 20px;">
		<table id="table" class="table">
			<tr>
				<th>表头1</th>
				<th>表头2</th>
				<th>表头3</th>
				<th>表头4</th>
			</tr>
			<tr>
				<td>内容11</td>
				<td>内容12</td>
				<td>内容13</td>
				<td>内容14</td>
			</tr>
			<tr>
				<td>内容21</td>
				<td>内容22</td>
				<td>内容23</td>
				<td>内容24</td>
			</tr>
			<tr>
				<td>内容31</td>
				<td>内容32</td>
				<td>内容33</td>
				<td>内容34</td>
			</tr>
		</table>
   	<div class="form-group" id="btn">
	    <div style="float:right; margin: 0 20px;">
	      <button type="button" class="btn btn-default" id="submit">确认修改</button>
	    </div>
	  </div>
	</form>
</body>
<script type="text/javascript">
  var styleObj = {};
  var p = window.parent;
	var editingFrame = p.editingWidget.frame[p.editingWidgetIndex];
  var styleObj = deepClone(p.editingWidget.frame[p.editingWidgetIndex].styleObj);
	
	$(document).on("ready",function() {
		for(var i in styleObj){
			addCss(styleObj[i],i);
		}
		
		$("#submit").on("click",function(){
			p.editingWidget.frame[p.editingWidgetIndex].styleObj = styleObj;
			var frame = p.$("#"+editingFrame.frameId)[0].contentWindow;
			frame.styleObj = styleObj;
			frame.setCss();
			p.editingWidget = null;
			p.editingWidgetIndex = null;
			p.layer.close(p.editingLayer);
		});
	});
	
	function addCss(obj,key){
		var bgColor = obj.backgroundColor?'<div class="form-group"><label for="bgColor_'+key+'" class="col-xs-2 control-label" style="padding-top:7px;">背景颜色</label>' +
									'<div class="col-xs-4">' +
							      '<input type="text" id="bgColor_'+key+'" />' +
							    '</div></div>':"";
		var weightFlag = obj.fontWeight == "bold"?'checked':'';
		var lineHeight = obj.lineHeight?'<label for="lineHeight'+key+'" class="col-xs-2 control-label" style="padding-top:7px;">行高(px)</label>' +
									'<div class="col-xs-4">' +
							      '<input type="text" class="form-control" id="lineHeight_'+key+'" />' +
							    '</div>':"";
		var str = '<fieldset id="'+key+'">' + 
								'<legend>'+obj.title+'</legend>' +
								'<div class="form-group">' +
									'<label for="fontSize_'+key+'" class="col-xs-2 control-label" style="padding-top:7px;">文本大小</label>' +
									'<div class="col-xs-4">' +
										'<select type="text" id="fontSize_'+key+'" class="form-control">' +
							      	'<option value="14px">14像素</option>' +
							      	'<option value="18px">18像素</option>' +
							      	'<option value="22px">22像素</option>' +
							      	'<option value="26px">26像素</option>' +
							      	'<option value="30px">30像素</option>' +
							      	'<option value="34px">34像素</option>' +
							      '</select>' +
									'</div>' +
									'<label for="fontColor_'+key+'" class="col-xs-2 control-label" style="padding-top:7px;">文本颜色</label>' +
									'<div class="col-xs-4">' +
							      '<input type="text" id="fontColor_'+key+'" />' +
							    '</div>' +
								'</div>' +
								'<div class="form-group">' +
									'<div class="col-xs-6"><input type="checkbox" id="fontWeight_'+key+'" '+weightFlag+' /> 粗体</div>' +
									lineHeight +
								'</div>' +
								bgColor +
							'</fieldset>';
		$("#btn").before(str);
		
		if(key == "H1Style"){
			$("#H1").css(obj);
		}else if(key == "thStyle"){
			$("#table").find("th").css(obj);
		}else{
			$("#table").find("td").css(obj);
		}
		
		$("#fontSize_"+key).val(obj.fontSize);
		
		$("#lineHeight_"+key).val(parseInt(obj.lineHeight));
		
	  $("#fontColor_"+key).spectrum({
	    color: obj.color,
	    change: function(color) {
	    	var key = $(this).attr("id").split("_")[1];
		    styleObj[key].color = color.toHexString(); // #ff0000
				var obj = domSel(key);
				obj.css({
					"color":color.toHexString()
				});
			}
		});
		
		$("#bgColor_"+key).spectrum({
	    color: obj.backgroundColor,
	    change: function(color) {
	    	var key = $(this).attr("id").split("_")[1];
		    styleObj[key].backgroundColor = color.toHexString(); // #ff0000
		    var obj = domSel(key);
				obj.css({
					"backgroundColor":color.toHexString()
				});
			}
		});
		
		$('#fontSize_'+key).on("change",function(){
			var key = $(this).attr("id").split("_")[1];
			styleObj[key].fontSize = $(this).val();
			var obj = domSel(key);
			obj.css({
				"fontSize":$(this).val()
			});
		});
		
		$('#fontWeight_'+key).on("change",function(){
			var key = $(this).attr("id").split("_")[1];
			styleObj[key].fontWeight = $(this).prop("checked")?"bold":"normal";
			var obj = domSel(key);
			var weight = $(this).prop("checked")?"bold":"normal";
			obj.css({
				"fontWeight":weight
			});
		});
		
		$('#lineHeight_'+key).on("change",function(){
			var key = $(this).attr("id").split("_")[1];
			styleObj[key].lineHeight = $(this).val()+"px";
			var obj = domSel(key);
			obj.css({
				"lineHeight":$(this).val()+"px"
			});
		});	
	}
	
	function domSel(key){
		var obj;
		switch(key){
			case "H1Style":
				obj = $("#H1");
				break;
			case "thStyle":
				obj = $("#table").find("th");
				break;
			case "tdStyle":
				obj = $("#table").find("td");
				break;
			default:
				break;
		}
		return obj;
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