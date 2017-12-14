package com.bocom.service.pac.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bocom.dao.WidgetServiceDao;
import com.bocom.service.pac.WidgetServiceService;
import com.bocom.util.ConfigGetPropertyUtil;
import com.bocom.util.PageUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WidgetServiceServiceImpl implements WidgetServiceService {

    @Resource
    private WidgetServiceDao widgetServiceDao;

    private static Map<String, Object> statisticsMap = new HashMap<>();

    static {
        if (statisticsMap.isEmpty()) {
            statisticsMap.put("rob", "抢劫");
            statisticsMap.put("theft", "市场营销盗窃");
            statisticsMap.put("dispute", "纠纷");
            statisticsMap.put("traffic_accident", "客户支持交通事故");
            statisticsMap.put("drug", "贩毒");
            statisticsMap.put("wounding", "伤人");
        }
    }

    @Override
    public List<Map<String, Object>> selectPersonInfoByNameIdCard(Map<String, Object> param) {
        PageUtil.dealPage(param);
        return widgetServiceDao.selectPersonInfoByNameIdCard(param);
    }

    @Override
    public List<Map<String, Object>> selectPersonInfoByNameJobAddress(Map<String, Object> param) {
        PageUtil.dealPage(param);
        return widgetServiceDao.selectPersonInfoByNameJobAddress(param);
    }

    @Override
    public List<Map<String, Object>> selectPersonInfoByNameBirth(Map<String, Object> param) {
        PageUtil.dealPage(param);
        return widgetServiceDao.selectPersonInfoByNameBirth(param);
    }

    @Override
    public JSONArray selectStatistics() {
        List<Map<String, Object>> map = widgetServiceDao.selectStatistics();
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> forMap : map) {
            JSONObject forJson = new JSONObject();
            forJson.put("cityName", forMap.get("city"));
            forJson.put("cityNum", forMap.get("city_num"));
            List<JSONObject> list = new ArrayList<>();
            dealStatisticsData("rob", forMap, list);
            dealStatisticsData("theft", forMap, list);
            dealStatisticsData("dispute", forMap, list);
            dealStatisticsData("traffic_accident", forMap, list);
            dealStatisticsData("drug", forMap, list);
            dealStatisticsData("wounding", forMap, list);
            forJson.put("data", list);
            jsonArray.add(forJson);
        }
        return jsonArray;
    }

    private void dealStatisticsData(String code, Map<String, Object> map, List<JSONObject> list) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("name", statisticsMap.get(code));
        jsonObject.put("data", Integer.parseInt(map.get(code).toString()));
        list.add(jsonObject);
    }

    @Override
    public List<Map<String, Object>> test(Map<String, Object> param) {
        PageUtil.dealPage(param);
        return widgetServiceDao.testSelect(param);
    }

    @Override
    public List<Map<String, Object>> interfaceList(Map<String, Object> param) {
        return widgetServiceDao.interfaceList(param);
    }

    @Override
    public List<Map<String, Object>> interfaceListByWidget(Map<String, Object> param) {
        return widgetServiceDao.interfaceListByWidget(param);
    }

    @Override
    public List<Map<String, Object>> getCarInfoByIdCardCar(Map<String, Object> param) {
        PageUtil.dealPage(param);
        return widgetServiceDao.getCarInfoByIdCardCar(param);
    }

}
