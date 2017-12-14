package com.bocom.controller.pac;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bocom.business.WidgetBusiness;
import com.bocom.domain.pac.AppWidgetInfo;
import com.bocom.domain.pac.AppWidgetInitInfo;
import com.bocom.dto.session.SessionUserInfo;
import com.bocom.enums.DictionaryEnums;
import com.bocom.service.pac.BaseService;
import com.bocom.service.pac.NoticeService;
import com.bocom.service.pac.WidgetService;
import com.bocom.service.pac.WidgetServiceService;
import com.bocom.service.pac.impl.BaseServiceImpl;
import com.bocom.support.servlet.ResultStringKey;
import com.bocom.util.*;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/manager/widget")
public class WidgetController {

    private static Logger logger = LoggerFactory
            .getLogger(WidgetController.class);

    private static final String WIDGET_NAME = "widgetName";

    private static final String INIT_ID = "initId";

    private static final String PAGE_NUM = "pageNum";

    private static final String PAGE_SIZE = "pageSize";
    private static final String WIDGET_TYPE_ID = "widgetTypeId";

    private static final String MSG = "数据错误，请重试！";

    private static final String MSG_ERROR = "服务器异常，请稍后重试！";

    private static final String MSG_PARAM = "参数不合法、请重试！";

    @Resource
    private WidgetBusiness widgetBusiness;

    @Resource
    private WidgetService widgetService;

    @Resource
    private NoticeService noticeService;

    @Resource
    private WidgetServiceService widgetServiceService;

    private int pageSize = 20;


    /**
     * 保存发布的微应用（widget） 压缩整个控件目录  并上传到文件服务器
     *
     * @param request
     * @param session
     * @return 是否成功
     * @throws IOException
     */
    @RequestMapping(value = "/addWidget", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addWidget(HttpServletRequest request, HttpSession session) throws IOException {
        JSONObject result = new JSONObject();
        try {
            String path = request.getSession().getServletContext().getRealPath("/");//资源目录路径
            String widgetName = request.getParameter(WIDGET_NAME);//控件名称
            String doc = request.getParameter("doc");//html标签  内容
            String initId = request.getParameter(INIT_ID);// 为初始化时候控件的ID
            String widgetType = request.getParameter("widgetType");// 控件类型
            String typeCode1 = request.getParameter("typeCode1");// 一级分类
            String typeCode2 = request.getParameter("typeCode2");// 二级分类
            String typeCode2Name = request.getParameter("typeCode2Name");// 二级分类
            String appImg = request.getParameter("appImg");//20171201added
            StringBuilder sql = new StringBuilder();
            if (!StringUtils.isNullOrEmpty(widgetType) && Integer.parseInt(widgetType) == 2) {
                String list = request.getParameter("list");// 为初始化时候控件的ID
                JSONArray jsonArray = JSONArray.parseArray(list);
                for (Object object : jsonArray) {
                    JSONObject jsonObject = JSONObject.parseObject(object.toString());
                    sql.append("区域为: " + jsonObject.getString("areaId"));
                    sql.append(";数据库为: " + jsonObject.getString("dbId"));
                    sql.append(" ;查询sql 为 : " + jsonObject.getString("sql"));
                }
            }
            String userName = UserUtils.getUserName(session);
            if (!StringUtils.isNullOrEmpty(doc, widgetName, initId)) {
                String widgetDecoder = URLDecoder.decode(widgetName, "UTF-8");
                Map<String, Object> param = new HashMap<>();
                param.put(WIDGET_NAME, widgetDecoder);
                param.put(ResultStringKey.USER_ID, UserUtils.getUserName(session));
                int checkWidgetByName = widgetBusiness.getCountByAppName(param);
                if (checkWidgetByName > 0) {
                    result.put(ResultStringKey.SUCCESS, false);
                    result.put(ResultStringKey.REASON, "微应用名称重复、请修改！");
                    return result;
                }
                AppWidgetInfo appWidgetInfo = new AppWidgetInfo();
                appWidgetInfo.setCreateType(1);//创建类型、组织创建？ 个人创建？ 暂时定为个人吧
                appWidgetInfo.setCreateId(userName);//创建人ID
                appWidgetInfo.setCreateTime(new Date());
//                appWidgetInfo.setPath(ConfigGetPropertyUtil.get("fastDFS.http.url"));//上传到文件服务器的ip
                appWidgetInfo.setStatus(1);//状态： 1 运行中
                appWidgetInfo.setWidgetName(widgetDecoder);
                appWidgetInfo.setIp(request.getRemoteAddr());
                appWidgetInfo.setWidgetInitId(Integer.parseInt(initId));
                appWidgetInfo.setCreateUserName(userName);
                appWidgetInfo.setVersion(1);//新增的控件版本号为0  更新一次加1
                appWidgetInfo.setAppImg(appImg);//20171201added Richard.W
                if (typeCode1 != null && typeCode1.trim().length() > 0
                        && !"null".equals(typeCode1)) {
                    appWidgetInfo.setTypeCode1(Integer.parseInt(typeCode1));
                }
                if (typeCode2 != null && typeCode2.trim().length() > 0
                        && !"null".equals(typeCode2)) {
                    appWidgetInfo.setTypeCode2(Integer.parseInt(typeCode2));
                    appWidgetInfo.setTypeName2(StringUtils.isNullOrEmpty(typeCode2Name) ? "" : typeCode2Name);
                }
                result = widgetBusiness.saveWidget(path, doc, appWidgetInfo, sql.toString());

                if (result.getBoolean(ResultStringKey.SUCCESS)) {
                    noticeMAR(request, session, appWidgetInfo);
                }

                return result;
            }
        } catch (Exception e) {
            logger.error("addWidget error msg >>>>>>>" + e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, "服务器异常，请重试！");
            return result;
        }
        result.put(ResultStringKey.SUCCESS, false);
        result.put(ResultStringKey.REASON, MSG_PARAM);
        return result;
    }

    private void noticeMAR(HttpServletRequest request, HttpSession session, AppWidgetInfo appWidgetInfo) {
        //notice MAR
        try {
            JSONObject mAppParam = new JSONObject();
            mAppParam.put("mAppId", appWidgetInfo.getWidgetId());
            mAppParam.put("mAppName", appWidgetInfo.getWidgetName());
            mAppParam.put("mAppDesc", "");
            mAppParam.put("mAppVersion", "1.0.0");
            mAppParam.put("localAppId", "");
            mAppParam.put("appType", 1);//0代表PAC 1代表BSC
            mAppParam.put("localAppVersion", "");
            mAppParam.put("deployUrl", request.getScheme() + "://" + request.getServerName() + ":" + request
                    .getServerPort() + request.getContextPath());
            mAppParam.put("visitUrl", "/widgetFrame" + File.separator + "app"
                    + File.separator + appWidgetInfo.getFolderName()
                    + File.separator + "widget_" + appWidgetInfo.getWidgetId() + ".html");
            mAppParam.put("mAppSource", "1");
            SessionUserInfo sessionUserInfo = null;
            if (session.getAttribute("sessionUserInfo") != null) {
                sessionUserInfo =
                        (SessionUserInfo) session.getAttribute("sessionUserInfo");
            }
            mAppParam.put("orgCode", sessionUserInfo.getOrgCode());
            mAppParam.put("orgName", sessionUserInfo.getOrgName());
            mAppParam.put("createBy", appWidgetInfo.getCreateId());
            mAppParam.put("createByName", appWidgetInfo.getCreateUserName());
            mAppParam.put("skillType", "9");//公安：  消防：
            mAppParam.put("skillTypeName", "其他软件类");//todo
//            mAppParam.put("businessType", "16");
            mAppParam.put("businessTypeName", appWidgetInfo.getTypeName2());

            JSONArray mImg = new JSONArray();
            JSONObject img = new JSONObject();
            img.put("mAppId", appWidgetInfo.getWidgetId());
            img.put("mAppImg", ConfigGetPropertyUtil.get("fastDFS.http.url") + appWidgetInfo.getAppImg());
            mImg.add(img);
            mAppParam.put("mAppImg", mImg);//图片暂无
            mAppParam.put("firstCategory", appWidgetInfo.getTypeCode1());
            mAppParam.put("businessType", appWidgetInfo.getTypeCode2());
            String dependDataStr = request.getParameter("list");//depend info
            String widgetType = request.getParameter("widgetType");//1:application 2:data 3:template
            JSONArray dependArray = new JSONArray();
            JSONArray paramDependArray = new JSONArray();
            if (dependDataStr != null) {
                dependArray = JSONArray.parseArray(dependDataStr);
                for (int i = 0; i < dependArray.size(); i++) {
                    JSONObject depend = dependArray.getJSONObject(i);
                    if (!"2".equals(widgetType)) {
                        //not data widget
//                        JSONObject serverJson = new JSONObject();
//                        serverJson.put("serviceName", "");
//                        serverJson.put("serviceIp", "");
//                        serverJson.put("servicePort", "");
//                        serverJson.put("servicrDesc", "");
//                        serverJson.put("serviceType", "");
//                        serverJson.put("serviceTypeName", "");
//                        paramDependArray.add(serverJson);
                        mAppParam.put("mAppService", paramDependArray);//非数据控件会有依赖的服务、接口
                    } else {
                        //data widget
//                        JSONObject serverJson = new JSONObject();
//                        serverJson.put("instanceName", "");
//                        serverJson.put("dbDesc", "");
//                        serverJson.put("dbVersion", "");
//                        serverJson.put("dbIp", "");
//                        serverJson.put("dbPort", "");
//                        serverJson.put("dbLoginName", "");
//                        serverJson.put("dbLoginPass", "");
//                        serverJson.put("dbType", "");
//                        serverJson.put("dbTypeName", "");
//                        serverJson.put("areaId", "");
//                        serverJson.put("areaName", "");
//                        serverJson.put("dataType", "");
//                        paramDependArray.add(serverJson);
                        mAppParam.put("mAppDb", paramDependArray);//数据控件会有依赖的数据源
                    }
                    break;
                }
            }
            String registerInfo = HttpClientUtil.postBase64(ConfigGetPropertyUtil.get("rest.mar.addMappInfo.url")
                    , mAppParam.toJSONString(), "UTF-8");
            if (logger.isDebugEnabled()) {
                logger.debug("noticeMAR mAppParam.toJSONString is: " + mAppParam.toJSONString());
                logger.debug("noticeMAR regist result is : " + registerInfo);
            }
        } catch (Exception e) {
            logger.error("noticeMAR error" + e);
        }
    }

    /**
     * 更新微应用：在已组装微应用页面发布时候 调用此方法，用来更新已发布的微应用
     *
     * @param request
     * @param session
     * @return JSONObject
     * @throws IOException
     */
    @RequestMapping(value = "/updateWidget", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateWidget(HttpServletRequest request, HttpSession session) throws IOException {
        JSONObject result = new JSONObject();
        try {
            String path = request.getSession().getServletContext().getRealPath("/");//资源目录路径
            String widgetName = request.getParameter(WIDGET_NAME);//控件名称
            String doc = request.getParameter("doc");//html标签  内容
            String widgetId = request.getParameter(ResultStringKey.WIDGET_ID);//html标签  内容
            if (!StringUtils.isNullOrEmpty(doc, widgetName, widgetId)) {
                result = widgetBusiness.updateWidget(path, widgetName, doc, widgetId, UserUtils.getUserName(session));
                return result;
            }
        } catch (Exception e) {
            logger.error("updateWidget error msg >>>>>>>" + e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, "服务器异常，请重试！");
            return result;
        }
        result.put(ResultStringKey.SUCCESS, false);
        result.put(ResultStringKey.REASON, MSG_PARAM);
        return result;
    }

    /**
     * 下载可用的控件 并返回前端这个控件的html路径
     *
     * @param request
     * @param session
     * @return 返回前端这个控件的html路径
     * @throws IOException
     */

    @RequestMapping(value = "/downloadWidget", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject downloadWidget(HttpServletRequest request, HttpSession session) throws IOException {
        JSONObject result = new JSONObject();
        try {
            String path = request.getSession().getServletContext().getRealPath("/");
            String widgetId = request.getParameter(ResultStringKey.WIDGET_ID);
            if (!StringUtils.isNullOrEmpty(widgetId)) {
                result = widgetBusiness.unzipWidgetFile(widgetId, path);
                if (logger.isDebugEnabled()) {
                    logger.debug("downloadWidget result msg >>>>>>>" + result.toJSONString());
                }
                return result;
            }
        } catch (Exception e) {
            logger.error("downloadWidget error msg >>>>>>>" + e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, "服务器问题、请重试！");
            return result;
        }
        result.put(ResultStringKey.SUCCESS, false);
        result.put(ResultStringKey.REASON, MSG_PARAM);
        return result;
    }

    /**
     * 获取已发布的微应用列表
     *
     * @param request
     * @param session
     * @return JSONObject
     */
    @RequestMapping(value = "/getWidgetList", method = RequestMethod.GET)
    @ResponseBody
    public Map getWidgetList(HttpServletRequest request, HttpSession session) {
        PageInfo pageInfo = null;
        JSONObject result = new JSONObject();
        try {
            String userName = UserUtils.getUserName(session);
            if ("".equals(userName)) {
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.DATA, "");
                result.put(ResultStringKey.REASON, "获取用户信息失败，请重新登录！");
                return result;
            }
            String typeCode1 = request.getParameter("typeCode1");
            String typeCode2 = request.getParameter("typeCode2");
            Map<String, Object> param = new HashMap<>();
            param.put("typeCode1", typeCode1);
            param.put("typeCode2", typeCode2);
            PageUtil.setParams(request, param);
            List<AppWidgetInfo> widgetList = widgetBusiness.getAppWidgetList(param, userName);
            pageInfo = new PageInfo(widgetList);
            if (logger.isDebugEnabled()) {
                logger.debug("widgetList is = " + widgetList);
            }
        } catch (Exception e) {
            logger.error("getWidgetList error msg >>>>>>>", e);
            pageInfo = null;
        }
        return PageUtil.covertMap(new Object[]{"page"},
                new Object[]{pageInfo});
    }

    /**
     * 删除微应用
     *
     * @param request
     * @param session
     * @return JSONObject
     */
    @RequestMapping("/deleteWidget")
    @ResponseBody
    public JSONObject deleteWidget(HttpServletRequest request, HttpSession session) {
        JSONObject result = new JSONObject();
        try {
            String widgetId = request.getParameter(ResultStringKey.WIDGET_ID);
            if (widgetId != null && !"".equals(widgetId)) {
                String path = request.getSession().getServletContext().getRealPath("/");
                widgetBusiness.deleteWidget(widgetId, path, "");
            } else {
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.REASON, MSG);
                logger.error("deleteWidget error ; reason : widgetId is null !! ");
                return result;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("delete widget ;widgetId is = " + widgetId);
            }
        } catch (Exception e) {
            logger.error("delete error " + e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, "服务器问题、请重试");
            return result;
        }
        result.put(ResultStringKey.SUCCESS, true);
        return result;
    }

    /**
     * 校验微应用是否被组装应用所组装  为删除前做校验准备
     *
     * @param request
     * @param session
     * @return JSONObject
     */
    @RequestMapping("/checkWidget")
    @ResponseBody
    public JSONObject checkRelation(HttpServletRequest request, HttpSession session) {
        JSONObject result = new JSONObject();
        String widgetId = request.getParameter(ResultStringKey.WIDGET_ID);
        if (widgetId != null && !"".equals(widgetId)) {
            result = widgetBusiness.checkRelation(widgetId, "widget");
        } else {
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, MSG);
            logger.error("deleteWidget error ; reason : widgetId is null !! ");
            return result;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("checkRelation result is  " + result);
        }
        return result;
    }

    /**
     * 更新微应用状态
     *
     * @param request
     * @param session
     * @return JSONObject
     */
    @RequestMapping("/updateWidgetStatus")
    @ResponseBody
    public JSONObject updateStatus(HttpServletRequest request, HttpSession session) {
        JSONObject result = new JSONObject();
        String status = request.getParameter("status");
        String widgetId = request.getParameter("id");
        boolean flag = (status != null && !"".equals(status)) && (widgetId != null && !"".equals(widgetId));
        if (flag) {
            Map<String, Object> param = new HashMap<>();
            param.put("status", status);
            param.put(ResultStringKey.WIDGET_ID, widgetId);
            widgetBusiness.updateStatus(param, null);
        } else {
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, MSG);
            logger.error("updateStatus error ; reason : status is null !! ");
            return result;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("updateStatus widget ;widgetId is = " + widgetId + "  and status is = " + status);
        }
        result.put(ResultStringKey.SUCCESS, true);
        return result;
    }

    /**
     * 从控件库获取初始化控件列表
     *
     * @param request
     * @param session
     * @return JSONObject
     */
    @RequestMapping("/getWidgetInitList")
    @ResponseBody
    public JSONObject getWidgetInitList(HttpServletRequest request, HttpSession session) {
        JSONObject result = new JSONObject();
        try {
            JSONObject paramJson = new JSONObject();
            String pageNum = request.getParameter(PAGE_NUM);
            String pageSize = request.getParameter(PAGE_SIZE);
            //20171117added
            String typeCode1 = request.getParameter("typeCode1");//一级分类
            String typeCode2 = request.getParameter("typeCode2");//二级分类
//            typeCode1="1";  //3
//            typeCode2="2";//2
            if (typeCode1 == null || typeCode1.trim().length() == 0 || "null".equals(typeCode1)) {
                typeCode1 = null;
            } else {
                paramJson.put("firstCategoryId", Integer.parseInt(typeCode1));
            }
            if (typeCode2 == null || typeCode2.trim().length() == 0 || "null".equals(typeCode2)) {
                typeCode2 = null;
            } else {
                paramJson.put("sencondCategoryId", Integer.parseInt(typeCode2));
            }
            //20170523 新增了控件类型
            String widgetTypeId = request.getParameter(WIDGET_TYPE_ID);

            if (!StringUtils.isNullOrEmpty(pageNum, pageSize)
                    && StringUtils.isNumber(pageNum, pageSize)) {
                paramJson.put(PAGE_NUM, Integer.parseInt(pageNum));
                paramJson.put(PAGE_SIZE, Integer.parseInt(pageSize));
            } else {
                paramJson.put(PAGE_NUM, 1);
                paramJson.put(PAGE_SIZE, 10);
            }
//            widgetTypeId="2";//todo 这是测试代码，使用完毕之后必须删掉，否则很惨
            paramJson.put(WIDGET_TYPE_ID, Integer.parseInt(widgetTypeId));
            paramJson.put(ResultStringKey.USER_NAME, UserUtils.getUserName(session));
            JSONObject wrmWidgetInitInfo = widgetBusiness.getWidgetInitList(paramJson);
            if (logger.isDebugEnabled()) {
                logger.debug(" get widgetListInitInfo from wrm is ===" + wrmWidgetInitInfo);
            }
            if (wrmWidgetInitInfo.getBoolean(ResultStringKey.SUCCESS)) {
                JSONObject resultJsonWrm = wrmWidgetInitInfo.getJSONObject(ResultStringKey.DATA);
                resultJsonWrm.put(ResultStringKey.SUCCESS, true);
                return resultJsonWrm;
            } else {
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.REASON, "控件库服务器有异常，请重试！");
                return result;
            }
        } catch (JSONException e) {
            logger.debug("getWidgetInitList error ;msg is = " + e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.DATA, "");
            result.put(ResultStringKey.REASON, "控件库服务器有异常，请重试！");
        } catch (Exception e) {
            logger.debug("getWidgetInitList error ;msg is = " + e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.DATA, "");
            result.put(ResultStringKey.REASON, MSG_ERROR);
        }
        return result;
    }

    /**
     * 保存初始化的控件信息
     *
     * @param session    session
     * @param jsonObject param
     * @param request    request
     * @return JSONObject
     */
    @RequestMapping("/insertWidgetInit")
    @ResponseBody
    public JSONObject insertWidgetInit(HttpSession session, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        try {
            //控件名称，控件版本，和控件ID
            String widgetName = request.getParameter(WIDGET_NAME);
            String version = request.getParameter("version");
            String wrmId = request.getParameter(ResultStringKey.WIDGET_ID);
            if (!StringUtils.isNullOrEmpty(widgetName, version, wrmId)) {
                AppWidgetInitInfo appWidgetInitInfo = new AppWidgetInitInfo();
                appWidgetInitInfo.setCreateTime(new Date());
                appWidgetInitInfo.setCreateIp(UserUtils.getIp(request));
                appWidgetInitInfo.setCreateUserID(UserUtils.getUserId(session));
                appWidgetInitInfo.setWidgetName(URLDecoder.decode(widgetName, "utf-8"));
                appWidgetInitInfo.setStatus(0);//暂时还不可用，未解压到本项目内
                appWidgetInitInfo.setVersion(version);
                appWidgetInitInfo.setWrmId(Integer.parseInt(wrmId));//上传成功返还回来的 WRM 的控件ID
                appWidgetInitInfo.setType("upload");//类型，说明此控件是由我们上传上去的
                int resultInt = widgetBusiness.insertWidgetInit(appWidgetInitInfo);
                if (resultInt > 0) {
                    result.put(ResultStringKey.SUCCESS, true);
                }
            }
        } catch (Exception e) {
            logger.debug("insertWidgetInit error ;msg is = " + e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, "服务器异常，请重试！ ");
        }
        return result;
    }

    /**
     * `
     * 初始化
     *
     * @param request
     * @param session
     * @return JSONObject
     */
    @RequestMapping("/initWidget")
    @ResponseBody
    public JSONObject initWidget(HttpSession session, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        try {
            //初始化控件ID
            String initId = request.getParameter(INIT_ID);
            String widgetName = request.getParameter("widgetName");
            String version = request.getParameter("version");
            String dfsUrl = request.getParameter("dfsUrl");
            if (!StringUtils.isNullOrEmpty(initId, version, widgetName, dfsUrl)) {
                String path = request.getSession().getServletContext().getRealPath("/");//资源目录路径
                result = widgetBusiness.initWidget(Integer.parseInt(initId), path, UserUtils.getUserName(session),
                        version, URLDecoder.decode(widgetName, "UTF-8"), dfsUrl);
            } else {
                result.put(ResultStringKey.SUCCESS, false);
                result.put(ResultStringKey.REASON, "参数不对，请重试！");
            }
        } catch (Exception e) {
            logger.debug("insertWidgetInit error ;msg is = " + e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, MSG_ERROR);
        }
        return result;
    }

    /**
     * 初始化
     *
     * @param request
     * @param session
     * @return JSONObject
     */
    @RequestMapping("/deleteInitWidget")
    @ResponseBody
    public JSONObject deleteInitWidget(HttpSession session, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        try {
            //初始化控件ID
            String initId = request.getParameter(INIT_ID);
            if (!StringUtils.isNullOrEmpty(initId)) {
                widgetBusiness.deleteInitWidget(Integer.parseInt(initId));
                result.put(ResultStringKey.SUCCESS, true);
            }
        } catch (Exception e) {
            logger.debug("deleteInitWidget error ;msg is = " + e);
            result.put(ResultStringKey.SUCCESS, false);
            result.put(ResultStringKey.REASON, MSG_ERROR);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/interfaceList", method = RequestMethod.GET)
    public JSONObject interfaceList(HttpServletRequest request)
            throws UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        String interfaceName = request.getParameter("interfaceName");
        if (interfaceName != null)
            interfaceName = URLDecoder.decode(interfaceName, "UTF-8");
        String type = request.getParameter("type");
        String inputCount = request.getParameter("inputCount");
        if (StringUtils.isNullOrEmpty(type, inputCount)) {
            result.put(ResultStringKey.REASON, "非法请求，请重试！");
            result.put(ResultStringKey.SUCCESS, false);
            return result;
        }
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("type", type);
            param.put("inputCount", inputCount);
            param.put("interfaceName", interfaceName);
            List<Map<String, Object>> map = widgetServiceService.interfaceList(param);
            result.put(ResultStringKey.DATA, map);
            result.put(ResultStringKey.SUCCESS, true);
            return result;
        } catch (Exception e) {
            logger.error("get interfaceList error == " + e);
            result.put(ResultStringKey.REASON, "服务器异常，请重试！");
            result.put(ResultStringKey.SUCCESS, false);
            return result;
        }

    }

    @ResponseBody
    @RequestMapping(value = "/interfaceListByWidget", method = RequestMethod.GET)
    public JSONObject interfaceListByWidget(HttpServletRequest request)
            throws UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        String interfaceName = request.getParameter("interfaceName");
        if (interfaceName != null)
            interfaceName = URLDecoder.decode(interfaceName, "UTF-8");
        String type = request.getParameter("type");
        String inputType = request.getParameter("inputType");
        if (StringUtils.isNullOrEmpty(type, inputType)) {
            result.put(ResultStringKey.REASON, "非法请求，请重试！");
            result.put(ResultStringKey.SUCCESS, false);
            return result;
        }
        try {
            int txtCount = 0;
            int selectCount = 0;
            int dateCount = 0;
            String[] inpuntTypeList = inputType.split(",");
            for (String str : inpuntTypeList) {
                switch (Integer.parseInt(str.split("_")[0])) {
                    case 1:
                        txtCount = Integer.parseInt(str.split("_")[1]);
                        break;
                    case 2:
                        selectCount = Integer.parseInt(str.split("_")[1]);
                        break;
                    case 3:
                        dateCount = Integer.parseInt(str.split("_")[1]);
                        break;
                    default:
                        break;
                }
            }
            int count = txtCount + selectCount + dateCount;
            Map<String, Object> param = new HashMap<>();
            param.put("type", type);
            param.put("txt", txtCount);
            param.put("select", selectCount);
            param.put("date", dateCount);
            param.put("inputCount", count);
            param.put("interfaceName", interfaceName);
            List<Map<String, Object>> map = widgetServiceService.interfaceListByWidget(param);
            result.put(ResultStringKey.DATA, map);
            result.put(ResultStringKey.SUCCESS, true);
            return result;
        } catch (Exception e) {
            logger.error("get interfaceList error == " + e);
            result.put(ResultStringKey.REASON, "服务器异常，请重试！");
            result.put(ResultStringKey.SUCCESS, false);
            return result;
        }

    }

    @ResponseBody
    @RequestMapping(value = "/shareWidget", method = RequestMethod.GET)
    public JSONObject shareWidget(HttpServletRequest request) {
        JSONObject result = new JSONObject();


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
            JSONArray newData = new JSONArray();
            Map<String, Map> dbMap = new HashMap<String, Map>();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    JSONObject o = data.getJSONObject(i);
                    if (o.getString("dbCategoryName") != null &&
                            o.getString("dbCategoryName").equalsIgnoreCase("mysql")) {
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

    @RequestMapping("/uploadPhoto1")
    public void uploadLogoWebPhoto(@RequestParam MultipartFile logoWeb,
                                   HttpServletResponse response) {
        try {
            ResponseVo responseVo = widgetService.uploadPhoto(logoWeb);
            writePrint(responseVo, response);
        } catch (Exception e) {
            logger.error("上传图片错误：" + e.getMessage());
        }
    }

    /**
     * 通过response返回信息，处理ie浏览器图片上传接受返回值出现问题，通过response返回页面
     *
     * @param responseVo
     * @param resp
     */
    private static void writePrint(ResponseVo responseVo,
                                   HttpServletResponse resp) {
        resp.setContentType("text/plain; charset=UTF-8");
        try {
            String json = JsonUtil.toJSon(responseVo);
            resp.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
