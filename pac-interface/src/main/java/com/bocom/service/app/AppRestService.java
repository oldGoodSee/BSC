package com.bocom.service.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface AppRestService {

    /**
     * 从应用注册中心获取数据
     *
     * @param param
     * @return JSON
     */
    JSONObject queryAppByAppId(Map<String, Object> param);

    /**
     * 从微应用注册中心获取数据
     *
     * @param param
     * @return JSON
     */
    JSONObject queryMicroApp(Map<String, String> param);

    /**
     * 从微应用注册中心获取传统应用的AppName
     *
     * @return JSON
     */
    JSONArray queryAllAppName();

}
