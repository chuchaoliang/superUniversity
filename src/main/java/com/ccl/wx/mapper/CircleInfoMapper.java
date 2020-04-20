package com.ccl.wx.mapper;

import com.ccl.wx.entity.CircleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/4/16 10:17
 */

@Mapper
public interface CircleInfoMapper {
    int deleteByPrimaryKey(Long circleId);

    int insert(CircleInfo record);

    int insertSelective(CircleInfo record);

    CircleInfo selectByPrimaryKey(Long circleId);

    int updateByPrimaryKeySelective(CircleInfo record);

    int updateByPrimaryKey(CircleInfo record);

    /**
     * 根据条件查询圈子
     *
     * @param circleInfo 条件
     * @return
     */
    List<CircleInfo> selectByAll(CircleInfo circleInfo);

    /**
     * 根据圈子名称查找圈子
     *
     * @param circleName 圈子名称
     * @return
     */
    List<CircleInfo> selectByCircleName(@Param("circleName") String circleName);

    /**
     * 查询圈子
     *
     * @param keyword 关键词
     * @param type    类型（位置）
     * @return
     */
    List<CircleInfo> selectSearchCircleInfo(@Param("keyword") String keyword, @Param("type") Integer type);

    /**
     * 更新圈子中数据
     *
     * @param circleInfo 圈子信息
     * @param circleId   圈子id
     * @param value      需要增加的值
     * @return
     */
    int updateCircleData(@Param("circleInfo") CircleInfo circleInfo, @Param("circleId") Long circleId, @Param("value") Integer value);
}