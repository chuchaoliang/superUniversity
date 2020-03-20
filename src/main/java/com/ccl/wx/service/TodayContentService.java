package com.ccl.wx.service;

import com.ccl.wx.entity.TodayContent;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 褚超亮
 * @date 2020/3/5 21:35
 */

public interface TodayContentService {

    int deleteByPrimaryKey(Long id);

    int insert(TodayContent record);

    int insertSelective(TodayContent record);

    TodayContent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TodayContent record);

    int updateByPrimaryKey(TodayContent record);

    /**
     * 保存每日内容图片
     *
     * @param image  图片信息
     * @param userId 用户id
     * @param id     每日内容id
     * @return
     */
    String saveEverydayImage(MultipartFile image, String userId, Long id);

    /**
     * 根据圈子主题的id获取主题信息
     *
     * @param id
     * @return
     */
    String getTodayContentById(Long id);

    /**
     * 保存今日内容
     *
     * @param todayContent
     * @return
     */
    String saveCircleTheme(TodayContent todayContent);

    /**
     * 根据圈子主题id删除主题信息
     *
     * @param themeId 主题id
     * @return
     */
    String deleteCircleTheme(Long themeId);

    /**
     * 获取圈子全部主题
     *
     * @param circleId
     * @param sign
     * @return
     */
    String getCircleTheme(Long circleId, String sign);

    /**
     * 保存圈子主题标题图片
     *
     * @param image
     * @param userId
     * @param id
     * @return
     */
    String saveThemeHeadImage(MultipartFile image, String userId, Long id);

    /**
     * 增加圈子主题浏览量
     *
     * @param userId
     * @param themeId
     * @return
     */
    void addCircleThemeBrowse(String userId, Long themeId);

    /**
     * 根据圈子id删除状态为删除状态的圈子主题
     */
    void deleteCircleThemeFormDatabase();

    /**
     * 根据圈子id 和！=主题状态查找存在的主题总数
     *
     * @param circleId
     * @param status
     * @return
     */
    int countByCircleIdAndContentStatus(Long circleId, Integer status);

    /**
     * 根据圈子id获取全部主题信息
     *
     * @param circleId
     * @return
     */
    String selectAllThemeByCircleIdPage(Long circleId, String userId, Integer page);

    /**
     * 根据圈子id获取全部主题id 不分页
     *
     * @param circleId
     * @param userId
     * @return
     */
    String selectAllThemeByCircleId(Long circleId, String userId);
}





