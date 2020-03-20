package com.ccl.wx.service;

import com.ccl.wx.entity.CircleInfo;

/**
 * @author 褚超亮
 * @date 2020/3/6 14:22
 */

public interface CircleInfoService {


    int deleteByPrimaryKey(Long circleId);

    int insert(CircleInfo record);

    int insertSelective(CircleInfo record);

    CircleInfo selectByPrimaryKey(Long circleId);

    int updateByPrimaryKeySelective(CircleInfo record);

    int updateByPrimaryKey(CircleInfo record);

}

