//@ sourceURL=msgprompt.js
function init(){
	//这里也需要清除，因为用户可能左侧切换应用
	$("#resultList").empty();
	config = {};
	//初始的控件需要添加提示
	if(!window.frames["mainFrame"].config.url){
		if(window.frames["mainFrame"].$("#blankTip").length==0){
			window.frames["mainFrame"].$("body").append("<p id='blankTip' class='blank-tip'>请选择接口！</p>");
		}
	}else{
		config.url = window.frames["mainFrame"].config.url;
		config.labels = window.frames["mainFrame"].config.labels;
		config.colNames = window.frames["mainFrame"].config.colNames;
		$("#interFaceList").selectpicker('val',window.frames["mainFrame"].config.url);
		$("#interFaceList").trigger("changed.bs.select");
	}
	//$("#resultList").hide();
}

//接口切换调用的方法
function changeInterface(obj){
	$("#resultList").empty();
	var fieldList = $.parseJSON("["+obj.field_meaning.replace(/\"/g,'"')+"]");
	var labels = [],
		  colNames = [];
	var innerStr = "";
	if(!config.url){		//完全新建
		for(var i=0;i<fieldList.length;i++){
			labels.push(fieldList[i].mean);
			colNames.push(fieldList[i].field);
			innerStr += '<div class="form-group">'+
									'<label>'+fieldList[i].mean+'</label>&nbsp;&nbsp;'+
									'<button class="btn btn-success" type="button" colName="'+fieldList[i].field+'"><span class="glyphicon glyphicon-link"></span></button>&nbsp;'+
								    '<button class="btn btn-default" type="button" colName="'+fieldList[i].field+'"><span class="glyphicon glyphicon-remove"></span></button>&nbsp;'+
								'</div>';
		}
		config = {
			url:obj.url,
			labels:labels,
			colNames:colNames
		};
		window.frames["mainFrame"].config = {
			url:obj.url,
			labels:labels,
			colNames:colNames
		};
		//根据选择的值生成新的chart
		window.frames["mainFrame"].$("#blankTip").html("加载中...");
		window.frames["mainFrame"].ajaxChart(window.frames["mainFrame"].$(".ajax-data"));
	}else{	//已有配置信息
		for(var i=0;i<fieldList.length;i++){
			var btnStatus = $.inArray(fieldList[i].field,config.colNames) != -1?"btn-success":"btn-default";
			var btnIcon = $.inArray(fieldList[i].field,config.colNames) != -1?"glyphicon-ok":"glyphicon-link";
			innerStr += '<div class="form-group">'+
									'<label>'+fieldList[i].mean+'</label>&nbsp;&nbsp;'+
									'<button class="btn '+btnStatus+'" type="button" colName="'+fieldList[i].field+'"><span class="glyphicon '+btnIcon+'"></span></button>&nbsp;'+
								    '<button class="btn btn-default" type="button" colName="'+fieldList[i].field+'"><span class="glyphicon glyphicon-remove"></span></button>&nbsp;'+
								'</div>';
		}
	}
	$("#resultList").append(innerStr);
}

//获取提交的html代码
function getHtmlStr(){
	window.frames["mainFrame"].$("#configJs").html("var config="+JSON.stringify(config));
	window.frames["mainFrame"].$("#layuicss-skinlayercss").remove();
    var page = $("#mainFrame").prop('contentWindow').document;
    var pageStr = $(page)[0].documentElement.innerHTML;
    var chart = window.frames["mainFrame"].$(".ajax-data").html();
    pageStr = pageStr.replace(chart,"");
    return pageStr;
}

function selDom(jqDom){}

function btnClick(btn){
	if(btn.children().eq(0).hasClass("glyphicon-ok")){
  		return;
  	}
	//添加列
  	if(btn.children().eq(0).hasClass("glyphicon-link")){
  		btn.removeClass("btn-default");
  		btn.addClass("btn-success");
  		btn.children().eq(0).removeClass("glyphicon-link");
  		btn.children().eq(0).addClass("glyphicon-ok");  		
  	}
    //删除列
  	if(btn.children().eq(0).hasClass("glyphicon-remove")){
  		var list = $("#resultList").find(".btn-success");
  		if(list.length==3){
  			layer.alert("蜘蛛图应至少保留3列。");
  			return;
  		}
  		//只有前面一个为已绑定才起作用
  		if(btn.prev().hasClass("btn-success")){
  			btn.prev().removeClass("btn-success");
			btn.prev().addClass("btn-default")
			btn.prev().children().eq(0).removeClass("glyphicon-ok");
			btn.prev().children().eq(0).addClass("glyphicon-link");
		}
  	}
  	//判断最终选择了多少列，并显示响应chart
  	var list = $("#resultList").find(".btn-success");
	var labels = [],colNames=[];
  	for(var i=0;i<list.length;i++){
  		colNames.push(list.eq(i).attr("colName"));
  		labels.push(list.eq(i).prev().html());
  	}
  	config.colNames = colNames;
  	config.labels = labels;
  	window.frames["mainFrame"].config.colNames = colNames;
  	window.frames["mainFrame"].config.labels = labels;
  	window.frames["mainFrame"].resetChart();
}