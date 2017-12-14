package com.bocom.dto.session;

public class UserRoleInfo {

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色编号 1.领导 2.专员
     */
    private String roleCode;

    /**
     * 组织编码
     */
    private String roleOrgCode;

    /**
     * 组织名称
     */
    private String roleOrgName;

    /**
     * 父组织名称
     */
    private String parentOrgName;

    /**
     * 父组织编码
     */
    private String parentOrgCode;


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleOrgCode() {
        return roleOrgCode;
    }

    public void setRoleOrgCode(String roleOrgCode) {
        this.roleOrgCode = roleOrgCode;
    }

    public String getRoleOrgName() {
        return roleOrgName;
    }

    public void setRoleOrgName(String roleOrgName) {
        this.roleOrgName = roleOrgName;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public String getParentOrgCode() {
        return parentOrgCode;
    }

    public void setParentOrgCode(String parentOrgCode) {
        this.parentOrgCode = parentOrgCode;
    }
}
