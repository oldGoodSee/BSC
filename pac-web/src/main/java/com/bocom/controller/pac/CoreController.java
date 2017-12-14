package com.bocom.controller.pac;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.domain.pac.AppBaseInfo;
import com.bocom.domain.pac.PackagingInfo;
import com.bocom.domain.pac.PageInfo;
import com.bocom.domain.pac.ResultDo;
import com.bocom.dto.pac.PackagingDto;
import com.bocom.dto.session.SessionUserInfo;
import com.bocom.service.app.AppRestService;
import com.bocom.service.pac.AppBaseInfoService;
import com.bocom.service.pac.CoreService;
import com.bocom.support.servlet.ResultStringKey;
import com.bocom.util.ConfigGetPropertyUtil;
import com.bocom.util.JsonUtil;
import com.bocom.util.UserUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/manager/basket")
public class CoreController {

    // log
    private static Logger logger = LoggerFactory
            .getLogger(CoreController.class);

    @Resource
    private CoreService coreService;

    @Resource
    private AppBaseInfoService appBaseInfoService;

    @Resource
    private AppRestService appRestService;

    private static final String APP_ID = "appId";

    private static final String RESULT = "result";

    private static final String SUCCESS = "success";

    private static final String STATUS = "status";

    private static final String ERROR = "error";

    private static final String REASON = "reason";

    /**
     * 将选中的页面加入到组装清单中
     *
     * @param request packagingInfo  session
     * @return modelMap
     */
    @RequestMapping(value = "/addToBasket", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> addToBasket(HttpServletRequest request,
                                           PackagingInfo packagingInfo,
                                           HttpSession session) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            //get user info
            String userId = getUserId(session);
            String pageInfo = request.getParameter("pageInfo");
            pageInfo = URLDecoder.decode(pageInfo, "utf-8");
            JSONObject jsonObject = JSONObject.parseObject(pageInfo);
            if (jsonObject.get("funcList") != null) {
                JSONArray functionList = jsonObject.getJSONArray("funcList");
            /*
             * appid:"3",
			 * functionList:{"1,添加线索,/xx/eee/xxx,OappId,OappName","1,添加线索,/xx/eee/xxx,OappId,OappName"
			 * ,"1,添加线索,/xx/eee/xxx,OappId,OappName"}
			 */
                List<PackagingInfo> paramList = new ArrayList<>();
                setPackagingInfo(functionList, paramList);
                if (!paramList.isEmpty()) {
                    coreService.addToBasket(userId, paramList, UserUtils.getUserName(session));
                }
            }
            modelMap.put(RESULT, SUCCESS);
        } catch (Exception e) {
            logger.error("addToBasket is error " + e);
        }

        return modelMap;
    }

    /**
     * delete pages from basket
     *
     * @param request session
     * @return String
     */
    @RequestMapping(value = "/deleteFromBasket", method = RequestMethod.GET)
    public String deleteFromBasket(HttpServletRequest request, HttpSession session) {
        String pageInfo = request.getParameter("pageInfo");
        JSONObject jsonObject = JSONObject.parseObject(pageInfo);
        String appId = jsonObject.getString(APP_ID);
        JSONArray pageArray = jsonObject.getJSONArray("pageId");
        PackagingInfo packagingInfo = new PackagingInfo();
        String userId = getUserId(session);
        if (appId != null) {
            packagingInfo.setAppid(appId);
            packagingInfo.setUserid(userId);
        }
        if (pageArray != null) {
            if (pageArray.size() == 1) {
                packagingInfo.setPageid(pageArray.getString(0));
                packagingInfo.setPageids(null);
            } else if (pageArray.size() > 1) {
                packagingInfo.setPageid(null);
                List<String> pageids = new ArrayList<>();
                for (Object o : pageArray) {
                    pageids.add(String.valueOf(o));
                }
                if (!pageids.isEmpty()) {
                    packagingInfo.setPageids(pageids);
                }
            }
        }
        packagingInfo.setUserid(UserUtils.getUserId(session));
        //app_basket_item
        coreService.deleteFromBasket(packagingInfo);
        //app_config_info
        return "redirect:/manager/basket/getCoreInfoByAppId?appId=" + appId;
    }

    /**
     * 通过UserId来获取组装清单信息
     *
     * @param request session
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/getCoreInfoByAppId")
    public String getCoreInfoByUserId(HttpServletRequest request,
                                      HttpSession session) {
        //get user info
        String userId = getUserId(session);
        String appId = request.getParameter(APP_ID);
        List<PackagingDto> packagingDtos = new ArrayList<>();
        appId = getPackDtos(userId, appId, packagingDtos);
        String pageJson = JsonUtil.toJSon(packagingDtos);
        request.setAttribute("packagingDtos", JSONArray.parse(pageJson));
        request.setAttribute(RESULT, "sucess");
        if (null == appId) {
            appId = "-1";
        }
        request.setAttribute(APP_ID, appId);
        //get all app
        Map<String, Object> appParam = new HashMap<>();
        appParam.put(STATUS, 6);
        List<AppBaseInfo> appNameIDList = appBaseInfoService.queryAppNameIdList(appParam);
        String str = JsonUtil.toJSon(appNameIDList);
        JSONArray appBaseInfoJsonArray = JSONArray.parseArray(str);
        request.setAttribute("appNameIDList", appBaseInfoJsonArray);
        return "/pac_gl/myBasket";
    }

    private String getUserId(HttpSession session) {
        SessionUserInfo sessionUserInfo;
        if (session.getAttribute("sessionUserInfo") != null) {
            sessionUserInfo =
                    (SessionUserInfo) session.getAttribute("sessionUserInfo");
        } else {
            return "";
        }
        return String.valueOf(sessionUserInfo.getUserId());
    }

    //处理组装清单信息
    private void setPackagingInfo(JSONArray functionList, List<PackagingInfo> paramList) {
        for (Object temp : functionList) {
            String[] littleArray = String.valueOf(temp).split(",");
            PackagingInfo p = new PackagingInfo();
//            if (!"".equals(appId)) {
//                p.setAppid(appId);
//            }
            if (littleArray[0] != null) {
                p.setPageid(littleArray[0]);
            }
            if (littleArray[1] != null) {
                p.setPagename(littleArray[1]);
            }
            if (littleArray[2] != null) {
                p.setUrl(littleArray[2]);
            }
            if (littleArray[3] != null) {
                p.setOappid(littleArray[3]);
            }
            if (littleArray[4] != null) {
                p.setOappname(littleArray[4]);
            }
            paramList.add(p);
        }
    }

    @RequestMapping("/toSetRight")
    public String toSetRight2(HttpServletRequest request) {
        request.setAttribute(APP_ID, request.getParameter(APP_ID));
        return "/pac_gl/userRight";
    }


    /**
     * 组装packagingDto
     *
     * @param userId
     * @param appId
     * @param packagingDtos
     * @return
     */
    private String getPackDtos(String userId, String appId,
                               List<PackagingDto> packagingDtos) {
        String newAppId = appId;
        List<PackagingInfo> packagingInfos;
        Map<String, Object> param = new HashMap<>();
        param.put(APP_ID, appId);
        param.put("userid", userId);

        packagingInfos = coreService.getCoreInfoByAppId(param);
        List<String> oAppIdList = new ArrayList<>();//判断是否已经添加过
        for (PackagingInfo packagingInfo : packagingInfos) {
            String oappId = packagingInfo.getOappid();
            if (null == appId || "".equals(appId)) {
                newAppId = packagingInfo.getAppid();
            }
            if (oAppIdList.contains(oappId)) {
                continue;
            } else {
                oAppIdList.add(oappId);
            }
            PackagingDto packagingDto = new PackagingDto();
            BeanUtils.copyProperties(packagingInfo, packagingDto);
            List<PackagingInfo> infos = new ArrayList<>();
            for (PackagingInfo info : packagingInfos) {
                if (info.getOappid().equals(oappId)) {
                    infos.add(info);
                }
            }
            packagingDto.setPackagingInfos(infos);
            packagingDtos.add(packagingDto);
        }
        return newAppId;
    }

    /**
     * 更新应用状态
     *
     * @return map
     */
    @ResponseBody
    @RequestMapping("/updateAppStatus")
    public Map<String, String> updateAppStatus(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        try {
            String appId = request.getParameter(APP_ID);
            String status = request.getParameter(STATUS);
            if (StringUtils.isEmpty(appId.trim()) || StringUtils.isEmpty(status.trim())) {
                map.put(RESULT, ERROR);
                map.put(REASON, "数据错误。请重试！");
                return map;
            }
            Map<String, Object> param = new HashMap<>();
            param.put(APP_ID, appId);
            param.put(STATUS, status);
            appBaseInfoService.updateAppStatus(param);
            map.put(RESULT, SUCCESS);
        } catch (Exception e) {
            map.put(RESULT, ERROR);
            map.put(REASON, "系统错误，请联系管理员！");
            logger.error("updateAppStatus failed" + e);
        }
        return map;
    }


    /**
     * get data of market
     *
     * @return map
     */
    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    public String getData(HttpServletRequest request,
                          Model model, HttpSession session,
                          String appId, String searchType, String keyWord)
            throws UnsupportedEncodingException {
//        String appId=jsonObject.getString("appId");
//        String searchType=jsonObject.getString("searchType");//type 1:筛选 2:main search 3:sort
//        String keyWord=jsonObject.getString("keyWord");

        keyWord = URLDecoder.decode(keyWord, "UTF-8");
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("searchType", searchType);
        model.addAttribute("appId", appId);

        //筛选
        String userId = getUserId(session);
        JSONObject appInfoJson = null;
        if (keyWord == null || keyWord.trim().length() == 0) {
            appInfoJson = appRestService.queryAppByAppId(null);
        } else {
            String appName = request.getParameter("appName");
            String funcName = request.getParameter("funcName");
            if ("1".equals(searchType)) {//app
                appName = keyWord;
                funcName = "";
            } else if ("2".equals(searchType)) {//function
                appName = "";
                funcName = keyWord;
            }
            Map<String, Object> param = new HashMap<>();
            if (null != appName && !"".equals(appName)) {
//                查询的两个参数：
//                1、searchType 有两个值
//                 |--app   根据app信息模糊查询
//                        |--page 根据page信息模糊查询
//                2、searchParam 查询具体参数值
                param.put("searchParam", appName);
                param.put("searchType", "app");
            } else {
                param.put("searchParam", funcName);
                param.put("searchType", "page");
            }
            appInfoJson = appRestService.queryAppByAppId(param);
        }

        if (null == appInfoJson) {
            appInfoJson = new JSONObject();
            appInfoJson.put("data", "null");
            appInfoJson.put("msg", "error");
            appInfoJson.put("reason", "获取应用注册中心数据有误，请过会再试。");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("appInfoJson is : " + appInfoJson);
        }
        Map<String, Object> resultMap = coreService.getMarketData(appInfoJson);
        List<PageInfo> resultList = null;
        if (appId != null && appId.trim().length() > 0) {
            resultList = coreService.screenMarketData((List) resultMap.get("pagesList"), appId);
            {
                //get compose num
                resultList = coreService.transferComposeNum(resultList);
            }
            model.addAttribute("pagesList", resultList);
        } else {
            List<PageInfo> pagesList = (List) resultMap.get("pagesList");
            {
                pagesList = coreService.transferComposeNum(pagesList);
            }
            model.addAttribute("pagesList", pagesList);
        }

        model.addAttribute("appName", resultMap.get("appName"));

//        model.addAttribute("pagesList",resultList);

        return "pac_gl/policeAppView_new";
    }


    @RequestMapping("/getAppInfoDataNew")
    public Map<String, Object> getAppInfoDataListNew(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String appName = request.getParameter("appName");
            String funcName = request.getParameter("funcName");
            Map<String, Object> param = new HashMap<>();
            if (null != appName && !"".equals(appName)) {
//                查询的两个参数：
//                1、searchType 有两个值
//                 |--app   根据app信息模糊查询
//                        |--page 根据page信息模糊查询
//                2、searchParam 查询具体参数值
                param.put("searchParam", appName);
                param.put("searchType", "app");
            } else {
                param.put("searchParam", funcName);
                param.put("searchType", "page");
            }
            JSONObject appInfoJson = appRestService
                    .queryAppByAppId(param);
            if (logger.isDebugEnabled()) {
                logger.debug("queryAppByAppId param  is " + param);
                logger.debug("appInfoJson is : " + appInfoJson);
            }
            resultMap.put("result", "success");
            resultMap.put("data", appInfoJson);
        } catch (Exception e) {
            logger.debug("getAppInfoDataList failed : " + e);
            resultMap.put("result", "error");
            resultMap.put("reason", "get data failed!");
        }
        return resultMap;
    }

    /**
     * 获取篮子中的数量
     *
     * @return
     */
    @RequestMapping(value = "/getCountForBasket", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getCountForBasket(HttpSession session) {
        String userId = UserUtils.getUserId(session);
        JSONObject result = new JSONObject();
        int count = coreService.getCountForBasket(Integer.parseInt(userId));
        result.put("basketCount", count);
        return result;
    }

    /**
     * 将选中的页面加入到组装清单中new
     *
     * @param request packagingInfo  session
     * @return modelMap
     */
    @RequestMapping(value = "/addToBasketNew", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> addToBasketNew(HttpServletRequest request,
                                              HttpSession session)
            throws UnsupportedEncodingException {

        String appuuid = request.getParameter("appuuid");
        String pageid = request.getParameter("pageid");
        String pagename = URLDecoder.decode(request.getParameter("pagename"), "UTF-8");
        String pageurl = request.getParameter("pageurl");
        String appid = request.getParameter("appid");
        String appname = URLDecoder.decode(request.getParameter("appname"), "UTF-8");
        Map<String, Object> modelMap = new HashMap<>();
        Map<String, String> param = new HashMap<>();
        param.put("pageid", pageid);
        param.put("pagename", pagename);
        param.put("pageurl", pageurl);
        param.put("appid", appid);
        param.put("appname", appname);
        try {
            //get user info
            String userId = getUserId(session);
            param.put("userid", userId);
            int result = coreService.addToBasketNew(userId, param, UserUtils.getUserName(session));
            if (result != 0) {
                modelMap.put(RESULT, ERROR);
                modelMap.put(REASON, "已存在篮子中 !");
            } else {
                modelMap.put(RESULT, SUCCESS);
            }
        } catch (Exception e) {
            logger.error("addToBasket is error " + e);
        }

        return modelMap;
    }
}
