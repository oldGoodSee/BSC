package com.bocom.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * ClassName:WebSocketConfig
 * Function: 
 * Date:     2017年8月7日下午4:01:27
 * @author   chenzz
 * @since    JDK 1.7
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// 注册websocket实现类，指定参数访问地址;allowed-origins="*" 允许跨域
		registry.addHandler(socketHandler(), "/socketServer").addInterceptors(webSocketInterceptor()).setAllowedOrigins("*");

		// 注册SockJs的处理拦截器,拦截url为/sockjs/socketServer的请求
		registry.addHandler(socketHandler(), "/sockjs/socketServer").addInterceptors(webSocketInterceptor()).withSockJS();
	}

	@Bean
	public SocketHandler socketHandler() {
		return new SocketHandler();
	}

	@Bean
	public WebSocketInterceptor webSocketInterceptor() {
		return new WebSocketInterceptor();
	}

}
