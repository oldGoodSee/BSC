package com.bocom.ds2.service;

import java.util.List;
import java.util.Map;

import com.bocom.domain.pac.ScreenHis;

/**
 * ClassName:ThirdGenericDao
 * Function: 
 * Date:     2017年8月9日下午3:12:46
 * @author   chenzz
 * @since    JDK 1.7
 */
public interface ThirdGenericService {
	
	//任务列表
	public List<Map<String, Object>> listTask(Map<String, Object> params);
	
	public List<ScreenHis> listGk(Map<String, Object> params);

}
