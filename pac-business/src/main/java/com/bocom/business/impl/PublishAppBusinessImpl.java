package com.bocom.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bocom.business.PublishAppBusiness;
import com.bocom.domain.pac.AppRelationInfo;
import com.bocom.domain.pac.ResultDo;
import com.bocom.service.app.AppRestService;
import com.bocom.service.pac.CoreService;
import com.bocom.service.pac.WidgetService;
import com.bocom.service.pac.impl.AppBaseInfoServiceImpl;
import com.bocom.support.exception.MyRunTimeException;
import com.bocom.support.servlet.ResultStringKey;
import com.bocom.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.*;

@Service
public class PublishAppBusinessImpl implements PublishAppBusiness {

    private static Logger logger = LoggerFactory
            .getLogger(PublishAppBusinessImpl.class);

    @Resource
    private CoreService coreService;

    @Resource
    private WidgetService widgetService;

    private static final String RESULT = "result";

    private static final String ERROR = "error";

    private static final String REASON = "reason";

    private static final String APP_TYPE = "appType";

    private static final String APP_ID = "appId";

    private static final String WIDGET = "widget";

    @Value("${rest.mar.upMAppUseTime.url}")
    private String upMAppUseTime;

    @Override
    public Map<String, Object> publishApp(String file, String appId, String rate, String userId) {
        Map<String, Object> map = new HashMap<>();
        try {
            JSONArray json = JSON.parseArray(rate);
            Integer appIdInt = Integer.parseInt(appId);
            JSONObject jsonObject = dealData(json, appIdInt, userId);

            //插入组装关系之前  先删除之前老的关系  并让老的关联关系和新的关联关系进行比较  然后进行增删次数
            List<AppRelationInfo> appRelationInfoListAll =
                    widgetService.selectRelationByAppId(Integer.parseInt(appId));
            Map<String, Integer> count = new HashMap<>();
            widgetService.deleteByAppId(appIdInt);
            for (AppRelationInfo appRelationInfo : appRelationInfoListAll) {
                String mAppIds;
                if ("WIDGET".equals(appRelationInfo.getType())) {
                    mAppIds = appRelationInfo.getWidgetIds();
                } else {
                    mAppIds = appRelationInfo.getPageIds();
                }
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
            }
            // 计数
            List<Map<String, String>> compositionInfoList = (List<Map<String, String>>) jsonObject.get("num");
            for (Map<String, String> compositionInfoMap : compositionInfoList) {
                if (coreService.saveCompositionNum(compositionInfoMap, count, userId)) {
                    logger.debug("Popular application count success" + compositionInfoMap);
                }
                //讲计数发送给微应用注册中心
                sentCountToMar(compositionInfoMap, count);
            }
            if (!count.isEmpty()) {
                Iterator<Map.Entry<String, Integer>> it = count.entrySet().iterator();
                Map<String, String> marParam = new HashMap<>();
                while (it.hasNext()) {
                    Map.Entry<String, Integer> entry = it.next();
                    marParam.put("num", String.valueOf(entry.getValue()));
                    marParam.put("type", "2");
                    marParam.put(APP_ID, entry.getKey());
                    marParam.put("appType", "1");//0代表PAC 1代表BSC
                    HttpClientUtil.getBase64Data(upMAppUseTime, marParam);
                }
            }
            //插入应用组装关系 -- widget
            AppRelationInfo widgetInfo = (AppRelationInfo) jsonObject.get(WIDGET);
            if (!StringUtils.isNullOrEmpty(widgetInfo.getWidgetIds())) {
                int insert = widgetService.insertRelationInfo(widgetInfo);
                logger.debug("widgetgAppRelationInfo  insert  success ,result is =" + insert);
            }
            //插入应用组装关系 -- app
            List<AppRelationInfo> appRelationInfoList = (List<AppRelationInfo>) jsonObject.get("app");
            for (AppRelationInfo relationInfo : appRelationInfoList) {
                if (!StringUtils.isNullOrEmpty(relationInfo.getPageIds())) {
                    int insert2 = widgetService.insertRelationInfo(relationInfo);
                    logger.debug("appRelationInfo   insert  success ,result is =" + insert2);
                }
            }
            String filePath = ConfigGetPropertyUtil.get("configs.filePath.url");
            File configFile = new File(filePath + "configs" + appId + "_txt");
            if (createFile(map, file, configFile))
                return map;
            ResultDo resultDo = coreService.makeNewApp(appId);
            if (resultDo.isSuccess()) {
                map.put(RESULT, "success");
            } else {
                map.put(RESULT, ERROR);
                map.put(REASON, resultDo.getMessage());
                return map;
            }
        } catch (Exception e) {
            logger.error("publishApp createFile error " + e);
            throw new MyRunTimeException("publishApp createFile error " + e + this.getClass());
        }
        return map;
    }

    /**
     * 发布时候通知 mar 更新相应的组装次数
     *
     * @param compositionInfoMap
     */
    private void sentCountToMar(Map<String, String> compositionInfoMap, Map<String, Integer> countAll) {
        if (countAll.isEmpty()) {
            Map<String, String> getParam = new HashMap<>();
            String mAppId = compositionInfoMap.get(APP_ID);
            String num = compositionInfoMap.get("num");
            getParam.put(APP_ID, mAppId);
            getParam.put("num", num);
            getParam.put("type", "1");
            getParam.put("appType", "1");//0代表PAC 1代表BSC
            String result = HttpClientUtil.getBase64Data(upMAppUseTime, getParam);
            if (logger.isDebugEnabled()) {
                logger.debug("upMAppUseTime result === " + result);
            }
        } else {
            Map<String, String> getParam = new HashMap<>();
            String mAppId = compositionInfoMap.get(APP_ID);
            String num = compositionInfoMap.get("num");
            Integer numNew = Integer.parseInt(num);
            Integer numOld = countAll.get(mAppId) == null ? 0 : countAll.get(mAppId);
            countAll.remove(mAppId);
            if (numNew > numOld) {
                getParam.put(APP_ID, mAppId);
                getParam.put("num", String.valueOf(numNew - numOld));
                getParam.put("type", "1");
            } else {
                getParam.put(APP_ID, mAppId);
                getParam.put("num", String.valueOf(numOld - numNew));
                getParam.put("type", "2");
            }
            getParam.put("appType", "1");//0代表PAC 1代表BSC
            String result = HttpClientUtil.getBase64Data(upMAppUseTime, getParam);
            if (logger.isDebugEnabled()) {
                logger.debug("upMAppUseTime result === " + result);
            }

        }
    }

    private boolean createFile(Map<String, Object> map, String file, File configFile) throws IOException {
        File filePath = new File(configFile.getParent());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        try (OutputStreamWriter fileWriter = new OutputStreamWriter(
                new FileOutputStream(configFile), "UTF-8")) {
            fileWriter.write(URLDecoder.decode(file, "utf-8"));
        } catch (Exception e) {
            logger.error(String.valueOf(e));
            map.put(RESULT, ERROR);
            map.put(REASON, "发布失败 ，请重试 ！");
            return true;
        }
        return false;
    }

    //另存为模板最新
    private boolean createFileSaveAs(Map<String, Object> map, String file, File configFile
            , String fileName)
            throws IOException {
        File filePath = new File(configFile.getParent());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        try (OutputStreamWriter fileWriter = new OutputStreamWriter(
                new FileOutputStream(configFile), "UTF-8")) {
            fileWriter.write(URLDecoder.decode(file, "utf-8"));//到这里，configs文件写好了


        } catch (Exception e) {
            logger.error(String.valueOf(e));
            map.put(RESULT, ERROR);
            map.put(REASON, "发布失败 ，请重试 ！");
            return true;
        }
        return false;
    }

    //另存为的时候保存数据文件iframe
    private boolean createFileForIframe(Map<String, Object> map, String file, File configFile) throws IOException {
        File filePath = new File(configFile.getParent());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        try (OutputStreamWriter fileWriter = new OutputStreamWriter(
                new FileOutputStream(configFile), "UTF-8")) {
            fileWriter.write(URLDecoder.decode(file, "utf-8"));
        } catch (Exception e) {
            logger.error(String.valueOf(e));
            map.put(RESULT, ERROR);
            map.put(REASON, "发布失败 ，请重试 ！");
            return true;
        }
        return false;
    }

    private static JSONObject dealData(JSONArray jsonArray, Integer appId, String userId) {

        AppRelationInfo widgetReInfo = new AppRelationInfo();
        widgetReInfo.setType("WIDGET");
        widgetReInfo.setAppId(appId);
        widgetReInfo.setUserId(userId);
        widgetReInfo.setCreateTime(new Date());
        List<AppRelationInfo> relationInfoList = new ArrayList<>();
        List<Map<String, String>> compositionInfoList = new ArrayList<>();
        Map<String, String> tempMapForApp = new HashMap<>();
        for (Object object : jsonArray) {
            JSONObject sourceJson = JSON.parseObject(String.valueOf(object));
            String type = sourceJson.getString(APP_TYPE);
            String num = sourceJson.getString("num");
            int numInt = Integer.parseInt(num);
            StringBuilder id = new StringBuilder();
            for (int i = 0; i < numInt; i++) {
                String idTemp = "id=" + sourceJson.getString(APP_ID) + ",";
                id.append(idTemp);
            }
//            String id = "id=" + sourceJson.getString(APP_ID) + ",";//格式方便，后续校验是否有关联时写sql
            if (WIDGET.equals(type)) {
                String ids = widgetReInfo.getWidgetIds();
                if (StringUtils.isNullOrEmpty(ids)) {
                    widgetReInfo.setWidgetIds(id.toString());
                } else {
                    widgetReInfo.setWidgetIds(ids + id);
                }
            } else {
                String oAppId = tempMapForApp.get(type);
                if (StringUtils.isNullOrEmpty(oAppId)) {
                    tempMapForApp.put(type, id.toString());
                } else {
                    tempMapForApp.put(type, oAppId + id);
                }
            }
            Map<String, String> compositionInfoMap = new HashMap<>();
            compositionInfoMap.put(APP_TYPE, type.toUpperCase());
            compositionInfoMap.put(APP_ID, sourceJson.getString(APP_ID));
            compositionInfoMap.put("num", num);
            compositionInfoList.add(compositionInfoMap);
        }
        for (Map.Entry<String, String> entry : tempMapForApp.entrySet()) {
            AppRelationInfo relationInfo = new AppRelationInfo();
            relationInfo.setType("APP");
            relationInfo.setThirdAppId(entry.getKey());
            relationInfo.setPageIds(entry.getValue());
            relationInfo.setAppId(appId);
            relationInfo.setUserId(userId);
            relationInfo.setCreateTime(new Date());
            relationInfoList.add(relationInfo);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(WIDGET, widgetReInfo);
        jsonObject.put("app", relationInfoList);
        jsonObject.put("num", compositionInfoList);
        return jsonObject;
    }

    @Override
    public Map<String, Object> saveConfigs(String file, String appId, String rate, String userId,
                                           String userTypeFileName, String iframeId, String configsFilePath) {
        Map<String, Object> map = new HashMap<>();
        try {
//            String filePath = ConfigGetPropertyUtil.get("backupconfigs.filePath.url");
            String filePath = ConfigGetPropertyUtil.get("saveas.filePath.url");

            //校验模板名字是否有重复
            File tempFile = new File(filePath);
            String[] tempFileList = tempFile.list();
            if (tempFileList != null) {
                for (String tempNameFile : tempFileList) {
                    String tempName = tempNameFile.replace("-configs_txt", "");
                    if (tempName.equals(userTypeFileName)) {
                        map.put(RESULT, ERROR);
                        map.put(REASON, "模板名称重复，请重试！");
                        return map;
                    }
                }
            }

//            File configFile = new File(filePath + "configs" + appId + "_txt");
            File configFile = new File(filePath +
                    userTypeFileName + "-configs_txt");//文件保存在项目内部, 不可放置在外部
//            configFile=new File(configsFilePath+(userTypeFileName+"-configs_txt"));
            if (createFileSaveAs(map, file, configFile, userTypeFileName))
                return map;

            //开始处理iframe的事情
            backUpFileSaveAs(appId, userTypeFileName);
            ResultDo resultDo = new ResultDo();
            resultDo.setSuccess(true);
            if (resultDo.isSuccess()) {
                map.put(RESULT, "success");
            } else {
                map.put(RESULT, ERROR);
                map.put(REASON, resultDo.getMessage());
                return map;
            }
        } catch (Exception e) {
            logger.error("publishApp createFile error " + e);
            throw new MyRunTimeException("\\saveConfigs createFile error " + e + this.getClass());
        }
        return map;
    }

    //另存为模板最新2.0
    private String backUpFileSaveAs(String appId, String fileName) {
        String filePath = ConfigGetPropertyUtil.get("configs.filePath.url");
        File file = new File(filePath + "backUp");
        if (!file.exists() && !file.mkdirs()) {
            logger.error("create backUpdir error ");
        }
        File iframeFIle = new File(filePath + "iframe_" + appId);
        if (iframeFIle.exists()) {
            FileUtil.copy(filePath + "iframe_" + appId, filePath + "saveas/" + fileName + "/iframe");
            return ResultStringKey.SUCCESS;
        }
        return ResultStringKey.ERROR;
    }
}
