package com.ccl.wx.mapper;

import com.ccl.wx.entity.CircleInfo;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/6 15:36
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
     * 根据圈子类型查找圈子
     *
     * @param circleLocation
     * @return
     */
    List<CircleInfo> findAllByCircleLocation(@Param("circleLocation") Integer circleLocation);

    /**
     * 根据关键词模糊查询数据
     *
     * @param likeCircleName 圈子查询关键词
     * @return
     */
    List<CircleInfo> selectAllLikeAndCircleName(@Param("likeCircleName") String likeCircleName);

    /**
     * 根据关键词和圈子位置查询圈子数据
     *
     * @param likeCircleName 圈子查询关键词
     * @param circleLocation 圈子位置信息
     * @return
     */
    List<CircleInfo> selectAllByCircleNameLikeAndCircleLocation(@Param("likeCircleName") String likeCircleName, @Param("circleLocation") Integer circleLocation);

    /**
     * 挑选圈子的所有名称
     *
     * @return
     */
    List<String> selectAllCircleName();

    /**
     * 更新圈子成员
     *
     * @param circleId
     * @param value    +1 -1
     * @return
     */
    int updateCircleMemberByCircleId(@Param("circleId") Long circleId, @Param("value") Integer value);
}