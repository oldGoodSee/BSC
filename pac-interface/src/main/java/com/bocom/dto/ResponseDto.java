package com.bocom.dto;

public class ResponseDto {
	private boolean success;
	private String msg;
	private Integer errorCode;
	private Object data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ResponseDto(boolean success, String msg, Integer errorCode,
			Object data) {
		super();
		this.success = success;
		this.msg = msg;
		this.errorCode = errorCode;
		this.data = data;
	}

	public ResponseDto() {
		super();
	}

}
