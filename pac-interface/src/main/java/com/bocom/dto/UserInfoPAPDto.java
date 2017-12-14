package com.bocom.dto;

public class UserInfoPAPDto {
	private String appId;
	private String userName;
	private String version;
	private String source;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public UserInfoPAPDto(String appId, String version, String userName,
                          String source) {
		super();
		this.appId = appId;
		this.userName = userName;
		this.version = version;
		this.source = source;
	}
}
