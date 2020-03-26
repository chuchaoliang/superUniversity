package com.ccl.wx.service.impl;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.dto.CircleTodayContentDTO;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.entity.TodayContent;
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
    public int updateByPrimaryKey(TodayContent record) {
        return todayContentMapper.updateByPrimaryKey(record);
    }

    @Override
    public String saveEverydayImage(MultipartFile image, String userId, Long id) {
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(id);
        boolean flag;
        flag = "".equals(todayContent.getTodayImage());
        String imagePath = FtpUtil.uploadFile(userId, image);
        if (imagePath.equals(EnumResultStatus.FAIL.getValue())) {
            // 文件上传失败
            return EnumResultStatus.FAIL.getValue();
        }
        // 拼接字符串
        todayContentMapper.concatImage(id, imagePath, flag);
        return EnumResultStatus.SUCCESS.getValue();
    }

    @Override
    public String selectCircleThemeInfoById(Long themeId, Long circleId) {
        TodayContent todayContent = todayContentMapper.selectByPrimaryKey(themeId);
        CircleTodayContentDTO circleTodayContentDTO = new CircleTodayContentDTO();
        // 判断是否有此今日内容 一般肯定不为空
        if (StringUtils.isEmpty(todayContent)) {
            // 出错今日内容为空，出现未知错误
            return EnumResultStatus.UNKNOWN.getValue();
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
        if (todayContent == null || EnumThemeStatus.DELETE_STATUS.getValue() == todayContent.getContentStatus()) {
            return EnumResultStatus.UNKNOWN.getValue();
        }
        CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(circleId);
        if (circleInfo.getThemeSum() > 1) {
            // 圈子主题-1
            circleInfoService.updateThemeNumberByCircleId(circleId, Integer.parseInt(EnumCommon.UPDATE_SUB.getValue()));
        }
        // 设置删除主题后，更新全部主题状态为主题删除状态并且仅仅自己可见
        userDiaryService.updateDiaryStatusByThemeId(themeId.intValue(), EnumUserDiary.USER_DIARY_THEME_DELETE.getValue());
        // 主题设置为删除状态
        todayContent.setContentStatus(EnumThemeStatus.DELETE_STATUS.getValue());
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
    public String selectAllThemeByCircleIdPage(Long circleId, String userId, Integer page) {
        // 查询用户加入圈子的信息
        JoinCircle joinCircle = joinCircleService.selectByPrimaryKey(circleId, userId);
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
            circleThemeVO.setSignInSuccess(false);
            if (!StringUtils.isEmpty(joinCircle.getThemeId())) {
                List<String> themes = Arrays.asList(joinCircle.getThemeId().split(","));
                if (themes.contains(String.valueOf(todayContent.getId()))) {
                    // 用户今天此主题已经打卡
                    circleThemeVO.setSignInSuccess(true);
                }
            }
            circleThemeVO.setCircleMaster(false);
            // 判断是否为圈主
            if (userId.equals(circleInfo.getCircleUserid())) {
                circleThemeVO.setCircleMaster(true);
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
        return JSON.toJSONStringWithDateFormat(themeList, DatePattern.CHINESE_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public String selectAllThemeByCircleHome(String userId, Long circleId) {
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
        return JSON.toJSONStringWithDateFormat(circleThemeVOS, DatePattern.CHINESE_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
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
        if (voice != null) {
            TodayContent todayContent = todayContentMapper.selectByPrimaryKey(id.longValue());
            if (StringUtils.isEmpty(todayContent.getThemeVoice())) {
                String voicePath = FtpUtil.uploadFile(userId, voice);
                if (!EnumResultStatus.FAIL.getValue().equals(voicePath)) {
                    // 上传成功
                    todayContent.setThemeVoice(voicePath);
                    todayContentMapper.updateByPrimaryKeySelective(todayContent);
                } else {
                    // 上传失败
                    return EnumResultStatus.FAIL.getValue();
                }
            } else {
                return EnumResultStatus.UNKNOWN.getValue();
            }
        }
        // voice文件为空出现未知错误
        return EnumResultStatus.UNKNOWN.getValue();
    }

    @Override
    public String saveCircleThemeVideo(String userId, Integer id, MultipartFile video) {
        if (video != null) {
            TodayContent todayContent = todayContentMapper.selectByPrimaryKey(id.longValue());
            if (StringUtils.isEmpty(todayContent.getThemeVideo())) {
                String videoPath = FtpUtil.uploadFile(userId, video);
                if (!EnumResultStatus.FAIL.getValue().equals(videoPath)) {
                    // 上传成功
                    todayContent.setThemeVideo(videoPath);
                    todayContentMapper.updateByPrimaryKeySelective(todayContent);
                    return EnumResultStatus.SUCCESS.getValue();
                } else {
                    // 上传失败
                    return EnumResultStatus.FAIL.getValue();
                }
            } else {
                return EnumResultStatus.UNKNOWN.getValue();
            }
        }
        // video文件为空出现未知错误
        return EnumResultStatus.UNKNOWN.getValue();
    }
}





