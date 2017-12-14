package com.bocom.service.pac;

import com.bocom.domain.pac.AppRelationInfo;
import com.bocom.domain.pac.AppWidgetInfo;
import com.bocom.domain.pac.AppWidgetInitInfo;
import com.bocom.util.ResponseVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by bocom-qy on 2017-1-16.
 */
public interface WidgetService {

    List<AppWidgetInfo> getAppWidgetList(Map<String, Object> param);

    List<AppWidgetInfo> getAppWidgetListByIdList(Map<String, Object> param);

    AppWidgetInfo getAppWidgetById(Map<String, Object> param);

    void updateStatus(Map<String, Object> param);

    void updateWidget(Map<String, Object> param);

    void deleteWidget(Integer widgetId);

    void addWidget(AppWidgetInfo appWidgetInfo, HttpServletRequest request) throws IOException;

    void addWidget(AppWidgetInfo appWidgetInfo);

    List<Map<String, Object>> getWidgetInitList(Map<String, Object> param);

    void updateWidgetInit(Map<String, Object> param);

    List<AppRelationInfo> checkRelation(AppRelationInfo appRelationInfo);

    int insertRelationInfo(AppRelationInfo record);

    Integer selectCountByAppId(Integer appId);

    void deleteByAppId(Integer appId);

    int insertWidgetInitInfo(AppWidgetInitInfo record);

    AppWidgetInitInfo selectWidgetInitInfoById(Integer widgetInitId);

    List<AppRelationInfo> selectRelationByAppId(Integer appId);

    int updateWidgetInitInfoById(AppWidgetInitInfo widgetInitInfo);

    void deleteWidgetInitInfoById(Integer widgetInitId);

    AppRelationInfo selectWidgetRelationByAppId(Integer appId);

    int getCountByAppName(Map<String, Object> param);

    ResponseVo uploadPhoto(MultipartFile file) throws Exception;

}
