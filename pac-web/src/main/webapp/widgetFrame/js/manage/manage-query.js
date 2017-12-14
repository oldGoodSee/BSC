function init(){
	window.frames["mainFrame"].edit();
	$("#resultList").show();
	//需要获取子页面config
	config = window.frames["mainFrame"].config?window.frames["mainFrame"].config:{};
	//如果已经设置值了，则需要切换select到响应的接口，并在下方的cols列表中选中响应的项
	if(config.url){
		$("#interFaceList").selectpicker('val',config.url);
		$("#interFaceList").trigger("changed.bs.select");
	}
}

function changeInterface(obj){
	$("#resultList").empty();
	for(var i=0;i<interfaceList.length;i++){
		if(interfaceList[i].url == obj.url){
			var fieldList = $.parseJSON("["+interfaceList[i].field_meaning.replace(/\"/g,'"')+"]");
			var innerStr = "";
    		for(var j=0;j<fieldList.length;j++){
    			innerStr += '<div class="form-group">'+
    									'<label>'+fieldList[j].mean+'</label>&nbsp;&nbsp;'+
    									'<button class="btn btn-default" type="button" colName="'+fieldList[j].field+'" action="binding"><span class="glyphicon glyphicon-link"></span></button>&nbsp;'+
    								    '<button class="btn btn-default" type="button" colName="'+fieldList[j].field+'" action="unbinding"><span class="glyphicon glyphicon-remove"></span></button>&nbsp;'+
    								'</div>';
    		}
    		$("#resultList").append(innerStr);
    		//根据容器内的colName来设置btn和对应btn的样式
    		var ths = $("#mainFrame").contents().find("th");
    		for(var k=0;k<ths.length;k++){
    			if(ths.eq(k).attr("colname")){
    				ths.eq(k).addClass("connect-finished");
    				$("#resultList").find("button[colname='"+ths.eq(k).attr("colname")+"']").eq(0).addClass("btn-success").removeClass("btn-default").find(".glyphicon").removeClass("glyphicon-link").addClass("glyphicon-ok");
    			}
    		}
			break;
		}
	}
}

function getHtmlStr(){
	window.frames["mainFrame"].$("#configJs").html("var config="+JSON.stringify(config));
	window.frames["mainFrame"].$("#layuicss-skinlayercss").remove();
    var page = $("#mainFrame").prop('contentWindow').document;
    return $(page)[0].documentElement.innerHTML;
}

function selDom(jqDom){
	//现在只能先选择左侧的列，再选择右侧的按钮
	//先判断是否jqDom就是th
	if(jqDom[0].tagName == "TH"){
		connectDom = jqDom;
		window.frames["mainFrame"].$(".seled").removeClass("seled");
		jqDom.addClass("seled");
	}
}

function btnClick(btn){
	if(btn.children().eq(0).hasClass("glyphicon-ok")){
  		return;
  	}
	//绑定
  	if(btn.children().eq(0).hasClass("glyphicon-link")){
  		//只有选中列才可以绑定
  		//已经选中列
  		if(connectDom){
      		connectDom.attr("colName",btn.attr("colName"));
      		connectDom.removeClass("seled");
      		connectDom.addClass("connect-finished");
      		btn.removeClass("btn-default");
      		btn.addClass("btn-success");
      		btn.children().eq(0).removeClass("glyphicon-link");
      		btn.children().eq(0).addClass("glyphicon-ok");
      		connectDom = null;
  		}
  	}
    //解除绑定
  	if(btn.children().eq(0).hasClass("glyphicon-remove")){
  		//只有前面一个为已绑定才起作用
  		if(btn.prev().hasClass("btn-success")){
  			//清除按钮样式
			btn.prev().removeClass("btn-success");
			btn.prev().addClass("btn-default")
			btn.prev().children().eq(0).removeClass("glyphicon-ok");
			btn.prev().children().eq(0).addClass("glyphicon-link");
			//清除th样式和colName
  			var ths = window.frames["mainFrame"].$(".ajax-data").find("th");
			for(var i=0;i<ths.length;i++){
				if(ths.eq(i).attr("colName") == btn.prev().attr("colName")){
					ths.eq(i).removeClass("connect-finished");
					ths.eq(i).attr("colName","");
					break;
				}
			}
		}
  	}
}