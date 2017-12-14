package com.bocom.domain.pac;

import java.util.Date;

public class AppWidgetInitInfo {
    private Integer id;

    private String widgetName;

    private String folderName;

    private String htmlName;

    private String path;

    private Integer status;

    private String createUserName;

    private Date createTime;

    private String createIp;

    private String version;

    private Integer wrmId;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getHtmlName() {
        return htmlName;
    }

    public void setHtmlName(String htmlName) {
        this.htmlName = htmlName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateUserID() {
        return createUserName;
    }

    public void setCreateUserID(String createUserID) {
        this.createUserName = createUserID;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getWrmId() {
        return wrmId;
    }

    public void setWrmId(Integer wrmId) {
        this.wrmId = wrmId;
    }

    @Override
    public String toString() {
        return "AppWidgetInitInfo{" +
                "id=" + id +
                ", widgetName='" + widgetName + '\'' +
                ", folderName='" + folderName + '\'' +
                ", htmlName='" + htmlName + '\'' +
                ", path='" + path + '\'' +
                ", status=" + status +
                ", createUserName='" + createUserName + '\'' +
                ", createTime=" + createTime +
                ", createIp='" + createIp + '\'' +
                ", version='" + version + '\'' +
                ", wrmId=" + wrmId +
                ", type='" + type + '\'' +
                '}';
    }
}