package com.bocom.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class LoginController {

	@RequestMapping(method=RequestMethod.GET)
	public String homePage() {
		return "redirect:/manager/menuAction/toIndex";
	}
	/**
	 * 结合sso自动登录访问地址
	 */
	@RequestMapping(value = "/manager/loginAction/loginCas")
	public String loginCas(ModelMap model, HttpSession session) {
		return "home/homepage";
	}
}
