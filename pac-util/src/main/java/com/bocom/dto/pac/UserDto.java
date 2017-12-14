package com.bocom.dto.pac;

import com.bocom.domain.UserRoleOrg;

import java.util.List;

/**
 * Created by bocom-qy on 2016/12/8.
 */
public class UserDto {

    private int orgCode ;

    private String orgName ;

    List<UserRoleOrg> userRoleOrgs;

    public int getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(int orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<UserRoleOrg> getUserRoleOrgs() {
        return userRoleOrgs;
    }

    public void setUserRoleOrgs(List<UserRoleOrg> userRoleOrgs) {
        this.userRoleOrgs = userRoleOrgs;
    }
}
