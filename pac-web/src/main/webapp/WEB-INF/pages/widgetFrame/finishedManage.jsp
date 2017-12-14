<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

  <head>
    <meta charset="UTF-8">
    <title>微应用管理</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/media/image/favicon.ico" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/css/bootstrap.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/widgetFrame/css/bootstrap-select.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/widgetFrame/css/main_component_manage.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/widgetFrame/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" />
    <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/js/bootstrap-select.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/layer/layer.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/js/laypage/laypage.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <!--[if lt IE 9]>
    <style type="text/css">
        .layui-layer {
            border: 1px solid #ccc;
        }
    </style>
    <script src="${pageContext.request.contextPath}/modelFrame-tab/js/html5shiv.min.js"></script>
    <script src="${pageContext.request.contextPath}/modelFrame-tab/js/respond.min.js"></script>
    <![endif]-->
  </head>

  <body id="body" oncontextmenu="return false;">
    <div class="sidebar" id="sideBar">
			<div style="margin: 10px;">
				<select class="selectpicker" id="type1"></select>
      </div>
			<div style="margin: 10px;">
				<select class="selectpicker" id="type2">
					<option value="">全部</option>
				</select>
			</div>
      <div class="sidebar-content" id="sidebarContent">
        <ul id="microAppList"></ul>
      </div>
      <div class="sidebar-page" id="microAppListPage"></div>
    </div>

    <div class="main-content" id="mainContent">
      <iframe class="layout-iframe" frameborder="0" name="mainFrame" id="mainFrame" src=""></iframe>
    </div>

    <div id="hiddenContainer" style="display:none;"></div>
  </body>
  <script type="text/javascript" src="${pageContext.request.contextPath}/widgetFrame/js/fnList-1.0.js"></script>
  <script type="text/javascript">
		var pageNum = 1;
    function loginCasOut() {
      $.ajax({
        async: false,
        cache: false,
        contentType: "application/json",
        type: 'POST',
        dataType: "json",
        url: "${pageContext.request.contextPath}" + "/manager/loginAction/loginCasOut",
        error: function() {
          alert('请求失败');
        },
        success: function(res) { //请求成功后处理函数。
          if(res.success) {
            window.location.href = "${casServerUrlPrefix}/logout?service=<%=request.getScheme()+": //"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()%>/manager/loginAction/loginCas";
          } else {
            alert('退出失败！');
          }
        }
      });
    }
    $(document).on('ready', function() {
      //获取已发布微应用列表
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
        success: function(res) { //请求成功后处理函数。
          if(res.success) {
						var typeList = res.data
						if(!typeList){
							layer.alert('未获取到控件类型');
							return;
						}
						if(typeList.length==0){
							layer.alert('未获取到控件类型');
							return;
						}
						var innerStr1 = '';
						var innerStr2 = '<option value="">全部</option>';
						for(var i=0;i<typeList.length;i++){
							if(typeList[i].categoryType == "1"){
								innerStr1 += '<option value="'+typeList[i].id+'">'+typeList[i].categoryName+'</option>';
							}else{
								innerStr2 += '<option value="'+typeList[i].id+'">'+typeList[i].categoryName+'</option>';
							}
						}
						$("#type1").html(innerStr1);
						$("#type2").html(innerStr2);
						$('.selectpicker').selectpicker('refresh');
						getPubWidget(pageNum,$("#type1").selectpicker('val'));
          } else {
           layer.alert('获取控件类型失败');
          }
        }
      });
			
			$('#type1').on('hidden.bs.select', function (e) {
				getPubWidget(1,$("#type1").selectpicker('val'),$("#type2").selectpicker('val'));
			});
			
			$("#type2").on("hidden.bs.select",function(){
				getPubWidget(1,$("#type1").selectpicker('val'),$("#type2").selectpicker('val'));
			});
    });

    //获取已组装微应用
    function getPubWidget(curr,type1,type2) {
			var obj = {
   			pageNum: curr,
        pageSize: 20,
        typeCode1:type1
    	};
    	if(type2){
    		obj.typeCode2 = type2;
    	}
      $.ajax({
        dataType: "json",
        //type: "POST",
        //url: "${pageContext.request.contextPath}/widgetFrame/data/microApp1.json",
        url: "${pageContext.request.contextPath}/manager/widget/getWidgetList",
        data: obj,
        cache: false,
        success: function(result) {
          if(result.success) {
            var innerStr = "";
						if(!result.page){
							$("#microAppList").html('<span style="color:#999;">没有数据</span>');
							return;
						}
						if(result.page.list == null){
							$("#microAppList").html('<span style="color:#999;">没有数据</span>');
							return;
						}
            for(var i = 0; i < result.page.list.length; i++) {
              innerStr += '<li><a href="javascript:void(0)"  type="id" widgetId="' + result.page.list[i].widgetId + '" widgetName="' + result.page.list[i].widgetName + '" initId="' + result.page.list[i].widgetInitId + '">' + result.page.list[i].widgetName + '</a><span class="glyphicon glyphicon-remove"></span></li>';
            }
            $("#microAppList").html(innerStr);
            $("#microAppList").find("a").on("click", function() {
              initId = $(this).attr("initId");
              widgetId = $(this).attr("widgetId");
              widgetName = $(this).attr("widgetName");
              innerPage(widgetId, "id",widgetName);
              $("#save").html("保存");

              $("#mainContent").animate({
                right: "0"
              }, 300);
              $("#sidebarR").animate({
                width: "0"
              }, 300);
            });

            $("#microAppList").find(".glyphicon-remove").on("click", function() {
              initId = $(this).prev().attr("initId");
              widgetId = $(this).prev().attr("widgetId");
              checkDelApp();
            });

            pageNum = result.page.pageNum;
            //显示分页
            laypage({
              cont: 'microAppListPage', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
              pages: result.page.pages, //通过后台拿到的总页数
              curr: curr || 1, //当前页
              skin: '#428bca',
              groups: 2,
              first: 1,
              last: result.page.pages,
              prev: '<', //若不显示，设置false即可
              next: '>',
              jump: function(obj, first) { //触发分页后的回调
                if(!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                  getPubWidget(obj.curr,type1,type2);
                }
              }
            });
          } else {
            layer.alert("获取已发布微应用失败。")
          }
        },
        error: function() {
          layer.alert("获取微应用信息失败！");
        }
      });
    }

    //根据左侧选择直接加载iframe或者根据id读取src,并重置editContent
    function innerPage(param, type, jqDom,widgetName) {
      $("#editContent").empty();
      if(type == "id") {
        $.ajax({
          data: {
            widgetId: param
          },
          url: '${pageContext.request.contextPath}/manager/widget/downloadWidget',
          cache: false,
          success: function(result) {
            if(result.success) {
              var now = new Date();
              var url = "${pageContext.request.contextPath}/widgetFrame/app/" + result.data;
              $("#mainContent").find("iframe").attr("src", url + "?_=" + now.getTime());
              $("#editContent").css("visibility", "visible");
              $("#save").css("visibility", "visible");
            } else {
              layer.alert(result.reason);
            }
          },
          error: function() {
            layer.alert("获取微应用路径失败");
          }
        });
      } else {
        $.ajax({
          data: {
            initId: param,
            widgetName: encodeURI(widgetName),
            version: jqDom.attr("version")
          },
          url: '${pageContext.request.contextPath}/manager/widget/initWidget',
          cache: false,
          success: function(result) {
            if(result.success) {
              var url = "${pageContext.request.contextPath}/widgetFrame/" + result.data
              var now = new Date();
              $("#mainContent").find("iframe").attr("src", url + "?_=" + now.getTime());
              $("#editContent").css("visibility", "visible");
              $("#save").css("visibility", "visible");
            } else {
              layer.alert(result.reason);
            }
          },
          error: function() {
            layer.alert("获取控件路径失败");
          }
        });
      }
    }

		//提示是否删除已组装的微应用
    function checkDelApp() {
      layer.confirm('确定要删除该微应用吗？', {
        btn: ['删除', '取消'] //按钮
      }, function(index) {
        layer.close(index);
        $.ajax({
          data: {
            widgetId: widgetId
          },
          url: '${pageContext.request.contextPath}/manager/widget/checkWidget',
          cache: false,
          success: function(result) {
            if(result.success) {
              if(result.check) {
                layer.alert('该微应被' + result.data.length + '个应用调用，无法删除。');
              } else {
                delApp(widgetId);
              }
            } else {
              layer.alert("获取是否该微应用是否被调用信息失败");
            }
          },
          error: function() {
            layer.alert("获取是否该微应用是否被调用信息失败");
          }
        });
      }, function() {

      });
    }

		//删除已组装的微应用
    function delApp() {
      $.ajax({
        data: {
          widgetId: widgetId
        },
        url: '${pageContext.request.contextPath}/manager/widget/deleteWidget',
        cache: false,
        success: function(result) {
          if(result.success) {
            layer.alert("删除成功", function(i) {
              getPubWidget(pageNum);
              layer.close(i);
            });
          } else {
            layer.alert("删除失败");
          }
        },
        error: function() {
          layer.alert("删除失败");
        }
      });
    }
  </script>

</html>