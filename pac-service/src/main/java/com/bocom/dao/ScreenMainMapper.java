package com.bocom.dao;

import java.util.List;
import java.util.Map;

import com.bocom.domain.pac.ScreenMain;

public interface ScreenMainMapper {

	public List<ScreenMain> list(Map<String, Object> params);
}