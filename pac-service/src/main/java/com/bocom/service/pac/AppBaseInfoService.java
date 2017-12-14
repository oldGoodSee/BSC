package com.bocom.service.pac;

import com.alibaba.fastjson.JSONObject;
import com.bocom.domain.pac.AppBaseInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface AppBaseInfoService {

    JSONObject getAppBaseInfoList(Map<String, Object> param);

    Integer addAppInfo(AppBaseInfo appBaseInfo) throws SQLException;

    List<AppBaseInfo> queryDataByParam(Map<String, Object> param);

    List<AppBaseInfo> queryAppNameIdList(Map<String, Object> param);

    void updateAppStatus(Map<String, Object> param);

    String deleteApp(String appId, String status, String flag) throws IOException;

    List<Map<String, Object>> getAppHistory(Map<String, Object> param, String userId, boolean historyFlag);

    int selectAppInfoByAppName(Map<String, Object> param);

    AppBaseInfo getAppInfoById(Integer appId);

    List<AppBaseInfo> getInfoByUninstallTime(Map<String, Object> param);

    int updateAppBaseInfo(AppBaseInfo param);

    AppBaseInfo getAppInfoByTaskId(String taskId);
}
