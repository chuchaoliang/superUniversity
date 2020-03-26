package com.ccl.wx.util;

import cn.hutool.extra.ftp.Ftp;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.properties.FtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author CCL
 */
@Slf4j
@Component
public class FtpUtil {

    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static FtpProperties ftpProperties;

    private static String FILE_USELESS_PREFIX;

    @Value("${file.upload.useless.local.path}")
    private void setFILE_USELESS_PREFIX(String FILE_USELESS_PREFIX) {
        FtpUtil.FILE_USELESS_PREFIX = FILE_USELESS_PREFIX;
    }

    @Resource
    private FtpProperties initFtpProperties;

    /**
     * 获取ftp对象
     */
    private static Ftp ftp;

    @PostConstruct
    public void init() {
        ftpProperties = initFtpProperties;
        ftp = new Ftp(ftpProperties.getHost(), ftpProperties.getPort(), ftpProperties.getUser(), ftpProperties.getPassword());
    }

    public static String uploadFile(String userid, MultipartFile file) {
        String filename = CclUtil.getFileUploadName(file);
        String fileaddress = CclUtil.getFileUploadAddress(userid, file).replace("\\", "/");
        if (EnumResultStatus.FAIL.getValue().equals(fileaddress)) {
            // 上传文件类型不支持
            return EnumResultStatus.UNKNOWN.getValue();
        }
        log.info("文件名：" + filename);
        log.info("文件地址：" + fileaddress);
        log.info("处理后的文件地址：" + CclUtil.delStringPrefix(fileaddress, ftpProperties.getBasePath()));
        boolean uploadStatus = upload(CclUtil.delStringPrefix(fileaddress, ftpProperties.getBasePath()), filename, file);
        if (uploadStatus) {
            logger.info("文件：【" + file.getOriginalFilename() + "】上传成功");
            return ftpProperties.getHttpPath() + CclUtil.delStringPrefix(fileaddress, FILE_USELESS_PREFIX) + filename;
        } else {
            logger.info("文件：【" + file.getOriginalFilename() + "】上传失败");
            // 上传文件失败
            return EnumResultStatus.FAIL.getValue();
        }
    }

    public static String delFile(String filepath) {
        if (StringUtils.isEmpty(filepath)) {
            return "fail";
        } else {
            boolean deleteFile = deleteFile(CclUtil.getFileLocation(filepath, ftpProperties.getHttpPath()), CclUtil.getFileName(filepath));
            if (deleteFile) {
                logger.info("图片路径：【" + filepath + "】删除成功！");
                return EnumResultStatus.SUCCESS.getValue();
            } else {
                logger.info("圈子头像路径：【" + filepath + "】删除失败！");
                return EnumResultStatus.UNKNOWN.getValue();
            }
        }
    }


    /**
     * Description: 向FTP服务器上传文件
     *
     * @param filePath FTP服务器文件存放路径。
     * @param filename 上传到FTP服务器上的文件名
     * @param file     文件
     * @return 成功返回true，否则返回false
     */
    public static boolean upload(String filePath, String filename, MultipartFile file) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            // 连接ftp
            ftp.connect(ftpProperties.getHost(), ftpProperties.getPort());
            ftp.login(ftpProperties.getUser(), ftpProperties.getPassword());
            // 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                logger.info("服务器【" + ftpProperties.getHost() + ":" + ftpProperties.getPort() + "】连接失败！");
                return false;
            }
            logger.info("服务器【" + ftpProperties.getHost() + ":" + ftpProperties.getPort() + "】连接成功！");
            //切换到上传目录
            if (!ftp.changeWorkingDirectory(filePath)) {
                //如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = ftpProperties.getBasePath();
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return false;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // 设置为被动模式上传文件
            ftp.enterLocalPassiveMode();
            //上传文件
            if (!ftp.storeFile(filename, file.getInputStream())) {
                return false;
            }
            file.getInputStream().close();
            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * 删除文件 *
     *
     * @param pathname FTP服务器保存目录 *
     * @param filename 要删除的文件名称 *
     * @return
     */
    public static boolean deleteFile(String pathname, String filename) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            ftpClient.connect(ftpProperties.getHost(), ftpProperties.getPort());
            ftpClient.login(ftpProperties.getUser(), ftpProperties.getPassword());
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.info("服务器【" + ftpProperties.getHost() + ":" + ftpProperties.getPort() + "】连接失败！");
            }
            logger.info("服务器【" + ftpProperties.getHost() + ":" + ftpProperties.getPort() + "】连接成功！");
            //切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.dele(filename);
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param host       FTP服务器hostname
     * @param port       FTP服务器端口
     * @param username   FTP登录账号
     * @param password   FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @param localPath  下载后保存到本地的路径
     * @return
     */
    //public static boolean down(String host, int port, String username, String password, String remotePath,
    //                           String fileName, String localPath) {
    //    boolean result = false;
    //    FTPClient ftp = new FTPClient();
    //    try {
    //        int reply;
    //        ftp.connect(host, port);
    //        // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
    //        ftp.login(username, password);// 登录
    //        reply = ftp.getReplyCode();
    //        if (!FTPReply.isPositiveCompletion(reply)) {
    //            ftp.disconnect();
    //            return result;
    //        }
    //        ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
    //        FTPFile[] fs = ftp.listFiles();
    //        for (FTPFile ff : fs) {
    //            if (ff.getName().equals(fileName)) {
    //                File localFile = new File(localPath + "/" + ff.getName());
    //
    //                OutputStream is = new FileOutputStream(localFile);
    //                ftp.retrieveFile(ff.getName(), is);
    //                is.close();
    //            }
    //        }
    //        ftp.logout();
    //        result = true;
    //    } catch (IOException e) {
    //        e.printStackTrace();
    //    } finally {
    //        if (ftp.isConnected()) {
    //            try {
    //                ftp.disconnect();
    //            } catch (IOException ioe) {
    //            }
    //        }
    //    }
    //    return result;
    //}
}