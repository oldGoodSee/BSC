package com.bocom.controller.pac;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.domain.pac.AppBaseInfo;
import com.bocom.dto.session.SessionUserInfo;
import com.bocom.enums.DictionaryEnums;
import com.bocom.service.pac.AppBaseInfoService;
import com.bocom.service.pac.WidgetServiceService;
import com.bocom.service.pac.impl.BaseServiceImpl;
import com.bocom.service.user.UserRestService;
import com.bocom.support.servlet.ResultStringKey;
import com.bocom.util.ConfigGetPropertyUtil;
import com.bocom.util.PageUtil;
import com.bocom.util.StringUtils;
import com.bocom.util.UserUtils;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/server/widget")
public class WidgetServiceController extends BaseServiceImpl {

    // log
    private static Logger logger = LoggerFactory
            .getLogger(ServerController.class);


    @Resource
    private WidgetServiceService widgetServiceService;

    @Resource
    private AppBaseInfoService appBaseInfoService;

    @Resource
    private UserRestService userRestService;

    @Resource
    private HttpServletRequest request;

    private static final String ID_CARD = "idCard";

    @ResponseBody
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public JSONObject selectStatistics() {
        JSONObject result = new JSONObject();
        try {
            JSONArray jsonArray = widgetServiceService.selectStatistics();
            result.put("series", jsonArray);
            String appId = request.getParameter("at");
            String userName = request.getParameter("userName");
            String msg = "警情统计";
            recordLog(userName, appId, msg, "");
        } catch (Exception e) {
            logger.error("selectStatistics error  msg >>>  " + e);
        }
        if (logger.isDebugEnabled()) {
            logger.error("selectStatistics result >>>  " + result);
        }
        return result;
    }

    private void recordLog(String userName, String appId, String msg, String param) {
        try {
            AppBaseInfo appBaseInfo;
            String logMsg;
            if (StringUtils.isNullOrEmpty(appId)) {
                logMsg = "";
            } else {
                appBaseInfo = appBaseInfoService.getAppInfoById(Integer.parseInt(appId));
                logMsg = "组装应用  " + appBaseInfo.getAppName() + " 中的";
            }
            if (StringUtils.isNullOrEmpty(userName)) {
                userName = UserUtils.getUserName(request.getSession());
            }
            SessionUserInfo sessionUserInfo = userRestService.getUserInfoByLoginName(userName);
            super.saveLog(DictionaryEnums.ACTION_VIEW.getDictionaryCode(), DictionaryEnums.BUSINESS_SERVICE.getDictionaryCode(),
                    userName + "  访问" + logMsg + "微服务 : "
                            + msg + param, sessionUserInfo);
        } catch (Exception e) {
            logger.error("recordLog error " + e);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/testSelect", method = RequestMethod.GET)
    public Map testSelect() {
        PageInfo pageInfo = null;
        try {
            String param = request.getParameter("param");
            String name = request.getParameter("name");
            Map<String, Object> paramSelect = new HashMap<>();
            paramSelect.put("param", param);
            StringBuilder recordParam = new StringBuilder();
            recordParam.append(" , 参数为 : ");
            boolean recordFlag = false;
            if (!StringUtils.isNullOrEmpty(name)) {
                paramSelect.put("name", URLDecoder.decode(name, ResultStringKey.CHAR_UTF8));
                recordParam.append("  " + URLDecoder.decode(name, ResultStringKey.CHAR_UTF8));
                recordFlag = true;
            }
            PageUtil.setParams(request, paramSelect);
            List<Map<String, Object>> map = widgetServiceService.test(paramSelect);
            pageInfo = new PageInfo(map);

            String appId = request.getParameter("at");
            String userName = request.getParameter("userName");
            String msg = "人员信息查询";
            if (!recordFlag) {
                recordParam.setLength(0);
            }
            recordLog(userName, appId, msg, recordParam.toString());

        } catch (Exception e) {
            logger.error("testSelect error  msg >>>  " + e);
        }
        return PageUtil.covertMap(new Object[]{"page"},
                new Object[]{pageInfo});
    }

    @ResponseBody
    @RequestMapping(value = "/getCarInfoByIdCardCar", method = RequestMethod.GET)
    public Map getCarInfoByIdCardCar() {
        PageInfo pageInfo = null;
        try {
            String plateNumber = request.getParameter("plateNumber");
            String idCard = request.getParameter("idCard");
            Map<String, Object> paramSelect = new HashMap<>();
            StringBuilder recordParam = new StringBuilder();
            recordParam.append(" , 参数为 : ");
            boolean recordFlag = false;

            if (!StringUtils.isNullOrEmpty(idCard)) {
                paramSelect.put(ID_CARD, idCard);
                recordParam.append("  " + idCard);
                recordFlag = true;
            }
            if (!StringUtils.isNullOrEmpty(plateNumber)) {
                paramSelect.put("plateNumber", URLDecoder.decode(plateNumber, ResultStringKey.CHAR_UTF8));
                recordParam.append("  " + URLDecoder.decode(plateNumber, ResultStringKey.CHAR_UTF8));
                recordFlag = true;
            }

            PageUtil.setParams(request, paramSelect);
            List<Map<String, Object>> map = widgetServiceService.getCarInfoByIdCardCar(paramSelect);
            for (Map<String, Object> widgetInfo : map) {
                String url = widgetInfo.get("carPicture").toString();
                String[] imgUrl = url.split(",");
                String newUrl = "";
                for (String str : imgUrl) {
                    newUrl += ConfigGetPropertyUtil.get("fastDFS.http.url") + str + ",";
                }
                widgetInfo.put("carPicture", newUrl.substring(0, newUrl.length() - 1));
            }
            pageInfo = new PageInfo(map);

            String appId = request.getParameter("at");
            String userName = request.getParameter("userName");
            String msg = "车辆违规信息查询";
            if (!recordFlag) {
                recordParam.setLength(0);
            }
            recordLog(userName, appId, msg, recordParam.toString());

        } catch (Exception e) {
            logger.error("getCarInfoByIdCardCar error  msg >>>  " + e);
        }
        return PageUtil.covertMap(new Object[]{"page"},
                new Object[]{pageInfo});
    }

    @ResponseBody
    @RequestMapping(value = "/getInfoByNameIdCard")
    public Map selectInfoByNameIdCard() {
        PageInfo pageInfo = null;
        try {
            String idCard = request.getParameter(ID_CARD);
            String name = request.getParameter("name");
            Map<String, Object> paramSelect = new HashMap<>();

            StringBuilder recordParam = new StringBuilder();
            recordParam.append(" , 参数为 : ");
            boolean recordFlag = false;
            if (!StringUtils.isNullOrEmpty(idCard)) {
                paramSelect.put(ID_CARD, idCard);
                recordParam.append("  " + idCard);
                recordFlag = true;
            }
            if (!StringUtils.isNullOrEmpty(name)) {
                paramSelect.put("name", URLDecoder.decode(name, ResultStringKey.CHAR_UTF8));
                recordParam.append("  " + URLDecoder.decode(name, ResultStringKey.CHAR_UTF8));
                recordFlag = true;
            }
            PageUtil.setParams(request, paramSelect);
            List<Map<String, Object>> map = widgetServiceService.selectPersonInfoByNameIdCard(paramSelect);
            pageInfo = new PageInfo(map);

            String appId = request.getParameter("at");
            String userName = request.getParameter("userName");
            String msg = "新人员信息查询（姓名、身份证）";
            if (!recordFlag) {
                recordParam.setLength(0);
            }
            recordLog(userName, appId, msg, recordParam.toString());

        } catch (Exception e) {
            logger.error("selectInfoByNameIdCard error  msg >>>  " + e);
        }
        return PageUtil.covertMap(new Object[]{"page"},
                new Object[]{pageInfo});
    }

    @ResponseBody
    @RequestMapping(value = "/getInfoByNameAddressJob")
    public Map getInfoByNameAddressJob() {
        PageInfo pageInfo = null;
        try {
            String address = request.getParameter("address");
            String jobTitle = request.getParameter("jobTitle");
            String name = request.getParameter("name");
            Map<String, Object> paramSelect = new HashMap<>();
            StringBuilder recordParam = new StringBuilder();
            recordParam.append(" , 参数为 : ");
            boolean recordFlag = false;
            if (!StringUtils.isNullOrEmpty(address)) {
                paramSelect.put("address", URLDecoder.decode(address, ResultStringKey.CHAR_UTF8));
                recordParam.append("  " + URLDecoder.decode(address, ResultStringKey.CHAR_UTF8));
                recordFlag = true;
            }
            if (!StringUtils.isNullOrEmpty(name)) {
                paramSelect.put("name", URLDecoder.decode(name, ResultStringKey.CHAR_UTF8));
                recordParam.append("  " + URLDecoder.decode(name, ResultStringKey.CHAR_UTF8));
                recordFlag = true;
            }
            if (!StringUtils.isNullOrEmpty(jobTitle)) {
                paramSelect.put("jobTitle", URLDecoder.decode(jobTitle, ResultStringKey.CHAR_UTF8));
                recordParam.append("  " + URLDecoder.decode(jobTitle, ResultStringKey.CHAR_UTF8));
                recordFlag = true;
            }
            PageUtil.setParams(request, paramSelect);
            List<Map<String, Object>> map = widgetServiceService.selectPersonInfoByNameJobAddress(paramSelect);
            pageInfo = new PageInfo(map);

            String appId = request.getParameter("at");
            String userName = request.getParameter("userName");
            String msg = "新人员信息查询（姓名、地址、职称）";
            if (!recordFlag) {
                recordParam.setLength(0);
            }
            recordLog(userName, appId, msg, recordParam.toString());

        } catch (Exception e) {
            logger.error("getInfoByNameAddressJob error  msg >>>  " + e);
        }
        return PageUtil.covertMap(new Object[]{"page"},
                new Object[]{pageInfo});
    }

    @ResponseBody
    @RequestMapping(value = "/getInfoByNameBirth")
    public Map getInfoByNameBirth() {
        PageInfo pageInfo = null;
        try {
            String birthDateStart = request.getParameter("birthDateStart");
            String birthDateEnd = request.getParameter("birthDateEnd");
            String name = request.getParameter("name");
            Map<String, Object> paramSelect = new HashMap<>();

            StringBuilder recordParam = new StringBuilder();
            recordParam.append(" , 参数为 : ");
            boolean recordFlag = false;

            //在此日期之后生的
            if (!StringUtils.isNullOrEmpty(birthDateStart) && StringUtils.isNullOrEmpty(birthDateEnd)) {
                paramSelect.put("birthDate", birthDateStart);
                recordParam.append("  " + birthDateStart);
                recordFlag = true;
            }
            //在此日期之前生的
            if (!StringUtils.isNullOrEmpty(birthDateEnd) && StringUtils.isNullOrEmpty(birthDateStart)) {
                paramSelect.put("birthDateAfter", birthDateEnd);
                recordParam.append("  " + birthDateEnd);
                recordFlag = true;
            }
            //生日查询区间
            if (!StringUtils.isNullOrEmpty(birthDateStart, birthDateEnd)) {
                paramSelect.put("birthDateStart", birthDateStart);
                paramSelect.put("birthDateEnd", birthDateEnd);
                recordParam.append("  " + birthDateStart + "  " + birthDateEnd);
                recordFlag = true;
            }
            if (!StringUtils.isNullOrEmpty(name)) {
                paramSelect.put("name", URLDecoder.decode(name, ResultStringKey.CHAR_UTF8));
                recordParam.append("  " + URLDecoder.decode(name, ResultStringKey.CHAR_UTF8));
                recordFlag = true;
            }
            PageUtil.setParams(request, paramSelect);
            List<Map<String, Object>> map = widgetServiceService.selectPersonInfoByNameBirth(paramSelect);
            pageInfo = new PageInfo(map);

            String appId = request.getParameter("at");
            String userName = request.getParameter("userName");
            String msg = "新人员信息查询（姓名、出生日期）";
            if (!recordFlag) {
                recordParam.setLength(0);
            }
            recordLog(userName, appId, msg, recordParam.toString());

        } catch (Exception e) {
            logger.error("getInfoByNameBirth error  msg >>>  " + e);
        }
        return PageUtil.covertMap(new Object[]{"page"},
                new Object[]{pageInfo});
    }
}
