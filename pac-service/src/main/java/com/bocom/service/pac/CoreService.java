package com.bocom.service.pac;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.domain.UserRoleOrg;
import com.bocom.domain.pac.PackagingInfo;
import com.bocom.domain.pac.PageInfo;
import com.bocom.domain.pac.ResultDo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
public interface CoreService {

    void addToBasket(String userId, List<PackagingInfo> paramList, String userName);

    List<PackagingInfo> getCoreInfoByAppId(Map<String, Object> param);

    void deleteFromBasket(PackagingInfo packagingInfo);

    void addCashInfo(String appId, List<PackagingInfo> paramList);

    ResultDo savePageSort(String pageInfo);

    List<UserRoleOrg> getOrgList();

    ResultDo addAppUser(String appId, JSONArray userArray);
    ResultDo addAppOrg(String appId, String orgId);
    void delAppOrg(UserRoleOrg userRoleOrg);

    ResultDo makeNewApp(String appId) throws IOException;

    JSONObject getPacList(Map<String, Object> param);

    JSONObject getPacList(PackagingInfo param);

    int validatePacUser(PackagingInfo packagingInfo);

    JSONArray getAppUsers(PackagingInfo packagingInfo);
    JSONArray getAppOrg(PackagingInfo packagingInfo);

    Integer getAppStatus(PackagingInfo packagingInfo);

    boolean saveCompositionNum(Map<String, String> compositionInfoMap,Map<String, Integer> oldAppId,String userId);

    List<String> getPageIdsByUserId(Integer userId);

    Map<String, Object> getMarketData(JSONObject appInfoJson);//获得arcm数据之后，进行转换，细化为应用和功能页面（微应用）

    public List<PageInfo> screenMarketData(List<PageInfo> pagesList, String appId);

    List<Map<String, Integer>> getComposeNum();

    int addToBasketNew(String userId, Map<String, String> param, String userName);

    List<PageInfo> transferComposeNum(List<PageInfo> pagesList);

    Integer getCountForBasket(Integer userId);

}
