<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String wsPath = "ws://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
  <title>Home</title>
</head>
<body>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<script type="text/javascript" src="https://cdn.bootcss.com/sockjs-client/1.1.4/sockjs.min.js"></script>


<!-- <script type="text/javascript">
$(function(){
	//建立socket连接
	var sock;
	if ('WebSocket' in window) {
		sock = new WebSocket("ws://local.pc.com:9001/pac-web/socketServer");
	} else if ('MozWebSocket' in window) {
		sock = new MozWebSocket("ws://local.pc.com:9001/pac-web/socketServer");
	} else {
		sock = new SockJS("ws://local.pc.com:9001/pac-web/sockjs/socketServer");
	}
	sock.onopen = function (e) {
		console.log(e);
		sock.send("Text1!!!!!")
	};
	sock.onmessage = function (e) {
		console.log(e)
		$("#message").append("<p><font color='red'>"+e.data+"</font>");
		sock.send("client send message:"+e.data)
	};
	sock.onerror = function (e) {
		console.log(e);
	};
	sock.onclose = function (e) {
		console.log(e);
	}
});
</script> -->
<script type="text/javascript">
	var url = "local.pc.com:9001/pac-web";
	var websocket = null;
	if ('WebSocket' in window) {
		websocket = new WebSocket("ws://" + url + "/socketServer");
	} else {
		websocket = new SockJS("http://" + url + "/sockjs/socketServer");
	}
	websocket.onopen = onOpen;
	websocket.onmessage = onMessage;
	websocket.onerror = onError;
	websocket.onclose = onClose;

	function onOpen(openEvent) {
		document.getElementById("console").innerHTML = document.getElementById("console").innerHTML+ "OPEN<br/>";
	}

	function onMessage(event) {
		document.getElementById("console").innerHTML = document.getElementById("console").innerHTML+ event.data+"<br/>";
	}
	function onError() {
	}
	function onClose() {
		document.getElementById("console").innerHTML = document.getElementById("console").innerHTML+ "CLOSE<br/>";
	}

	function doSend() {
		if (websocket !=null && SockJS.OPEN == websocket.readyState) {
			var msg = document.getElementById("message").value;
			websocket.send(msg);
		} else {
			document.getElementById("console").innerHTML=document.getElementById("console").innerHTML+"当前连接失败!<br/>";
		}
	}
	
	
	function disconnect(){
		if (websocket != null) {
			websocket.close();
			websocket = null;
        }
	}
	
	function reconnect(){
		if (websocket != null) {
			websocket.close();
			websocket = null;
        }
		if ('WebSocket' in window) {
			websocket = new WebSocket("ws://" + url + "/socketServer");
		} else {
			websocket = new SockJS("http://" + url + "/sockjs/socketServer");
		}
		websocket.onopen = onOpen;
		websocket.onmessage = onMessage;
		websocket.onerror = onError;
		websocket.onclose = onClose;
	}
</script>
</head>
<body>
	<div>
		<button id="disconnect" onclick="disconnect()">断开连接</button>
		<button id="send" onclick="doSend()">发送消息</button>
		<button id="reconnect" onclick="reconnect()">重新连接</button>
	</div>
    <div>
       <textarea id="message" style="width: 350px">Here is a message!</textarea>
    </div>
    <div>日志信息：</div>
	<p id="console" width="600px"></p>
</body>
</body>
</html>