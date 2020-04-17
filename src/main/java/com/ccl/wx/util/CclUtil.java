package com.ccl.wx.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.ccl.wx.enums.EnumResultStatus;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author CCL 褚超亮de工具类
 */
@Component
public class CclUtil {

    private final static String FILEWEBPATH = "http://localhost:8080/fileSuffix/";

    /**
     * 常见的图片类型
     */
    private final static String IMAGE_TYPE = "jpeg,png,gif,bmp,jpg";

    /**
     * 常见的音频类型
     */
    private final static String VOICE_TYPE = "mp3,wma,flac,aac,mmf,amr,m4a,m4r,Ogg,wav,wavpack,au,CD";

    /**
     * 常见的视频类型
     */
    private final static String VIDEO_TYPE = "mp4,avi,3gp,rmvb,rm,wmv,mkv,mpeg,vob,mov,swf,flv,f4v,dat,VCD,DVD";

    /**
     * 文件上传路径
     */
    private static String FILE_LOCAL_PATH;

    /**
     * 上传的文件根目录
     *
     * @param FILE_LOCAL_PATH
     */
    @Value("${file.upload.local.path}")
    private void setFILE_LOCAL_PATH(String FILE_LOCAL_PATH) {
        CclUtil.FILE_LOCAL_PATH = FILE_LOCAL_PATH;
    }

    private static String FILE_UPLOAD_SUFFIX_PATH;

    @Value("${file.upload.suffix.path}")
    private void setFILE_UPLOAD_SUFFIX_PATH(String FILE_UPLOAD_SUFFIX_PATH) {
        CclUtil.FILE_UPLOAD_SUFFIX_PATH = FILE_UPLOAD_SUFFIX_PATH;
    }

    /**
     * nginx映射目录
     */
    private static String FILE_UPLOAD_USELESS_LOCAL_PATH;

    @Value("${file.upload.useless.local.path}")
    private void setFILE_UPLOAD_USELESS_LOCAL_PATH(String FILE_UPLOAD_USELESS_LOCAL_PATH) {
        CclUtil.FILE_UPLOAD_USELESS_LOCAL_PATH = FILE_UPLOAD_USELESS_LOCAL_PATH;
    }

    public static String CHAR_STR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 比较两个对象的属性值是否相等，相等true，不相等false
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean compareObjectAttribute(Object obj1, Object obj2) {
        try {
            if (obj1.getClass() == obj2.getClass()) {
                Class clazz = obj1.getClass();
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    //获取属性值
                    String name = pd.getName();
                    //get方法
                    Method readMethod = pd.getReadMethod();
                    Object o1 = readMethod.invoke(obj1);
                    Object o2 = readMethod.invoke(obj2);
                    // 如果不相等则return false
                    if (!o1.equals(o2)) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 比较两个对象属性值是否相同 相同true，不相同false，添加了忽略属性列表
     *
     * @param obj1
     * @param obj2
     * @param ignoreAttribute 忽略属性列表
     * @return
     */
    public static boolean compareObjectAttribute(Object obj1, Object obj2, List<String> ignoreAttribute) {
        try {
            if (obj1.getClass() == obj2.getClass()) {
                Class clazz = obj1.getClass();
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    String name = pd.getName();
                    //查看忽略列表，若属性值在忽略列表中，跳过检查
                    if (ignoreAttribute != null && ignoreAttribute.contains(name)) {
                        continue;
                    }
                    Method readMethod = pd.getReadMethod();
                    Object o1 = readMethod.invoke(obj1);
                    Object o2 = readMethod.invoke(obj2);
                    if (!o1.equals(o2)) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 比较两个对象的属性值哪些不相同，并通过Map输出那些不同的属性
     *
     * @param obj1
     * @param obj2
     * @param ignoreAttribute
     * @return
     */
    public static Map<String, List<Object>> returnStrCompareAttribute(Object obj1, Object obj2, List<String> ignoreAttribute) {
        try {
            Map<String, List<Object>> map = new HashMap<String, List<Object>>();
            if (obj1.getClass() == obj2.getClass()) {
                Class clazz = obj1.getClass();
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    String name = pd.getName();
                    if (ignoreAttribute != null && ignoreAttribute.contains(name)) {
                        continue;
                    }
                    Method readMethod = pd.getReadMethod();
                    Object o1 = readMethod.invoke(obj1);
                    Object o2 = readMethod.invoke(obj2);
                    if (o1 instanceof Timestamp) {
                        o1 = new Date(((Timestamp) o1).getTime());
                    }
                    if (o2 instanceof Timestamp) {
                        o2 = new Date(((Timestamp) o2).getTime());
                    }
                    if (o1 == null && o2 == null) {
                        continue;
                    } else if (o1 == null && o2 != null) {
                        List<Object> list = new ArrayList<Object>();
                        list.add(o1);
                        list.add(o2);
                        map.put(name, list);
                        continue;
                    }
                    if (!o1.equals(o2)) {
                        List<Object> list = new ArrayList<Object>();
                        list.add(o1);
                        list.add(o2);
                        map.put(name, list);
                    }
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检测一个类中的指定属性是否为空
     * 如果为false 则待检测的属性，有的为空
     * 否则 待检测的属性，全部都不为空
     *
     * @param obj1            待检测的类
     * @param inspectProperty 待检测的属性
     * @return
     */
    public static boolean classPropertyIsNull(Object obj1, List<String> inspectProperty) {
        try {
            Class clazz = obj1.getClass();
            PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                String name = pd.getName();
                if (inspectProperty.contains(name)) {
                    Method readMethod = pd.getReadMethod();
                    Object o1 = readMethod.invoke(obj1);
                    if (StringUtils.isEmpty(o1)) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 检测一个类中的指定属性是否为空
     *
     * @param obj1            待检测的类
     * @param inspectProperty 需要检测的列表
     * @return
     */
    public static String classPropertyIsNullReturn(Object obj1, List<String> inspectProperty) {
        try {
            Class clazz = obj1.getClass();
            PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                String name = pd.getName();
                if (inspectProperty.contains(name)) {
                    Method readMethod = pd.getReadMethod();
                    Object o1 = readMethod.invoke(obj1);
                    if (StringUtils.isEmpty(o1)) {
                        return String.valueOf(name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnumResultStatus.SUCCESS.getValue();
    }

    /**
     * 如果字符串全不为空返回true 有一个为空返回false
     *
     * @param strs 需要判断的字符串列表
     * @return
     */
    public static boolean strListIsEntity(List<String> strs) {
        for (String str : strs) {
            if (StringUtils.isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 处理文件路径
     *
     * @param filepath
     * @return
     */
    public static String filePath(String filepath) {
        String fstr = "";
        String[] strs = filepath.split("\\\\");
        for (int i = 0; i < strs.length; i++) {
            if (i != strs.length - 1) {
                fstr += strs[i] + "/";
            } else {
                fstr += strs[i];
            }
        }
        return FILEWEBPATH + fstr;
    }

    /**
     * Window路径
     *
     * @param filepath
     * @return
     */
    public static String filePathW(String filepath) {
        String fstr = "";
        String[] strs = filepath.split("\\\\");
        for (int i = 0; i < strs.length; i++) {
            if (i != strs.length - 1) {
                fstr += strs[i] + "/";
            } else {
                fstr += strs[i];
            }
        }
        return fstr;
    }

    /**
     * 删除字符串指定的前缀
     *
     * @param str    字符串
     * @param prefix 前缀字符串
     * @return
     */
    public static String getFileLocation(String str, String prefix) {
        int start = prefix.length();
        int end = str.length();
        for (int i = str.length() - 1; i > 0; i--) {
            if (str.charAt(i) == '/') {
                end = i;
                break;
            }
        }
        return FILE_UPLOAD_USELESS_LOCAL_PATH + str.substring(start, end);
    }

    /**
     * 根据文件网络位置，获取文件的名
     *
     * @param str
     * @return
     */
    public static String getFileName(String str) {
        String[] split = str.split("/");
        return split[split.length - 1];
    }

    /**
     * 截取字符串，去掉前缀值
     *
     * @param str
     * @param prefix
     * @return
     */
    public static String delStringPrefix(String str, String prefix) {
        return str.substring(prefix.length());
    }

    /**
     * 获取图片上传文件夹
     * 用户名-当前时间（年月日时分秒）-image（图片）/voice（声音）/ video（视频）
     *
     * @param userid 用户id
     * @param file   上传的文件
     * @return
     */
    public static String getFileUploadAddress(String userid, MultipartFile file) {
        StringBuilder fileAddress = new StringBuilder();
        fileAddress
                .append(FILE_LOCAL_PATH.replace("/", File.separator))
                .append(userid).append(File.separator)
                .append(cn.hutool.core.date.DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN))
                .append(File.separator);
        String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
        List<String> imageList = Arrays.asList(IMAGE_TYPE.split(","));
        List<String> voiceList = Arrays.asList(VOICE_TYPE.split(","));
        List<String> videoList = Arrays.asList(VIDEO_TYPE.split(","));
        if (imageList.contains(fileType)) {
            // 是图片类型
            fileAddress.append("image").append(File.separator);
        } else if (voiceList.contains(fileType)) {
            // 是音频类型
            fileAddress.append("voice").append(File.separator);
        } else if (videoList.contains(fileType)) {
            // 是视频类型
            fileAddress.append("video").append(File.separator);
        } else {
            // 上传的未知类型
            return EnumResultStatus.FAIL.getValue();
        }
        return fileAddress.toString();
    }

    /**
     * 获取上传地址
     *
     * @param userid 用户id
     * @return
     */
    public static String getFileUploadAddress(String userid) {
        StringBuilder fileAddress = new StringBuilder();
        fileAddress
                .append(FILE_LOCAL_PATH.replace("/", File.separator))
                .append(userid).append(File.separator)
                .append(cn.hutool.core.date.DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN))
                .append(File.separator);
        return fileAddress.toString();
    }

    /**
     * 根据文件列表删除全部文件信息
     *
     * @param files 文件列表
     * @return
     */
    public static void delFileList(List<String> files) {
        // 文件列表为空删除列表
        if (!files.isEmpty()) {
            for (String file : files) {
                // 如果文件为空
                if (StringUtils.isEmpty(file)) {
                    continue;
                } else {
                    FtpUtil.delFile(file);
                }
            }
        }
    }

    /**
     * 获取图片上传的文件名
     * 当前时间（年月日） + _ + uuid + 文件后缀名
     *
     * @param file
     * @return
     */
    public static String getFileUploadName(MultipartFile file) {
        StringBuffer fileAddress = new StringBuffer();
        fileAddress
                .append(DateUtil.format(new Date(), "yyyyMMddHHmmss"))
                .append("_")
                .append(UUID.randomUUID().toString().replace("-", "").toLowerCase())
                .append(".")
                .append(FilenameUtils.getExtension(file.getOriginalFilename()));
        return fileAddress.toString();
    }

    /**
     * 获取文件名
     *
     * @param suffix 文件后缀
     * @return
     */
    public static String getFileUploadName(String suffix) {
        StringBuffer fileAddress = new StringBuffer();
        fileAddress
                .append(DateUtil.format(new Date(), "yyyyMMddHHmmss"))
                .append("_")
                .append(UUID.randomUUID().toString().replace("-", "").toLowerCase())
                .append(".")
                .append(suffix);
        return fileAddress.toString();
    }

    /**
     * 判断字符串是否是Base64编码
     *
     * @param str 需要判断的字符串
     * @return
     */
    public static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    /**
     * 将列表转换为？分隔的字符串
     *
     * @param list      列表
     * @param separator 分隔的符号
     * @return
     */
    public static String listToString(List list, char separator) {
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 判断字符串行数是否分隔
     *
     * @param text
     * @return
     */
    public static Boolean judgeTextEllipsis(String text) {
        String[] str = text.split("\n");
        if (text.length() > 200 || str.length > 6) {
            return true;
        }
        return false;
    }


    /**
     * @Title: getRandomStrByNum
     * @Description: 获取不同位数的随机字符串
     * @Author: lxt
     * @param: factor
     * @Date: 2019-02-13 15:25
     * @return: java.lang.String
     * @throws:
     */
    public static String getRandomStrByNum(int factor) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < factor; i++) {
            sb.append(CHAR_STR.charAt(random.nextInt(36)));
        }
        return sb.toString();
    }

    /**
     * @Title: getRandomByNum
     * @Description: 获取指定位数的随机数字
     * @Author: lxt
     * @param: factor 位数
     * @Date: 2019-02-18 14:38
     * @return: int
     * @throws:
     */
    public static int getRandomByNum(int factor) {
        int min = 1, max = 1;
        for (int i = 0; i < factor; i++) {
            if (i < factor - 1) {
                min *= 10;
            }
            max *= 10;
        }
        return getRandomIntInRange(min, max - 1);
    }

    /**
     * @Title: getRandomIntInRange
     * @Description: 获取指定范围的随机数
     * @Author: lxt
     * @param: min 最小值
     * @param: max 最大值
     * @Date: 2019-02-18 14:38
     * @return: int
     * @throws:
     */
    public static int getRandomIntInRange(int min, int max) {
        return new Random().ints(min, (max + 1)).limit(1).findFirst().getAsInt();
    }

    /**
     * 判断是否存在下一页
     *
     * @param allPageNumber 全部页数
     * @param pageNumber    每页的数量
     * @param page          当前是第几页
     * @return
     */
    public static boolean judgeNextPage(int allPageNumber, int pageNumber, int page) {
        // 总页数
        int allPage = allPageNumber % pageNumber == 0 ? allPageNumber / pageNumber : allPageNumber / pageNumber + 1;
        // 判断是否存在下一页
        if (page < allPage - 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 文件列表的处理
     *
     * @param newFileLists     新的文件列表
     * @param historyFileLists 历史文件列表
     * @return
     */
    public static String fileListDispose(List<String> newFileLists, List<String> historyFileLists) {
        if (newFileLists == null || newFileLists.isEmpty()) {
            // 前端传输的文件列表为空，删除全部历史文件
            if (!historyFileLists.isEmpty()) {
                for (String historyFile : historyFileLists) {
                    FtpUtil.delFile(historyFile);
                }
            }
            // 返回空字符串
            return "";
        } else {
            // 新文件列表不为空
            ArrayList<String> finImages = new ArrayList<>();
            for (String historyFile : historyFileLists) {
                if (newFileLists.contains(historyFile)) {
                    // 判断历史文件列表中是否存在新的文件,存在不删除
                    finImages.add(historyFile);
                } else {
                    if (HttpUtil.isHttps(historyFile)) {
                        // 是https开头则为不存在的历史文件，删除
                        FtpUtil.delFile(historyFile);
                    }
                }
            }
            if (finImages.isEmpty()) {
                return "";
            } else {
                return listToString(finImages, ',');
            }
        }
    }

    /**
     * 单个文件的处理
     *
     * @param newFile     新的文件地址
     * @param historyFile 历史文件地址
     * @return
     */
    public static String fileDispose(String newFile, String historyFile) {
        if (StringUtils.isEmpty(newFile)) {
            // 新的文件为空
            if (!StringUtils.isEmpty(historyFile)) {
                // 判断历史文件地址是不为空，删除文件
                FtpUtil.delFile(historyFile);
            }
            return "";
        } else {
            // 新的文件不为空
            if (HttpUtil.isHttps(newFile)) {
                // 是https开头的文件地址，文件未发生变化
                return historyFile;
            } else {
                // 不是https开头的文件地址
                if (!StringUtils.isEmpty(historyFile)) {
                    FtpUtil.delFile(historyFile);
                }
                return "";
            }
        }
    }

    /**
     * 获取用户uid
     *
     * @param id 用户的主键
     * @return
     */
    public static String getUid(int id) {
        return String.valueOf(1000 + id);
    }
}