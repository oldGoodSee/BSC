package com.bocom.service.pac.impl;

import com.bocom.dao.AppRelationDao;
import com.bocom.dao.AppWidgetDao;
import com.bocom.dao.AppWidgetInitDao;
import com.bocom.domain.pac.AppRelationInfo;
import com.bocom.domain.pac.AppWidgetInfo;
import com.bocom.domain.pac.AppWidgetInitInfo;
import com.bocom.service.pac.WidgetService;
import com.bocom.util.FastDfsUtil;
import com.bocom.util.ResponseVo;
import com.bocom.util.ResponseVoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class WidgetServiceImpl implements WidgetService {

    private static Logger logger = LoggerFactory
            .getLogger(WidgetServiceImpl.class);

    @Resource
    private AppWidgetDao appWidgetDao;

    @Resource
    private AppRelationDao appRelationDao;

    @Resource
    private AppWidgetInitDao appWidgetInitDao;

    @Override
    public List<AppWidgetInfo> getAppWidgetList(Map<String, Object> param) {
        return appWidgetDao.getAppWidgetList(param);
    }

    @Override
    public List<AppWidgetInfo> getAppWidgetListByIdList(Map<String, Object> param) {
        return appWidgetDao.getAppWidgetListByIdList(param);
    }

    @Override
    public int getCountByAppName(Map<String, Object> param) {
        return appWidgetDao.selectCountByAppName(param);
    }

    @Override
    public void updateStatus(Map<String, Object> param) {
        appWidgetDao.updateStatus(param);
    }

    @Override
    public void updateWidget(Map<String, Object> param) {
        appWidgetDao.updateWidget(param);
    }

    @Override
    public void deleteWidget(Integer widgetId) {
        appWidgetDao.deleteWidget(widgetId);
    }

    @Override
    public void addWidget(AppWidgetInfo appWidgetInfo, HttpServletRequest request) throws IOException {
        appWidgetDao.addWidget(appWidgetInfo);

        String fileName = request.getSession().getServletContext().getRealPath("/")
                + "widgetFrame\\app\\widget_"
                + appWidgetInfo.getWidgetId()
                + ".html";

        try (OutputStreamWriter fw = new OutputStreamWriter(
                new FileOutputStream(fileName), "UTF-8")) {
            String doc = request.getParameter("doc");
            doc = "<!DOCTYPE html>\n<html>\n" + doc + "\n</html>";
            fw.write(doc);
            fw.close();
            FastDfsUtil fast = new FastDfsUtil();
            String str = fast.uploadFile(fileName);
            Map<String, Object> param = new HashMap<>();
            param.put("widgetId", appWidgetInfo.getWidgetId());
            param.put("fileName", str);
            appWidgetDao.updateWidget(param);
            if (logger.isDebugEnabled()) {
                logger.debug("appWidgetInfo is = " + appWidgetInfo.toString());
                logger.debug("updateWidget is = " + param);
            }
        } catch (Exception e) {
            logger.error("Service addWidget error " + e);
        }
    }

    @Override
    public void addWidget(AppWidgetInfo appWidgetInfo) {
        appWidgetDao.addWidget(appWidgetInfo);

    }

    @Override
    public AppWidgetInfo getAppWidgetById(Map<String, Object> param) {
        return appWidgetDao.getAppWidgetById(param);
    }

    @Override
    public List<Map<String, Object>> getWidgetInitList(Map<String, Object> param) {
        return appWidgetDao.getWidgetInitList(param);
    }

    @Override
    public void updateWidgetInit(Map<String, Object> param) {
        appWidgetDao.updateWidgetInit(param);
    }

    @Override
    public List<AppRelationInfo> checkRelation(AppRelationInfo appRelationInfo) {
        return appRelationDao.checkRelation(appRelationInfo);
    }

    @Override
    public int insertRelationInfo(AppRelationInfo record) {

        return appRelationDao.insertSelective(record);
    }

    @Override
    public Integer selectCountByAppId(Integer appId) {
        return appRelationDao.selectCountByAppId(appId);
    }

    @Override
    public AppRelationInfo selectWidgetRelationByAppId(Integer appId) {
        return appRelationDao.selectWidgetRelationByAppId(appId);
    }

    @Override
    public List<AppRelationInfo> selectRelationByAppId(Integer appId) {
        return appRelationDao.selectRelationByAppId(appId);
    }

    @Override
    public void deleteByAppId(Integer appId) {
        appRelationDao.deleteByAppId(appId);
    }

    @Override
    public int insertWidgetInitInfo(AppWidgetInitInfo record) {
        return appWidgetInitDao.insertWidgetInit(record);
    }

    @Override
    public AppWidgetInitInfo selectWidgetInitInfoById(Integer widgetInitId) {
        return appWidgetInitDao.selectById(widgetInitId);
    }

    @Override
    public int updateWidgetInitInfoById(AppWidgetInitInfo widgetInitInfo) {
        return appWidgetInitDao.updateByPrimaryKeySelective(widgetInitInfo);
    }

    @Override
    public void deleteWidgetInitInfoById(Integer widgetInitId) {
        appWidgetInitDao.deleteByPrimaryKey(widgetInitId);
    }

    //这下面是service里面的方法
    public ResponseVo uploadPhoto(MultipartFile file) throws Exception {
        byte[] logoWebByte = file.getBytes();
        String logoWebFilePath = new FastDfsUtil().uploadFile(logoWebByte,
                file.getOriginalFilename());
        return ResponseVoUtil.success(logoWebFilePath);
    }
}
