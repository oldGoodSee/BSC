package com.bocom.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;

public class FileUtil {

    private FileUtil() {
    }

    private static Logger logger = LoggerFactory
            .getLogger(FileUtil.class);

    public static boolean createFile(String fileContent, File configFile) {
        File filePath = new File(configFile.getParent());
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        try (OutputStreamWriter fileWriter = new OutputStreamWriter(
                new FileOutputStream(configFile), "UTF-8")) {
            fileWriter.write(URLDecoder.decode(fileContent, "utf-8"));
        } catch (Exception e) {
            logger.error("FileUtil.createFile error ", e);
            return false;
        }
        return true;
    }

    public static StringBuilder getFileContent(File configFile) {
        StringBuilder result = new StringBuilder();
        if (!configFile.exists()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(
                new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            logger.error("FileUtil.getFileContent error ", e);
            return null;
        }
        return result;
    }

    public static StringBuilder getFileContentUTF8(File configFile) {
        StringBuilder result = new StringBuilder();
        if (!configFile.exists()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile),
                "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            logger.error("FileUtil.getFileContent error ", e);
            return null;
        }
        return result;
    }


    /**
     * 拷贝文件
     *
     * @param src 文件/文件夹
     * @param des 文件夹
     */
    public static void copy(String src, String des) {
        File file = new File(src);
        File file2 = new File(des);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (file.isFile()) {
            fileCopy(file.getPath(), des + File.separator + file.getName()); //调用文件拷贝的方法
        } else if (file.isDirectory()) {
            copyFile(file.getPath(), des);
        }

    }

    /**
     * 拷贝文件夹
     *
     * @param src
     * @param des
     */
    private static void copyFile(String src, String des) {
        File file1 = new File(src);
        File[] fs = file1.listFiles();
        for (File f : fs) {
            if (f.isFile()) {
                fileCopy(f.getPath(), des + File.separator + f.getName()); //调用文件拷贝的方法
            } else if (f.isDirectory()) {
                copy(f.getPath(), des + File.separator + f.getName());
            }
        }

    }

    /**
     * 文件拷贝的方法
     */
    private static void fileCopy(String src, String des) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(src)));
             PrintStream ps = new PrintStream(new FileOutputStream(des))
        ) {
            String s = null;
            while ((s = br.readLine()) != null) {
                ps.println(s);
                ps.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
