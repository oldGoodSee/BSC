package com.bocom.business;


import java.util.Map;

public interface PublishAppBusiness {
    Map<String, Object> publishApp(String file, String appId, String rate, String userId);
    Map<String, Object> saveConfigs(String file, String appId, String rate, String userId,
                                    String userTypeFileName,
                                    String iframeId, String configsFilePath);
}
