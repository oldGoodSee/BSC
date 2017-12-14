package com.bocom.dto;

import java.util.List;

public class UserRoleDto {
	private boolean success;
	private String message;
	private Integer errorCode;
	private List<UserRole> data;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public List<UserRole> getData() {
		return data;
	}
	public void setData(List<UserRole> data) {
		this.data = data;
	}
	
	
}
