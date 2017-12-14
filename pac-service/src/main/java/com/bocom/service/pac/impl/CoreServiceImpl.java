package com.bocom.service.pac.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.dao.AppBaseInfoDao;
import com.bocom.dao.AppBasketItemDao;
import com.bocom.dao.CoreDAO;
import com.bocom.domain.UserRoleOrg;
import com.bocom.domain.pac.*;
import com.bocom.dto.pac.UserDto;
import com.bocom.enums.DictionaryEnums;
import com.bocom.service.app.AppRestService;
import com.bocom.service.pac.CoreService;
import com.bocom.service.pac.WidgetService;
import com.bocom.support.exception.MyRunTimeException;
import com.bocom.support.servlet.ResultStringKey;
import com.bocom.util.Behavior.Configs;
import com.bocom.util.ConfigGetPropertyUtil;
import com.bocom.util.JsonUtil;
import com.bocom.util.PageUtil;
import com.bocom.util.StringUtils;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by Administrator on 2016/12/5.
 */
@Service
public class CoreServiceImpl extends BaseServiceImpl implements CoreService {

    // log
    private static Logger logger = LoggerFactory
            .getLogger(CoreService.class);
    @Resource
    private CoreDAO coreDAO;

    @Resource
    private AppBaseInfoDao appBaseInfoDao;

    @Resource
    private AppBasketItemDao appBasketItemDao;

    @Resource
    private AppRestService appRestService;

    @Resource
    private WidgetService widgetService;


    private static final String SCREEN_NAME = "screenName";

    /**
     * 将选中的页面功能加入到篮子中
     *
     * @param appId
     * @param paramList
     */
    @Override
    @Transactional
    public void addToBasket(String userId, List<PackagingInfo> paramList, String userName) {
        String basketId = null;
        if (userId != null && !userId.isEmpty()) {
            basketId = getBasketId(userId, userName);//判断此人是否有篮子？  有 则得到此篮子ID  无  则创建一个篮子 并返回此篮子ID
        }

        if (paramList != null && !paramList.isEmpty()) {
            saveBasketExceptDuplicate(userId, paramList, basketId);
        }
    }

    //保存篮子里的东西，并去掉重复的（不重复保存）
    private void saveBasketExceptDuplicate(String userId, List<PackagingInfo> paramList, String basketId) {
        for (PackagingInfo p : paramList) {
            if (basketId != null) {
                p.setId(new Integer(basketId));
                coreDAO.addToBasket(p);
                super.saveLog(DictionaryEnums.ACTION_ADD.getDictionaryCode(), DictionaryEnums.BUSINESS_BASKET
                                .getDictionaryCode(),
                        "增加功能 : " + p.getPagename());
            }
        }
    }

    private String getBasketId(String userId, String userName) {
        String basketId;
        PackagingInfo packagingInfo = new PackagingInfo();
        packagingInfo.setCreatetime(new Date());
        packagingInfo.setUserid(userId);
        basketId = coreDAO.isBasketExist(packagingInfo);
        if (basketId == null) {//判断篮子基础信息是否存在,如果不存在则自动创建
            super.saveLog(DictionaryEnums.ACTION_ADD.getDictionaryCode(), DictionaryEnums.BUSINESS_BASKET
                            .getDictionaryCode(),
                    "为用户 ： " + userName + " 创建应用篮子");
            coreDAO.addNewBasket(packagingInfo);
            basketId = String.valueOf(packagingInfo.getId());
            if (logger.isDebugEnabled()) {
                logger.debug(" basketid: " + basketId);
            }
        }
        return basketId;
    }

    /**
     * 从篮子中移除页面功能
     *
     * @param packagingInfo
     */
    @Override
    @Transactional
    public void deleteFromBasket(PackagingInfo packagingInfo) {
        try {
            List<AppBasketItemInfo> basketItemInfos = appBasketItemDao.queryBasketInfo(packagingInfo);
            List<String> pageNameList = new ArrayList<>();
            for (AppBasketItemInfo appBasketItemInfo : basketItemInfos) {
                pageNameList.add(appBasketItemInfo.getPageName());
            }
            super.saveLog(DictionaryEnums.ACTION_DELETE.getDictionaryCode(), DictionaryEnums.BUSINESS_BASKET
                            .getDictionaryCode(),
                    "删除功能 : " + pageNameList);
            coreDAO.deleteFromBasket(packagingInfo);
        } catch (Exception e) {
            throw new MyRunTimeException("deleteFromBasket error " + e);
        }

    }

    @Override
    public List<PackagingInfo> getCoreInfoByAppId(Map<String, Object> param) {
        PageUtil.dealPage(param);
        return coreDAO.getBasketByAppId(param);
    }

    @Override
    @Transactional
    public void addCashInfo(String appId, List<PackagingInfo> paramList) {
        //看结算表有没有记录（如果有记录，要防止重复插入）
        PackagingInfo queryBean = new PackagingInfo();
        queryBean.setAppid(appId);
        List<PackagingInfo> resultList = coreDAO.getCashInfo(queryBean);
        //组装表没有记录
        if (resultList == null || !resultList.isEmpty()) {
            for (PackagingInfo element : paramList) {
                element.setStatus(Configs.PAGE_STATUS_EDIT.getIntCode());
                coreDAO.addCashInfo(element);
            }
        } else {
            //组装表有记录
            Set<String> existSet = new HashSet<>();
            for (PackagingInfo element : resultList) {
                if (element.getPageid() != null) {
                    existSet.add(element.getPageid());
                }
            }
            for (PackagingInfo element : paramList) {
                if (!existSet.contains(element.getPageid())) {
                    element.setStatus(Configs.PAGE_STATUS_EDIT.getIntCode());
                    coreDAO.addCashInfo(element);
                }
            }
        }


    }

    @Override
    @Transactional
    public ResultDo savePageSort(String pageInfo) {
        ResultDo resultDo = new ResultDo();
        resultDo.setSuccess(false);
        JSONObject jsonObject = JSONObject.parseObject(pageInfo);
        if (jsonObject.get(ResultStringKey.APP_ID) == null || jsonObject.getString(ResultStringKey.APP_ID).isEmpty()) {
            resultDo.setSuccess(false);
            resultDo.setMessage("appId is null");
            return resultDo;
        }
        String appId = jsonObject.getString(ResultStringKey.APP_ID);
        JSONArray functionList = jsonObject.getJSONArray("functionList");
        if (functionList == null || !functionList.isEmpty()) {
            resultDo.setSuccess(false);
            resultDo.setMessage("there is no functionList");
            return resultDo;
        }
        for (Object element : functionList) {
            String[] array = String.valueOf(element).split(",");
            PackagingInfo p = new PackagingInfo();
            p.setAppid(appId);
            p.setPageid(String.valueOf(array[0]));
            p.setSort(new Integer(array[1]));
            p.setShowtype(String.valueOf(array[2]));
            //resolution取消了数组长度判断，16-12-20
            p.setResolution(String.valueOf(array[3]));
            p.setPosition(String.valueOf(array[4]));
            p.setFatherid(String.valueOf(array[5]));
            coreDAO.savePageSort(p);
        }
        resultDo.setMessage(ResultStringKey.SUCCESS);
        resultDo.setSuccess(true);
        return resultDo;
    }

    @Override
    public List<UserRoleOrg> getOrgList() {
        return coreDAO.getOrgList();
    }

    private UserDto getUser(JSONObject jsonObject) {
        UserDto userDto = new UserDto();
        List<UserRoleOrg> userRoleOrgs = new ArrayList<>();
        if (null != jsonObject.getJSONArray("data")) {
            JSONArray orgJsonArray = jsonObject.getJSONArray("data");
            for (Object object : orgJsonArray) {
                UserRoleOrg userRoleOrg = new UserRoleOrg();
                JSONObject json = (JSONObject) object;
                userRoleOrg.setUserId(json.getString(ResultStringKey.USER_ID));
                userRoleOrg.setUserName(json.getString(ResultStringKey.USER_NAME));
                userRoleOrgs.add(userRoleOrg);
            }
        }
        userDto.setUserRoleOrgs(userRoleOrgs);
        return userDto;
    }

    @Override
    @Transactional
    public ResultDo addAppUser(String appId, JSONArray userArray) {
        UserRoleOrg param = new UserRoleOrg();
        List<String> userNameList = coreDAO.getUserNameByAppId(appId);
        param.setAppId(appId);
        coreDAO.deleteAppUser(param);
        AppBaseInfo appBaseInfo = appBaseInfoDao.getAppBaseInfoById(Integer.parseInt(appId));
        for (Object element : userArray) {
            UserRoleOrg user = new UserRoleOrg();
            user.setAppId(appId);
            String userName = "";
            if (((JSONObject) element).get(ResultStringKey.USER_ID) != null) {
                user.setUserId(((JSONObject) element).getString(ResultStringKey.USER_ID));
            }
            if (((JSONObject) element).get(SCREEN_NAME) != null) {
                userName = ((JSONObject) element).getString(SCREEN_NAME);
                user.setScreenName(userName);//English
            }
            if (((JSONObject) element).get(ResultStringKey.USER_NAME) != null) {
                try {
                    user.setUserName(URLDecoder.decode(((JSONObject) element).getString(ResultStringKey.USER_NAME),
                            "UTF-8"));//CHN
                } catch (UnsupportedEncodingException e) {
                    logger.error(String.valueOf(e));
                }
            }
            coreDAO.addAppUser(user);
            if (!userNameList.contains(((JSONObject) element).getString(SCREEN_NAME))) {
                super.saveLog(DictionaryEnums.ACTION_UPDATE.getDictionaryCode(), DictionaryEnums.BUSINESS_PAC
                                .getDictionaryCode(),
                        "将 : " + appBaseInfo.getAppName() + " 授权给 " + (userName));
            } else {
                userNameList.remove(((JSONObject) element).getString(SCREEN_NAME));
            }
        }
        for (String str : userNameList) {
            super.saveLog(DictionaryEnums.ACTION_UPDATE.getDictionaryCode(), DictionaryEnums.BUSINESS_PAC
                            .getDictionaryCode(),
                    "将 : " + str + " 从 " + appBaseInfo.getAppName() + "移除");
        }
        return new ResultDo(true, "ok");
    }

    @Override
    @Transactional
    public ResultDo addAppOrg(String appId, String orgId) {
        UserRoleOrg param = new UserRoleOrg();
        param.setOrgId(orgId);
        param.setAppId(appId);
        coreDAO.addAppOrg(param);
        return new ResultDo(true, "ok");
    }

    @Override
    public void delAppOrg(UserRoleOrg userRoleOrg) {
        coreDAO.delAppOrg(userRoleOrg);
    }

    /**
     * 读取配置，生成用户端的配置文件
     *
     * @param appId
     * @return
     * @throws IOException
     */
    @Override
    public ResultDo makeNewApp(String appId) throws IOException {
        AppBaseInfo appBaseInfo = appBaseInfoDao.getAppBaseInfoById(Integer.parseInt(appId));
        super.saveLog(DictionaryEnums.ACTION_UPDATE.getDictionaryCode(), DictionaryEnums.BUSINESS_PAC
                        .getDictionaryCode(),
                "发布组装应用 : " + appBaseInfo.getAppName());
        PackagingInfo queryBean = new PackagingInfo();
        queryBean.setAppid(appId);
        coreDAO.updateConfigByApp(queryBean);
        Map<String, Object> param = new HashMap<>();
        param.put("status", Configs.PAGE_STATUS_RUN.getIntCode());
        param.put(ResultStringKey.APP_ID, appId);
        appBaseInfoDao.updateStatus(param);
        return new ResultDo(true, ResultStringKey.SUCCESS);
    }

    @Override
    public JSONObject getPacList(Map<String, Object> param) {
        List<AppBaseInfo> appList = new ArrayList<>();
        JSONObject result = new JSONObject();
        JSONArray data = JSONArray.parseArray(JSONArray.toJSONString(appList));
        result.put("data", data);
        result.put(ResultStringKey.SUCCESS, true);
        result.put("message", ResultStringKey.SUCCESS);
        return result;
    }

    @Override
    public JSONObject getPacList(PackagingInfo param) {
        JSONObject result = new JSONObject();
        List<AppBaseInfo> appList = new ArrayList<>();
        JSONArray data = JSONArray.parseArray(JSONArray.toJSONString(appList));
        result.put("data", data);
        result.put("success", true);
        result.put("message", "success");
        return result;
    }

    @Override
    public int validatePacUser(PackagingInfo packagingInfo) {
        return coreDAO.validatePacUser(packagingInfo);
    }

    @Override
    public JSONArray getAppUsers(PackagingInfo packagingInfo) {
        List<UserRoleOrg> userL = coreDAO.getAppUser(packagingInfo);
        JSONArray jsonArray = new JSONArray();
        for (UserRoleOrg u : userL) {
            JSONObject j = new JSONObject();
            if (u.getUserId() != null) {
                j.put(ResultStringKey.USER_ID, u.getUserId());
            }
            if (u.getUserName() != null) {
                j.put(ResultStringKey.USER_NAME, u.getUserName());
            }
            if (u.getUserName() != null) {
                j.put(SCREEN_NAME, u.getScreenName());
            }
            jsonArray.add(j);
        }
        return jsonArray;
    }

    @Override
    public JSONArray getAppOrg(PackagingInfo packagingInfo) {
        List<UserRoleOrg> userL = coreDAO.getAppOrg(packagingInfo);
        JSONArray jsonArray = new JSONArray();
        for (UserRoleOrg u : userL) {
            JSONObject j = new JSONObject();
            j.put("appId", packagingInfo.getAppid());
            if (u.getUserId() != null) {
                j.put(ResultStringKey.USER_ID, u.getUserId());
            }
            if (u.getUserName() != null) {
                j.put(ResultStringKey.USER_NAME, u.getUserName());
            }
            if (u.getUserName() != null) {
                j.put(SCREEN_NAME, u.getScreenName());
            }
            if (u.getOrgId() != null) {
                j.put("orgId", u.getOrgId());
            }
            jsonArray.add(j);
        }
        return jsonArray;
    }

    @Override
    public Integer getAppStatus(PackagingInfo packagingInfo) {
        return coreDAO.getAppStatus(packagingInfo);
    }

    /**
     * @return boolean
     * @函数的作用 将微应用的组装次数入库
     * @函数的入参 compositionInfoMap
     * @函数的使用方法 compositionInfoMap必须包涵3个参数：appId(微应用id)，
     * appType(应用类型，可以是DEM,DCM,PWC,控件使用固定值WIDGET)，
     * num（本次组装总共使用到这个微应用的次数）
     * 例如
     * compositionInfoMap.put("appType","DCM");
     * compositionInfoMap.put("appId","27");
     * compositionInfoMap.put("num","3");然后调用这个函数就好了
     * @author Richard W
     * @create_date 2017-3-27
     */
    @Override
    @Transactional
    public boolean saveCompositionNum(Map<String, String> compositionInfoMap, Map<String, Integer> oldAppIdList,
                                      String userId) {
        //防止重复提交日志
        Integer numOld = oldAppIdList.get(compositionInfoMap.get("appId")) == null ? 0 : oldAppIdList.get
                (compositionInfoMap.get("appId"));
        Integer numNew = Integer.parseInt(compositionInfoMap.get("num"));
        if (numOld < numNew) {
            String appType = compositionInfoMap.get("appType");
            if ("widget".equals(appType.toLowerCase().trim())) {
                Map<String, Object> param = new HashMap<>();
                param.put("widgetId", Integer.parseInt(compositionInfoMap.get("appId")));
                param.put("fastDFSUrl", ConfigGetPropertyUtil.get("fastDFS.http.url"));
                AppWidgetInfo appWidgetInfo = widgetService.getAppWidgetById(param);
                super.saveLog(DictionaryEnums.ACTION_ADD.getDictionaryCode(), DictionaryEnums.BUSINESS_HOT_APP
                                .getDictionaryCode(),
                        "记录微热门应用 : " + appWidgetInfo.getWidgetName());
            } else {
                PackagingInfo packagingInfo = new PackagingInfo();
                packagingInfo.setPageid(compositionInfoMap.get("appId"));
                packagingInfo.setUserid(userId);
                List<AppBasketItemInfo> list = appBasketItemDao.queryBasketInfo(packagingInfo);
                for (AppBasketItemInfo appBasketItemInfo : list) {
                    String pageName = appBasketItemInfo.getPageName();
                    String oappName = appBasketItemInfo.getOappName();
                    super.saveLog(DictionaryEnums.ACTION_ADD.getDictionaryCode(), DictionaryEnums.BUSINESS_HOT_APP
                                    .getDictionaryCode(),
                            "记录热门微应用 : " + oappName + "  中的微应用：" + pageName);
                    break;
                }
            }
        }
        //看库里有没有记录了!!作用是：决定后面到底是update还是insert
        int ex = coreDAO.validateHotAppEx(compositionInfoMap);
        if (ex == 0) {
            //库里对这个微应用无记录，只能先插入一条
            coreDAO.saveCompositionNum(compositionInfoMap);
        } else {
            //一定只会>0, 那么就在原有记录上累加就好
            coreDAO.updateCompositionNum(compositionInfoMap);
        }
        return true;
    }

    @Override
    public List<String> getPageIdsByUserId(Integer userId) {
        return coreDAO.getPageIdsByUserId(userId);
    }

    @Override
    public Map<String, Object> getMarketData(JSONObject appInfoJson) {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> microAppList = new ArrayList<>();
        List<PageInfo> pagesList = new ArrayList<>();
        List<Map> microAppRelationList = new ArrayList<>();
        JSONArray data = appInfoJson.getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            JSONObject o = data.getJSONObject(i);
            microAppList.add(String.valueOf(o.getString("appId")));
            if (!"PAC".equals(o.getString("appId"))) {
                Map<String, String> microAppRelationMap = new HashMap<>();
                microAppRelationMap.put("appId", o.getString("appId"));
                microAppRelationMap.put("appName", o.getString("appName"));
                microAppRelationList.add(microAppRelationMap);
            }
            String url = o.getString("url");
            String imgUrl = o.getString("logoApp");
            JSONArray pages = o.getJSONArray("pages");
            if (pages != null && !pages.isEmpty()) {
                for (int y = 0; y < pages.size(); y++) {
                    JSONObject page = pages.getJSONObject(y);
                    PageInfo pageInfo = new PageInfo();
                    pageInfo.setId(page.getString("id"));
                    pageInfo.setPagename(page.getString("pageName"));
                    pageInfo.setPageurl(url + page.getString("pageUrl"));
                    pageInfo.setPagedesc(page.getString("pageDesc"));
                    pageInfo.setAppname(o.getString("appName"));
                    pageInfo.setAppid(o.getString("appId"));
                    pageInfo.setImgUrl(imgUrl);
                    pagesList.add(pageInfo);

                }
            }
        }
        resultMap.put("appName", microAppRelationList);
        resultMap.put("pagesList", pagesList);
        if (logger.isDebugEnabled()) {
            logger.debug("getMarketData microAppList == " + microAppList);
        }
        return resultMap;
    }

    /**
     * 筛选合适的市场数据
     *
     * @param pagesList
     * @param appId
     * @return
     */
    public List<PageInfo> screenMarketData(List<PageInfo> pagesList, String appId) {
        List<PageInfo> resultList = new ArrayList<>();
        for (PageInfo pageInfo : pagesList) {
            if (pageInfo != null && pageInfo.getAppid() != null
                    && appId.equals(pageInfo.getAppid())) {
                resultList.add(pageInfo);
            }
        }
        return resultList;
    }

    @Override
    public List<Map<String, Integer>> getComposeNum() {
        return coreDAO.getComposeNum();
    }

    /**
     * 将选中的页面功能加入到篮子中new
     *
     * @param param    入参
     * @param userId   用户iD
     * @param userName 用户名称
     */
    @Override
    @Transactional
    public int addToBasketNew(String userId, Map<String, String> param, String userName) {
        String basketId;
        if (userId != null && !userId.isEmpty()) {
            basketId = getBasketId(userId, userName);//判断此人是否有篮子？  有 则得到此篮子ID  无  则创建一个篮子 并返回此篮子ID
            param.put("id", basketId);
        }
        int c = coreDAO.beforeAddBasket(param);
        if (c == 0) {
            coreDAO.addToBasketNew(param);
            super.saveLog(DictionaryEnums.ACTION_ADD.getDictionaryCode(), DictionaryEnums.BUSINESS_BASKET
                            .getDictionaryCode(),
                    "增加功能 : " + param.get("pagename"));
        }
        return c;
    }

    //将组装次数拼装到结果集中
    @Override
    public List<PageInfo> transferComposeNum(List<PageInfo> pagesList) {
        //get compose num
        List<Map<String, Integer>> numList = getComposeNum();
        for (PageInfo page : pagesList) {
            if (page != null && page.getId() != null) {
                if (numList != null) {
                    for (Map<String, Integer> m : numList) {
                        if (m.get("mappid") != null &&
                                String.valueOf(m.get("mappid")).equals(page.getId())) {
                            page.setComposenum(new Integer(m.get("num")));
                        }
                    }
                    if (page.getComposenum() == null) {
                        page.setComposenum(0);
                    }
                }
            }
        }
        return pagesList;
    }

    public Integer getCountForBasket(Integer userId) {
        return coreDAO.getCountForBasketByUserId(userId);
    }

}
