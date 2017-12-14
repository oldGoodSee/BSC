function createUrlSel(list) {
  //重置urlList，这是各全局变量
  urlList = list;
  //重置页面的config对象
  if(typeof(window.frames["mainFrame"].config) == "undefined") {
    config = {
      url: {}, //数组里面是string
			urlName: {},
      title: {},
      colNames: {}, ///二维数组
      params: {},
			paramsReady: {}
    };
  } else {
    config = window.frames["mainFrame"].config;
  }
  var urlObj = {
    colSet: {},
    noColSet: []
  };
  //按需要绑定的对象重新格式化
  for(var i = 0; i < urlList.length; i++) {
    if(urlList[i].colSet.type == -1) {
      urlList[i].selCol = false;
      urlObj.noColSet.push(urlList[i]);
    } else {
      //根据不同类型及绑定的元素来分组，注意，非动态创建的元素必定要绑定
      urlList[i].selCol = true;
      switch(urlList[i].colSet.type) {
        case 0: //要绑定
          if(!urlObj.colSet[urlList[i].renderTo + "_" + urlList[i].colSet.type + "_" + urlList[i].colSet.selector]) {
            urlObj.colSet[urlList[i].renderTo + "_" + urlList[i].colSet.type + "_" + urlList[i].colSet.selector] = [];
          }
          urlObj.colSet[urlList[i].renderTo + "_" + urlList[i].colSet.type + "_" + urlList[i].colSet.selector].push(urlList[i]);
          break;
        case 1: //动态创建
          if(!urlObj.colSet[urlList[i].renderTo + "_" + urlList[i].colSet.type]) {
            urlObj.colSet[urlList[i].renderTo + "_" + urlList[i].colSet.type] = [];
          }
          urlObj.colSet[urlList[i].renderTo + "_" + urlList[i].colSet.type].push(urlList[i]);
          break;
        default:
          break;
      }
    }
  }
	var innerStr = '';
  for(var j in urlObj.colSet) {
    innerStr += createSel(urlObj.colSet[j], 1);
  }
  if(urlObj.noColSet.length > 0) {
    innerStr += createSel(urlObj.noColSet, 0);
  }
	$("#editContent").html(innerStr);
  //即使config为刚初始化，也需要设置，避免子页面报错
  window.frames["mainFrame"].config = config;
  window.frames["mainFrame"].init.init();
  //根据config设置
  //根据刘博的清明前的意见，已发布的只允许删除，不能修改
  //setByConfig();
}

function createDbSel(setList) {
	config = {
		dbSet:{},
		sqlUrl:baseUrl+"/server/widget/queryDB"
	};
	window.frames["mainFrame"].config = config;
  window.frames["mainFrame"].init.init();
	tableList = null;
	var innerStr = "";
	for(var i=0;i<setList.length;i++){
		config.dbSet[setList[i].id] = {};
		innerStr += createSet(setList[i]);
	}
	$("#editContent").html(innerStr);
}

//根据参数创建接口select及对应的colSel，type为0表示
function createSel(urlList, type) {
  var innerStr = type == 0 ? "" : '<div style="border:1px solid #ccc; padding: 10px; margin-bottom: 15px; background-color:#eee;">';
  var setCol = type == 0 ? false : true;
  //根据参数里的initUse判断是否是页面载入是需要使用的，并设置相应的config
  for(var i = 0; i < urlList.length; i++) {
  	var selBy = urlList[i].paramSet?"widget":"interface";
    if(urlList[i].type == 2) {
      innerStr += '<div class="form-group">' +
					        '	 <label>请设置图表标题</label>' +
					        '	 <button class="btn btn-default btn-title" type="button">' +
					        '	    <span class="glyphicon glyphicon-pencil"></span>' +
					        '	 </button>' +
					        '</div>';
    }
    innerStr += '<div class="form-group">' +
					      '	 <label>' + urlList[i].info + '</label>' +
					      '</div>' +
					      '<div class="form-group">' +
					      '  <button style="width:100%; overflow:hidden; text-overflow:ellipsis; white-space: nowrap;" class="btn btn-default interface-btn" type="button" selBy="'+selBy+'" interfaceType="' + urlList[i].type + '" urlId="' + urlList[i].id + '" colNum="' + urlList[i].len + '">请选择接口</button>' +
					      '</div>' +
					      '<p id="explain"></p>';
    if(type == 1) {
      innerStr += '	 <form class="col-set form" style="display:none;"></form>' + //存放参数
									'	 <form class="col-set form" style="display:none;"></form>' + //存放列
        					'  <form class="col-set form" style="margin-top: 5px; display:none;"><span class="txt-gray">请选择接口</span></form>'; //存放接口	 
      innerStr += '</div>';
    }
  }
  return innerStr;
}

function createSet(dbSet){
	var innerStr = "";
	innerStr += '<div class="form-group">' +
							'	 <label>' + dbSet.label + '</label>' +
							'</div>' +
							'<div class="form-group" dbId="'+dbSet.id+'">' +
							'  <button type="button" style="width:100%; overflow:hidden; text-overflow:ellipsis; white-space: nowrap;" class="btn btn-default area-sel">请选择区域</button>' +
							'  <button type="button" style="width:100%; overflow:hidden; text-overflow:ellipsis; white-space: nowrap;" class="btn btn-default db-sel">请选择数据库</button>' +
							'</div>' +
							'<input type="text" class="form-control" id="tableSearchInp" style="width:180px; display:inline-block; margin-right: 5px;" /><button class="btn btn-default table-list-search" type="button">搜索</button>' +
							'<p>根据表的中文名称搜索</p>' +
							'<ul class="table-list" id="tableList"></ul>' +
							'<div id="tablePage"></div>';
	return innerStr;
}

//设置chart的标题
function setChartTitle(btn) {
  layer.prompt({ title: '请输入标题' }, function(text, index) {
    layer.close(index);
    editingTitle.html("已设置标题：" + text);
    var urlId = editingTitle.parent().parent().find(".interface-btn").eq(0).attr("urlId");
    config.title[urlId] = text;
    window.frames["mainFrame"].config = config;
    if(config.url[urlId]) {
      window.frames["mainFrame"].init.urlFresh(urlId);
    }
  });
}

//创建URL选择弹窗
function createSelWin(interfaceObj,selBy) {
	var inpAdd = "";
  //以接口为主,控件模板
	if(interfaceObj.inputCount || interfaceObj.inputCount == 0) {
		inpAdd += '<input type="text" style="display:none" name="inputCount" value="'+interfaceObj.inputCount+'" />';
  }
  //以控件为主
  if(interfaceObj.paramSet) {
    var paramSet = {};
    var paramSetStr = [];
    for(var i = 0; i < interfaceObj.paramSet.length; i++) {
    	if(!paramSet[interfaceObj.paramSet[i].type]){
    		paramSet[interfaceObj.paramSet[i].type] = 1;
    	}else{
    		paramSet[interfaceObj.paramSet[i].type]++;
    	}
    }
    for(i in paramSet){
    	paramSetStr.push(i+"_"+paramSet[i]);
    }
    inpAdd += '<input type="text" style="display:none" name="inputType" value="'+paramSetStr.join(",")+'" />';
  }
	var interfaceContent = '<div style="padding: 20px;">' +
												 '	<form class="form-inline" id="interfaceSelForm">' +
												 '		<div class="form-group">' +
    										 '			<label for="interfaceName">接口名称</label>' +
  										 	 '			<input type="text" class="form-control" name="interfaceName" id="interfaceName" placeholder="接口名称">' +
  										 	 '			<input type="text" style="display:none" name="type" value="'+interfaceObj.type+'">' +
  										 	 				inpAdd +
  											 '		</div>' +
  											 '		<button type="button" id="interfaceSearch" class="btn btn-default" selBy="'+selBy+'">查询</button>'	+
												 '	</form>' +
												 '	<hr />'	+
												 '	<div id="interfaceContainer" class="interface-container" selBy="'+selBy+'" urlId="'+interfaceObj.id+'"></div>' +
												 '	<div id="interfacePage" class="interface-page"></div>' +
												 '</div>';
  layer.open({
    type: 1,
    title: "请选择接口", //不显示标题
    area: ['600px', '400px'],
    content: interfaceContent,
    success:function(){
  		searchInterface(1,selBy);
  	}
  });
}

function createDbSetWin(dbId,type){
	layer.open({
    type: 1,
    title: "请选择接口", //不显示标题
    area: ['600px', '400px'],
    content: '<div id="dbInfo" dbId="'+dbId+'"></div><div class="page-r" id="dbInfoPage"></div>',
    success:function(){
  		getDbInfo(dbId,type,1);
  	}
  });
}
  //根据选择接口创建col
  function createCol(urlObj) {
    var contaienr = urlBtn.parent().parent().children().last();
    var obj = getUrlInfo(urlBtn.attr("urlId"));
    contaienr.attr("colNum", obj.len);
    var innerStr = '<h2 style="border-top: 0; margin-top: 10px; padding-top:0;">可选择的返回列</h2>';
    var fields = $.parseJSON("[" + urlObj.field_meaning + "]");
    for(var i = 0; i < fields.length; i++) {
      innerStr += '<button class="btn btn-default" type="button" colCode="' + fields[i].field + '">' + fields[i].mean + '</button>'
    }
    innerStr += '<h2>已选择的返回列</h2>' +
      					'<div class="result"></div>';
    contaienr.html(innerStr);
  }

  //根据选择接口创建参数选择
  function createParam(urlObj,urlId) {
  	console.log(urlObj);
  	var contaienr = urlBtn.parent().parent().children().eq(urlBtn.parent().parent().children().length - 2);
    var innerStr = '<h2 style="border-top: 0; margin-top: 10px; padding-top:0;">参数设置</h2>';
    
		if(0 == urlObj.input_count) {
			return;
		}
		var paramsInps = $.parseJSON("[" + urlObj.input_details + "]");
		if(paramsInps.length == 0){
			return;
		}
		//以接口为主
		if(urlObj.selBy == "interface"){
			if(paramsInps.length>10){
				innerStr = '<select class="form-control" id="interfaceSelect" urlId="' + urlBtn.attr("urlId") + '" onchange="createParamSingle(this)"><option></option>';
				for(var i = 0; i < paramsInps.length; i++) {
					innerStr += '<option value="'+paramsInps[i].required+'▼'+paramsInps[i].key+'▼'+paramsInps[i].restrict+'▼'+paramsInps[i].length+'▼'+paramsInps[i].type+'">'+paramsInps[i].name+'</option>';
				}
				innerStr += '</select><div urlId="' + urlBtn.attr("urlId") + '" style="margin-top: 10px;"></div>';
			}else{
				for(var i = 0; i < paramsInps.length; i++) {
					var inp = "";
					switch(paramsInps[i].type) {
						case 1: //普通文本输入
							inp = '<input type="text" class="form-control" disabled />';
							break;
						case 2: //select
							inp = '<select class="form-control inp-select" disabled ></select>';
							break;
						case 3: //日期
							inp = '<input type="text" class="form-control inp-date" disabled />';
							break;
						case 4: //大段文本输入
							inp = '<textarea type="text" class="form-control" disabled /></textarea>';
							break;
						default:
							break;
					}
					if(paramsInps[i].required == 0) { //0为必填
						innerStr += '<div class="interface-params-panel param-seled" urlId="' + urlBtn.attr("urlId") + '" key="' + paramsInps[i].key + '" restrict="' + paramsInps[i].restrict + '" length="' + paramsInps[i].length + '" type="' + paramsInps[i].type + '">' +
												'	 <label>' + paramsInps[i].name + '</label><b style="color: red;">*</b> <a class="btn btn-default btn-xs"><span class="glyphicon glyphicon-pencil"></span></a><br />' +
												'	 <input type="checkbox" use="useSettledValue" /> 使用固定值 ' + inp + '<br />' +
												'	 <input type="checkbox" use="useDefaultValue" /> 使用默认值 ' + inp +
												'</div>';
						//在事件里面已经把所有的params全部写入，这里只需要设置是否使用
						config.params[urlBtn.attr("urlId")][i].use = true;
					} else {
						innerStr += '<div class="interface-params-panel" urlId="' + urlBtn.attr("urlId") + '" key="' + paramsInps[i].key + '" restrict="' + paramsInps[i].restrict + '" length="' + paramsInps[i].length + '" type="' + paramsInps[i].type + '">' +
												'  <input type="checkbox" use="setUse" />' +
												'	 <label>' + paramsInps[i].name + '</label><br />' +
												'	 <input type="checkbox" use="useSettledValue" /> 使用固定值 ' + inp + '<br />' +
												'	 <input type="checkbox" use="useDefaultValue" /> 使用默认值 ' + inp +
												'</div>';
						//在事件里面已经把所有的params全部写入，这里只需要设置是否使用
						config.params[urlBtn.attr("urlId")][i].use = false;
					}
				}
			}
		}else{	//以控件为主
			innerStr += '<div class="paramset">';
			//这里的输入的出现顺序是按照页面的设置来的
			for(var i=0;i<config.params[editingUrl.id].length;i++){
				innerStr += '<button class="btn btn-default" type="button" urlId="'+editingUrl.id+'">'+config.params[editingUrl.id][i].name+'</button>';
				config.params[urlBtn.attr("urlId")][i].use = true;
			}
			innerStr += '</div>';
		}
   	//这里因为尚未设置初始列，所以不用刷新，否则会报错
    //window.frames["mainFrame"].init.urlFresh(urlBtn.attr("urlId"));
    contaienr.html(innerStr);
    //设置时间控件
    if($(".inp-date").length > 0) {
      $(".inp-date").datetimepicker({
        format: 'yyyy-mm-dd',
        pickerPosition: "top-left",
        language: "zh",
        minView: "month",
        autoclose: true
      }).on("changeDate", function(ev) {
        var container = $(this).parent();
        var urlId = container.attr("urlId");
        var vSet = container.attr("vSet");
        //因为上面有个h1，所以index值需要-1
        var index = container.index() - 1;
        if(vSet == "useSettled") {
          config.params[urlId][index].settledValue = ev.date.valueOf();
          container.attr("settledValue", ev.date.valueOf());
        } else if(vSet == "useDefault") {
          config.params[urlId][index].defaultValue = ev.date.valueOf();
          container.attr("defaultValue", ev.date.valueOf());
        }
        window.frames["mainFrame"].config = config;
        window.frames["mainFrame"].init.urlFresh(urlId);
      });
    }
    contaienr.show();
  }
	
	//根据选择接口创建参数选择
	//这里只允许单选，所以需要清零
  function createParamSingle(obj) {
		var selObj = $(obj);
		var v = obj.value;
		var name = selObj.find("option:selected").text();
		var i = selObj.find("option:selected").index()-1;
		var urlId = selObj.next().attr("urlId");
		if(!v){
			return;
		}
		var arr = v.split("▼");
		var inp;
		var innerStr = "";
		switch(arr[4]) {
			case "1": //普通文本输入
				inp = '<input type="text" class="form-control" disabled />';
				break;
			case "2": //select
				inp = '<select class="form-control inp-select" disabled ></select>';
				break;
			case "3": //日期
				inp = '<input type="text" class="form-control inp-date" disabled />';
				break;
			case "4": //大段文本输入
				inp = '<textarea type="text" class="form-control" disabled /></textarea>';
				break;
			default:
				break;
		}
		if(arr[0] == "0") { //0为必填
			innerStr += '<div class="interface-params-panel param-seled" urlId="' + urlId + '" key="' + arr[1] + '" restrict="' + arr[2] + '" length="' + arr[3] + '" type="' + arr[4] + '">' +
									'	 <label>' + name + '</label><b style="color: red;">*</b> <a class="btn btn-default btn-xs"><span class="glyphicon glyphicon-pencil"></span></a><br />' +
									'	 <input type="checkbox" use="useSettledValue" /> 使用固定值 ' + inp + '<br />' +
									'	 <input type="checkbox" use="useDefaultValue" /> 使用默认值 ' + inp +
									'</div>';
			//在事件里面已经把所有的params全部写入，这里只需要设置是否使用
			config.params[urlId][i].use = true;
		} else {
			innerStr += '<div class="interface-params-panel" urlId="' + urlId + '" key="' + arr[1] + '" restrict="' + arr[2] + '" length="' + arr[3] + '" type="' + arr[4] + '">' +
									'  <input type="checkbox" use="setUse" />' +
									'	 <label>' + name + '</label><br />' +
									'	 <input type="checkbox" use="useSettledValue" /> 使用固定值 ' + inp + '<br />' +
									'	 <input type="checkbox" use="useDefaultValue" /> 使用默认值 ' + inp +
									'</div>';
			//在事件里面已经把所有的params全部写入，这里只需要设置是否使用
			config.params[urlId][i].use = false;
		}
		//暂时只允许单选，select修改后需要清除之前选择的
		for(var j=0;j<config.params[urlId].length;j++){
			if(config.params[urlId][j].use == true){
				config.params[urlId][j].use = false;
			}
			if(config.params[urlId][j].defaultValue){
				delete config.params[urlId][j].defaultValue;
			}
			if(config.params[urlId][j].settledValue){
				delete config.params[urlId][j].settledValue;
			}
		}
		selObj.next().html(innerStr);
		window.frames["mainFrame"].config = config;
		window.frames["mainFrame"].init.urlFresh(urlId);
		if(selObj.next().find(".inp-date").length > 0) {
      selObj.next().find(".inp-date").datetimepicker({
        format: 'yyyy-mm-dd',
        pickerPosition: "top-left",
        language: "zh",
        minView: "month",
        autoclose: true
      }).on("changeDate", function(ev) {
        var container = $(this).parent();
        var urlId = container.attr("urlId");
        var vSet = container.attr("vSet");
        //因为这里没有H1了，所以不需要-1
        var index = document.getElementById("interfaceSelect").selectedIndex-1;
        if(vSet == "useSettled") {
          config.params[urlId][index].settledValue = ev.date.valueOf();
          container.attr("settledValue", ev.date.valueOf());
        } else if(vSet == "useDefault") {
          config.params[urlId][index].defaultValue = ev.date.valueOf();
          container.attr("defaultValue", ev.date.valueOf());
        }
        window.frames["mainFrame"].config = config;
        window.frames["mainFrame"].init.urlFresh(urlId);
      });
		}
	}

  //点击列的操作
  function selCol(btn, changeConfig) {
    var resultContainer = btn.parent().children().last();
    var colSelDoms = resultContainer.find("span");
    var interfaceSelDoms = btn.parent().parent().find(".interface-btn");
    //判断是否允许添加
    if(parseInt(btn.parent().attr("colNum")) <= colSelDoms.length) {
      layer.alert("已经达到列上限。");
      return;
    }
    //如果是chart，则不允许重复
    if(interfaceSelDoms.eq(0).attr("interfacetype") == "chart") {
      var spans = btn.parent().children().last().find("span");
      for(var i = 0; i < spans.length; i++) {
        if(spans.eq(i).attr("colCode") == btn.attr("colCode")) {
          return;
        }
      }
    }
    resultContainer.append('<span colCode="' + btn.attr("colCode") + '">' + btn.html() + '</span>');
    if(changeConfig) {
      //这里值可能是增加
      for(var i = 0; i < interfaceSelDoms.length; i++) {
        if($.isArray(config.colNames[interfaceSelDoms.eq(i).attr("urlId")])) {
          config.colNames[interfaceSelDoms.eq(i).attr("urlId")].push({
            colName: btn.html(),
            colCode: btn.attr("colCode")
          });
        } else {
          config.colNames[interfaceSelDoms.eq(i).attr("urlId")] = [{
            colName: btn.html(),
            colCode: btn.attr("colCode")
          }];
        }
      }
      window.frames["mainFrame"].config = config;
      //可能会同时刷新多个
      for(var j = 0; j < interfaceSelDoms.length; j++) {
        window.frames["mainFrame"].init.urlFresh(interfaceSelDoms.eq(j).attr("urlId"));
      }
    }
  }

  //删除绑定操作
  function unSelCol(span) {
    var resultContainer = span.parent();
    var interfaceSelDoms = resultContainer.parent().parent().find(".interface-btn");
    for(var i = 0; i < interfaceSelDoms.length; i++) {
      for(var j = 0; j < config.colNames[interfaceSelDoms.eq(i).attr("urlId")].length; j++) {
        if(config.colNames[interfaceSelDoms.eq(i).attr("urlId")][j].colCode == span.attr("colCode")) {
          config.colNames[interfaceSelDoms.eq(i).attr("urlId")].splice(j, 1);
          break;
        }
      }
    }
    span.remove();
    window.frames["mainFrame"].config = config;
    for(var k = 0; k < interfaceSelDoms.length; k++) {
      window.frames["mainFrame"].init.urlFresh(interfaceSelDoms.eq(k).attr("urlId"));
    }
  }

  //参数是否启用
  function paramChange(checkInp) {
    var container = checkInp.parent();
		var index = container.index() - 1;
		if(container.parent().children().length == 1){
			index = document.getElementById("interfaceSelect").selectedIndex-1;
		}
    var urlId = container.attr("urlId");
    if(checkInp.prop("checked")) {
      config.params[urlId][index].use = true;
      /*if(!config.params[urlId]){
      	config.params[urlId] = [];
      }
      config.params[urlId].push({
      	key:container.attr("key"),
      	restrict:container.attr("restrict"),
      	length:container.attr("length"),
      	name:container.find("label").eq(0).html(),
      	type:parseInt(container.attr("type"))
      });*/
      container.addClass("param-seled");
    } else {
      config.params[urlId][index].use = false;
      /*for(var i=0;i<config.params[urlId].length;i++){
      	if(config.params[urlId][i].key == container.attr("key")){
      		config.params[urlId].splice(i,1);
      		container.removeClass("param-seled");
      		break;
      	}
      }*/
      container.removeClass("param-seled");
    }
    window.frames["mainFrame"].config = config;
    window.frames["mainFrame"].init.urlFresh(urlId);
  }
  //参数是否使用固定值
  function useSettled(checkInp) {
    var container = checkInp.parent();
    var index = container.index() - 1;
		if(container.parent().children().length == 1){
			index = document.getElementById("interfaceSelect").selectedIndex-1;
		}
    var inp = container.find("input").eq(container.find("input").length - 3);
    var checkDefalut = container.find("input").eq(container.find("input").length - 2);
    var urlId = container.attr("urlId");
    if(checkInp.prop("checked")) {
      inp.attr("disabled", false);
      container.attr("vSet", "useSettled");
      checkDefalut.prop("checked", false);
      config.params[urlId][index].settledValue = container.attr("settledValue");
			delete config.params[urlId][index].defaultValue
    } else {
      inp.attr("disabled", true);
      container.attr("vSet", "");
      delete config.params[urlId][index].settledValue;
    }
    window.frames["mainFrame"].config = config;
    window.frames["mainFrame"].init.urlFresh(urlId);
  }

  //参数是否使用默认值
  function useDefault(checkInp) {
    var container = checkInp.parent();
    var index = container.index() - 1;
		if(container.parent().children().length == 1){
			index = document.getElementById("interfaceSelect").selectedIndex-1;
		}
    var inp = container.find("input").last();
    var checkDefault = container.find("input").eq(container.find("input").length - 4);
    var urlId = container.attr("urlId");
    if(checkInp.prop("checked")) {
      inp.attr("disabled", false);
      container.attr("vSet", "useDefault");
      checkDefault.prop("checked", false);
      config.params[urlId][index].defaultValue = container.attr("defaultValue");
			//使用默认值要删除固定值，因为固定值优先级比较高
			delete config.params[urlId][index].settledValue;
    } else {
      inp.attr("disabled", true);
      container.attr("vSet", "");
      delete config.params[urlId][index].defaultValue;
    }
    window.frames["mainFrame"].config = config;
    window.frames["mainFrame"].init.urlFresh(urlId);
  }

  //参数设置固定值
  function setSettled(inp) {
    var container = inp.parent();
		var index = container.index() - 1;
		if(container.parent().children().length == 1){
			index = document.getElementById("interfaceSelect").selectedIndex-1;
		}
    var urlId = container.attr("urlId");
    config.params[urlId][index].settledValue = inp.val();
    window.frames["mainFrame"].config = config;
    window.frames["mainFrame"].init.urlFresh(urlId);
  }

  //参数设置默认值
  function setDefault(inp) {
    var container = inp.parent();
    var index = container.index() - 1;
		if(container.parent().children().length == 1){
			index = document.getElementById("interfaceSelect").selectedIndex-1;
		}
    var urlId = container.attr("urlId");
    config.params[urlId][index].defaultValue = inp.val();
    window.frames["mainFrame"].config = config;
    window.frames["mainFrame"].init.urlFresh(urlId);
  }

  //根据url获得interface
  function getInterfaceByUrl(url) {
    for(var i = 0; i < interfaceList.length; i++) {
      if(interfaceList[i].url == url) {
        return interfaceList[i];
      }
    }
  }

  //根据urlId获得注册接口信息
  function getUrlInfo(urlId) {
    for(var i = 0; i < urlList.length; i++) {
      if(urlList[i].id == urlId) {
        return urlList[i];
      }
    }
  }

  //比较两个filed是否一致
  //field1是interfaceList里的field列表，2是由select change创建的
  /*function compareField(fields,field2){
  	for(var i=0;i<fields.length;i++){
  		if(fields[i].fieldMean == field2.label && field[i].fieldName == field2.colName){
  			return true;
  		}
  	}
  	return false;
  }*/

  //比较两个filed组，并返回新的field组里有哪些老的field组里没有的
  //比较2个Interface的fields，返回新的Interface的field有哪些老的没有的,fields1是新的
  /*function comoareInterfacefields(fields1,fields2){
  	var nonIn = [];
  	for(var i=0;i<fields1.length;i++){
  		var flag = false;
  		for(var j=0;j<fields2.length;j++){
  			if(fields2[j].fieldName == fields1[i].fieldName && fields2[j].fieldMean == fields1[i].fieldMean){
  				flag = true;
  				break;
  			}
  		}
  		if(!flag){
  			nonIn.push(fields1[i]);
  		}
  	}
  	return nonIn;
  }*/

  //是根据子页面的config创建的各项内容
  /*function setByConfig(){
  	var btnInterfaces = $("#editContent").find(".interface-btn");
  	for(var i in config.url){
  		for(var j=0;j<btnInterfaces.length;j++){
  			if(btnInterfaces.eq(j).attr("urlid") == i){
  				//设置editingUrl
  				for(var m=0;m<urlList.length;m++){
  					if(urlList[m].id == i){
  						editingUrl = urlList[m];
  						break;
  					}
  				}
  				//设置接口颜色及文字
  				btnInterfaces.eq(j).toggleClass("btn-default");
  				btnInterfaces.eq(j).toggleClass("btn-success");
  				var interfaceObj = getInterface(config.url[i]);
  				btnInterfaces.eq(j).html(interfaceObj.name);
  				//填充列
  				urlBtn = btnInterfaces.eq(j);
  				createCol(interfaceObj);
  				//选择已经选择的列
  				for(var k=0;k<config.colNames[i].length;k++){
  					selCol(btnInterfaces.eq(j).parent().parent().children().last().find("button[colname='"+config.colNames[i][k].colCode+"']"),false);
  				}
  				break;
  			}
  		}
  	}
  }*/