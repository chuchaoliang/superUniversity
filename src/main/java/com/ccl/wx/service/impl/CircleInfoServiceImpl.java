package com.ccl.wx.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.common.comparator.CircleInfoComparator;
import com.ccl.wx.common.list.UserPermissionList;
import com.ccl.wx.dto.CircleInfoDTO;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.enums.*;
import com.ccl.wx.mapper.CircleInfoMapper;
import com.ccl.wx.config.properties.DefaultProperties;
import com.ccl.wx.service.*;
import com.ccl.wx.util.CclUtil;
import com.ccl.wx.util.FtpUtil;
import com.ccl.wx.vo.CircleIndexVO;
import com.ccl.wx.vo.CircleInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 褚超亮
 * @date 2020/3/6 14:22
 */

@Service
public class CircleInfoServiceImpl implements CircleInfoService {

    @Resource
    private CircleInfoMapper circleInfoMapper;

    @Resource
    private JoinCircleService joinCircleService;

    @Resource
    private UserDiaryService userDiaryService;

    @Resource
    private TodayContentService todayContentService;

    @Resource
    private DefaultProperties defaultProperties;

    @Resource
    private CircleIntroService circleIntroService;

    @Override
    public int deleteByPrimaryKey(Long circleId) {
        return circleInfoMapper.deleteByPrimaryKey(circleId);
    }

    @Override
    public int insert(CircleInfo record) {
        return circleInfoMapper.insert(record);
    }

    @Override
    public int insertSelective(CircleInfo record) {
        return circleInfoMapper.insertSelective(record);
    }

    @Override
    public CircleInfo selectByPrimaryKey(Long circleId) {
        return circleInfoMapper.selectByPrimaryKey(circleId);
    }

    @Override
    public int updateByPrimaryKeySelective(CircleInfo record) {
        return circleInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(CircleInfo record) {
        return circleInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public String getCircleIndexAllContent(String userId, Integer circleId) {
        CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(Long.valueOf(circleId));
        // 创建circleInfoDTO对象
        CircleInfoDTO circleInfoDTO = new CircleInfoDTO();
        BeanUtils.copyProperties(circleInfo, circleInfoDTO);
        // 判断用户是否加入圈子
        Boolean userJoin = joinCircleService.judgeUserInCircle(circleId, userId);
        // 判断用户是否为圈或者圈子管理员
        Boolean userMaster = joinCircleService.judgeUserIsCircleManage(circleId, UserPermissionList.circleAdmin(), userId);
        // 获取圈子中的总人数
        Integer sumMember = joinCircleService.countByCircleIdAndUserStatus(Long.valueOf(circleId), EnumUserCircle.USER_NORMAL_STATUS.getValue());
        // 设置圈子中总人数
        circleInfoDTO.setCircleMember(sumMember);
        // 设置用户在此圈子中的状态
        circleInfoDTO.setUserJoin(userJoin);
        // 设置圈子是否可以直接加入
        circleInfoDTO.setApplyJoin(circleInfo.getCircleSet().equals(EnumCircle.AGREE_JOIN.getValue()));
        // 设置是否为圈主
        circleInfoDTO.setUserMaster(userMaster);
        // 设置圈子日志总数
        ArrayList<Integer> diaryStatus = new ArrayList<>();
        diaryStatus.add(EnumUserDiary.USER_DIARY_NORMAL.getValue());
        diaryStatus.add(EnumUserDiary.USER_DIARY_PERMISSION.getValue());
        int diaryNumber = userDiaryService.selectAllByCircleIdAndDiaryStatus(circleId.longValue(), diaryStatus).size();
        circleInfoDTO.setDiaryNumber(diaryNumber);
        // 设置圈子总活跃度
        circleInfoDTO.setCircleVitality(joinCircleService.sumUserVitalityByCircleIdAndUserStatus(circleId.longValue(), EnumUserCircle.USER_NORMAL_STATUS.getValue()));
        // 设置圈子公告
        String circleTask = circleInfo.getCircleTask();
        if (StringUtils.isEmpty(circleTask)) {
            // 圈子不存在公告
            circleInfoDTO.setCircleNotice(false);
        } else {
            circleInfoDTO.setCircleNotice(true);
            circleInfoDTO.setCircleNoticeContent(circleTask);
        }
        CircleIndexVO circleIndexVO = new CircleIndexVO();
        BeanUtils.copyProperties(circleInfoDTO, circleIndexVO);
        // 设置圈子主题信息
        circleIndexVO.setCircleThemeList(todayContentService.selectAllThemeByCircleHome(userId, circleId.longValue()));
        return JSON.toJSONStringWithDateFormat(circleIndexVO, DatePattern.CHINESE_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public String fondCircle(CircleInfo circleInfo, MultipartFile image) {
        // 检测圈子名称是否重复
        if (checkCircleName(circleInfo.getCircleName())) {
            if (!StringUtils.isEmpty(circleInfo.getCircleSet()) && circleInfo.getCircleSet().equals(EnumCircle.PASSWORD_JOIN.getValue())) {
                String circlePassword = StringUtils.trimAllWhitespace(circleInfo.getCirclePassword());
                //[0-9]{6} 6位数字
                boolean password = !StringUtils.isEmpty(circlePassword) &&
                        circlePassword.length() == EnumCircle.PASSWORD_LENGTH.getValue() &&
                        Validator.isMactchRegex(PatternPool.NUMBERS, circlePassword);
                if (!password) {
                    return EnumResultStatus.FAIL.getValue();
                }
            } else {
                circleInfo.setCirclePassword(null);
            }
            // 如果前端
            if (image == null) {
                circleInfo.setCircleHimage(defaultProperties.getDefaultImage());
            } else {
                String imagePath = FtpUtil.uploadFile(circleInfo.getCircleUserid(), image);
                circleInfo.setCircleHimage(imagePath);
            }
            int insert = circleInfoMapper.insertSelective(circleInfo);
            if (insert == 1) {
                // 加入圈子
                String result = joinCircleService.joinCircle(circleInfo.getCircleId(), circleInfo.getCircleUserid(), null);
                if (EnumResultStatus.FAIL.getValue().equals(result)) {
                    return EnumResultStatus.FAIL.getValue();
                } else {
                    // 插入成功返回圈子信息
                    return JSON.toJSONString(circleInfo);
                }
            } else {
                // 返回值 1 失败
                return EnumResultStatus.FAIL.getValue();
            }
        } else {
            // 圈子名字重复
            return EnumResultStatus.FAIL.getValue();
        }
    }

    @Override
    public boolean checkCircleName(String circleName) {
        List<CircleInfo> circleInfos = circleInfoMapper.selectByCircleName(circleName);
        if (circleInfos.isEmpty()) {
            // 如果不存在
            return true;
        }
        return false;
    }

    @Override
    public String selectAdornCircle(List<CircleInfo> circles, String userId, Integer number, Integer page) {
        Integer pageNumber = EnumPage.PAGE_NUMBER.getValue();
        List<CircleInfo> disposeCircles = circles.stream().sorted(new CircleInfoComparator())
                .skip(page * pageNumber.longValue()).limit(pageNumber).collect(Collectors.toList());
        List<CircleInfoVO> circleInfoVOS = new ArrayList<>();
        for (CircleInfo circle : disposeCircles) {
            CircleInfoVO circleInfoVO = new CircleInfoVO();
            BeanUtils.copyProperties(circle, circleInfoVO);
            // 设置圈子成员
            circleInfoVO.setCircleMember(joinCircleService.countByCircleIdAndUserStatus(circle.getCircleId(), EnumUserCircle.USER_NORMAL_STATUS.getValue()));
            // 设置是否为私密圈子
            circleInfoVO.setPrivacy(circle.getCircleSet().equals(EnumCircle.PASSWORD_JOIN.getValue()));
            // 设置用户是否加入圈子
            circleInfoVO.setJoin(joinCircleService.judgeUserInCircle(circle.getCircleId().intValue(), userId));
            // 设置用户是否为圈子管理人员
            circleInfoVO.setManage(joinCircleService.judgeUserIsCircleManage(circle.getCircleId().intValue(), UserPermissionList.circleAdmin(), userId));
            circleInfoVOS.add(circleInfoVO);
        }
        // 判断是否存在下一页
        List<Object> result = new ArrayList<>();
        result.add(circleInfoVOS);
        result.add(CclUtil.judgeNextPage(number, EnumPage.PAGE_NUMBER.getValue(), page));
        return JSON.toJSONString(result);
    }

    @Override
    public String selectCircleByType(Integer type, String userId, Integer page) {
        List<CircleInfo> circles = circleInfoMapper.selectSearchCircleInfo(null, type);
        return selectAdornCircle(circles, userId, circles.size(), page);
    }

    @Override
    public String judgeUserIntoPrivacyCircle(String userId, Long circleId) {
        boolean userInCircle = joinCircleService.judgeUserInCircle(circleId.intValue(), userId);
        CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(circleId);
        if (!circleInfo.getCircleSet().equals(EnumCircle.PASSWORD_JOIN.getValue())) {
            return EnumResultStatus.SUCCESS.getValue();
        }
        if (userInCircle) {
            return EnumResultStatus.SUCCESS.getValue();
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public List<CircleInfo> selectByAll(CircleInfo circleInfo) {
        return circleInfoMapper.selectByAll(circleInfo);
    }

    @Override
    public Boolean judgeUserIsCircleMaster(String userId, Long circleId) {
        CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(circleId);
        if (circleInfo.getCircleUserid().equals(userId)) {
            return true;
        }
        return false;
    }

    @Override
    public int updateCircleData(CircleInfo circleInfo, Long circleId, Integer value) {
        return circleInfoMapper.updateCircleData(circleInfo, circleId, value);
    }

    @Override
    public String searchCircleByTypeKeyWord(String keyword, Integer type, String userId, Integer page) {
        List<CircleInfo> circleInfos = circleInfoMapper.selectSearchCircleInfo(keyword, type);
        return selectAdornCircle(circleInfos, userId, circleInfos.size(), page);
    }

    @Override
    public List<CircleInfo> selectSearchCircleInfo(String keyword, Integer type) {
        return circleInfoMapper.selectSearchCircleInfo(keyword, type);
    }

    @Override
    public String searchCircleByKeyWord(String keyword, String userId, Integer page) {
        List<CircleInfo> circleInfos = circleInfoMapper.selectSearchCircleInfo(keyword, null);
        return selectAdornCircle(circleInfos, userId, circleInfos.size(), page);
    }
}



