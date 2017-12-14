<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8" />
    <title>404页面</title>
    <style  type="text/css">
        body{
            background-color: #eee;
        }
        .main-container {
            position:absolute;
            left: 50%;
            top: 50%;
            margin-left: -350px;
            margin-top: -180px;
            width: 700px;
            height: 300px;
            background:url(${pageContext.request.contextPath}/pac_gl/images/404.png) 30px center no-repeat #fff;
            border: 5px solid #ccc;
        }
        .info-container{
            margin: 70px 0 0 250px;
            font-family: "微软雅黑";
        }
        .info-container h1{
            margin: 0;
            padding: 0;
            font-size: 64px;
            color: #666;
        }
        .info-container p{
            margin-top: 20px;
            font-size: 16px;
            color: #666;
        }
    </style>
</head>
<body>
<div class="main-container">
    <div class="info-container">
        <h1>404</h1>
        <p>抱歉！你请求的地址不存在，请确定路径正确无误。</p>
    </div>
</div>
</body>
</html>