package com.ccl.wx.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.ccl.wx.dto.CircleTodayContentDTO;
import com.ccl.wx.dto.UserDiaryDTO;
import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.entity.TodayContent;
import com.ccl.wx.entity.UserDiary;
import com.ccl.wx.enums.*;
import com.ccl.wx.mapper.UserDiaryMapper;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.TodayContentService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.CclUtil;
import com.ccl.wx.util.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private UserDiaryMapper userDiaryMapper;

    @Autowired
    private TodayContentService todayContentService;

    @Autowired
    private JoinCircleService joinCircleService;

    @Autowired
    private RedisTemplate redisTemplate;

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
    public String updateCircleTodayContent(CircleTodayContentDTO circleTodayContentDTO) {
        // 根据id查询每日内容的数据
        TodayContent todayContent = todayContentService.selectByPrimaryKey(circleTodayContentDTO.getId());
        if (StringUtils.isEmpty(todayContent)) {
            // 对象为空 理论上不存在这种情况，存在则出现错误！！
            return "-1";
        } else {
            // 对象不空 1. 查看此今日内容是否存在图片
            // 设置更新时间
            todayContent.setTodayContent(circleTodayContentDTO.getTodayContent());
            todayContent.setUpdateTime(new Date());
            if (StringUtils.isEmpty(todayContent.getTodayImage())) {
                //    图片为空，更新文本内容，返回此今日内容的id，上传图片
                todayContentService.updateByPrimaryKeySelective(todayContent);
            } else {
                //    图片不为空，删除之前的图片，并且更新文本内容，返回此今日内容的id，上传图片
                List<String> images = Arrays.asList(todayContent.getTodayImage().split(","));
                // 对图片的处理
                todayContent.setTodayImage(imageDispose(circleTodayContentDTO.getTodayImages(), images));
                // 更新数据
                todayContentService.updateByPrimaryKeySelective(todayContent);
            }
            return String.valueOf(circleTodayContentDTO.getId());
        }
    }

    @Override
    public String updateCircleDiaryContent(UserDiaryDTO userDiaryDTO) {
        // 根据id查询日记内容
        UserDiary userDiary = userDiaryMapper.selectByPrimaryKey(userDiaryDTO.getId());
        // 判断此日记是否为空
        if (userDiary != null) {
            // 重新设置圈子内容
            userDiary.setDiaryContent(userDiaryDTO.getDiaryContent());
            // 重新设置圈子所在地址
            userDiary.setDiaryAddress(userDiaryDTO.getDiaryAddress());
            // 重新设置日记状态
            userDiary.setDiaryStatus(userDiaryDTO.getDiaryStatus());
            // 设置日志更新时间
            userDiary.setDiaryUpdatetime(new Date());
            // 得到历史图片列表(先判断历史图片是否为空)
            String diaryImage = userDiary.getDiaryImage();
            List<String> historyImages = new ArrayList<>();
            if (diaryImage != null && !"".equals(diaryImage)) {
                historyImages = Arrays.asList(diaryImage.split(","));
            }
            // 设置处理后的图片列表
            userDiary.setDiaryImage(imageDispose(userDiaryDTO.getImages(), historyImages));
            // 更新日志信息
            userDiaryMapper.updateByPrimaryKeySelective(userDiary);
            // 返回此日志id
            return String.valueOf(userDiaryDTO.getId());
        } else {
            // 理论上不存在这种情况 TODO 未处理
            return "-1";
        }
    }

    @Override
    public String imageDispose(List<String> images, List<String> historyImages) {
        if (images.size() == 0) {
            // 前端传输的图片列表为空，删除全部历史图片
            if (historyImages.size() != 0) {
                for (String image : historyImages) {
                    FtpUtil.delFile(image);
                }
            }
            // 返回空字符串
            return "";
        } else {
            // 新图片不为空
            ArrayList<String> finImages = new ArrayList<>();
            for (String image : images) {
                if (historyImages.contains(image)) {
                    // 判断历史图片列表中是否存在新的图片,存在不删除
                    finImages.add(image);
                } else {
                    if (image.startsWith("https")) {
                        // 是https开头则为不存在的历史图片，删除
                        FtpUtil.delFile(image);
                    }
                }
            }
            if (finImages.size() == 0) {
                return "";
            } else {
                return CclUtil.listToString(finImages, ',');
            }
        }
    }

    @Override
    public String deleteUserDiaryInfo(Long diaryid) {
        // 根据id查询日志信息
        UserDiary userDiary = userDiaryMapper.selectByPrimaryKey(diaryid);
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
        // 判断主题列表是否为空
        if (StringUtils.isEmpty(themeIds)) {
            return EnumResultStatus.UNKNOWN.getValue();
        }
        // 获取主题列表
        ArrayList<String> themeIdsList = new ArrayList<>(Arrays.asList(themeIds.split(",")));
        // 判断日志是否为今日
        if (DateUtil.betweenDay(userDiary.getDiaryCreatetime(), new Date(), true) == 0) {
            // 判断主题id是否在今日的主题列表中
            if (!themeIdsList.contains(themeId)) {
                return EnumResultStatus.UNKNOWN.getValue();
            }
            // 删除此日志主题
            themeIdsList.removeIf(s -> s.equals(themeId));
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
        if (dateList.size() != 0) {
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
        int index = disposeDateList.indexOf(createTime);
        return index;
    }

    @Override
    public String publishUserDiary(UserDiary userDiary) {
        // TODO 日志主题重复待检测（理论上不会出现这种情况）
        // 设置日志创建时间
        userDiary.setDiaryCreatetime(new Date());
        // 查询用户加入圈子的信息
        Long circleId = userDiary.getCircleId();
        String userId = userDiary.getUserId();
        Integer themeId = 0;
        if (userDiary.getThemeId() != null) {
            themeId = userDiary.getThemeId();
        }
        JoinCircle circleInfo = joinCircleService.selectByPrimaryKey(circleId, userId);
        // 若此用户完成全部打卡则提示您已经完成全部打卡主题
        if (!circleInfo.getUserSignStatus().equals(EnumUserClockIn.USER_ALL_CLOCK_IN_SUCCESS.getValue())) {
            List<String> themeList = new ArrayList<>();
            // 判断此用户的打卡主题是否为空
            if (!StringUtils.isEmpty(circleInfo.getThemeId())) {
                // 获取全部打卡列表
                themeList = new ArrayList<>(Arrays.asList(circleInfo.getThemeId().split(",")));
            }
            // 添加主题到主题列表中（理论上无需判断此种情况不会出现！）
            if (!themeList.contains(String.valueOf(userDiary.getThemeId()))) {
                themeList.add(String.valueOf(themeId));
            } else {
                // 未知错误，对一个主题重复打卡，或者日记出现问题
                return EnumResultStatus.UNKNOWN.getValue();
            }
            // 设置主题id
            circleInfo.setThemeId(CclUtil.listToString(themeList, ','));
            // 判断此主题用户是否发表过日记
            int countDiary = userDiaryMapper.countByUserIdAndCircleIdAndThemeId(userId, circleId, themeId);
            if (countDiary == 0) {
                // 此日记对应的主题是用户今天第一次发表，活跃度+5
                circleInfo.setUserVitality(circleInfo.getUserVitality() + EnumUserVitality.USER_NO_CONTINUOUS_CLOCK_IN.getValue());
            }
            // 获取圈子中全部的主题总数 + 1 因为每个圈子中都存在一个无主题的情况
            int themes = todayContentService.countByCircleIdAndContentStatus(circleId, EnumThemeStatus.DELETE_STATUS.getValue()) + 1;
            // 用户发表的日志主题被删除了
            if (themeList.size() > themes) {
                // 未知错误
                return EnumResultStatus.UNKNOWN.getValue();
            }
            // 设置打卡主题为1
            circleInfo.setUserSignStatus(EnumUserClockIn.USER_CLOCK_IN_SUCCESS.getValue());
            // 判断用户是否完成全部主题的打卡
            if (themeList.size() == themes || themes == 0) {
                // 此用户已经完成全部打卡
                circleInfo.setUserSignStatus(EnumUserClockIn.USER_ALL_CLOCK_IN_SUCCESS.getValue());
            }
            // 格式化今天的日期
            String signInTime = DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
            // 判断此用户今天第一次打卡 主题列表中为1，并且此日记不是用户删除之后再打卡的
            if (themeList.size() == 1) {
                // 获取用户第一次打卡后是否被删除，再次重新打卡
                circleInfo.setUserSigninDay(circleInfo.getUserSigninDay() + 1);
                // 用户连续打卡天数+1
                circleInfo.setUserSignin(circleInfo.getUserSignin() + 1);
                // 判断是否为连续打卡
                boolean judgeClockIn = circleInfo.getUserSignin() % EnumUserVitality.INTEGRATION_PERIOD.getValue() == 0;
                // 获取用户打卡日历
                String clockinCalendar = circleInfo.getClockinCalendar();
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
                circleInfo.setClockinCalendar(sb.toString());
                // 判断是否为今天第一次打卡
                if (countDiary == 0) {
                    // 得到用户今天打卡的日记数目
                    int diaryCount = userDiaryMapper.countByUserIdAndCircleIdAndDiaryCreatetimeLike(userId, circleId, signInTime);
                    if (diaryCount == 1) {
                        // 判断是否为连续打卡
                        if (judgeClockIn) {
                            // 获取用户的活跃度
                            Long userVitality = circleInfo.getUserVitality();
                            // 是连续第7天打卡，活跃度额外增加
                            circleInfo.setUserVitality(userVitality + EnumUserVitality.USER_CONTINUOUS_CLOCK_IN.getValue());
                        }
                    }
                }
            }
            // 设置用户最后打卡时间
            circleInfo.setUserSignTime(new Date());
            // 更新用户加入圈子信息
            joinCircleService.updateByPrimaryKeySelective(circleInfo);
            // 将日志数据保存到数据库中
            userDiaryMapper.insertSelective(userDiary);
            return String.valueOf(userDiary.getId());
        } else {
            // 今天已经打卡
            return EnumResultStatus.SUCCESS.getValue();
        }
    }

    /**
     * TODO 保存日志图片信息
     *
     * @param image
     * @param userId
     * @param id
     * @return
     */
    @Override
    public String saveDiaryImage(MultipartFile image, String userId, Long id) {
        UserDiary userDiary = userDiaryMapper.selectByPrimaryKey(id);
        boolean flag;
        if ("".equals(userDiary.getDiaryImage())) {
            flag = true;
        } else {
            flag = false;
        }
        String imagePath = FtpUtil.uploadFile(userId, image);
        userDiaryMapper.concatImage(id, imagePath, flag);
        return EnumResultStatus.SUCCESS.getValue();
    }

    @Override
    public String getCircleDiaryById(Long diaryId) {
        UserDiary userDiary = userDiaryMapper.selectByPrimaryKey(diaryId);
        if ("".equals(userDiary.getDiaryImage())) {
            userDiary.setDiaryImage(null);
        }
        return JSON.toJSONString(userDiary);
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
}