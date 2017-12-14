package com.bocom.util;

import com.bocom.dto.session.SessionUserInfo;
import com.bocom.dto.session.UserRoleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bocom-qy on 2017-3-17.
 */
public class UserUtils {

    private static Logger logger = LoggerFactory
            .getLogger(UserUtils.class);


    public static String getUserId(HttpSession session) {
        SessionUserInfo sessionUserInfo = null;
        if (session.getAttribute("sessionUserInfo") != null) {
            sessionUserInfo =
                    (SessionUserInfo) session.getAttribute("sessionUserInfo");
        }
        if (sessionUserInfo == null) {
            return "";
        }
        return String.valueOf(sessionUserInfo.getUserId());
    }

    public static String getUserName(HttpSession session) {
        SessionUserInfo sessionUserInfo = null;
        if (session.getAttribute("sessionUserInfo") != null) {
            sessionUserInfo =
                    (SessionUserInfo) session.getAttribute("sessionUserInfo");
        }
        if (sessionUserInfo == null) {
            return "";
        }
        return String.valueOf(sessionUserInfo.getUserName());
    }

    public static String getUserOrgId(HttpSession session) {
        SessionUserInfo sessionUserInfo = null;
        if (session.getAttribute("sessionUserInfo") != null) {
            sessionUserInfo =
                    (SessionUserInfo) session.getAttribute("sessionUserInfo");
        }
        if (sessionUserInfo == null) {
            return "";
        }
        return String.valueOf(sessionUserInfo.getOrgCode());
    }

    public static List<String> getUserRoleCode(HttpSession session) {
        List<String> roleList = new ArrayList<>();
        SessionUserInfo sessionUserInfo = null;
        if (session.getAttribute("sessionUserInfo") != null) {
            sessionUserInfo =
                    (SessionUserInfo) session.getAttribute("sessionUserInfo");
        }
        if (sessionUserInfo == null) {
            return roleList;
        }
        for (UserRoleInfo userRoleInfo : sessionUserInfo.getUserRoles()) {
            if (!StringUtils.isNullOrEmpty(userRoleInfo.getRoleCode())) {
                roleList.add(userRoleInfo.getRoleCode());
            }
        }
        return roleList;
    }

    public static String getIp(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            //这里主要是获取本机的ip,可有可无
            if (ipAddress.equals("127.0.0.1")
                    || ipAddress.endsWith("0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    logger.error("getIp error " + e);
                }
                ipAddress = inet.getHostAddress();
            }

        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        //或者这样也行,对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        return ipAddress;
    }
}
