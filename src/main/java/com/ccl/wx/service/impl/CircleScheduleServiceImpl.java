package com.ccl.wx.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.ccl.wx.entity.UserDiary;
import com.ccl.wx.entity.UserLike;
import com.ccl.wx.enums.EnumLike;
import com.ccl.wx.enums.EnumRedis;
import com.ccl.wx.enums.EnumUserDiary;
import com.ccl.wx.service.*;
import com.ccl.wx.util.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 褚超亮
 * @date 2020/1/30 18:40
 */
@Slf4j
@Service
public class CircleScheduleServiceImpl implements CircleScheduleService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserDiaryService userDiaryService;

    @Resource
    private UserLikeService userLikeService;

    @Resource
    private CommentService commentService;

    @Resource
    private ReplyService replyService;

    @Override
    public void saveUserLikeDataPersistence() {
        // 获取所有的键
        Set<String> likeUsers = redisTemplate.keys(EnumRedis.LIKE_PREFIX.getValue() + "*");
        log.info(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN) + "持久化点赞数据：" + likeUsers.size() + "条数据");
        if (likeUsers.size() != 0) {
            // 遍历
            for (String likeUser : likeUsers) {
                // 得到圈子id 和 日志id 数组
                redisTemplate.opsForHash().entries(likeUser).forEach((key, value) -> {
                    // 为有效点赞
                    String[] userIdArray = String.valueOf(likeUser).split(EnumRedis.REDIS_JOINT.getValue());
                    String[] circleIdAndDiaryId = String.valueOf(key).split(EnumRedis.REDIS_JOINT.getValue());
                    String circleId = circleIdAndDiaryId[0];
                    String diaryId = circleIdAndDiaryId[1];
                    String userId = userIdArray[1];
                    UserLike userLikeInfo = userLikeService.selectByTypeId(Long.valueOf(diaryId));
                    if (value.equals(EnumLike.LIKE_SUCCESS.getValue())) {
                        UserLike userLike = new UserLike();
                        if (userLikeInfo != null) {
                            // 更新数据
                            userLikeService.concatLikeUserId(Long.valueOf(diaryId), userId + ",");
                        } else {
                            // 插入数据
                            userLike.setLikeUserid(userId + ",");
                            userLike.setCircleId(Long.valueOf(circleId));
                            userLike.setTypeId(Long.valueOf(diaryId));
                            userLike.setType(EnumLike.LIKE_DIARY.getValue());
                            userLike.setLikeCreatetime(new Date());
                            userLike.setLikeUpdatetime(new Date());
                            userLike.setLikeStatus(EnumLike.LIKE_SUCCESS.getValue());
                            userLikeService.insertSelective(userLike);
                        }
                    } else {
                        // 若点赞对象不为空，且点赞用户不为空
                        if (userLikeInfo != null && !StringUtils.isEmpty(userLikeInfo.getLikeUserid())) {
                            List<String> likeUserIds = new ArrayList<>(Arrays.asList(userLikeInfo.getLikeUserid().split(",")));
                            if (likeUserIds.contains(userId) && likeUserIds.size() == 1) {
                                // 存在且只有这一个用户，直接删除
                                userLikeService.deleteByPrimaryKey(userLikeInfo.getId());
                            } else {
                                // 如果点赞列表中存在此信息，更新数据
                                userLikeInfo.setLikeUpdatetime(new Date());
                                likeUserIds.removeIf(s -> s.equals(userId));
                                userLikeInfo.setLikeUserid(String.join(",", likeUserIds) + ",");
                                userLikeService.updateByPrimaryKeySelective(userLikeInfo);
                            }
                        }
                    }
                    // 删除缓存中的数据
                    redisTemplate.opsForHash().delete(likeUser, key);
                });
            }
        }
    }

    @Override
    public void saveUserAccountLikeDataPersistence() {
        // 获取like的全部键
        Set<String> keys = redisTemplate.keys(EnumRedis.LIKE_SUM_PREFIX.getValue() + "*");
        int size = keys.size();
        log.info(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN) + "redis中点赞中总数量：" + keys.size() + "条数据");
        for (String key : keys) {
            String[] split = key.split("::");
            String diaryId = split[1];
            UserDiary userDiary = userDiaryService.selectByPrimaryKey(Long.valueOf(diaryId));
            // 判断日记是否为空或者是否为删除状态
            if (userDiary == null || userDiary.getDiaryStatus().equals(EnumUserDiary.USER_DIARY_DELETE.getValue())) {
                size--;
                continue;
            }
            Integer likeAccount = userDiary.getDiaryLike() + Integer.parseInt(String.valueOf(redisTemplate.opsForValue().get(key)));
            // 如果点赞数小于0（理论上不存在） 设置为0
            if (likeAccount < 0) {
                userDiary.setDiaryLike(0);
            } else {
                userDiary.setDiaryLike(likeAccount);
            }
            // 更新数据
            userDiaryService.updateByPrimaryKeySelective(userDiary);
            // 删除redis中的数据
            redisTemplate.delete(key);
        }
        log.info(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN) + "redis中持久化点赞总数量：" + size + "条数据");
    }

    @Override
    public void deleteUserDiaryInfoAndComment() {
        // 删除的 日记总数、评论总数、回复总数、点赞总数
        int diarySum = 0, commentSum = 0, replySum = 0, likeSum = 0;
        // 获取所有删除状态的日志信息
        List<Long> diaryIds = userDiaryService.selectIdByDiaryStatus(EnumUserDiary.USER_DIARY_DELETE.getValue());
        for (Long diaryId : diaryIds) {
            UserDiary userDiary = userDiaryService.selectByPrimaryKey(diaryId);
            // 查看日志图片信息
            String diaryImage = userDiary.getDiaryImage();
            // 图片信息是否为空
            if (!StringUtils.isEmpty(diaryImage)) {
                // 不为空 分隔字符串
                List<String> images = Arrays.asList(diaryImage.split(","));
                // 删除服务器上的图片
                for (String image : images) {
                    // 删除图片
                    FtpUtil.delFile(image);
                }
            }
            // 根据日记id查询全部评论列表
            List<Long> commentIds = commentService.selectIdByDiaryId(diaryId);
            // 根据日记id删除评论
            for (Long commentId : commentIds) {
                commentSum++;
                commentService.deleteByPrimaryKey(commentId);
            }
            // 根据日记id查询全部回复列表
            List<Long> replyIds = replyService.selectIdByDiaryId(diaryId);
            // 根据日记id删除回复
            for (Long replyId : replyIds) {
                replySum++;
                replyService.deleteByPrimaryKey(replyId);
            }
            // 根据日记id删除全部点赞列表
            List<Long> likeIds = userLikeService.selectIdByTypeId(diaryId);
            // 根据日记id删除点赞
            for (Long likeId : likeIds) {
                likeSum++;
                userLikeService.deleteByPrimaryKey(likeId);
            }
            // 删除日志
            diarySum++;
            // 删除日志信息
            userDiaryService.deleteByPrimaryKey(diaryId);
        }
        // 格式化时间
        String dateInfo = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);
        // 统计删除信息
        log.info(dateInfo + "删除日志数:" + diarySum);
        log.info(dateInfo + "删除评论数:" + commentSum);
        log.info(dateInfo + "删除回复数:" + replySum);
        log.info(dateInfo + "删除点赞数:" + likeSum);
    }
}
