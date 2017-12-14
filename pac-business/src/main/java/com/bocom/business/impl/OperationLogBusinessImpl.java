package com.bocom.business.impl;

import com.bocom.business.OperationLogBusiness;
import com.bocom.domain.pac.AppOperationLogInfo;
import com.bocom.service.pac.BehaviorService;
import com.bocom.util.PageUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class OperationLogBusinessImpl implements OperationLogBusiness {

    @Resource
    private BehaviorService behaviorService;

    @Override
    public List<Map<String,Object>> queryLogByLimit(Map<String, Object> param) {
        PageUtil.dealPage(param);
        return behaviorService.queryLogByLimit(param);
    }
    @Override
    public List<Map<String,Object>> queryDictionary(Map<String, Object> param) {
        return behaviorService.queryDictionary(param);
    }
    @Override
    public List<Map<String,Object>> queryDictionaryMap(Map<String, Object> param) {
        return behaviorService.queryDictionaryMap(param);
    }
}
