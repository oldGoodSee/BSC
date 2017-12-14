package com.bocom.util;

public class ResponseVoUtil {
    public static ResponseVo success(Object object){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setSuccess(true);
        responseVo.setData(object);
        return responseVo;
    }

    public static ResponseVo fail(String msg, Object object){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setSuccess(false);
        responseVo.setData(object);
        responseVo.setMessage(msg);
        return responseVo;
    }
}
