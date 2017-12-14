package com.bocom.dao;

import java.util.List;
import java.util.Map;

/**
 * ClassName:ThirdGenericDao
 * Function: 
 * Date:     2017年8月9日下午3:12:46
 * @author   chenzz
 * @since    JDK 1.7
 */
public interface ThirdGenericDao {
	
	//任务列表
	public List<Map<String, Object>> listTask(Map<String, Object> params);

	//获取检查点数据
	public List<Map<String, Object>> listCheckPointData(Map<String, Object> params);

	//获取社会面数据
	public List<Map<String, Object>> listSocietyData(Map<String, Object> params);
}
