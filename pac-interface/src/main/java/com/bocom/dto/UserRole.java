package com.bocom.dto;

import java.util.List;

public class UserRole {
	private Long userId;
	private List<String> haveRoleCodeList;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<String> getHaveRoleCodeList() {
		return haveRoleCodeList;
	}

	public void setHaveRoleCodeList(List<String> haveRoleCodeList) {
		this.haveRoleCodeList = haveRoleCodeList;
	}

}
