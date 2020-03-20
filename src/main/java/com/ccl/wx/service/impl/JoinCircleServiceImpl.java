package com.ccl.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.enums.EnumPage;
import com.ccl.wx.enums.EnumUserCircle;
import com.ccl.wx.exception.UserJoinCircleException;
import com.ccl.wx.mapper.JoinCircleMapper;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.util.CclUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 褚超亮
 * @date 2020/3/7 17:19
 */

@Service
public class JoinCircleServiceImpl implements JoinCircleService {

    @Resource
    private JoinCircleMapper joinCircleMapper;

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
    public int updateByPrimaryKey(JoinCircle record) {
        return joinCircleMapper.updateByPrimaryKey(record);
    }

    @Override
    public int concatCircleTheme(Long circleId, String userId, String themeId, Boolean flag) {
        return joinCircleMapper.concatCircleTheme(circleId, userId, themeId, flag);
    }

    @Override
    public int updateCircleSignInStatus(Long circleId, Integer signInStatus) {
        return joinCircleMapper.updateByCircleId(circleId, signInStatus);
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
}
