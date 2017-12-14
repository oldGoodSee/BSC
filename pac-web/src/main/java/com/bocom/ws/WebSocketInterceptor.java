package com.bocom.ws;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * ClassName:WebSocketInterceptor
 * Function: 
 * Date:     2017年8月7日下午4:04:05
 * @author   chenzz
 * @since    JDK 1.7
 */
public class WebSocketInterceptor implements HandshakeInterceptor {

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
		System.out.println("After handshake " + request.getRemoteAddress().toString());
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Map<String, Object> map) throws Exception {
		System.out.println("Before handshake " + request.getRemoteAddress().toString());
		return true;
	}

}
