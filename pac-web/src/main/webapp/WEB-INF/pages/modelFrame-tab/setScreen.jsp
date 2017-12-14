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
			      <label class="layui-form-label">大屏宽度</label>
			      <div class="layui-input-inline">
			        <input type="number" name="width" id="width" autocomplete="off" class="layui-input">
			      </div>
			    </div>
	        <div class="layui-inline">
			      <label class="layui-form-label">大屏高度</label>
			      <div class="layui-input-inline">
			        <input type="number" name="height" id="height" autocomplete="off" class="layui-input">
			      </div>
			    </div>
	      </div>
	      <div class="layui-form-item">
      	  <div class="layui-input-block">
  					<input type="checkbox" name="auto" id="auto" title="自动">
  				</div>
	      </div>
	      <hr />
	      <div class="layui-form-item">
	        <div class="layui-inline">
			      <label class="layui-form-label">水平偏移</label>
			      <div class="layui-input-inline">
			        <input type="number" name="offsetX" id="offsetX" autocomplete="off" class="layui-input">
			      </div>
			    </div>
			    <div class="layui-inline">
			      <label class="layui-form-label">垂直偏移</label>
			      <div class="layui-input-inline">
			        <input type="number" name="offsetY" id="offsetY" autocomplete="off" class="layui-input">
			      </div>
			    </div>
	      </div>
	      <div class="layui-form-item">
			    <label class="layui-form-label">网格尺寸</label>
			    <div class="layui-input-block">
			      <select name="grid" id="grid">
			        <option value="100">100</option>
			        <option value="50">50</option>
			        <option value="20">20</option>
			      </select>
			    </div>
			  </div>
	      <div class="layui-form-item">
	        <label class="layui-form-label">背景图片</label>
	      	<div id="imgList" class="img-list layui-input-block"></div>
	      </div>
				<div class="layui-form-item">
	        <label class="layui-form-label">边框</label>
	      	<div id="borderList" class="img-list layui-input-block"></div>
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
		var obj = window.parent.activeTab;
		p.document.getElementById("bgGhost0").style.backgroundImage = "url("+obj.bg+")";
		p.document.getElementById("bgGhost0").style.display = "block";
		
		var w = obj.width == "100%"?p.document.getElementById("body").offsetWidth:parseInt(obj.width);
		var h = obj.height == "100%"?p.document.getElementById("body").offsetHeight:parseInt(obj.height);
		var borderList;
		//Demo
		layui.use(['form','jquery','laytpl'], function(){
		  var form = layui.form;
		  var $ = layui.jquery;
		  
		  $("#width").val(w);
		  $("#height").val(h);
		  $("#offsetX").val(obj.offset[0]);
		  $("#offsetY").val(obj.offset[1]);
		  $("#grid").val(obj.grid);
		  form.render("select");
		  if(obj.width == "100%" && obj.height == "100%"){
		  	$("#auto").prop("checked",true);
		  	form.render("checkbox");
		  	$("#width").prop("disabled",true);
		  	$("#height").prop("disabled",true);
		  }
		  
		  $.ajax({
				type:"get",
				url:"${pageContext.request.contextPath}/modelFrame-tab/img/bg/config.json",
				dataType:"json",
				async:false,
				success:function(result){
					var arr = [];
					for(var i=0;i<result.length;i++){
						var cls = obj.bg.indexOf(result[i].url) != -1 ?"active":"";
						arr.push('<a href="javascript:void(0)" class="'+cls+'" title="'+result[i].title+'"><img src="${pageContext.request.contextPath}/modelFrame-tab/img/bg/'+result[i].url+'" /></a>');
					}
					$("#imgList").html(arr.join(''));
					$("#imgList").find("a").on("click",function(){
						if($(this).hasClass("active")){
							return;
						}
						$("#imgList").find(".active").removeClass("active");
						$(this).addClass("active");
						var src = $(this).find("img").attr("src");
						
						p.$("#bgGhost1").css({
							backgroundImage:"url("+src+")"
						}).fadeIn(300);
						setTimeout(function(){
							p.$("#bgGhost0").css({
								backgroundImage:"url("+src+")"
							});
						},300);
						setTimeout(function(){
							p.$("#bgGhost1").hide();
						},350);
					});
				},
				error: function(jqXHR, textStatus, errorThrown) {
					if(jqXHR.status == 404) {
						//$("#contentCenter").show();
						//addTab("页面1");
					}
					p.layer.alert("获取背景图片列表失败！");
				}
			});
			
			$.ajax({
				type:"get",
				url:"${pageContext.request.contextPath}/modelFrame-tab/img/border/config.json",
				dataType:"json",
				async:false,
				success:function(result){
					var arr = [];
					borderList = result;
					for(var i=0;i<result.length;i++){
						var list = obj.borderType.split("/");
						var cls = list[list.length-2] == result[i].borderType ?"active":"";
						arr.push('<a href="javascript:void(0)" class="'+cls+'" title="'+result[i].title+'"><img src="${pageContext.request.contextPath}/modelFrame-tab/img/border/'+result[i].borderType+'/img.png" /></a>');
					}
					$("#borderList").html(arr.join(''));
					$("#borderList").find("a").on("click",function(){
						if($(this).hasClass("active")){
							return;
						}
						$("#borderList").find(".active").removeClass("active");
						$(this).addClass("active");
					});
				},
				error: function(jqXHR, textStatus, errorThrown) {
					if(jqXHR.status == 404) {
						//$("#contentCenter").show();
						//addTab("页面1");
					}
					p.layer.alert("获取边框列表失败！");
				}
			});
		  
		  form.on('checkbox', function(data){
			  if(data.elem.checked){
			  	$("#width").prop("disabled",true).val(p.document.getElementById("body").offsetWidth);
		  		$("#height").prop("disabled",true).val(p.document.getElementById("body").offsetHeight);
			  }else{
			  	$("#width").prop("disabled",false);
		  		$("#height").prop("disabled",false);
			  }
			});
			
			//监听提交
		  form.on('submit(formDemo)', function(data){
//		    layer.msg(JSON.stringify(data.field));
		    if(data.field.auto == "on"){
		    	obj.width = "100%";
		    	obj.height = "100%";
		    }else{
		    	obj.width = $("#width").val()+'px';
		    	obj.height = $("#height").val()+'px';
		    }
		    obj.offset = [parseInt($("#offsetX").val()),parseInt($("#offsetY").val())];
		    obj.grid = parseInt($("#grid").val());
		    var activeImg = $("#imgList").find(".active").eq(0).find("img");
		    obj.bg = activeImg.attr("src");
				var index = $("#borderList").find(".active").eq(0).index();
				obj.borderWidth = borderList[index].borderWidth;
				obj.borderOffset = borderList[index].borderOffset;
				obj.borderType = "${pageContext.request.contextPath}/modelFrame-tab/img/border/"+borderList[index].borderType+"/";
				obj.cornerSize = borderList[index].cornerSize;
		    p.$("#bgGhost0").hide();
		    p.$("#bgGhost1").hide();
		    p.layer.closeAll();
		    return false;
		  });
		  
		  $("#cancel").on("click",function(){
				p.layer.closeAll();
			});
		});
	</script>
</html>