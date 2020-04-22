package com.ccl.wx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.common.list.UserPermissionList;
import com.ccl.wx.dto.UserSignSuccessDTO;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.enums.*;
import com.ccl.wx.exception.UserJoinCircleException;
import com.ccl.wx.mapper.JoinCircleMapper;
import com.ccl.wx.service.CircleInfoService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.service.UserInfoService;
import com.ccl.wx.util.CclDateUtil;
import com.ccl.wx.util.CclUtil;
import com.ccl.wx.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private JoinCircleService joinCircleService;

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
    public String getUserJoinCircleNickname(String userId, Long circleId) {
        JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, userId);
        if (joinCircle == null || StringUtils.isEmpty(joinCircle.getUserNickName())) {
            UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
            return userInfo.getNickname();
        } else {
            return joinCircle.getUserNickName();
        }
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
    public Boolean judgeUserIsCircleManage(Integer circleId, List<Integer> userPermission, String userId) {
        List<JoinCircle> joinCircles = joinCircleMapper.selectUserIdByUserPermission(circleId, userPermission, 0, EnumCircle.ADMIN_MAX_NUMBER.getValue());
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
                userInfo.put("nickname", joinCircleService.getUserJoinCircleNickname(String.valueOf(userInfo.get("userId")), circleId));
                return userVitalityInfo;
            }
        }
        for (Map map : userVitalityRankingInfo) {
            map.put("nickname", joinCircleService.getUserJoinCircleNickname(String.valueOf(map.get("userId")), circleId));
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
                userInfo.put("nickname", joinCircleService.getUserJoinCircleNickname(String.valueOf(userInfo.get("userId")), circleId));
                return userSignInInfo;
            }
        }
        for (Map map : userSignInRankingInfo) {
            map.put("nickname", joinCircleService.getUserJoinCircleNickname(String.valueOf(map.get("userId")), circleId));
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
        for (Map map : userVitalityRanking) {
            map.put("nickname", joinCircleService.getUserJoinCircleNickname(String.valueOf(map.get("userId")), circleId));
        }
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
        for (Map map : userSignInRanking) {
            map.put("nickname", joinCircleService.getUserJoinCircleNickname(String.valueOf(map.get("userId")), circleId));
        }
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
            userSuccessInfo.put("nickname", joinCircleService.getUserJoinCircleNickname(userId, circleId));
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
        for (Map map : userFailSignInByDate) {
            map.put("nickname", joinCircleService.getUserJoinCircleNickname(String.valueOf(map.get("userId")), circleId));
        }
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
    public String joinCircle(Long circleId, String userId, String applyReason) {
        // 根据圈子id查询圈子数据
        CircleInfo circle = circleInfoService.selectByPrimaryKey(circleId);
        JoinCircle joinCircle = new JoinCircle();
        // 设置圈子id
        joinCircle.setCircleId(circleId);
        // 设置用户id
        joinCircle.setUserId(userId);
        JoinCircle circleUser = joinCircleMapper.selectByPrimaryKey(circleId, userId);
        // 加入圈子为空（用户曾经未加入过此圈子 && 圈子设置不能为需要同意才可加入） || （用户加入过此圈子 && 用户状态不等于正常状态 && 圈子设置不等于需要管理员同意才能加入！）
        boolean judgeJoin = (circleUser == null && !circle.getCircleSet().equals(EnumCircle.AGREE_JOIN.getValue()))
                || (circleUser != null && !circleUser.getUserStatus().equals(EnumUserCircle.USER_NORMAL_STATUS.getValue())
                && !circle.getCircleSet().equals(EnumCircle.AGREE_JOIN.getValue()));
        if (judgeJoin) {
            // 圈子中人数 +1
            CircleInfo circleInfo = new CircleInfo();
            circleInfo.setCircleMember(0);
            circleInfoService.updateCircleData(circleInfo, circleId, EnumCommon.UPDATE_ADD.getData());
        }
        if (circleUser != null) {
            // 用户曾经加入过圈子，TODO 这里可以设置用户是否可以加入被淘汰的圈子，或者其他的限制
            circleUser.setJoinTime(new Date());
            circleUser.setExitTime(null);
            circleUser.setOutReason("");
            circleUser.setRefuseReason("");
            if (circle.getCircleSet().equals(EnumCircle.AGREE_JOIN.getValue())) {
                // 设置申请理由
                circleUser.setApplyReason(applyReason);
                circleUser.setUserStatus(EnumUserCircle.USER_AWAIT_STATUS.getValue());
            } else {
                circleUser.setUserStatus(EnumUserCircle.USER_NORMAL_STATUS.getValue());
            }
            joinCircleMapper.updateByPrimaryKey(circleUser);
            joinCircle.setUserStatus(circleUser.getUserStatus());
            return JSON.toJSONString(joinCircle);
        } else {
            // 查看是否为圈主或者圈子成员
            if (circle.getCircleUserid().equals(userId)) {
                // 圈主
                joinCircle.setUserPermission(EnumUserPermission.MASTER_USER.getValue());
            } else {
                // 查看圈子状态
                if (circle.getCircleSet().equals(EnumCircle.AGREE_JOIN.getValue())) {
                    // 设置申请理由
                    joinCircle.setApplyReason(applyReason);
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

    /**
     * 同意用户加入圈子申请
     *
     * @param applyUserId 申请人用户id
     * @param circleId    圈子id
     * @return
     */
    @Override
    public String agreeJoinApply(String applyUserId, Long circleId) {
        JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, applyUserId);
        if (joinCircle == null || !joinCircle.getUserStatus().equals(EnumUserCircle.USER_AWAIT_STATUS.getValue())) {
            log.error("(同意加入)操作异常异常用户-->" + applyUserId + "异常圈子id-->" + circleId);
            return EnumResultStatus.FAIL.getValue();
        } else {
            String result = normalJoinCircle(circleId, applyUserId);
            if (EnumResultStatus.FAIL.getValue().equals(result)) {
                return EnumResultStatus.FAIL.getValue();
            }
            return EnumResultStatus.SUCCESS.getValue();
        }
    }

    /**
     * 申请加入圈子
     *
     * @param circleId    圈子id
     * @param userId      用户id
     * @param applyReason 申请理由
     * @return
     * @deprecated 暂时不使用这个方法
     */
    public String applyJoinCircle(Long circleId, String userId, String applyReason) {
        JoinCircle joinCircle = new JoinCircle();
        // 设置圈子id
        joinCircle.setCircleId(circleId);
        // 设置用户id
        joinCircle.setUserId(userId);
        JoinCircle circleUser = joinCircleMapper.selectByPrimaryKey(circleId, userId);
        if (circleUser != null) {
            // 用户曾加入过此圈子
            circleUser.setJoinTime(new Date());
            circleUser.setExitTime(null);
            circleUser.setOutReason("");
            circleUser.setRefuseReason("");
            circleUser.setUserStatus(EnumUserCircle.USER_AWAIT_STATUS.getValue());
            joinCircleMapper.updateByPrimaryKey(circleUser);
            joinCircle.setUserStatus(circleUser.getUserStatus());
        } else {
            joinCircle.setUserStatus(EnumUserCircle.USER_AWAIT_STATUS.getValue());
            joinCircleMapper.updateByPrimaryKey(joinCircle);
        }
        return JSON.toJSONString(joinCircle);
    }

    /**
     * 用户正常直接加入圈子
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    public String normalJoinCircle(Long circleId, String userId) {
        JoinCircle joinCircle = new JoinCircle();
        // 设置圈子id
        joinCircle.setCircleId(circleId);
        // 设置用户id
        joinCircle.setUserId(userId);
        JoinCircle circleUser = joinCircleMapper.selectByPrimaryKey(circleId, userId);
        // 圈子中人数 +1
        CircleInfo circleInfo = new CircleInfo();
        circleInfo.setCircleMember(0);
        circleInfoService.updateCircleData(circleInfo, circleId, EnumCommon.UPDATE_ADD.getData());
        if (circleUser != null) {
            // 用户曾经加入过圈子，
            circleUser.setJoinTime(new Date());
            circleUser.setExitTime(null);
            circleUser.setOutReason("");
            circleUser.setRefuseReason("");
            circleUser.setUserStatus(EnumUserCircle.USER_NORMAL_STATUS.getValue());
            joinCircleMapper.updateByPrimaryKey(circleUser);
            joinCircle.setUserStatus(circleUser.getUserStatus());
            return JSON.toJSONString(joinCircle);
        } else {
            // 根据圈子id查询圈子数据
            CircleInfo circle = circleInfoService.selectByPrimaryKey(circleId);
            // 查看是否为圈主或者圈子成员
            if (circle.getCircleUserid().equals(userId)) {
                // 圈主
                joinCircle.setUserPermission(EnumUserPermission.MASTER_USER.getValue());
            } else {
                // 设置用户为正常状态
                joinCircle.setUserStatus(EnumUserCircle.USER_NORMAL_STATUS.getValue());
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
                String result = joinCircle(circleId, userId, null);
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
        JoinCircle circleInfo = joinCircleMapper.selectByPrimaryKey(circleId, userId);
        if (circleInfo == null || circleInfoService.judgeUserIsCircleMaster(userId, circleId)) {
            // 是圈主人失败
            return EnumResultStatus.FAIL.getValue();
        }
        // 退出圈子，圈子人数-1
        if (circleInfo.getUserStatus().equals(EnumUserCircle.USER_NORMAL_STATUS.getValue())) {
            CircleInfo circleInfoData = new CircleInfo();
            circleInfoData.setCircleMember(0);
            circleInfoService.updateCircleData(circleInfoData, circleId, EnumCommon.UPDATE_SUB.getData());
        }
        circleInfo.setExitTime(new Date());
        circleInfo.setUserStatus(EnumUserCircle.USER_EXIT_STATUS.getValue());
        int i = joinCircleMapper.updateByPrimaryKeySelective(circleInfo);
        if (i == 1) {
            return EnumResultStatus.SUCCESS.getValue();
        }
        return EnumResultStatus.FAIL.getValue();
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

    @Override
    public String getCircleNormalUser(Long circleId, Integer page) {
        JoinCircle joinCircle = new JoinCircle();
        joinCircle.setCircleId(circleId);
        joinCircle.setUserStatus(EnumUserCircle.USER_NORMAL_STATUS.getValue());
        List<JoinCircle> joinCircleList = joinCircleMapper.selectByAll(joinCircle);
        int pageNumber = EnumPage.PAGE_NUMBER.getValue();
        List<JoinCircle> joinCircles = joinCircleList.stream().sorted(Comparator.comparing(JoinCircle::getJoinTime).reversed())
                .skip(page.longValue() * pageNumber).limit(pageNumber).collect(Collectors.toList());
        return selectAdornNormalJoinCircle(joinCircles, joinCircleList.size(), page);
    }

    @Override
    public String selectAdornNormalJoinCircle(List<JoinCircle> joinCircles, Integer number, Integer page) {
        ArrayList<CircleNormalUserInfoVO> circleNormalUserInfoVOS = new ArrayList<>();
        for (JoinCircle joinCircle : joinCircles) {
            CircleNormalUserInfoVO circleNormalUserInfoVO = new CircleNormalUserInfoVO();
            BeanUtils.copyProperties(joinCircle, circleNormalUserInfoVO);
            // 查询用户信息
            UserInfo userInfo = userInfoService.selectByPrimaryKey(joinCircle.getUserId());
            if (userInfo == null) {
                log.error("出现错误，存在用户未在用户列表中！！！！");
                continue;
            } else {
                userInfo.setNickname(joinCircleService.getUserJoinCircleNickname(userInfo.getId(), joinCircle.getCircleId()));
                // 设置用户昵称
                circleNormalUserInfoVO.setNickname(userInfo.getNickname());
                // 设置用户头像
                circleNormalUserInfoVO.setAvatarurl(userInfo.getAvatarurl());
                // 设置用户性别
                circleNormalUserInfoVO.setGender(userInfo.getGender());
            }
            circleNormalUserInfoVOS.add(circleNormalUserInfoVO);
        }
        List<Object> result = new ArrayList<>();
        result.add(circleNormalUserInfoVOS);
        result.add(CclUtil.judgeNextPage(number, EnumPage.PAGE_NUMBER.getValue(), page));
        return JSON.toJSONStringWithDateFormat(result, DatePattern.NORM_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public String selectAdornJoinCircle(List<JoinCircle> joinCircles, Integer number, Integer page) {
        List<CircleUserInfoVO> circleUserInfoVOS = new ArrayList<>();
        for (JoinCircle joinCircle : joinCircles) {
            CircleUserInfoVO circleUserInfoVO = new CircleUserInfoVO();
            BeanUtils.copyProperties(joinCircle, circleUserInfoVO);
            // 查询用户信息
            UserInfo userInfo = userInfoService.selectByPrimaryKey(joinCircle.getUserId());
            if (userInfo == null) {
                log.error("出现错误，存在用户未在用户列表中！！！！");
                continue;
            } else {
                // 设置用户昵称，头像，性别
                circleUserInfoVO.setNickname(userInfo.getNickname());
                circleUserInfoVO.setAvatarurl(userInfo.getAvatarurl());
                circleUserInfoVO.setGender(userInfo.getGender());
            }
            circleUserInfoVOS.add(circleUserInfoVO);
        }
        List<Object> result = new ArrayList<>();
        result.add(circleUserInfoVOS);
        result.add(CclUtil.judgeNextPage(number, EnumPage.PAGE_NUMBER.getValue(), page));
        return JSON.toJSONStringWithDateFormat(result, DatePattern.NORM_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public String getCircleRefuseUser(Long circleId, Integer page) {
        JoinCircle joinCircle = new JoinCircle();
        joinCircle.setCircleId(circleId);
        joinCircle.setUserStatus(EnumUserCircle.USER_REFUSE_STATUS.getValue());
        List<JoinCircle> joinCircleList = joinCircleMapper.selectByAll(joinCircle);
        int pageNumber = EnumPage.PAGE_NUMBER.getValue();
        List<JoinCircle> joinCircles = joinCircleList.stream().sorted(Comparator.comparing(JoinCircle::getJoinTime).reversed())
                .skip(page.longValue() * pageNumber).limit(pageNumber).collect(Collectors.toList());
        return selectAdornJoinCircle(joinCircles, joinCircleList.size(), page);
    }

    @Override
    public String getCircleAuditUser(Long circleId, Integer page) {
        JoinCircle joinCircle = new JoinCircle();
        joinCircle.setCircleId(circleId);
        joinCircle.setUserStatus(EnumUserCircle.USER_AWAIT_STATUS.getValue());
        List<JoinCircle> joinCircleList = joinCircleMapper.selectByAll(joinCircle);
        int pageNumber = EnumPage.PAGE_NUMBER.getValue();
        List<JoinCircle> joinCircles = joinCircleList.stream().sorted(Comparator.comparing(JoinCircle::getJoinTime).reversed())
                .skip(page.longValue() * pageNumber).limit(pageNumber).collect(Collectors.toList());
        return selectAdornJoinCircle(joinCircles, joinCircleList.size(), page);
    }

    @Override
    public String refuseJoinCircle(Long circleId, String applyUserId, String refuseReason) {
        JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, applyUserId);
        if (joinCircle == null) {
            return EnumResultStatus.FAIL.getValue();
        } else {
            if (!joinCircle.getUserStatus().equals(EnumUserCircle.USER_AWAIT_STATUS.getValue())) {
                log.error("(拒绝加入)操作异常异常用户-->" + applyUserId + "异常圈子id-->" + circleId);
                return EnumResultStatus.FAIL.getValue();
            }
            // 设置拒绝理由
            joinCircle.setRefuseReason(refuseReason);
            // 设置状态
            joinCircle.setUserStatus(EnumUserCircle.USER_REFUSE_STATUS.getValue());
            // 设置拒绝时间
            joinCircle.setExitTime(new Date());
            int i = joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
            if (i != 1) {
                return EnumResultStatus.FAIL.getValue();
            } else {
                return EnumResultStatus.SUCCESS.getValue();
            }
        }
    }

    @Override
    public String outCircleUser(Long circleId, String outUserId, String userId) {
        if (outUserId.equals(userId)) {
            return EnumResultStatus.FAIL.getValue();
        } else {
            JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, outUserId);
            String fail = EnumResultStatus.FAIL.getValue();
            if (joinCircle == null || !joinCircle.getUserStatus().equals(EnumUserCircle.USER_NORMAL_STATUS.getValue())) {
                return fail;
            } else {
                // 判断是否为管理员
                boolean admin = judgeUserIsCircleManage(circleId.intValue(), UserPermissionList.circleAdmin(), userId);
                if (admin) {
                    // 查看被淘汰用户是否为管理员
                    boolean outAdmin = judgeUserIsCircleManage(circleId.intValue(), UserPermissionList.circleAdmin(), outUserId);
                    String success = EnumResultStatus.SUCCESS.getValue();
                    if (outAdmin) {
                        // 判断是否为圈主
                        boolean master = judgeUserIsCircleManage(circleId.intValue(), UserPermissionList.circleMaster(), outUserId);
                        if (master) {
                            joinCircle.setUserStatus(EnumUserCircle.USER_OUT_STATUS.getValue());
                            joinCircle.setExitTime(new Date());
                            int i = joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
                            if (i == 0) {
                                return fail;
                            } else {
                                CircleInfo circleInfo = new CircleInfo();
                                circleInfo.setCircleMember(0);
                                circleInfoService.updateCircleData(circleInfo, circleId, EnumCommon.UPDATE_SUB.getData());
                                return success;
                            }

                        } else {
                            return EnumResultStatus.UNKNOWN.getValue();
                        }
                    } else {
                        // 不是管理员
                        joinCircle.setUserStatus(EnumUserCircle.USER_OUT_STATUS.getValue());
                        joinCircle.setExitTime(new Date());
                        int i = joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
                        if (i == 0) {
                            return fail;
                        } else {
                            CircleInfo circleInfo = new CircleInfo();
                            circleInfo.setCircleMember(0);
                            circleInfoService.updateCircleData(circleInfo, circleId, EnumCommon.UPDATE_SUB.getData());
                            return success;
                        }
                    }
                } else {
                    log.error("未知异常userId-->" + userId + "========" + "circleId-->" + circleId);
                    return EnumResultStatus.UNKNOWN.getValue();
                }
            }
        }
    }

    @Override
    public String saveCircleNickname(String userId, Long circleId, String nickname) {
        JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, userId);
        if (joinCircle == null || !joinCircle.getUserStatus().equals(EnumUserCircle.USER_NORMAL_STATUS.getValue())
                || !StringUtils.isEmpty(joinCircle.getUserNickName()) || StringUtils.isEmpty(nickname)) {
            return EnumResultStatus.FAIL.getValue();
        } else {
            joinCircle.setUserNickName(nickname);
            int i = joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
            if (i == 0) {
                return EnumResultStatus.FAIL.getValue();
            } else {
                return EnumResultStatus.SUCCESS.getValue();
            }
        }
    }

    @Override
    public String updateCircleNicknameDefault(String userId, Long circleId) {
        if (judgeUserJoinCircleStatus(userId, circleId)) {
            // 状态正常
            JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, userId);
            joinCircle.setUserNickName("");
            int i = joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
            if (i == 0) {
                return EnumResultStatus.FAIL.getValue();
            } else {
                return EnumResultStatus.SUCCESS.getValue();
            }
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public String checkUserCircleNickname(Long circleId, String userId) {
        if (judgeUserJoinCircleStatus(userId, circleId)) {
            JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, userId);
            HashMap<String, Object> hashMap = new HashMap<>(2);
            String userNickName = joinCircle.getUserNickName();
            if (StringUtils.isEmpty(userNickName)) {
                // 昵称为空
                hashMap.put("nicknameIsNull", true);
            } else {
                hashMap.put("nicknameIsNull", false);
                hashMap.put("nickname", userNickName);
            }
            return JSON.toJSONString(hashMap);
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public String updateUserCircleNickname(Long circleId, String userId, String nickname) {
        if (judgeUserJoinCircleStatus(userId, circleId) && !StringUtils.isEmpty(nickname)) {
            JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, userId);
            joinCircle.setUserNickName(nickname);
            int i = joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
            if (i == 0) {
                return EnumResultStatus.FAIL.getValue();
            } else {
                return EnumResultStatus.SUCCESS.getValue();
            }
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public String getCircleAdminInfo(Long circleId) {
        List<JoinCircle> joinCircles = joinCircleMapper.selectUserIdByUserPermission(circleId.intValue(), UserPermissionList.circleAdminOutMaster(), 0, EnumCircle.ADMIN_MAX_NUMBER.getValue());
        return JSON.toJSONString(adornUserInfo(joinCircles));
    }

    @Override
    public String getCircleGeneralUserInfo(Long circleId, Integer page) {
        int pageNumber = EnumPage.PAGE_NUMBER.getValue();
        List<JoinCircle> joinCircles = joinCircleMapper.selectUserIdByUserPermission(circleId.intValue(), UserPermissionList.circleGeneral(), page * pageNumber, pageNumber);
        // 获取圈子全部普通用户总数
        Integer circleAllUserNumber = joinCircleMapper.countByCircleIdAndUserPermission(circleId, UserPermissionList.circleGeneral());
        List<UserVO> userVOS = adornUserInfo(joinCircles);
        ArrayList<Object> result = new ArrayList<>();
        result.add(userVOS);
        result.add(CclUtil.judgeNextPage(circleAllUserNumber, EnumPage.PAGE_NUMBER.getValue(), page));
        return JSON.toJSONString(result);
    }

    @Override
    public String checkAddCircleAdmin(Long circleId) {
        Integer adminNumber = joinCircleMapper.countByCircleIdAndUserPermission(circleId, UserPermissionList.circleAdminOutMaster());
        if (adminNumber >= EnumCircle.ADMIN_MAX_NUMBER.getValue()) {
            return EnumResultStatus.FAIL.getValue();
        } else {
            return EnumResultStatus.SUCCESS.getValue();
        }
    }

    @Override
    public String addCircleAdmin(Long circleId, String userId) {
        JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, userId);
        String fail = EnumResultStatus.FAIL.getValue();
        if (judgeUserJoinCircleStatus(userId, circleId) && !joinCircle.getUserPermission().equals(EnumUserPermission.MASTER_USER.getValue())) {
            joinCircle.setUserPermission(EnumUserPermission.ADMIN_USER.getValue());
            int i = joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
            if (i == 0) {
                return fail;
            }
            return EnumResultStatus.SUCCESS.getValue();
        }
        return fail;
    }

    @Override
    public String outCircleAdminInfo(Long circleId, String userId) {
        String fail = EnumResultStatus.FAIL.getValue();
        if (judgeUserJoinCircleStatus(userId, circleId)) {
            JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, userId);
            if (!joinCircle.getUserPermission().equals(EnumUserPermission.MASTER_USER.getValue())) {
                joinCircle.setUserPermission(EnumUserPermission.ORDINARY_USER.getValue());
                int i = joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
                if (i == 0) {
                    return fail;
                }
                return EnumResultStatus.SUCCESS.getValue();
            } else {
                return fail;
            }
        }
        return fail;
    }

    @Override
    public String transferCircle(Long circleId, String tUserId, String userId) {
        String fail = EnumResultStatus.FAIL.getValue();
        if (judgeUserJoinCircleStatus(userId, circleId) && judgeUserJoinCircleStatus(tUserId, circleId)) {
            JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, userId);
            if (joinCircle.getUserPermission().equals(EnumUserPermission.MASTER_USER.getValue())) {
                // 是圈主
                JoinCircle tJoinCircle = joinCircleMapper.selectByPrimaryKey(circleId, tUserId);
                // 判断要转让的用户是否为管理员
                if (tJoinCircle.getUserPermission().equals(EnumUserPermission.ADMIN_USER.getValue())) {
                    // 转让
                    tJoinCircle.setUserPermission(EnumUserPermission.MASTER_USER.getValue());
                    joinCircle.setUserPermission(EnumUserPermission.ADMIN_USER.getValue());
                    joinCircleMapper.updateByPrimaryKeySelective(tJoinCircle);
                    joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
                    return EnumResultStatus.SUCCESS.getValue();
                }
            } else {
                return fail;
            }
        }
        return fail;
    }

    @Override
    public String getRecordUserInfo(Long circleId, String userId, String tUserId) {
        String sUserId = StringUtils.isEmpty(tUserId) ? userId : tUserId;
        if (judgeUserJoinCircleStatus(sUserId, circleId)) {
            UserInfo userInfo = userInfoService.selectByPrimaryKey(sUserId);
            UserCircleRecordVO userCircleRecordVO = new UserCircleRecordVO();
            BeanUtils.copyProperties(userInfo, userCircleRecordVO);
            JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, sUserId);
            BeanUtils.copyProperties(joinCircle, userCircleRecordVO);
            return JSON.toJSONStringWithDateFormat(userCircleRecordVO, DatePattern.NORM_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
        }
        return EnumResultStatus.FAIL.getValue();
    }

    /**
     * 装饰用户信息
     *
     * @param joinCircles 用户加入圈子信息列表
     * @return
     */
    private List<UserVO> adornUserInfo(List<JoinCircle> joinCircles) {
        List<String> userIds = joinCircles.stream().map(JoinCircle::getUserId).collect(Collectors.toList());
        ArrayList<UserVO> userVOS = new ArrayList<>();
        for (String userId : userIds) {
            UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(userInfo, userVO);
            userVOS.add(userVO);
        }
        return userVOS;
    }

    /**
     * 判断用户加入圈子状态是否正常
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @return
     */
    private boolean judgeUserJoinCircleStatus(String userId, Long circleId) {
        JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleId, userId);
        if (joinCircle == null || !joinCircle.getUserStatus().equals(EnumUserCircle.USER_NORMAL_STATUS.getValue())) {
            return false;
        }
        return true;
    }
}


