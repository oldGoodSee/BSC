<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/ztree/css/zTreeStyle/metro.css"/>
    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/ztree/css/zTreeStyle/zTreeStyle.css"/>--%>
    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/ztree/css/awesomeStyle/awesome.css"/>--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <script src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>

    <style type="text/css">
        html, body {
            height: 100%;
            overflow: hidden;
        }

        .top {
            position: absolute;
            left: 0;
            top: 0;
            right: 0;
            height: 200px;
            overflow: auto;
        }

        .orgs {
            position: absolute;
            left: 0;
            top: 210px;
            width: 50%;
            bottom: 0;
            overflow: auto;
            border: 1px solid #ccc;
            border-left: 0 none;
            background-color: #EEEEEE;
        }

        .persons {
            position: absolute;
            left: 50%;
            top: 210px;
            right: 0;
            bottom: 0;
						overflow-y:auto;
        }
        .top .btn,
        .persons .btn{
        	margin: 5px 0 0 10px;
        }
        .top .btn a{
        	color: #fff !important;
        	text-decoration: none;
        }

        .top > fieldset,
        .persons > fieldset {
            margin: 10px 0 0 0;
        }
        
        .top > fieldset {
            width: 50%;
            float:left;
       	}

        .top > fieldset > legend,
        .persons > fieldset > legend {
            padding: 0 0 10px 10px;
            font-size: 18px;
            font-family: "黑体";
            color: #069;
            font-weight: bold;

        }

        /*---- override ----*/
        .ztree * {
            background-color: transparent;
        }

        .ztree {
            color: #444;
            background-color: transparent;
        }

        .ztree li a {
            color: #444;
        }

        .ztree li a.curSelectedNode {
            background-color: #F7E1B5;
            color: #000;
            border-color: #F0AD4E;
        }

        .cross {
            background: url(${pageContext.request.contextPath}/image/cross.png) center center no-repeat #666;
        }
    </style>

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/ztree/js/jquery.ztree.all-3.5.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/layer/layer.js"></script>
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/js/html5shiv.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div style="position: absolute; z-index: 100; right: 20px; top: 10px; width:100px; height: 40px;">
    <c:if test="${empty param.source}">
        <button class="btn btn-default" type="button" id="back">返回</button>
    </c:if>
</div>
<div class="top">
    <fieldset id="">
        <legend>已选择人员</legend>
        <div id="personSeled"></div>
    </fieldset>
    <fieldset id="">
        <legend>已选择部门</legend>
        <div id="orgSeled"></div>
    </fieldset>
</div>
<div class="orgs">
    <ul class="ztree" id="orgs"></ul>
</div>
<div class="persons">
    <fieldset>
        <legend>人员列表</legend>
        <div id="persons"></div>
    </fieldset>
</div>
</body>
<script type="text/javascript">
    //已选中人员
    var seledPerson = [];
    var seledPersonId = [];
    //以选中机构
    var seledOrg;

    var appId = '${appId}';
    var zTree;
    var treeNodes;
    
    var firstLoad = true;
    
    $(document).on("ready", function () {
    		
        $("#back").on("click", function () {
            window.parent.menuClick("appView");
        });
        
	      $("#personSeled").on("click", ".btn span", function () {
	          var seled = $(this).parent();
	          delPerson(seled);
	      });
	      
	      $("#orgSeled").on("click", ".btn span",function () {
	          var seled = $(this).parent();
	          delOrg(seled);
	      });
	      
	      $("#persons").on("click", ".btn",function () {
	          selPerson($(this));
	      });
        
        var setting = {
            data: {
                simpleData: {
                    enable: true,
                    idKey: "organizationCode",
                    pIdKey: "parentOrganizationCode",
                    rootPId: '0'
                }
            },
            check: {
							enable: true,
							chkStyle: "checkbox",
							chkboxType: { "Y": "", "N": "" }
						},
            callback: {
                onClick: getPersonByOrg,
                onCheck: function (event, treeId, treeNode) {
                	if(!firstLoad){
                		setOrgPermission(treeNode);
                	}
                }
            }
        };

        //获取已选中人员
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            //你这面也许要post，我看原来代码是post
            type: 'get',
            dataType: "json",
            url: "${pageContext.request.contextPath}/manager/app/getPersonSeled",//请求的action路径
            data: {
                appId: appId
            },
            error: function () {//请求失败处理函数
                layer.alert('获取已选中人员信息失败');
            },
            success: function (res) { //请求成功后处理函数。
                var nodeData = res.data;
                for (var i = 0; i < nodeData.length; i++) {
                    seledPerson.push({
                        screenName: nodeData[i].screenName,
                        userName: nodeData[i].userName,
                        userId: nodeData[i].userId
                    });
                    seledPersonId.push(nodeData[i].userId);
                }
                addSeledPerson();
            }
        });
        
        //获取已选中部门
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            //你这面也许要post，我看原来代码是post
            type: 'get',
            dataType: "json",
            url: "${pageContext.request.contextPath}/manager/app/getOrgSeled",//请求的action路径
            data: {
                appId: appId
            },
            error: function () {//请求失败处理函数
                layer.alert('获取已授权部门信息失败');
            },
            success: function (res) { //请求成功后处理函数。
            		seledOrg = res.data;
              	if(zTree){
              		for(var i=0;i<res.data.length;i++){
              			var node = zTree.getNodesByParam("organizationCode", res.data[i].orgId, null)[0];
              			zTree.checkNode(node, true, true);
              			seledOrg[i].name = node.name;
              		}
              		addSeledOrg();
              		firstLoad = false;
              	}
            }
        });
				
				//获取所有部门
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            //你这面也许要post，我看原来代码是post
            type: 'get',
            dataType: "json",
            url: "${pageContext.request.contextPath}/manager/app/getAllOrg",//请求的action路径
            error: function () {//请求失败处理函数
                layer.alert('获取部门信息失败');
            },
            success: function (res) { //请求成功后处理函数。
                var nodeData = JSON.parse(res.data);
                if (nodeData.success) {
                		treeNodes = nodeData.data;   //把后台封装好的简单Json格式赋给treeNodes
                    $.fn.zTree.init($("#orgs"), setting, treeNodes).expandAll(false);
                    zTree = $.fn.zTree.getZTreeObj("orgs");
                    if(seledOrg){
                    	for(var i=0;i<seledOrg.length;i++){
	                			var node = zTree.getNodesByParam("organizationCode", seledOrg[i].orgId, null)[0];
	                			zTree.checkNode(node, true, true);
	                			seledOrg[i].name = node.name;
	                		}
                    	addSeledOrg();
                			firstLoad = false;
                    }
                } else {
                    layer.alert('获取部门信息失败');
                }
            }
        });
    });
		
		//点击节点获取该节点下的人
    function getPersonByOrg(event, treeId, treeNode) {
        $.ajax({
            async: false,
            cache: false,
            contentType: "application/json",
            //你这面也许要post，我看原来代码是post
            type: 'get',
            dataType: "json",
            url: "${pageContext.request.contextPath}/manager/app/getPersonByOrg",//请求的action路径
            data: {
                organizationCode: treeNode.organizationCode
            },
            error: function () {//请求失败处理函数
                alert('获取人员信息失败');
            },
            success: function (res) { //请求成功后处理函数。
                var nodeData = JSON.parse(res.data);
                if (nodeData.success) {
                    addPersonsByNode(nodeData.data);
                } else {
                    alert('获取人员信息失败');
                }
            }
        });
    }
		
		//根据点击树节点的返回值展示该节点下的人
    function addPersonsByNode(json) {
        if (!json) {
            $("#persons").empty();
            return;
        }
        if (json.length == 0) {
            $("#persons").empty();
            return;
        }
        var innerStr = "";
        for(var i=0;i<json.length;i++){
        	var cls = $.inArray(json[i].userId+"", seledPersonId) != -1?"btn-success":"btn-default";  
          innerStr += '<button class="btn '+cls+'" id="btn_'+json[i].userId+'" screenName="' + json[i].screenName + '">' + json[i].firstName +'</button>';
        }
        $("#persons").html(innerStr);
    }
		
		//初始化填写选择的用户
    function addSeledPerson(json) {
        var innerStr = "";
        for (var i = 0; i < seledPerson.length; i++) {
            innerStr += '<a class="btn btn-primary" id="person_'+seledPerson[i].userId+'" screenName="' + seledPerson[i].screenName + '">' + 
								            seledPerson[i].userName +
								            '<span class="glyphicon glyphicon-remove"></span>'+
								        '</a>';
        }
        $("#personSeled").append(innerStr);
    }
    
    //初始化填写选择的部门
    function addSeledOrg() {
        var innerStr = "";
        for (var i = 0; i < seledOrg.length; i++) {
            innerStr += '<a class="btn btn-primary" id="org_'+seledOrg[i].orgId+'">' + 
								            seledOrg[i].name +
								            '<span class="glyphicon glyphicon-remove"></span>'+
								        '</a>';
        }
        $("#orgSeled").append(innerStr);
    }
    
    //通过点击treeNode来改变部门选择
    function setOrgPermission(treeNode){
    	var nodeId = treeNode.organizationCode;
    	var nodeName = treeNode.name;
    	if(treeNode.checked){
    		$.ajax({
          async: false,
          cache: false,
          contentType: "application/json",
          //你这面也许要post，我看原来代码是post
          type: 'get',
          dataType: "json",
          url: "${pageContext.request.contextPath}/manager/app/addRightOrg",//请求的action路径
          data: {
              appId: appId,
              orgId: nodeId
          },
          error: function () {//请求失败处理函数
              layer.alert('授权失败');
              zTree.checkNode(treeNode, false, false);
          },
          success: function (res) { //请求成功后处理函数。
              if (res.success) {
                  var innerStr = '<a class="btn btn-primary" id="org_'+nodeId+'">' + 
													            nodeName +
													            '<span class="glyphicon glyphicon-remove"></span>'+
													        '</a>';
    							$("#orgSeled").append(innerStr);
              } else {
                  layer.alert('授权失败');
                  zTree.checkNode(treeNode, false, false);
              }
          }
      	});
    	}else{
    		$.ajax({
          async: false,
          cache: false,
          contentType: "application/json",
          //你这面也许要post，我看原来代码是post
          type: 'get',
          dataType: "json",
          url: "${pageContext.request.contextPath}/manager/app/delAppOrg",//请求的action路径
          data: {
              appId: appId,
              orgId: nodeId
          },
          error: function () {//请求失败处理函数
              layer.alert('授权失败');
              zTree.checkNode(treeNode, true, false);
          },
          success: function (res) { //请求成功后处理函数。
              if (res.success) {
                  var btns = $("#orgSeled").find(".btn");
					    		for(var i = 0;i<btns.length;i++){
					    			if(btns.eq(i).attr("id") == "org_"+nodeId){
					    				btns.eq(i).remove();
					    				break;
					    			}
					    		}
              } else {
                  layer.alert('授权失败');
                  zTree.checkNode(treeNode, true, false);
              }
          }
      	});
    	}
    }
    
    //根据右下角的人员按钮添加或删除人员授权
    function selPerson(btn){
    	//删除
    	if(btn.hasClass("btn-success")){
    		var id = btn.attr("id").split("_")[1];
    		var seledBtn = $("#person_"+id);
    		var index = seledBtn.index();
    		var removeItem = seledPerson.splice(index,1);
    		$.ajax({
          async: false,
          cache: false,
          contentType: "application/json",
          //你这面也许要post，我看原来代码是post
          type: 'get',
          dataType: "json",
          url: "${pageContext.request.contextPath}/manager/app/addRight",//请求的action路径
          data: {
              appId: appId,
              users: encodeURI(JSON.stringify(seledPerson))
          },
          error: function () {//请求失败处理函数
              seledPerson.splice(index, 0, removeItem);
              alert('获取人员信息失败');
          },
          success: function (res) { //请求成功后处理函数。
              if (res.success) {
									btn.removeClass("btn-success").addClass("btn-default");
                  seledBtn.remove();
              } else {
									seledPerson.splice(index, 0, removeItem);
                  alert('获取人员信息失败');
              }
          }
      	});
    	}else{	//增加
    		var id = btn.attr("id").split("_")[1];
    		var text = btn.html();
    		seledPerson.push({
            screenName: btn.attr("screenName"),
            userName: btn.html(),
            userId: id
       });
    		$.ajax({
          async: false,
          cache: false,
          contentType: "application/json",
          //你这面也许要post，我看原来代码是post
          type: 'get',
          dataType: "json",
          url: "${pageContext.request.contextPath}/manager/app/addRight",//请求的action路径
          data: {
              appId: appId,
              users: encodeURI(JSON.stringify(seledPerson))
          },
          error: function () {//请求失败处理函数
              seledPerson.pop();
              alert('获取人员信息失败');
          },
          success: function (res) { //请求成功后处理函数。
              if (res.success) {
									btn.removeClass("btn-default").addClass("btn-success");
                  var innerStr = '<a class="btn btn-primary" id="person_'+id+'">' + 
													            text +
													            '<span class="glyphicon glyphicon-remove"></span>'+
													        '</a>';
    							$("#personSeled").append(innerStr);
    							seledPersonId.push(id);
              } else {
									seledPerson.pop();
                  alert('获取人员信息失败');
              }
          }
      	});
    	}
    }
    
    //点击上方的按钮上的关闭按钮关闭取消人员授权
    function delPerson(seled){
    	var id = seled.attr("id").split("_")[1];
    	var index = seled.index();
  		var removeItem = seledPerson.splice(index,1);
  		$.ajax({
          async: false,
          cache: false,
          contentType: "application/json",
          //你这面也许要post，我看原来代码是post
          type: 'get',
          dataType: "json",
          url: "${pageContext.request.contextPath}/manager/app/addRight",//请求的action路径
          data: {
              appId: appId,
              users: encodeURI(JSON.stringify(seledPerson))
          },
          error: function () {//请求失败处理函数
              seledPerson.splice(index, 0, removeItem);
              alert('获取人员信息失败');
          },
          success: function (res) { //请求成功后处理函数。
              if (res.success) {
              		//右下角人员列表中有该用户，联动
              		if($("#btn_"+id).length >0){
  										$("#btn_"+id).removeClass("btn-success").addClass("btn-default");
    							}
									seledPersonId.splice(index,1);
                  seled.remove();
              } else {
									seledPerson.splice(index, 0, removeItem);
                  alert('获取人员信息失败');
              }
          }
      });
    }
    
    //点击上方的按钮上的关闭按钮关闭取消人员授权
    function delOrg(seled){
    	var id = seled.attr("id").split("_")[1];
    	var index = seled.index();
  		var removeItem = seledOrg.splice(index,1);
  		$.ajax({
          async: false,
          cache: false,
          contentType: "application/json",
          //你这面也许要post，我看原来代码是post
          type: 'get',
          dataType: "json",
          url: "${pageContext.request.contextPath}/manager/app/delAppOrg",//请求的action路径
          data: {
              appId: appId,
              orgId: id
          },
          error: function () {//请求失败处理函数
              layer.alert('授权失败');
          },
          success: function (res) { //请求成功后处理函数。
              if (res.success) {
              		var node = zTree.getNodesByParam("organizationCode", id, null)[0];
              		zTree.checkNode(node, false, false);
                  seled.remove();
              } else {
                  layer.alert('授权失败');
              }
          }
      	});
    }
</script>
</html>
