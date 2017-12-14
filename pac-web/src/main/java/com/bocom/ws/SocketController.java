package com.bocom.ws;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.TextMessage;

/**
 * ClassName:SocketController
 * Function: 
 * Date:     2017年8月7日下午4:05:49
 * @author   chenzz
 * @since    JDK 1.7
 */
@Controller
@RequestMapping("/ws")
public class SocketController {

	private static final Logger logger = LoggerFactory.getLogger(SocketController.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");

	@Autowired
	private SocketHandler socketHandler;

	@RequestMapping(value = "/login")
	public String login(HttpSession session) {
		logger.info("用户登录了建立连接啦");

		session.setAttribute("user", "liulichao");

		return "home";
	}

	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public String sendMessage() {

		return "message";
	}

}
