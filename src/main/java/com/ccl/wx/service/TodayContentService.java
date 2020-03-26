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
     * 保存圈子主题内容
     *
     * @param todayContent 今日内容
     * @param userId       用户id
     * @param image        图片地址
     * @return
     */
    String saveCircleThemeContent(TodayContent todayContent, String userId, MultipartFile image);

    /**
     * 根据圈子主题id删除主题信息
     *
     * @param themeId  主题id
     * @param circleId 圈子id
     * @return
     */
    String deleteCircleTheme(Long circleId, Long themeId);

    /**
     * 获取圈子全部主题
     *
     * @param circleId 圈子id
     * @param sign
     * @return
     */
    String getCircleTheme(Long circleId, String sign);

    /**
     * 增加圈子主题浏览量
     *
     * @param userId
     * @param themeId
     * @return
     */
    void addCircleThemeBrowse(String userId, Long themeId);

    /**
     * 删除全部主题状态为删除状态的主题
     */
    void deleteCircleThemeFormDatabase();

    /**
     * 根据圈子id 和！=主题状态查找存在的主题总数
     *
     * @param circleId 圈子id
     * @param status   状态值
     * @return
     */
    int countByCircleIdAndContentStatus(Long circleId, Integer status);

    /**
     * 根据圈子id分页获取全部主题信息
     *
     * @param circleId 圈子id
     * @return
     */
    String selectAllThemeByCircleIdPage(Long circleId, String userId, Integer page);

    /**
     * 根据圈子id获取全部主题信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    String selectAllThemeByCircleId(Long circleId, String userId);

    /**
     * 根据圈子id获取全部主题id 装饰得到用户是否打卡此主题
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    String selectAllThemeByCircleIdDecorate(Long circleId, String userId);

    /**
     * 保存主题音频文件
     *
     * @param userId 用户id
     * @param id     主题id
     * @param voice  音频文件
     * @return
     */
    String saveCircleThemeVoice(String userId, Integer id, MultipartFile voice);

    /**
     * 保存主题视频文件
     *
     * @param userId 用户id
     * @param id     主题id
     * @param video  视频文件
     * @return
     */
    String saveCircleThemeVideo(String userId, Integer id, MultipartFile video);
}





