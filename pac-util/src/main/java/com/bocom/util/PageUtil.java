package com.bocom.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;

public class PageUtil {
	private static Integer defaultPageSize = 20;
	private static Integer defaultPageNum = 1;

	public static void setParams(HttpServletRequest request,
			Map<String, Object> params) {
		String pageSize = request.getParameter("pageSize");
		String pageNum = request.getParameter("pageNum");
		params.put("pageSize", StringUtils.isEmpty(pageSize) ? defaultPageSize
				: Integer.valueOf(pageSize));
		params.put("pageNum", StringUtils.isEmpty(pageNum) ? defaultPageNum
				: Integer.valueOf(pageNum));
	}

	public static Map covertMap(Object[] key, Object[] value) {
		HashMap returnMap = new HashMap();
		returnMap.put("success", false);
		if (null == key || null == value) {
			return returnMap;
		}
		if (key.length != value.length) {
			returnMap.put("message", "数据组装错误");
			return returnMap;
		}
		for (int i = 0, len = key.length; i < len; i++) {
			returnMap.put(key[i], value[i]);
		}
		returnMap.put("success", true);
		return returnMap;
	}

	public static void dealPage(Map<String, Object> param) {
		if (null != param) {
			Integer pageNum = (Integer) param.get("pageNum");
			Integer pageSize = (Integer) param.get("pageSize");
			if (null != pageNum && null != pageSize) {
				PageHelper.startPage(pageNum, pageSize);
			}
		}
	}
}
