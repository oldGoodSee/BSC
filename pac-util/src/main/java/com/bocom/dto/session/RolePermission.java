package com.bocom.dto.session;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

public class RolePermission {
	private String roleCode;
	private String permission;
	private List<String> plist;

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public List<String> getPlist() {
		return plist;
	}

	public void setPlist(List<String> plist) {
		this.plist = plist;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
		if (StringUtils.isNotEmpty(permission)) {
			String[] split = permission.split(",");
			this.plist = Arrays.asList(split);
		}
	}

}
