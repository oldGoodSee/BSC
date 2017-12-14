package com.bocom.enums;
/**
 * 审批状态枚举类
 */
public enum ApproveStatusEnum {
	
	APPROVE_NOT("0","未审批"),
	
	APPROVE_ALREADY("1","审批 ");

	/**审批状态编码*/
	private String approveCode;
	
	/**审批状态名称*/
	private String approveName;

	private ApproveStatusEnum(String approveCode, String approveName) {
		this.approveCode = approveCode;
		this.approveName = approveName;
	}

	public String getApproveCode() {
		return approveCode;
	}

	public void setApproveCode(String approveCode) {
		this.approveCode = approveCode;
	}

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}

	
}
