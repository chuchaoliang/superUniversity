package com.ccl.wx.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.ccl.wx.mapper.UserNotifyMapper;
import com.ccl.wx.entity.UserNotify;
import com.ccl.wx.service.UserNotifyService;
/**
 * @author  褚超亮
 * @date  2020/5/23 22:23
 */

@Service
public class UserNotifyServiceImpl implements UserNotifyService{

    @Resource
    private UserNotifyMapper userNotifyMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userNotifyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserNotify record) {
        return userNotifyMapper.insert(record);
    }

    @Override
    public int insertSelective(UserNotify record) {
        return userNotifyMapper.insertSelective(record);
    }

    @Override
    public UserNotify selectByPrimaryKey(Long id) {
        return userNotifyMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserNotify record) {
        return userNotifyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserNotify record) {
        return userNotifyMapper.updateByPrimaryKey(record);
    }

}
