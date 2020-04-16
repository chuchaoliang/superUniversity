package com.ccl.wx.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.common.list.DiaryStatusList;
import com.ccl.wx.common.list.UserPermissionList;
import com.ccl.wx.dto.CircleTodayContentDTO;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.entity.TodayContent;
import com.ccl.wx.entity.UserDiary;
import com.ccl.wx.enums.*;
import com.ccl.wx.mapper.TodayContentMapper;
import com.ccl.wx.properties.DefaultProperties;
import com.ccl.wx.service.CircleInfoService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.TodayContentService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.CclUtil;
import com.ccl.wx.util.FtpUtil;
import com.ccl.wx.vo.CircleThemeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 褚超亮
 * @date 2020/3/5 21:35
 */
@Slf4j
@Service
public class TodayContentServiceImpl implements TodayContentService {

    @Resource
    private CircleInfoService circleInfoService;

    @Resource
    private TodayContentMapper todayContentMapper;

    @Resource
    private JoinCircleService joinCircleService;

    @Resource
    private UserDiaryService userDiaryService;

    @Resource
    private DefaultProperties defaultProperties;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return todayContentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(TodayContent record) {
        return todayContentMapper.insert(record);
    }

    @Override
    public int insertSelective(TodayContent record) {
        return todayContentMapper.insertSelective(record);
    }

    @Override
    public TodayContent selectByPrimaryKey(Long id) {
        return todayContentMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(TodayContent record) {
        return todayContentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public TodayContent selectByCircleIdAndId(Long circleId, Long id) {
        return todayContentMapper.selectByCircleIdAndId(circleId, id);
    }

    @Override
    public int updateByPrimaryKey(TodayContent record) {
        return todayContentMapper.updateByPrimaryKey(record);
    }

    @Override
    public String saveEverydayImage(MultipartFile image, String userId, Long id) {
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(id);
        if (todayContent == null || !todayContent.getContentStatus().equals(EnumThemeStatus.USE_STATUS.getValue())) {
            return EnumResultStatus.FAIL.getValue();
        }
        boolean flag;
        flag = "".equals(todayContent.getTodayImage());
        String imagePath = FtpUtil.uploadFile(userId, image);
        if (imagePath.equals(EnumResultStatus.FAIL.getValue())) {
            // 文件上传失败
            return EnumResultStatus.FAIL.getValue();
        }
        // 拼接字符串
        todayContentMapper.concatImage(id, imagePath, flag);
        return JSON.toJSONString(imagePath);
    }

    @Override
    public String selectCircleThemeInfoById(Long themeId, Long circleId) {
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(themeId);
        CircleTodayContentDTO circleTodayContentDTO = new CircleTodayContentDTO();
        // 判断是否有此今日内容 一般肯定不为空
        if (todayContent == null || todayContent.getContentStatus().equals(EnumThemeStatus.DELETE_STATUS.getValue())) {
            // 此主题被删除
            return EnumResultStatus.FAIL.getValue();
        } else {
            // 今日内容不为空
            BeanUtils.copyProperties(todayContent, circleTodayContentDTO);
            if (!StringUtils.isEmpty(todayContent.getTodayImage())) {
                List<String> images = Arrays.asList(todayContent.getTodayImage().split(","));
                circleTodayContentDTO.setTodayImages(images);
            }
            // 根据主题id查询此主题存在的日志数量
            int diaryNumber = userDiaryService.countByThemeIdAndDiaryStatus(todayContent.getId().intValue(), circleId.intValue());
            circleTodayContentDTO.setDiaryNumber(diaryNumber);
            return JSON.toJSONStringWithDateFormat(circleTodayContentDTO, DatePattern.CHINESE_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
        }
    }

    @Override
    public String saveCircleThemeContent(TodayContent todayContent, String userId, MultipartFile image) {
        // 圈子id
        Long circleId = todayContent.getCircleId();
        // 判断圈子id 是否为空，如果为空则出现错误
        String fail = EnumResultStatus.FAIL.getValue();
        if (StringUtils.isEmpty(circleId)) {
            return fail;
        }
        // 设置创建时间
        todayContent.setCreateTime(new Date());
        // 设置此圈子的完成全部主题的用户设置为部分打卡
        joinCircleService.updateByCircleIdAndUserSignStatus(circleId, EnumUserClockIn.USER_CLOCK_IN_SUCCESS.getValue());
        if (image == null) {
            // 前端传输来的图片是否为空，设置为默认图片
            todayContent.setHeadImage(defaultProperties.getDefaultImage());
        } else {
            // 上传文件
            String imagePath = FtpUtil.uploadFile(userId, image);
            if (imagePath.equals(fail)) {
                return fail;
            }
            todayContent.setHeadImage(imagePath);
        }
        // 更新圈子主题总数 + 1
        CircleInfo circleInfo = new CircleInfo();
        circleInfo.setThemeSum(0);
        circleInfoService.updateCircleData(circleInfo, circleId, EnumCommon.UPDATE_ADD.getData());
        // 插入数据
        int i = todayContentMapper.insertSelective(todayContent);
        if (i == 1) {
            return JSON.toJSONStringWithDateFormat(todayContent, DatePattern.NORM_DATETIME_MINUTE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
        } else {
            return fail;
        }
    }

    @Override
    public String deleteCircleTheme(Long circleId, Long themeId) {
        // 查找此主题
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(themeId);
        // 要删除的主题不存在？？
        if (todayContent == null || EnumThemeStatus.DELETE_STATUS.getValue() == todayContent.getContentStatus()) {
            return EnumResultStatus.FAIL.getValue();
        }
        CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(circleId);
        if (circleInfo.getThemeSum() > 1) {
            // 圈子主题-1
            CircleInfo circleInfoData = new CircleInfo();
            circleInfoData.setThemeSum(0);
            circleInfoService.updateCircleData(circleInfoData, circleId, EnumCommon.UPDATE_SUB.getData());
        }
        // 设置删除主题后，更新全部主题状态为主题删除状态并且仅仅自己可见
        userDiaryService.updateDiaryStatusByThemeId(themeId.intValue(), EnumUserDiary.USER_DIARY_THEME_DELETE.getValue());
        // 主题设置为删除状态
        todayContent.setContentStatus(EnumThemeStatus.DELETE_STATUS.getValue());
        todayContent.setDeleteTime(new Date());
        todayContentMapper.updateByPrimaryKeySelective(todayContent);
        return EnumResultStatus.SUCCESS.getValue();
    }

    @Deprecated
    @Override
    public String getCircleTheme(Long circleId, String sign) {
        List<TodayContent> todayContents = todayContentMapper.getAllByCircleId(circleId);
        List<CircleTodayContentDTO> circleTodayContentDTOS = new ArrayList<>();
        for (TodayContent todayContent : todayContents) {
            if (todayContent.getContentStatus().equals(EnumThemeStatus.DELETE_STATUS.getValue())) {
                continue;
            }
            CircleTodayContentDTO circleTodayContentDTO = new CircleTodayContentDTO();
            BeanUtils.copyProperties(todayContent, circleTodayContentDTO);
            // 获取每日内容图片
            if (!StringUtils.isEmpty(todayContent.getTodayImage())) {
                List<String> todayImages = Arrays.asList(todayContent.getTodayImage().split(","));
                circleTodayContentDTO.setTodayImages(todayImages);
            }
            circleTodayContentDTOS.add(circleTodayContentDTO);
        }
        if (StringUtils.isEmpty(sign)) {
            // 降序排列
            List<CircleTodayContentDTO> finCircleTodayContentDTOS = circleTodayContentDTOS.stream().sorted(Comparator.comparing(CircleTodayContentDTO::getCreateTime).reversed()).collect(Collectors.toList());
            return JSON.toJSONStringWithDateFormat(finCircleTodayContentDTOS, DatePattern.NORM_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
        } else {
            // 升序排列
            List<CircleTodayContentDTO> finCircleTodayContentDTOS = circleTodayContentDTOS.stream().sorted(Comparator.comparing(CircleTodayContentDTO::getCreateTime)).collect(Collectors.toList());
            return JSON.toJSONStringWithDateFormat(finCircleTodayContentDTOS, DatePattern.NORM_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
        }
    }

    @Override
    public void addCircleThemeBrowse(String userId, Long themeId) {
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(themeId);
        boolean flag = false;
        // 判断内容用户是否为空
        if (StringUtils.isEmpty(todayContent.getReadUser())) {
            flag = true;
        } else {
            List<String> userReads = Arrays.asList(todayContent.getReadUser().split(","));
            // 判断用户是否曾经看过此篇主题
            if (!userReads.contains(userId)) {
                todayContentMapper.concatUserRead(themeId, userId, flag);
            }
        }
    }

    @Override
    public void deleteCircleThemeFormDatabase() {
        List<TodayContent> todayContents = todayContentMapper.selectAllByContentStatus(EnumThemeStatus.DELETE_STATUS.getValue());
        log.info("删除圈子删除状态的信息总数为：" + todayContents.size());
        for (TodayContent todayContent : todayContents) {
            // 日记图片不为空
            if (!StringUtils.isEmpty(todayContent.getTodayImage())) {
                String[] images = todayContent.getTodayImage().split(",");
                for (String image : images) {
                    FtpUtil.delFile(image);
                }
            }
            // 删除此主题的头像、视频、和音频信息
            String headImage = todayContent.getHeadImage();
            // 判断圈子标题是否等于默认图片，并且判断是否为空
            if (!StringUtils.isEmpty(headImage) && !defaultProperties.getDefaultImage().equals(headImage)) {
                FtpUtil.delFile(headImage);
            }
            String themeVideo = todayContent.getThemeVideo();
            if (!StringUtils.isEmpty(themeVideo)) {
                FtpUtil.delFile(themeVideo);
            }
            String themeVoice = todayContent.getThemeVoice();
            if (!StringUtils.isEmpty(themeVoice)) {
                FtpUtil.delFile(themeVoice);
            }
            // 删除此主题
            todayContentMapper.deleteByPrimaryKey(todayContent.getId());
        }
    }

    @Override
    public int countByCircleIdAndContentStatus(Long circleId, Integer status) {
        return todayContentMapper.countByCircleIdAndContentStatus(circleId, status);
    }

    @Override
    public String selectAllThemeByCircleIdPage(Long circleId, String userId, Integer page, Boolean signIn, Date date) {
        // 查询用户加入圈子的信息
        JoinCircle joinCircle = joinCircleService.selectByPrimaryKey(circleId, userId);
        // 此用户未加入此圈子 或者被淘汰了
        if (joinCircle == null || !joinCircle.getUserStatus().equals(EnumUserCircle.USER_NORMAL_STATUS.getValue())) {
            return EnumResultStatus.FAIL.getValue();
        }
        // 判断此用户今日打卡主题是否为空
        int pageNumber = EnumPage.PAGE_NUMBER.getValue();
        List<TodayContent> todayContents = todayContentMapper.selectAllByCircleIdOrderByCreateTimeDesc(circleId, EnumThemeStatus.USE_STATUS.getValue(), page * pageNumber, pageNumber);
        CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(circleId);
        // 判断是否还有下一页
        boolean nextPage = CclUtil.judgeNextPage(circleInfo.getThemeSum(), pageNumber, page);
        // 如果没有下一页 将无主题添加进来
        if (!nextPage) {
            TodayContent todayContent = selectCircleDefaultThemeInfo(circleId);
            todayContents.add(todayContent);
        }
        List<CircleThemeVO> circleThemeVOS = new ArrayList<>();
        for (TodayContent todayContent : todayContents) {
            CircleThemeVO circleThemeVO = new CircleThemeVO();
            // 如果圈子主题是否为空
            if (StringUtils.isEmpty(todayContent.getHeadImage())) {
                todayContent.setHeadImage(defaultProperties.getDefaultThemeImage());
            }
            // 根据主题id查询此主题存在的日志数量
            int diaryNumber = userDiaryService.countByThemeIdAndDiaryStatus(todayContent.getId().intValue(), circleId.intValue());
            BeanUtils.copyProperties(todayContent, circleThemeVO);
            // 设置日志总数
            circleThemeVO.setDiaryNumber(diaryNumber);
            // 设置今天是否打卡
            if (date != null) {
                // 判断用户是否打卡此主题
                boolean themeSignIn = judgeUserThemeClockByDate(circleId, userId, date, DiaryStatusList.userCircleDiaryStatusList(), todayContent.getId().intValue());
                circleThemeVO.setSignInSuccess(themeSignIn);
            }
            circleThemeVO.setDefaultTheme(false);
            // 判断是否为默认主题
            if (defaultProperties.getDefaultThemeId().equals(todayContent.getId().intValue())) {
                circleThemeVO.setDefaultTheme(true);
            }
            circleThemeVOS.add(circleThemeVO);
        }
        List<Object> themeList = new ArrayList<>();
        themeList.add(circleThemeVOS);
        themeList.add(nextPage);
        if (!signIn) {
            // 判断是否为圈子管理人员（管理员、圈主）
            themeList.add(joinCircleService.judgeUserIsCircleManage(circleId.intValue(), UserPermissionList.circleAdmin(), userId));
        }
        return JSON.toJSONStringWithDateFormat(themeList, DatePattern.CHINESE_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public boolean judgeUserThemeClockByDate(Long circleId, String userId, Date date, List<Integer> diaryStatus, Integer themeId) {
        String formatDate = DateUtil.format(date, DatePattern.PURE_DATE_PATTERN);
        List<UserDiary> userDiaries = userDiaryService.selectByCircleIdAndUserIdAndDiaryCreatetimeAndDiaryStatus(circleId, userId, formatDate, diaryStatus, themeId);
        if (userDiaries.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public List<CircleThemeVO> selectAllThemeByCircleHome(String userId, Long circleId) {
        List<TodayContent> todayContents = todayContentMapper.selectAllByCircleIdOrderByCreateTimeDesc(circleId, EnumThemeStatus.USE_STATUS.getValue(), 0, EnumPage.CIRCLE_HOME_THEME.getValue());
        todayContents.add(selectCircleDefaultThemeInfo(circleId));
        List<CircleThemeVO> circleThemeVOS = new ArrayList<>();
        for (TodayContent todayContent : todayContents) {
            if (StringUtils.isEmpty(todayContent.getHeadImage())) {
                todayContent.setHeadImage(defaultProperties.getDefaultThemeImage());
            }
            CircleThemeVO circleThemeVO = new CircleThemeVO();
            BeanUtils.copyProperties(todayContent, circleThemeVO);
            // 根据主题id查询此主题存在的日志数量
            int diaryNumber = userDiaryService.countByThemeIdAndDiaryStatus(todayContent.getId().intValue(), circleId.intValue());
            circleThemeVO.setDiaryNumber(diaryNumber);
            circleThemeVOS.add(circleThemeVO);
        }
        return circleThemeVOS;
    }

    @Override
    public TodayContent selectCircleDefaultThemeInfo(Long circleId) {
        TodayContent todayContent = new TodayContent();
        CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(circleId);
        // 设置主题id
        todayContent.setId(defaultProperties.getDefaultThemeId().longValue());
        // 设置圈子id
        todayContent.setCircleId(circleId);
        // 设置主题标题
        todayContent.setThemeTitle(defaultProperties.getDefaultThemeTitle());
        // 设置主题创建时间
        todayContent.setCreateTime(circleInfo.getCircleCreatetime());
        // 设置主题头像
        todayContent.setHeadImage(defaultProperties.getDefaultThemeImage());
        return todayContent;
    }

    @Override
    public String saveCircleThemeVoice(String userId, Integer id, MultipartFile voice) {
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(id.longValue());
        if (todayContent == null || todayContent.getContentStatus().equals(EnumThemeStatus.DELETE_STATUS.getValue())) {
            return EnumResultStatus.FAIL.getValue();
        }
        if (StringUtils.isEmpty(todayContent.getThemeVoice())) {
            String voicePath = FtpUtil.uploadFile(userId, voice);
            if (!EnumResultStatus.FAIL.getValue().equals(voicePath)) {
                // 上传成功
                todayContent.setThemeVoice(voicePath);
                todayContentMapper.updateByPrimaryKeySelective(todayContent);
                return JSON.toJSONString(voicePath);
            } else {
                // 上传失败
                return EnumResultStatus.FAIL.getValue();
            }
        } else {
            return EnumResultStatus.FAIL.getValue();
        }
    }

    @Override
    public String saveCircleThemeVideo(String userId, Integer id, MultipartFile video) {
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(id.longValue());
        if (todayContent == null || todayContent.getContentStatus().equals(EnumThemeStatus.DELETE_STATUS.getValue())) {
            return EnumResultStatus.FAIL.getValue();
        }
        if (StringUtils.isEmpty(todayContent.getThemeVideo())) {
            String videoPath = FtpUtil.uploadFile(userId, video);
            if (!EnumResultStatus.FAIL.getValue().equals(videoPath)) {
                // 上传成功
                todayContent.setThemeVideo(videoPath);
                todayContentMapper.updateByPrimaryKeySelective(todayContent);
                return JSON.toJSONString(videoPath);
            } else {
                // 上传失败
                return EnumResultStatus.FAIL.getValue();
            }
        } else {
            return EnumResultStatus.FAIL.getValue();
        }
    }

    @Override
    public String saveThemeHeadImage(MultipartFile image, String userId, Long id) {
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(id);
        if (todayContent == null || todayContent.getContentStatus().equals(EnumThemeStatus.DELETE_STATUS.getValue())) {
            return EnumResultStatus.FAIL.getValue();
        }
        // 主题头像是否为空
        if (StringUtils.isEmpty(todayContent.getHeadImage()) || defaultProperties.getDefaultThemeImage().equals(todayContent.getHeadImage())) {
            String headImagePath = FtpUtil.uploadFile(userId, image);
            if (!EnumResultStatus.FAIL.getValue().equals(headImagePath)) {
                // 上传成功
                todayContent.setHeadImage(headImagePath);
                todayContentMapper.updateByPrimaryKeySelective(todayContent);
                return JSON.toJSONString(headImagePath);
            } else {
                // 图片上传失败
                todayContent.setHeadImage(defaultProperties.getDefaultThemeImage());
                todayContentMapper.updateByPrimaryKeySelective(todayContent);
                return EnumResultStatus.FAIL.getValue();
            }
        } else {
            // 主题头像重复上传
            return EnumResultStatus.FAIL.getValue();
        }
    }

    @Override
    public String updateCircleTodayContent(CircleTodayContentDTO circleTodayContentDTO) {
        if (StringUtils.isEmpty(circleTodayContentDTO.getId())) {
            // 此主题的id为空 理论上不存在这种情况，存在则出现错误！！
            return EnumResultStatus.FAIL.getValue();
        } else {
            // 根据id查询每日内容的数据
            TodayContent todayContent = todayContentMapper.selectByCircleIdAndId(circleTodayContentDTO.getCircleId(), circleTodayContentDTO.getId());
            // 要更新的今日内容为空
            if (todayContent == null || todayContent.getContentStatus().equals(EnumThemeStatus.DELETE_STATUS.getValue())) {
                // 未知错误
                return EnumResultStatus.FAIL.getValue();
            }
            todayContent.setTodayContent(circleTodayContentDTO.getTodayContent());
            // 设置更新时间
            todayContent.setUpdateTime(new Date());
            // 处理图片
            if (!StringUtils.isEmpty(todayContent.getTodayImage())) {
                //    图片不为空，删除之前的图片，并且更新文本内容，返回此今日内容的id，上传图片
                List<String> images = Arrays.asList(todayContent.getTodayImage().split(","));
                // 对图片的处理
                todayContent.setTodayImage(CclUtil.fileListDispose(circleTodayContentDTO.getTodayImages(), images));
            }
            // 处理标题头像
            if (!StringUtils.isEmpty(todayContent.getHeadImage())) {
                // 如果为默认主题头像
                if (defaultProperties.getDefaultThemeImage().equals(todayContent.getHeadImage())) {
                    // 判断是否前端传输是否为默认头像
                    if (!todayContent.getHeadImage().equals(circleTodayContentDTO.getHeadImage())) {
                        // 圈子头像
                        todayContent.setHeadImage("");
                    }
                } else {
                    todayContent.setHeadImage(CclUtil.fileDispose(circleTodayContentDTO.getHeadImage(), todayContent.getHeadImage()));
                }
            }
            // 处理音频文件
            if (!StringUtils.isEmpty(todayContent.getThemeVoice())) {
                todayContent.setThemeVoice(CclUtil.fileDispose(circleTodayContentDTO.getThemeVoice(), todayContent.getThemeVoice()));
            }
            // 处理视频文件
            if (!StringUtils.isEmpty(todayContent.getThemeVideo())) {
                todayContent.setThemeVideo(CclUtil.fileDispose(circleTodayContentDTO.getThemeVideo(), todayContent.getThemeVideo()));
            }
            todayContentMapper.updateByPrimaryKeySelective(todayContent);
            return JSON.toJSONStringWithDateFormat(todayContent, DatePattern.NORM_DATETIME_MINUTE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
        }
    }
}





