package com.bocom.service.pac.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bocom.dao.ScreenMainMapper;
import com.bocom.domain.pac.ScreenMain;
import com.bocom.service.pac.ScreenMainService;
import com.github.pagehelper.PageHelper;

/**
 * ClassName:ScreenMainServiceImpl
 * Function: 
 * Date:     2017年8月2日上午9:59:13
 * @author   chenzz
 * @since    JDK 1.7
 */
@Service
public class ScreenMainServiceImpl implements ScreenMainService {

	@Autowired
	private ScreenMainMapper screenMainMapper;

	@Override
	public List<ScreenMain> list(Map<String, Object> params) {
		if (null != params) {
			// 判断是否存在分页
			String pageNum = (String) params.get("pageNum");
			String pageSize = (String) params.get("pageSize");
			if (StringUtils.isNotBlank(pageNum) && StringUtils.isNotBlank(pageSize)) {
				// 分页查询
				PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
			}
		}
		return screenMainMapper.list(params);
	}

}
