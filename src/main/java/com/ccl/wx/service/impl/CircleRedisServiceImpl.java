package com.ccl.wx.service.impl;

import com.ccl.wx.service.CircleRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author 褚超亮
 * @date 2020/1/27 16:22
 */
@Service
public class CircleRedisServiceImpl implements CircleRedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CircleRedisService circleRedisService;

    /**
     * redis存储的点赞前缀
     */
    private final static String LIKE_PREFIX = "like::";

    /**
     * redis存储的点赞和前缀
     */
    private final static String LIKE_SUM_PREFIX = "account::";

    /**
     * redis存储的点赞状态
     */
    private final static String LIKE_STATUS_PREFIX = "likeStatus::";

    /**
     * redis中连接符号
     */
    private final static String CONNECT_VALUE = "::";

    /**
     * 点赞状态
     */
    private final static Integer LIKE_STATUS = 1;

    /**
     * 取消点赞状态
     */
    private final static Integer UNLIKE_STATUS = 0;

    /**
     * 1分钟内最多操作的
     */
    private final static Integer SUM_LIKE = 15;

    /**
     * 过期时间
     */
    private final static Integer OUT_TIME = 60;

    @Override
    public String saveLikeRedis(String userId, String circleId, String diaryId) {
        // 用户点赞状态存储到redis中 like::用户id 键 圈子id::日志id 值 1 点赞 0 取消点赞
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(circleId) || StringUtils.isEmpty(diaryId)) {
            return "fail";
        } else {
            String key = circleId + CONNECT_VALUE + diaryId;
            redisTemplate.opsForHash().put(LIKE_PREFIX + userId, key, LIKE_STATUS);
            // 点赞数目 +1 若状态已经是1 则点赞数目不增加
            circleRedisService.incrementLikedCount(diaryId);
            return "success";
        }
    }

    @Override
    public String unLikeFromRedis(String userId, String circleId, String diaryId) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(circleId) || StringUtils.isEmpty(diaryId)) {
            return "fail";
        } else {
            String key = circleId + CONNECT_VALUE + diaryId;
            redisTemplate.opsForHash().put(LIKE_PREFIX + userId, key, UNLIKE_STATUS);
            // 点赞数目 -1
            circleRedisService.decrementLikeCount(diaryId);
            return "success";
        }
    }

    @Override
    public String deleteLikeFromRedis(String userId, String circleId, String diaryId) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(circleId) || StringUtils.isEmpty(diaryId)) {
            return "fail";
        } else {
            String key = circleId + CONNECT_VALUE + diaryId;
            // 删除redis缓存
            redisTemplate.opsForHash().delete(LIKE_PREFIX + userId, key);
            circleRedisService.decrementLikeCount(diaryId);
            return "success";
        }
    }

    @Override
    public String incrementLikedCount(String diaryId) {
        if (StringUtils.isEmpty(diaryId)) {
            return "fail";
        } else {
            if (!redisTemplate.hasKey(LIKE_SUM_PREFIX + diaryId)) {
                redisTemplate.opsForValue().set(LIKE_SUM_PREFIX + diaryId, 1);
            } else {
                redisTemplate.opsForValue().increment(LIKE_SUM_PREFIX + diaryId, 1);
            }
            return "success";
        }
    }

    @Override
    public String decrementLikeCount(String diaryId) {
        if (StringUtils.isEmpty(diaryId)) {
            return "fail";
        } else {
            if (!redisTemplate.hasKey(LIKE_SUM_PREFIX + diaryId)) {
                redisTemplate.opsForValue().set(LIKE_SUM_PREFIX + diaryId, -1);
            } else {
                redisTemplate.opsForValue().increment(LIKE_SUM_PREFIX + diaryId, -1);
            }
            return "success";
        }
    }

    @Override
    public Boolean judgeLikeStatus(String userid) {
        // 值+1
        String userKey = LIKE_STATUS_PREFIX + userid;
        redisTemplate.opsForValue().increment(userKey);
        int likeStatus = Integer.parseInt(String.valueOf(redisTemplate.opsForValue().get(userKey)));
        if (likeStatus <= SUM_LIKE) {
            // 可以进行点赞,并且设置过期时间为60s
            redisTemplate.expire(userKey, OUT_TIME, TimeUnit.SECONDS);
            return true;
        } else {
            // 不能设置
            return false;
        }
    }

    @Override
    public Integer getUserLikeStatus(String userId, String circleId, Long diaryId) {
        String hash = LIKE_PREFIX + userId;
        String key = circleId + CONNECT_VALUE + diaryId;
        Boolean keyStatus = redisTemplate.opsForHash().hasKey(hash, key);
        if (keyStatus) {
            // 此键存在
            return Integer.parseInt(String.valueOf(redisTemplate.opsForHash().get(hash, key)));
        } else {
            return null;
        }
    }
}
