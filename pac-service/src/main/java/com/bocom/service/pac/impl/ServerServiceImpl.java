package com.bocom.service.pac.impl;

import com.bocom.dao.AppValidateUserDao;
import com.bocom.domain.pac.AppValidateUser;
import com.bocom.service.pac.ServerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ServerServiceImpl implements ServerService {

    @Resource
    private AppValidateUserDao appValidateUserDao;

    @Override
    public AppValidateUser getPowerDesc(String appCode) {

        return appValidateUserDao.getPowerDesc(appCode);
    }

}
