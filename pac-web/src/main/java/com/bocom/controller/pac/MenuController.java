package com.bocom.controller.pac;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.business.PublishAppBusiness;
import com.bocom.business.WidgetBusiness;
import com.bocom.domain.pac.AppBaseInfo;
import com.bocom.domain.pac.NoticeInfo;
import com.bocom.domain.pac.PackagingInfo;
import com.bocom.domain.pac.PageInfo;
import com.bocom.service.app.AppRestService;
import com.bocom.service.pac.*;
import com.bocom.support.servlet.ResultStringKey;
import com.bocom.util.Behavior.Configs;
import com.bocom.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Administrator on 2016/11/17.
 */
@Controller
@RequestMapping("/manager/menuAction")
public class MenuController {

    // log
    private static Logger logger = LoggerFactory
            .getLogger(MenuController.class);

    @Resource
    private FunctionService functionService;

    @Resource
    private AppRestService appRestService;

    @Resource
    private AppBaseInfoService appBaseInfoService;

    @Resource
    private NoticeService noticeService;

    @Resource
    private CoreService coreService;

    @Resource
    private WidgetBusiness widgetBusiness;

    @Resource
    private PublishAppBusiness publishAppBusiness;

    private static final String SESSIONUSERINFO = "sessionUserInfo";

    /**
     * @param model
     * @param request
     * @param session
     * @return java.lang.String
     * @function-description 进入管理端系统主界面
     */
    @RequestMapping("/toIndex")
    public String toIndex(Model model, HttpServletRequest request, HttpSession session) {
        List<Map<String, Object>> functionList = functionService.getFunctionList();
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> map : functionList) {
            jsonArray.add(JSONObject.parseObject(JsonUtil.toJSon(map)));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("functionList is : " + jsonArray);
        }
        model.addAttribute("functionList", jsonArray);
        model.addAttribute("flag", request.getParameter("flag"));
        return "pac_gl/index";
    }

    @RequestMapping("/policeAppView")
    public String policeAppView(Model model, HttpSession session) {
        String userId = UserUtils.getUserId(session);
        JSONObject appInfoJson = appRestService.queryAppByAppId(null);
        if (null == appInfoJson) {
            appInfoJson = new JSONObject();
            appInfoJson.put("data", "null");
            appInfoJson.put("msg", "error");
            appInfoJson.put(ResultStringKey.REASON, "获取应用注册中心数据有误，请过会再试。");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("appInfoJson is : " + appInfoJson);
        }
        List<String> pageIds = coreService.getPageIdsByUserId(Integer.parseInt(UserUtils.getUserId(session)));
        model.addAttribute("appInfoJson", appInfoJson);
        model.addAttribute("pageIdList", pageIds);
        JSONArray appBaseInfoJsonArray = getEditApp(userId);
        model.addAttribute("appBaseInfoJsonArray", appBaseInfoJsonArray);
        return "pac_gl/policeAppView";
    }

    //警务应用市场
    @RequestMapping("/policeAppViewNew")
    public String policeAppViewNew(Model model, HttpSession session,
                                   HttpServletRequest request) {
        String userId = UserUtils.getUserId(session);
        JSONObject appInfoJson = appRestService.queryAppByAppId(null);
        if (null == appInfoJson) {
            appInfoJson = new JSONObject();
            appInfoJson.put("data", "null");
            appInfoJson.put("msg", "error");
            appInfoJson.put(ResultStringKey.REASON, "获取应用注册中心数据有误，请过会再试。");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("appInfoJson is : " + appInfoJson);
        }
        Map<String, Object> resultMap = coreService.getMarketData(appInfoJson);

        model.addAttribute("appName", resultMap.get("appName"));
        List<PageInfo> pagesList = (List) resultMap.get("pagesList");

        //get compose num
        pagesList = coreService.transferComposeNum(pagesList);

        Collections.sort(pagesList);
        model.addAttribute("pagesList", pagesList);
        int bestAppNumInt = 0;

        //精选应用个数计算
        double bestAppNum = 0d;
        double numInRow = 4d;
        if (pagesList != null) {
            bestAppNum = Math.ceil((double) pagesList.size() / numInRow);
        }
        bestAppNumInt = (int) bestAppNum;
        List<PageInfo> bestPagesList = new ArrayList<PageInfo>();
        int i = 0;
        for (PageInfo page : pagesList) {
            if (i == bestAppNumInt) {
                break;
            }
            bestPagesList.add(page);
            i++;
        }
        request.getServletContext().setAttribute("bestPagesList", bestPagesList);

        List<String> pageIds = coreService.getPageIdsByUserId(Integer.parseInt(UserUtils.getUserId(session)));
        model.addAttribute("appInfoJson", appInfoJson);
        model.addAttribute("pageIdList", pageIds);
        JSONArray appBaseInfoJsonArray = getEditApp(userId);
        model.addAttribute("appBaseInfoJsonArray", appBaseInfoJsonArray);
        return "pac_gl/policeAppView_new";
    }

    @RequestMapping(value = "/getMicroAppFromMar")
    public String getMicroAppFromMar(Model model, HttpSession session, HttpServletRequest request) {
//        String userId = UserUtils.getUserId(session);
//        String userName = UserUtils.getUserName(session);
        Map<String, String> marParam = new HashMap<>();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        String localAppName = request.getParameter("localAppName");
        String command = request.getParameter("command");
        if (StringUtils.isNullOrEmpty(pageNum, pageSize)) {
            pageNum = "1";
            pageSize = "20";
        }
//        marParam.put("createByName", userName);
        marParam.put("mAppSource", "0");
        marParam.put("pageNum", pageNum);
        marParam.put("pageSize", pageSize);
        if (!StringUtils.isNullOrEmpty(command)) {
            marParam.put("command", URLEncoder.encode(command));
        }
        if (!StringUtils.isNullOrEmpty(localAppName)) {
            marParam.put("localAppName", URLEncoder.encode(localAppName));
        }
        JSONObject appInfoJson = appRestService.queryMicroApp(marParam);
        if (null == appInfoJson) {
            appInfoJson = new JSONObject();
            appInfoJson.put(ResultStringKey.SUCCESS, false);
            appInfoJson.put(ResultStringKey.REASON, "获取应用注册中心数据有误，请过会再试。");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("appInfoJson is : " + appInfoJson);
        }
        List<JSONObject> pagesList = (List) appInfoJson.getJSONObject("page").get("list");
        int bestAppNumInt = 0;

        //精选应用个数计算
        double bestAppNum = 0d;
        double numInRow = 4d;
        if (pagesList != null) {
            bestAppNum = Math.ceil((double) pagesList.size() / numInRow);
        }
        bestAppNumInt = (int) bestAppNum;
        List<JSONObject> bestPagesList = new ArrayList<JSONObject>();
        int i = 0;
        for (JSONObject jsonObject : pagesList) {
            if (i == bestAppNumInt) {
                break;
            }
            bestPagesList.add(jsonObject);
            i++;
        }
        JSONArray appNameJson = appRestService.queryAllAppName();
        model.addAttribute("total", appInfoJson.getJSONObject("page").getBigInteger("total"));
        model.addAttribute("pagesList", pagesList);
        model.addAttribute("appInfoJson", appInfoJson);
        request.getServletContext().setAttribute("bestPagesList", bestPagesList);
        request.getServletContext().setAttribute("appNameList", appNameJson);
        request.getServletContext().setAttribute("fastDFSUrl", ConfigGetPropertyUtil.get("fastDFS.http.url"));
        return "pac_gl/policeAppView_mar";
    }

    @RequestMapping(value = "/getMarketData", method = RequestMethod.POST)
    public String getMarketData(Model model, HttpSession session, HttpServletRequest request) throws
            UnsupportedEncodingException {
//        String userId = UserUtils.getUserId(session);
//        String userName = UserUtils.getUserName(session);
        Map<String, String> marParam = new HashMap<>();
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        String localAppName = request.getParameter("localAppName");
        String command = request.getParameter("command");
        if (StringUtils.isNullOrEmpty(pageNum, pageSize)) {
            pageNum = "1";
            pageSize = "20";
        }
//        marParam.put("createByName", userName);
        marParam.put("mAppSource", "0");
        marParam.put("pageNum", pageNum);
        marParam.put("pageSize", pageSize);
        if (!StringUtils.isNullOrEmpty(command)) {
            marParam.put("command", command);
            model.addAttribute("command", URLDecoder.decode(command, "UTF-8"));
        }
        if (!StringUtils.isNullOrEmpty(localAppName)) {
            marParam.put("localAppName", localAppName);
        }
        JSONObject appInfoJson = appRestService.queryMicroApp(marParam);
        if (null == appInfoJson) {
            appInfoJson = new JSONObject();
            appInfoJson.put(ResultStringKey.SUCCESS, false);
            appInfoJson.put(ResultStringKey.REASON, "获取应用注册中心数据有误，请过会再试。");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("appInfoJson is : " + appInfoJson);
        }
        model.addAttribute("total", appInfoJson.getJSONObject("page").getBigInteger("total"));
        List<JSONObject> pagesList = (List) appInfoJson.getJSONObject("page").get("list");
        model.addAttribute("pagesList", pagesList);
        model.addAttribute("appInfoJson", appInfoJson);
        return "pac_gl/policeAppView_mar";
    }

    @RequestMapping("/pageDetail")
    public String pageDetail(Model model, HttpServletRequest request)
            throws UnsupportedEncodingException {
        String pageId = request.getParameter("pageId");
        String pageName = URLDecoder.decode(request.getParameter("pageName"), "UTF-8");
        String pageDesc = URLDecoder.decode(request.getParameter("pageDesc"), "UTF-8");
        String composeNum = request.getParameter("composeNum");
        String appName = URLDecoder.decode(request.getParameter("appName"), "UTF-8");
        String pageUrl = request.getParameter("pageUrl");
        String appId = request.getParameter("appId");
        model.addAttribute("pageId", request.getParameter("pageId"));
        model.addAttribute("pageName", URLDecoder.decode(request.getParameter("pageName"), "UTF-8"));
        model.addAttribute("pageDesc", request.getParameter("pageDesc"));
        model.addAttribute("composeNum", request.getParameter("composeNum"));
        model.addAttribute("pageUrl", request.getParameter("pageUrl"));
        model.addAttribute("appId", request.getParameter("appId"));
        model.addAttribute("appName", URLDecoder.decode(request.getParameter("appName"), "UTF-8"));
        return "pac_gl/pageDetail";
    }

    @RequestMapping("/policeWidgetView")
    public String policeWidgetView(Model model, HttpSession session) {
        return "widgetFrame/manage";
    }

    @RequestMapping("/policeWidgetManage")
    public String policeWidgetManage(Model model, HttpSession session) {
        return "widgetFrame/finishedManage";
    }

    @RequestMapping("/setCss")
    public String setCss(Model model, HttpSession session) {
        return "modelFrame-tab/setCss";
    }

    @RequestMapping("/setData")
    public String setData(Model model, HttpSession session) {
        return "modelFrame-tab/setData";
    }

    @RequestMapping("/setDataMore")
    public String setDataMore(Model model, HttpSession session) {
        return "modelFrame-tab/setDataMore";
    }

    @RequestMapping("/setCssTxt")
    public String setCssTxt(Model model, HttpSession session) {
        return "modelFrame-tab/setCssTxt";
    }

    @RequestMapping("/setInfoCss")
    public String setInfoCss(Model model, HttpSession session) {
        return "modelFrame-tab/setInfoCss";
    }

    @RequestMapping("/setTableData")
    public String setTableData(Model model, HttpSession session) {
        return "modelFrame-tab/setTable";
    }

    @RequestMapping("/setTableCss")
    public String setTableCss(Model model, HttpSession session) {
        return "modelFrame-tab/setTableCss";
    }

    @RequestMapping("/setListData")
    public String setListData(Model model, HttpSession session) {
        return "modelFrame-tab/setList";
    }

    @RequestMapping("/setListCss")
    public String setListCss(Model model, HttpSession session) {
        return "modelFrame-tab/setListCss";
    }

    @RequestMapping("/setCssTxt1")
    public String setCssTxt1(Model model, HttpSession session) {
        return "modelFrame-tab/setCssTxt1";
    }

    @RequestMapping("/setListCss1")
    public String setListCss1(Model model, HttpSession session) {
        return "modelFrame-tab/setListCss1";
    }

    @RequestMapping("/setTraffivCss")
    public String setTraffivCss(Model model, HttpSession session) {
        return "modelFrame-tab/setTraffivCss";
    }

    @RequestMapping("/setTraffivData")
    public String setTraffivData(Model model, HttpSession session) {
        return "modelFrame-tab/setTraffivData";
    }

    @RequestMapping("/setMapData")
    public String setMapData(Model model, HttpSession session) {
        return "modelFrame-tab/setMap";
    }

    @RequestMapping("/setMapCityData")
    public String setMapCityData(Model model, HttpSession session) {
        return "modelFrame-tab/setMapCity";
    }

    @RequestMapping("/setMapCss")
    public String setMapCss(Model model, HttpSession session) {
        return "modelFrame-tab/setMapCss";
    }
    
    @RequestMapping("/setTitle")
    public String setTitle(Model model, HttpSession session) {
        return "modelFrame-tab/setTitle";
    }
    
    @RequestMapping("/setScreen")
    public String setScreen(Model model, HttpSession session) {
        return "modelFrame-tab/setScreen";
    }
    
    @RequestMapping("/setTab")
    public String setTab(Model model, HttpSession session) {
        return "modelFrame-tab/setTab";
    }
    
    @RequestMapping("/useTemplate")
    public String useTemplate(Model model, HttpSession session) {
        return "modelFrame-tab/useTemplate";
    }
    
    @RequestMapping("/setConnect")
    public String setConnect(Model model, HttpSession session) {
        return "modelFrame-tab/setConnect";
    }

    private JSONArray getEditApp(String userID) {
        Map<String, Object> param = new HashMap<>();
        param.put("status", Configs.PAGE_STATUS_EDIT.getIntCode());
        param.put("userId", userID);
        List<AppBaseInfo> appBaseInfoList = appBaseInfoService.queryAppNameIdList(param);
        String str = JsonUtil.toJSon(appBaseInfoList);
        return JSONArray.parseArray(str);
    }

    @RequestMapping("/newAppView")
    public String newAppView(Model model, HttpServletRequest request) {
        String appId = String.valueOf(request.getAttribute("appId"));
        request.setAttribute("appId", appId);
        return "pac_gl/newAppView";
    }

    @RequestMapping("/buildNewApp")
    public String buildNewApp(HttpServletRequest request, Model model) {
        String resource = request.getParameter("resource");
        if ("1".equals(resource)) {
            model.addAttribute("resource", "1");
        } else {
            model.addAttribute("resource", "0");
        }
        return "pac_gl/buildNewApp";
    }

    @RequestMapping("/toUrl")
    public String toUrl(HttpServletRequest request, Model model) {

        String url = request.getParameter("url");
        String[] commonParam = request.getParameterValues("commonParam");
        String json = JsonUtil.toJSon(commonParam);
        if (null != commonParam) {
            model.addAttribute("commonParam", json);
        }
        if (null != url) {
            return "pac_gl/" + url;
        }
        return "pac_gl/buildNewApp";
    }

    @RequestMapping("/viewMyBasket")
    public String viewMyBasket(HttpServletRequest request, Model model) {
        return "modelFrame-tab/view";
    }


    /**
     * 发布应用
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Map<String, Object> publish(HttpServletRequest request, HttpSession session, @RequestBody JSONObject
            jsonObject) {
        Map<String, Object> map = new HashMap<>();
        try {
            String file = jsonObject.getString("file");
            String appId = jsonObject.getString("appId");
            String rate = jsonObject.getString("rate");//[{"appType":"DCM","appId":"5","num":1},{"appType":"widget",
            // "appId":50,"num":2},{"appType":"widget","appId":42,"num":1},{"appType":"DIM","appId":"14","num":1},
            // {"appType":"DMMPDR","appId":"11","num":1},{"appType":"DIM","appId":"12","num":1}]
            String userId = UserUtils.getUserId(session);
            if (logger.isDebugEnabled()) {
                logger.debug("publish app the param is :  rate == " + rate);
            }
            if (StringUtils.isNullOrEmpty(file, appId, rate, userId)) {
                map.put(ResultStringKey.RESULT, "error");
                map.put(ResultStringKey.REASON, "数据有问题，请重试 ！");
                return map;
            }

            map = publishAppBusiness.publishApp(file, appId, rate, userId);
        } catch (Exception e) {
            logger.error("publish app error msg is == " + e);
            map.put(ResultStringKey.RESULT, "error");
            map.put(ResultStringKey.REASON, "服务器异常，请重试 ！");
            return map;
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public Map<String, String> file(HttpServletRequest request, HttpSession session, @RequestBody JSONObject
            jsonObject) {
        Map<String, String> map = new HashMap<>();
        String file = jsonObject.getString("file");
        String appId = jsonObject.getString("appId");
        String userName = UserUtils.getUserName(session);

        if (!StringUtils.isNullOrEmpty(file, appId, userName)) {
            String path = request.getSession().getServletContext().getRealPath("/");
            //根据appId获取 相应的控件组装关系，若此控件之前解压下来的已经不存在  则去再次下载解压
            try {
                widgetBusiness.createMissWidgetFile(appId, userName, path);
            } catch (Exception e) {
                logger.error(" createMissWidgetFile error " + e);
            }
            try {
                if (null == path) {
                    map.put(ResultStringKey.RESULT, "error");
                    map.put(ResultStringKey.REASON, "生成预览文件失败！");
                    return map;
                }
                File configFile = new File(path
                        + File.separator + "modelFrame-tab"
                        + File.separator + "data"
                        + File.separator + "preview.json");
                if (createFile(map, file, configFile))
                    return map;
                map.put(ResultStringKey.RESULT, "success");
            } catch (Exception e) {
                logger.error("create file error" + e);
                map.put(ResultStringKey.RESULT, "error");
                map.put(ResultStringKey.REASON, "服务器异常，请重试 ");
            }
        }
        return map;
    }

    @RequestMapping("/qingdan")
    public String editApp(HttpSession session, HttpServletRequest request) {
        //edit start 2017-10-09 (获得客户端浏览器类型)
        String from = request.getParameter("from");
        request.setAttribute("from", from);
        //edit end 【Richard.W】
        //get user info
        String userId = UserUtils.getUserId(session);
        String appId = request.getParameter("appId");
        Map<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        param.put("userid", userId);
        List<PackagingInfo> packagingInfos = coreService.getCoreInfoByAppId(param);
        String pageJson = JsonUtil.toJSon(packagingInfos);
        request.setAttribute("packagingDtos", JSONArray.parse(pageJson));
        request.setAttribute(ResultStringKey.RESULT, "sucess");
        if (null == appId) {
            appId = "-1";
        }
        request.setAttribute("appId", appId);
        //管理端  布局文件
        String file = request.getSession().getServletContext().getRealPath("/")
                + "modelFrame-tab/data/";
        SshUtil.validateConfigFile(appId, file);

        return "modelFrame-tab/manage";
    }


    private boolean createFile(Map<String, String> map, String file, File configFile) throws IOException {
        File filePath = new File(configFile.getParent());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        try (OutputStreamWriter fileWriter = new OutputStreamWriter(
                new FileOutputStream(configFile), "UTF-8")) {
            fileWriter.write(URLDecoder.decode(file, "utf-8"));
        } catch (Exception e) {
            logger.error(String.valueOf(e));
            map.put(ResultStringKey.RESULT, "error");
            map.put(ResultStringKey.REASON, "发布失败 ，请重试 ！");
            return true;
        }
        return false;
    }

    @RequestMapping("/myBasket")
    public String toMyBasket(Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("status", 6);
        List<AppBaseInfo> appNameIDList = appBaseInfoService.queryAppNameIdList(param);
        String str = JsonUtil.toJSon(appNameIDList);
        JSONArray appBaseInfoJsonArray = JSONArray.parseArray(str);
        model.addAttribute("appNameIDList", appBaseInfoJsonArray);
        return "pac_gl/myBasket";
    }

    @RequestMapping("/notice")
    public String notice(HttpSession session, Model model) {
        Map<String, Object> param = new HashMap<>();
        param.put("deleteFlag", "0");
        param.put("createUserId", UserUtils.getUserId(session));
        List<NoticeInfo> noticeInfos = noticeService.getNoticeInfos(param);
        model.addAttribute("noticeList", noticeInfos);
        Map<String, Object> paramApp = new HashMap<>();
        paramApp.put("status", 1);
        paramApp.put("userId", UserUtils.getUserId(session));
        List<AppBaseInfo> appNameIDList = appBaseInfoService.queryAppNameIdList(paramApp);
        model.addAttribute("appNameIDList", appNameIDList);
        return "pac_gl/notice";
    }

    @RequestMapping("/wait")
    public String wait(Model model) {
        return "pac_gl/wait";
    }

    /**
     * 直接保存为历史模板
     * 2017-11-6
     * @param request
     * @return
     * @throws Exception
     * @Author Richard.W
     */
    @ResponseBody
    @RequestMapping(value = "/saveConfigs", method = RequestMethod.POST)
    public Map<String, Object> saveConfigs(HttpServletRequest request,
                                           HttpSession session,
                                           @RequestBody JSONObject jsonObject)
    {
        Map<String, Object> map = new HashMap<>();
        try {
            String file = jsonObject.getString("file");
            String appId = jsonObject.getString("appId");
            String rate = jsonObject.getString("rate");
            //多接受一个参数：用户输入的希望保存的文件名
            String userTypeFileName = jsonObject.getString("fileName");
            //新增iframeId,可能会有多个，会用逗号分隔  1,2,3,4
            String iframeId = jsonObject.getString("iframeId");

//           String userTypeFileName = "testFileName2";

            //[{"appType":"DCM","appId":"5","num":1},{"appType":"widget",
            // "appId":50,"num":2},{"appType":"widget","appId":42,"num":1},{"appType":"DIM","appId":"14","num":1},
            // {"appType":"DMMPDR","appId":"11","num":1},{"appType":"DIM","appId":"12","num":1}]
            String userId = UserUtils.getUserId(session);
            if (logger.isDebugEnabled()) {
                logger.debug("save config file the param is :  rate == " + rate);
            }
            if (StringUtils.isNullOrEmpty(file, appId, rate, userId)) {
                map.put(ResultStringKey.RESULT, "error");
                map.put(ResultStringKey.REASON, "数据有问题，请重试 ！");
                return map;
            }
            //这个文件必须放置在项目中, 否则前端页面无法直接调用
//            String configsFilePath=request.getSession().getServletContext().getRealPath("/")
//                    + "modelFrame-tab/configs_saveas/";//展宏要求放在项目外,放置文件redeploy之后丢失
            String configsFilePath=request.getSession().getServletContext().getRealPath("/")
                    + "modelFrame-tab/configs_saveas/";
            map = publishAppBusiness.saveConfigs(file, appId, rate, userId
                    ,userTypeFileName, iframeId, configsFilePath);
        } catch (Exception e) {
            logger.error("publish app error msg is == " + e);
            map.put(ResultStringKey.RESULT, "error");
            map.put(ResultStringKey.REASON, "服务器异常，请重试 ！");
            return map;
        }
        return map;
    }

    @RequestMapping("/queryCategory")
    @ResponseBody
    public ResponseVo queryCategory(@RequestParam(value = "categoryType", required = false) String categoryType)
    {
        return widgetBusiness.queryCategory(categoryType);
    }

}
