package com.bocom.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Properties;

public class WebAppInitializer extends
        AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String PROPERTIY_PATH = "/WEB-INF/classes/conf/application.properties";

    private static Properties properteis;

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return null;
    }

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        InputStream in = servletContext.getResourceAsStream(PROPERTIY_PATH);
        properteis = new Properties();
        try {
            properteis.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 系统启动时注册filter
        Dynamic casFilter = servletContext.addFilter("CASFilter",
                AuthenticationFilter.class);
        casFilter.setInitParameter("casServerLoginUrl",
                properteis.getProperty("cas.casServerLoginUrl"));
        casFilter.setInitParameter("serverName",
                properteis.getProperty("cas.serverName"));
        casFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class),
                true,
                properteis.getProperty("cas.authenticationFilter.urlPattern"));

        Dynamic cprtValidationFilter = servletContext.addFilter(
                "CASValidationFilter",
                Cas20ProxyReceivingTicketValidationFilter.class);
        cprtValidationFilter.setInitParameter("casServerUrlPrefix",
                properteis.getProperty("cas.casServerUrlPrefix"));
        cprtValidationFilter.setInitParameter("serverName",
                properteis.getProperty("cas.serverName"));
        cprtValidationFilter.addMappingForUrlPatterns(
                EnumSet.allOf(DispatcherType.class), true,
                properteis.getProperty("cas.cas20ProxyRTVFilter.urlPattern"));

        super.onStartup(servletContext);
    }

    @Override
    protected Dynamic registerServletFilter(ServletContext arg0, Filter filter) {
        return super.registerServletFilter(arg0, filter);
    }

    public static String get(String key) {
        return properteis.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return properteis.getProperty(key, defaultValue);
    }
}
