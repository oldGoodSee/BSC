package com.bocom.dto;

import java.util.List;


public class SessionUserInfoDto {
	
	/**
	 * 用户ID
	 */
	private Integer userId;
	
	/**
	 * 登录名
	 */
    private String userName;
    
    /**
     * 警察名称
     */
    private String policeName;
    
    /**
     * 警察编号
     */
    private String policeCode;
    
    /**
     * 用户-角色-组织信息列表（某一个组织内用户的角色信息）
     */
    private List<OrgRoleInfo> OrgRoleUserInfoList;
    
    /**
     * 角色-组织信息列表（某一个组织的角色信息）
     */
    private List<OrgRoleInfo> OrgRoleInfoList;
    
	public List<OrgRoleInfo> getOrgRoleInfoList() {
		return OrgRoleInfoList;
	}

	public void setOrgRoleInfoList(List<OrgRoleInfo> orgRoleInfoList) {
		OrgRoleInfoList = orgRoleInfoList;
	}

	public List<OrgRoleInfo> getOrgRoleUserInfoList() {
		return OrgRoleUserInfoList;
	}

	public void setOrgRoleUserInfoList(List<OrgRoleInfo> orgRoleUserInfoList) {
		OrgRoleUserInfoList = orgRoleUserInfoList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPoliceName() {
		return policeName;
	}

	public void setPoliceName(String policeName) {
		this.policeName = policeName;
	}

	public String getPoliceCode() {
		return policeCode;
	}

	public void setPoliceCode(String policeCode) {
		this.policeCode = policeCode;
	}

}
