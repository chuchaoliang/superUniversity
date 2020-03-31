package com.ccl.wx.service.impl;

import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.entity.UserLike;
import com.ccl.wx.enums.EnumLike;
import com.ccl.wx.enums.EnumRedis;
import com.ccl.wx.mapper.UserLikeMapper;
import com.ccl.wx.service.CircleRedisService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.service.UserInfoService;
import com.ccl.wx.service.UserLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private UserDiaryService userDiaryService;

    @Resource
    private UserLikeMapper userLikeMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private CircleRedisService circleRedisService;

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
    public List<UserInfo> getAllLikeUserNickName(String userId, String circleId, Long diaryId) {
        // 先判断缓存中是否存在
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        UserLike userLike = userLikeMapper.selectByTypeId(diaryId);
        Integer userLikeStatus = circleRedisService.getUserLikeStatus(userId, circleId, diaryId);
        if (userLike != null && !StringUtils.isEmpty(userLike.getLikeUserid())) {
            // 数据库中存在此数据
            List<String> userids = new ArrayList<>(Arrays.asList(userLike.getLikeUserid().split(",")));
            // 用户id列表去重
            List<String> fuserids = userids.stream().distinct().collect(Collectors.toList());
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
        return userInfos;
    }

    @Override
    public String getAllLikeUserNickName(List<UserInfo> userInfos) {
        StringBuffer stringBuffer = new StringBuffer();
        if (!userInfos.isEmpty()) {
            for (UserInfo userInfo : userInfos) {
                stringBuffer.append(userInfo.getNickname()).append(",");
            }
            return String.valueOf(stringBuffer.deleteCharAt(stringBuffer.length() - 1));
        } else {
            return "";
        }
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
}
