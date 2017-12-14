package com.bocom.service.pac;

import java.util.List;
import java.util.Map;

import com.bocom.domain.pac.ScreenHis;

public interface ScreenHisService {

	public List<ScreenHis> list(Map<String, Object> params);
}