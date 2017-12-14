package com.bocom.support.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class SessionFilter implements Filter {
	
	/** 要检查的 session 的名称 */
    private String sessionKey;
     
    /** 需要排除（不拦截）的URL的正则表达式 */
    private Pattern excepUrlPattern;
     
    /** 检查不通过时，转发的URL */
    private String forwardUrl;

	public void init(FilterConfig cfg) throws ServletException {
		sessionKey = cfg.getInitParameter("sessionKey");  
	    String excepUrlRegex = cfg.getInitParameter("excepUrlRegex");
	        if (!StringUtils.isBlank(excepUrlRegex)) {
	            excepUrlPattern = Pattern.compile(excepUrlRegex);
	        }
	    forwardUrl = cfg.getInitParameter("forwardUrl");
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
	    // 如果 sessionKey 为空，则直接放行
        if (StringUtils.isBlank(sessionKey)) {
            chain.doFilter(req, res);
            return;
        }
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String servletPath = request.getServletPath();
        // 如果请求的路径与forwardUrl相同，或请求的路径是排除的URL时，则直接放行
        if (servletPath.equals(forwardUrl) || excepUrlPattern.matcher(servletPath).matches()) {
            chain.doFilter(req, res);
            return;
        }
        
        Object sessionObj = request.getSession().getAttribute(sessionKey);
        // 如果Session为空，则跳转到指定页面
        if (sessionObj == null) {
            String contextPath = request.getContextPath();
            //response.sendRedirect(contextPath + StringUtils.defaultIfEmpty(forwardUrl, "/"));
            
            String str = "<script language='javascript'>alert('会话过期,请重新登录');"
                    + "window.top.location.href='"
                    + contextPath + StringUtils.defaultIfEmpty(forwardUrl, "/")
                    + "';</script>";
            response.setContentType("text/html;charset=UTF-8");// 解决中文乱码
            try {
                PrintWriter writer = response.getWriter();
                writer.write(str);
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
	    } else {
	        chain.doFilter(req, res);
	    }
		
	}

	public void destroy() {
		
	}


}
