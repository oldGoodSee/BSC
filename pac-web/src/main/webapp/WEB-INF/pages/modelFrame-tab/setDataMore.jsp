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
   		<div style="float:left; margin: 0 20px;">
   			<input type="button" class="btn btn-default"  text="添加维度" value="添加维度" id="addfieldset"/>
	    </div>
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
	
	var dataList = deepClone(frame.res);
	
	//日期正则表达式
  var dateReg = new RegExp(/^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))/);
	$(document).on("ready",function() {
		
	  //这里数据集可能有多个
	  var innerStr = '';
	  for(var i=0;i<dataList.length;i++){
	  	innerStr += '<fieldset id="'+i+'">' + 
										'<div class="form-group">' +
											'<div class="col-xs-6">' +
												'<input type="text" class="form-control inp-title" value="'+dataList[i].title+'" />' +
											'</div>' +
										'</div>' +
										'<hr />';
			for(var j in dataList[i].data){
				var inp;
				if(dateReg.test(dataList[i].data[j])){
					inp = "date";
				}else{
					inp = (""+dataList[i].data[j]).length<=20?'<input type="text" class="form-control inp-value" value="'+dataList[i].data[j]+'" />':'<textarea class="form-control inp-value" style="width:100%;" id="'+i+j+'">'+dataList[i].data[j]+'</textarea>';
				}
				innerStr += '<div class="form-group">' +
											'<div class="col-xs-5">' +
									      '<input id="'+i+"_"+j+'" class="form-control inp-key" value="'+j+'" />'+  //这里的id用于寻找旧的值及判断是否是新增属性
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
											'<button type="button" class="btn btn-default" id="addItem_'+i+'"><span class="glyphicon glyphicon-plus"></span></button>' +
										'</div>' +
									'</div>';
			innerStr += '</fieldset>';
	  }
		$("#btn").before(innerStr);
		
		//动态添加删除事件和添加事件
		$("#form").on("click","button",function(e){
			var span = $(e.currentTarget).find("span").eq(0);
			if(span.hasClass("glyphicon-minus")){
				var key = $(this).parent().parent().find(".inp-key").eq(0).val();
				var index = $(this).parent().parent().index()-1;
				if(key == ""){
					for(var i=0;i<dataList.length;i++){
						$("#form").find("fieldset").eq(i).find(".form-group").eq(index).remove();
					}
				}else{
					for(var i=0;i<dataList.length;i++){
						delete dataList[i].data[key];
						$("#form").find("fieldset").eq(i).find(".form-group").eq(index).remove();
					}
				}
			}else if(span.hasClass("glyphicon-plus")){
				//添加项
				innerStr = '<div class="form-group">' +
											'<div class="col-xs-5">' +
												'<input id="" class="form-control inp-key" value="" />'+  //这里的id用于寻找旧的值及判断是否是新增属性
											'</div>' +
											'<div class="col-xs-5">' +
												'<input type="text" class="form-control inp-value" value="0" />' +
											'</div>' +
											'<div class="col-xs-1">' +
												'<button type="button" class="btn btn-default"><span class="glyphicon glyphicon-minus"></span></button>' +
											'</div>' +
										'</div>';
				for(var i=0;i<dataList.length;i++){
					$("#form").find("fieldset").eq(i).find(".form-group").last().before(innerStr);
				}
			}
		});
		
		//动态添加新增属性事件
		$("#form").on("change",".inp-key",function(e){
			var inp = $(e.currentTarget);
			var itemIndex = $(this).parent().parent().parent().index();
			var index = inp.parent().parent().index()-1;
			if(inp.attr("id") != ""){	//修改
				var key = inp.attr("id").split("_")[1];
				for(var i=0;i<dataList.length;i++){
					var keyInp = $("#form").find("fieldset").eq(i).find(".form-group").eq(index).find("input").eq(0);
					var valueInp = $("#form").find("fieldset").eq(i).find(".form-group").eq(index).find("input").eq(1);
					delete dataList[i].data[key];
					dataList[i].data[inp.val()] = parseInt(valueInp.val());
					keyInp.val(inp.val()).attr("id",i+"_"+inp.val());
				}
			}else{	//新增
				if(dataList[itemIndex].data[inp.val()] != undefined){
					window.parent.myAlert("新增项和已有的数据重复！");
					return;
				}
				for(var i=0;i<dataList.length;i++){
					var keyInp = $("#form").find("fieldset").eq(i).find(".form-group").eq(index).find("input").eq(0);
					var valueInp = $("#form").find("fieldset").eq(i).find(".form-group").eq(index).find("input").eq(1);
					dataList[i].data[inp.val()+""] = parseInt(valueInp.val());
					keyInp.val(inp.val()).attr("id",i+"_"+inp.val());
				}
			}
		});
		
		//动态添加为属性赋值事件
		$("#form").on("change",".inp-value",function(e){
			var inp = $(e.currentTarget);
			var itemIndex = $(this).parent().parent().parent().index();
			var keyInp = $(this).parent().prev().children().eq(0);
			if(keyInp.attr("id") == ""){	//未设置key值，无操作
				return;
			}
			var v = parseInt(inp.val())?parseInt(inp.val()):inp.val();
			dataList[itemIndex].data[keyInp.val()+""] = v;
		});
	  
		$("#submit").on("click",function(){
			var saveObj = {
				data:dataList,
//				lastEditTime:lastEditTime,
				useUserData:true
			}
			p.widgetSave(saveObj,editingFrame.id,"data");
		});
		
		//添加新的维度信息
		$("#addfieldset").click(function(){
			var fieldHtml = "";
			var fieldnum = $("#form").find("fieldset").length+1;
			var addfielddata = dataList[0]?deepClone(dataList[0]):{title:"未命名",data:{}};
			//向当前数据对象里面添加新元素
			var dataObj = new Object();
			addfielddata.title="未命名";
			for(var i in addfielddata.data){
				addfielddata.data[i] = 0;
			}

			dataObj.data = addfielddata.data;
			dataObj.title = addfielddata.title;
			dataList.push(dataObj);
			
			fieldHtml +='<fieldset id="'+fieldnum+'">' + 
										'<div class="form-group">' +
										'<div class="col-xs-6">' +
											'<input type="text" class="form-control inp-title" value="'+addfielddata.title+'" />' +
										'</div>' +
									'</div>' +
									'<hr />';
			for(var j in addfielddata.data){
				var inp;
				if(dateReg.test(addfielddata.data[j])){
					inp = "date";
				}else{
					inp = (""+addfielddata.data[j]).length<=20?'<input type="text" class="form-control inp-value" value="'+addfielddata.data[j]+'" />':'<textarea class="form-control inp-value" style="width:100%;" id="'+fieldnum+j+'">'+addfielddata.data[j]+'</textarea>';
				}
				fieldHtml += '<div class="form-group">' +
											'<div class="col-xs-5">' +
									      '<input id="'+fieldnum+"_"+j+'" class="form-control inp-key" value="'+j+'" />'+  //这里的id用于寻找旧的值及判断是否是新增属性
									    '</div>' +
											'<div class="col-xs-5">' +
									      inp +
									    '</div>' +
											'<div class="col-xs-1">' +
												'<button type="button" class="btn btn-default"><span class="glyphicon glyphicon-minus"></span></button>' +
											'</div>' +
									  '</div>';
			}
			fieldHtml += '<div class="form-group">' +
										'<div class="col-xs-12">' +
											'<button type="button" class="btn btn-default" id="addItem_'+fieldnum+'"><span class="glyphicon glyphicon-plus"></span></button>' +
										'</div>' +
									'</div>';
			fieldHtml += '</fieldset>';
			//将其拼接到最后一个 fieldset后面去
			$("#btn").before(fieldHtml);			
		})
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