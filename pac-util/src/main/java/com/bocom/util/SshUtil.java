package com.bocom.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SshUtil {

    private static String IP = ConfigGetPropertyUtil.get("linux.ip");
    private static int PORT = 22;
    private static String USERNAME = ConfigGetPropertyUtil.get("linux.user.name");
    private static String PASSWORD = ConfigGetPropertyUtil.get("linux.user.pass");

    private static Logger logger = LoggerFactory
            .getLogger(SshUtil.class);

    /**
     * 判断布局文件是否存在  不存在则复制一个新的文件到项目中
     *
     * @param appId
     */
    public static void validateConfigFile(String appId, String targetFilePath) {
        try {
            String targetFile = targetFilePath + "configs" + appId + "_txt";
            String sourceFile = ConfigGetPropertyUtil.get("configs.filePath.url")
                    + "configs" + appId + "_txt";
            // 复制文件
            cpFile(targetFile, sourceFile, false);

            File iframeFIle = new File(ConfigGetPropertyUtil.get("configs.filePath.url") + "iframe_" + appId);
            if (iframeFIle.exists()) {
                sourceFile = ConfigGetPropertyUtil.get("configs.filePath.url") + "iframe_" + appId;
                FileUtil.copy(sourceFile, targetFilePath);
            }
        } catch (Exception e) {
            logger.error("Connection linux error  " + e);
        }
    }

    /**
     * 判断布局文件是否存在  不存在则复制一个新的文件到项目中
     *
     * @param appId
     */
    public static void validateConfigFileToNewApp(String appId, String targetFilePath, String newAppId) {
        try {
            String targetFile = targetFilePath + "configs" + appId + "_txt";
            String sourceFile = ConfigGetPropertyUtil.get("configs.filePath.url")
                    + "configs" + appId + "_txt";
            // 复制文件
            cpFile(targetFile, sourceFile, false);

            File iframeFIle = new File(ConfigGetPropertyUtil.get("configs.filePath.url") + "iframe_" + appId);
            if (iframeFIle.exists()) {
                sourceFile = ConfigGetPropertyUtil.get("configs.filePath.url") + "iframe_" + appId;
                FileUtil.copy(sourceFile, ConfigGetPropertyUtil.get("configs.filePath.url") + "iframe_" + newAppId);
            }
        } catch (Exception e) {
            logger.error("Connection linux error  " + e);
        }
    }

    /**
     * 判断布局文件是否存在  不存在则复制一个新的文件到项目中(2.0)
     *
     * @param appId
     */
    public static void validateConfigFileToNewAppForSave(String appId, String targetFilePath, String newAppId) {
        try {
//            String targetFile = targetFilePath + "configs" + appId + "_txt";
            String targetFile = targetFilePath + appId;
            String sourceFile = ConfigGetPropertyUtil.get("configs.filePath.url")
                    + "backUp/" + appId;
            // 复制文件
            cpFile(targetFile, sourceFile, false);
        } catch (Exception e) {
            logger.error("Connection linux error  " + e);
        }
    }

    private static void executeCmd(String cmd) throws IOException, InterruptedException {
        String[] cmds = {"/bin/sh", "-c", cmd};
        for (String cmdStr : cmds) {
            logger.debug("cmdStr  >>>>>>>  " + cmdStr);
        }
        Process pro = Runtime.getRuntime().exec(cmds);
        pro.waitFor();
        InputStream in = pro.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = read.readLine()) != null) {
            logger.debug("cmdStr result >>>>>>>>>>>>> " + line);
        }
        read.close();
        in.close();
        pro.destroy();
    }

    public static String cpFile(String targetFile, String sourceFile, boolean deleteFlag)
            throws IOException {
        String result = "";
        String line;
        try (
                //InputStream :1）抽象类，2）面向字节形式的I/O操作（8 位字节流） 。
                InputStream inputStream = new FileInputStream(sourceFile);
                //Reader :1）抽象类，2）面向字符的 I/O操作（16 位的Unicode字符） 。
                Reader reader = new InputStreamReader(inputStream, "UTF-8");
                //增加缓冲功能
                BufferedReader bufferedReader = new BufferedReader(reader);
                OutputStreamWriter fileWriter = new OutputStreamWriter(
                        new FileOutputStream(targetFile), "UTF-8");
        ) {
            while ((line = bufferedReader.readLine()) != null) {
                fileWriter.write(line);
            }
            result = "success";
        } catch (Exception e) {
            logger.error("cp file error  >>>>   " + e);
            result = "error";
        }
        return result;
    }

    /**
     * 执行命令
     *
     * @param command
     */
    public static String executeCom(String command) {
        String result = "";
        try {
            executeCmd(command);
            result = "success";
        } catch (Exception e) {
            logger.error("execute command error" + e);
            result = "error";
        }
        return result;
    }
}
