package com.bocom.domain.pac;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bocom-qy on 2016/11/28.
 */
public class AppBaseInfo {

    private int appId;//应用id
    private String appName;//应用名称
    private String appDesc;//应用描述
    private int assemblyThreshold;//功能阈值
    private Date createTime;//创建时间
    private String createUserId;//创建用户ID
    private String userIpAddr;//创建人IP
    private int status;//状态
    private String ver;//版本号
    private Date lastMofityTime;//最后修改时间
    private int isAutoUninstall;//时候自动卸载
    private Date uninstallTime;//卸载时间
    private String url;
    private String isrun;
    private String include_app;
    private String source;
    private String taskId;
    //是否属于授权的
    private String isAuthorize;

    private String userName;

    private String userRealName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getIsAuthorize() {
        return isAuthorize;
    }

    public void setIsAuthorize(String isAuthorize) {
        this.isAuthorize = isAuthorize;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInclude_app() {
        return include_app;
    }

    public void setInclude_app(String include_app) {
        this.include_app = include_app;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAssemblyThreshold() {
        return assemblyThreshold;
    }

    public void setAssemblyThreshold(int assemblyThreshold) {
        this.assemblyThreshold = assemblyThreshold;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUserIpAddr() {
        return userIpAddr;
    }

    public void setUserIpAddr(String userIpAddr) {
        this.userIpAddr = userIpAddr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getLastMofityTime() {
        return lastMofityTime;
    }

    public void setLastMofityTime(Date lastMofityTime) {
        this.lastMofityTime = lastMofityTime;
    }

    public int getIsAutoUninstall() {
        return isAutoUninstall;
    }

    public void setIsAutoUninstall(int isAutoUninstall) {
        this.isAutoUninstall = isAutoUninstall;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getUninstallTime() {
        return uninstallTime;
    }

    public void setUninstallTime(Date uninstallTime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            this.uninstallTime = format.parse(format.format(uninstallTime));
        } catch (Exception e) {
            this.uninstallTime = uninstallTime;
        }

    }

    public String getIsrun() {
        return isrun;
    }

    public void setIsrun(String isrun) {
        this.isrun = isrun;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AppBaseInfo() {
    }

    @Override
    public String toString() {
        return "AppBaseInfo{" +
                "appId=" + appId +
                ", appName='" + appName + '\'' +
                ", appDesc='" + appDesc + '\'' +
                ", assemblyThreshold=" + assemblyThreshold +
                ", createTime=" + createTime +
                ", createUserId='" + createUserId + '\'' +
                ", userIpAddr='" + userIpAddr + '\'' +
                ", status=" + status +
                ", ver='" + ver + '\'' +
                ", lastMofityTime=" + lastMofityTime +
                ", isAutoUninstall=" + isAutoUninstall +
                ", uninstallTime=" + uninstallTime +
                ", url='" + url + '\'' +
                ", isrun='" + isrun + '\'' +
                ", include_app='" + include_app + '\'' +
                ", source='" + source + '\'' +
                ", taskId='" + taskId + '\'' +
                ", isAuthorize='" + isAuthorize + '\'' +
                '}';
    }
}
