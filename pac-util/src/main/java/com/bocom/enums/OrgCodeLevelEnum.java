package com.bocom.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 组织编码级别类
 */
public enum OrgCodeLevelEnum {
	
	UNKNOWN(-1,null,"未知"),
	
	FIRST(0,"","第一层"),
	
	SECOND(2,"0000000000","第二层"),
	
	THIRD(4,"00000000","第三层"),
	
	FOURTH(6,"000000","第四层"),
	
	FIFTH(8,"0000","第五层"),
	
	SIXTH(10,"00","第六层");

	/**组织编码起始地址，每层2个字符*/
	private int startNum;	
	
	/**组织尾编码*/
	private String endCode;

	/**层级名称*/
	private String floorName;
	
	private OrgCodeLevelEnum(int startNum, String endCode, String floorName) {
		this.startNum = startNum;
		this.endCode = endCode;
		this.floorName = floorName;
	}

	public int getStartNum() {
		return startNum;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getEndCode() {
		return endCode;
	}

	public void setEndCode(String endCode) {
		this.endCode = endCode;
	}
	
	/**判断是否相应等级的组织编码*/
	public static Boolean isOrgInfoRight(String orgCode, OrgCodeLevelEnum orgCodeLevelEnum) {
		if(StringUtils.isBlank(orgCode)){
			return false;
		}
		String code = orgCode.substring(orgCodeLevelEnum.getStartNum()); 
		if(code.equals(orgCodeLevelEnum.getEndCode()))
		{
			return true;
		}
		return false;
	}
}
