package com.bocom.dao;

import java.util.List;
import java.util.Map;

import com.bocom.domain.pac.ScreenHis;

public interface ScreenHisMapper {
	
	public List<ScreenHis> list(Map<String, Object> params);
}