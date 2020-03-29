package com.ccl.wx.service;

import com.ccl.wx.dto.UserDiaryDTO;
import com.ccl.wx.entity.UserDiary;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/2/18 10:20
 */
public interface UserDiaryService {

    /**
     * 根据id删除日志信息
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入日志信息（全部参数都存在）
     *
     * @param record
     * @return
     */
    int insert(UserDiary record);

    /**
     * 插入日志信息（部分日志参数就可以）
     *
     * @param record
     * @return
     */
    int insertSelective(UserDiary record);

    /**
     * 根据id查询日志信息
     *
     * @param id
     * @return
     */
    UserDiary selectByPrimaryKey(Long id);

    /**
     * 更新部分日志数据
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UserDiary record);

    /**
     * 更新日志全部数据
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(UserDiary record);

    /**
     * 编辑更新圈子日记
     * 日记id，日记内容，日记图片，日记权限，日记地址
     *
     * @param userDiaryDTO
     * @return
     */
    String updateCircleDiaryContent(UserDiaryDTO userDiaryDTO);

    /**
     * 删除用户日志信息
     *
     * @param diaryid 日记id
     * @return
     */
    String deleteUserDiaryInfo(Long diaryid);

    /**
     * 判断用户活跃度减去几 -1 还是 -7
     *
     * @param createTime 日志创建日期
     * @param dateList   用户日期列表
     * @return
     */
    String judgeUserVitality(String createTime, List<String> dateList);

    /**
     * 删除用户相应的打卡日期
     *
     * @param createTime 日志创建日期
     * @param dateList   用户日期列表
     * @return 返回删除之后的打卡列表
     */
    String deleteUserClockDate(String createTime, List<String> dateList);

    /**
     * 获取用户连续打卡天数
     *
     * @param createTime 日志创建时间
     * @param dateList   用户日期列表
     * @param days       用户连续打卡天数
     * @return
     */
    Integer getUserContinuousClockInDays(String createTime, Integer days, List<String> dateList);

    /**
     * 根据分隔符 来获取某个字符串在列表中的下标
     *
     * @param createTime 需要判断的字符串
     * @param dateList   字符串列表
     * @param splitStr   分隔符
     * @return
     */
    Integer getTargetStrIndexOf(String createTime, List<String> dateList, String splitStr);

    /**
     * 用户发表日记
     *
     * @param userDiary 前端传输来的用户打卡数据
     * @return
     */
    String publishUserDiary(UserDiary userDiary);

    /**
     * 保存日志图片
     *
     * @param userId
     * @param id
     * @param image
     * @return
     */
    String saveDiaryImage(MultipartFile image, String userId, Long id);

    /**
     * 根据日志id获取圈子信息
     *
     * @param diaryId 日志id
     * @return
     */
    String getCircleDiaryById(Long diaryId);

    /**
     * 增加浏览量
     *
     * @param userId  用户id
     * @param diaryId 日记id
     */
    void addDiaryBrowseNumber(String userId, Long diaryId);

    /**
     * 持久化日记浏览量到数据库中
     *
     * @return
     */
    void saveDiaryBrowseNumber();

    /**
     * 根据日记主题id查询日记信息
     *
     * @param themeId
     * @return
     */
    List<UserDiary> getUserDiaryByThemeId(Integer themeId);

    /**
     * 根据日志所在的主题更新日志状态
     *
     * @param themeId     日记的主题
     * @param diaryStatus 日志状态
     * @return
     */
    int updateDiaryStatusByThemeId(Integer themeId, Integer diaryStatus);

    /**
     * 判断当天用户的主题所对的的日记是否全部删除
     * 为空 true
     * 不为空 false
     *
     * @param userId          用户id
     * @param circleId        圈子id
     * @param diaryCreateTime 日记创建时间
     * @param diaryStatus     日志状态
     * @return
     */
    boolean judgeThemeIdIsNull(String userId, Long circleId, String diaryCreateTime, Integer diaryStatus);

    /**
     * 获取圈子中某一天的打卡人数
     *
     * @param circleId 圈子id
     * @param date     日期
     * @return
     */
    int countThemeUserNumberByDate(Long circleId, Date date);

    /**
     * 获取主题id下的正常状态日志总数
     *
     * @param themeId  主题id
     * @param circleId 圈子id
     * @return
     */
    int countByThemeIdAndDiaryStatus(Integer themeId, Integer circleId);

    /**
     * 根据日记id和日记状态查询日志
     *
     * @param value 日记状态
     * @return
     */
    List<Long> selectIdByDiaryStatus(Integer value);
}