package com.bocom.domain.pac;

import java.util.Date;

/**
 * Created by bocom-qy on 2017-1-16.
 */
public class NoticeInfo {

    private int id;
    private String content;//通知内容
    private int sort;//序号
    private Date createTime;//创建时间
    private Date lastModifyTime;//最后修改时间
    private String createUserId;//创建用户ID
    private int frequency;//频率
    private String noticeType;//通知类型  -- public（全局）  --private（跟应用挂钩）
    private int deleteFlag;//删除标识
    private int appId;//组装应用ID
    private String appName;//组装应用名称

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "NoticeInfo{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", sort=" + sort +
                ", createTime=" + createTime +
                ", lastModifyTime=" + lastModifyTime +
                ", createUserId='" + createUserId + '\'' +
                ", frequency=" + frequency +
                ", noticeType='" + noticeType + '\'' +
                ", deleteFlag=" + deleteFlag +
                ", appId=" + appId +
                ", appName='" + appName + '\'' +
                '}';
    }
}
