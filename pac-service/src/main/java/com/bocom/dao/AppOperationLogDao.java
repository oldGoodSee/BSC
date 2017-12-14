package com.bocom.dao;

import com.bocom.domain.pac.AppOperationLogInfo;

import java.util.List;
import java.util.Map;

public interface AppOperationLogDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AppOperationLogInfo record);

    int insertSelective(AppOperationLogInfo record);

    AppOperationLogInfo selectByPrimaryKey(Integer id);

    List<Map<String, Object>> queryLogByLimit(Map<String, Object> param);

    int updateByPrimaryKeySelective(AppOperationLogInfo record);

    int updateByPrimaryKey(AppOperationLogInfo record);

    int createHistoryTable(Map<String, Object> param);

    int deleteHistoryRecord(Map<String, Object> param);
}