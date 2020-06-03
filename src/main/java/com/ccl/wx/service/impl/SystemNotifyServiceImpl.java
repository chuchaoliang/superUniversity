package com.ccl.wx.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.ccl.wx.entity.SystemNotify;
import com.ccl.wx.mapper.SystemNotifyMapper;
import com.ccl.wx.service.SystemNotifyService;
/**
 * @author  褚超亮
 * @date  2020/6/3 22:28
 */

@Service
public class SystemNotifyServiceImpl implements SystemNotifyService{

    @Resource
    private SystemNotifyMapper systemNotifyMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return systemNotifyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SystemNotify record) {
        return systemNotifyMapper.insert(record);
    }

    @Override
    public int insertSelective(SystemNotify record) {
        return systemNotifyMapper.insertSelective(record);
    }

    @Override
    public SystemNotify selectByPrimaryKey(Integer id) {
        return systemNotifyMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SystemNotify record) {
        return systemNotifyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SystemNotify record) {
        return systemNotifyMapper.updateByPrimaryKey(record);
    }

}
