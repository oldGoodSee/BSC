package com.bocom.service.pac;

import java.util.List;
import java.util.Map;

import com.bocom.domain.pac.ScreenCategory;

public interface ScreenCategoryService {
	public List<ScreenCategory> list(Map<String, Object> params);

}