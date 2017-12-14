package com.bocom.dao;

import com.bocom.domain.pac.TDictionaryInfo;

import java.util.List;
import java.util.Map;

public interface TDictionaryDao {
    int insert(TDictionaryInfo record);

    int insertSelective(TDictionaryInfo record);

    List<Map<String, Object>> queryDictionary(Map<String, Object> param);

    List<Map<String, Object>> queryDictionaryMap(Map<String, Object> param);
}