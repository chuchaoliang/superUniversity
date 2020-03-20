package com.ccl.wx.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * @ClassName: FileUtil
 * @Description: 文件上传工具类
 * @Version 1.0
 **/
public class FileUtil {
    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    /**
     * 文件上传路径前缀
     */
    public static String uploadSuffixPath;
    /**
     * 本地磁盘目录
     */
    public static String uploadLocalPath;

    /**
     * @Title: uploadFile
     * @Description: 单文件上传到本地磁盘
     * @param: multipartFile
     * @return: java.lang.String
     * @throws:
     */
    public static String uploadFile(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return null;
        }
        //获取文件相对路径
        String fileName = getUploadFileName(multipartFile.getOriginalFilename());
        String dateDir = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        File destFileDir = new File(uploadLocalPath + File.separator + dateDir);
        if (!destFileDir.exists()) {
            destFileDir.mkdirs();
        }
        try {
            File destFile = new File(destFileDir.getAbsoluteFile() + File.separator + fileName);
            multipartFile.transferTo(destFile);
            logger.info("文件【" + multipartFile.getOriginalFilename() + "】上传成功");
            return dateDir + File.separator + fileName;
        } catch (IOException e) {
            logger.error("文件上传异常：" + multipartFile.getOriginalFilename(), e);
            return null;
        }
    }

    public static String getFileName(Integer userId) {
        //1、获取当前时间的毫秒数
        long currentTimeMillis = System.currentTimeMillis();
        //2、创建Random对象，获取0-999之间随机数
        Random random = new Random();
        int randomNum = random.nextInt(999);
        //3、最终的文件名
        //%03d: %是占位符 3:3位数  0:不够3位数补零   d:数字
        String fileName = currentTimeMillis + userId + String.format("%03d", randomNum);
        //4、返回文件名
        return fileName;
    }

    /**
     * @Title: getUploadFilePath
     * @Description: 获取上传后的文件相对路径  --数径
     * @param: fileName据库存储该路
     * @return: java.lang.String
     * @throws:
     */
    public static String getUploadFileName(String fileName) {
        return new StringBuilder()
                .append(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN))
                .append("_").append(CclUtil.getRandomStrByNum(6))
                .append(".").append(FilenameUtils.getExtension(fileName))
                .toString();
    }

    /**
     * @Title: isFileBySuffix
     * @Description: 通过后缀名判断是否是某种文件
     * @param: fileName 文件名称
     * @param: suffix 后缀名
     * @return: boolean
     * @throws:
     */
    public static boolean isFileBySuffix(String fileName, String suffix) {
        if (StringUtils.isNoneBlank(fileName) && StringUtils.isNoneBlank(suffix)) {
            return fileName.endsWith(suffix.toLowerCase()) || fileName.endsWith(suffix.toUpperCase());
        }
        return false;
    }
}
