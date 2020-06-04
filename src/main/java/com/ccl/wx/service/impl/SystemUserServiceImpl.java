package com.ccl.wx.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.ccl.wx.mapper.SystemUserMapper;
import com.ccl.wx.entity.SystemUser;
import com.ccl.wx.service.SystemUserService;
/**
 * @author  褚超亮
 * @date  2020/6/3 22:55
 */

@Service
public class SystemUserServiceImpl implements SystemUserService{

    @Resource
    private SystemUserMapper systemUserMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return systemUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SystemUser record) {
        return systemUserMapper.insert(record);
    }

    @Override
    public int insertSelective(SystemUser record) {
        return systemUserMapper.insertSelective(record);
    }

    @Override
    public SystemUser selectByPrimaryKey(Integer id) {
        return systemUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SystemUser record) {
        return systemUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SystemUser record) {
        return systemUserMapper.updateByPrimaryKey(record);
    }

}
