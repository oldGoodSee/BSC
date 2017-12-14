package com.bocom.service.pac.impl;

import com.bocom.domain.pac.AppOperationLogInfo;
import com.bocom.dto.session.SessionUserInfo;
import com.bocom.service.pac.BaseService;
import com.bocom.service.pac.BehaviorService;
import com.bocom.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;


public class BaseServiceImpl implements BaseService {

    @Resource
    private BehaviorService behaviorService;

    @Resource
    protected HttpServletRequest request;

    private static Logger logger = LoggerFactory
            .getLogger(BaseServiceImpl.class);

    /**
     * 日志保存
     *
     * @param operationType    操作类型
     * @param operationContent 操作内容
     */
    @Override
    public void saveLog(String operationType, String businessType, String operationContent) {
        try {
            HttpSession session = request.getSession();
            SessionUserInfo userInfo = (SessionUserInfo) session.getAttribute("sessionUserInfo");
            this.saveLog(operationType, businessType, operationContent, userInfo);
        } catch (Exception e) {
            logger.error("saveLog error " + e);
            this.saveLog(operationType, businessType, operationContent, null);
        }

    }

    /**
     * 日志保存
     *
     * @param operationType    操作类型
     * @param operationContent 操作内容
     * @param userInfo         用户信息
     */
    @Override
    public void saveLog(String operationType, String businessType, String operationContent, SessionUserInfo userInfo) {
        AppOperationLogInfo appOperationLogInfo = new AppOperationLogInfo();
        String ip = "0";
        if (null != userInfo) {
            appOperationLogInfo.setUserName(userInfo.getUserName());
            appOperationLogInfo.setUserId(String.valueOf(userInfo.getUserId() == null ? "" : userInfo.getUserId()));
            appOperationLogInfo.setOrgId(userInfo.getOrgCode());
            appOperationLogInfo.setOrgName(userInfo.getOrgName());
            ip = UserUtils.getIp(request);
        }
        appOperationLogInfo.setOperationType(operationType);
        appOperationLogInfo.setBusinessType(businessType);
        appOperationLogInfo.setContent(operationContent);
        appOperationLogInfo.setCreateTime(new Date());
        appOperationLogInfo.setIpAddr(ip);
        behaviorService.addBehaviorLog(appOperationLogInfo);
    }

    public static void main(String[] args) {
        String aaa = String.valueOf(null);
    }
}
