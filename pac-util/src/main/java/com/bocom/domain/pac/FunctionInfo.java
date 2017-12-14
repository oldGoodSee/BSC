package com.bocom.domain.pac;

/**
 * Created by Administrator on 2016/11/17.
 */
public class FunctionInfo {

    private String function_name;
    private String function_url;
    private int id;
    private int sort;
    private String sign;
    private Integer flag;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public FunctionInfo() {
    }

    public FunctionInfo(String function_name, String function_url, int id,
                        int sort) {
        super();
        this.function_name = function_name;
        this.function_url = function_url;
        this.id = id;
        this.sort = sort;
    }

    public String getFunction_name() {
        return function_name;
    }

    public void setFunction_name(String function_name) {
        this.function_name = function_name;
    }

    public String getFunction_url() {
        return function_url;
    }

    public void setFunction_url(String function_url) {
        this.function_url = function_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
