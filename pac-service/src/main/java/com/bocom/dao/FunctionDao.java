package com.bocom.dao;

import com.bocom.domain.pac.FunctionInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/17.
 */
public interface FunctionDao {

    List<Map<String,Object>> getFunctionList();

    void insertTest(FunctionInfo functionInfo);

    void addException();

}
