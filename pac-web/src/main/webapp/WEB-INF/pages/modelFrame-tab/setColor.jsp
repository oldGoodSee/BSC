<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
	<meta charset="UTF-8">
	<title></title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/css/bootstrap.min.css" />
	<style>
		.color-row {
			margin: 10px;
			padding: 10px;
			cursor: pointer;
		}
		.color-row span{
			display:inline-block;
			width:30px;
			height: 30px;
		}
		.active{
			background-color:moccasin;
			border: 1px solid #f90;
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
</body>
<script type="text/javascript">
	
	$(document).on("ready",function() {
		var colorList = [
	  	["#ff9900","#006699","#990000","#e26c6c","#449d44","#8b449d","#67bbaf","#98bb67","#e2d86c","#eca9df","#c0d5ef","#666666"],
	  	["#96b97d","#ff9933","#cccccc","#75b5f4","#f4c475","#8af475","#666666","#ff9900","#f275f4","#60a369","#ffc9ec","#c3c600"]
	  ];
	  var p = window.parent;
	  var index = 0;
	  if(p.editingWidget.colorIndex){
	  	if(p.editingWidget.colorIndex[p.editingWidgetIndex]){
	  		index = p.editingWidget.colorIndex[p.editingWidgetIndex];
	  	}
	  }
	  
	  var innerStr = "";
		for(var i=0;i<colorList.length;i++){
			var cls = i==index?"color-row active":"color-row";
			innerStr += '<div class="'+cls+'">';
			for(var j=0;j<colorList[i].length;j++){
				innerStr += '<span style="background:'+colorList[i][j]+'"></span>';
			}
			innerStr += "</div>";
		}
		$("#main").append(innerStr);
		
		$("#main").find(".color-row").on("click",function(){
			var colorRow = $(this);
			if(colorRow.hasClass("active")){
				return;
			}
			var index = colorRow.index();
			window.parent.setChartColor(index);
			$("#main").find(".active").removeClass("active");
			colorRow.addClass("active");
		});
	});
</script>
</html>