package com.ccl.wx.service.impl;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.dto.CircleThemeDTO;
import com.ccl.wx.dto.CircleTodayContentDTO;
import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.entity.TodayContent;
import com.ccl.wx.enums.*;
import com.ccl.wx.mapper.TodayContentMapper;
import com.ccl.wx.properties.DefaultProperties;
import com.ccl.wx.service.CircleInfoService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.TodayContentService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.FtpUtil;
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
    public int updateByPrimaryKey(TodayContent record) {
        return todayContentMapper.updateByPrimaryKey(record);
    }

    @Override
    public String saveEverydayImage(MultipartFile image, String userId, Long id) {
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(id);
        boolean flag;
        flag = "".equals(todayContent.getTodayImage());
        String imagePath = FtpUtil.uploadFile(userId, image);
        // 拼接字符串
        todayContentMapper.concatImage(id, imagePath, flag);
        return "success";
    }

    @Override
    public String getTodayContentById(Long id) {
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(id);
        CircleTodayContentDTO circleTodayContentDTO = new CircleTodayContentDTO();
        // 判断是否有此今日内容 一般肯定不为空
        if (StringUtils.isEmpty(todayContent)) {
            // 出错今日内容为空
            return "-1";
        } else {
            // 今日内容不为空
            BeanUtils.copyProperties(todayContent, circleTodayContentDTO);
            if (!StringUtils.isEmpty(todayContent.getTodayImage())) {
                List<String> images = Arrays.asList(todayContent.getTodayImage().split(","));
                circleTodayContentDTO.setTodayImages(images);
            }
            return JSON.toJSONStringWithDateFormat(circleTodayContentDTO, DatePattern.NORM_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
        }
    }

    @Override
    public String saveCircleThemeContent(TodayContent todayContent, String userId, MultipartFile image) {
        // 圈子id
        Long circleId = todayContent.getCircleId();
        // 判断圈子id 是否为空，如果为空则出现错误
        if (StringUtils.isEmpty(circleId)) {
            return EnumResultStatus.FAIL.getValue();
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
            todayContent.setHeadImage(imagePath);
        }
        // 更新圈子主题总数 + 1
        circleInfoService.updateThemeNumberByCircleId(todayContent.getCircleId(), Integer.parseInt(EnumCommon.UPDATE_ADD.getValue()));
        // 插入数据
        todayContentMapper.insertSelective(todayContent);
        // 返回插入数据后的id
        return String.valueOf(todayContent.getId());
    }

    @Override
    public String deleteCircleTheme(Long circleId, Long themeId) {
        // 查找此主题
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(themeId);
        // 要删除的主题不存在？？
        if (todayContent == null) {
            return EnumResultStatus.UNMEANING.getValue();
        }
        // 圈子主题-1
        circleInfoService.updateThemeNumberByCircleId(circleId, Integer.parseInt(EnumCommon.UPDATE_SUB.getValue()));
        // 设置删除主题后，更新全部主题状态为主题删除状态并且仅仅自己可见
        userDiaryService.updateDiaryStatusByThemeId(themeId.intValue(), EnumUserDiary.USER_DIARY_THEME_DELETE.getValue());
        // 主题设置为删除状态
        todayContent.setContentStatus(EnumThemeStatus.DELETE_STATUS.getValue());
        todayContentMapper.updateByPrimaryKeySelective(todayContent);
        return EnumResultStatus.SUCCESS.getValue();
    }

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
    public String saveThemeHeadImage(MultipartFile image, String userId, Long id) {
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(id);
        if (StringUtils.isEmpty(todayContent.getHeadImage())) {
            // 圈子主题头像为空
            String imagePath = FtpUtil.uploadFile(userId, image);
            todayContent.setHeadImage(imagePath);
            todayContentMapper.updateByPrimaryKeySelective(todayContent);
        } else {
            // 删除图片之后在更新图片
            FtpUtil.delFile(todayContent.getHeadImage());
            String imagePath = FtpUtil.uploadFile(userId, image);
            todayContent.setHeadImage(imagePath);
            todayContentMapper.updateByPrimaryKeySelective(todayContent);
        }
        return "success";
    }

    @Override
    public void addCircleThemeBrowse(String userId, Long themeId) {
        // 增加浏览量 TODO
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
            if (!StringUtils.isEmpty(headImage)) {
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
    public String selectAllThemeByCircleIdPage(Long circleId, String userId, Integer page) {
        // 查询用户加入圈子的信息 TODO
        JoinCircle joinCircle = joinCircleService.selectByPrimaryKey(circleId, userId);
        // 判断此用户今日打卡主题是否为空
        if (!StringUtils.isEmpty(joinCircle.getThemeId())) {
            // 主题列表
            List<String> themes = Arrays.asList(joinCircle.getThemeId().split(","));
            int pageNumber = EnumPage.PAGE_NUMBER.getValue();
            List<TodayContent> todayContents = todayContentMapper.selectAllByCircleIdOrderByCreateTimeDesc(circleId, page * pageNumber, pageNumber);
        }
        return null;
    }

    @Override
    public String selectAllThemeByCircleId(Long circleId, String userId) {
        return null;
    }

    @Override
    public String selectAllThemeByCircleIdDecorate(Long circleId, String userId) {
        // 查询用户加入圈子的信息
        JoinCircle joinCircle = joinCircleService.selectByPrimaryKey(circleId, userId);
        List<TodayContent> todayContents = todayContentMapper.selectAllByCircleId(circleId);
        // 获取日记的打卡列表
        List<String> themes = Arrays.asList(joinCircle.getThemeId().split(","));
        // 今日打卡主题不为空
        List<CircleThemeDTO> circleThemeDTOS = new ArrayList<>();
        TodayContent decorateTodayContent = new TodayContent();
        // 设置圈子id
        decorateTodayContent.setCircleId(circleId);
        // 设置主题id
        decorateTodayContent.setId(0L);
        decorateTodayContent.setHeadImage(defaultProperties.getDefaultImage());
        decorateTodayContent.setThemeTitle("无主题");
        todayContents.add(decorateTodayContent);
        for (TodayContent todayContent : todayContents) {
            CircleThemeDTO circleThemeDTO = new CircleThemeDTO();
            BeanUtils.copyProperties(todayContent, circleThemeDTO);
            int diaryNumber = userDiaryService.countByThemeIdAndDiaryStatus(todayContent.getId().intValue());
            // 设置此主题的日志总数
            circleThemeDTO.setDiaryNumber(diaryNumber);
            // 判断用户今天是否打卡
            circleThemeDTO.setJudgeClockIn(themes.contains(String.valueOf(todayContent.getId())));
            circleThemeDTOS.add(circleThemeDTO);
        }
        return JSON.toJSONStringWithDateFormat(circleThemeDTOS, DatePattern.CHINESE_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
    }
}





