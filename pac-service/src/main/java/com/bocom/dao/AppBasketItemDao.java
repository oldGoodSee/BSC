package com.bocom.dao;

import com.bocom.domain.pac.AppBasketItemInfo;
import com.bocom.domain.pac.PackagingInfo;

import java.util.List;

public interface AppBasketItemDao {
    int deleteByPrimaryKey(Integer uuid);

    int insert(AppBasketItemInfo record);

    int insertSelective(AppBasketItemInfo record);

    AppBasketItemInfo selectByPrimaryKey(Integer uuid);

    int updateByPrimaryKeySelective(AppBasketItemInfo record);

    int updateByPrimaryKey(AppBasketItemInfo record);

    List<AppBasketItemInfo> queryBasketInfo(PackagingInfo param);
}