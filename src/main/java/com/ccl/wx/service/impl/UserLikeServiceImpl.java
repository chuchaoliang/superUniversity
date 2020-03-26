package com.ccl.wx.service.impl;

import com.ccl.wx.entity.UserLike;
import com.ccl.wx.mapper.UserLikeMapper;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.service.UserLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
}
