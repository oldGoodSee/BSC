package com.bocom.service.pac.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.dao.AppBaseInfoDao;
import com.bocom.dao.AppRelationDao;
import com.bocom.dao.CoreDAO;
import com.bocom.domain.UserRoleOrg;
import com.bocom.domain.pac.AppBaseInfo;
import com.bocom.domain.pac.AppRelationInfo;
import com.bocom.enums.DictionaryEnums;
import com.bocom.service.pac.AppBaseInfoService;
import com.bocom.support.servlet.ResultStringKey;
import com.bocom.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Service
public class AppBaseInfoServiceImpl extends BaseServiceImpl implements AppBaseInfoService {


    private static Logger logger = LoggerFactory
            .getLogger(AppBaseInfoServiceImpl.class);

    @Resource
    private AppBaseInfoDao appBaseInfoDao;
    @Resource
    private AppRelationDao appRelationDao;
    @Resource
    private CoreDAO coreDao;

    @Value("${rest.mar.upMAppUseTime.url}")
    private String upMAppUseTime;

    @Override
    public JSONObject getAppBaseInfoList(Map<String, Object> param) {
        JSONObject result = new JSONObject();
        List<AppBaseInfo> appBaseInfo = appBaseInfoDao.getAppBaseInfoList(param);
        JSONArray data = JSONArray.parseArray(JSONArray.toJSONString(appBaseInfo));
        result.put("data", data);
        result.put("success", true);
        result.put("message", "success");
        return result;
    }

    @Override
    public Integer addAppInfo(AppBaseInfo appBaseInfo) throws SQLException {
        super.saveLog(DictionaryEnums.ACTION_ADD.getDictionaryCode(), DictionaryEnums.BUSINESS_PAC.getDictionaryCode
                (), "创建组装应用 ：" + appBaseInfo.getAppName());
        return appBaseInfoDao.addAppInfo(appBaseInfo);
    }

    @Override
    public List<AppBaseInfo> queryDataByParam(Map<String, Object> param) {
        PageUtil.dealPage(param);
        return appBaseInfoDao.queryAppInfo(param);
    }

    @Override
    public AppBaseInfo getAppInfoById(Integer appId) {
        return appBaseInfoDao.getAppBaseInfoById(appId);
    }

    @Override
    public void updateAppStatus(Map<String, Object> param) {
        AppBaseInfo appBaseInfo = appBaseInfoDao.getAppBaseInfoById(Integer.parseInt(param.get(ResultStringKey
                .APP_ID) + ""));
        appBaseInfoDao.updateStatus(param);
        String msg;
        if (Integer.parseInt(param.get("status").toString()) == 0) {
            msg = "屏蔽";
        } else if (Integer.parseInt(param.get("status").toString()) == 1) {
            msg = "正在运行";
        } else {
            msg = param.get("status") + "";
        }
        super.saveLog(DictionaryEnums.ACTION_UPDATE.getDictionaryCode(), DictionaryEnums.BUSINESS_PAC
                        .getDictionaryCode(),
                "更新组装应用 ： " + appBaseInfo.getAppName() + "；更新状态为 " + msg);
    }

    @Override
    public String deleteApp(String appId, String status, String flag) throws IOException {
        if (StringUtils.isNullOrEmpty(appId, status)) {
            return ResultStringKey.ERROR;
        }
        if (!"6".equals(status.trim())) {
            //备份文件
            backUpFile(appId);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appId", Integer.parseInt(appId));
        param.put("uninstallTime", DateUtil.dateToStrLong(new Date()));
        List<AppRelationInfo> appRelationInfoList = appRelationDao.selectRelationByAppId(Integer.parseInt(appId));
        for (AppRelationInfo appRelationInfo : appRelationInfoList) {
            if (appRelationInfo != null) {
                String type = appRelationInfo.getType();
                //卸载组装应用时候
                if ("WIDGET".equals(type)) {
                    String widgetIds = appRelationInfo.getWidgetIds();
                    // 将 微应用卸载次数发送给MAR
                    sendUnCountToMar(widgetIds);
                } else {
                    String pageIds = appRelationInfo.getPageIds();
                    // 将 传统 微应用卸载次数发送给MAR
                    sendUnCountToMar(pageIds);
                }
            }
        }
        AppBaseInfo appBaseInfo = appBaseInfoDao.getAppBaseInfoById(Integer.parseInt(appId));
        //插入到备份表中
        appBaseInfoDao.insertAppSnapshot(param);
        //更新卸载时间
        appBaseInfoDao.updateAppSnaTime(param);
        //删除组装应用
        appBaseInfoDao.deleteApp(param);
        //删除组装关系
        appRelationDao.deleteByAppId(Integer.parseInt(appId));
        //删除之前的授权信息
        UserRoleOrg userRoleOrg = new UserRoleOrg();
        userRoleOrg.setAppId(appId);
        coreDao.deleteAppUser(userRoleOrg);
        super.saveLog(DictionaryEnums.ACTION_DELETE.getDictionaryCode(), DictionaryEnums.BUSINESS_PAC
                        .getDictionaryCode(),
                flag + "卸载组装应用 ：" + appBaseInfo.getAppName());
        return ResultStringKey.SUCCESS;
    }

    /**
     * 卸载时候通知 mar 更新相应的卸载次数
     *
     * @param mAppIds
     */
    private void sendUnCountToMar(String mAppIds) {
        try {
            Map<String, Integer> count = new HashMap<>();
            String[] ids = mAppIds.replace("id=", "").split(",");
            for (String str : ids) {
                if (StringUtils.isNullOrEmpty(str)) {
                    break;
                }
                if (count.containsKey(str)) {
                    count.put(str, count.get(str) + 1);
                } else {
                    count.put(str, 1);
                }
            }
            Iterator<Map.Entry<String, Integer>> it = count.entrySet().iterator();
            Map<String, String> marParam = new HashMap<>();
            while (it.hasNext()) {
                Map.Entry<String, Integer> entry = it.next();
                marParam.put("num", String.valueOf(entry.getValue()));
                marParam.put("type", "2");
                marParam.put("appId", entry.getKey());
                marParam.put("appType", "1");//0代表PAC 1代表BSC
                HttpClientUtil.getBase64Data(upMAppUseTime, marParam);
            }
        } catch (Exception e) {
            logger.error("sendUnCountToMar error " + e);
        }
    }

    private void backUpFile(String appId) throws IOException {
        String filePath = ConfigGetPropertyUtil.get("configs.filePath.url");
        File file = new File(filePath + "backUp");
        if (!file.exists() && !file.mkdirs()) {
            logger.error("create backUpdir error ");
        }
        String resourceFile = filePath + "configs" + appId + "_txt";
        String targetFile = filePath + "backUp/" + "configs" + appId + "_txt";
        // 复制文件
        //复制配置文件
        SshUtil.cpFile(targetFile, resourceFile, true);

        //复制iframe
        File iframeFIle = new File(filePath + "iframe_" + appId);
        if (iframeFIle.exists()) {
            FileUtil.copy(filePath + "iframe_" + appId, filePath + "backUp");
            iframeFIle.delete();
        }
    }


    /**
     * @param param       获取卸载历史 的 appId
     * @param userId      用户ID
     * @param historyFlag 是否有卸载历史记录
     * @return list
     */
    @Override
    public List<Map<String, Object>> getAppHistory(Map<String, Object> param, String userId, boolean historyFlag) {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);//运行中
        statusList.add(0);//屏蔽中
        Map<String, Object> param2 = new HashMap<String, Object>();
        param2.put("statusList", statusList);
        param2.put("userId", userId);
        List<Map<String, Object>> result = new ArrayList<>();
        if (historyFlag) {
            result = appBaseInfoDao.getAppHistory(param);
            getAppRuningList(result, param2);
        } else {
            getAppRuningList(result, param2);
        }
        return result;
    }

    private void getAppRuningList(List<Map<String, Object>> result, Map<String, Object> param2) {
        List<AppBaseInfo> app = appBaseInfoDao.getAppNameIdList(param2);
        for (AppBaseInfo appBaseInfo : app) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("appId", appBaseInfo.getAppId());
            map.put("appName", appBaseInfo.getAppName());
            result.add(map);
        }
    }

    @Override
    public List<AppBaseInfo> queryAppNameIdList(Map<String, Object> param) {
        PageUtil.dealPage(param);
        return appBaseInfoDao.getAppNameIdList(param);
    }

    @Override
    public int selectAppInfoByAppName(Map<String, Object> param) {
        return appBaseInfoDao.selectAppInfoByAppName(param);
    }

    @Override
    public List<AppBaseInfo> getInfoByUninstallTime(Map<String, Object> param) {
        return appBaseInfoDao.getInfoByUninstallTime(param);
    }

    @Override
    public AppBaseInfo getAppInfoByTaskId(String taskId) {
        return appBaseInfoDao.getAppInfoByTaskId(taskId);
    }

    @Override
    public int updateAppBaseInfo(AppBaseInfo appBaseInfo) {
        super.saveLog(DictionaryEnums.ACTION_UPDATE.getDictionaryCode(), DictionaryEnums.BUSINESS_PAC
                        .getDictionaryCode(),
                "更新组装应用: " + appBaseInfo.getAppName());
        return appBaseInfoDao.updateAppBaseInfo(appBaseInfo);
    }
}
