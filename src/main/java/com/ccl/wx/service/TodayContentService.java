package com.ccl.wx.service;

import com.ccl.wx.dto.CircleTodayContentDTO;
import com.ccl.wx.entity.TodayContent;
import com.ccl.wx.vo.CircleThemeVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

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
     * @param themeId  主题id
     * @param circleId 圈子id
     * @return
     */
    String selectCircleThemeInfoById(Long themeId, Long circleId);

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
     * @param userId   用户id
     * @param page     第几页
     * @param signIn   是否为打卡 是打卡（true）否（false）
     * @return
     */
    String selectAllThemeByCircleIdPage(Long circleId, String userId, Integer page, Boolean signIn, Date date);

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

    /**
     * 获取圈子默认主题
     *
     * @param circleId 圈子id
     * @return
     */
    TodayContent selectCircleDefaultThemeInfo(Long circleId);

    /**
     * 获取圈子首页主题相关信息
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @return
     */
    List<CircleThemeVO> selectAllThemeByCircleHome(String userId, Long circleId);

    /**
     * 编辑更新圈子每日内容
     * 每日内容id，内容，图片路径
     *
     * @param circleTodayContentDTO 圈子每日内容
     * @return
     */
    String updateCircleTodayContent(CircleTodayContentDTO circleTodayContentDTO);

    /**
     * 根据圈子id 和主题id获取主题信息
     *
     * @param circleId 圈子id
     * @param id       主题id
     * @return
     */
    TodayContent selectByCircleIdAndId(Long circleId, Long id);

    /**
     * 保存主题头像
     *
     * @param image
     * @param userId
     * @param id
     * @return
     */
    String saveThemeHeadImage(MultipartFile image, String userId, Long id);

    /**
     * 判断用户某一天是否打卡某一主题
     * true打卡 false未打卡
     *
     * @param circleId    圈子id
     * @param userId      用户id
     * @param themeId     主题id
     * @param date        日期
     * @param diaryStatus 日志状态列表
     * @return
     */
    boolean judgeUserThemeClockByDate(Long circleId, String userId, Date date, List<Integer> diaryStatus, Integer themeId);
}





