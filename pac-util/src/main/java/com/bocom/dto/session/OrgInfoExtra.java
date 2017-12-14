package com.bocom.dto.session;

import com.bocom.domain.OrgInfo;

public class OrgInfoExtra extends OrgInfo {
	//是否为主部门
	private Boolean isMainDepartMent = false ; 
	
	private String adminCode;

    private String adminName;
	
	public Boolean getIsMainDepartMent() {
		return isMainDepartMent;
	}

	public void setIsMainDepartMent(Boolean isMainDepartMent) {
		this.isMainDepartMent = isMainDepartMent;
	}

	public String getAdminCode() {
		return adminCode;
	}

	public void setAdminCode(String adminCode) {
		this.adminCode = adminCode;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

}

