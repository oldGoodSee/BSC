package com.bocom.ds2.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bocom.dao.ThirdGenericDao;
import com.bocom.domain.pac.ScreenHis;
import com.bocom.ds2.service.ThirdGenericService;
import com.bocom.service.pac.ScreenHisService;
import com.bocom.util.JsonUtil;

/**
 * ClassName:ThirdGenericServiceImpl
 * Function: 
 * Date:     2017年8月10日下午2:51:09
 * @author   chenzz
 * @since    JDK 1.7
 */
@Service
@SuppressWarnings("all")
public class ThirdGenericServiceImpl implements ThirdGenericService {
	@Autowired
	private ThirdGenericDao thirdGenericDao;
	@Autowired
	private ScreenHisService screenHisService;

	@Override
	public List<Map<String, Object>> listTask(Map<String, Object> params) {
		return thirdGenericDao.listTask(params);
	}

	@Override
	public List<ScreenHis> listGk(Map<String, Object> params) {
		List<Map<String, Object>> ckList = thirdGenericDao.listCheckPointData(params);
		List<Map<String, Object>> scList = thirdGenericDao.listSocietyData(params);
		params.remove("beginTime");
		params.remove("endTime");
		List<ScreenHis> dataList = screenHisService.list(params);
		ArrayList<ScreenHis> resultList = new ArrayList<ScreenHis>();
		ScreenHis temp =null;
		if (dataList != null && dataList.size() > 0) {
			for (ScreenHis data : dataList) {
				String name = data.getName();
				if("title".equals(name)) {
					resultList.add(data);
					continue;
				}
				String content = data.getContent();
				for (Map his : ckList) {
					BigDecimal cnt = (BigDecimal) his.get(content.trim().toUpperCase());
					if (null != cnt) {
						temp=new ScreenHis();
						BeanUtils.copyProperties(data, temp);
						temp.setContent(cnt.toString());
						temp.setDealtime((Date)his.get("TIME"));
						resultList.add(temp);
						temp=null;
					}
				}
				for (Map his : scList) {
					BigDecimal cnt = (BigDecimal) his.get(content.trim().toUpperCase());
					if (null != cnt) {
						temp=new ScreenHis();
						BeanUtils.copyProperties(data, temp);
						temp.setContent(cnt.toString());
						temp.setDealtime((Date)his.get("TIME"));
						resultList.add(temp);
						temp=null;
					}
				}
			}

		}
		return resultList;
	}

}
