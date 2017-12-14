/**
 * Project Name:pwc-web 
 * File Name:LoginRestController.java 
 * Package Name:com.bocom.controller.rest 
 * Date:2016年7月20日上午9:29:13 
 * Copyright (c) 2016, gaozhaosheng@bocom.cn All Rights Reserved. 
 * 
 */
package com.bocom.controller.rest;

import com.bocom.util.ResponseVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/** 
 * ClassName: LoginRestController <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2016年7月20日 上午9:29:13 <br/> 
 * 
 * @author gaozhaosheng
 * @version  
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/manager/loginAction")
public class LoginRestController {

	/**
	 *结合sso自动登录访问地址
	 */
	@RequestMapping(value="/loginCasOut", method=RequestMethod.POST)
	@ResponseBody
	public ResponseVo loginCasOut(HttpSession session){
		session.invalidate();
		return new ResponseVo();
	}
	
}
