package com.bocom.enums;

/**
 * 上传类型类 1.上报附件 2.流转附件 3.破案附件
 */
public enum UploadTypeEnum {

	REPROT(1, "上报"),

	WANDER(2, "流转"),

	CRACK(3, "破案");

	/** 管理员类型编码 */
	private Integer code;

	/** 管理员名称 */
	private String name;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private UploadTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	private UploadTypeEnum() {
	}

}
