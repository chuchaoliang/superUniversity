package com.ccl.wx.service.impl;

import com.ccl.wx.entity.UserChat;
import com.ccl.wx.mapper.UserChatMapper;
import com.ccl.wx.service.UserChatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
 * @author  褚超亮
 * @date  2020/6/1 17:11
 */

@Service
public class UserChatServiceImpl implements UserChatService{

    @Resource
    private UserChatMapper userChatMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userChatMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserChat record) {
        return userChatMapper.insert(record);
    }

    @Override
    public int insertSelective(UserChat record) {
        return userChatMapper.insertSelective(record);
    }

    @Override
    public UserChat selectByPrimaryKey(Long id) {
        return userChatMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserChat record) {
        return userChatMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserChat record) {
        return userChatMapper.updateByPrimaryKey(record);
    }

}
