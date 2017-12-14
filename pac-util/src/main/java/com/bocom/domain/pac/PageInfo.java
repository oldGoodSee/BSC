package com.bocom.domain.pac;

import com.github.pagehelper.Page;

/**
 * Created by Administrator on 2017/5/15.
 */
public class PageInfo implements Comparable<PageInfo> {

    private String id;
    private String pageurl;
    private String pagedesc;
    private String pagename;
    private String appuuid;
    private String appid;
    private String appname;
    private String imgUrl;
    private Integer composenum;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPageurl() {
        return pageurl;
    }

    public void setPageurl(String pageurl) {
        this.pageurl = pageurl;
    }

    public String getPagedesc() {
        return pagedesc;
    }

    public void setPagedesc(String pagedesc) {
        this.pagedesc = pagedesc;
    }

    public String getPagename() {
        return pagename;
    }

    public void setPagename(String pagename) {
        this.pagename = pagename;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public Integer getComposenum() {
        return composenum;
    }

    public void setComposenum(Integer composenum) {
        this.composenum = composenum;
    }

    public String getAppuuid() {
        return appuuid;
    }

    public void setAppuuid(String appuuid) {
        this.appuuid = appuuid;
    }

    // 实现Comparable接口中的这个方法
    public int compareTo(PageInfo pageInfo) {
        //从大到小排序
        return pageInfo.getComposenum().compareTo(this.getComposenum());
        //从小到大排序
//        return this.getComposenum().compareTo(pageInfo.getComposenum());
    }
}
