package com.bocom.dao;

import java.util.List;
import java.util.Map;

import com.bocom.domain.pac.ScreenCategory;

public interface ScreenCategoryMapper {
	public List<ScreenCategory> list(Map<String, Object> params);

}