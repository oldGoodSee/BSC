package com.bocom.business.impl;

import com.bocom.business.AppBaseBusiness;
import com.bocom.util.DateUtil;
import com.bocom.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

@Service
public class AppBaseBusinessImpl implements AppBaseBusiness {


    private static Logger logger = LoggerFactory
            .getLogger(AppBaseBusinessImpl.class);

    @Value("${configs.filePath.url}")
    private String publishConfigPath;

    @Override
    public boolean publishIframe(String appId, String type, String json, String iframeId, String filePath) throws
            UnsupportedEncodingException{

        //iframe的文件夹
        String targetPath = publishConfigPath + "iframe_" + appId + File.separator;
        //iframe的文件
        String targetPathFile = targetPath + iframeId + "_" + type + ".json";

        if ("data".equalsIgnoreCase(type)) {
            //存档用的文件夹
            String savePath = publishConfigPath + "forAnalysis" + File.separator;
            //存档用的iframe的文件
            String savePathFile = savePath + iframeId + "_" + type + "_" + DateUtil.dateToStryyyyMMdd(new Date()) + "" +
                    ".json";
            FileUtil.createFile(URLDecoder.decode(json,"utf-8"), new File(savePathFile));
        }
        if (FileUtil.createFile(json, new File(targetPathFile))) {
//            FileUtil.copy(targetPathFile, filePath + "iframe_" + appId);
            return true;
        }
        return false;
    }

    @Override
    public String getContentIframe(String appId, String type, String iframeId) {

        //iframe的文件夹
        String targetPath = publishConfigPath + "iframe_" + appId + File.separator;

        //iframe的文件
        String targetPathFile = targetPath + iframeId + "_" + type + ".json";

        StringBuilder result = FileUtil.getFileContentUTF8(new File(targetPathFile));

        return null == result ? "" : result.toString();
    }
}
