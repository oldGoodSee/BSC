package com.bocom.enums;

/**
 * 管理员类型类
 * 管理员编码  1.系统超级管理员（全部）2.组织超级管理员（操作当前组织、下一级组织 以及相关管理员信息） 3.组织管理员（操作当前组织人员信息）
 */
public enum DictionaryEnums {

    ACTION_NULL("0", "无"),
    ACTION_ADD("1", "新增"),
    ACTION_UPDATE("2", "更新"),
    ACTION_DELETE("3", "删除"),
    ACTION_VIEW("4", "访问"),
    BUSINESS_MICO_APP("100", "微应用"),
    BUSINESS_PAC("101", "组装应用"),
    BUSINESS_NOTICE("102", "通知"),
    BUSINESS_BASKET("103", "篮子"),
    BUSINESS_HOT_APP("104", "热门微应用"),
    BUSINESS_LOG("105", "日志"),
    BUSINESS_SERVICE("106", "微服务"),
    ERROR_MSG("88", "错误");

    /**
     * 数据字典code
     */
    private String dictionaryCode;

    /**
     * 数据字典Name
     */
    private String dictionaryName;

    DictionaryEnums(String adminCode, String adminName) {
        this.dictionaryCode = adminCode;
        this.dictionaryName = adminName;
    }

    public String getDictionaryCode() {
        return dictionaryCode;
    }

    void setDictionaryCode(String dictionaryCode) {
        this.dictionaryCode = dictionaryCode;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }


    /**
     * 根据管理员编号获取管理员类型枚举对象
     */
    public static DictionaryEnums getAdminTypeByAdminCode(String dictionaryCode) {
        for (DictionaryEnums adminTypeEnum : DictionaryEnums.values()) {
            if (adminTypeEnum.getDictionaryCode().equals(dictionaryCode)) {
                return adminTypeEnum;
            }
        }
        return DictionaryEnums.ACTION_NULL;
    }


    /**
     * 根据管理员编号获取管理员类型枚举对象
     */
    public static String getAdminNameByAdminCode(String adminCode) {
        for (DictionaryEnums adminTypeEnum : DictionaryEnums.values()) {
            if (adminTypeEnum.getDictionaryCode().equals(adminCode)) {
                return adminTypeEnum.getDictionaryName();
            }
        }
        return DictionaryEnums.ACTION_NULL.getDictionaryName();
    }


}
