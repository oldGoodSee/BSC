package com.bocom.domain.pac;

/**
 * Created by Administrator on 2016/12/8.
 */
public class ResultDo {

    private boolean success;
    private String message;

    public ResultDo(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResultDo() {
        this.success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
