package com.bocom.dao;

import com.bocom.domain.pac.AppRelationInfo;

import java.util.List;


public interface AppRelationDao {

    int deleteByPrimaryKey(Integer id);

    int deleteByAppId(Integer id);

    int insert(AppRelationInfo record);

    int insertSelective(AppRelationInfo record);

    Integer selectCountByAppId(Integer appId);

    AppRelationInfo selectByPrimaryKey(Integer id);

    AppRelationInfo selectWidgetRelationByAppId(Integer appId);

    List<AppRelationInfo> selectRelationByAppId(Integer appId);

    List<AppRelationInfo> checkRelation(AppRelationInfo param);

    int updateByPrimaryKeySelective(AppRelationInfo record);

    int updateByPrimaryKey(AppRelationInfo record);
}
