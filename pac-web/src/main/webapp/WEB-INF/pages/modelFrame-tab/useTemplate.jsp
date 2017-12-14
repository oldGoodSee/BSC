<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

  <head>
    <meta charset="UTF-8">
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/js/layui/css/layui.css" />
    <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/layui/layui.js" ></script>
  </head>

  <body>
  	<div style="margin:15px 15px 0 0;" id="list"></div>
  </body>
	<script type="text/javascript">
		var p = window.parent;
		var tabs = window.parent.appTab.tab;
		//Demo
		layui.use(['form','jquery'], function(){
		  var form = layui.form;
		  var $ = layui.jquery;
		  
		  $.ajax({
        dataType: "json",
        url: "${pageContext.request.contextPath}/manager/history/getAppHistory",
        cache: false,
        success: function(result) {
          if(result.result == "success") {
            histroyObj = result.data;
            var innerStr = "";
            for(var i = 0; i < histroyObj.length; i++) {
            	if("2.0" == histroyObj[i].version){
            		innerStr += '<button type="button" style="margin:5px;" class="layui-btn layui-btn-primary">' + histroyObj[i].appName + '</button>';
            	}
            }
            $("#list").html(innerStr);
            //选择历史模板
            $("#list").find("button").on("click", function() {
              $.ajax({
				        async: false,
				        cache: false,
				        contentType: "application/json",
				        type: 'GET',
				        dataType: "json",
				        data: {
				          appId: $(this).html(),
									newAppId:p.appId
				        },
				        url: p.baseUrl+"/manager/history/copyHistoryRecord",
				        success: function(res) { //请求成功后处理函数。
				          if(res.result == "success") {
				            $.ajax({
				              dataType: "json",
				              //type: "POST",
				              url: "${pageContext.request.contextPath}/modelFrame-tab/configs_saveas/"+res.data+"-configs_txt",
				              cache: false,
				              success: function(result) {
				                p.appTab.tab = result;
				                p.activeTab = p.getObjInArr(true,result,"active");
				              },
											error:function(){
												p.layer.alert("读取历史模板失败");
											}
				            });
				          }else{
										p.layer.alert("获取历史模板路径失败");
									}
				        },
								error:function(){
									p.layer.alert("获取历史模板路径失败");
								}
				      });
            });
          } else {
            p.layer.alert("获取模板失败");
          }
        },
        error: function(jqXHR, textStatus, errorThrown) {
          p.layer.alert("获取模板列表失败");
        }
      });
		});
		
		//根据参数从一个对象数组中获取对象
		function inArr(param,arr){
			for(var i in arr){
				if(param==i){
					return true;
				}
			}
			return false;
		}
	</script>
</html>