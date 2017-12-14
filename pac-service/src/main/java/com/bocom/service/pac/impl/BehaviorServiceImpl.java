package com.bocom.service.pac.impl;

import com.bocom.dao.AppOperationLogDao;
import com.bocom.dao.BehaviorDao;
import com.bocom.dao.TDictionaryDao;
import com.bocom.domain.pac.AppOperationLogInfo;
import com.bocom.domain.pac.BehaviorInfo;
import com.bocom.service.pac.BehaviorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BehaviorServiceImpl implements BehaviorService {

    // log
    private static Logger logger = LoggerFactory
            .getLogger(BehaviorServiceImpl.class);

    @Resource
    private BehaviorDao behaviorDao;

    @Resource
    private AppOperationLogDao appOperationLogDao;

    @Resource
    private TDictionaryDao queryDictionary;

    /**
     * 记录关键行为日志
     *
     * @param behaviorInfo // FIXME: 2016/11/29
     */
    @Override
    public void addBehaviorLog(BehaviorInfo behaviorInfo) {
        behaviorDao.addBehaviorLog(behaviorInfo);
    }

    @Override
    public void addBehaviorLog(AppOperationLogInfo appOperationLogInfo) {
        appOperationLogDao.insert(appOperationLogInfo);
    }

    /**
     * 日志保存
     *
     * @param operationType    操作类型
     * @param operationContent 操作内容
     */
    @Override
    public void saveLog(String operationType, String businessType, String operationContent) {
        AppOperationLogInfo appOperationLogInfo = new AppOperationLogInfo();
        appOperationLogInfo.setOperationType(operationType);
        appOperationLogInfo.setBusinessType(businessType);
        appOperationLogInfo.setContent(operationContent);
        appOperationLogInfo.setCreateTime(new Date());
        addBehaviorLog(appOperationLogInfo);
    }

    @Override
    public List<Map<String, Object>> queryLogByLimit(Map<String, Object> param) {

        return appOperationLogDao.queryLogByLimit(param);
    }

    @Override
    public List<Map<String, Object>> queryDictionary(Map<String, Object> param) {
        return queryDictionary.queryDictionary(param);
    }

    @Override
    public List<Map<String, Object>> queryDictionaryMap(Map<String, Object> param) {
        return queryDictionary.queryDictionaryMap(param);
    }

    @Override
    public int addHistoryTable(Map<String, Object> param) {
        return appOperationLogDao.createHistoryTable(param);
    }

    @Override
    public int deleteHistoryRecord(Map<String, Object> param) {
        return appOperationLogDao.deleteHistoryRecord(param);
    }
}
