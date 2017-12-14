package com.bocom.domain.pac;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */
public class PackagingInfo {

    private String appid;  //新组装应用ID
    private String appname;
    private List<String> pageids;//页面传递过来的用户勾选的所有页面id
    private String pageid;//单个页面ID
    private Date createtime;
    private int id;//篮子ID
    private String url;//页面访问地址
    private String pagename;//页面名称
    private String pageName;//页面名称
    private int sort;//排序值
    private int status;//页面当前状态
    private String oappid;
    private String oappname;
    private String showtype;
    private String resolution;
    private String position;
    private String fatherid;
    private String userid;
    private String orgid;


    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public PackagingInfo(String appid) {
        this.appid = appid;
    }

    public PackagingInfo(String userid, int id) {
        this.userid = userid;
        this.id = id;
    }

    public PackagingInfo() {
    }

    public String getShowtype() {
        return showtype;
    }

    public void setShowtype(String showtype) {
        this.showtype = showtype;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public List<String> getPageids() {
        return pageids;
    }

    public void setPageids(List<String> pageids) {
        this.pageids = pageids;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPagename() {
        return pagename;
    }

    public void setPagename(String pagename) {
        this.pagename = pagename;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOappid() {
        return oappid;
    }

    public void setOappid(String oappid) {
        this.oappid = oappid;
    }

    public String getOappname() {
        return oappname;
    }

    public void setOappname(String oappname) {
        this.oappname = oappname;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFatherid() {
        return fatherid;
    }

    public void setFatherid(String fatherid) {
        this.fatherid = fatherid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    @Override
    public String toString() {
        return "PackagingInfo{" +
                "appid='" + appid + '\'' +
                ", appname='" + appname + '\'' +
                ", pageids=" + pageids +
                ", pageid='" + pageid + '\'' +
                ", createtime=" + createtime +
                ", id=" + id +
                ", url='" + url + '\'' +
                ", pagename='" + pagename + '\'' +
                ", sort=" + sort +
                ", status=" + status +
                ", oappid='" + oappid + '\'' +
                ", oappname='" + oappname + '\'' +
                ", showtype='" + showtype + '\'' +
                ", resolution='" + resolution + '\'' +
                ", position='" + position + '\'' +
                ", fatherid='" + fatherid + '\'' +
                ", userid='" + userid + '\'' +
                ", orgid='" + orgid + '\'' +
                '}';
    }
}
