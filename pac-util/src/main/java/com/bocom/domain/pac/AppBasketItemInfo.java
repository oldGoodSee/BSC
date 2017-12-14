package com.bocom.domain.pac;

public class AppBasketItemInfo {
    private Integer uuid;

    private String id;

    private String pageId;

    private String pageName;

    private String url;

    private String oappId;

    private String oappName;

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOappId() {
        return oappId;
    }

    public void setOappId(String oappId) {
        this.oappId = oappId;
    }

    public String getOappName() {
        return oappName;
    }

    public void setOappName(String oappName) {
        this.oappName = oappName;
    }

    @Override
    public String toString() {
        return "AppBasketItemInfo{" +
                "uuid=" + uuid +
                ", id='" + id + '\'' +
                ", pageId='" + pageId + '\'' +
                ", pageName='" + pageName + '\'' +
                ", url='" + url + '\'' +
                ", oappId='" + oappId + '\'' +
                ", oappName='" + oappName + '\'' +
                '}';
    }
}