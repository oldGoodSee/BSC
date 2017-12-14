package com.bocom.ws;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * ClassName:SocketHandler
 * Function: 
 * Date:     2017年8月7日下午4:04:57
 * @author   chenzz
 * @since    JDK 1.7
 */
@Service
public class SocketHandler implements WebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(SocketHandler.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("成功建立socket连接");
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		try {
			System.out.println("Req: " + message.getPayload());
			TextMessage returnMessage = new TextMessage(message.getPayload() + " received at server");
			session.sendMessage(returnMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable error) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		logger.error("连接出现错误:", error);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
		logger.info("连接已关闭");
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}
