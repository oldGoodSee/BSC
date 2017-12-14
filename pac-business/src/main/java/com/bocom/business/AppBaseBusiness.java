package com.bocom.business;

import java.io.UnsupportedEncodingException;

public interface AppBaseBusiness {

    boolean publishIframe(String appId, String type, String json, String iframeId,String filePath)throws
            UnsupportedEncodingException;

    String getContentIframe(String appId, String type, String iframeId);
}
