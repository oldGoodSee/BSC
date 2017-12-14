package com.bocom.domain.pac;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/28.
 */
public class BehaviorInfo {

    private Date time;
    private String userName;
    private String userId;
    private String content;
    private int operateType;
    private String ipAddr;

    public BehaviorInfo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BehaviorInfo(Date time, String userName, String content, int operateType,
                        String ipAddr, String methodName) {
        this.time = time;
        this.userName = userName;
        this.content = content;
        this.operateType = operateType;
        this.ipAddr = ipAddr;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    @Override
    public String toString() {
        return userName + "于：" + time + "执行了关键性操作，操作内容是：" + content + ";" +
                "操作类型为：" + operateType + ";ip地址为：" + ipAddr + ";";
    }
}
