package com.bocom.enums;
/**
 * 管理员类型类
 * 管理员编码  1.系统超级管理员（全部）2.组织超级管理员（操作当前组织、下一级组织 以及相关管理员信息） 3.组织管理员（操作当前组织人员信息）
 */
public enum AdminTypeEnum {
	
	UNKNOWN_ADMIN("0","无"),
	
	ALL_SUPER_ADMIN("1","系统超级管理员"),
	
	ORG_SUPER_ADMIN("2","组织超级管理员"),
	
	ORG_ADMIN("3","组织管理员");
	
	
	/**管理员类型编码*/
	private String adminCode;

	/**管理员名称*/
	private String adminName;

	private AdminTypeEnum(String adminCode, String adminName) {
		this.adminCode = adminCode;
		this.adminName = adminName;
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
	

	/**
	 * 根据管理员编号获取管理员类型枚举对象
	 */
	public static AdminTypeEnum getAdminTypeByAdminCode(String adminCode) {
		for(AdminTypeEnum adminTypeEnum :AdminTypeEnum.values()){
			if(adminTypeEnum.getAdminCode().equals(adminCode)){
				return adminTypeEnum;
			}
		}
		return AdminTypeEnum.UNKNOWN_ADMIN;
	}
	

	/**
	 * 根据管理员编号获取管理员类型枚举对象
	 */
	public static String getAdminNameByAdminCode(String adminCode) {
		for(AdminTypeEnum adminTypeEnum :AdminTypeEnum.values()){
			if(adminTypeEnum.getAdminCode().equals(adminCode)){
				return adminTypeEnum.getAdminName();
			}
		}
		return AdminTypeEnum.UNKNOWN_ADMIN.getAdminName();
	}
	
	
	
}
