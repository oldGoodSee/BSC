package com.bocom.controller.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.business.AppBaseBusiness;
import com.bocom.business.WidgetBusiness;
import com.bocom.domain.pac.AppBaseInfo;
import com.bocom.dto.session.SessionUserInfo;
import com.bocom.enums.DictionaryEnums;
import com.bocom.service.pac.AppBaseInfoService;
import com.bocom.service.pac.NoticeService;
import com.bocom.service.pac.impl.BaseServiceImpl;
import com.bocom.service.user.UserRestService;
import com.bocom.util.StringUtils;
import com.bocom.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/server/widget")
public class WidgetRestController extends BaseServiceImpl {

    // log
    private static Logger logger = LoggerFactory
            .getLogger(WidgetRestController.class);

    @Resource
    private UserRestService userRestService;

    @Resource
    private AppBaseInfoService appBaseInfoService;

    @Resource
    private NoticeService noticeService;

    @Resource
    private WidgetBusiness widgetBusiness;

    private int pageSize = 20;

    @Resource
    private AppBaseBusiness appBaseBusiness;


    /**
     * 读取组装应用中的iframe数据=
     *
     * @author QY
     */
    @ResponseBody
    @RequestMapping("/getContentIframe")
    public JSONObject getContentIframe(String appId, String type, String iframeId) {
        JSONObject result = new JSONObject();
        if (StringUtils.isNullOrEmpty(appId, type, iframeId)) {
            result.put("success", false);
            result.put("reason", "参数不对，请重试！");
        } else {
            try {
                String contentResult = appBaseBusiness.getContentIframe(appId, type, iframeId);
                result.put("success", true);
                result.put("data", contentResult);
            } catch (Exception e) {
                logger.error("publishIframe is error ", e);
                result.put("success", false);
                result.put("reason", "系统出错，请联系管理员！");
            }
        }
        return result;
    }




    /**
     * 为数据控件加载区域、数据库信息，供用户选择
     *
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDBInfo", method = RequestMethod.GET,
            produces = {"application/json;charset=UTF-8"})
    public JSONObject getDBInfo(HttpServletRequest req)
            throws UnsupportedEncodingException {
        String str = null;
        String areaId = req.getParameter("areaId");
        String serverId = req.getParameter("serverId");
        if ((areaId == null || "".equals(areaId)) &&
                (serverId == null || serverId.equals(""))) {
            //get all area info
            //rest.dmmpdr.queryAreaType.url
            str = widgetBusiness.queryDMMPDR(null, null, 0, 0);
        } else if ((areaId != null && !"".equals(areaId)) &&
                (serverId == null || serverId.equals(""))) {
            //get db info by areaid
            String pageNum = req.getParameter("pageNum");//想要去第几页？
            str = widgetBusiness.queryDMMPDR(areaId, null, pageSize, Integer.parseInt(pageNum));
            JSONObject jsonObject = JSONObject.parseObject(str);
            JSONObject dataPre = jsonObject.getJSONObject("data");
            JSONArray data = dataPre.getJSONArray("list");
//            JSONArray data = jsonObject.getJSONArray("data");
            JSONArray newData = new JSONArray();
            Map<String, Map> dbMap = new HashMap<String, Map>();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    JSONObject o = data.getJSONObject(i);
                    if (o.getString("dbCategoryName") != null &&
                            (o.getString("dbCategoryName").equalsIgnoreCase("mysql")
                                    || o.getString("dbCategoryName").equalsIgnoreCase("hana"))) {
                        logger.debug("find out!!!" + o.getString("dbCategoryName"));
                        Map<String, String> m = new HashMap<String, String>();
                        m.put("ipAddr", o.getString("ipAddr"));
                        m.put("port", o.getString("port"));
                        m.put("loginName", o.getString("loginName"));
                        m.put("instanceName", o.getString("instanceName"));
                        m.put("loginPwd", o.getString("loginPwd"));
                        dbMap.put(o.getString("id"), m);
                        o.put("ipAddr", "");
                        o.put("port", "");
                        o.put("loginName", "");
                        o.put("loginPwd", "");
                        o.put("dbCategoryName", o.getString("dbCategoryName").toLowerCase());
                        newData.add(o);
                    }
                }
            }
            req.getServletContext().setAttribute("dbMap", dbMap);
            //jsonObject.remove("data");
            jsonObject.put("newdata", newData);
            str = jsonObject.toJSONString();
        } else if ((areaId == null || "".equals(areaId)) &&
                (serverId != null && !serverId.equals(""))) {
            String pageNum = req.getParameter("pageNum");//想要去第几页？
            str = widgetBusiness.queryDMMPDR(null, serverId, pageSize, Integer.parseInt(pageNum));
            logger.debug("str: " + str);
        }
        return JSONObject.parseObject(str);
    }

    @ResponseBody
    @RequestMapping(value = "/queryDB", method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public JSONObject queryDB(HttpServletRequest req, String sql,
                              String dbId, String targetPage, String dbType) {
        Map<String, String> nbMap = new HashMap<>();
        nbMap.put("dbType", dbType);

        String appId = req.getParameter("at");
        String userName = req.getParameter("userName");
        if (StringUtils.isNullOrEmpty(userName)) {
            userName = UserUtils.getUserName(request.getSession());
        }
        recordLog(userName, sql, dbId, appId);
        if (logger.isDebugEnabled()) {
            logger.debug("sql == " + sql + " ; dbId == " + dbId +
                    " ; targetPage == " + targetPage + " dbType: " + dbType +
                    "   ...............");
        }
        return widgetBusiness.queryDB(dbId, sql,
                Integer.parseInt(targetPage), nbMap);
    }

    private void recordLog(String userName, String sql, String dbId, String appId) {
        try {

            String logMsg;
            if (StringUtils.isNullOrEmpty(appId)) {
                logMsg = "";
            } else {
                AppBaseInfo appBaseInfo = appBaseInfoService.getAppInfoById(Integer.parseInt(appId));
                logMsg = "组装应用  " + appBaseInfo.getAppName() + " 中的";
            }
            userName = StringUtils.isNullOrEmpty(userName) ? "" : userName;
            String msg = "数据查询服务";
            String param = " ; 查询sql为 : " + sql + "  数据库为  : " + dbId;
            SessionUserInfo sessionUserInfo = userRestService.getUserInfoByLoginName(userName);
            super.saveLog(DictionaryEnums.ACTION_VIEW.getDictionaryCode(), DictionaryEnums.BUSINESS_SERVICE.getDictionaryCode(),
                    userName + "  访问" + logMsg + "微服务 : "
                            + msg + param, sessionUserInfo);
        } catch (Exception e) {
            logger.error(" recordLog error " + e);
        }

    }

}
