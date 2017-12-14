package com.bocom.service.app.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.service.app.AppRestService;
import com.bocom.util.HttpClientUtil;
import com.bocom.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AppRestServiceImpl implements AppRestService {

    private static Logger logger = LoggerFactory
            .getLogger(AppRestServiceImpl.class);

    @Value("${rest.arcm.queryAppInfo.url}")
    private String queryAppInfoUrl;

    @Value("${rest.mar.queryMicroApp.url}")
    private String queryMicroApp;

    @Value("${rest.mar.queryAllAppName.url}")
    private String queryAllAppName;

    @Override
    public JSONObject queryAppByAppId(Map<String, Object> param) {
        JSONObject appInfo = null;
        try {
            appInfo = getRespCommonDataPost(queryAppInfoUrl, param);
        } catch (Exception e) {
            logger.error("AppRestServiceImpl queryAppByAppId error" + e);
        }
        return appInfo;
    }

    @Override
    public JSONObject queryMicroApp(Map<String, String> param) {
        JSONObject appInfo = null;
        try {
            param.put("appType","1");//0代表PAC 1代表BSC
            appInfo = JSONObject.parseObject(HttpClientUtil.postBase64(queryMicroApp, JsonUtil.toJSon(param)));
        } catch (Exception e) {
            logger.error("AppRestServiceImpl queryMicroApp error" + e);
        }
        return appInfo;
    }

    @Override
    public JSONArray queryAllAppName() {
        JSONArray appInfo = null;
        try {
            Map<String,String> param = new HashMap<>();
            param.put("appType","1");//0代表PAC 1代表BSC
            appInfo = JSONArray.parseArray(HttpClientUtil.getBase64Data(queryAllAppName,param));
        } catch (Exception e) {
            logger.error("AppRestServiceImpl queryAllAppName error" + e);
        }
        return appInfo;
    }

    private JSONObject getRespCommonDataPost(String url, Map<String, Object> param) {
        String data = HttpClientUtil.postBase64(url, param);
        if (logger.isInfoEnabled()) {
            logger.info("response data: " + data);
        }
        return JSONObject.parseObject(data);
    }


}
