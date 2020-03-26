package com.ccl.wx.service.impl;

import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.mapper.CircleInfoMapper;
import com.ccl.wx.service.CircleInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 褚超亮
 * @date 2020/3/6 14:22
 */

@Service
public class CircleInfoServiceImpl implements CircleInfoService {

    @Resource
    private CircleInfoMapper circleInfoMapper;

    @Override
    public int deleteByPrimaryKey(Long circleId) {
        return circleInfoMapper.deleteByPrimaryKey(circleId);
    }

    @Override
    public int insert(CircleInfo record) {
        return circleInfoMapper.insert(record);
    }

    @Override
    public int insertSelective(CircleInfo record) {
        return circleInfoMapper.insertSelective(record);
    }

    @Override
    public CircleInfo selectByPrimaryKey(Long circleId) {
        return circleInfoMapper.selectByPrimaryKey(circleId);
    }

    @Override
    public int updateByPrimaryKeySelective(CircleInfo record) {
        return circleInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(CircleInfo record) {
        return circleInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateThemeNumberByCircleId(Long circleId, Integer value) {
        return circleInfoMapper.updateThemeNumberByCircleId(circleId, value);
    }
}

