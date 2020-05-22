package com.ccl.wx.service.impl;

import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.entity.UserLike;
import com.ccl.wx.enums.diary.EnumLike;
import com.ccl.wx.enums.EnumRedis;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.mapper.UserLikeMapper;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.UserInfoService;
import com.ccl.wx.service.UserLikeService;
import com.ccl.wx.vo.DiaryLikeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 褚超亮
 * @date 2020/3/25 19:33
 */

@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class UserLikeServiceImpl implements UserLikeService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserLikeMapper userLikeMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private JoinCircleService joinCircleService;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userLikeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserLike record) {
        return userLikeMapper.insert(record);
    }

    @Override
    public int insertSelective(UserLike record) {
        return userLikeMapper.insertSelective(record);
    }

    @Override
    public UserLike selectByPrimaryKey(Long id) {
        return userLikeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserLike record) {
        return userLikeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserLike record) {
        return userLikeMapper.updateByPrimaryKey(record);
    }

    @Override
    public UserLike selectByTypeId(Long typeId) {
        return userLikeMapper.selectByTypeId(typeId);
    }

    @Override
    public void concatLikeUserId(Long typeId, String userId) {
        userLikeMapper.concatLikeUserId(typeId, userId);
    }

    @Override
    public List<Long> selectIdByTypeId(Long typeId) {
        return userLikeMapper.selectIdByTypeId(typeId);
    }

    @Override
    public List<DiaryLikeVO> getAllLikeUserNickName(String userId, String circleId, Long diaryId) {
        // 先判断缓存中是否存在
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        UserLike userLike = userLikeMapper.selectByTypeId(diaryId);
        Integer userLikeStatus = getUserLikeStatus(userId, circleId, diaryId);
        if (userLike != null && !StringUtils.isEmpty(userLike.getLikeUserid())) {
            // 数据库中存在此数据
            List<String> userids = new ArrayList<>(Arrays.asList(userLike.getLikeUserid().split(",")));
            // 用户id列表去重
            List<String> fuserids = userids.stream().distinct().limit(EnumLike.USER_LIKE_NUMBER.getValue()).collect(Collectors.toList());
            boolean loginUserLikeCondition = (userLikeStatus != null && userLikeStatus.equals(EnumLike.LIKE_SUCCESS.getValue()))
                    || (userLikeStatus == null && fuserids.contains(userId));
            // 删除本用户
            fuserids.removeIf(s -> s.equals(userId));
            // 判断删除本用户之后是否为空
            if (!fuserids.isEmpty()) {
                for (String likeUserId : fuserids) {
                    UserInfo userInfo = userInfoService.selectByPrimaryKey(likeUserId);
                    userInfos.add(userInfo);
                }
            }
            // 查找本用户（访问程序的用户）
            UserInfo loginUserInfo = userInfoService.selectByPrimaryKey(userId);
            // 添加本用户点赞条件
            if (loginUserLikeCondition) {
                userInfos.add(0, loginUserInfo);
            }
        } else {
            // 数据库中不存在 查看缓存中是否存在
            if (userLikeStatus != null && userLikeStatus.equals(EnumLike.LIKE_SUCCESS.getValue())) {
                userInfos.add(0, userInfoService.selectByPrimaryKey(userId));
            }
        }
        ArrayList<DiaryLikeVO> diaryLikeVOS = new ArrayList<>();
        userInfos.forEach(like -> {
            like.setNickname(joinCircleService.getUserJoinCircleNickname(like.getId(), Long.valueOf(circleId)));
            DiaryLikeVO diaryLikeVO = new DiaryLikeVO();
            BeanUtils.copyProperties(like, diaryLikeVO);
            diaryLikeVOS.add(diaryLikeVO);
        });
        return diaryLikeVOS;
    }

    @Override
    public Boolean judgeDiaryLikeStatus(String userId, String circleId, Long diaryId) {
        String hash = EnumRedis.LIKE_PREFIX.getValue() + userId;
        String key = circleId + EnumRedis.REDIS_JOINT.getValue() + diaryId;
        // 先判断缓存中是否存在
        if (redisTemplate.opsForHash().hasKey(hash, key)) {
            // 缓存中存在
            if (redisTemplate.opsForHash().get(hash, key).equals(EnumLike.LIKE_SUCCESS.getValue())) {
                // 状态为点赞状态1
                return true;
            } else {
                // 状态为取消点赞状态0
                return false;
            }
        } else {
            // 缓存为空
            UserLike userLike = userLikeMapper.selectByTypeId(diaryId);
            if (userLike != null) {
                // 数据库不为空
                if (userLike.getLikeStatus().equals(EnumLike.LIKE_SUCCESS.getValue())) {
                    // 为生效状态
                    List<String> userIds = Arrays.asList(userLike.getLikeUserid().split(","));
                    if (userIds.contains(userId)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                // 数据库为空
                return false;
            }
        }
    }

    @Override
    public String saveLikeRedis(String userId, String circleId, String diaryId) {
        boolean likeStatus = judgeLikeStatus(userId);
        if (likeStatus) {
            // 用户点赞状态存储到redis中 like::用户id 键 圈子id::日志id 值 1 点赞 0 取消点赞
            String key = circleId + EnumRedis.REDIS_JOINT.getValue() + diaryId;
            redisTemplate.opsForHash().put(EnumRedis.LIKE_PREFIX.getValue() + userId, key, EnumLike.LIKE_DIARY.getValue());
            // 点赞数目 +1 若状态已经是1 则点赞数目不增加
            incrementLikedCount(diaryId);
            return EnumResultStatus.SUCCESS.getValue();
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public String unLikeFromRedis(String userId, String circleId, String diaryId) {
        String key = circleId + EnumRedis.REDIS_JOINT.getValue() + diaryId;
        redisTemplate.opsForHash().put(EnumRedis.LIKE_PREFIX.getValue() + userId, key, EnumLike.LIKE_FAIL.getValue());
        // 点赞数目 -1
        decrementLikeCount(diaryId);
        return EnumResultStatus.SUCCESS.getValue();
    }

    @Override
    public String deleteLikeFromRedis(String userId, String circleId, String diaryId) {
        String key = circleId + EnumRedis.REDIS_JOINT.getValue() + diaryId;
        // 删除redis缓存
        redisTemplate.opsForHash().delete(EnumRedis.LIKE_PREFIX.getValue() + userId, key);
        // 点赞数目 -1
        decrementLikeCount(diaryId);
        return EnumResultStatus.SUCCESS.getValue();
    }

    @Override
    public String incrementLikedCount(String diaryId) {
        if (!redisTemplate.hasKey(EnumRedis.LIKE_SUM_PREFIX.getValue() + diaryId)) {
            redisTemplate.opsForValue().set(EnumRedis.LIKE_SUM_PREFIX.getValue() + diaryId, 1);
        } else {
            redisTemplate.opsForValue().increment(EnumRedis.LIKE_SUM_PREFIX.getValue() + diaryId, 1);
        }
        return EnumResultStatus.SUCCESS.getValue();
    }

    @Override
    public String decrementLikeCount(String diaryId) {
        if (!redisTemplate.hasKey(EnumRedis.LIKE_SUM_PREFIX.getValue() + diaryId)) {
            redisTemplate.opsForValue().set(EnumRedis.LIKE_SUM_PREFIX.getValue() + diaryId, -1);
        } else {
            redisTemplate.opsForValue().increment(EnumRedis.LIKE_SUM_PREFIX.getValue() + diaryId, -1);
        }
        return EnumResultStatus.SUCCESS.getValue();
    }

    @Override
    public Boolean judgeLikeStatus(String userId) {
        // 值+1
        String userKey = EnumRedis.LIKE_STATUS_PREFIX.getValue() + userId;
        redisTemplate.opsForValue().increment(userKey);
        int likeStatus = Integer.parseInt(String.valueOf(redisTemplate.opsForValue().get(userKey)));
        if (likeStatus <= EnumLike.SUM_LIKE.getValue()) {
            // 可以进行点赞,并且设置过期时间为60s
            redisTemplate.expire(userKey, EnumLike.OUT_TIME.getValue(), TimeUnit.SECONDS);
            return true;
        } else {
            // 不能设置
            return false;
        }
    }

    @Override
    public Integer getUserLikeStatus(String userId, String circleId, Long diaryId) {
        String hash = EnumRedis.LIKE_PREFIX.getValue() + userId;
        String key = circleId + EnumRedis.REDIS_JOINT.getValue() + diaryId;
        Boolean keyStatus = redisTemplate.opsForHash().hasKey(hash, key);
        if (keyStatus) {
            // 此键存在
            return Integer.parseInt(String.valueOf(redisTemplate.opsForHash().get(hash, key)));
        } else {
            return null;
        }
    }
}
