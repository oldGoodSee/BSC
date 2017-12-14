package com.bocom.domain.pac;

import com.alibaba.fastjson.JSONArray;

import java.util.Date;

/**
 * 控件实体类
 *
 * @ClassName: AppWidgetInfo
 * @author: 钱勇
 * @date: 2017年02月28日 上午10:56:19
 */
public class AppWidgetInfo {
    private Integer widgetId;//控件ID
    private String widgetName;//控件名称
    private String fileName;//控件上传到文件服务器上的fileID
    private String path;//文件服务器的前缀 http:// [ip]:[端口]
    private Date createTime;//控件创建时间
    private Integer createType;//控件类型
    private String createId;//创建人员Name
    private String createUserName;//创建人员姓名
    private String ip;//创建人员IP
    private Integer status;//控件状态
    private Integer widgetInitId;//待编辑控件ID
    private Integer version;//控件版本号
    private String folderName;//控件下载到服务器上面文件夹的名称
    private Integer wrmId;//控件库那边保存的控件ID
    private Integer typeCode1;//一级分类
    private Integer typeCode2;//二级分类
    private String typeName1;//一级分类
    private String typeName2;//二级分类
    private String appImg;
    private JSONArray mAppImg;

    public Integer getTypeCode1() {
        return typeCode1;
    }

    public void setTypeCode1(Integer typeCode1) {
        this.typeCode1 = typeCode1;
    }

    public Integer getTypeCode2() {
        return typeCode2;
    }

    public void setTypeCode2(Integer typeCode2) {
        this.typeCode2 = typeCode2;
    }

    public String getTypeName1() {
        return typeName1;
    }

    public void setTypeName1(String typeName1) {
        this.typeName1 = typeName1;
    }

    public String getTypeName2() {
        return typeName2;
    }

    public void setTypeName2(String typeName2) {
        this.typeName2 = typeName2;
    }

    public Integer getWrmId() {
        return wrmId;
    }

    public void setWrmId(Integer wrmId) {
        this.wrmId = wrmId;
    }


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Integer getWidgetInitId() {
        return widgetInitId;
    }

    public void setWidgetInitId(Integer widgetInitId) {
        this.widgetInitId = widgetInitId;
    }

    public Integer getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Integer widgetId) {
        this.widgetId = widgetId;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateType() {
        return createType;
    }

    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAppImg() {
        return appImg;
    }

    public void setAppImg(String appImg) {
        this.appImg = appImg;
    }

    public JSONArray getmAppImg() {
        return mAppImg;
    }

    public void setmAppImg(JSONArray mAppImg) {
        this.mAppImg = mAppImg;
    }

    @Override
    public String toString() {
        return "AppWidgetInfo{" +
                "widgetId=" + widgetId +
                ", widgetName='" + widgetName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", path='" + path + '\'' +
                ", createTime=" + createTime +
                ", createType=" + createType +
                ", createId=" + createId +
                ", ip='" + ip + '\'' +
                ", status=" + status +
                '}';
    }
}
