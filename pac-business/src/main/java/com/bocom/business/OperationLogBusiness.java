package com.bocom.business;

import java.util.List;
import java.util.Map;

public interface OperationLogBusiness {


    List<Map<String,Object>> queryLogByLimit(Map<String, Object> param);

    List<Map<String,Object>> queryDictionary(Map<String, Object> param);

    List<Map<String,Object>> queryDictionaryMap(Map<String, Object> param);
}
