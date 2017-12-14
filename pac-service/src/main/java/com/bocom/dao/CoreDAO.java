package com.bocom.dao;

import com.bocom.domain.UserRoleOrg;
import com.bocom.domain.pac.AppBaseInfo;
import com.bocom.domain.pac.PackagingInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/12/5.
 */
public interface CoreDAO {

    String isBasketExist(PackagingInfo packagingInfo);

    void addToBasket(PackagingInfo packagingInfo);

    int addNewBasket(PackagingInfo packagingInfo);

    List<PackagingInfo> getPageListByBasket(PackagingInfo packagingInfo);

    void deleteFromBasket(PackagingInfo packagingInfo);

    void deleteFromAppConfig(PackagingInfo packagingInfo);

    List<PackagingInfo> getBasketByAppId(Map<String, Object> param);

    List<PackagingInfo> getCashInfo(PackagingInfo packagingInfo);

    void addCashInfo(PackagingInfo packagingInfo);

    void savePageSort(PackagingInfo packagingInfo);

    List<UserRoleOrg> getUserList(UserRoleOrg userRoleOrg);

    List<UserRoleOrg> getOrgList();

    void deleteAppUser(UserRoleOrg userRoleOrg);

    void addAppUser(UserRoleOrg userRoleOrg);
    void addAppOrg(UserRoleOrg userRoleOrg);
    void delAppOrg(UserRoleOrg userRoleOrg);

    void updateConfigByApp(PackagingInfo packagingInfo);

    List<PackagingInfo> getConfigByApp(PackagingInfo packagingInfo);

    String getRoleIdByApp(PackagingInfo packagingInfo);

    List<AppBaseInfo> getPacList(PackagingInfo packagingInfo);

    List<PackagingInfo> getWinList(PackagingInfo packagingInfo);

    List<UserRoleOrg> getAppUser(PackagingInfo packagingInfo);

    List<UserRoleOrg> getAppOrg(PackagingInfo packagingInfo);

    Integer validateOrg(UserRoleOrg userRoleOrg);

    int validatePacUser(PackagingInfo packagingInfo);

    Integer getAppStatus(PackagingInfo packagingInfo);

    int validateHotAppEx(Map<String, String> compositionInfoMap);

    void saveCompositionNum(Map<String, String> compositionInfoMap);

    void updateCompositionNum(Map<String, String> compositionInfoMap);

    List<String> getPageIdsByUserId(Integer userId);

    List<String> getUserNameByAppId(String appId);

    List<Map<String, Integer>> getComposeNum();

    int beforeAddBasket(Map<String,String> param);

    void addToBasketNew(Map<String,String> param);

    Integer getCountForBasketByUserId(Integer userId);

}
