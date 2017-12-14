package com.bocom.domain.pac;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppOperationLogInfo {

    private Integer id;

    private String operationType;//操作类型

    private String businessType;//操作类型

    private String content;//操作内容

    private String userName;//用户name

    private String orgId;//用户组织ID

    private String orgName;//用户组织name

    private Date createTime;

    private String ipAddr;

    private String userId;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public String getCreateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AppOperationLogInfo{" +
                "id=" + id +
                ", operationType='" + operationType + '\'' +
                ", content='" + content + '\'' +
                ", userName='" + userName + '\'' +
                ", orgId='" + orgId + '\'' +
                ", orgName='" + orgName + '\'' +
                ", createTime=" + createTime +
                ", ipAddr='" + ipAddr + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}