package com.bocom.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.business.WidgetBusiness;
import com.bocom.domain.pac.*;
import com.bocom.dto.UserInfoPAPDto;
import com.bocom.dto.session.SessionUserInfo;
import com.bocom.enums.DictionaryEnums;
import com.bocom.service.pac.CoreService;
import com.bocom.service.pac.WidgetService;
import com.bocom.service.pac.impl.BaseServiceImpl;
import com.bocom.service.user.UserRestService;
import com.bocom.support.servlet.ResultStringKey;
import com.bocom.util.*;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Service
public class WidgetBusinessImpl extends BaseServiceImpl implements WidgetBusiness {

    private static Logger logger = LoggerFactory
            .getLogger(WidgetBusinessImpl.class);

    private static final String SUCCESS = "success";

    private static final String REASON = "reason";

    private static final String FOLDER_NAME = "folderName";

    private static final String WIDGET_SAVE_FOLDER = "widgetFrame" + File.separator + "app" + File.separator;

    private static final String WIDGET_INIT_FOLDER = "widgetFrame" + File.separator + "init" + File.separator;

    private static final String PREFIX_FOR_WIDGET = "widget_";

    private static final String HTML_EXTENSION = ".html";

    private static final String REASON_RESULT = "服务器问题，请重试！";

    @Value("${rest.wrm.getWidgetList.url}")
    private String getWidgetListUrl;

    @Value("${project.appId}")
    private String projectAppId;

    @Value("${rest.dmmpdr.queryAreaType.url}")
    private String queryAreaTypeUrl;

    @Value("${rest.dmmpdr.queryDB.url}")
    private String queryDBUrl;

    @Value("${rest.dmmpdr.queryTables.url}")
    private String queryTablesUrl;

    @Value("${rest.mar.queryMicroApp.url}")
    private String queryWidget;

    @Value("${rest.wrm.addRate.url}")
    private String addRate;

    @Value("${rest.mar.deleteMApp.url}")
    private String deleteMApp;


    @Value("${rest.mar.queryCategory.url}")
    private String queryCategory;

    @Resource
    private WidgetService widgetService;

    @Resource
    private UserRestService userRestService;

    @Resource
    private CoreService coreService;

    private ZipFileUtils zipFileUtils = new ZipFileUtils();

    private static ObjectMapper objectMapper = new ObjectMapper();

//    private FastDfsUtil fast = new FastDfsUtil();

    /**
     * 根据appId获取 相应的控件组装关系，若此控件之前解压下来的已经不存在  则去再次下载解压
     *
     * @param appId    应用ID
     * @param userName 用户名
     * @param path     资源路径
     */
    @Override
    public void createMissWidgetFile(String appId, String userName, String path) {
        PackagingInfo packagingInfo = new PackagingInfo();
        packagingInfo.setAppid(appId);
        Integer status = coreService.getAppStatus(packagingInfo);
        if (status == 1) {
            //获取组装关系为微应用的 id
            AppRelationInfo relationInfo = widgetService.selectWidgetRelationByAppId(Integer.parseInt(appId));
            String widgetIds = relationInfo.getWidgetIds();
            //数据库中保存的格式为 id=1,id=3,
            //在根据widgetId 获取解压过的文件，判断是否存在  不存在就再次生成微应用
            String[] widgetIdArray = widgetIds.trim().split("id=");
            for (String str : widgetIdArray) {
                String widgetId = str.replace(",", "");
                if (!StringUtils.isNullOrEmpty(widgetId) && StringUtils.isNumber(widgetId)) {
                    unzipWidgetFile(widgetId, path);
                }
            }
        }
    }

    /**
     * 保存 发布的控件
     *
     * @param path          项目的资源文件路径
     * @param doc           前台页面传入的标签<html>中的内容
     * @param appWidgetInfo 待保存的控件信息
     * @return jsonObject
     */
    @Override
    public JSONObject saveWidget(String path, String doc, AppWidgetInfo appWidgetInfo, String sql) {
        String msg;
        if (!StringUtils.isNullOrEmpty(sql)) {
            msg = "  , 数据控件信息为 : " + sql;
        } else {
            msg = "";
        }
        super.saveLog(DictionaryEnums.ACTION_ADD.getDictionaryCode(), DictionaryEnums.BUSINESS_MICO_APP
                        .getDictionaryCode(),
                "发布控件 : " + appWidgetInfo.getWidgetName() + msg);
        widgetService.addWidget(appWidgetInfo);//save widget  ;db return ID
        JSONObject jsonObject = buildWidget(path, doc, appWidgetInfo, null);
        if (jsonObject.getBoolean(ResultStringKey.SUCCESS)) {
            //notice wrm
            noticeWRM(appWidgetInfo);
        }
        return jsonObject;
    }

    /**
     * 通知控件库  此控件被使用
     *
     * @param appWidgetInfo
     */
    private void noticeWRM(AppWidgetInfo appWidgetInfo) {
        try {
            JSONObject param = new JSONObject();
            param.put("userName", appWidgetInfo.getCreateUserName());
            param.put("from", "PAC");
            param.put("widgetId", appWidgetInfo.getWidgetInitId());
            param.put("appType", 1);//0代表PAC 1代表BSC
            String result = HttpClientUtil.postBase64(addRate, param.toJSONString());
            if (logger.isDebugEnabled()) {
                logger.debug("noticeWRM param is : " + param.toJSONString());
                logger.debug("noticeWRM result is : " + result);
            }
            if (!StringUtils.isNullOrEmpty(result) && JSONObject.parseObject(result).getBoolean(ResultStringKey
                    .SUCCESS)) {
                return;
            } else {
                super.saveLog(DictionaryEnums.ERROR_MSG.getDictionaryCode(), DictionaryEnums.BUSINESS_MICO_APP
                                .getDictionaryCode(),
                        "通知控件库 : " + appWidgetInfo.getWidgetName() + " 被使用失败！");
            }
        } catch (Exception e) {
            super.saveLog(DictionaryEnums.ERROR_MSG.getDictionaryCode(), DictionaryEnums.BUSINESS_MICO_APP
                            .getDictionaryCode(),
                    "通知控件库 : " + appWidgetInfo.getWidgetName() + " 被使用失败！");
            logger.error("noticeWRM error " + e);
        }
    }

    private JSONObject buildWidget(String path, String doc, AppWidgetInfo appWidgetInfo, String widgetName) {
        JSONObject result = new JSONObject();
        String folderName = getFolderName(appWidgetInfo);
        Integer widgetId = appWidgetInfo.getWidgetId();
        //widget init folder
        String folderPath = path + WIDGET_INIT_FOLDER + folderName;
        //HTML tag to be saved as a file
        String fileName = folderPath
                + File.separator
                + PREFIX_FOR_WIDGET
                + widgetId
                + HTML_EXTENSION;
        String zipFileName = folderPath + File.separator + PREFIX_FOR_WIDGET + widgetId + ".zip";
        try (OutputStreamWriter fw = new OutputStreamWriter(
                new FileOutputStream(fileName), "UTF-8")) {
            String newDoc = "<!DOCTYPE html>\n<html>\n" + doc + "\n</html>";
            fw.write(newDoc);
            fw.close();
            //Compressed File
            if (zipFileUtils.zip(folderPath, zipFileName, false)) {
                FastDfsUtil fast = new FastDfsUtil();
                String str = fast.uploadFile(zipFileName);
                if (!StringUtils.isNullOrEmpty(str)) {
                    //上传控件成功后  更新数据库
                    updateWidgetAfterUploadSuccess(widgetId, fileName, zipFileName, str, widgetName);
                } else {
                    updateWidgetAfterUploadError(widgetId, zipFileName);
                    logger.error("FastDFS upload error");
                    result.put(SUCCESS, false);
                    result.put(REASON, "上传失败、请重试 ！");
                    return result;
                }
            } else {
                result.put(SUCCESS, false);
                result.put(REASON, "服务器异常，请稍后重试！");
                return result;
            }
        } catch (Exception e) {
            logger.error("addWidget error " + e);
            result.put(SUCCESS, false);
            result.put(REASON, "服务器异常，请稍后重试！");
            updateWidgetAfterUploadError(widgetId, zipFileName);
            return result;
        }
        result.put(SUCCESS, true);
        return result;
    }

    private void updateWidgetAfterUploadError(Integer widgetId, String zipFileName) {
        //删除之前解压的控件资源  并删除数据库中的记录
        File zipFile = new File(zipFileName);
        if (zipFile.exists() && zipFile.delete()) {
            //删除数据库记录
            widgetService.deleteWidget(widgetId);
        }
    }

    private void updateWidgetAfterUploadSuccess(Integer widgetId, String fileName, String zipFileName, String str,
                                                String widgetName) {
        //控件的资源文件压缩并上传完毕后  就开始更新此前新增在数据库信息
        Map<String, Object> param = new HashMap<>();
        param.put(ResultStringKey.WIDGET_ID, widgetId);
        param.put("fileName", str);
        if (StringUtils.isNullOrEmpty(widgetName)) {
            param.put("widgetName", widgetName);
        }
        widgetService.updateWidget(param);
        new File(zipFileName).delete();
        new File(fileName).delete();
    }
//    private byte[] wrmDownload(String userName, Integer wrmWidgetId) {
//        //从控件库下载文件（byte[])
//        JSONObject paramJson = new JSONObject();
//        paramJson.put(ResultStringKey.WIDGET_ID, wrmWidgetId);
//        paramJson.put(ResultStringKey.APP_ID, projectAppId);
//        paramJson.put(ResultStringKey.USER_NAME, userName);
//        paramJson.put(ResultStringKey.USER_ID, "2");
//        paramJson.put("appType", 0);
//        String resultData = HttpClientUtil.post(downloadWidget, paramJson.toJSONString());
//        //从控件库获取到的文件流保存到文件
//        try {
//            String data = new String(Base64.decodeBase64(resultData), "utf-8");
//            JSONObject jsonObject = JSON.parseObject(data);
//            Base64 base64 = new Base64();
//            return base64.decode(jsonObject.getJSONObject("data").getString("fileByte"));
//        } catch (UnsupportedEncodingException e) {
//            logger.error("UnsupportedEncodingException  the " + resultData + "  error ", e);
//            return "".getBytes();
//        }
//    }

    private boolean getExistingHtmlFile(String widgetId, String pathWidget, JSONObject result, AppWidgetInfo
            appWidgetInfo) {
        String unzipFolderName = appWidgetInfo.getFolderName();
        File unzipFolderFile = new File(pathWidget + unzipFolderName);
        if (unzipFolderFile.exists()) {
            result.put(SUCCESS, true);
            String htmlFile = unzipFolderName + File.separator + PREFIX_FOR_WIDGET + widgetId + HTML_EXTENSION;
            result.put(ResultStringKey.DATA, htmlFile);
            return true;
        }
        return false;
    }

    @Override
    public JSONObject updateWidget(String path, String widgetName, String doc, String widgetId, String userName) {
        Map<String, Object> param = new HashMap<>();
        param.put("widgetId", Integer.parseInt(widgetId));
        param.put("fastDFSUrl", ConfigGetPropertyUtil.get("fastDFS.http.url"));
        AppWidgetInfo appWidgetInfo = widgetService.getAppWidgetById(param);
        super.saveLog(DictionaryEnums.ACTION_UPDATE.getDictionaryCode(), DictionaryEnums.BUSINESS_MICO_APP
                        .getDictionaryCode(),
                "更新控件 : " + appWidgetInfo.getWidgetName());
        appWidgetInfo.setCreateUserName(userName);
        String folderName = appWidgetInfo.getFolderName();
        //更新 新版本控件
        JSONObject result = buildWidget(path, doc, appWidgetInfo, widgetName);
        if (Boolean.parseBoolean(result.getString(ResultStringKey.SUCCESS))) {
            //删除上一个版本的解压下来的文件夹
            File previousVersionFolder = new File(path + WIDGET_SAVE_FOLDER + folderName);
            if (previousVersionFolder.exists()) {
                zipFileUtils.deleteFileByPath(previousVersionFolder);
            }
            //在文件服务器上面 删除对应的控件
            FastDfsUtil fast = new FastDfsUtil();
            fast.deleteFile(appWidgetInfo.getFileName());
        }
        return result;
    }

    /**
     * 压缩文件：从文件服务器上面下载之前上传的控件资源文件（zip格式）
     * 并解压到widgetFrame/app/yyyyMMddHHmmss ，返回yyyyMMddHHmmss/widget_(ID).html
     *
     * @param widgetId 控件ID
     * @param path     项目的资源文件路径
     * @return JSONObject
     */
    @Override
    public JSONObject unzipWidgetFile(String widgetId, String path) {
        JSONObject result = new JSONObject();

        Map<String, Object> paramWidget = new HashMap<>();
        paramWidget.put("widgetId", Integer.parseInt(widgetId));
        paramWidget.put("fastDFSUrl", ConfigGetPropertyUtil.get("fastDFS.http.url"));

        AppWidgetInfo appWidgetInfo = widgetService.getAppWidgetById(paramWidget);
//        super.saveLog("下载控件", "下载控件 : " + appWidgetInfo.getWidgetName());
        if (null == appWidgetInfo) {
            result.put(SUCCESS, false);
            result.put(REASON, "此控件已被删除，请重试！");
            return result;
        }
        String pathWidget = path + WIDGET_SAVE_FOLDER;
        String widgetZipFile = pathWidget + PREFIX_FOR_WIDGET
                + widgetId + ".zip";
        try {
            //获取之前解压过的控件
            if (getExistingHtmlFile(widgetId, pathWidget, result, appWidgetInfo))
                return result;
            //之前未解压过  ：
            String dfsFileId = appWidgetInfo.getFileName();
            if (StringUtils.isNullOrEmpty(dfsFileId)) {
                result.put(SUCCESS, false);
                result.put(REASON, "数据有问题，请重试！");
                return result;
            }
            FastDfsUtil fastDfsUtil = new FastDfsUtil();
            String folderName = StringUtils.isNullOrEmpty(appWidgetInfo.getFolderName()) ? DateUtil.dateToStrSS(new
                    Date()) : appWidgetInfo.getFolderName();
            String targetWidgetFolder = pathWidget + folderName;
            if (fastDfsUtil.downloadFile(dfsFileId, widgetZipFile)
                    && zipFileUtils.unzip(widgetZipFile, targetWidgetFolder, true)) {
                File widgetHtmlFile = new File(targetWidgetFolder + File.separator + PREFIX_FOR_WIDGET + widgetId +
                        HTML_EXTENSION);
                if (!widgetHtmlFile.exists()) {
                    logger.error("The resource file for the widget is corrupted.");
                    result.put(SUCCESS, false);
                    result.put(REASON, "控件的资源文件已损坏");
                    return result;
                }
                result.put(SUCCESS, true);
                String htmlFile = folderName + File.separator + PREFIX_FOR_WIDGET + widgetId + HTML_EXTENSION;
                result.put(ResultStringKey.DATA, htmlFile);
                //将此次下载到本的目录 更新到数据库中，方便下次更新控件版本时候  删除此文件夹
                Map<String, Object> param = new HashMap<>();
                param.put(FOLDER_NAME, folderName);
                param.put(ResultStringKey.WIDGET_ID, widgetId);
                widgetService.updateWidget(param);
            } else {
                result.put(SUCCESS, false);
                result.put(REASON, REASON_RESULT);
            }
        } catch (Exception e) {
            logger.error(" unzipWidgetFile error " + e);
            result.put(SUCCESS, false);
            result.put(REASON, REASON_RESULT);
        }
        return result;
    }

    /**
     * 获取控件初始化表中的  文件夹名称
     *
     * @param appWidgetInfo 控件信息实体类
     * @return 返回文件夹名称
     */

    private String getFolderName(AppWidgetInfo appWidgetInfo) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", appWidgetInfo.getWidgetInitId());
        List<Map<String, Object>> widgetInitInfo = widgetService.getWidgetInitList(param);
        return String.valueOf(widgetInitInfo.get(0).get(FOLDER_NAME));
    }

    @Override
    public List<AppWidgetInfo> getAppWidgetList(Map<String, Object> param, String userName) {
        param.put("createId", userName);
        PageUtil.dealPage(param);
        {
            Map<String, Object> marParam = new HashMap<>();
            marParam.put("createByName", userName);
            marParam.put("mAppSource", "1");
            marParam.put("firstCategory", param.get("typeCode1"));
            marParam.put("businessType", param.get("typeCode2"));
//            marParam.put("pageNum", param.get("pageNum"));
//            marParam.put("pageSize", param.get("pageSize"));
            if (logger.isDebugEnabled()) {
                logger.debug("query mapp from mar ,param == " + marParam.entrySet());
            }
            marParam.put("appType", 1);//0代表PAC 1代表BSC
            String resultStr = HttpClientUtil.postBase64(queryWidget, JsonUtil.toJSon(marParam));
            System.out.println("result=====" + resultStr);
            JSONObject resultJson = JSONObject.parseObject(resultStr);
            if (logger.isDebugEnabled()) {
                logger.debug("query mapp from mar ,result == " + resultJson.toJSONString());
            }
            if (resultJson.getBoolean("success")) {
                List<JSONObject> list = (List<JSONObject>) resultJson.get("data");

                //1. get all the lv1 type id and name
                Map<String, String> typeStanderdMap = new HashMap<String, String>();
                for (JSONObject o : list) {
                    typeStanderdMap.put(o.getString("firstCategory")
                            , o.getString("firstCategoryName"));
                }
                //2.每条数据对应的分类汇聚
                Map<String, String> typeDataMap = new HashMap<String, String>();
                Map<String, String> typeDataMap2 = new HashMap<String, String>();
                for (JSONObject o : list) {
                    typeDataMap.put(o.getString("mAppId")
                            , o.getString("firstCategory"));
                }
                for (JSONObject o : list) {
                    typeDataMap2.put(o.getString("mAppId")
                            , o.getString("businessType"));//这个鬼地方，特么改了4次！！！！
                }
                /////20171128added 获得微应用图片 start
                Map<String, String> mappImgDataMap = new HashMap<String, String>();
                for (JSONObject o : list) {
                    JSONArray mAppImg = o.getJSONArray("mAppImg");
                    if (mAppImg != null && mAppImg.size() > 0) {
                        JSONObject oo = mAppImg.getJSONObject(0);
                        mappImgDataMap.put(o.getString("mAppId")
                                , oo.getString("mAppImg"));
                    }
                }
                /////20171128added 获得微应用图片  over
                //3. set中是不重复的一级分类id
                Set<String> typeSet = new HashSet<String>();
                List<Map<String, String>> typeList = new ArrayList<Map<String, String>>();
                for (JSONObject o : list) {
                    if (!typeSet.contains(o.getString("firstCategory"))) {
                        typeSet.add(o.getString("firstCategory"));
                    }
                }
                //4. compose data
                JSONArray resultArray = new JSONArray();//最终结果集
                Iterator<String> it = typeSet.iterator();
                while (it.hasNext()) {
                    String typeId = it.next();
//                    System.out.println(typeId);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("typeCode1", typeId);//lv1 type
                    jsonObject.put("typeName1", typeStanderdMap.get(typeId));
//                    jsonObject.put("data", null);

                    List<JSONObject> littleList = new ArrayList<JSONObject>();
                    for (JSONObject o : list) {
                        if (typeId.equals(o.getString("firstCategory"))) {
                            littleList.add(o);
                        }
                    }
                    jsonObject.put("data", littleList);
                    resultArray.add(jsonObject);
                }
                List<String> widgetIdList = new ArrayList<>();
                for (JSONObject jsonObject : list) {
                    widgetIdList.add(jsonObject.getString("mAppId"));
                }
                if (widgetIdList.isEmpty()) {
                    return null;
                }
                param.put("widgetIdList", widgetIdList);
                List<AppWidgetInfo> res = widgetService.getAppWidgetListByIdList(param);
                for (AppWidgetInfo appWidgetInfo : res) {
                    appWidgetInfo.setTypeCode1(Integer.valueOf(typeDataMap.get(String.valueOf(appWidgetInfo
                            .getWidgetId()))));
                    appWidgetInfo.setTypeCode2(Integer.valueOf(typeDataMap2.get(String.valueOf(appWidgetInfo
                            .getWidgetId()))));
                    appWidgetInfo.setTypeName1(typeStanderdMap.get(String.valueOf(appWidgetInfo.getTypeCode1())));
                    String imgUrl = mappImgDataMap.get(String.valueOf(appWidgetInfo.getWidgetId()));
                    if (!StringUtils.isNullOrEmpty(imgUrl)) {
                        imgUrl = imgUrl.startsWith("http") ? imgUrl : ConfigGetPropertyUtil.get("fastDFS.http.url") +
                                imgUrl;
                    }
                    appWidgetInfo.setAppImg(imgUrl);
                }
                return res;
            }
            return null;
        }
//        return widgetService.getAppWidgetList(param);
    }

    @Override
    public int getCountByAppName(Map<String, Object> param) {
        return widgetService.getCountByAppName(param);
    }

    @Override
    public void deleteWidget(String widgetId, String path, String resource) {
        //删除控件之前;先删除之前解压下来的文件夹

        Map<String, Object> paramWidget = new HashMap<>();
        paramWidget.put("widgetId", Integer.parseInt(widgetId));
        paramWidget.put("fastDFSUrl", ConfigGetPropertyUtil.get("fastDFS.http.url"));

        AppWidgetInfo appWidgetInfo = widgetService.getAppWidgetById(paramWidget);
        super.saveLog(DictionaryEnums.ACTION_DELETE.getDictionaryCode(), DictionaryEnums.BUSINESS_MICO_APP
                        .getDictionaryCode(),
                resource + "删除微应用 : " + appWidgetInfo.getWidgetName());
        File previousVersionFolder = new File(path + WIDGET_SAVE_FOLDER + appWidgetInfo.getFolderName());
        if (previousVersionFolder.exists()) {
            zipFileUtils.deleteFileByPath(previousVersionFolder);
        }
        //删除数据库记录
        widgetService.deleteWidget(Integer.parseInt(widgetId));
        //通知MAR 已删除
        Map<String, String> param = new HashMap<>();
        param.put("mAppId", widgetId);
        param.put("appType", "1");//0代表PAC 1代表BSC
        String result = HttpClientUtil.getBase64Data(deleteMApp, param);
        if (logger.isDebugEnabled()) {
            logger.debug("deleteMApp result is === " + result);
        }
        JSONObject resultJson = JSONObject.parseObject(result);
        if (!resultJson.getBoolean(ResultStringKey.SUCCESS)) {
            logger.error("MAR deleteMApp error ; WidgetName is " + appWidgetInfo.getWidgetName());
            throw new RuntimeException("MAR deleteMApp error");
        }
        //删除文件服务器上的文件
        FastDfsUtil fast = new FastDfsUtil();
        fast.deleteFile(appWidgetInfo.getFileName());
    }

    @Override
    public JSONObject checkRelation(String mappId, String type) {
        JSONObject result = new JSONObject();
        try {
            AppRelationInfo relationInfo = new AppRelationInfo();
            relationInfo.setType(type);
            if ("widget".equals(type.toLowerCase())) {
                relationInfo.setWidgetId(mappId);
            } else {
                relationInfo.setPageId(mappId);
            }
            List<AppRelationInfo> relationInfoList = widgetService.checkRelation(relationInfo);
            if (!relationInfoList.isEmpty()) {
                result.put(SUCCESS, true);
                result.put("check", true);
                result.put(ResultStringKey.DATA, relationInfoList);
            } else {
                result.put(SUCCESS, true);
                result.put("check", false);
                result.put(ResultStringKey.DATA, relationInfoList);
            }
        } catch (Exception e) {
            logger.error("checkRelation is error msg == " + e);
            result.put(SUCCESS, false);
            result.put(REASON, "服务器异常，请重试！");
            result.put(ResultStringKey.DATA, new ArrayList<>());
        }
        return result;
    }

    @Override
    public void updateStatus(Map<String, Object> param, JSONObject jsonObject) {
        Map<String, Object> paramWidget = new HashMap<>();
        paramWidget.put("widgetId", Integer.parseInt(param.get(ResultStringKey.WIDGET_ID).toString()));
        paramWidget.put("fastDFSUrl", ConfigGetPropertyUtil.get("fastDFS.http.url"));

        AppWidgetInfo appWidgetInfo = widgetService.getAppWidgetById(paramWidget);
        if (jsonObject == null) {
            super.saveLog(DictionaryEnums.ACTION_UPDATE.getDictionaryCode(), DictionaryEnums.BUSINESS_MICO_APP
                            .getDictionaryCode(),
                    "将  " + appWidgetInfo.getWidgetName() + "从 " + appWidgetInfo.getStatus() + " 更新到 : " + param.get
                            ("status"));
        } else {
            String userName = jsonObject.getString("userName");
            SessionUserInfo sessionUserInfo = null;
            if (!StringUtils.isNullOrEmpty(userName)) {
                sessionUserInfo = userRestService.getUserInfoByLoginName(userName);
            }
            super.saveLog(DictionaryEnums.ACTION_UPDATE.getDictionaryCode(), DictionaryEnums.BUSINESS_MICO_APP
                            .getDictionaryCode(),
                    "将  " + appWidgetInfo.getWidgetName() + "从 "
                            + appWidgetInfo.getStatus() + " 更新到 : " + param.get("status"),
                    sessionUserInfo);
        }

        widgetService.updateStatus(param);
    }

    @Override
    public JSONObject getWidgetInitList(JSONObject paramJson) {
        String dateStrStart = DateUtil.dateToStrLong(new Date());
        paramJson.put("appType", 1);//0代表PAC 1代表BSC
        String str = HttpClientUtil.postBase64(getWidgetListUrl, paramJson.toJSONString());
        String dateStrEnd = DateUtil.dateToStrLong(new Date());
        if (logger.isDebugEnabled()) {
            logger.debug("getWidgetInitList start time : " + dateStrStart
                    + " ; end time : " + dateStrEnd + " ; total time used : "
                    + DateUtil.getDistanceTime(dateStrStart, dateStrEnd));
        }
        return JSONObject.parseObject(str);
    }

    @Override
    public int insertWidgetInit(AppWidgetInitInfo appWidgetInitInfo) {
        return widgetService.insertWidgetInitInfo(appWidgetInitInfo);
    }

    /**
     * 初始化待编辑的控件 --  下载到项目内进行解压以供前台编辑（返回可编辑的html路径）
     *
     * @param wrmId    控件初始化表的ID
     * @param path     项目资源路径
     * @param userName 用户名
     * @return json
     */
    @Override
    public JSONObject initWidget(Integer wrmId, String path, String userName, String version, String widgetName,
                                 String dfsUrl) {
        super.saveLog(DictionaryEnums.ACTION_VIEW.getDictionaryCode(), DictionaryEnums.BUSINESS_MICO_APP
                        .getDictionaryCode(),
                "查看控件 : " + widgetName);
        JSONObject result = new JSONObject();
        AppWidgetInitInfo appWidgetInitInfo = widgetService.selectWidgetInitInfoById(wrmId);
        //空 表示此待编辑的控件暂时没有下载过
        if (null == appWidgetInitInfo) {
            String folderName = DateUtil.dateToStrSS(new Date());
            String htmlName = downloadWidgetAndGetHtmlName(wrmId, path, folderName, dfsUrl);
            //insert DB
            AppWidgetInitInfo initInfo = new AppWidgetInitInfo();
            initInfo.setWidgetName(widgetName);
            initInfo.setWrmId(wrmId);
            initInfo.setFolderName(folderName);
            initInfo.setHtmlName(htmlName);
            initInfo.setPath("init");
            initInfo.setStatus(1);
            initInfo.setCreateTime(new Date());
            initInfo.setCreateUserID(userName);
            initInfo.setVersion(version.trim());
            widgetService.insertWidgetInitInfo(initInfo);
            result.put(ResultStringKey.SUCCESS, true);
            result.put(ResultStringKey.DATA, "init" + File.separator + folderName + File.separator + htmlName);
            return result;
        } else if (version.trim().equals(appWidgetInitInfo.getVersion())) {//版本不一样  则是已升级、则需重新下载控件
            //先判断之前下载的文件是否存在？  不存在就再次下载
            String oldFolderName = appWidgetInitInfo.getFolderName();
            File oldFolderFile = new File(path + WIDGET_INIT_FOLDER + oldFolderName);
            if (oldFolderFile.exists()) {
                result.put(ResultStringKey.SUCCESS, true);
                result.put(ResultStringKey.DATA,
                        "init" + File.separator + oldFolderName + File.separator + appWidgetInitInfo.getHtmlName());
                return result;
            } else {
                downloadWidgetAndGetHtmlName(wrmId, path, oldFolderName, dfsUrl);
                result.put(ResultStringKey.SUCCESS, true);
                result.put(ResultStringKey.DATA, "init" + File.separator + oldFolderName +
                        File.separator + appWidgetInitInfo.getHtmlName());
                return result;
            }
        } else {
            //删除旧版本控件
            String oldFolderName = appWidgetInitInfo.getFolderName();
            File oldFolderFile = new File(path + WIDGET_INIT_FOLDER + oldFolderName);
            if (oldFolderFile.exists()) {
                zipFileUtils.deleteFileByPath(oldFolderFile);
            }
            //重新下载控件
            String htmlName = downloadWidgetAndGetHtmlName(wrmId, path, oldFolderName, dfsUrl);
            AppWidgetInitInfo updateInfoForVersion = new AppWidgetInitInfo();
            updateInfoForVersion.setVersion(version.trim());
            updateInfoForVersion.setWrmId(wrmId);
            updateInfoForVersion.setHtmlName(htmlName);
            widgetService.updateWidgetInitInfoById(updateInfoForVersion);
            result.put(ResultStringKey.SUCCESS, true);
            result.put(ResultStringKey.DATA, "init" + File.separator + appWidgetInitInfo.getFolderName() + File
                    .separator + htmlName);
            return result;
        }
    }

    private String downloadWidgetAndGetHtmlName(Integer wrmId, String path, String folderName,
                                                String dfsUrl) {

//        byte[] resultDownload = wrmDownload(userName, wrmId);
        String widgetZipFile = path + WIDGET_INIT_FOLDER + PREFIX_FOR_WIDGET + wrmId + ".zip";
        FastDfsUtil fastDfsUtil = new FastDfsUtil();
        fastDfsUtil.downloadFile(dfsUrl, widgetZipFile);
//        byte2File(resultDownload, widgetZipFile);
        //解压
        String targetWidgetFolder = path
                + WIDGET_INIT_FOLDER
                + folderName;
        if (zipFileUtils.unzip(widgetZipFile, targetWidgetFolder, true)) {
            //获取html名字
            return getHtmlName(targetWidgetFolder).replace(targetWidgetFolder + File.separator, "");
        }
        return "";
    }

    /**
     * 删除 初始化的控件
     *
     * @param widgetInitId 初始化控件Id
     */

    @Override
    public void deleteInitWidget(Integer widgetInitId) {
        widgetService.deleteWidgetInitInfoById(widgetInitId);
    }

    /*
    获取控件zip包中的html文件
    Richard.w

     */
    private static String getHtmlName(String fileStr) {
        String[] files;
        File file = new File(fileStr);
        if (!StringUtils.isNullOrEmpty(fileStr)) {
            files = file.list();
        } else {
            return "";
        }
        if (files != null && files.length > 0) {
            for (String newFile : files) {
//                String bol = getHtmlName(file + File.separator + newFile);
                if (newFile.endsWith(HTML_EXTENSION)) {
                    return newFile;
                }
            }
        }
        return "";
    }

//    private static void byte2File(byte[] buf, String targetFile) {
//        File file = new File(targetFile);
//        File filePath = new File(file.getParent());
//        if (!filePath.exists()) {
//            filePath.mkdirs();
//        }
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            fos.write(buf);
//        } catch (Exception e) {
//            logger.error("byte2File error ", e);
//        }
//    }

    @Override
    public String queryDMMPDR(String areaType, String serverId, int pageSize, int pageNum) {
        String str = null;
        if ((areaType == null || "".equals(areaType)) &&
                (serverId == null || "".equals(serverId))) {
            //get area
            str = HttpClientUtil.postBase64(queryAreaTypeUrl, "{}");
//            JSONObject jsonObject=JSONObject.parseObject(str);
//            if(jsonObject!=null &&
//                    "true".equals(jsonObject.getString("success"))){
//                JSONArray data=jsonObject.getJSONArray("data");
//                if(data!=null && data.size()>0){
//                    for(int i=0; i<data.size(); i++){
//                        JSONObject element=data.getJSONObject(i);
//                        DmmpdrAreaInfo dmmpdrAreaInfo=new DmmpdrAreaInfo();
//                        dmmpdrAreaInfo.setAreaname(element.getString("areaName"));
//                        dmmpdrAreaInfo.setId(element.getString("id"));
//                        dmmpdrAreaInfo.setCategory(element.getString("category"));
//                    }
//                }
//            }
        } else if (areaType != null && !"".equals(areaType)) {
            //get db
            JSONObject param = new JSONObject();
            param.put("areaType", areaType);
            param.put("pageSize", pageSize);
            param.put("pageNum", pageNum);
            str = HttpClientUtil.postBase64(queryDBUrl, param.toJSONString());
        } else if ((areaType == null || "".equals(areaType)) &&
                (serverId != null && !"".equals(serverId))) {
            //get tables
            JSONObject param = new JSONObject();
            param.put("serverId", serverId);
            param.put("pageSize", pageSize);
            param.put("pageNum", pageNum);
            str = HttpClientUtil.postBase64(queryTablesUrl, param.toJSONString());
            logger.debug("str: " + str);
        }
        return str;
    }

    /**
     * 灵活查询数据库
     *
     * @param userName
     * @param pwd
     * @param ip
     * @param port
     * @param dbName
     * @return
     */
    @Override
    public JSONObject queryDB(String dbId, String sql, int targetPage,
                              Map<String, String> nbMap) {
        JSONObject dbInfoNew = new JSONObject();
        //调用dmmpdr接口获得数据库信息
        JSONObject paramJson = new JSONObject();
        paramJson.put("id", dbId);
        String dataBaseInfoJson =
                HttpClientUtil.postBase64(queryDBUrl, paramJson.toJSONString());
        JSONObject dataBaseInfo = JSONObject.parseObject(dataBaseInfoJson);
        JSONObject data = dataBaseInfo.getJSONObject("data");
        JSONArray list = new JSONArray();
        if (data.get("list") != null) {
            list = data.getJSONArray("list");
        }
        if (list.size() > 0) {
            dbInfoNew = (JSONObject) list.get(0);
        }

        logger.debug("dbId == " + dbId + " ; sql = " + sql + " ; dbMap = "
                + " ; targetPage = " + targetPage);
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        String userName = null, pwd = null, ip = null, port = null, dbName = null;

        if (dbInfoNew != null && dbInfoNew.size() > 0) {
            userName = dbInfoNew.get("loginName") + "";
            pwd = dbInfoNew.get("loginPwd") + "";
            ip = dbInfoNew.get("ipAddr") + "";
            port = dbInfoNew.get("port") + "";
            dbName = dbInfoNew.get("instanceName") + "";
        } else {
            logger.error("queryDb error 未获取到数据库链接信息!");
            result.put("success", false);
            result.put("reason", "未获取到数据库链接信息!");
            return result;
        }
        Connection connection = null;
        String dbType = "";
        if (nbMap != null && nbMap.get("dbType") != null) {
            dbType = String.valueOf(nbMap.get("dbType"));
        }
//    	dbType="mysql";
        logger.debug("dbType is: " + dbType);
        if ("mysql".equalsIgnoreCase(dbType.trim())) {
            //for mysql
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(
                        "jdbc:mysql://" + ip + ":" + port + "/" + dbName,
                        userName, pwd);
                if (!connection.isClosed()) {
                    logger.debug("数据库连接成功！");//验证是否连接成功
                }
                String tableName = null;
                if (sql != null) {
                    String[] sqlArray = sql.split(" ");
                    for (int i = 0; i < sqlArray.length; i++) {
                        String s = sqlArray[i];
                        if (s.equalsIgnoreCase("from")) {
                            tableName = sqlArray[i + 1];
                            break;
                        }
                    }
                }

                List<Map<String, String>> commentList = DBUtil
                        .getColumnCommentByTableName(connection, tableName);
                JSONArray commentLists = new JSONArray();
                for (Map<String, String> m : commentList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("field", m.get("field"));
                    jsonObject.put("comment", m.get("comment"));
                    commentLists.add(jsonObject);
                }
                Statement statement = connection.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                //查询数据
                int numEveryPage = 10;
                ResultSet rs = statement.executeQuery(sql);
                rs.last();
                int totalNum = rs.getRow();
                result.put("totalNum", totalNum);//总行数
                double tatalPageNum = Math.ceil(
                        Double.parseDouble(totalNum + "") / Double.parseDouble(numEveryPage + ""));//总页数
                result.put("totalPageNum", tatalPageNum);
                ///////////////
                ResultSet rsQuery = statement.executeQuery(sql + " limit " + ((targetPage - 1) * numEveryPage) + " , " +
                        "" + numEveryPage);
                // 获取列数
                ResultSetMetaData metaData = rsQuery.getMetaData();
                int columnCount = metaData.getColumnCount();
                // 遍历ResultSet中的每条数据
                while (rsQuery.next()) {
                    JSONObject jsonObj = new JSONObject();
                    // 遍历每一列
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnLabel(i);
                        String value = rsQuery.getString(columnName);
                        System.out.println(columnName + " -- " + value);
                        jsonObj.put(columnName, value);
                    }
                    array.add(jsonObj);
                }
                rs.close();
                rsQuery.close();
                result.put("data", array);
                result.put("commentList", commentLists);
                result.put("success", true);
                result.put("message", "success");
                logger.debug(result.toJSONString());
            } catch (Exception e) {
                logger.error("queryDb error " + e);
                result.put("success", false);
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                        connection = null;
                    } catch (Exception e) {
                        logger.error("queryDb error finally " + e);
                        result.put("success", false);
                    }
                }
            }
        }
        if ("hana".equalsIgnoreCase(dbType.trim())) {
            try {

                Class.forName("com.sap.db.jdbc.Driver");
                logger.debug("in business, ip: " + ip + ". port:" + port + " .dbName:" + dbName);
                String url = "jdbc:sap://" + ip + ":" + port + "/" + dbName +
                        "?reconnect=true";
                logger.debug("in business, url is:" + url);
                HANAUtil hanaUtil = new HANAUtil();
                result = hanaUtil
                        .queryHANA(url, userName, pwd, sql, null, targetPage);
                logger.debug("res is: " + result.toJSONString());
            } catch (Exception e) {
                logger.error("queryDB error " + e);
            }
        }
        return result;
    }

    @Override
    public ResponseVo queryCategory(String categoryType) {
        try {
            Map map = new HashMap();
            map.put("categoryType", categoryType);
            String json = objectMapper.writeValueAsString(map);
            String data = HttpClientUtil.post(queryCategory, json);
            logger.info("response data: " + data);
// objectMapper
// .configure(
// org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
// false);
            ResponseVo rv = JsonUtil.readBase64Value(data, ResponseVo.class);
            if (null == rv) rv = ResponseVoUtil.fail("获取类别失败，请重试！", "获取失败，请重试！");
            return rv;
        } catch (Exception e) {
            logger.error("ArcmServiceImpl queryCategory error", e);
        }

        return ResponseVoUtil.fail("获取失败，请重试！", "获取失败，请重试！");
    }

}