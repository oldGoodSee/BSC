package com.bocom.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.bocom.dto.*;
import com.bocom.dto.session.SessionUserInfo;
import com.bocom.dto.session.UserRoleInfo;
import com.bocom.service.user.UserRestService;
import com.bocom.util.HttpClientUtil;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRestServiceImpl implements UserRestService {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Logger logger = LoggerFactory
            .getLogger(UserRestServiceImpl.class);

    @Value("${rest.user.getUserInfoByLoginName.url}")
    private String getUserInfoByLoginNameUrl;

    @Value("${rest.user.getOfficeInfo.url}")
    private String getOfficeInfoUrl;

    @Override
    public SessionUserInfo getUserInfoByLoginName(String loginName) {
        SessionUserInfo sessionUserInfo = new SessionUserInfo();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userName", loginName);
            String result = HttpClientUtil.postBase64(getOfficeInfoUrl, jsonObject.toJSONString());
            if (logger.isDebugEnabled()) {
                logger.debug("getOfficeInfo result is " + result);
            }
            JSONObject jsonObject1 = JSONObject.parseObject(result);
            if (jsonObject1.getBoolean("success")) {
                JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
                sessionUserInfo.setUserName(loginName);
                sessionUserInfo.setOrgCode(jsonObject2.getString("code"));
                sessionUserInfo.setOrgName(jsonObject2.getString("name"));
            }
        } catch (Exception e) {
            logger.error("UserRestServiceImpl getUserInfoByLoginName error", e);
        }
        return sessionUserInfo;
    }

    private SessionUserInfo createBean(SessionUserInfoDto dto) {
        SessionUserInfo data = new SessionUserInfo();
        if (dto != null) {
            data.setUserId(dto.getUserId());
            data.setUserName(dto.getUserName());
            data.setPoliceCode(dto.getPoliceCode());
            data.setPoliceName(dto.getPoliceName());
            List<OrgRoleInfo> roleUserInfoList = dto.getOrgRoleUserInfoList();
            if (roleUserInfoList != null && !roleUserInfoList.isEmpty()) {
                OrgRoleInfo info = roleUserInfoList.get(0);
                data.setRoleOrgCode(info.getRoleOrgCode());
                data.setRoleOrgName(info.getRoleOrgName());
                data.setOrgCode(info.getRoleOrgCode());
                data.setOrgName(info.getRoleOrgName());
                data.setParentOrgCode(info.getParentOrgCode());
                data.setParentOrgName(info.getParentOrgName());
                ArrayList<UserRoleInfo> userRoles = new ArrayList<>();
                for (int i = 0, len = roleUserInfoList.size(); i < len; i++) {
                    OrgRoleInfo info1 = roleUserInfoList.get(i);
                    UserRoleInfo userRoleInfo = new UserRoleInfo();
                    userRoleInfo.setRoleCode(info1.getRoleCode());
                    userRoleInfo.setRoleName(info1.getRoleName());
                    userRoles.add(userRoleInfo);
                }
                data.setUserRoles(userRoles);
            }
        }
        return data;
    }

    @Override
    public SessionUserInfo getUserInfoFromPAP(UserInfoPAPDto paramDto) {
        SessionUserInfo sessionUserInfo = null;
        try {
            String json = objectMapper.writeValueAsString(paramDto);
            String data = HttpClientUtil.postBase64(getUserInfoByLoginNameUrl, json);
            if (logger.isInfoEnabled()) {
                logger.info("response data: " + data);
            }
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            SessionUserRespDto responseDto = objectMapper.readValue(data,
                    SessionUserRespDto.class);
            sessionUserInfo = createBean(responseDto.getData());
        } catch (Exception e) {
            logger.error("UserRestServiceImpl getUserInfoFromPAP error", e);
        }
        return sessionUserInfo;
    }
}
