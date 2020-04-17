package com.ccl.wx.mapper;

import com.ccl.wx.entity.CircleIntro;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 褚超亮
 * @date 2020/4/16 22:01
 */

@Mapper
public interface CircleIntroMapper {
    int deleteByPrimaryKey(Integer circleId);

    int insert(CircleIntro record);

    int insertSelective(CircleIntro record);

    CircleIntro selectByPrimaryKey(Integer circleId);

    int updateByPrimaryKeySelective(CircleIntro record);

    int updateByPrimaryKey(CircleIntro record);

    /**
     * 拼接圈子简介图片
     *
     * @param image    图片地址
     * @param circleId 圈子id
     * @param flag     图片地址是否为空
     */
    int concatCircleImage(@Param("image") String image, @Param("circleId") Long circleId, @Param("flag") boolean flag);

    /**
     * 拼接圈主简介图片
     *
     * @param image    图片地址
     * @param circleId 圈子id
     * @param flag     图片地址是否为空
     * @return
     */
    int concatUserImage(@Param("image") String image, @Param("circleId") Long circleId, @Param("flag") boolean flag);
}