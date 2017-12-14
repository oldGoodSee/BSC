package com.bocom.service.pac;

import com.bocom.dto.session.SessionUserInfo;


public interface BaseService {

    void saveLog(String opertionType,String  businessType, String content);

    void saveLog(String opertionType,String  businessType, String content, SessionUserInfo userInfo);
}
