package com.bocom.service.pac.impl;

import com.bocom.dao.FunctionDao;
import com.bocom.service.pac.FunctionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/17.
 */
@Service
public class FunctionServiceImpl implements FunctionService {

    @Resource
    private FunctionDao functionDao;

    @Override
    public List<Map<String,Object>> getFunctionList() {
        return functionDao.getFunctionList();
    }

    @Override
    @Transactional
    public void addTest() throws SQLException {
        functionDao.addException();
    }
}
