package com.bocom.util.Behavior;

/**
 * Created by Richard.W on 2016/11/29.
 */

public enum Configs {

    OPERATE_TYPE_VISIT_PAGE(1), //访问界面类
    OPERATE_TYPE_CREATE_NEW_APP(2),//创建新组装APP
    OPERATE_TYPE_EDIT(3),//编辑类动作
    OPERATE_TYPE_DELETE(4),//删除/卸载类动作
    OPERATE_TYPE_CREATE_EXCEPT_APP(5), //新建类的动作，不包含APP，例如滚动通知
    PAGE_STATUS_RUN(1),//运行中
    PAGE_STATUS_EDIT(6),//编辑中
    APP_ID_ADMIN("1")
    ;

    private String stringCode;
    private int intCode;
    private boolean booleanCode;

    private Configs(){}
    private Configs(String stringCode){this.stringCode=stringCode;}
    private Configs(int intCode){this.intCode=intCode;}
    private Configs(boolean booleanCode){this.booleanCode=booleanCode;}


    public String getStringCode() {
        return stringCode;
    }

    public void setStringCode(String stringCode) {
        this.stringCode = stringCode;
    }

    public int getIntCode() {
        return intCode;
    }

    public void setIntCode(int intCode) {
        this.intCode = intCode;
    }

    public boolean isBooleanCode() {
        return booleanCode;
    }

    public void setBooleanCode(boolean booleanCode) {
        this.booleanCode = booleanCode;
    }

    public static void main(String[] a){

        System.out.println(Configs.OPERATE_TYPE_CREATE_NEW_APP);
    }
}
