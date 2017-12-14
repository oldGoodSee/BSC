package com.bocom.service.pac;

import com.bocom.domain.pac.AppOperationLogInfo;
import com.bocom.domain.pac.BehaviorInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/24.
 */
public interface BehaviorService {

    //record important action
    void addBehaviorLog(BehaviorInfo behaviorInfo);

    void addBehaviorLog(AppOperationLogInfo appOperationLogInfo);

    List<Map<String, Object>> queryLogByLimit(Map<String, Object> param);

    void saveLog(String operationType, String businessType, String operationContent);

    List<Map<String, Object>> queryDictionary(Map<String, Object> param);

    List<Map<String, Object>> queryDictionaryMap(Map<String, Object> param);

    int addHistoryTable(Map<String, Object> param);

    int deleteHistoryRecord(Map<String, Object> param);
}
