function init(){
	this.fnList = [];
}

//params应该包含方法：fn，是否自动允许：autoRun，使用到的url的index：urlIndex
init.prototype.setFn = function(params){
	var fnList = params instanceof Array?params:[params];
	this.fnList = fnList;
}

init.prototype.getFn = function(){
	return this.fnList;
}

init.prototype.setUrl = function(params){
	var urlList = params instanceof Array?params:[params];
	this.urlList = urlList;
	var str = document.body.innerHTML;
	if(window.parent){
		try{
			//在设置url之前，先让父页面把子页面的body内的内容保存下来
			window.parent.writeChildPage(str);
			//编辑情况
			window.parent.createUrlSel(urlList);
		}catch(e){
		}
	}else{
	}
}

init.prototype.setDb = function(set){
	var setList = set instanceof Array?set:[set];
	var str = document.body.innerHTML;
	if(window.parent){
		try{
			//在设置url之前，先让父页面把子页面的body内的内容保存下来
			window.parent.writeChildPage(str);
			//编辑情况
			window.parent.createDbSel(set);
		}catch(e){
		}
	}
}


init.prototype.getDbUrl = function(){
	return config.sqlUrl;
}

init.prototype.getDb = function(id){
	return config.dbSet[id];
}

init.prototype.getPublish = function(){
	return config.publish;
}

init.prototype.getUrl = function(id){
	return config.url[id];
}

init.prototype.getParams = function(id){
	if(config.params[id]){
		var useParams = [];
		for(var i=0;i<config.params[id].length;i++){
			if(config.params[id][i].use){
				useParams.push(config.params[id][i]);
			}
		}
		return useParams;
	}else{
		return [];
	}
}

init.prototype.getTitle = function(id){
	var title = config.title[id]?config.title[id]:"chart";
	return title;
}

init.prototype.getColumns = function(id){
	if(config.colNames[id]){
		return config.colNames[id];
	}else{
		return [];
	}
}

init.prototype.getColCodes = function(id){
	var arr =  config.colNames[id];
	if(!arr){
		return [];
	}
	var list = [];
	for(var i=0;i<arr.length;i++){
		list.push(arr[i].colCode);
	}
	return list;
}

init.prototype.getColNames = function(id){
	var arr =  config.colNames[id];
	if(!arr){
		return [];
	}
	var list = [];
	for(var i=0;i<arr.length;i++){
		list.push(arr[i].colName);
	}
	return list;
}

init.prototype.urlFresh = function(id){
	var fnList = this.fnList;
	for(var i=0;i<fnList.length;i++){
		if(fnList[i].urlId == id){
			fnList[i].fn();
			break;
		}
	}
}

init.prototype.init = function(){
	var fnList = this.fnList;
	if(typeof(config)=="undefined"){
		console.log("config未设置");
		return;
	}
	for(var i=0;i<fnList.length;i++){
		if(fnList[i].autoRun){
			if(config.url[this.fnList[i].urlId]){
				fnList[i].fn();
			}
		}
	}
}

function setCol(interfaces,cols){
	console.log(interfaces);
	console.log(cols);
}

function showImg(str){
	/*try{
		window.parent.showImg(str);
	}catch(e){
		if(document.getElementById("crossDomain")){
			document.body.innerHTML += '<iframe style="display:none;" src="" id="crossDomain"></iframe>';
		}
		document.getElementById("crossDomain").src="parent.html#"+str
	}*/
	var list = str.split(",");
	var json = {
		"status": 1,
		"msg": "",
		"title": "JSON请求的相册",
		"id": 8,
		"start": 0,
		"data": []
	};
	for(var i=0;i<list.length;i++){
		json.data.push({
			"pid": i,
			"src": list[i],
			"thumb": ""
		});
	}
	layer.photos({
    photos: json, //格式见API文档手册页
    anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机
  });
}

function getAt(){
	var arr = (window.location.href).split("#");
	if(arr.length>1){
		return arr[1];
	}else{
		return "";
	}
}

function getRow(json){
	var list = json.RBSPMessage.Method.Items.Item.Value.Row;
	if(list.length>=2){
		list.splice(0,2)
		return list;
	}else{
		return [];
	}
}

function buildJson(result){
	var obj = {
		data:{}
	};
	for(var i=0;i<result.length;i++){
		if(result[i].name == "title"){
			obj.title = result[i].content;
		}else{
			obj.data[result[i].name] = $.type(parseInt(result[i].content))=="number"?parseInt(result[i].content):result[i].content;
		}
	}
	return obj;
}

function createMenu(list){
	frameObj.menu = list;
}

function createConnect(list){
	var objList = [];
	for(var i=0;i<list.length;i++){
		objList.push({
			title:list[i]
		});
	}
	frameObj.connectList = objList;
}