package com.bocom.domain.pac;

/**
 * Created by Administrator on 2017/5/12.
 */
public class PoliceApp {

    private MicroApp[] micropps;//一个警务应用会包含多个微应用（功能页面）
    private String appid;
    private String appname;
    private String url;

    public MicroApp[] getMicropps() {
        return micropps;
    }

    public void setMicropps(MicroApp[] micropps) {
        this.micropps = micropps;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
