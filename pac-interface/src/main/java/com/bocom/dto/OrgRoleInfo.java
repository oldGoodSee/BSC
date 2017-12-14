package com.bocom.dto;

public class OrgRoleInfo {
	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色编号
	 */
	private String roleCode;

	/**
	 * 组织编号
	 */
	private String roleOrgCode;

	/**
	 * 组织名称
	 */
	private String roleOrgName;

	/**
	 * 父组织名称
	 */
	private String parentOrgName;

	/**
	 * 父组织编号
	 */
	private String parentOrgCode;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleOrgCode() {
		return roleOrgCode;
	}

	public void setRoleOrgCode(String roleOrgCode) {
		this.roleOrgCode = roleOrgCode;
	}

	public String getRoleOrgName() {
		return roleOrgName;
	}

	public void setRoleOrgName(String roleOrgName) {
		this.roleOrgName = roleOrgName;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public String getParentOrgCode() {
		return parentOrgCode;
	}

	public void setParentOrgCode(String parentOrgCode) {
		this.parentOrgCode = parentOrgCode;
	}

}
