package com.bocom.dao;

import java.util.List;
import java.util.Map;

/**
 * 控件例子所用到的   接口服务 Dao
 */
public interface WidgetServiceDao {

    List<Map<String, Object>> selectPersonInfoByNameIdCard(Map<String, Object> param);

    List<Map<String, Object>> selectPersonInfoByNameJobAddress(Map<String, Object> param);

    List<Map<String, Object>> selectPersonInfoByNameBirth(Map<String, Object> param);

    List<Map<String, Object>> selectStatistics();

    List<Map<String, Object>> testSelect(Map<String, Object> param);

    List<Map<String, Object>> interfaceList(Map<String, Object> param);

    List<Map<String, Object>> interfaceListByWidget(Map<String, Object> param);

    List<Map<String, Object>> getCarInfoByIdCardCar(Map<String, Object> param);
}
