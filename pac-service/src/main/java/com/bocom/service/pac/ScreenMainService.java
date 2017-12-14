package com.bocom.service.pac;

import java.util.List;
import java.util.Map;

import com.bocom.domain.pac.ScreenMain;

public interface ScreenMainService {

	public List<ScreenMain> list(Map<String, Object> params);
}