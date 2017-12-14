package com.bocom.dto.pac;

import java.util.List;

import com.bocom.domain.pac.PackagingInfo;

/**
 * Created by bocom-qy on 2016/12/7.
 */
public class PackagingDto {

    private String oappid;//原生应用appId
    private String oappname;//原生应用appName
    private List<PackagingInfo> packagingInfos;//app_basket_baseinfo 表对应的实体类

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

    public List<PackagingInfo> getPackagingInfos() {
        return packagingInfos;
    }

    public void setPackagingInfos(List<PackagingInfo> packagingInfos) {
        this.packagingInfos = packagingInfos;
    }
}
