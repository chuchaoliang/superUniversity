package com.ccl.wx.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.ccl.wx.entity.NotifyContent;
import com.ccl.wx.mapper.NotifyContentMapper;
import com.ccl.wx.service.NotifyContentService;
/**
 * @author  褚超亮
 * @date  2020/6/3 22:29
 */

@Service
public class NotifyContentServiceImpl implements NotifyContentService{

    @Resource
    private NotifyContentMapper notifyContentMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return notifyContentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(NotifyContent record) {
        return notifyContentMapper.insert(record);
    }

    @Override
    public int insertSelective(NotifyContent record) {
        return notifyContentMapper.insertSelective(record);
    }

    @Override
    public NotifyContent selectByPrimaryKey(Integer id) {
        return notifyContentMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(NotifyContent record) {
        return notifyContentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(NotifyContent record) {
        return notifyContentMapper.updateByPrimaryKey(record);
    }

}
