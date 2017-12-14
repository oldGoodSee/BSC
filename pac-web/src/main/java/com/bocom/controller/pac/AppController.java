package com.bocom.controller.pac;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.business.AppBaseBusiness;
import com.bocom.domain.UserRoleOrg;
import com.bocom.domain.pac.AppBaseInfo;
import com.bocom.domain.pac.NoticeInfo;
import com.bocom.domain.pac.PackagingInfo;
import com.bocom.domain.pac.ResultDo;
import com.bocom.dto.session.SessionUserInfo;
import com.bocom.service.pac.AppBaseInfoService;
import com.bocom.service.pac.CoreService;
import com.bocom.service.pac.NoticeService;
import com.bocom.support.servlet.ResultStringKey;
import com.bocom.util.*;
import com.bocom.util.Behavior.Configs;
import com.github.pagehelper.PageInfo;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/manager/app")
public class AppController {

    private static Logger logger = LoggerFactory
            .getLogger(AppController.class);

    @Resource
    private AppBaseInfoService appBaseInfoService;

    @Resource
    private AppBaseBusiness appBaseBusiness;

    @Resource
    private CoreService coreService;

    @Resource
    private NoticeService noticeService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpSession session;

    private static final String STATUS = "status";

    private String getUserId(HttpSession session) {
        SessionUserInfo sessionUserInfo = null;
        if (session.getAttribute("sessionUserInfo") != null) {
            sessionUserInfo =
                    (SessionUserInfo) session.getAttribute("sessionUserInfo");
        }
        return String.valueOf(sessionUserInfo.getUserId());
    }


    /**
     * 发布组装应用中的iframe  类型有data css
     *
     * @author QY
     */
    @ResponseBody
    @RequestMapping("/publishIframe")
    public JSONObject publishIframe(String appId, String type, String json, String iframeId) {
        JSONObject result = new JSONObject();
        if (StringUtils.isNullOrEmpty(appId, type, json, iframeId)) {
            result.put("success", false);
            result.put("reason", "参数不对，请重试！");
        } else {
            try {
                String filePath = request.getSession().getServletContext().getRealPath("/")
                        + "modelFrame-tab/data/";
                if (appBaseBusiness.publishIframe(appId, type, json, iframeId,filePath)) {
                    result.put("success", true);
                }
            } catch (Exception e) {
                logger.error("publishIframe is error ", e);
                result.put("success", false);
                result.put("reason", "系统出错，请联系管理员！");
            }
        }
        return result;
    }


    /**
     * 更新组装应该
     *
     * @return jsonObject
     */
    @ResponseBody
    @RequestMapping(value = "/updateAppBaseInfo", method = RequestMethod.POST)
    public JSONObject updateAppBaseInfo(@RequestBody JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AppBaseInfo appBaseInfo = objectMapper.readValue(JsonUtil.toJSon(jsonObject), AppBaseInfo.class);
            Date uninstallTime = appBaseInfo.getUninstallTime();
            if (uninstallTime != null) {
                appBaseInfo.setIsAutoUninstall(1);
            }
            appBaseInfo.setLastMofityTime(new Date());
            appBaseInfo.setAppName(URLDecoder.decode(appBaseInfo.getAppName(), "utf-8"));
            appBaseInfoService.updateAppBaseInfo(appBaseInfo);
        } catch (Exception e) {
            logger.error("updateAppBaseInfo  error " + e);
        }
        result.put(ResultStringKey.SUCCESS, true);
        result.put(ResultStringKey.RESULT, 1);
        return result;
    }

    /**
     * 卸载app
     *
     * @return map
     */
    @ResponseBody
    @RequestMapping("/uninstallApp")
    public Map<String, String> uninstallApp() {
        Map<String, String> map = new HashMap<>();
        try {
            String appId = request.getParameter(ResultStringKey.APP_ID);
            String status = request.getParameter(STATUS);
            if (org.springframework.util.StringUtils.isEmpty(appId.trim())) {
                map.put(ResultStringKey.RESULT, ResultStringKey.ERROR);
                map.put(ResultStringKey.REASON, "数据错误。请重试！");
                return map;
            }
            String result = appBaseInfoService.deleteApp(appId, status, "");
            if (result.equals(ResultStringKey.SUCCESS)) {
                map.put(ResultStringKey.RESULT, ResultStringKey.SUCCESS);
            } else {
                map.put(ResultStringKey.RESULT, ResultStringKey.ERROR);
                map.put(ResultStringKey.REASON, "卸载失败 ！");
            }
        } catch (Exception e) {
            map.put(ResultStringKey.RESULT, ResultStringKey.ERROR);
            map.put(ResultStringKey.REASON, "系统错误，请联系管理员！");
            logger.error("uninstallApp failed", e);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/getAppBaseInfo")
    public Map getAppBaseInfo() {
        PageInfo pageInfo = null;
        try {
            String appId = request.getParameter("appId");
            String appName = request.getParameter("appName");
            String status = request.getParameter("status");
            Map<String, Object> param = new HashMap<>();
            if (null != appName) {
                appName = URLDecoder.decode(appName, "utf-8");
            }
            param.put("appId", StringUtils.isNullOrEmpty(appId) ? null : appId.trim());
            param.put("appName", StringUtils.isNullOrEmpty(appName) ? null : appName.trim());
            param.put("status", StringUtils.isNullOrEmpty(status) ? null : status.trim());
            param.put("userid", getUserId(session));
            param.put("admin", UserUtils.getUserRoleCode(session).contains("1"));
            param.put("orgId", UserUtils.getUserOrgId(session));
            PageUtil.setParams(request, param);
            List<AppBaseInfo> appBaseInfoList = appBaseInfoService.queryDataByParam(param);
            if (logger.isDebugEnabled()) {
                logger.debug("appBaseInfoList is " + appBaseInfoList);
            }
            pageInfo = new PageInfo(appBaseInfoList);
        } catch (Exception e) {
            logger.debug("NewAppViewController.getAppBaseInfo error " + e.toString());
        }
        return PageUtil.covertMap(new Object[]{"page"},
                new Object[]{pageInfo});
    }

    @ResponseBody
    @RequestMapping("/addAppBaseInfo")
    public Map addAppBaseInfo() {
        Map<String, Object> result = new HashMap<>();
        try {
            SessionUserInfo sessionUserInfo = null;
            if (session.getAttribute("sessionUserInfo") != null) {
                sessionUserInfo =
                        (SessionUserInfo) session.getAttribute("sessionUserInfo");
                String appInfo = URLDecoder.decode(request.getParameter("appInfo"), "utf-8");
                JSONObject appInfoJson = JSON.parseObject(appInfo);
                String appName = appInfoJson.getString("appName");
                Map<String, Object> param = new HashMap<>();
                param.put("appName", appName);
                param.put("userId", UserUtils.getUserId(session));
                if (appBaseInfoService.selectAppInfoByAppName(param) > 0) {
                    result.put(ResultStringKey.RESULT, false);
                    result.put(ResultStringKey.REASON, "组装应用名称重复，请修改！");
                    return result;
                }
                String appDesc = appInfoJson.getString("appDesc");
                String autoUninstall = appInfoJson.getString("autoUninstall");
                String validityTime = appInfoJson.getString("validityTime");
                if (Integer.parseInt(autoUninstall) < 0) {
                    result.put(ResultStringKey.RESULT, "error");
                    result.put(ResultStringKey.REASON, "请选择有效期天数!");
                    return result;
                }
                if (!StringUtils.isInteger(validityTime)) {
                    result.put(ResultStringKey.RESULT, "error");
                    result.put(ResultStringKey.REASON, "请输入正确的有效期天数!");
                    return result;
                }
                Integer autoUni = autoUninstall.equals("0") ? new Integer(0) : new Integer(1);
                Date createTime = new Date();
                String userIpAddr = request.getRemoteAddr();
                String ver = "1";
                AppBaseInfo appBaseInfo = new AppBaseInfo();
                appBaseInfo.setAppName(appName);
                appBaseInfo.setAppDesc(appDesc);
                appBaseInfo.setIsAutoUninstall(autoUni);
                appBaseInfo.setCreateTime(createTime);
                appBaseInfo.setLastMofityTime(createTime);
                appBaseInfo.setCreateUserId(UserUtils.getUserId(session));
                appBaseInfo.setUserName(UserUtils.getUserName(session));
                appBaseInfo.setUserIpAddr(userIpAddr);
                appBaseInfo.setStatus(Configs.PAGE_STATUS_EDIT.getIntCode());
                appBaseInfo.setVer(ver);
                if (0 != autoUni) {
                    String afterDate = BeforeOrAfterDate.beforNumberDay(createTime,
                            Integer.parseInt(validityTime) == 0 ? 1 : Integer.parseInt(validityTime));
                    Date xDate = new SimpleDateFormat("yyyyMMdd").parse(afterDate);
                    appBaseInfo.setUninstallTime(xDate);
                }
                appBaseInfoService.addAppInfo(appBaseInfo);
                result.put(ResultStringKey.RESULT, "success");
                result.put("data", JsonUtil.toJSon(appBaseInfo));
            }
        } catch (Exception e) {
            logger.error("addAppInfo error " + e);
            result.put(ResultStringKey.RESULT, "error");
            result.put(ResultStringKey.REASON, "数据有问题!");
        }
        return result;
    }

    /**
     * save app users pac自有对客户端进行授权（人）
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addRight", method = RequestMethod.GET)
    public String addRight()
            throws UnsupportedEncodingException {
        String appId = request.getParameter(ResultStringKey.APP_ID);
        String users = request.getParameter("users");
        if (users != null) {
            users = URLDecoder.decode(users, "UTF-8");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("addRight users  is :" + users + "  appId is = " + appId);
        }
        JSONArray userArray = JSONArray.parseArray(users);
        ResultDo resultDo = coreService.addAppUser(appId, userArray);
        JSONObject jsonObject = new JSONObject();
        if (resultDo.isSuccess()) {
            jsonObject.put(ResultStringKey.SUCCESS, true);
        }
        return jsonObject.toJSONString();
    }

    /**
     * save app users pac自有对客户端进行授权（组织机构部门）
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addRightOrg", method = RequestMethod.GET)
    public String addRightOrg() {
        String appId = request.getParameter(ResultStringKey.APP_ID);
        String orgId = request.getParameter("orgId");
        logger.debug("request arrived to addRightOrg, appId is:" + appId + " ,and orgId is:" + orgId);
        if (logger.isDebugEnabled()) {
            logger.debug("addRightOrg orgId  is :" + orgId + "  appId is = " + appId);
        }
//        JSONArray userArray = JSONArray.parseArray(users);
        ResultDo resultDo = coreService.addAppOrg(appId, orgId);
        JSONObject jsonObject = new JSONObject();
        if (resultDo.isSuccess()) {
            jsonObject.put(ResultStringKey.SUCCESS, true);
        }
        logger.debug("对组织机构进行授权，结果是：" + jsonObject);
        return jsonObject.toJSONString();
    }

    /**
     * get all the organization
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAllOrg")
    public JSONObject getAllOrg() {
        String url = ConfigGetPropertyUtil.get("rest.allorg.interface.url");
        String data = HttpClientUtil.postBase64(url, "");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", data);
        return jsonObject;
    }

    /**
     * get all the person by orgId
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPersonByOrg", method = RequestMethod.GET)
    public JSONObject getPersonByOrg() {
        String orgCode = request.getParameter("organizationCode");
        String a = "error";
        String url = ConfigGetPropertyUtil.get("rest.personoforg.interface.url");
        if (orgCode != null && !"".equals(orgCode)) {
            url += orgCode;
            Map<String, Object> param = new HashMap<>();
            a = HttpClientUtil.postBase64(url, param);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", a);
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping(value = "/getPersonSeled", method = RequestMethod.GET)
    public JSONObject getPersonSeled() {
        String appId = request.getParameter(ResultStringKey.APP_ID);
        PackagingInfo p = new PackagingInfo();
        p.setAppid(appId);
        JSONArray userArray = coreService.getAppUsers(p);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", userArray);
        return jsonObject;
    }

    //为授权页面服务，获得所有已经授权过的组织机构
    @ResponseBody
    @RequestMapping(value = "/getOrgSeled", method = RequestMethod.GET)
    public JSONObject getOrgSeled() {
        String appId = request.getParameter(ResultStringKey.APP_ID);
        logger.debug("开始根据appId获得已经授权的组织机构，appId是：" + appId);
        PackagingInfo p = new PackagingInfo();
        p.setAppid(appId);
        JSONArray userArray = coreService.getAppOrg(p);
        logger.debug("已经授权的组织机构是：" + userArray);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", userArray);
        return jsonObject;
    }

    //取消对某组织机构对授权
    @ResponseBody
    @RequestMapping(value = "/delAppOrg", method = RequestMethod.GET)
    public JSONObject delAppOrg() {
        String appId = request.getParameter(ResultStringKey.APP_ID);
        String orgId = request.getParameter("orgId");
        logger.debug("进入取消对组织机构授权流程，appId is:" + appId + " ,orgId is:" + orgId);
        UserRoleOrg userRoleOrg = new UserRoleOrg();
        userRoleOrg.setAppId(appId);
        userRoleOrg.setOrgId(orgId);
        coreService.delAppOrg(userRoleOrg);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        logger.debug("取消对组织机构授权，结果是：" + jsonObject);
        return jsonObject;
    }


    @ResponseBody
    @RequestMapping(value = "/getMicroOAppInfo", method = RequestMethod.GET)
    public Map getMicroOAppInfo() {

        PageInfo pageInfo = null;
        try {
            String userId = UserUtils.getUserId(session);
            Map<String, Object> param = new HashMap<>();
            param.put("userid", userId);
            PageUtil.setParams(request, param);
            List<PackagingInfo> packagingInfos = coreService.getCoreInfoByAppId(param);
            pageInfo = new PageInfo(packagingInfos);

        } catch (Exception e) {
            logger.error("getMicroOAppInfo error ", e);
        }

        return PageUtil.covertMap(new Object[]{"page"},
                new Object[]{pageInfo});
    }

    @RequestMapping("/getNotices")
    @ResponseBody
    public String getNotice() {
        JSONObject result = new JSONObject();
        try {
            String appIdStr = request.getParameter(ResultStringKey.APP_ID);
            if (appIdStr.isEmpty()) {
                result.put("data", null);
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.REASON, "appId is empty");
                return Base64.encodeBase64String(result.toJSONString().getBytes());
            }
            Integer appId = Integer.valueOf(appIdStr);
            Map<String, Object> param = new HashMap<>();
            param.put("deleteFlag", "0");
            param.put("noticeType", "public");
            param.put(ResultStringKey.APP_ID, appId);
            List<NoticeInfo> noticeInfos = noticeService.getNoticeInfos(param);
            if (!noticeInfos.isEmpty()) {
                JSONArray jsonArray = new JSONArray();
                for (NoticeInfo noticeInfo : noticeInfos) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("title", noticeInfo.getContent());
                    jsonObject.put("url", "");
                    jsonArray.add(jsonObject);
                }
                result.put("data", jsonArray);
            } else {
                result.put("data", null);
            }
            result.put(ResultStringKey.SUCCESS, true);
            result.put(ResultStringKey.REASON, true);
            if (logger.isDebugEnabled()) {
                logger.debug("getNotices param is : appId == " + appIdStr);
                logger.debug("getNotices result is :" + result);
            }
        } catch (Exception e) {
            logger.error("get notice error  msg >>>  " + e);
            result.put("data", null);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, "系统错误，请稍后！");
            return Base64.encodeBase64String(result.toJSONString().getBytes());
        }
        return Base64.encodeBase64String(result.toJSONString().getBytes());
    }
}
