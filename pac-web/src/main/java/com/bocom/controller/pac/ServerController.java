package com.bocom.controller.pac;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.business.WidgetBusiness;
import com.bocom.domain.pac.*;
import com.bocom.dto.UserInfoPAPDto;
import com.bocom.dto.pac.AppValidateDto;
import com.bocom.dto.session.SessionUserInfo;
import com.bocom.enums.DictionaryEnums;
import com.bocom.service.pac.AppBaseInfoService;
import com.bocom.service.pac.CoreService;
import com.bocom.service.pac.NoticeService;
import com.bocom.service.pac.ServerService;
import com.bocom.service.pac.impl.BaseServiceImpl;
import com.bocom.service.user.UserRestService;
import com.bocom.support.servlet.ResultStringKey;
import com.bocom.util.*;
import com.bocom.util.Behavior.Configs;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/server/interface")
public class ServerController extends BaseServiceImpl {

    // log
    private static Logger logger = LoggerFactory
            .getLogger(ServerController.class);

    @Resource
    private CoreService coreService;

    @Resource
    private AppBaseInfoService appBaseInfoService;

    @Resource
    private NoticeService noticeService;

    @Resource
    private WidgetBusiness widgetBusiness;

    @Resource
    private ServerService serverService;

    @Resource
    private UserRestService userRestService;

    private static final String APP_ID = "appId";

    private static final String SUCCESS = "success";

    private static final String USER_ID = "userId";

    private static final String MESSAGE = "message";


    @RequestMapping("/validateUser")
    @ResponseBody
    public String validateUser(HttpServletRequest request) {
        String userId = request.getParameter(USER_ID);
        String loginName = request.getParameter("userName");
        String appCode = request.getParameter("appCode");
        JSONObject data = new JSONObject();
        JSONObject result = new JSONObject();
        String str = "";
        try {
            data.put(USER_ID, userId);
            data.put("userName", loginName);
            data.put("policeName", "");
            data.put("policeCode", "");
            if (logger.isInfoEnabled()) {
                logger.info("有请求到达 ip:" + request.getRemoteAddr());
                logger.info(userId + "===" + loginName + "===" + appCode);
            }
            result.put(SUCCESS, true);
            result.put(MESSAGE, SUCCESS);
            AppValidateUser appValidateUser = serverService.getPowerDesc(appCode);
            if (appValidateUser != null) {
                str = appValidateUser.getPowerDesc();
            } else {
                str = "[{\n" +
                        "        \"roleName\": \"无\",\n" +
                        "        \"roleCode\": \"\",\n" +
                        "        \"roleOrgCode\": null,\n" +
                        "        \"roleOrgName\": \"\",\n" +
                        "        \"parentOrgName\": null,\n" +
                        "        \"parentOrgCode\": null\n" +
                        "      }]";
            }
        } catch (Exception e) {
            str = "[{\n" +
                    "        \"roleName\": \"无\",\n" +
                    "        \"roleCode\": \"\",\n" +
                    "        \"roleOrgCode\": null,\n" +
                    "        \"roleOrgName\": \"\",\n" +
                    "        \"parentOrgName\": null,\n" +
                    "        \"parentOrgCode\": null\n" +
                    "      }]";
            logger.error("serverController validateUser error " + e);
            data.put("orgRoleUserInfoList", JSONArray.parseArray(str));
            result.put("data", data);
            //            return result; // 未加密
            return Base64.encodeBase64String(result.toJSONString().getBytes());
        }
        data.put("orgRoleUserInfoList", JSONArray.parseArray(str));
        result.put("data", data);
        if (logger.isInfoEnabled()) {
            logger.info("result of validateUser is: " + result);
        }
        //        return result;// 未加密
        return Base64.encodeBase64String(result.toJSONString().getBytes());
    }

    /**
     * 提供给门户用的接口，关联到个人
     *
     * @param request
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/getPacList", method = RequestMethod.POST)
    @ResponseBody
    public String getPacList(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        try {
            PackagingInfo packagingInfo = new PackagingInfo();
            String userId = jsonObject.getString("userId");
            packagingInfo.setUserid(userId);
            if (null != userId) {
                packagingInfo.setUserid(userId);
                result = coreService.getPacList(packagingInfo);
                if (logger.isDebugEnabled()) {
                    logger.error("getPacList param" + packagingInfo);
                    logger.error("getPacList result" + result);
                }
            } else {
                result.put("data", null);
                result.put(SUCCESS, false);
                result.put(MESSAGE, "参数不合格！");
            }
        } catch (Exception e) {
            logger.error("getPacList error" + e);
            result.put("data", null);
            result.put(SUCCESS, false);
            result.put(MESSAGE, "服务器出错、请联系管理员 ！");
        }
        //        return result; 未加密
        return Base64.encodeBase64String(result.toJSONString().getBytes());
    }

    /**
     * 提供给门户用的接口查询正在运行的 组装应用
     *
     * @return JSONObject
     */
    @RequestMapping(value = "/getPacAppList", method = RequestMethod.POST)
    @ResponseBody
    public String getPacAppList(HttpServletRequest request, @RequestBody JSONObject jsonObject) {

        String appId = jsonObject.getString(APP_ID);
        if (logger.isDebugEnabled()) {
            logger.debug(" getPacAppList  请求到达>>>>>>>>>>>>> ip :  " + request.getRemoteAddr() + ";   appId = " + appId);
        }
        JSONObject result = null;
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("status", "1");
            if (null != appId && !"".equals(appId)) {
                param.put(APP_ID, appId);
            }
            HttpSession session = request.getSession();
            result = appBaseInfoService.getAppBaseInfoList(param);
        } catch (Exception e) {
            logger.error("getPacAppList error The message is :  " + e);
            result = new JSONObject();
            result.put("data", null);
            result.put(SUCCESS, false);
            result.put(MESSAGE, "error");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getPacAppList result is :  " + result.toJSONString());
        }

        //        return result;未加密
        return Base64.encodeBase64String(result.toJSONString().getBytes());
    }


    /**
     * 提供给微应用注册中心   删除微应用时候检验  是否被组装应用调用
     *
     * @param inputParam JSONObject
     * @return byte[]
     */
    @ResponseBody
    @RequestMapping(value = "/deleteMAppCheck", method = RequestMethod.POST)
    public byte[] deleteMAppCheck(@RequestBody JSONObject inputParam) {
        JSONObject result = new JSONObject();
        try {
            String type = inputParam.getString("type");
            String mappId = inputParam.getString("mappId");
            if (logger.isDebugEnabled()) {
                logger.debug("deleteMAppCheck param : type =" + type + " ; mappId = " + mappId);
            }
            if (!StringUtils.isNullOrEmpty(type, mappId)) {
                JSONObject jsonObject;
                switch (Integer.parseInt(type)) {
                    case 0:
                        jsonObject = widgetBusiness.checkRelation(mappId, "APP");
                        break;
                    case 1:
                        jsonObject = widgetBusiness.checkRelation(mappId, "WIDGET");
                        break;
                    default:
                        result.put(ResultStringKey.SUCCESS, false);
                        result.put(ResultStringKey.REASON, "入参有误!");
                        logger.error("deleteMAppCheck result == " + result.toJSONString());
                        return Base64.encodeBase64(result.toJSONString().getBytes());
                }
                List<AppRelationInfo> appRelationInfos = (List<AppRelationInfo>) jsonObject.get("data");
                if (appRelationInfos.isEmpty()) {
                    result.put(ResultStringKey.SUCCESS, true);
                    result.put(ResultStringKey.DATA, false);
                    result.put("explain", "没有被调用");
                } else {
                    result.put(ResultStringKey.SUCCESS, true);
                    result.put(ResultStringKey.DATA, true);
                    result.put("explain", "被组装应用所调用");
                }
            } else {
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.REASON, "入参有误!");
            }
        } catch (Exception e) {
            logger.error("ServerController deleteMAppCheck" + e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, "服务器异常，请重试 ！");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("ServerController deleteMAppCheck result === " + result.toJSONString());
        }
        return Base64.encodeBase64(result.toJSONString().getBytes());
    }

    /**
     * 提供给微应用注册中心   删除微应用时候检验  是否被组装应用调用
     *
     * @param inputParam JSONObject
     * @return byte[]
     */
    @ResponseBody
    @RequestMapping(value = "/deleteWidgetMSG", method = RequestMethod.POST)
    public byte[] deleteWidgetMSG(@RequestBody JSONObject inputParam, HttpServletRequest request) {
        JSONObject result = new JSONObject();

        try {
            String path = request.getSession().getServletContext().getRealPath("/");
            String widgetId = inputParam.getString("mappId");
            if (logger.isDebugEnabled()) {
                logger.debug("Receive notification of deleting micro applications。the mappId  is ： " + widgetId);
            }
            if (StringUtils.isNullOrEmpty(widgetId)) {
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.REASON, "入参有误!");
                if (logger.isDebugEnabled()) {
                    logger.debug("ServerController deleteWidgetMSG widgetId is null ");
                }
                return Base64.encodeBase64(result.toJSONString().getBytes());
            }
            if (logger.isDebugEnabled()) {
                logger.debug("ServerController deleteWidgetMSG widgetId ==  " + widgetId + " ; path = " + path);
            }
            Map<String, Object> param = new HashMap<>();
            param.put("widgetId", widgetId);
            param.put("status", 5);//已关闭

            widgetBusiness.updateStatus(param, inputParam);

            result.put(ResultStringKey.SUCCESS, true);
        } catch (Exception e) {
            logger.error("ServerController deleteWidgetMSG error " + e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, "服务器异常，请重试 ！");
        }
        return Base64.encodeBase64(result.toJSONString().getBytes());
    }

    /**
     * 大屏定制特殊定制接口：
     * 通过外部系统来生成一个组装应用
     *
     * @return String
     */
    @RequestMapping(value = "/addAssemblyAppByCCP",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String addAssemblyAppByCCP(@RequestBody JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        try {
            String taskId = jsonObject.getString("taskId");
            if(StringUtils.isNullOrEmpty(taskId)){
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.REASON, "没有任务Id，请重试。");
                return Base64.encodeBase64String(result.toJSONString().getBytes());
            }
            AppBaseInfo appInfoByTaskId = appBaseInfoService.getAppInfoByTaskId(taskId);
            if(null != appInfoByTaskId && 0 != appInfoByTaskId.getAppId()){
                result.put(ResultStringKey.APP_ID,appInfoByTaskId.getAppId());
                result.put("appName",appInfoByTaskId.getAppName());
                result.put("status",appInfoByTaskId.getStatus());
                result.put(ResultStringKey.SUCCESS, true);
                result.put("alreadyCreated", true);
                return Base64.encodeBase64String(result.toJSONString().getBytes());
            }
            String appName = jsonObject.getString("appName");
            if (StringUtils.isNullOrEmpty(appName)) {
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.REASON, "没有组装应用名称，请重试。");
                return Base64.encodeBase64String(result.toJSONString().getBytes());
            }
            String createUserId = jsonObject.getString("createUserId");
            if (StringUtils.isNullOrEmpty(createUserId)) {
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.REASON, "没有用户信息，请重试。");
                return Base64.encodeBase64String(result.toJSONString().getBytes());
            }
            Map<String, Object> param = new HashMap<>();
            param.put("appName", appName);
            param.put("userId", createUserId);
            if (appBaseInfoService.selectAppInfoByAppName(param) > 0) {
                result.put(ResultStringKey.RESULT, false);
                result.put(ResultStringKey.REASON, "组装应用名称重复，请修改！");
                return Base64.encodeBase64String(result.toJSONString().getBytes());
            }
            String appDesc = jsonObject.getString("appDesc");
            if (StringUtils.isNullOrEmpty(appDesc)) {
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.REASON, "没有组装应用描述，请重试。");
                return Base64.encodeBase64String(result.toJSONString().getBytes());
            }
            String uninstallDays = jsonObject.getString("uninstallDays");
            AppBaseInfo appBaseInfo = new AppBaseInfo();
            appBaseInfo.setAppName(appName);
            appBaseInfo.setAppDesc(appDesc);
            appBaseInfo.setCreateUserId(createUserId);
            appBaseInfo.setUserName(createUserId);
            Date createTime = new Date();
            appBaseInfo.setCreateTime(createTime);
            appBaseInfo.setLastMofityTime(createTime);
            appBaseInfo.setStatus(Configs.PAGE_STATUS_EDIT.getIntCode());
            appBaseInfo.setVer("1");
            if (!StringUtils.isNullOrEmpty(uninstallDays) && StringUtils.isNumber(uninstallDays)) {
                String afterDate = BeforeOrAfterDate.beforNumberDay(createTime,
                        Integer.parseInt(uninstallDays) == 0 ? 1 : Integer.parseInt(uninstallDays));
                Date xDate = new SimpleDateFormat("yyyyMMdd").parse(afterDate);
                appBaseInfo.setIsAutoUninstall(1);
                appBaseInfo.setUninstallTime(xDate);
            } else if (StringUtils.isNullOrEmpty(uninstallDays)) {
                appBaseInfo.setIsAutoUninstall(0);
            } else {
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.REASON, "失效日期天数有误！请重试。");
                return Base64.encodeBase64String(result.toJSONString().getBytes());
            }
            appBaseInfo.setSource("1");//次来源标识着是外部系统创建的组装应用，即为大屏定制项目
            appBaseInfo.setTaskId(taskId);
            appBaseInfoService.addAppInfo(appBaseInfo);
            result.put(ResultStringKey.RESULT, "success");
            result.put("alreadyCreated", false);
            result.put(ResultStringKey.APP_ID,appBaseInfo.getAppId());
            result.put("appName",appBaseInfo.getAppName());
            result.put("status",appBaseInfo.getStatus());
        } catch (Exception e) {
            logger.error("addAssemblyAppBySource error ", e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, "未知错误，请联系管理员！");
            return Base64.encodeBase64String(result.toJSONString().getBytes());
        }
        return Base64.encodeBase64String(result.toJSONString().getBytes());
    }
}
