package com.bocom.dao;

import com.bocom.domain.pac.AppBaseInfo;

import java.util.List;
import java.util.Map;


public interface AppBaseInfoDao {

    List<AppBaseInfo> getAppBaseInfoList(Map<String, Object> param);

    List<AppBaseInfo> getAuthorizeAppList(Map<String, Object> param);

    AppBaseInfo getAppBaseInfoById(Integer appId);

    List<AppBaseInfo> getAppNameIdList(Map<String, Object> param);

    Integer addAppInfo(AppBaseInfo appBaseInfo);

    List<AppBaseInfo> queryAppInfo(Map<String, Object> param);

    void updateStatus(Map<String, Object> param);

    void deleteApp(Map<String, Object> param);

    void insertAppSnapshot(Map<String, Object> param);

    void updateAppSnaTime(Map<String, Object> param);

    List<Map<String, Object>> getAppHistory(Map<String, Object> param);

    int selectAppInfoByAppName(Map<String, Object> param);

    List<AppBaseInfo> getInfoByUninstallTime(Map<String, Object> param);

    int updateAppBaseInfo(AppBaseInfo param);

    AppBaseInfo getAppInfoByTaskId(String taskId);


}
