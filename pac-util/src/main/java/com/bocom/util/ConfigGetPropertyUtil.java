package com.bocom.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigGetPropertyUtil {

    private static final String PROPERTIES_PATH = "conf/application.properties";

    private static Properties properties = new Properties();

    // log
    private static Logger logger = LoggerFactory
            .getLogger(ConfigGetPropertyUtil.class);

    private ConfigGetPropertyUtil() {

    }

    private void onStartup() {
        try {
            String classesPath = this.getClass().getClassLoader().getResource("/").getPath();
            InputStream in = new FileInputStream(classesPath + PROPERTIES_PATH);
            properties.load(in);
        } catch (IOException e) {
            logger.error("onStartup properties error" + e);
        }
    }

    public static String get(String key) {
        if (properties.isEmpty()) {
            new ConfigGetPropertyUtil().onStartup();
        }
        return properties.getProperty(key);
    }

}
