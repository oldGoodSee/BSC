<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

  <head>
    <meta charset="utf-8" />
    <title>大屏定制</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/media/image/favicon.ico" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/js/layui/css/layui.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/css/override.css" />
    <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/es6-promise.auto.min.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/vue.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/layui/layui.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/DateFormat.js" ></script>
  </head>

  <body id="body">
    <div id="main">
    	<div class="tab-title-trigger" v-if="layoutStatus.status != 'layout'" id="tabHover" v-on:mouseenter="showTab"></div>
    	<transition name="fade">
	    	<div class="layui-tab layui-tab-brief tab-title" id="tabBar" v-if="layoutStatus.showTabTitle" v-on:mouseleave="hideTab">
				  <ul class="layui-tab-title">
				    <li v-for="tabItem in tab" v-if="tabItem.visible" v-bind:id="tabItem.id" :class="{'layui-this':tabItem.active}" v-on:click="changeTab">
		          {{ tabItem.name }}
		          <a href="javascript:void(0)"><i class="layui-icon" v-on:click="removeTab">&#x1006;</i></a>
		        </li>
		        <li id="addTab" class="extra-btn" v-if="layoutStatus.inConfig"><i class="layui-icon" v-on:click="addTab">&#xe654;</i></li>
		        <li id="setTab" class="extra-btn" v-if="layoutStatus.inConfig"><i class="layui-icon" v-on:click="setTab">&#xe631;</i></li>
				  </ul>
				</div>
			</transition>
			<!--这个才是真正显示的背景-->
			<transition name="fade" v-for="tabItem in tab">
				<div class="bg" v-if="tabItem.active" v-bind:style="{backgroundImage:'url('+tabItem.bg+')'}"></div>
			</transition>
			<!--这2个是编辑背景是切换用的-->
			<div class="bg" id="bgGhost0" style="display:none; z-index:1;"></div>
			<div class="bg" id="bgGhost1" style="display:none; z-index:2;"></div>
			
      <div class="tab-content">
      	<div v-for="tabItem in tab" v-if="tabItem.active" v-bind:id="tabItem.id" class="tab-content-item" :class="{'no-grid':!layoutStatus.inConfig,'grid-100':tabItem.grid==100,'grid-50':tabItem.grid==50,'grid-20':tabItem.grid==20}" v-bind:style="{ left:tabItem.offset[0]+'px',top:tabItem.offset[1]+'px',width:tabItem.width,height:tabItem.height}" v-on:mousedown="beginLayout" v-on:contextmenu.prevent = "showMenu">
          <div v-for="containerItem in tabItem.container" v-bind:id="containerItem.id" class="container" v-bind:class="{'fullscreen':containerItem.fullscreen,'no-bg':!layoutStatus.inConfig}" v-bind:style="{ width: containerItem.width+'px', height: containerItem.height+'px', left:containerItem.left+'px', top:containerItem.top+'px',zIndex:containerItem.index+1}">
            <div class="move" noFrame=true v-if="layoutStatus.inConfig && containerItem.frame.length==0" v-on:mousedown="beginMove"></div>
            <div class="frame-container-border border-l-t" v-bind:style="{width:tabItem.cornerSize+'px',height:tabItem.cornerSize+'px',background:'url('+tabItem.borderType+'lt.png)'}"></div>
            <div class="frame-container-border border-m-t" v-bind:style="{left:tabItem.cornerSize+'px',right:tabItem.cornerSize+'px',height:tabItem.borderWidth+'px',top:tabItem.borderOffset+'px',background:'url('+tabItem.borderType+'t.png)'}"></div>
            <div class="frame-container-border border-r-t" v-bind:style="{width:tabItem.cornerSize+'px',height:tabItem.cornerSize+'px',background:'url('+tabItem.borderType+'rt.png)'}"></div>
            <div class="frame-container-border border-l" v-bind:style="{top:tabItem.cornerSize+'px',bottom:tabItem.cornerSize+'px',width:tabItem.borderWidth+'px',left:tabItem.borderOffset+'px',background:'url('+tabItem.borderType+'l.png)'}"></div>
            <div class="frame-container-border border-r" v-bind:style="{top:tabItem.cornerSize+'px',bottom:tabItem.cornerSize+'px',width:tabItem.borderWidth+'px',right:tabItem.borderOffset+'px',background:'url('+tabItem.borderType+'r.png)'}"></div>
            <div class="frame-container-border border-l-b" v-bind:style="{width:tabItem.cornerSize+'px',height:tabItem.cornerSize+'px',background:'url('+tabItem.borderType+'lb.png)'}"></div>
            <div class="frame-container-border border-m-b" v-bind:style="{left:tabItem.cornerSize+'px',right:tabItem.cornerSize+'px',height:tabItem.borderWidth+'px',bottom:tabItem.borderOffset+'px',background:'url('+tabItem.borderType+'b.png)'}"></div>
            <div class="frame-container-border border-r-b" v-bind:style="{width:tabItem.cornerSize+'px',height:tabItem.cornerSize+'px',background:'url('+tabItem.borderType+'rb.png)'}"></div>
            <div class="container-resize" v-if="layoutStatus.inConfig" v-on:mousedown="beginContainerResize"></div>
            <div v-for="frameItem in containerItem.frame" class="frame-container" :class="{'active':frameItem.active}">
            	<div class="frame-container-title" v-if="frameItem.titleVisible">
	            	<h1><span>{{frameItem.title}}</span></h1>
	            </div>
	            <div class="move" v-if="layoutStatus.inConfig" v-on:mousedown="beginMove"></div>
	            <a href="javascript:void(0)" class="layui-btn layui-btn-primary layui-btn-small btn-menu" v-if="frameItem.menu && layoutStatus.inConfig" v-on:click="showFrameMenu"></a>
	            <ul v-if="frameItem.showMenu" class="frame-menu" v-on:mouseenter="showBtn" v-on:mouseleave="hideBtn">
	            	<li v-for="menuItem in frameItem.menu">
	            		<a href="javascript:void(0)" v-if="menuItem != '-'" v-on:click="clickMenu" :class="{'active':menuItem.active}">{{menuItem.text}}</a>
	            		<hr v-if="menuItem == '-'" />
	            	</li>
	            </ul>
            	<iframe v-bind:src="frameItem.url" v-if="frameItem.type=='yswyy'" v-bind:id="frameItem.id"></iframe>
            	<iframe v-bind:src="frameItem.url+'#'+frameItem.id" v-if="frameItem.type=='wyy'" v-bind:id="frameItem.id"></iframe>
            </div>
            <a href="javascript:void(0)" class="layui-btn layui-btn-primary layui-btn-small btn-fullscreen" v-if="!containerItem.fullscreen && containerItem.frame.length>0" v-on:click="fullScreen"></a>
            <a href="javascript:void(0)" class="layui-btn layui-btn-primary layui-btn-small btn-restore" v-if="containerItem.fullscreen" v-on:click="restore"></a>
            <a href="javascript:void(0)" v-if="containerItem.frame.length>1" class="next" v-on:click="next"></a>
            <a href="javascript:void(0)" v-if="containerItem.frame.length>1" class="prev" v-on:click="prev"></a>
          	<div class="interval-control" v-if="containerItem.frame.length>1 && layoutStatus.inConfig" style="opacity:0.01; filter:alpha(opacity=1);" v-on:mouseenter="showInterval"  v-on:mouseleave="hideInterval">
          		{{getActive(containerItem)}}/{{containerItem.frame.length}}
          		<a href="javascript:void(0)"><i class="layui-icon">&#xe614;</i></a>
        			是否自动播放
        			<input type="checkbox" name="auto" v-model="containerItem.autoInterval" v-on:change="setAutoInterval" />
        			切换间隔
        			<input type="number" name="time" v-bind:value="containerItem.interval" v-on:change="setIntervalTime" />
          	</div>
          </div>
          
          <div v-for="layerItem in tabItem.layer" v-bind:id="layerItem.id" class="layer" v-bind:class="{'fullscreen':layerItem.fullscreen,'no-bg':!layoutStatus.inConfig}" v-bind:style="{ width: layerItem.width+'px', height: layerItem.height+'px', left:layerItem.left+'px', top:layerItem.top+'px',zIndex:(layerItem.index+10000)}">
          	<div class="move" noFrame=true v-if="layoutStatus.inConfig && layerItem.frame.length==0" v-on:mousedown="beginMove"></div>
          	<div class="container-resize" v-if="layoutStatus.inConfig" v-on:mousedown="beginLayerResize"></div>
            <div v-for="frameItem in layerItem.frame" class="frame-container" :class="{'active':frameItem.active}">
            	<div class="frame-container-title" v-if="frameItem.titleVisible">
	            	<h1><span>{{frameItem.title}}</span></h1>
	            </div>
	            <div class="move" v-if="layoutStatus.inConfig" v-on:mousedown="beginMove"></div>
	            <a href="javascript:void(0)" class="layui-btn layui-btn-primary layui-btn-small btn-menu" v-if="frameItem.menu && layoutStatus.inConfig" v-on:click="showFrameMenu"></a>
	            <ul v-if="frameItem.showMenu" class="frame-menu">
	            	<li v-for="menuItem in frameItem.menu">
	            		<a href="javascript:void(0)" v-on:click="clickMenu">{{menuItem.text}}</a>
	            	</li>
	            </ul>
            	<iframe v-bind:src="frameItem.url" v-if="frameItem.type=='yswyy'" v-bind:id="frameItem.id"></iframe>
            	<iframe v-bind:src="frameItem.url+'#'+frameItem.id" v-if="frameItem.type=='wyy'" v-bind:id="frameItem.id"></iframe>
            </div>
            <a href="javascript:void(0)" class="layui-btn layui-btn-primary layui-btn-small btn-fullscreen" v-if="!layerItem.fullscreen && layerItem.frame.length>0" v-on:click="fullScreen"></a>
            <a href="javascript:void(0)" class="layui-btn layui-btn-primary layui-btn-small btn-restore" v-if="layerItem.fullscreen" v-on:click="restore"></a>
            <a href="javascript:void(0)" v-if="layerItem.frame.length>1" class="next" v-on:click="next"></a>
            <a href="javascript:void(0)" v-if="layerItem.frame.length>1" class="prev" v-on:click="prev"></a>
            <div class="interval-control" v-if="layerItem.frame.length>1 && layoutStatus.inConfig" style="opacity:0.01; filter:alpha(opacity=1);" v-on:mouseenter="showInterval"  v-on:mouseleave="hideInterval">
          		{{getActive(layerItem)}}/{{layerItem.frame.length}}
          		<a href="javascript:void(0)"><i class="layui-icon">&#xe614;</i></a>
        			是否自动播放
        			<input type="checkbox" name="auto" v-model="layerItem.autoInterval" v-on:change="setAutoInterval" />
        			切换间隔
        			<input type="number" name="time" v-bind:value="layerItem.interval" v-on:change="setIntervalTime" />
          	</div>
          </div>
          
          <div class="layout-div" v-if="layoutStatus.visible" v-bind:style="{width:layoutStatus.width+'px',height:layoutStatus.height+'px',left:layoutStatus.left+'px',top:layoutStatus.top+'px'}" v-on:click="layoutStatus.visible = false"></div>
          <div class="layout-container" :class="{'show':layoutStatus.status == 'layout' || layoutStatus.status == 'containerMove' || layoutStatus.status == 'layerMove' || layoutStatus.status == 'containerResize' || layoutStatus.status == 'layerResize' || layoutStatus.status == 'ysWyyDrag' || layoutStatus.status == 'wyyDrag' || layoutStatus.status == 'listDrag'}" v-on:mousemove="layouting" v-on:mouseup="endLayout"></div>
         
         	<div class="ruler-h" v-if="layoutStatus.status == 'layout' || layoutStatus.status == 'layoutReady' || layoutStatus.status == 'containerMove' || layoutStatus.status == 'layerMove' || layoutStatus.status == 'containerResize' || layoutStatus.status == 'layerResize'">
         		<div class="ruler-block" v-bind:style="{left:layoutStatus.left+'px',width:layoutStatus.width+'px'}"></div>
         		<div class="scale"></div>
         	</div>
         	<div class="ruler-v" v-if="layoutStatus.status == 'layout' || layoutStatus.status == 'layoutReady' || layoutStatus.status == 'containerMove' || layoutStatus.status == 'layerMove' || layoutStatus.status == 'containerResize' || layoutStatus.status == 'layerResize'">
         		<div class="ruler-block" v-bind:style="{top:layoutStatus.top+'px',height:layoutStatus.height+'px'}"></div>
         		<div class="scale"></div>
         	</div>
         	
         	<div v-if="layoutStatus.status == 'ysWyyDrag' || layoutStatus.status == 'wyyDrag'" class="drag-el" v-bind:style="{ left: layoutStatus.left+'px',top: layoutStatus.top+'px'}"></div>
         	<div v-for="containerItem in tabItem.container" v-if="layoutStatus.status == 'ysWyyDrag' || layoutStatus.status == 'wyyDrag'" v-bind:id="'ghost_'+containerItem.id" class="ghost-container" v-bind:style="{ width: containerItem.width+'px', height: containerItem.height+'px', left:containerItem.left+'px', top:containerItem.top+'px',zIndex:containerItem.index+1000000}" v-on:mousemove="layouting" v-on:mouseenter="ghostHover" v-on:mouseleave="ghostLeave" v-on:mouseup="endLayout"></div>
      		<div v-for="layerItem in tabItem.layer"  v-if="layoutStatus.status == 'ysWyyDrag' || layoutStatus.status == 'wyyDrag'" v-bind:id="'ghost_'+layerItem.id" class="ghost-layer" v-bind:style="{ width: layerItem.width+'px', height: layerItem.height+'px', left:layerItem.left+'px', top:layerItem.top+'px',zIndex:layerItem.index+2000000}" v-on:mouseenter="ghostHover" v-on:mousemove="layouting" v-on:mouseleave="ghostLeave" v-on:mouseup="endLayout"></div>
        </div>
       
        <div id="appList" class="app-list" v-show="layoutStatus.inConfig">
	       	<h1 v-on:mousedown="dragList">组件列表</h1>
					<div class="layui-btn-group" style="margin:10px 0 0 10px;">
						<button class="layui-btn" v-for="btn in layoutStatus.showType" :class="{'layui-btn-normal':btn.active,'layui-btn-primary':!btn.active}" v-on:click="changeShowType">{{btn.name}}</button>
					</div>

					<!--div class="layui-btn-group" style="margin:10px 0 0 10px;">
						<button class="layui-btn" v-for="codeItem in typeCode1" v-bind:id="codeItem.categoryType" :class="{'layui-btn-normal':codeItem.active,'layui-btn-primary':!codeItem.active}" v-on:click="changeTypeCode">{{codeItem.categoryName}}</button>
					</div-->
					
					<form class="layui-form" action="" style="margin:10px 5px 0 0;">
						<div class="layui-form-item">
							<div class="layui-inline">
								<select name="typeCode1" id="typeCode1" v-on:change="changeTypeCode" lay-ignore class="search-inp" style="margin-left:10px;">
									<option v-for="codeItem in typeCode1" v-bind:value="codeItem.id">{{codeItem.categoryName}}</option>
								</select>
							</div>
							<div class="layui-inline">
								<input type="text" id="filterTxt" class="search-inp" />
								<button type="button" class="layui-btn layui-btn-normal layui-btn-small" v-on:click="filterApp"><i class="layui-icon">&#xe615;</i></button>
							</div>
						</div>
					</form>
		    	<!--ul class="list-type1">
		    		<li v-for="ysWyy in ysWyyList" v-if="filter(ysWyy)">
		    			<a href="javascript:void(0)" v-bind:id="ysWyy.id" v-on:mousedown="dragYsWyy">{{ysWyy.name}}</a>
		    		</li>
		    		<li v-for="wyy in wyyList" v-if="filter(wyy)">
		    			<a href="javascript:void(0)" v-bind:id="wyy.id" v-on:mousedown="dragWyy">{{wyy.name}}</a>
		    		</li>
		    	</ul-->
					<div class="list-type1">
						<table class="layui-table">
							<colgroup>
								<col width="30">
								<col width="250">
								<col width="130">
								<col width="100">
								<col width="100">
							</colgroup>
							<thead>
								<tr>
									<th></th>
									<th>组件名称</th>
									<th>业务类型</th>
									<th>示例图</th>
									<th>发布日期</th>
								</tr> 
							</thead>
							<tbody v-if="layoutStatus.showType[0].active">
								<!--tr v-for="(wyy,index) in wyyList" v-if="filter(wyy)"-->
								<tr v-for="wyy in wyyList" v-if="filter(wyy)">
									<td>{{wyy.index+1}}</td>
									<td v-on:mousedown="dragWyy" v-bind:id="wyy.id">{{wyy.name}}</td>
									<td>{{getTypeName(wyy.typeCode2)}}</td>
									<td>
										<img v-bind:src="wyy.appImg" style="width:40px; height:40px;" />
									</td>
									<td>{{formatDate(wyy.createTime)}}</td>
								</tr>
							</tbody>
							<tbody v-if="layoutStatus.showType[1].active">
								<tr v-for="(yswyy,index) in ysWyyList" v-if="filter(yswyy)">
									<td>{{index+1}}</td>
									<td v-on:mousedown="dragYsWyy">{{yswyy.name}}</td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="list-page" id="appListPage"></div>
		    </div>
      </div>
      
      <div class="contextmenu" v-if="contextmenu.visible" v-bind:style="{ left: contextmenu.left+'px',top:contextmenu.top+'px',height:contextmenu.height+'px'}">
      	<ul>
      		<li v-for="menuItem in contextmenu.item" v-if="!menuItem.disabled">
      			<a href="javascript:void(0)" v-on:click="menuItem.fn">{{menuItem.name}}</a>
      		</li>
      	</ul>
      </div>
	    
	    <div class="main-tool" id="mainTool">
	    	<button class="layui-btn layui-btn-primary layui-btn-small" v-if="layoutStatus.inConfig" v-on:click="publish"><img src="${pageContext.request.contextPath}/modelFrame-tab/img/save.png" style="width:16px; height: 16px;" /></button>
	    	<button class="layui-btn layui-btn-primary layui-btn-small" v-if="!layoutStatus.inConfig" v-on:click="config"><i class="layui-icon">&#xe614;</i></button>
	    	<button class="layui-btn layui-btn-primary layui-btn-small" v-if="layoutStatus.inConfig" v-on:click="view"><i class="layui-icon">&#xe605;</i></button>
    	</div>
    </div>
  </body>
  <script type="text/javascript">
  	var userName = '${sessionUserInfo.userName }';
  	var baseUrl = '${pageContext.request.contextPath}';
  	var appId = ${appId};
  	var activeTab = null;
  	var editingDom = null;
  	var dragWyy = null;
  	var $,layer,form,laypage;
  	var editingFrameObj = null;
  	var intervalFlag = {};
  	
  	var layerList=[];
  	var tabBak;
  	
  	var list = ${packagingDtos};
		 console.log(list);
		var typeReady = false;
		var appReady = false;
		
  	var ysWyyList = [];
  	for(var i=0;i<list.length;i++){
  		ysWyyList.push({
  			id:list[i].id,
  			name:list[i].pageName,
  			url:list[i].url,
				appType:list[i].oappid,
				appId:list[i].pageid
  		});
  	}
  	
  	//组织默认的鼠标右键
  	document.oncontextmenu = function(){
		  return false;
		}
  	
  	layui.use(['layer','form','jquery','laypage'], function(){
		  layer = layui.layer;
		  form = layui.form;
		  $ = layui.jquery;
			laypage = layui.laypage;
		  
      //尝试读取已在编辑的应用
		  if(-1 != appId) {
        $.ajax({
          dataType: "json",
          //type: "POST",
          url: "${pageContext.request.contextPath}/modelFrame-tab/data/configs" + appId + "_txt",
          cache: false,
          success: function(result) {
            appTab.tab = result;
				    if(appTab.tab){
				    	if(appTab.tab.length>0){
				    		for(var i=0;i<appTab.tab.length;i++){
				    			if(appTab.tab[i].active){
				    				activeTab = appTab.tab[i];
				    				setTimeout(function(){
				    					createIntervalAll();
				    				},1000);
				    				break;
				    			}
				    		}
				    	}
				    }
          },
          error: function(jqXHR, textStatus, errorThrown) {
            if(jqXHR.status != 404) {
            	layer.alert("读取配置文件失败");
            }
          }
        });
      }
		  
			//读取类型
      $.ajax({
        async: false,
        cache: false,
        contentType: "application/json",
        type: 'get',
        dataType: "json",
        url: "${pageContext.request.contextPath}/manager/menuAction/queryCategory",
        error: function() {
          layer.alert('获取控件类型失败');
        },
				success:function(res){
					var typeList = res.data
					if(!typeList){
						layer.alert('未获取到控件类型');
						return;
					}
					if(typeList.length==0){
						layer.alert('未获取到控件类型');
						return;
					}
					for(var i=0;i<typeList.length;i++){
						if(typeList[i].categoryType == "1"){
							appTab.typeCode1.push(typeList[i]);
							// if(appTab.typeCode1.length==1){
								// appTab.typeCode1[appTab.typeCode1.length-1].active = true;
							// }else{
								// appTab.typeCode1[appTab.typeCode1.length-1].active = false;
							// }
						}else{
							appTab.typeCode2.push(typeList[i]);
						}
					}
					getWidgetList({
						typeCode1:typeList[0].id,
						pageNum:1
					});
				}
			});
		});
	  
	  //总的对象，保存的是tab部分
    var appTab = new Vue({
      el: '#main',
      data: {
      	baseUrl:"${pageContext.request.contextPath}/",
        tab: [],
        ysWyyList:ysWyyList,
      	wyyList:[],
				typeCode1:[],
				typeCode2:[],
        layoutStatus:{
        	inConfig:false,
        	status:null,
        	beginLeft:0,
        	beginTop:0,
        	left:0,
        	top:0,
        	width:0,
        	height:0,
        	offset:[0,0],
        	visible:false,
        	acceptObj:null,
        	showTabTitle:false,
        	filterTxt:null,
					showType:[{
						id:"wyy",
						name:"系统组件",
						active:true
					},{
						id:"wyWyy",
						name:"第三方组件",
						active:false
					}]
        },
        contextmenu:{
        	visible:false,
        	height:0,
        	item:[{
        		name:"创建容器",
        		disabled:true,
        		fn:function(event){
        			appTab.layoutStatus.visible = false;
        			appTab.layoutStatus.status = null;
        			activeTab.container.push({
        				id:(new Date()).getTime(),
        				status:"",
        				width:appTab.layoutStatus.width,
        				height:appTab.layoutStatus.height,
        				left:appTab.layoutStatus.left,
        				top:appTab.layoutStatus.top,
        				fullscreen:false,
        				autoInterval:false,
								interval:false,
								index:activeTab.container.length-1,
        				frame:[]
        			});
        			appTab.contextmenu.visible = false;
        		}
        	},{
        		name:"添加层",
        		disabled:true,
        		fn:function(event){
        			activeTab.layer.push({
        				id:(new Date()).getTime(),
        				status:"",
        				width:200,
        				height:200,
        				left:Math.round(document.body.offsetWidth/2)-100,
        				top:Math.round(document.body.offsetHeight/2)-100,
        				fullscreen:false,
        				autoInterval:false,
								interval:false,
        				index:activeTab.layer.length-1,
        				frame:[]
        			});
        			appTab.contextmenu.visible = false;
        		}
        	},{
        		name:"删除容器或层",
        		disabled:true,
        		fn:function(event){
        			var arr = editingDom.className.indexOf("container")!=-1?activeTab.container:activeTab.layer;
							var index = getObjInArr(editingDom.id,arr,"id",true)[1];
							arr.splice(index,1);
							editingDom = null;
							appTab.contextmenu.visible = false;
        		}
        	},{
        		name:"删除控件",
        		disabled:true,
        		fn:function(event){
        			var arr = editingDom.className.indexOf("container")!=-1?activeTab.container:activeTab.layer;
        			var obj = getObjInArr(editingDom.id,arr,"id");
        			var index = getObjInArr(true,obj.frame,"active",true)[1];
        			obj.frame.splice(index,1);
        			if(obj.frame.length==1){
        				obj.frame[0].active = true;
        			}else if(obj.frame.length>1){
        				var newIndex = index == obj.frame.length?index-1:index;
        				obj.frame[newIndex].active = true;
        			}
							editingDom = null;
							appTab.contextmenu.visible = false;
        		}
        	},{
        		name:"设置标题",
        		disabled:true,
        		fn:function(event){
							var container = getObjById(editingDom.id,"container");
							var frameObj = getObjInArr(true,container.frame,"active");
        			layer.open({
					      type: 2,
					      title: '设置标题',
					      shadeClose: false,
					      shade: 0.3,
					      area: ['680px', '450px'],
					      content: '${pageContext.request.contextPath}/manager/menuAction/setTitle?frameId='+frameObj.id
					    });
					  	appTab.contextmenu.visible = false;
        		}
        	},{
        		name:"设置大屏",
        		disabled:true,
        		fn:function(event){
				  		layer.open({
					      type: 2,
					      title: '大屏设置',
					      shadeClose: false,
					      shade: 0.3,
					      area: ['680px', '450px'],
					      content: '${pageContext.request.contextPath}/manager/menuAction/setScreen'
					    });
					  	appTab.contextmenu.visible = false;
        		}
        	},{
        		name:"使用模板",
        		disabled:true,
        		fn:function(event){
        			tabBak = deepClone(appTab.tab);
        			layer.open({
					      type: 2,
					      title: '使用模板',
					      shadeClose: false,
					      shade: 0.3,
					      area: ['680px', '450px'],
					      content: '${pageContext.request.contextPath}/manager/menuAction/useTemplate',
					      btn: ['确定', '取消'],
							  yes: function(index, layero){
							    tabBak = null;
									layer.closeAll();
							  },
							  btn2: function(index, layero){
							    appTab.tab = deepClone(tabBak);
							    activeTab = getObjInArr(true,appTab.tab,"active");
									tabBak = null;
									layer.closeAll();
							  },
							  cancel: function(){
							    appTab.tab = deepClone(tabBak);
							    activeTab = getObjInArr(true,appTab.tab,"active");
									tabBak = null;
									layer.closeAll();
							  }
					    });
					  	appTab.contextmenu.visible = false;
        		}
        	},{
        		name:"保存模板",
        		disabled:true,
        		fn:function(event){
        			layer.prompt({
							  formType: 0,
							  title: '请输入标题',
							}, function(value, index, elem){
								var reg1 = new RegExp(/^[0-9]{1,}$/);
								var reg2 = new RegExp(/^[\da-zA-Z\u4e00-\u9f5a]{1,20}$/);
								
								if(value.match(reg1)){
									layer.alert("标题必须由1-20个数字字母和汉字组成，且不得为纯数字");
									return;
								}
								if(!value.match(reg2)){
									layer.alert("标题必须由1-20个数字字母和汉字组成，且不得为纯数字");
									return;
								}
								appTab.contextmenu.visible = false;
								var data = {
					        appId: appId,
					        file: encodeURI(JSON.stringify(appTab.tab)),
					        fileName:value,
					        rate: "[]"
					      };
					      $.ajax({
					        async: false,
					        cache: false,
					        contentType: "application/json",
					        type: 'post',
					        dataType: "json",
					        data: JSON.stringify(data),
					        url: "${pageContext.request.contextPath}/manager/menuAction/saveConfigs",
					        error: function() {
					          layer.alert('保存模板失败');
					        },
					        success: function(res) { //请求成功后处理函数。
					          result = res.result;
					          if(res.result == "success") {
					            layer.alert("保存模板成功！",function(){
												layer.closeAll();
					            });
					          } else {
					            layer.alert(res.reason);
					          }
					        }
					      });
							});
        		}
        	},{
        		name:"重置tab",
        		disabled:true,
        		fn:function(event){
        			activeTab.container=[];
        			activeTab.layer=[];
        			activeTab.group=[];
  						editingDom = null;
  						dragWyy = null;
  						appTab.layoutStatus.acceptObj = null;
  						appTab.layoutStatus.status = null;
  						appTab.layoutStatus.visible = false;
  						appTab.contextmenu.visible = false;
        		}
        	}]
        }
      },
      methods:{
      	config:function(event){
      		appTab.layoutStatus.inConfig = true;
      		if(appTab.tab.length==0){
      			addTab();
      		}
      	},
      	view:function(event){
      		appTab.layoutStatus.inConfig = false;
      		appTab.layoutStatus.visible = false;
      		appTab.layoutStatus.status = null;
      	},
      	addTab:function(event){
      		addTab();
      	},
      	setTab:function(event){
		  		layer.open({
			      type: 2,
			      title: 'tab显示设置',
			      shadeClose: false,
			      shade: 0.3,
			      area: ['400px', '300px'],
			      content: '${pageContext.request.contextPath}/manager/menuAction/setTab'
			   });
			  	appTab.contextmenu.visible = false;
      	},
      	changeTab:function(event){
      		var id = event.target.id;
      		if(!id){  //这个是表示点击了关闭操作触发的
      			return;
      		}
      		var obj = getObjInArr(id,appTab.tab,"id");
      		if(obj.active){
      			return;
      		}
      		activeTab.active = false;
      		obj.active = true;
      		activeTab = obj;
      		appTab.contextmenu.visible = false;
      	},
      	removeTab:function(event){
      		var id = event.target.className == "layui-icon"?event.target.parentNode.parentNode.id:event.target.parentNode.id;
      		var arr = getObjInArr(id,appTab.tab,"id",true);
      		var obj = arr[0];
      		var index = arr[1];
      		if(obj.active){
      			if(appTab.tab.length!=1){
      				if(index == 0){ //下一个激活
      					appTab.tab[index+1].active = true;
      					activeTab = appTab.tab[index+1];
      				}else{	//上一个激活
      					appTab.tab[index-1].active = true;
      					activeTab = appTab.tab[index-1];
      				}
      			}
      			appTab.contextmenu.visible = false;
      		}
      		appTab.tab.splice(index,1);
      	},
      	showTab:function(event){
      		appTab.layoutStatus.showTabTitle = true;
      	},
      	hideTab:function(event){
      		appTab.layoutStatus.showTabTitle = false;
      	},
      	beginLayout:function(event){
      		if(!appTab.layoutStatus.inConfig){
      			return;
      		}
      		if(event.which == 1){
      			if($(event.target).hasClass("tab-content-item")){
	      			appTab.layoutStatus.visible = true;
	      			appTab.layoutStatus.width = activeTab.grid;
	      			appTab.layoutStatus.height = activeTab.grid;
	      			appTab.layoutStatus.left = Math.floor((event.offsetX - activeTab.offset[0])/activeTab.grid)*activeTab.grid;
	      			appTab.layoutStatus.top = Math.floor((event.offsetY - activeTab.offset[1])/activeTab.grid)*activeTab.grid;
	      			appTab.layoutStatus.beginLeft = Math.floor((event.offsetX - activeTab.offset[0])/activeTab.grid)*activeTab.grid;
	      			appTab.layoutStatus.beginTop = Math.floor((event.offsetY - activeTab.offset[1])/activeTab.grid)*activeTab.grid;
	      			appTab.layoutStatus.status = "layout";
	      		}
      		}
      		appTab.contextmenu.visible = false;
      	},
      	layouting:function(event){
      		switch(appTab.layoutStatus.status){
      			case "layout":
      				appTab.layoutStatus.visible = true;
	      			//向左
	      			if(event.offsetX<appTab.layoutStatus.beginLeft){
	      				appTab.layoutStatus.left = Math.floor((event.offsetX - activeTab.offset[0])/activeTab.grid)*activeTab.grid;
	      				appTab.layoutStatus.width = Math.ceil((appTab.layoutStatus.beginLeft - event.offsetX)/activeTab.grid)*activeTab.grid + activeTab.grid;
	      			}else if(event.offsetX>appTab.layoutStatus.beginLeft+activeTab.grid){	//向右，只有超过右侧边界才会触发
	      				appTab.layoutStatus.left = appTab.layoutStatus.beginLeft;
	      				appTab.layoutStatus.width = Math.ceil((event.offsetX - appTab.layoutStatus.beginLeft)/activeTab.grid)*activeTab.grid;
	      			}
	      			//向上
	      			if(event.offsetY<appTab.layoutStatus.beginTop){
	      				appTab.layoutStatus.top = Math.floor((event.offsetY - activeTab.offset[1])/activeTab.grid)*activeTab.grid;
	      				appTab.layoutStatus.height = Math.ceil((appTab.layoutStatus.beginTop - event.offsetY)/activeTab.grid)*activeTab.grid + activeTab.grid;
	      			}else if(event.offsetY>appTab.layoutStatus.beginTop+activeTab.grid){	//向右，只有超过下方边界才会触发
	      				appTab.layoutStatus.top = appTab.layoutStatus.beginTop;
	      				appTab.layoutStatus.height = Math.ceil((event.offsetY - appTab.layoutStatus.beginTop)/activeTab.grid)*activeTab.grid;
	      			}
      				break;
      			case "containerMove":
      				var container = getObjInArr("move",activeTab.container,"status");
	      			container.left = event.pageX - appTab.layoutStatus.offset[0];
	      			container.top = event.pageY - appTab.layoutStatus.offset[1];
	      			appTab.layoutStatus.left = Math.round(container.left/activeTab.grid)*activeTab.grid;
	    				appTab.layoutStatus.top = Math.round(container.top/activeTab.grid)*activeTab.grid;
      				break;
      			case "layerMove":
      				var layerObj = getObjInArr("move",activeTab.layer,"status");
	      			layerObj.left = event.pageX - appTab.layoutStatus.offset[0];
	      			layerObj.top = event.pageY - appTab.layoutStatus.offset[1];
	      			appTab.layoutStatus.left = event.pageX - appTab.layoutStatus.offset[0];
	    				appTab.layoutStatus.top = event.pageY - appTab.layoutStatus.offset[1];
      				break;
      			case "containerResize":
      				var container = getObjInArr("resize",activeTab.container,"status");
		      		container.width = Math.abs(event.offsetX - container.left);
		      		container.height = Math.abs(event.offsetY - container.top);
	      			appTab.layoutStatus.width = Math.round((event.offsetX - container.left)/activeTab.grid)*activeTab.grid;
	      			appTab.layoutStatus.height = Math.round((event.offsetY - container.top)/activeTab.grid)*activeTab.grid;
	      			break;
	      		case "layerResize":
      				var layerObj = getObjInArr("resize",activeTab.layer,"status");
		      		layerObj.width = Math.abs(event.offsetX - layerObj.left);
		      		layerObj.height = Math.abs(event.offsetY - layerObj.top);
		      		appTab.layoutStatus.width = Math.abs(event.offsetX - layerObj.left);
	    				appTab.layoutStatus.height = Math.abs(event.offsetY - layerObj.top);
	      			break;
	      		case "ysWyyDrag":
      				if($("#"+event.target.id).hasClass("ghost-container") || $("#"+event.target.id).hasClass("ghost-layer")){
      					appTab.layoutStatus.left=event.offsetX + $("#"+event.target.id).position().left;
	      				appTab.layoutStatus.top=event.offsetY+ $("#"+event.target.id).position().top;
      				}else{
      					appTab.layoutStatus.left=event.offsetX;
	      				appTab.layoutStatus.top=event.offsetY;
      				}
	      			break;
	      		case "wyyDrag":
      				if($("#"+event.target.id).hasClass("ghost-container") || $("#"+event.target.id).hasClass("ghost-layer")){
      					appTab.layoutStatus.left=event.offsetX + $("#"+event.target.id).position().left;
	      				appTab.layoutStatus.top=event.offsetY+ $("#"+event.target.id).position().top;
      				}else{
      					appTab.layoutStatus.left=event.offsetX;
	      				appTab.layoutStatus.top=event.offsetY;
      				}
	      			break;
						case "listDrag":
							$("#appList").css({
								right:($("body").width()-event.offsetX-appTab.layoutStatus.left)+'px',
								top:(event.offsetY-appTab.layoutStatus.top)+'px'
							})
	      			break;
	      		default:
	      			break;
      		}
      	},
      	endLayout:function(event){
      		switch(appTab.layoutStatus.status){
      			case "layout":
      				appTab.layoutStatus.status = "layoutReady";
      				break;
      			case "containerMove":
      				var container = getObjInArr("move",activeTab.container,"status");
	      			appTab.layoutStatus.status = null;
	      			container.left = appTab.layoutStatus.left;
	      			container.top = appTab.layoutStatus.top;
	      			appTab.layoutStatus.visible = false;
	      			container.status = null;
	      			break;
	      		case "layerMove":
	      			appTab.layoutStatus.status = null;
	      			appTab.layoutStatus.visible = false;
	      			var layerObj = getObjInArr("move",activeTab.layer,"status");
	      			layerObj.status = null;
	      			break;
	      		case "containerResize":
      				var container = getObjInArr("resize",activeTab.container,"status");
	      			appTab.layoutStatus.status = null;
	      			container.width = appTab.layoutStatus.width;
	      			container.height = appTab.layoutStatus.height;
	      			appTab.layoutStatus.visible = false;
      				break;
      			case "layerResize":
	      			appTab.layoutStatus.status = null;
	      			appTab.layoutStatus.visible = false;
      				break;
      			case "ysWyyDrag":
	      			appTab.layoutStatus.status = null;
	      			appTab.layoutStatus.visible = false;
	      			if(!appTab.layoutStatus.acceptObj || document.getElementById("ghost_"+appTab.layoutStatus.acceptObj.id).className.indexOf("ghost-hover") == -1){
	      				appTab.layoutStatus.acceptObj = null;
	      				return;
	      			}
	      			if(appTab.layoutStatus.acceptObj.frame.length>0){
	      				var activeFrame = getObjInArr(true,appTab.layoutStatus.acceptObj.frame,'active');
	      				layer.confirm('该容器中已经有控件了，您想执行什么操作？', {
								  btn: ['添加','替换']
								}, function(index){
									activeFrame.active = false;
									if(appTab.layoutStatus.acceptObj.frame.length == 1){	//由1个变到多个需要改变interval的值
										appTab.layoutStatus.acceptObj.interval = 5000;
										appTab.layoutStatus.acceptObj.autoInterval = true;
										addInterval(appTab.layoutStatus.acceptObj);
									}
			      			appTab.layoutStatus.acceptObj.frame.push({
										"id": (new Date()).getTime(),
										"url": dragWyy.url,
										"type":"yswyy",
										"title":dragWyy.name,
										"titleVisible":false,
										"active": true,
										"menu":false,
										"appType":dragWyy.appType,
										"appId":dragWyy.appId
									});
      						appTab.layoutStatus.acceptObj = null;
      						dragWyy = null;
      						layer.close(index);
								}, function(index){
								  activeFrame.id = (new Date()).getTime();
								  activeFrame.url = dragWyy.url;
								  activeFrame.type = "yswyy";
								  activeFrame.title = dragWyy.name;
								  activeFrame.titleVisible = false;
								  activeFrame.menu = false;
								  activeFrame.appType = dragWyy.appType;
								  activeFrame.appId = dragWyy.appType;
								  appTab.layoutStatus.acceptObj = null;
								  dragWyy = null;
      						layer.close(index);
								});
	      			}else{
	      				appTab.layoutStatus.acceptObj.frame.push({
									"id": dragWyy.id,
									"url": dragWyy.url,
									"type":"yswyy",
									"title":dragWyy.name,
									"titleVisible":false,
									"active": true,
									"menu":false,
									"appType":dragWyy.appType,
									"appId":dragWyy.appId
								});
	      				appTab.layoutStatus.acceptObj = null;
							  dragWyy = null;
	      			}
	      			break;
	      		case "wyyDrag":
	      			appTab.layoutStatus.status = null;
	      			appTab.layoutStatus.visible = false;
	      			if(!appTab.layoutStatus.acceptObj || !$("#ghost_"+appTab.layoutStatus.acceptObj.id).hasClass("ghost-hover")){
	      				appTab.layoutStatus.acceptObj = null;
	      				return;
	      			}
	      			if(appTab.layoutStatus.acceptObj.frame.length>0){
	      				var activeFrame = getObjInArr(true,appTab.layoutStatus.acceptObj.frame,'active');
	      				layer.confirm('该容器中已经有控件了，您想执行什么操作？', {
								  btn: ['添加','替换']
								}, function(index){
									setUrlByWidgetId(dragWyy,"add",activeFrame,appTab.layoutStatus.acceptObj,index);
								}, function(index){
									setUrlByWidgetId(dragWyy,"replace",activeFrame,index);
								});
	      			}else{
								setUrlByWidgetId(dragWyy,"add","",appTab.layoutStatus.acceptObj);
	      			}
	      			break;
						case "listDrag":
							appTab.layoutStatus.status = null;
	      			appTab.layoutStatus.visible = false;
							break;
      			default:
      				break;
      		}
      	},
      	hideMenu:function(event){
      		appTab.contextmenu.visible = false;
      	},
      	beginMove:function(event){
      		if(event.which != 1){
      			return;
      		}
      		var id,containerDom;
      		if($(event.target).attr("noFrame")){
      			id = event.target.parentNode.id;
      			containerDom = $(event.target.parentNode);
      		}else{
      			id = event.target.parentNode.parentNode.id;
      			containerDom = $(event.target.parentNode.parentNode);
      		}
      		if(containerDom.hasClass("container")){
	      		//这里需要重新设置每个容器的index
	      		var container = getObjInArr(id,activeTab.container,"id");
	      		container.status = "move";
		    		appTab.contextmenu.visible = false;
		    		appTab.layoutStatus.offset[0] = event.pageX - container.left;
		    		appTab.layoutStatus.offset[1] = event.pageY - container.top;
		    		appTab.layoutStatus.status="containerMove";
		    		appTab.layoutStatus.visible = true;
		    		appTab.layoutStatus.left = container.left;
		    		appTab.layoutStatus.top = container.top;
		    		appTab.layoutStatus.width = container.width;
		    		appTab.layoutStatus.height = container.height;
	      		for(var i=0;i<activeTab.container.length;i++){
	      			if(activeTab.container[i].id != id && activeTab.container[i].index > container.index){
	      				activeTab.container[i].index--;
	      			}
	      		}
	      		container.index = activeTab.container.length-1;
      		}else{
      			var layerObj = getObjInArr(id,activeTab.layer,"id");
	      		layerObj.status = "move";
	      		appTab.contextmenu.visible = false;
	      		appTab.layoutStatus.offset[0] = event.pageX - layerObj.left;
	      		appTab.layoutStatus.offset[1] = event.pageY - layerObj.top;
	      		appTab.layoutStatus.left = layerObj.left;
	      		appTab.layoutStatus.top = layerObj.top;
	      		appTab.layoutStatus.width = layerObj.width;
	      		appTab.layoutStatus.height = layerObj.height;
	      		appTab.layoutStatus.status="layerMove";
	      		//这里需要重新设置每个容器的index
	      		for(var i=0;i<activeTab.layer.length;i++){
	      			if(activeTab.layer[i].id != id && activeTab.layer[i].index>layer.index){
	      				activeTab.layer[i].index--;
	      			}
	      		}
	      		layerObj.index = activeTab.layer.length-1;
      		}
      		event.preventDefault();
      	},
      	beginContainerResize:function(event){
      		if(event.which != 1){
      			return;
      		}
      		var id = event.target.parentNode.id;
      		var container = getObjInArr(id,activeTab.container,"id");
      		container.status = "resize";
      		appTab.layoutStatus.status="containerResize";
      		appTab.layoutStatus.width = container.width;
      		appTab.layoutStatus.height = container.height;
      		appTab.layoutStatus.left = container.left;
      		appTab.layoutStatus.top = container.top;
      		appTab.layoutStatus.visible = true;
      		event.preventDefault();
      	},
      	beginLayerResize:function(event){
      		if(event.which != 1){
      			return;
      		}
      		var id = event.target.parentNode.id;
      		var layerObj = getObjInArr(id,activeTab.layer,"id");
      		layerObj.status = "resize";
      		appTab.layoutStatus.status="layerResize";
      		appTab.layoutStatus.left = layerObj.left;
      		appTab.layoutStatus.top = layerObj.top;
      		appTab.layoutStatus.visible = true;
      		event.preventDefault();
      	},
      	dragYsWyy:function(event){
      		if(event.which == 1){
	      		appTab.layoutStatus.status="ysWyyDrag";
	      		appTab.layoutStatus.left=event.pageX;
	      		appTab.layoutStatus.top=event.pageY;
						var index = parseInt($(event.target).prev().html()-1);
	      		dragWyy = appTab.ysWyyList[index];
      		}
      		event.preventDefault();
      	},
      	dragWyy:function(event){
      		if(event.which == 1){
	      		appTab.layoutStatus.status="wyyDrag";
	      		appTab.layoutStatus.left=event.pageX;
	      		appTab.layoutStatus.top=event.pageY;
	      		dragWyy = getObjInArr(event.target.id,appTab.wyyList,"id");
      		}
      		event.preventDefault();
      	},
				dragList:function(event){
      		if(event.which == 1){
	      		appTab.layoutStatus.status="listDrag";
						appTab.layoutStatus.left = $(event.target).width()-event.offsetX;
						appTab.layoutStatus.top = event.offsetY;
      		}
      		event.preventDefault();
      	},
      	ghostHover:function(event){
      		var id = event.target.id.split("_")[1];
      		var arr = event.target.className == "ghost-container"?activeTab.container:activeTab.layer;
      		appTab.layoutStatus.acceptObj = getObjInArr(id,arr,"id");
      		event.target.className += ' ghost-hover';
      		console.log(appTab.layoutStatus.acceptObj);
      	},
      	ghostLeave:function(event){
      		//擦，这里因为有提示弹窗，触发了万恶的ghostLeave
      		//appTab.layoutStatus.acceptObj = null;
      		event.target.className = event.target.className.split(" ")[0];
      	},
      	showMenu:function(event){
      		if(!appTab.layoutStatus.inConfig){
      			return;
      		}
    			appTab.contextmenu.visible = false;
      		//判断是否有iframe
      		var arr;
      		if(event.target.className == "layout-div"){
      			arr = [0,5,6,7,8];
      			editingDom = null;
      		}else if($(event.target.parentNode).hasClass("container") || $(event.target.parentNode.parentNode).hasClass("container")){
      			var id = $(event.target.parentNode).hasClass("container")?event.target.parentNode.id:event.target.parentNode.parentNode.id;
      			var container = getObjInArr(id,activeTab.container,"id");
      			var arr = container.frame.length==0?[2,5,6,7,8]:[2,3,4,5,6,7,8];
      			appTab.layoutStatus.visible = false;
      			editingDom = $(event.target.parentNode).hasClass("container")?event.target.parentNode:event.target.parentNode.parentNode;
      		}else if($(event.target.parentNode).hasClass("layer") || $(event.target.parentNode.parentNode).hasClass("layer")){
      			var id = $(event.target.parentNode).hasClass("layer")?event.target.parentNode.id:event.target.parentNode.parentNode.id;
      			var layerObj = getObjInArr(id,activeTab.layer,"id");
      			var arr = layerObj.frame.length==0?[2,5,6,7,8]:[2,3,4,5,6,7,8];
      			appTab.layoutStatus.visible = false;
      			editingDom = $(event.target.parentNode).hasClass("layer")?event.target.parentNode:event.target.parentNode.parentNode;
      		}else if(event.target.className.indexOf("tab-content-item") != -1){
      			arr = [1,5,6,7,8];
      			appTab.layoutStatus.visible = false;
      			editingDom = null;
      		}
      		setMenu(arr);
      		appTab.contextmenu.height = arr.length*30+20;
      		appTab.contextmenu.left = event.pageX + 200 > document.body.offsetWidth?event.pageX - 200:event.pageX;
      		appTab.contextmenu.top = event.pageY + appTab.contextmenu.height > document.body.offsetHeight?event.pageY - appTab.contextmenu.height:event.pageY;
      		appTab.contextmenu.visible = true;
    		},
    		next:function(event){
    			var arr = event.target.parentNode.className.indexOf("container")!=-1?activeTab.container:activeTab.layer;
    			var id = event.target.parentNode.id;
    			var obj = getObjInArr(id,arr,"id");
    			var index = getObjInArr(true,obj.frame,"active",true)[1];
    			var newIndex = index == obj.frame.length-1?0:index+1;
    			obj.frame[index].active = false;
    			obj.frame[newIndex].active = true;
    		},
    		prev:function(event){
    			var arr = event.target.parentNode.className.indexOf("container")!=-1?activeTab.container:activeTab.layer;
    			var id = event.target.parentNode.id;
    			var obj = getObjInArr(id,arr,"id");
    			var index = getObjInArr(true,obj.frame,"active",true)[1];
    			var newIndex = index == 0?obj.frame.length-1:index-1;
    			obj.frame[index].active = false;
    			obj.frame[newIndex].active = true;
    		},
    		fullScreen:function(event){
    			var id = event.target.parentNode.id;
    			var arr = event.target.parentNode.className.indexOf("container") != -1?activeTab.container:activeTab.layer;
    			var obj = getObjInArr(id,arr,"id");
    			obj.fullscreen = true;
    			$("#mainTool").hide();
    			$("#tabHover").addClass("hide");
    			$("#tabBar").addClass("hide");
    		},
    		restore:function(event){
    			var id = event.target.parentNode.id;
    			var arr = event.target.parentNode.className.indexOf("container") != -1?activeTab.container:activeTab.layer;
    			var obj = getObjInArr(id,arr,"id");
    			obj.fullscreen = false;
    			$("#mainTool").show();
    			$("#tabHover").removeClass("hide");
    			$("#tabBar").removeClass("hide");
    		},
    		showFrameMenu:function(event){
    			var id = $(event.target).next().attr("id");
    			editingFrameObj = getObjById(id,"frame");
    			editingFrameObj.showMenu = true;
    			return true;
    		},
    		showBtn:function(event){
    			$(event.target).prev().addClass("btn-show");
    		},
    		hideBtn:function(event){
    			var btn = $(event.target).prev();
    			var id = $(event.target).next().attr("id");
    			var frameObj = getObjById(id,"frame");
    			frameObj.showMenu = false;
    			setTimeout(function(){
    				btn.removeClass("btn-show");
    			},60);    			
    		},
    		clickMenu:function(event){
    			$(event.target.parentNode.parentNode).prev().removeClass("btn-show");
    			var index = $(event.target.parentNode).index();
    			var id= $(event.target.parentNode.parentNode).next().attr("id");
    			var activeFrame = getObjById(id,"frame");
    			activeFrame.menu[index].fn();
    			activeFrame.showMenu = false;
    		},
    		publish:function(){
					var rateObj = {};
    			var publishObj = deepClone(appTab.tab);
    			publishObj[0].appName = decodeURI(encodeURI('${param.appName}'));
					for(var i=0;i<appTab.tab.length;i++){
						var containerList = appTab.tab[i].container;
						var layerList = appTab.tab[i].layer;
						countRate(rateObj,containerList);
						countRate(rateObj,layerList);
					}
					var rateArr = [];
					for (var i in rateObj) {
						rateArr.push(rateObj[i]);
					}
    			var data = {
		        appId: appId,
		        file: encodeURI(JSON.stringify(publishObj)),
		        rate: JSON.stringify(rateArr)
		      };
		      $.ajax({
		        async: false,
		        cache: false,
		        contentType: "application/json",
		        type: 'post',
		        dataType: "json",
		        data: JSON.stringify(data),
		        url: "${pageContext.request.contextPath}/manager/menuAction/publish",
		        error: function() {
		          layer.alert('请求失败');
		        },
		        success: function(res) { //请求成功后处理函数。
		          result = res.result;
		          if(res.result == "success") {
		            layer.alert("保存成功！");
		          } else {
		            layer.alert(res.reason);
		          }
		        }
		      });
    		},
    		getActive:function(obj){
    			return getObjInArr(true,obj.frame,"active",true)[1]+1;
    		},
    		showInterval:function(event){
    			$(event.target).animate({
    				"opacity":1,
						"filter":"alpha(opacity=100)"
    			},300);
    		},
    		hideInterval:function(event){
    			$(event.target).animate({
    				"opacity":0.01,
						"filter":"alpha(opacity=1)"
    			},300);
    		},
    		setAutoInterval:function(event){
    			var id = event.target.parentNode.parentNode.id;
    			var obj = getObjById(id,"container");
    			obj.autoInterval = $(event.target).prop("checked");
    			if(obj.autoInterval){
    				addInterval(obj);
    			}else{
    				clearInterval(intervalFlag[id]);
    			}
    		},
    		setIntervalTime:function(event){
    			var id = event.target.parentNode.parentNode.id;
    			var obj = getObjById(id,"container");
    			obj.interval = $(event.target).val();
    			clearInterval(intervalFlag[id]);
    			addInterval(obj);
    		},
				changeShowType:function(event){
					if(event.target.innerHTML == '系统组件'){
						appTab.layoutStatus.showType[0].active = true;
						appTab.layoutStatus.showType[1].active = false;
						$("#typeCode1").show();
						$("#appListPage").show();
					}else{
						appTab.layoutStatus.showType[0].active = false;
						appTab.layoutStatus.showType[1].active = true;
						$("#typeCode1").hide();
						$("#appListPage").hide();
					}
					// $("#filterTxt").val("");
					// appTab.layoutStatus.filterTxt="";
					getWidgetList({
						typeCode1:$("#typeCode1").val(),
						pageNum:1,
						filterTxt:$("#filterTxt").val()
					});
				},
				changeTypeCode:function(event){
					var val = event.target.value;
					getWidgetList({
						typeCode1:val,
						pageNum:1
						// true
					});
				},
				checkShow:function(typeCode){
					var obj = getObjInArr(typeCode,appTab.typeCode1,"id")
					if(!obj){
						return false;
					}
					if(!obj.active){
						return false;
					}
					return true;
				},
				getTypeName:function(typeCode2){
					var obj = getObjInArr(typeCode2,appTab.typeCode2,"id");
					if(obj){
						return obj.categoryName;
					}else{
						return "";
					}
				},
				formatDate:function(s){
					var dateObj = new Date(s);
					return dateObj.Format("yyyy-MM-dd");
				},
    		filterApp:function(event){
					if(appTab.layoutStatus.showType[0].active){
						getWidgetList({
							filterTxt:$("#filterTxt").val(),
							typeCode1:$("#typeCode1").val(),
							pageNum:1
						});
					}else{
						appTab.layoutStatus.filterTxt = $("#filterTxt").val();
					}
    		},
    		filter:function(obj){
    			if(!appTab.layoutStatus.filterTxt){
    				return true;
    			}
    			if(obj.name.indexOf(appTab.layoutStatus.filterTxt) != -1){
    				return true;
    			}else{
    				return false;
    			}
    		}
      }
    });
    
    //根据参数从一个对象数组中获取对象
		function getObjInArr(param,arr,paramName,returnIndex){
			for(var i=0;i<arr.length;i++){
				if(param==arr[i][paramName]){
					if(returnIndex){
						return [arr[i],i];
					}else{
						return arr[i];
					}
				}
			}
		}
		
		//设置菜单哪些项隐藏，哪些项显示
		function setMenu(arr){
			for(var i=0;i<appTab.contextmenu.item.length;i++){
				appTab.contextmenu.item[i].disabled = true;
			}
			for(var i=0;i<arr.length;i++){
				appTab.contextmenu.item[arr[i]].disabled = false;
			}
		}
		
		//根据id直接获取对应的对象
		function getObjById(id,type){
			if(type!="frame"){
				var list = type=="container"?activeTab.container:activeTab.layer;
				for(var i=0;i<list.length;i++){
					if(list[i].id == id){
						return list[i];
					}
				}
			}else{
				var pId = $("#"+id).parent().parent().attr("id");
				var arr = $("#"+id).parent().parent().hasClass("container")?activeTab.container:activeTab.layer;
				var containerObj = getObjInArr(pId,arr,"id");
				return getObjInArr(id,containerObj.frame,"id");
			}
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
		
		function addTab(){
			appTab.contextmenu.visible = false;
		  layer.prompt({
			  formType: 0,
			  title: '请输入标题',
			}, function(value, index, elem){
			  if(!value){
			  	layer.alert("请输入标题");
			  	return;
			  }
			  if(activeTab){
			  	activeTab.active = false;
			  }
			  appTab.tab.push({
			  	name:value,
			  	id:(new Date()).getTime(),
			  	active: true,
					visible:true,
					width:"100%",
					height:"100%",
			  	grid: 50,
			  	offset: [0,0],
					bg: "${pageContext.request.contextPath}/modelFrame-tab/img/bg/1.jpg",
					cornerSize:51,
					borderWidth:2,
					borderType:'${pageContext.request.contextPath}/modelFrame-tab/img/border/simpleBlue/',
					borderOffset:0,
					container: [],
					layer:[],
					group:[]
			  });
			  activeTab = appTab.tab[appTab.tab.length-1];
			  layer.close(index);
			});
		}
		
		//获取微应用的地址并填写相关信息
    function setUrlByWidgetId(appObj,type,activeFrame,p,index) {
      $.ajax({
        data: {
          widgetId: appObj.id
        },
        url: '${pageContext.request.contextPath}/manager/widget/downloadWidget',
        cache: false,
        success: function(result) {
          if(result.success) {
            var url = "${pageContext.request.contextPath}/widgetFrame/app/" + result.data+'?source=pac&userName='+userName;
            if(type=="add"){
            	if(activeFrame){
            		activeFrame.active = false;
            	}
            	if(p.frame.length==1){
            		p.interval = 5000;
            		p.autoInterval = true;
            		addInterval(p);
            	}
            	p.frame.push({
            		id:(new Date()).getTime(),
            		url:url,
            		type:"wyy",
            		title:appObj.name,
								titleVisible:true,
            		active:true,
								menu:false,
								showMenu:false,
            		useUserData:false,
								appType:appObj.appType,
								appId:appObj.appId
            	});
            }else{
            	activeFrame.id = (new Date()).getTime();
						  activeFrame.url = "${pageContext.request.contextPath}/modelFrame-tab/blank.html";
						  activeFrame.type = "wyy";
						  activeFrame.title = appObj.name;
						  activeFrame.titleVisible = true;
						  activeFrame.menu = false;
						  activeFrame.useUserData = false;
							activeFrame.appType = appObj.appType;
							activeFrame.appId = appObj.appId;
						  setTimeout(function(){
						  	 activeFrame.url = url;
						  },300);
            }
            appTab.layoutStatus.acceptObj = null;
						dragWyy = null;
						if(index){
							layer.close(index);
						}
          } else {
          	layer.alert("加载控件失败");
          	appTab.layoutStatus.acceptObj = null;
						dragWyy = null;
						if(index){
							layer.close(index);
						}
          }
        },
        error: function() {
          layer.alert("加载控件失败");
          appTab.layoutStatus.acceptObj = null;
					dragWyy = null;
					if(index){
						layer.close(index);
					}
        }
      });
    }
    
    function widgetSave(obj,frameId,type,noReload){
    	$.ajax({
        cache: false,
        contentType: "application/json",
        type: 'get',
        dataType: "json",
				data: {
					type:type,
					appId:appId,
					iframeId:frameId,
					json:encodeURI(JSON.stringify(obj))
				},
        url: "${pageContext.request.contextPath}/manager/app/publishIframe",
        success: function(res) { //请求成功后处理函数。
          result = res.result;
          if(res.success) {
            layer.alert("保存成功！",function(){
            	if(!noReload){
								$("#"+frameId)[0].contentWindow.location.reload();
            	}
							layer.closeAll();
						});
          } else {
            layer.alert(res.reason);
          }
        },
        error: function() {
          layer.alert('保存失败');
        }
      });
    }
    
    function addInterval(obj){
    	intervalFlag[obj.id] = setInterval(function(){
				var activeObj = getObjInArr(true,obj.frame,"active",true);
				var index = activeObj[1];
				var activeFrame = activeObj[0];
				activeFrame.active = false;
				var newActiveFrame = index == obj.frame.length-1?obj.frame[0]:obj.frame[index+1];
				newActiveFrame.active = true;
			},obj.interval);
    }
    
    function createIntervalAll(){
    	for(var i=0;i<activeTab.container.length;i++){
    		if(activeTab.container[i].autoInterval){
    			addInterval(activeTab.container[i]);
    		}
    	}
    	for(var i=0;i<activeTab.layer.length;i++){
    		if(activeTab.layer[i].autoInterval){
    			addInterval(activeTab.layer[i]);
    		}
    	}
    }
		
		function getWidgetList(myObj){
			var pageSize = 20;
			if(myObj.filterTxt){
				if(myObj.filterTxt != ""){
					pageSize = 1000;
				}
			}
			//读取微应用
      $.ajax({
        dataType: "json",
        url: "${pageContext.request.contextPath}/manager/widget/getWidgetList",
        data: {
          pageSize: pageSize,
          pageNum: myObj.pageNum,
					typeCode1:myObj.typeCode1
        },
        cache: false,
        success: function(result) {
          if(result.success) {
						var arr = [];
            if(result.page && result.page.list) {
							if(myObj.filterTxt){
								if(myObj.filterTxt != ""){
									appTab.layoutStatus.filterTxt = myObj.filterTxt;
								}else{
									appTab.layoutStatus.filterTxt = "";
								}
							}else{
								appTab.layoutStatus.filterTxt = "";
							}
            	for(var i=0;i<result.page.list.length;i++){
								var appImg = result.page.list[i].appImg != ''?result.page.list[i].appImg:"${pageContext.request.contextPath}/modelFrame-tab/img/appdefault.png";
            		arr.push({
            			id:result.page.list[i].widgetId,
									index:(result.page.pageNum-1)*pageSize + i,
            			name:result.page.list[i].widgetName,
            			widgetInitId:result.page.list[i].widgetInitId,
									createTime:result.page.list[i].createTime,
									typeCode1:result.page.list[i].typeCode1,
									typeCode2:result.page.list[i].typeCode2,
									appImg:appImg,
									appType:"widget",
									appId:result.page.list[i].widgetId
            		});
            	}
							laypage.render({
								elem: 'appListPage' //注意，这里的 test1 是 ID，不用加 # 号
								,count: result.page.total //数据总数，从服务端得到
								,limit:pageSize
								,curr:myObj.pageNum
								,groups:3
								,jump: function(obj, first){
									 //首次不执行
									if(!first){
										getWidgetList({
											typeCode1:$("#typeCode1").val(),
											pageNum:obj.curr
										});
									}
								}
							});
            }
						appTab.wyyList = arr;
						// if(showAll){
							// $("#filterTxt").val("");
							// appTab.layoutStatus.filterTxt="";
						// }
          } else {
						appTab.wyyList = [];
						// if(showAll){
							// $("#filterTxt").val("");
							// appTab.layoutStatus.filterTxt="";
						// }
            layer.alert("获取微应用列表失败！");
          }
        },
        error: function() {
					appTab.wyyList = [];
					// if(showAll){
						// $("#filterTxt").val("");
						// appTab.layoutStatus.filterTxt="";
					// }
          layer.alert("获取微应用列表失败！");
        }
      });
		}
		
		function getAppInfo(obj){
			$.ajax({
        dataType: "json",
        url: "",
        data: {
          
        },
        cache: false,
        success: function(result) {
          if(result.success) {
          	
          }
        },
        error: function() {
					
        }
      });
		}
		
		function countRate(rateObj,containerList){
			for(var j=0;j<containerList.length;j++){
				var frameList = containerList[j].frame;
				for(var k=0;k<frameList.length;k++){
					if(frameList[k].appType && frameList[k].appId){
						if(!rateObj[frameList[k].appType + "_" + frameList[k].appId]){
							rateObj[frameList[k].appType + "_" + frameList[k].appId] = {
								appType: frameList[k].appType,
								appId: frameList[k].appId,
								num: 1
							};
						}else{
							rateObj[frameList[k].appType + "_" + frameList[k].appId].num++;
						}
					}
				}
			}
		}
  </script>
</html>