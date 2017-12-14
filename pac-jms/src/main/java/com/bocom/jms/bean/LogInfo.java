package com.bocom.jms.bean;

import java.util.Date;

public class LogInfo {
	/**
	 * 应用ID
	 */
	private String appId;
	/**
	 * 访问URL
	 */
	private String url;
	/**
	 * 源ID地址
	 */
	private String sourceIp;
	/**
	 * 目的ID地址
	 */
	private String purposeIp;
	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 业务编号
	 */
	private String businessCode;
	/**
	 * 访问开始时间
	 */
	private Date elapsedTime;
	/**
	 * 访问时长（毫秒）
	 */
	private String elapsedLong;
	/**
	 * 预留
	 */
	private String dataJson;
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	public String getPurposeIp() {
		return purposeIp;
	}
	public void setPurposeIp(String purposeIp) {
		this.purposeIp = purposeIp;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public Date getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(Date elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public String getElapsedLong() {
		return elapsedLong;
	}
	public void setElapsedLong(String elapsedLong) {
		this.elapsedLong = elapsedLong;
	}
	public String getDataJson() {
		return dataJson;
	}
	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}
	public LogInfo(String appId, String url, Integer userId, Date elapsedTime) {
		super();
		this.appId = appId;
		this.url = url;
		this.userId = userId;
		this.elapsedTime = elapsedTime;
	}
}
