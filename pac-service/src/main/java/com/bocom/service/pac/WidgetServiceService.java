package com.bocom.service.pac;

import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Map;


public interface WidgetServiceService {

    List<Map<String, Object>> selectPersonInfoByNameIdCard(Map<String, Object> param);

    List<Map<String, Object>> selectPersonInfoByNameJobAddress(Map<String, Object> param);

    List<Map<String, Object>> selectPersonInfoByNameBirth(Map<String, Object> param);

    JSONArray selectStatistics();

    List<Map<String, Object>> test(Map<String, Object> param);

    List<Map<String, Object>> interfaceList(Map<String, Object> param);
    
    List<Map<String, Object>> interfaceListByWidget(Map<String, Object> param);

    List<Map<String, Object>> getCarInfoByIdCardCar(Map<String, Object> param);
}
