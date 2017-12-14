<%@page import="com.bocom.dto.session.UserRoleInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.bocom.dto.session.SessionUserInfo"%>
<%@page import="org.springframework.util.CollectionUtils"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title>你没有访问此内容的权限</title>
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="你没有访问此内容的权限" name="description" />
	<meta content="博康" name="author" />
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/media/image/favicon.ico" />
	<script src="${pageContext.request.contextPath}/pac_gl/js/jquery-1.9.1.min.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/media/js/app.js" type="text/javascript"></script>
	<style type="text/css">
		.img-container{
			position:absolute;
			left: 50%;
			top:50%;
			margin-left: -182px;
			margin-top: -300px;
			text-align: center;
			color: #555;
		}
		.img-container img{
			display: inline-block;
			margin: 0 0 20px 23px;
		}
	</style>
</head>
<body>
<div class="img-container">
	<img src="${pageContext.request.contextPath}/pac_gl/images/403.png" /><br />
	403:你没有访问此内容的权限&nbsp;&nbsp;<a href="javascript:void(0)" id="back">返回登录</a>
</div>
</body>
<script type="text/javascript">
    $(document).ready(function() {
        $("#back").on("click",function(){
            loginCasOut();
        });
        function loginCasOut(){
            $.ajax({
                async : false,
                cache:false,
                contentType: "application/json" ,
                type: 'POST',
                dataType : "json",
                url: "${pageContext.request.contextPath}"+"/manager/loginAction/loginCasOut",
                error: function () {
                    alert('请求失败');
                },
                success:function(res){ //请求成功后处理函数。
                    if(res.success){
                        if(window.parent){
                            window.parent.location.href="${casServerUrlPrefix}/logout?service=<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()%>/manager/loginAction/loginCas";
                        }else{
                            window.location.href="${casServerUrlPrefix}/logout?service=<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()%>/manager/loginAction/loginCas";
                        }
                    }else{
                        alert('退出失败！');
                    }
                }
            });
        }
    });
</script>
</html>