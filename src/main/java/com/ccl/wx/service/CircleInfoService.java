package com.ccl.wx.service;

import com.ccl.wx.entity.CircleInfo;

import java.util.List;

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

    /**
     * 获取圈子内的主页面加载的全部内容
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return 圈子全部内容json字符串
     */
    String getCircleIndexAllContent(String userId, Integer circleId);

    /**
     * 根据关键词模糊查询数据
     *
     * @param likeCircleName 圈子查询关键词
     * @return
     */
    List<CircleInfo> selectAllLikeAndCircleName(String likeCircleName);

    /**
     * 挑选圈子的所有名称
     *
     * @return
     */
    List<String> selectAllCircleName();

    /**
     * 根据圈子类型查询圈子数据
     *
     * @param tid
     * @return
     */
    List<CircleInfo> findAllByCircleLocation(int tid);

    /**
     * 根据圈子类型和关键字查询圈子数据
     *
     * @param keyword 关键字
     * @param tid     圈子类型
     * @return
     */
    List<CircleInfo> selectAllByCircleNameLikeAndCircleLocation(String keyword, int tid);
}

