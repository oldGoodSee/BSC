package com.bocom.util;

public class ResponseVo {
	
	/**返回的数据对象*/
	private Object data;
	
	/**是否成功*/
	private Boolean success;
	
	/**提示信息*/
	private String message;

	public ResponseVo(Object obj) {
		if(obj instanceof Exception){
			this.data = obj;
			this.success = false;
			this.message = "error";
		}else{
			this.data = obj;
			this.success = true;
			this.message = "sucess";
		}
	}

	public ResponseVo() {
		this.data = null;
		this.success = true;
		this.message = "sucess";
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
