package com.bocom.util;

import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 通过fastDFS来管理文件
 *
 * @ClassName: FileUtil
 * @author: 韦冬冬
 * @date: 2016年11月25日 上午10:56:19
 */
public class FastDfsUtil {
    private static final Logger logger = LoggerFactory
            .getLogger(FastDfsUtil.class);
    private static String confFileName = Thread.currentThread().getContextClassLoader().getResource("/").getPath() +
            "conf" + File.separator + "fdfs_client.conf";
    private static StorageServer storageServer;
    private static TrackerServer trackerServer;
    private static StorageClient1 storageClient1;

    public FastDfsUtil() {
        try {
            if (trackerServer != null && !ProtoCommon.activeTest(trackerServer.getSocket())) {
                ClientGlobal.init(confFileName);
                TrackerClient tracker = new TrackerClient();
                trackerServer = tracker.getConnection();
                storageClient1 = new StorageClient1(trackerServer, storageServer);
                logger.debug("init FastDfsServer success ");
            }
        } catch (FileNotFoundException e) {
            logger.error("uploadFile FileNotFoundException init error ", e);
        } catch (IOException e) {
            logger.error("uploadFile IOException init error ", e);
        } catch (Exception e) {
            logger.error("uploadFile Exception init error ", e);
        }
    }

    /**
     * 上传文件
     *
     * @param
     * @throws Exception
     * @throws IOException
     * @Title: uploadFile
     * @author: 韦冬冬
     * @date: 2016年11月25日 上午10:56:57
     * @return: String[] 文件在服务器上的坐标
     */
    public String uploadFile(byte[] b, String fileName) {
        try {
            // 获取文件类型
            String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
            return storageClient1.upload_file1(b, fileType, null);
        } catch (FileNotFoundException e) {
            logger.error("uploadFile FileNotFoundException error ", e);
        } catch (IOException e) {
            logger.error("uploadFile IOException error ", e);
        } catch (Exception e) {
            logger.error("uploadFile Exception error ", e);
        }
        return "";
    }

    /**
     * 上传文件
     *
     * @param filePath 文件路径
     * @Title: uploadFile
     * @author: 韦冬冬
     * @date: 2016年12月16日 下午4:46:43
     * @return: String 文件在服务器上的虚拟路径
     */
    public String uploadFile(String filePath) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("confFileName ==== " + confFileName);
            }
            // 获取文件类型
            String fileType;
            File file = new File(filePath);
            if (file.getName().contains(".")) {
                fileType = filePath.substring(filePath.lastIndexOf('.') + 1);
            } else {
                fileType = null;
            }
            return storageClient1.upload_file1(filePath, fileType, null);
        } catch (FileNotFoundException e) {
            logger.error("uploadFile FileNotFoundException error ", e);
        } catch (IOException e) {
            logger.error("uploadFile IOException error ", e);
        } catch (Exception e) {
            logger.error("uploadFile Exception error ", e);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param fileInfo 文件在服务器上的坐标
     * @Title: deleteFile
     * @author: 韦冬冬
     * @date: 2016年11月25日 上午10:57:46
     * @return: boolean 删除成功 true 删除失败 false
     */
    public boolean deleteFile(String fileInfo) {
        try {
            int i = storageClient1.delete_file1(fileInfo);
            if (logger.isDebugEnabled()) {
                logger.debug("fastDFS删除文件返回值：" + i);
            }
            return i == 0;
        } catch (Exception e) {
            logger.error("deleteFile Exception error ", e);
        }
        return false;
    }

    /**
     * 从服务器上下载文件
     *
     * @param fileInfo 文件在服务器上的坐标
     * @Title: downloadFile
     * @author: 韦冬冬
     * @date: 2016年11月25日 下午3:47:47
     * @return: 二进制数组
     */
    public byte[] downloadFile(String filePath) {
        try {
            return storageClient1.download_file1(filePath);
        } catch (FileNotFoundException e) {
            logger.error("downloadFile FileNotFoundException error ", e);
        } catch (IOException e) {
            logger.error("downloadFile IOException error ", e);
        } catch (Exception e) {
            logger.error("downloadFile Exception error ", e);
        }
        return new byte[0];
    }

    public boolean downloadFile(String fileInfo, String outFile) {
        try (FileOutputStream fos = new FileOutputStream(outFile);) {
            byte[] b = storageClient1.download_file1(fileInfo);
            fos.write(b);
            return true;
        } catch (FileNotFoundException e) {
            logger.error("downloadFile FileNotFoundException error ", e);
        } catch (IOException e) {
            logger.error("downloadFile IOException error ", e);
        } catch (Exception e) {
            logger.error("downloadFile Exception error ", e);
        }
        return false;

    }
}
