package com.bocom.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ZipFileUtils {

    private static Logger logger = LoggerFactory
            .getLogger(ZipFileUtils.class);

    private static final String EN_CODING = "utf-8";

    /**
     * 压缩文件和文件夹
     *
     * @param srcPathname 需要被压缩的文件或文件夹路径
     * @param zipFilepath 将要生成的zip文件路径
     */
    public boolean zip(String srcPathname, String zipFilepath, boolean deleteFile) {
        File file = new File(srcPathname);
        if (!file.exists() && logger.isDebugEnabled()) {
            logger.debug("source file or directory " + srcPathname + " does not exist.");
            return false;
        }
        Project project = new Project();
        FileSet fileSet = new FileSet();
        fileSet.setProject(project);
        // 判断是目录还是文件
        if (file.isDirectory()) {
            fileSet.setDir(file);
        } else {
            fileSet.setFile(file);
        }

        Zip zip = new Zip();
        zip.setProject(project);
        zip.setDestFile(new File(zipFilepath));
        zip.addFileset(fileSet);
        zip.setEncoding(EN_CODING);
        zip.execute();
        if (logger.isDebugEnabled()) {
            logger.debug(srcPathname + "  Compressed file success  the path is " + zipFilepath + ", deleteFile is " + deleteFile);
        }
        if (deleteFile) {
            deleteFileByPath(file);
        }
        return true;
    }

    /**
     * 解压缩文件和文件夹
     *
     * @param zipFilepath 需要被解压的zip文件路径
     * @param destDir     将要被解压到哪个文件夹
     * @param deleteFile  是否删除文件
     */
    public boolean unzip(String zipFilepath, String destDir, boolean deleteFile) {
        File zipFile = new File(zipFilepath);
        if (!zipFile.exists() && logger.isDebugEnabled()) {
            logger.debug("source file or directory " + zipFilepath + " does not exist.");
            return false;
        }
        Project project = new Project();
        Expand expand = new Expand();
        expand.setProject(project);
        expand.setTaskType("unzip");
        expand.setTaskName("unzip");
        expand.setEncoding(EN_CODING);
        expand.setSrc(new File(zipFilepath));
        expand.setDest(new File(destDir));
        expand.execute();
        if (logger.isDebugEnabled()) {
            logger.debug("Extract file success " + zipFilepath + ", the path is  " + destDir);
        }
        if (deleteFile) {
            deleteFileByPath(zipFile);
        }
        return true;

    }

    /**
     * 删除目录下所有的文件;
     *
     * @param file 源文件
     */
    public boolean deleteFileByPath(File file) {
        String[] files;
        if (file != null) {
            files = file.list();
        } else {
            return false;
        }
        if (file.isDirectory() && files != null) {
            for (String newFile : files) {
                boolean bol = deleteFileByPath(new File(file, newFile));
                if (!bol && logger.isDebugEnabled()) {
                    logger.debug("删除失败" + newFile);
                    return false;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("删除成功" + file.getName());
        }
        return file.delete();
    }

}
