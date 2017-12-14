package com.bocom.dao;

import com.bocom.domain.pac.AppWidgetInitInfo;

public interface AppWidgetInitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AppWidgetInitInfo record);

    int insertWidgetInit(AppWidgetInitInfo record);

    AppWidgetInitInfo selectById(Integer id);

    int updateByPrimaryKeySelective(AppWidgetInitInfo record);

    int updateByPrimaryKey(AppWidgetInitInfo record);
}