package com.bocom.service.user;

import com.bocom.dto.UserInfoPAPDto;
import com.bocom.dto.session.SessionUserInfo;

public interface UserRestService {
	/**
	 * 根据登录名获取用户信息
	 * 
	 * @param loginName
	 *            登录名
	 * @return
	 */
	SessionUserInfo getUserInfoByLoginName(String loginName);

	/**
	 * 从pap获取用户信息
	 * @return
	 */
	public SessionUserInfo getUserInfoFromPAP(UserInfoPAPDto dto);

}
