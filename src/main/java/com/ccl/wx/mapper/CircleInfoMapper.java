package com.ccl.wx.mapper;

import com.ccl.wx.entity.CircleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/4/10 15:42
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
     * @param circleLocation 圈子所在位置
     * @return
     */
    List<CircleInfo> findAllByCircleLocation(@Param("circleLocation") Integer circleLocation);

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
     * 更新圈子中数据
     *
     * @param circleInfo 圈子信息
     * @param circleId   圈子id
     * @param value      需要增加的值
     * @return
     */
    int updateCircleData(@Param("circleInfo") CircleInfo circleInfo, @Param("circleId") Long circleId, @Param("value") Integer value);

    /**
     * 悲观锁
     *
     * @param circleId
     * @return
     */
    List<CircleInfo> selectByCircleId(@Param("circleId") Long circleId);

    /**
     * 根据圈子id更新圈子主题总数
     *
     * @param circleId 圈子id
     * @param value    需要加或者减的值 + value - value
     * @return
     */
    Integer updateThemeNumberByCircleId(@Param("circleId") Long circleId, @Param("value") Integer value);
}