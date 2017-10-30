package com.mmall.utils;

import com.mmall.service.FileServiceImp;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    private static final Logger log = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPassword = PropertiesUtil.getProperty("ftp.pass");
    private static String workingDir = PropertiesUtil.getProperty("ftp.work.directory");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public static boolean uploadFile(List<File> fileList) {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPassword);
        log.info("连接ftp服务器");
        boolean result = ftpUtil.uploadFile(workingDir,fileList);
        log.info("上传文件结果{}",result);
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) {
        boolean uploaded = true;
        FileInputStream fileInputStream = null;
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                //更改工作目录
                ftpClient.changeWorkingDirectory(remotePath);
                //设置缓冲区
                ftpClient.setBufferSize(1024);
                //设置编码
                ftpClient.setControlEncoding("UTF-8");
                //设置文件类型为二进制文件，可以防止乱码问题
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //打开本地的被动模式（ftp服务上配置的被动）
                ftpClient.enterLocalPassiveMode();
                for (File file : fileList) {
                    fileInputStream = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(), fileInputStream);
                }
            } catch (IOException e) {
                log.error("上传文件异常", e);
                uploaded = false;
            } finally {
                closeStreamOrConnection(fileInputStream, this.ftpClient);
            }
        }
        return uploaded;
    }

    private boolean connectServer(String ip, int port, String user, String pwd) {
        boolean isSuccess = true;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            ftpClient.login(user, pwd);
        } catch (IOException e) {
            isSuccess = false;
            log.error("连接ftp服务器异常", e);
        }
        return isSuccess;
    }

    private void closeStreamOrConnection(FileInputStream fileInputStream, FTPClient ftpClient) {
        try {
            if (fileInputStream != null) {

                fileInputStream.close();
            }
            if (ftpClient != null) {
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            log.error("关闭流失败", e);
        }
    }

    public static String getFtpIp() {
        return ftpIp;
    }

    public static void setFtpIp(String ftpIp) {
        FTPUtil.ftpIp = ftpIp;
    }

    public static String getFtpUser() {
        return ftpUser;
    }

    public static void setFtpUser(String ftpUser) {
        FTPUtil.ftpUser = ftpUser;
    }

    public static String getFtpPassword() {
        return ftpPassword;
    }

    public static void setFtpPassword(String ftpPassword) {
        FTPUtil.ftpPassword = ftpPassword;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
