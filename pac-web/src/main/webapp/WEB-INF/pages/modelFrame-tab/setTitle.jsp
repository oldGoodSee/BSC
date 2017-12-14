<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String frameId = request.getParameter("frameId");
%>
<!DOCTYPE html>
<html>

  <head>
    <meta charset="UTF-8">
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/modelFrame-tab/js/layui/css/layui.css" />
    <style type="text/css">
    	input[disabled]{
    		color: #ccc;
    	}
    	.img-list{
    		max-height: 120px;
    		margin:0 0 20px 30px;
    	}
    	.img-list a{
    		display: inline-block;
    		margin-left:2px;
    	}
    	.img-list a img{
    		width:120px;
    		height:80px;
    	}
    	.img-list a:link img,
    	.img-list a:visited img{
    		border:4px solid #eee;
    	}
    	.img-list a:hover img,
    	.img-list a:active img{
    		border:4px solid #ff9900;
    	}
    	.img-list>.active:link>img,
    	.img-list>.active:visited>img{
    		border:4px solid #ff9900;
    	}
    </style>
    <script type="text/javascript" src="${pageContext.request.contextPath}/modelFrame-tab/js/layui/layui.js" ></script>
  </head>

  <body>
  	<div style="margin:15px 15px 0 0;">
	    <form class="layui-form" action="">
	      <div class="layui-form-item">
			    <div class="layui-inline">
			      <label class="layui-form-label">标题名称</label>
			      <div class="layui-input-inline">
			        <input type="text" name="title" id="title" autocomplete="off" class="layui-input">
			      </div>
			    </div>
	      </div>
	      <div class="layui-form-item">
					<label class="layui-form-label">可见性</label>
					<div class="layui-input-block">
						<input type="checkbox" name="titleVisible" id="titleVisible" lay-skin="switch" lay-text="显示|隐藏" checked>
					</div>
				</div>
	      <div class="layui-form-item">
			    <div class="layui-input-block">
			      <button class="layui-btn" type="button" id="submit">确 认</button>
			      <button class="layui-btn layui-btn-primary" type="button" id="cancel">取 消</button>
			    </div>
			  </div>
	    </form>
    </div>
  </body>
	<script type="text/javascript">
		var p = window.parent;
		var frameId = "<%=frameId%>";
		var frameObj = p.getObjById(frameId,"frame");
		//Demo
		layui.use(['form','jquery'], function(){
		  var form = layui.form;
		  var $ = layui.jquery;
			if(!frameObj.titleVisible){
				$("#titleVisible").prop("checked",false);
			}
			$("#title").val(frameObj.title);
		  
		  form.render();
			
			form.on('switch', function(data){
				if(data.elem.checked){
					$("#title").prop("disabled",false);
				}else{
					$("#title").prop("disabled",true);
				}
				form.render();
			});  
			
			//监听提交
		  $("#submit").on('click', function(data){
				frameObj.title = $("#title").val();
				frameObj.titleVisible = $("#titleVisible").prop("checked");
		    p.layer.closeAll();
		  });
		  
		  $("#cancel").on("click",function(){
				p.layer.closeAll();
			});
		});
	</script>
</html>