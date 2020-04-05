package com.ccl.wx.service.impl;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.dto.CircleInfoDTO;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.enums.EnumUserCircle;
import com.ccl.wx.enums.EnumUserDiary;
import com.ccl.wx.enums.EnumUserPermission;
import com.ccl.wx.mapper.CircleInfoMapper;
import com.ccl.wx.properties.DefaultProperties;
import com.ccl.wx.service.CircleInfoService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.TodayContentService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.FtpUtil;
import com.ccl.wx.vo.CircleIndexVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public int updateThemeNumberByCircleId(Long circleId, Integer value) {
        return circleInfoMapper.updateThemeNumberByCircleId(circleId, value);
    }

    @Override
    public List<CircleInfo> selectAllLikeAndCircleName(String likeCircleName) {
        return circleInfoMapper.selectAllLikeAndCircleName(likeCircleName);
    }

    @Override
    public List<String> selectAllCircleName() {
        return circleInfoMapper.selectAllCircleName();
    }

    @Override
    public List<CircleInfo> findAllByCircleLocation(int tid) {
        return circleInfoMapper.findAllByCircleLocation(tid);
    }

    @Override
    public List<CircleInfo> selectAllByCircleNameLikeAndCircleLocation(String keyword, int tid) {
        return circleInfoMapper.selectAllByCircleNameLikeAndCircleLocation(keyword, tid);
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
        ArrayList<Integer> userPermission = new ArrayList<>();
        userPermission.add(EnumUserPermission.ADMIN_USER.getValue());
        userPermission.add(EnumUserPermission.MASTER_USER.getValue());
        Boolean userMaster = joinCircleService.judgeUserIsCircleManage(circleId, userPermission, userId);
        // 获取圈子中的总人数
        Integer sumMember = joinCircleService.countByCircleIdAndUserStatus(Long.valueOf(circleId), EnumUserCircle.USER_NORMAL_STATUS.getValue());
        // 设置圈子中总人数
        circleInfoDTO.setCircleMember(sumMember);
        // 设置用户在此圈子中的状态
        circleInfoDTO.setUserJoin(userJoin);
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
            CircleInfo finCircleInfo = new CircleInfo();
            BeanUtils.copyProperties(circleInfo, finCircleInfo);
            finCircleInfo.setCircleCreatetime(new Date());
            finCircleInfo.setCircleHimage(defaultProperties.getDefaultImage());
            finCircleInfo.setCircleLocation(circleInfo.getCircleLocation() + 1);

            // 如果前端
            if (StringUtils.isEmpty(circleInfo.getCircleHimage())) {
                circleInfo.setCircleHimage(defaultProperties.getDefaultImage());
            } else {
                String imagePath = FtpUtil.uploadFile(circleInfo.getCircleUserid(), image);
            }

            int insert = circleInfoMapper.insertSelective(finCircleInfo);
            if (insert == 1) {
                // 插入成功返回圈子信息
                return JSON.toJSONString(circleInfo);
            } else {
                // 返回值 1 失败
                return EnumResultStatus.UNKNOWN.getValue();
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
}

