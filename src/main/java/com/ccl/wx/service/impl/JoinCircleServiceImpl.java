package com.ccl.wx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.dto.UserSignSuccessDTO;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.enums.*;
import com.ccl.wx.exception.UserJoinCircleException;
import com.ccl.wx.mapper.JoinCircleMapper;
import com.ccl.wx.service.CircleInfoService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.CclDateUtil;
import com.ccl.wx.util.CclUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 褚超亮
 * @date 2020/3/7 17:19
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class JoinCircleServiceImpl implements JoinCircleService {

    @Resource
    private JoinCircleMapper joinCircleMapper;

    @Resource
    private UserDiaryService userDiaryService;

    @Resource
    private CircleInfoService circleInfoService;

    @Override
    public int deleteByPrimaryKey(Long circleId, String userId) {
        return joinCircleMapper.deleteByPrimaryKey(circleId, userId);
    }

    @Override
    public int insert(JoinCircle record) {
        return joinCircleMapper.insert(record);
    }

    @Override
    public int insertSelective(JoinCircle record) {
        return joinCircleMapper.insertSelective(record);
    }

    @Override
    public JoinCircle selectByPrimaryKey(Long circleId, String userId) {
        return joinCircleMapper.selectByPrimaryKey(circleId, userId);
    }

    @Override
    public int updateByPrimaryKeySelective(JoinCircle record) {
        return joinCircleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByCircleIdAndUserSignStatus(Long circleId, Integer userSignStatus) {
        return joinCircleMapper.updateByCircleIdAndUserSignStatus(circleId, userSignStatus);
    }

    @Override
    public int updateByPrimaryKey(JoinCircle record) {
        return joinCircleMapper.updateByPrimaryKey(record);
    }

    @Override
    public int concatCircleTheme(Long circleId, String userId, String themeId, Boolean flag) {
        return joinCircleMapper.concatCircleTheme(circleId, userId, themeId, flag);
    }

    @Override
    public int countByCircleIdAndUserStatus(Long circleId, Integer userStatus) {
        return joinCircleMapper.countByCircleIdAndUserStatus(circleId, userStatus);
    }

    @Override
    public List<Map> getUserVitalityRanking(Long circleId, Integer start, Integer pageNumber) {
        return joinCircleMapper.getUserVitalityRanking(circleId, start, pageNumber);
    }

    @Override
    public List<Map> getUserSignInRanking(Long circleId, Integer start, Integer pageNumber) {
        return joinCircleMapper.getUserSignInRanking(circleId, start, pageNumber);
    }

    @Override
    public List<Map> getUserVitalityInfo(Long circleId, String userId) {
        return joinCircleMapper.getUserVitalityInfo(circleId, userId);
    }

    @Override
    public List<Map> getUserSignInInfo(Long circleId, String userId) {
        return joinCircleMapper.getUserSignInInfo(circleId, userId);
    }

    @Override
    public List<JoinCircle> selectUserIdByUserPermission(Integer circleId, List<Integer> userPermission) {
        return joinCircleMapper.selectUserIdByUserPermission(circleId, userPermission);
    }

    @Override
    public Boolean judgeUserIsCircleManage(Integer circleId, List<Integer> userPermission, String userId) {
        List<JoinCircle> joinCircles = joinCircleMapper.selectUserIdByUserPermission(circleId, userPermission);
        List<String> userIds = joinCircles.stream().map(JoinCircle::getUserId).collect(Collectors.toList());
        if (userIds.contains(userId)) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean judgeUserInCircle(Integer circleId, String userId) {
        List<String> userIds = joinCircleMapper.selectUserIdByCircleId(Long.valueOf(circleId));
        JoinCircle circleInfo = joinCircleMapper.selectByPrimaryKey(Long.valueOf(circleId), userId);
        if (userIds.contains(userId) && circleInfo.getUserStatus().equals(EnumUserCircle.USER_NORMAL_STATUS.getValue())) {
            return true;
        }
        return false;
    }

    @Override
    public List<JoinCircle> selectAllByUserIdAndUserPermission(String userId, List<Integer> userPermission) {
        return joinCircleMapper.selectAllByUserIdAndUserPermission(userId, userPermission);
    }

    @Override
    public Long sumUserVitalityByCircleIdAndUserStatus(Long circleId, Integer userStatus) {
        return joinCircleMapper.sumUserVitalityByCircleIdAndUserStatus(circleId, userStatus);
    }

    /**
     * @param circleId   圈子id
     * @param userId     用户id
     * @param start      开始数据
     * @param pageNumber 一共多少数据
     * @return
     */
    @Override
    public List<Map> getUserVitalityRankingInfo(Long circleId, String userId, Integer start, Integer pageNumber) {
        List<Map> userVitalityRankingInfo = joinCircleMapper.getUserVitalityRankingInfo(circleId, userId, start, pageNumber);
        if (userVitalityRankingInfo.isEmpty()) {
            // 用户不在前 pageNumber 名词内
            List<Map> userVitalityInfo = getUserVitalityInfo(circleId, userId);
            if (userVitalityInfo.size() != 1) {
                // 加入圈子异常
                throw new UserJoinCircleException("用户不在这个圈子中，或者其它异常！");
            } else {
                Map userInfo = userVitalityInfo.get(0);
                userInfo.put("rowNum", "9999");
                return userVitalityInfo;
            }
        }
        return userVitalityRankingInfo;
    }

    @Override
    public List<Map> getUserSignInRankingInfo(Long circleId, String userId, Integer start, Integer pageNumber) {
        List<Map> userSignInRankingInfo = joinCircleMapper.getUserSignInRankingInfo(circleId, userId, start, pageNumber);
        if (userSignInRankingInfo.isEmpty()) {
            // 用户不存在当前名次内
            List<Map> userSignInInfo = getUserSignInInfo(circleId, userId);
            if (userSignInInfo.size() != 1) {
                throw new UserJoinCircleException("用户不在这个圈子中，或其他异常！");
            } else {
                Map userInfo = userSignInInfo.get(0);
                userInfo.put("rowNum", "9999");
                return userSignInInfo;
            }
        }
        return userSignInRankingInfo;
    }

    @Override
    public String getUserVitalityRankingData(Long circleId, Integer page) {
        // 获取加入圈子中的人数
        int userAmount = countByCircleIdAndUserStatus(circleId, EnumUserCircle.USER_NORMAL_STATUS.getValue());
        // 判断是否存在下一页
        boolean judgeNextPage = CclUtil.judgeNextPage(userAmount, EnumPage.PAGE_NUMBER.getValue(), page);
        // 获取用户活跃度排行数据
        List<Map> userVitalityRanking = getUserVitalityRanking(circleId, page * EnumPage.PAGE_NUMBER.getValue(), EnumPage.PAGE_NUMBER.getValue());
        // 定义返回数据列表
        ArrayList<Object> userVitalityData = new ArrayList<>();
        userVitalityData.add(userVitalityRanking);
        userVitalityData.add(judgeNextPage);
        return JSON.toJSONString(userVitalityData);
    }

    @Override
    public String getUserSignInRankingData(Long circleId, Integer page) {
        // 获取加入圈子中的人数
        int userAmount = countByCircleIdAndUserStatus(circleId, page);
        // 判断是否存在下一页
        boolean judgeNextPage = CclUtil.judgeNextPage(userAmount, EnumPage.PAGE_NUMBER.getValue(), page);
        List<Map> userSignInRanking = getUserSignInRanking(circleId, page * EnumPage.PAGE_NUMBER.getValue(), EnumPage.PAGE_NUMBER.getValue());
        // 定义返回数据列表
        ArrayList<Object> userSignInData = new ArrayList<>();
        userSignInData.add(userSignInRanking);
        userSignInData.add(judgeNextPage);
        return JSON.toJSONString(userSignInData);
    }

    @Override
    public String getCircleSignInInfo(Long circleId, Date date) {
        // 获取圈子总人数
        int circleNumber = countByCircleIdAndUserStatus(circleId, EnumUserCircle.USER_NORMAL_STATUS.getValue());
        // 获取打卡人数
        int signInSuccessNumber = userDiaryService.countThemeUserNumberByDate(circleId, date);
        // 获取未打卡人数
        int signInFailNumber = Math.max(Math.subtractExact(circleNumber, signInSuccessNumber), 0);
        HashMap<String, Integer> hashMap = new HashMap<>(2);
        hashMap.put("success", signInSuccessNumber);
        hashMap.put("fail", signInFailNumber);
        return JSON.toJSONString(hashMap);
    }

    @Override
    public String getUserSignStatisticsSuccessInfo(Long circleId, String userId, Date date, Integer page) {
        // 格式化时间
        String formatDate = DateUtil.format(date, DatePattern.PURE_DATE_PATTERN);
        int pageNumber = EnumPage.PAGE_NUMBER.getValue();
        List<Map> userSuccessSignInByDate = joinCircleMapper.getUserSuccessSignInByDate(circleId, formatDate, page * pageNumber, pageNumber);
        ArrayList<UserSignSuccessDTO> userSignSuccessDTOS = new ArrayList<>();
        for (Map userSuccessInfo : userSuccessSignInByDate) {
            // 将Map中的数据复制到Bean中
            UserSignSuccessDTO userSignSuccessDTO = BeanUtil.fillBeanWithMap(userSuccessInfo, new UserSignSuccessDTO(), false);
            JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, userSignSuccessDTO.getUserId());
            if (joinCircle == null) {
                throw new UserJoinCircleException("此用户不在此圈子中-->" + userSignSuccessDTO.getUserId());
            } else {
                if (joinCircle.getUserStatus() != EnumUserCircle.USER_NORMAL_STATUS.getValue()) {
                    continue;
                }
            }
            // 设置用户打卡天数
            userSignSuccessDTO.setUserSigninDay(joinCircle.getUserSigninDay().intValue());
            Date diaryCreateTime = userSignSuccessDTO.getDiaryCreatetime();
            if (DateUtil.betweenDay(date, new Date(), true) == 0) {
                // 是今天
                userSignSuccessDTO.setFormatDiaryCreate(CclDateUtil.format(diaryCreateTime));
            } else {
                // 不是今天
                userSignSuccessDTO.setFormatDiaryCreate(CclDateUtil.formatDateHoursMinutes(diaryCreateTime));
            }
            userSignSuccessDTOS.add(userSignSuccessDTO);
        }
        // 获取某一天的打卡总人数
        int signInNumber = userDiaryService.countThemeUserNumberByDate(circleId, date);
        // 判断是否存在下一页
        boolean judgeNextPage = CclUtil.judgeNextPage(signInNumber, pageNumber, page);
        List<Object> returnList = new ArrayList<>();
        returnList.add(userSignSuccessDTOS);
        returnList.add(judgeNextPage);
        return JSON.toJSONStringWithDateFormat(returnList, DatePattern.NORM_DATETIME_PATTERN, SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public String getUserSignStatisticsFailInfo(Long circleId, String userId, Date date, Integer page) {
        // 格式化时间
        String formatDate = DateUtil.format(date, DatePattern.PURE_DATE_PATTERN);
        int pageNumber = EnumPage.PAGE_NUMBER.getValue();
        // 获取未打卡用户数据
        List<Map> userFailSignInByDate = joinCircleMapper.getUserFailSignInByDate(circleId, formatDate, pageNumber * page, pageNumber);
        // 获取今天的打卡人数
        int signInNumber = userDiaryService.countThemeUserNumberByDate(circleId, date);
        // 获取圈子总人数
        Integer circleNumber = joinCircleMapper.countByCircleIdAndUserStatus(circleId, EnumUserCircle.USER_NORMAL_STATUS.getValue());
        // 判断是否存在下一页
        boolean judgeNextPage = CclUtil.judgeNextPage(circleNumber - signInNumber, pageNumber, page);
        List<Object> returnList = new ArrayList<>();
        returnList.add(userFailSignInByDate);
        returnList.add(judgeNextPage);
        return JSON.toJSONString(returnList);
    }

    @Override
    public Boolean checkUserSignInStatus(Long circleId, String userId) {
        JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, userId);
        // 检测用户是否完成全部主题的打卡
        if (joinCircle == null || !joinCircle.getUserStatus().equals(EnumUserCircle.USER_NORMAL_STATUS.getValue())
                || joinCircle.getUserSignStatus().equals(EnumUserClockIn.USER_ALL_CLOCK_IN_SUCCESS.getValue())) {
            return false;
        }
        return true;
    }

    @Override
    public String joinCircle(Long circleId, String userId) {
        // 根据圈子id查询圈子数据
        CircleInfo circle = circleInfoService.selectByPrimaryKey(circleId);
        JoinCircle joinCircle = new JoinCircle();
        // 设置圈子id
        joinCircle.setCircleId(circleId);
        // 设置用户id
        joinCircle.setUserId(userId);
        JoinCircle circleUser = joinCircleMapper.selectByPrimaryKey(circleId, userId);
        // 加入圈子为空（用户曾经未加入过此圈子） || 用户状态不等于正常状态 && 圈子设置不等于需要管理员同意才能加入！
        boolean judgeJoin = circleUser == null || (!circleUser.getUserStatus().equals(EnumUserCircle.USER_NORMAL_STATUS.getValue()) &&
                !circle.getCircleSet().equals(EnumCircle.AGREE_JOIN.getValue()));
        if (judgeJoin) {
            // 圈子中人数 +1
            circleInfoService.updateCircleMemberByCircleId(circleId, 1);
        }
        if (circleUser != null) {
            // 用户曾经加入过圈子，TODO 这里可以设置用户是否可以加入被淘汰的圈子，或者其他的限制
            if (circle.getCircleSet().equals(EnumCircle.AGREE_JOIN.getValue()) &&
                    !circleUser.getUserStatus().equals(EnumUserCircle.USER_NORMAL_STATUS.getValue())) {
                joinCircle.setUserStatus(EnumUserCircle.USER_AWAIT_STATUS.getValue());
            } else {
                joinCircle.setUserStatus(EnumUserCircle.USER_NORMAL_STATUS.getValue());
            }
            joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
            return JSON.toJSONString(joinCircle);
        } else {
            // 查看是否为圈主或者圈子成员
            if (circle.getCircleUserid().equals(userId)) {
                // 圈主
                joinCircle.setUserPermission(EnumUserPermission.MASTER_USER.getValue());
            } else {
                // 查看圈子状态
                if (circle.getCircleSet().equals(EnumCircle.AGREE_JOIN.getValue())) {
                    // 设置用户为等待加入状态
                    joinCircle.setUserStatus(EnumUserCircle.USER_AWAIT_STATUS.getValue());
                } else {
                    // 设置用户为正常状态
                    joinCircle.setUserStatus(EnumUserCircle.USER_NORMAL_STATUS.getValue());
                }
            }
            int i = joinCircleMapper.insertSelective(joinCircle);
            if (i == 1) {
                return JSON.toJSONString(joinCircle);
            } else {
                return EnumResultStatus.FAIL.getValue();
            }
        }
    }

    @Override
    public String joinCircleByPassword(Long circleId, String userId, String password) {
        CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(circleId);
        // 圈子密码不存在
        String circlePassword = circleInfo.getCirclePassword();
        if (StringUtils.isEmpty(circlePassword)) {
            return EnumResultStatus.FAIL.getValue();
        } else {
            if (password.equals(circlePassword)) {
                // 密码正确，加入圈子
                String result = joinCircle(circleId, userId);
                if (!EnumResultStatus.FAIL.getValue().equals(result)) {
                    // 加入成功
                    return result;
                } else {
                    return EnumResultStatus.FAIL.getValue();
                }
            } else {
                return EnumResultStatus.FAIL.getValue();
            }
        }
    }

    @Override
    public String exitCircle(Long circleId, String userId) {
        // 退出圈子，圈子人数-1
        circleInfoService.updateCircleMemberByCircleId(circleId, -1);
        JoinCircle circleInfo = joinCircleMapper.selectByPrimaryKey(circleId, userId);
        circleInfo.setExitTime(new Date());
        circleInfo.setUserStatus(EnumUserCircle.USER_EXIT_STATUS.getValue());
        System.out.println(circleInfo);
        joinCircleMapper.updateByPrimaryKeySelective(circleInfo);
        return "success";
    }

    @Override
    public String selectUserJoinCircle(String userId, Integer page) {
        JoinCircle joinCircle = new JoinCircle();
        joinCircle.setUserId(userId);
        joinCircle.setUserStatus(EnumUserCircle.USER_NORMAL_STATUS.getValue());
        // 查询条件为用户id和用户状态
        List<Long> circleIds = joinCircleMapper.selectByAll(joinCircle).stream().map(JoinCircle::getCircleId).collect(Collectors.toList());
        List<CircleInfo> circleInfos = new ArrayList<>();
        for (Long circleId : circleIds) {
            CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(circleId);
            circleInfos.add(circleInfo);
        }
        return circleInfoService.selectAdornCircle(circleInfos, userId, circleInfos.size(), page);
    }

    @Override
    public String selectUserFoundCircle(String userId, Integer page) {
        CircleInfo circleInfo = new CircleInfo();
        circleInfo.setCircleUserid(userId);
        List<CircleInfo> circleInfos = circleInfoService.selectByAll(circleInfo);
        return circleInfoService.selectAdornCircle(circleInfos, userId, circleInfos.size(), page);
    }
}
