package com.ccl.wx.mapper;

import com.ccl.wx.entity.TodayContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/12 20:47
 */

@Mapper
public interface TodayContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TodayContent record);

    int insertSelective(TodayContent record);

    TodayContent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TodayContent record);

    int updateByPrimaryKey(TodayContent record);

    /**
     * 根据圈子id获取圈子数据
     *
     * @param circleId 圈子id
     * @return
     */
    List<TodayContent> getAllByCircleId(@Param("circleId") Long circleId);

    /**
     * 根据圈子id 和圈子状态查询圈子数据
     *
     * @param circleId      圈子id
     * @param contentStatus 圈子状态
     * @return
     */
    List<TodayContent> selectByCircleIdAndContentStatus(@Param("circleId") Long circleId, @Param("contentStatus") Integer contentStatus);

    /**
     * 根据每日内容id 拼接图片字符串
     *
     * @param id        每日内容id
     * @param imagePath 图片路径
     * @return
     */
    int concatImage(@Param("id") Long id, @Param("imagePath") String imagePath, @Param("flag") Boolean flag);

    /**
     * 拼接用户阅读人id字符串
     *
     * @param id
     * @param userId
     * @param flag
     */
    int concatUserRead(@Param("id") Long id, @Param("userId") String userId, @Param("flag") Boolean flag);

    /**
     * 根据圈子id查询圈子主题信息
     *
     * @param circleId
     * @return
     */
    List<TodayContent> selectAllByCircleId(@Param("circleId") Long circleId);

    /**
     * 根据日记状态获取圈子主题信息
     *
     * @param contentStatus
     * @return
     */
    List<TodayContent> selectAllByContentStatus(@Param("contentStatus") Integer contentStatus);

    /**
     * 获取圈子的全部状态信息，不等于contentStatus 状态
     *
     * @param circleId      圈子id
     * @param contentStatus 主题状态
     * @return
     */
    Integer countByCircleIdAndContentStatus(@Param("circleId") Long circleId, @Param("contentStatus") Integer contentStatus);

    /**
     * 获取圈子id的主题
     *
     * @param circleId   圈子id
     * @param start      开始数
     * @param pageNumber 每页的数据
     * @return
     */
    List<TodayContent> selectAllByCircleIdOrderByCreateTimeDesc(@Param("circleId") Long circleId, @Param("start") Integer start, @Param("pageNumber") Integer pageNumber);
}