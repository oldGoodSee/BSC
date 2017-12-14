package com.bocom.controller.pac;

import com.alibaba.fastjson.JSONObject;
import com.bocom.domain.pac.PackagingInfo;
import com.bocom.service.pac.AppBaseInfoService;
import com.bocom.service.pac.CoreService;
import com.bocom.util.*;

import org.apache.tools.ant.taskdefs.condition.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/manager/history")
public class HistoryController {

    // log
    private static Logger logger = LoggerFactory
            .getLogger(HistoryController.class);

    @Resource
    private AppBaseInfoService appBaseInfoService;

    @Resource
    private CoreService coreService;

    private static final String RESULT = "result";

    private static final String SUCCESS = "success";

    private static final String ERROR = "error";

    private static final String REASON = "reason";


    /**
     * 获取卸载目录下面所有的应用记录
     *
     * @return JSONObject
     */
    @ResponseBody
    @RequestMapping("/getAppHistory")
    public JSONObject getAppHistory(HttpSession session, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        try {
            String userId = UserUtils.getUserId(session);
            if ("".equals(userId)) {
                result.put(RESULT, ERROR);
                result.put(REASON, "未获取到用户信息，请重试！");
                return result;
            }
            //获取备份目录
            String backUpFilePath = ConfigGetPropertyUtil.get("configs.filePath.url") + "backUp";
            List<String> appIdList = new ArrayList<>();
            //获取appId
            recursion(backUpFilePath, appIdList);
            List<Map<String, Object>> historyResult;
            if (!appIdList.isEmpty()) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("appId", appIdList);
                param.put("userId", userId);
                //根据appId 从快照表中读取appName   historyResult结构为：appId ：xxx,appName ： xxxx
                historyResult =
                        appBaseInfoService.getAppHistory(param, userId, true);
                logger.debug("historyResult:" + historyResult);
                logger.debug("historyResult size():" + historyResult.size());

            } else {
                historyResult =
                        appBaseInfoService.getAppHistory(null, userId, false);
                result.put("data", historyResult);
            }
            getTemplateConfigList(request, result, historyResult);
            result.put(RESULT, SUCCESS);
        } catch (Exception e) {
            result.put(RESULT, ERROR);
            result.put(REASON, "系统错误，请联系管理员！");
            logger.error("getAppHistory  error" + e);
        }
        return result;
    }

    private void getTemplateConfigList(HttpServletRequest request, JSONObject result, List<Map<String, Object>>
            historyResult) {
        String saveasFilePath = ConfigGetPropertyUtil.get("saveas.filePath.url");
        logger.debug("saveasFilePath is: " + saveasFilePath);
        List<String> fileNameList = recursionForSave(saveasFilePath);
        logger.debug("fileNameList: " + fileNameList);
        logger.debug("fileNameList is null: " + (fileNameList == null));
        if (fileNameList != null) {
            logger.debug("fileNameList size(): " + fileNameList.size());
        }
        if (fileNameList != null && fileNameList.size() > 0) {
            for (String fileName : fileNameList) {
                Map<String, Object> fileMap = new HashMap<String, Object>();
                String newFileName = fileName.substring(0, fileName.indexOf("-configs_txt"));
                fileMap.put("appId", newFileName);
                fileMap.put("appName", newFileName);
                fileMap.put("version", "2.0");//标示出:属于BSC2.0版本
                fileMap.put("filePath",
                        request.getSession().getServletContext().getRealPath("/")
                                + "modelFrame-tab/configs_saveas/" + (fileName + "-configs_txt"));
                historyResult.add(fileMap);
            }
        }
        logger.debug("historyResult:" + historyResult);
        logger.debug("historyResult size(): " + historyResult.size());
        result.put("data", historyResult);
    }

    /**
     * 复用卸载记录，将已卸载的文件重新copy到工程目录中，进行复用
     *
     * @return JSONObject
     */
    @ResponseBody
    @RequestMapping("/copyHistoryRecord")
    public JSONObject copyHistoryRecord(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String appId = request.getParameter("appId");
        String newAppId = request.getParameter("newAppId");
        try {
            if (StringUtils.isNumber(appId)) {
                //appId 是数字，老的历史模板
                if (!StringUtils.isNullOrEmpty(appId) && StringUtils.isNumber(appId.trim())) {
                    PackagingInfo packagingInfo = new PackagingInfo();
                    packagingInfo.setAppid(appId);
                    //validate: app can be accessed
                    Integer status = coreService.getAppStatus(packagingInfo);
                    if (null != status && (1 == status || 0 == status)) {
                        String file = request.getSession().getServletContext().getRealPath("/")
                                + "modelFrame-tab/data/";
                        SshUtil.validateConfigFileToNewApp(appId, file, newAppId);
                        result.put(RESULT, SUCCESS);
                        result.put("data", appId);
                        return result;
                    }
                    //获取配置文件保存路径
                    String filePath = ConfigGetPropertyUtil.get("configs.filePath.url");
                    String fileName = "configs" + appId + "_txt";
                    String resourceFile = filePath + "backUp/" + fileName;
                    String targetFilePath = request.getSession().getServletContext().getRealPath("/") +
                            "modelFrame-tab/data/";
                    File file = new File(resourceFile);
                    if (!file.exists()) {
                        result.put(RESULT, ERROR);
                        result.put(REASON, "参数错误，请检查！");
                        return result;
                    }
                    // 复制文件
                    String resultStr = SshUtil.cpFile(targetFilePath + fileName, resourceFile, false);

                    File iframeFIle = new File(filePath + "backUp/" + "iframe_" + appId);
                    if (iframeFIle.exists()) {
                        resourceFile = filePath + "backUp/" + "iframe_" + appId;
                        FileUtil.copy(resourceFile, filePath + "iframe_" + newAppId);
                    }
                    result.put(RESULT, resultStr);
                    result.put("data", appId);
                } else {
                    result.put(RESULT, ERROR);
                    result.put(REASON, "参数错误，请检查！");
                }
            } else {
                //appId不是数字，2.0改造的另存为模板，appId是文件名
                if (!StringUtils.isNullOrEmpty(appId)) {
                    //获取配置文件保存路径
                    appId = URLDecoder.decode(appId, "UTF-8");
                    logger.debug("appId:" + appId + ", newappid:" + newAppId);
                    String filePath = ConfigGetPropertyUtil.get("saveas.filePath.url");
//                    String fileName = "configs" + appId + "_txt";
//                    String fileName = appId + "-configs_txt";
                    String fileName = appId + "-configs_txt";
                    String resourceFile = filePath + fileName;
                    String targetFilePath = request.getSession().getServletContext().getRealPath("/") +
                            "modelFrame-tab/data/";
                    String configsFilePath = request.getSession().getServletContext().getRealPath("/")
                            + "modelFrame-tab/configs_saveas/";
                    File file = new File(resourceFile);
                    if (!file.exists()) {
                        result.put(RESULT, ERROR);
                        result.put(REASON, "参数错误，请检查！");
                        return result;
                    }
                    File configPath = new File(configsFilePath);
                    if (!configPath.exists()) {
                        configPath.mkdirs();
                    }

                    // 复制文件
                    String resultStr = SshUtil.cpFile(configsFilePath + (fileName), resourceFile, false);

                    File iframeFIle = new File(filePath + appId + "/iframe");
                    if (iframeFIle.exists()) {
                        resourceFile = filePath + appId + "/iframe";
                        FileUtil.copy(resourceFile, ConfigGetPropertyUtil.get("configs.filePath.url") + "iframe_" + newAppId);
                    }
                    result.put(RESULT, resultStr);
                    result.put("data", appId);
                } else {
                    result.put(RESULT, ERROR);
                    result.put(REASON, "参数错误，请检查2！");
                }

            }

        } catch (Exception e) {
            result.put(RESULT, ERROR);
            result.put(REASON, "系统错误，请联系管理员！");
            logger.error(" copyHistoryRecord error " + e);
        }
        return result;
    }

    //遍历目录下的文件，获取获取文件名并截取出appId
    private static void recursion(String root, List<String> appIdList) {
        File file = new File(root);
        File[] subFileList = file.listFiles();
        if (null != subFileList) {
            for (File subFile : subFileList) {
                if (subFile.isDirectory()) {
                    recursion(subFile.getAbsolutePath(), appIdList);
                } else {
                    String appId = interceptAppId(subFile.getName());
                    if (!"".equals(appId)) {
                        appIdList.add(appId);
                    }
                }
            }
        }
    }

    //返回所有历史模板（用户另存为的，区分于上一个方法，返回的直接是文件的名字了，因为没有appid）
    private static List<String> recursionForSave(String root) {
        List<String> fileNameList = new ArrayList<String>();
        File file = new File(root);
        File[] subFileList = file.listFiles();
        if (null != subFileList) {
            for (File subFile : subFileList) {
                if (!subFile.isDirectory()) {
                    logger.debug("fileName: " + subFile.getName());
//                    recursion(subFile.getAbsolutePath(), appIdList);
                    String fileName = subFile.getName();
                    fileNameList.add(fileName);
                } else {
//                    String fileName = subFile.getName();
//                    fileNameList.add(fileName);
                }
            }
        }
        return fileNameList;
    }

    //截取出appId
    private static String interceptAppId(String str) {

        String appId = "";
        // 按指定模式在字符串查找
        String pattern = "(configs)(\\d*)(_txt)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(str);
        if (m.find()) {
            appId = m.group(2);
        }
        return appId;
    }

    public static void main(String[] args) {
        String fila = "ziwei_nice-configs_txt";
        String a = fila.substring(0, fila.indexOf("-configs_txt"));
        System.out.println(a);
    }
}
