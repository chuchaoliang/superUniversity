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

    /**
     * 根据圈子id 更新圈子主题总数
     *
     * @param circleId 圈子id
     * @param value    需要+ - 的值 + 1 - 1
     * @return
     */
    int updateThemeNumberByCircleId(Long circleId, Integer value);
}

