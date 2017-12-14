

package com.bocom.business;

import com.alibaba.fastjson.JSONObject;
import com.bocom.domain.pac.AppWidgetInfo;
import com.bocom.domain.pac.AppWidgetInitInfo;
import com.bocom.util.ResponseVo;

import java.util.List;
import java.util.Map;


public interface WidgetBusiness {

    JSONObject saveWidget(String path, String doc, AppWidgetInfo appWidgetInfo, String sql);

    List<AppWidgetInfo> getAppWidgetList(Map<String, Object> param, String userName);

    int getCountByAppName(Map<String, Object> param);

    void deleteWidget(String widgetId, String path, String resource);

    JSONObject checkRelation(String mappId, String type);

    void updateStatus(Map<String, Object> param,JSONObject jsonObject);

    JSONObject updateWidget(String path, String widgetName, String doc, String widgetId, String userName);

    JSONObject getWidgetInitList(JSONObject paramJson);

    JSONObject unzipWidgetFile(String widgetId, String path);

    int insertWidgetInit(AppWidgetInitInfo appWidgetInitInfo);

    JSONObject initWidget(Integer widgetInitId, String path, String userName, String version, String widgetName,
                          String dfsUrl);

    void deleteInitWidget(Integer widgetInitId);

    void createMissWidgetFile(String appId, String userName, String path);

    String queryDMMPDR(String areaType, String serverId, int pageSize, int pageNum);

    JSONObject queryDB(String dbId, String sql,
                       int targetPage, Map<String, String> nbMap);

    public ResponseVo queryCategory(String categoryType);


}
