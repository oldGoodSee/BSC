package com.bocom.dto;

public class SessionUserRespDto {
	private boolean success;
	private String message;
	private SessionUserInfoDto data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public SessionUserRespDto(boolean success, String message,
			SessionUserInfoDto data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SessionUserInfoDto getData() {
		return data;
	}

	public void setData(SessionUserInfoDto data) {
		this.data = data;
	}

	public SessionUserRespDto() {
		super();
	}
}
