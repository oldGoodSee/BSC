package com.bocom.service.pac;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/17.
 */

public interface FunctionService {

    List<Map<String,Object>> getFunctionList();

    void addTest() throws SQLException;
}
