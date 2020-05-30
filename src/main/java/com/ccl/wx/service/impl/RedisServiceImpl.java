package com.ccl.wx.service.impl;

import com.ccl.wx.enums.redis.EnumRedis;
import com.ccl.wx.enums.common.EnumCommon;
import com.ccl.wx.enums.diary.EnumLike;
import com.ccl.wx.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 褚超亮
 * @date 2020/5/12 13:37
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean judgeButtonClick(String userId, boolean isHigh) {
        // 值+1
        String userKey = (isHigh ? EnumRedis.HIGH_FREQUENCY_BUTTON.getValue() : EnumRedis.LOW_FREQUENCY_BUTTON.getValue())
                + EnumRedis.REDIS_JOINT.getValue() + userId;
        redisTemplate.opsForValue().increment(userKey);
        int likeStatus = Integer.parseInt(String.valueOf(redisTemplate.opsForValue().get(userKey)));
        int clickRestrain = isHigh ? EnumCommon.HIGH_CLICK.getData() : EnumCommon.LOW_CLICK.getData();
        if (likeStatus <= clickRestrain) {
            // 可以进行点赞,并且设置过期时间为60s
            redisTemplate.expire(userKey, EnumLike.OUT_TIME.getValue(), TimeUnit.SECONDS);
            return true;
        } else {
            // 不能设置
            return false;
        }
    }

    @Override
    public void stringSetValue(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
}
