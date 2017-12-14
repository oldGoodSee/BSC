package com.bocom.dao;

import com.bocom.domain.pac.AppWidgetInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by bocom-qy on 2016/11/28.
 */
public interface AppWidgetDao {

    List<AppWidgetInfo> getAppWidgetList(Map<String, Object> param);

    List<AppWidgetInfo> getAppWidgetListByIdList(Map<String, Object> param);

    int selectCountByAppName(Map<String, Object> param);

    AppWidgetInfo getAppWidgetById(Map<String, Object> param);

    void updateStatus(Map<String, Object> param);

    void updateWidget(Map<String, Object> param);

    void deleteWidget(Integer widgetId);

    void addWidget(AppWidgetInfo appWidgetInfo);

    // init  info
    List<Map<String, Object>> getWidgetInitList(Map<String, Object> param);

    void updateWidgetInit(Map<String, Object> param);
}
