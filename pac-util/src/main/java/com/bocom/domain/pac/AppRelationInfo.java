package com.bocom.domain.pac;

import java.util.Date;

public class AppRelationInfo {

    private Integer id;

    private Integer appId;// 存放的应用ID

    private String type;// type : widget/app

    private String thirdAppId;// thirdAppId : DEM,DCM,AADM,PAC,PAP.......

    private String pageIds;//数据库字段  存放功能页面的ID

    private String widgetIds;//数据库字段  存放控件的ID

    private String pageId;//检查是否有被引用的数据   存放待检查的功能页面ID

    private String widgetId;//检查是否有被引用的数据   存放待检查的控件ID

    private Date createTime;

    private String userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThirdAppId() {
        return thirdAppId;
    }

    public void setThirdAppId(String thirdAppId) {
        this.thirdAppId = thirdAppId;
    }

    public String getPageIds() {
        return pageIds;
    }

    public void setPageIds(String pageIds) {
        this.pageIds = pageIds;
    }

    public String getWidgetIds() {
        return widgetIds;
    }

    public void setWidgetIds(String widgetIds) {
        this.widgetIds = widgetIds;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AppRelationInfo{" +
                "id=" + id +
                ", appId=" + appId +
                ", type='" + type + '\'' +
                ", thirdAppId='" + thirdAppId + '\'' +
                ", pageIds='" + pageIds + '\'' +
                ", widgetIds='" + widgetIds + '\'' +
                ", pageId='" + pageId + '\'' +
                ", widgetId='" + widgetId + '\'' +
                ", createTime=" + createTime +
                ", userId='" + userId + '\'' +
                '}';
    }
}