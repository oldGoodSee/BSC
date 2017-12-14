/**
 * Project Name:pwc-util
 * File Name:AutoSetUserAdapterFilter.java
 * Package Name:com.bocom.support.filter
 * Date:2016年7月19日上午11:03:00
 * Copyright (c) 2016, gaozhaosheng@bocom.cn All Rights Reserved.
 */
package com.bocom.filter;

import com.bocom.activemq.TopicMessageSender;
import com.bocom.config.WebAppInitializer;
import com.bocom.dto.UserInfoPAPDto;
import com.bocom.dto.session.RolePermission;
import com.bocom.dto.session.SessionUserInfo;
import com.bocom.dto.session.UserRoleInfo;
import com.bocom.jms.bean.LogInfo;
import com.bocom.service.user.UserRestService;
import com.bocom.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 自动根据单点登录系统的信息设置本系统的用户信息 date: 2016年7月19日 上午11:03:00 <br/>
 *
 * @author gaozhaosheng
 * @since JDK 1.7
 */
@WebFilter(filterName = "AutoSetUserAdapterFilter", urlPatterns = "/*")
public class AutoSetUserAdapterFilter implements Filter {


    private static Logger logger = LoggerFactory
            .getLogger(AutoSetUserAdapterFilter.class);

    private List<RolePermission> rolePermList;
    private String appId;
    private String appVersion;
    private boolean jmsSwitcher;
    private String casServerUrlPrefix;


    /**
     * 登陆业务
     */
    private UserRestService userRestService;

    private TopicMessageSender topicMessageSender;

    /**
     * 用户的Session标志
     */
    private static String USER = "sessionUserInfo";

    /**
     * 保存用户信息到Session
     *
     * @param user
     */
    private static void saveUserToSession(HttpSession session, SessionUserInfo user) {
        session.setAttribute(USER, user);
    }

    private static void saveCasToSession(HttpSession session,
                                         String casServerUrlPrefix) {
        if (StringUtils.isNotBlank(casServerUrlPrefix)) {
            casServerUrlPrefix = casServerUrlPrefix.replace("/login", "");
        }
        session.setAttribute("casServerUrlPrefix", casServerUrlPrefix);
    }

    /**
     * 获取当前登录的用户
     *
     * @param session
     * @return
     */
    private static SessionUserInfo getCurrentUser(HttpSession session) {
        Object sessionUser = session.getAttribute(USER);
        if (sessionUser == null) {
            return null;
        }
        SessionUserInfo sessionUserInfo = (SessionUserInfo) sessionUser;
        return sessionUserInfo;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * 过滤逻辑：首先判断单点登录的账户是否已经存在本系统中，
     * 如果不存在使用用户查询接口查询出用户对象并设置在Session中
     *
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        // _const_cas_assertion_是CAS中存放登录用户名的session标志
        Object object = httpRequest.getSession().getAttribute("_const_cas_assertion_");

        if (object != null) {
            Assertion assertion = (Assertion) object;
            String loginName = assertion.getPrincipal().getName();
            SessionUserInfo sessionUserInfo = getCurrentUser(httpRequest
                    .getSession());
            //PAC 为组装应用提供方、不需要对自己进行临时授权
//            String userName = httpRequest.getParameter("userName");
//            String source = httpRequest.getParameter("source");
//            if (StringUtils.isNotEmpty(userName)
//                    && StringUtils.isNotEmpty(source)) {
//                sessionUserInfo = null;
//                loginName = userName;
//            }
            // 第一次登录系统
            if (sessionUserInfo == null) {
                sessionUserInfo = getSessionUserInfo(session, loginName, sessionUserInfo, "");
            }
            String requestURI = httpRequest.getRequestURI();
            String suffixURI = requestURI.substring(
                    requestURI.lastIndexOf("/") + 1, requestURI.length());
            if (!suffixURI.contains(".")) {
                // 发送消息
                if (jmsSwitcher) {
                    try {
                        topicMessageSender.sendMessage(new LogInfo(appId,
                                httpRequest.getRequestURL().toString(),
                                sessionUserInfo.getUserId(), new Date()));
                    } catch (Exception e) {
                        logger.error("logMessageQueueSender send message error", e);
                    }
                }
                // 权限控制
                if (!(StringUtils.isEmpty(suffixURI)
                        || suffixURI.startsWith(";jsessionid") || suffixURI
                        .contains("loginCasOut"))) {
                    if (verifyPermission(requestURI, sessionUserInfo.getUserRoles())) {
                        ((HttpServletResponse) response).sendError(403);
                    }
                }
            }
        }else{
            String ticket = httpRequest.getParameter("token");
            if (StringUtils.isNotBlank(ticket)) {
                Cookie casTGC = new Cookie("CASTGC", ticket);
                casTGC.setPath("/");
                ((HttpServletResponse) response).addCookie(casTGC);
                ((HttpServletResponse) response).sendRedirect(httpRequest.getRequestURI());
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private SessionUserInfo getSessionUserInfo(HttpSession session, String loginName, SessionUserInfo
            sessionUserInfo, String source) {
        if (StringUtils.isNotBlank(loginName)) {
            // 保存用户信息到Session
            sessionUserInfo = userRestService
                    .getUserInfoFromPAP(new UserInfoPAPDto(appId,
                            appVersion, loginName, source));
            saveUserToSession(session, sessionUserInfo);
            saveCasToSession(session, casServerUrlPrefix);
        }
        return sessionUserInfo;
    }

    private boolean verifyPermission(String uri, List<UserRoleInfo> userRoles) {
        boolean isForbidden = true;
        if (null != userRoles && !userRoles.isEmpty()) {
            for (UserRoleInfo userRoleInfo : userRoles) {
                String roleCode = userRoleInfo.getRoleCode();
                for (RolePermission role : rolePermList) {
                    if (null != role && role.getRoleCode().equals(roleCode)) {
                        for (String permission : role.getPlist()) {
                            if (uri.contains(permission)) {
                                isForbidden = false;
                                break;
                            }
                        }
                        break;
                    }
                }
                if (!isForbidden) {
                    break;
                }
            }
        }
        return isForbidden;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.setProperty("jsse.enableSNIExtension", "false");
        WebApplicationContext wac = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig
                        .getServletContext());
        userRestService = (UserRestService) wac.getBean("userRestServiceImpl");
        topicMessageSender = (TopicMessageSender) wac
                .getBean("topicMessageSender");
        String rolePermission = WebAppInitializer.get("role.permission");
        RolePermission[] rolePermissions = JsonUtil.readValue(rolePermission,
                RolePermission[].class);
        rolePermList = Arrays.asList(rolePermissions);
        jmsSwitcher = Boolean.valueOf(WebAppInitializer.get("jms.switcher",
                "false"));
//        casServerUrlPrefix = WebAppInitializer.get("cas.casServerUrlPrefix");
        casServerUrlPrefix = WebAppInitializer.get("cas.casServerLoginUrl");
        appId = WebAppInitializer.get("project.appId");
        appVersion = WebAppInitializer.get("project.app.version");
    }

}
