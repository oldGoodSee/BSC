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
  	<div style="margin:15px 15px 0 0;">
	    <form class="layui-form" action="">
	      <div class="layui-form-item">
	      	<label class="layui-form-label">tab可见性</label>
  				<div class="layui-input-block" id="tabList"></div>
	      </div>
	      <div class="layui-form-item">
			    <div class="layui-input-block">
			      <button class="layui-btn" lay-submit lay-filter="formDemo">确 认</button>
			      <button class="layui-btn layui-btn-primary" type="button" id="cancel">取 消</button>
			    </div>
			  </div>
	    </form>
    </div>
  </body>
	<script type="text/javascript">
		var p = window.parent;
		var tabs = window.parent.appTab.tab;
		//Demo
		layui.use(['form','jquery'], function(){
		  var form = layui.form;
		  var $ = layui.jquery;
		  var str = "";
		  for(var i=0;i<tabs.length;i++){
		  	var checked = tabs[i].visible?"checked":"";
		  	str +=  '<input type="checkbox" name="'+tabs[i].id+'" id="'+tabs[i].id+'" title="'+tabs[i].name+'" '+checked+'>';
			  $("#tabList").html(str);
			  form.render("checkbox");
		  }
			
			//监听提交
		  form.on('submit(formDemo)', function(data){
//		    console.log(data.field)
				var flag = false;
				for(var i=0;i<tabs.length;i++){
					tabs[i].visible = inArr(tabs[i].id,data.field);
					if(!tabs[i].visible && tabs[i].active){
						tabs[i].active = false;
						flag = true;
					}
				}
				if(flag){
					for(var i=0;i<tabs.length;i++){
						if(tabs[i].visible){
							tabs[i].active = true;
							p.activeTab = tabs[i];
							break;
						}
					}
				}
				p.layer.closeAll();
		    return false;
		  });
			
			$("#cancel").on("click",function(){
				p.layer.closeAll();
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