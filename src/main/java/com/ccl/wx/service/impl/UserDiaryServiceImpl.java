package com.ccl.wx.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.common.list.DiaryStatusList;
import com.ccl.wx.config.properties.DefaultProperties;
import com.ccl.wx.dto.CommentDTO;
import com.ccl.wx.dto.UserDiaryDTO;
import com.ccl.wx.entity.*;
import com.ccl.wx.enums.*;
import com.ccl.wx.mapper.UserDiaryMapper;
import com.ccl.wx.service.*;
import com.ccl.wx.util.CclDateUtil;
import com.ccl.wx.util.CclUtil;
import com.ccl.wx.util.FtpUtil;
import com.ccl.wx.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 褚超亮
 * @date 2020/2/18 10:21
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class UserDiaryServiceImpl implements UserDiaryService {

    @Resource
    private UserDiaryMapper userDiaryMapper;

    @Resource
    private TodayContentService todayContentService;

    @Resource
    private JoinCircleService joinCircleService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CommentService commentService;

    @Resource
    private UserLikeService userLikeService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private DefaultProperties defaultProperties;

    @Resource
    private CircleInfoService circleInfoService;

    /**
     * 多少个小时内，用户可以再次增加浏览量
     */
    public static final Integer BROWSE_PAST_TIME = 24;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userDiaryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserDiary record) {
        return userDiaryMapper.insert(record);
    }

    @Override
    public int insertSelective(UserDiary record) {
        return userDiaryMapper.insertSelective(record);
    }

    @Override
    public UserDiary selectByPrimaryKey(Long id) {
        return userDiaryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserDiary record) {
        return userDiaryMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<Long> selectIdByDiaryStatus(Integer value) {
        return userDiaryMapper.selectIdByDiaryStatus(value);
    }

    /**
     * @param circleId 圈子id
     * @param userId   用户id
     * @param page     页数
     * @return
     */
    @Override
    public String getAllDiaryInfo(Long circleId, String userId, Integer page) {
        // 判断用户是否为圈子成员
        boolean userJoinStatus = joinCircleService.judgeUserInCircle(circleId.intValue(), userId);
        ArrayList<Integer> diaryStatus = new ArrayList<>();
        diaryStatus.add(EnumUserDiary.USER_DIARY_NORMAL.getValue());
        if (userJoinStatus) {
            // 是圈子成员
            diaryStatus.add(EnumUserDiary.USER_DIARY_PERMISSION.getValue());
        }
        // 获取日志总数,是否过滤掉不是此圈子的日志
        long diarySum = userDiaryMapper.countByCircleIdAndUserIdAndDiaryStatus(circleId, null, diaryStatus);
        int pageNumber = EnumPage.PAGE_NUMBER.getValue();
        // 用户日志信息
        List<UserDiary> userDiaries = userDiaryMapper.selectAllByCircleIdAndUserId(circleId, null, page * pageNumber, pageNumber, diaryStatus);
        return adornDiaryInfo(userDiaries, userId, diarySum, page, false);
    }

    @Override
    public String getAssignDiaryInfo(Long circleId, String userId, Integer page) {
        // 获取日志总数
        List<Integer> diaryStatus = DiaryStatusList.userCircleDiaryStatusList();
        Long diarySum = userDiaryMapper.countByCircleIdAndUserIdAndDiaryStatus(circleId, userId, diaryStatus);
        // 获取总页数
        int pageNumber = EnumPage.PAGE_NUMBER.getValue();
        // 用户日志信息
        List<UserDiary> userDiaries = userDiaryMapper.selectAllByCircleIdAndUserId(circleId, userId, page * pageNumber, pageNumber, diaryStatus);
        return adornDiaryInfo(userDiaries, userId, diarySum, page, false);
    }

    @Override
    public String getUserIndexDiaryInfo(String userId, Integer page) {
        int pageNumber = EnumPage.PAGE_NUMBER.getValue();
        List<Integer> diaryStatus = DiaryStatusList.outCircleDiaryStatusList();
        Long diarySum = userDiaryMapper.countByCircleIdAndUserIdAndDiaryStatus(null, userId, diaryStatus);
        List<UserDiary> userDiaries = userDiaryMapper.selectAllByCircleIdAndUserId(null, userId, page * pageNumber, pageNumber, diaryStatus);
        return adornDiaryInfo(userDiaries, userId, diarySum, page, true);
    }

    /**
     * @param userDiaries 日志信息
     * @param userId      访问用户id
     * @param diarySum    日志总数
     * @param page        当前页数
     * @param index       访问的是否为我的页面（true是false不是）
     * @return
     */
    public String adornDiaryInfo(List<UserDiary> userDiaries, String userId, Long diarySum, Integer page, boolean index) {
        ArrayList<CircleHomeDiaryVO> circleHomeDiaryVOS = new ArrayList<>();
        for (UserDiary userDiary : userDiaries) {
            CircleHomeDiaryVO userDiaryVO = getUserDiaryVO(userId, index, userDiary);
            circleHomeDiaryVOS.add(userDiaryVO);
        }
        ArrayList<Object> diaryList = new ArrayList<>();
        diaryList.add(circleHomeDiaryVOS);
        diaryList.add(CclUtil.judgeNextPage(diarySum.intValue(), EnumPage.PAGE_NUMBER.getValue(), page));
        return JSON.toJSONStringWithDateFormat(diaryList, DatePattern.NORM_DATE_PATTERN, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 日志对象加强方法
     *
     * @param userId    用户id
     * @param index     是否为用户主页
     * @param userDiary 日志id
     * @return
     */
    private CircleHomeDiaryVO getUserDiaryVO(String userId, boolean index, UserDiary userDiary) {
        UserDiaryDTO userDiaryDTO = new UserDiaryDTO();
        BeanUtils.copyProperties(userDiary, userDiaryDTO);
        Boolean ellipsis = CclUtil.judgeTextEllipsis(userDiary.getDiaryContent());
        userDiaryDTO.setEllipsis(ellipsis);
        if (!StringUtils.isEmpty(userDiary.getDiaryImage())) {
            userDiaryDTO.setImages(Arrays.asList(userDiary.getDiaryImage().split(",")));
        }
        // 设置日记评论和回复的总数
        userDiaryDTO.setCommentSum(commentService.getDiaryAllComment(userDiary.getId()));
        // 获取圈子id
        Long circleId = userDiary.getCircleId();
        // 查找日志点赞人，点赞人信息
        List<DiaryLikeVO> likeUserInfo = userLikeService.getAllLikeUserNickName(userId, String.valueOf(circleId), userDiary.getId());
        // 查找全部的评论
        List<CommentDTO> diaryComment = commentService.getDiaryComment(userDiary.getId(), 0, true);
        // 查找全部的点评
        List<CommentDTO> masterComment = commentService.getMasterComment(userDiary.getId());
        // 设置评论
        userDiaryDTO.setComments(diaryComment);
        // 设置点评
        userDiaryDTO.setMasterComments(masterComment);
        // 设置点赞人信息
        userDiaryDTO.setLikeUserInfos(likeUserInfo);
        // 设置点赞状态
        userDiaryDTO.setLikeStatus(userLikeService.judgeDiaryLikeStatus(userId, String.valueOf(circleId), userDiary.getId()));
        // 获取用户信息
        UserInfo userInfo = userInfoService.selectByPrimaryKey(userDiary.getUserId());
        // 设置用户昵称
        userInfo.setNickname(joinCircleService.getUserJoinCircleNickname(userId, circleId));
        // 设置创建时间
        userDiaryDTO.setFormatCreateTime(DateUtil.format(userDiary.getDiaryCreatetime(), DatePattern.NORM_DATETIME_PATTERN));
        // 设置用户头像
        userDiaryDTO.setUserHeadImage(userInfo.getAvatarurl());
        // 设置用户性别
        userDiaryDTO.setUserGender(userInfo.getGender());
        JoinCircle joinCircle = joinCircleService.selectByPrimaryKey(circleId, userDiary.getUserId());
        // 设置用户打卡天数
        userDiaryDTO.setUserSignNumber(joinCircle.getUserSigninDay());
        // 设置用户处理后的创建时间 （几天前）
        userDiaryDTO.setCreateTime(CclDateUtil.todayDate(userDiary.getDiaryCreatetime()));
        TodayContent todayContent = todayContentService.selectByPrimaryKey(userDiary.getThemeId().longValue());
        if (todayContent != null && userDiary.getThemeId() != 0) {
            CircleHomeThemeVO circleHomeThemeVO = new CircleHomeThemeVO();
            BeanUtils.copyProperties(todayContent, circleHomeThemeVO);
            userDiaryDTO.setThemeInfo(circleHomeThemeVO);
        }
        if (index) {
            CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(circleId);
            CircleVO circleVO = new CircleVO();
            BeanUtils.copyProperties(circleInfo, circleVO);
            userDiaryDTO.setCircleInfo(circleVO);
        }
        CircleHomeDiaryVO circleHomeDiaryVO = new CircleHomeDiaryVO();
        BeanUtils.copyProperties(userDiaryDTO, circleHomeDiaryVO);
        return circleHomeDiaryVO;
    }

    @Override
    public Long countByCircleIdAndUserIdAndDiaryStatus(Long circleId, List<Integer> diaryStatus, String userId) {
        return userDiaryMapper.countByCircleIdAndUserIdAndDiaryStatus(circleId, userId, diaryStatus);
    }

    @Override
    public int updateByPrimaryKey(UserDiary record) {
        return userDiaryMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<UserDiary> getUserDiaryByThemeId(Integer themeId) {
        return userDiaryMapper.selectAllByThemeId(themeId);
    }

    @Override
    public int updateDiaryStatusByThemeId(Integer themeId, Integer diaryStatus) {
        return userDiaryMapper.updateDiaryStatusByThemeId(themeId, diaryStatus);
    }

    @Override
    public int countThemeUserNumberByDate(Long circleId, Date date) {
        String formatDate = DateUtil.format(date, DatePattern.PURE_DATE_PATTERN);
        return userDiaryMapper.countThemeUserNumberByDate(circleId, formatDate);
    }

    @Override
    public int countByThemeIdAndDiaryStatus(Integer themeId, Integer userId) {
        return userDiaryMapper.countByThemeIdAndDiaryStatus(themeId, userId);
    }

    @Override
    public List<UserDiary> selectAllByCircleIdAndDiaryStatus(Long circleId, List<Integer> diaryStatus, String userId) {
        return userDiaryMapper.selectAllByCircleIdAndDiaryStatus(circleId, userId, diaryStatus);
    }

    @Override
    public String updateCircleDiaryContent(UserDiaryVO userDiaryVO) {
        // 根据id查询日记内容
        UserDiary userDiary = userDiaryMapper.selectByPrimaryKey(userDiaryVO.getId());
        // 判断此日记是否为空
        if (userDiary == null || userDiary.getDiaryStatus().equals(EnumUserDiary.USER_DIARY_DELETE.getValue())) {
            // 理论上不存在这种情况
            return EnumResultStatus.FAIL.getValue();
        } else {
            Integer themeId = userDiaryVO.getThemeId();
            if (!defaultProperties.getDefaultThemeId().equals(themeId)) {
                TodayContent todayContent = todayContentService.selectByPrimaryKey(themeId.longValue());
                if (todayContent == null || todayContent.getContentStatus().equals(EnumThemeStatus.DELETE_STATUS.getValue())) {
                    return EnumResultStatus.FAIL.getValue();
                }
            }
            // 重新设置主题id
            userDiary.setThemeId(userDiaryVO.getThemeId());
            // 重新设置圈子内容
            userDiary.setDiaryContent(userDiaryVO.getDiaryContent());
            // 重新设置圈子所在地址
            userDiary.setDiaryAddress(userDiaryVO.getDiaryAddress());
            // 重新设置日记状态
            userDiary.setDiaryStatus(StringUtils.isEmpty(userDiaryVO.getDiaryStatus()) ? EnumUserDiary.USER_DIARY_NORMAL.getValue() : userDiaryVO.getDiaryStatus());
            // 得到历史图片列表(先判断历史图片是否为空)
            String diaryImage = userDiary.getDiaryImage();
            List<String> historyImages = new ArrayList<>();
            if (!StringUtils.isEmpty(diaryImage)) {
                historyImages = Arrays.asList(diaryImage.split(","));
            }
            // 设置处理后的图片列表
            userDiary.setDiaryImage(CclUtil.fileListDispose(userDiaryVO.getImages(), historyImages));
            // 设置处理后的视频文件
            userDiary.setDiaryVideo(CclUtil.fileDispose(userDiaryVO.getDiaryVideo(), userDiary.getDiaryVideo()));
            // 设置处理后的音频文件
            userDiary.setDiaryVoice(CclUtil.fileDispose(userDiaryVO.getDiaryVoice(), userDiary.getDiaryVoice()));
            // 更新日志信息
            userDiaryMapper.updateByPrimaryKeySelective(userDiary);
            // 返回此日志更新后的信息
            return JSON.toJSONStringWithDateFormat(userDiary, DatePattern.NORM_DATETIME_MINUTE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
        }
    }

    @Override
    public String deleteUserDiaryInfo(Long diaryId) {
        // 根据id查询日志信息
        UserDiary userDiary = userDiaryMapper.selectByPrimaryKey(diaryId);
        if (userDiary == null || userDiary.getDiaryStatus().equals(EnumUserDiary.USER_DIARY_DELETE.getValue())) {
            return EnumResultStatus.FAIL.getValue();
        }
        // 将日志状态设置为删除状态
        userDiary.setDiaryStatus(EnumUserDiary.USER_DIARY_DELETE.getValue());
        // 更新日志
        userDiaryMapper.updateByPrimaryKeySelective(userDiary);
        // 用户id
        String userId = userDiary.getUserId();
        // 圈子id
        Long circleId = userDiary.getCircleId();
        // 此日记的所对应的主题id
        String themeId = String.valueOf(userDiary.getThemeId());
        // 得到用户加入圈子的信息
        JoinCircle joinCircle = joinCircleService.selectByPrimaryKey(circleId, userId);
        // 将日志创建时间格式化并且转化为字符串
        String diaryCreateStr = String.valueOf(DateUtil.format(userDiary.getDiaryCreatetime(), DatePattern.NORM_DATE_PATTERN));
        // 获取日志的主题id，判断主题列表id是否为空
        String themeIds = joinCircle.getThemeId();
        // 判断日志是否为今日
        if (DateUtil.betweenDay(userDiary.getDiaryCreatetime(), new Date(), true) == 0) {
            // 判断主题列表是否为空
            if (StringUtils.isEmpty(themeIds)) {
                return EnumResultStatus.FAIL.getValue();
            }
            // 获取主题列表
            ArrayList<String> themeIdsList = new ArrayList<>(Arrays.asList(themeIds.split(",")));
            // 判断主题id是否在今日的主题列表中
            if (themeIdsList.contains(themeId)) {
                // 删除此日志主题
                themeIdsList.removeIf(s -> s.equals(themeId));
            }
            // 判断列表id对应的日志是否为空
            if (themeIdsList.isEmpty()) {
                // 日志已经全部删除
                joinCircle.setUserSignStatus(EnumUserClockIn.USER_CLOCK_IN_FAIL.getValue());
                joinCircle.setThemeId("");
            } else {
                // 日记部分删除
                joinCircle.setUserSignStatus(EnumUserClockIn.USER_CLOCK_IN_SUCCESS.getValue());
                joinCircle.setThemeId(CclUtil.listToString(themeIdsList, ','));
            }
        }
        boolean diaryIsNull = judgeThemeIdIsNull(userId, circleId, diaryCreateStr, EnumUserDiary.USER_DIARY_DELETE.getValue());
        // 如果日记全部删除后
        if (diaryIsNull) {
            // 用户打卡天数-1
            if (joinCircle.getUserSigninDay() > 0) {
                joinCircle.setUserSigninDay(joinCircle.getUserSigninDay() - 1);
            }
            // 获取去重后的用户打卡列表
            List<String> dateList = Arrays.stream(joinCircle.getClockinCalendar().split(",")).distinct().collect(Collectors.toList());
            // 获取用户连续打卡天数
            Integer userDays = getUserContinuousClockInDays(diaryCreateStr, joinCircle.getUserSignin(), dateList);
            // 设置用户连续打卡天数
            joinCircle.setUserSignin(userDays);
            // 删除用户打卡列表中的相应值，并返回相应数据
            String finDateList = deleteUserClockDate(diaryCreateStr, dateList);
            // 设置用户打卡列表
            joinCircle.setClockinCalendar(finDateList);
        }
        // 圈子日志 -1
        CircleInfo circleInfo = new CircleInfo();
        circleInfo.setDiarySum(0);
        circleInfoService.updateCircleData(circleInfo, circleId, EnumCommon.UPDATE_SUB.getData());
        // 更新用户数据
        joinCircleService.updateByPrimaryKeySelective(joinCircle);
        return EnumResultStatus.SUCCESS.getValue();
    }

    @Override
    public boolean judgeThemeIdIsNull(String userId, Long circleId, String diaryCreateTime, Integer diaryStatus) {
        List<UserDiary> userDiaries = userDiaryMapper.selectAllByUserIdAndCircleIdAndDiaryCreatetimeLikeAndDiaryStatus(userId, circleId, diaryCreateTime, diaryStatus);
        if (!userDiaries.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String judgeUserVitality(String createTime, List<String> dateList) {
        // 获取目标日期下标
        int index = getTargetStrIndexOf(createTime, dateList, EnumUserVitalityJoint.USER_JOINT_STRING.getValue());
        // 得到目标日期
        String targetStr = dateList.get(index);
        return EnumUserVitalityJoint.USER_JOINT_STRING.getValue() + targetStr.split(EnumUserVitalityJoint.USER_JOINT_STRING.getValue())[1];
    }

    @Override
    public String deleteUserClockDate(String createTime, List<String> dateList) {
        // 获取目标日期下标
        int index = getTargetStrIndexOf(createTime, dateList, EnumUserVitalityJoint.USER_JOINT_STRING.getValue());
        if (index == -1) {
            // 说明这个时间不在此列表中
            return CclUtil.listToString(dateList, ',') + EnumCommon.STRING_JOINT.getValue();
        }
        dateList.remove(index);
        if (!dateList.isEmpty()) {
            return CclUtil.listToString(dateList, ',') + EnumCommon.STRING_JOINT.getValue();
        }
        return "";
    }

    @Override
    public Integer getUserContinuousClockInDays(String createTime, Integer days, List<String> dateList) {
        // 获取目标日期下标
        int index = getTargetStrIndexOf(createTime, dateList, EnumUserVitalityJoint.USER_JOINT_STRING.getValue());
        int dateListSize = dateList.size();
        if (index >= dateListSize - days) {
            // 说明删除的日期为连续打卡日期中的一个
            if (index != dateListSize - 1) {
                // 删除的不是最后一天
                return dateListSize - 1 - index;
            } else {
                // 删除的是最后一天
                return days - 1;
            }
        }
        return days;
    }

    @Override
    public Integer getTargetStrIndexOf(String createTime, List<String> dateList, String splitStr) {
        List<String> disposeDateList = new ArrayList<>();
        for (String date : dateList) {
            // 将日期全部添加到列表中
            disposeDateList.add(date.split(splitStr)[0]);
        }
        // 得到在列表中的位置
        return disposeDateList.indexOf(createTime);
    }

    @Override
    public String saveUserDiary(UserDiary userDiary) {
        // 设置日志创建时间
        userDiary.setDiaryCreatetime(new Date());
        // 查询用户加入圈子的信息
        Long circleId = userDiary.getCircleId();
        String userId = userDiary.getUserId();
        Integer themeId = 0;
        // 判断是否连续打卡一定天数获得额外能量球
        boolean continuous = false;
        if (!userDiary.getThemeId().equals(0)) {
            themeId = userDiary.getThemeId();
            TodayContent todayContent = todayContentService.selectByPrimaryKey(themeId.longValue());
            if (todayContent == null || !todayContent.getContentStatus().equals(EnumThemeStatus.USE_STATUS.getValue())) {
                return EnumResultStatus.FAIL.getValue();
            }
        }
        JoinCircle joinCircle = joinCircleService.selectByPrimaryKey(circleId, userId);
        // 若此用户完成全部打卡则提示您已经完成全部打卡主题
        if (!joinCircle.getUserSignStatus().equals(EnumUserClockIn.USER_ALL_CLOCK_IN_SUCCESS.getValue())) {
            List<String> themeList = new ArrayList<>();
            // 判断此用户的打卡主题是否为空
            if (!StringUtils.isEmpty(joinCircle.getThemeId())) {
                // 获取全部打卡列表
                themeList = new ArrayList<>(Arrays.asList(joinCircle.getThemeId().split(",")));
            }
            // 添加主题到主题列表中（理论上无需判断此种情况不会出现！）
            if (!themeList.contains(String.valueOf(userDiary.getThemeId()))) {
                themeList.add(String.valueOf(themeId));
            } else {
                // 未知错误，对一个主题重复打卡，或者日记出现问题
                return EnumResultStatus.FAIL.getValue();
            }
            // 设置主题id
            joinCircle.setThemeId(CclUtil.listToString(themeList, ','));
            // 判断此主题用户是否发表过日记
            int countDiary = userDiaryMapper.countByUserIdAndCircleIdAndThemeId(userId, circleId, themeId);
            if (countDiary == 0) {
                // 此日记对应的主题是用户今天第一次发表，活跃度+5
                joinCircle.setUserVitality(joinCircle.getUserVitality() + EnumUserVitality.USER_NO_CONTINUOUS_CLOCK_IN.getValue());
            }
            // 获取圈子中全部的主题总数 + 1 因为每个圈子中都存在一个无主题的情况
            int themes = todayContentService.countByCircleIdAndContentStatus(circleId, EnumThemeStatus.DELETE_STATUS.getValue()) + 1;
            // 设置打卡主题为1
            joinCircle.setUserSignStatus(EnumUserClockIn.USER_CLOCK_IN_SUCCESS.getValue());
            // 判断用户是否完成全部主题的打卡
            if (themeList.size() == themes || themes == 0) {
                // 此用户已经完成全部打卡
                joinCircle.setUserSignStatus(EnumUserClockIn.USER_ALL_CLOCK_IN_SUCCESS.getValue());
            }
            // 格式化今天的日期
            String signInTime = DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
            // 判断此用户今天第一次打卡 主题列表中为1，并且此日记不是用户删除之后再打卡的
            if (themeList.size() == 1) {
                // 获取用户第一次打卡后是否被删除，再次重新打卡
                joinCircle.setUserSigninDay(joinCircle.getUserSigninDay() + 1);
                // 用户连续打卡天数+1
                joinCircle.setUserSignin(joinCircle.getUserSignin() + 1);
                // 判断是否为连续打卡
                boolean judgeClockIn = joinCircle.getUserSignin() % EnumUserVitality.INTEGRATION_PERIOD.getValue() == 0;
                // 获取用户打卡日历
                String clockinCalendar = joinCircle.getClockinCalendar();
                // 更新用户连续打卡列表信息
                StringBuilder sb = new StringBuilder();
                if (judgeClockIn) {
                    sb.append(clockinCalendar).append(signInTime).append(EnumUserVitalityJoint.USER_NO_CONTINUOUS_CLOCK_IN.getValue())
                            .append(EnumCommon.STRING_JOINT.getValue());
                } else {
                    if (clockinCalendar != null) {
                        sb.append(clockinCalendar);
                    }
                    sb.append(signInTime).append(EnumUserVitalityJoint.USER_CONTINUOUS_CLOCK_IN.getValue())
                            .append(EnumCommon.STRING_JOINT.getValue());
                }
                // 设置用户打卡日期列表
                joinCircle.setClockinCalendar(sb.toString());
                // 判断是否为今天第一次打卡
                if (countDiary == 0) {
                    // 得到用户今天打卡的日记数目
                    int diaryCount = userDiaryMapper.countByUserIdAndCircleIdAndDiaryCreatetimeLike(userId, circleId, signInTime);
                    if (diaryCount == 0) {
                        // 判断是否为连续打卡
                        if (judgeClockIn) {
                            // 获取用户的活跃度
                            Long userVitality = joinCircle.getUserVitality();
                            // 是连续第7天打卡，活跃度额外增加
                            joinCircle.setUserVitality(userVitality + EnumUserVitality.USER_CONTINUOUS_CLOCK_IN.getValue());
                            continuous = true;
                        }
                    }
                }
            }
            // 设置用户最后打卡时间
            joinCircle.setUserSignTime(new Date());
            // 日志总数 +1
            CircleInfo circleInfo = new CircleInfo();
            circleInfo.setDiarySum(0);
            circleInfoService.updateCircleData(circleInfo, circleId, EnumCommon.UPDATE_ADD.getData());
            // 更新用户加入圈子信息
            joinCircleService.updateByPrimaryKeySelective(joinCircle);
            // 将日志数据保存到数据库中
            userDiaryMapper.insertSelective(userDiary);
            List<Object> diaryList = new ArrayList<>();
            diaryList.add(userDiary);
            diaryList.add(continuous);
            return JSON.toJSONStringWithDateFormat(diaryList, DatePattern.NORM_DATETIME_MINUTE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
        } else {
            // 今天已经完成全部打卡
            return EnumResultStatus.FAIL.getValue();
        }
    }

    @Override
    public String saveDiaryImage(MultipartFile image, String userId, Long diaryId) {
        UserDiary userDiary = userDiaryMapper.selectByPrimaryKey(diaryId);
        if (userDiary == null) {
            return EnumResultStatus.FAIL.getValue();
        }
        boolean flag;
        if ("".equals(userDiary.getDiaryImage())) {
            flag = true;
        } else {
            flag = false;
        }
        String imagePath = FtpUtil.uploadFile(userId, image);
        if (EnumResultStatus.FAIL.getValue().equals(imagePath)) {
            return EnumResultStatus.FAIL.getValue();
        }
        userDiaryMapper.concatImage(diaryId, imagePath, flag);
        return JSON.toJSONString(imagePath);
    }

    @Override
    public String saveDiaryVoice(MultipartFile file, String userId, Long diaryId) {
        UserDiary userDiary = userDiaryMapper.selectByPrimaryKey(diaryId);
        if (userDiary == null) {
            return EnumResultStatus.FAIL.getValue();
        } else {
            if (!StringUtils.isEmpty(userDiary.getDiaryVoice())) {
                return EnumResultStatus.FAIL.getValue();
            } else {
                String filePath = FtpUtil.uploadFile(userId, file);
                if (EnumResultStatus.FAIL.getValue().equals(filePath)) {
                    return EnumResultStatus.FAIL.getValue();
                } else {
                    userDiary.setDiaryVoice(filePath);
                    userDiaryMapper.updateByPrimaryKeySelective(userDiary);
                    return JSON.toJSONString(filePath);
                }
            }
        }
    }

    @Override
    public String saveDiaryVideo(MultipartFile file, String userId, Long diaryId) {
        UserDiary userDiary = userDiaryMapper.selectByPrimaryKey(diaryId);
        if (userDiary == null) {
            return EnumResultStatus.FAIL.getValue();
        } else {
            if (!StringUtils.isEmpty(userDiary.getDiaryVideo())) {
                return EnumResultStatus.FAIL.getValue();
            } else {
                String filePath = FtpUtil.uploadFile(userId, file);
                if (EnumResultStatus.FAIL.getValue().equals(filePath)) {
                    return EnumResultStatus.FAIL.getValue();
                } else {
                    userDiary.setDiaryVideo(filePath);
                    userDiaryMapper.updateByPrimaryKeySelective(userDiary);
                    return JSON.toJSONString(filePath);
                }
            }
        }
    }

    @Override
    public List<UserDiary> selectByCircleIdAndUserIdAndDiaryCreatetimeAndDiaryStatus(Long circleId, String userId, String date, List<Integer> diaryStatus, Integer themeId) {
        return userDiaryMapper.selectByCircleIdAndUserIdAndDiaryCreatetimeAndDiaryStatus(circleId, userId, date, diaryStatus, themeId);
    }

    @Override
    public void addDiaryBrowseNumber(String userId, Long diaryId) {
        String key = EnumRedis.DIARY_BROWSE.getValue() + EnumRedis.REDIS_JOINT.getValue() + userId + EnumRedis.REDIS_JOINT.getValue() + diaryId;
        Boolean browseBoolean = redisTemplate.hasKey(key);
        // 获取当前的格式化时间
        String date = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
        if (!browseBoolean) {
            // 不存在，浏览量+1
            redisTemplate.opsForValue().increment(EnumRedis.DIARY_BROWSE.getValue() + EnumRedis.REDIS_JOINT.getValue() + EnumRedis.NUMBER.getValue() + EnumRedis.REDIS_JOINT.getValue() + diaryId);
            // 设置key-value 过期时间为24小时，24小时内只能为日志增加1浏览量
            redisTemplate.opsForValue().set(key, date, BROWSE_PAST_TIME, TimeUnit.HOURS);
        }
    }

    @Override
    public void saveDiaryBrowseNumber() {
        String key = EnumRedis.DIARY_BROWSE.getValue() + EnumRedis.REDIS_JOINT.getValue() + EnumRedis.NUMBER.getValue() + "*";
        Set<String> set = redisTemplate.keys(key);
        for (String s : set) {
            String[] split = s.split("::");
            String diaryId = split[split.length - 1];
            Integer browseNumber = (Integer) redisTemplate.opsForValue().get(s);
            userDiaryMapper.updateDiaryBrowseById(browseNumber, Long.valueOf(diaryId));
            redisTemplate.delete(set);
        }
    }

    @Override
    public String getDiaryInfoById(Long diaryId, String userId) {
        UserDiary userDiary = userDiaryMapper.selectByPrimaryKey(diaryId);
        if (userDiary == null) {
            return EnumResultStatus.FAIL.getValue();
        }
        return JSON.toJSONString(getUserDiaryVO(userId, false, userDiary));
    }
}