package com.ccl.wx.mapper;

import com.ccl.wx.entity.UserDiary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/7 10:15
 */

@Mapper
public interface UserDiaryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDiary record);

    int insertSelective(UserDiary record);

    UserDiary selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDiary record);

    int updateByPrimaryKey(UserDiary record);

    /**
     * 根据圈子id 和日志状态获取日志总数
     *
     * @param circleId    圈子id
     * @param diaryStatus 日志状态
     * @return
     */
    Long countByCircleIdAndDiaryStatus(@Param("circleId") Long circleId, @Param("diaryStatus") List<Integer> diaryStatus);

    /**
     * 根据圈子id 和用户id 获取所有的日志总数
     *
     * @param circleId    圈子id
     * @param userId      用户id
     * @param diaryStatus 日记状态列表
     * @return
     */
    Long countByCircleIdAndUserId(@Param("circleId") Long circleId, @Param("userId") String userId, @Param("diaryStatus") List<Integer> diaryStatus);

    /**
     * 拼接日记图片字符串
     *
     * @param id        日志id
     * @param imagePath 图片地址
     * @param flag
     * @return
     */
    int concatImage(@Param("id") Long id, @Param("imagePath") String imagePath, @Param("flag") Boolean flag);

    /**
     * 根据圈子id查找日记
     *
     * @param circleId 圈子id
     * @return
     */
    List<UserDiary> selectAllByCircleId(@Param("circleId") Long circleId);

    /**
     * 根据圈子id查找日记 并且分页
     *
     * @param circleId   圈子id
     * @param start      起始数
     * @param pageNumber 每一页的数据
     * @return
     */
    List<UserDiary> selectAllByCircleIdAndLimit(@Param("circleId") Long circleId, @Param("start") Integer start, @Param("pageNumber") Integer pageNumber, @Param("diaryStatus") List<Integer> diaryStatus);

    /**
     * 根据圈子id和用户id查找日记信息 分页
     *
     * @param circleId   圈子id
     * @param userId     用户id
     * @param start      起始数
     * @param pageNumber 每一页的数据
     * @return
     */
    List<UserDiary> selectAllByCircleIdAndUserIdAndLimit(@Param("circleId") Long circleId, @Param("userId") String userId, @Param("start") Integer start, @Param("pageNumber") Integer pageNumber);

    /**
     * 根据日志状态查询日志的全部id
     *
     * @param diaryStatus
     * @return
     */
    List<Long> selectIdByDiaryStatus(@Param("diaryStatus") Integer diaryStatus);

    /**
     * 更新日志的浏览量 在原来的基础上添加
     *
     * @param updatedDiaryBrowse
     * @param id
     * @return
     */
    int updateDiaryBrowseById(@Param("updatedDiaryBrowse") Integer updatedDiaryBrowse, @Param("id") Long id);

    /**
     * 根据主题id和日志状态查询日志信息
     *
     * @param themeId
     * @param diaryStatus
     * @return
     */
    List<UserDiary> selectAllByThemeIdAndDiaryStatus(@Param("themeId") Integer themeId, @Param("diaryStatus") Integer diaryStatus);

    /**
     * 根据主题id查询全部日志信息
     *
     * @param themeId
     * @return
     */
    List<UserDiary> selectAllByThemeId(@Param("themeId") Integer themeId);

    /**
     * 更新某个主题下的日志状态
     *
     * @param updatedDiaryStatus
     * @param themeId
     * @return
     */
    int updateDiaryStatusByThemeId(@Param("updatedDiaryStatus") Integer updatedDiaryStatus, @Param("themeId") Integer themeId);

    /**
     * 查询某位用户某一天的日志信息
     *
     * @param userId              用户id
     * @param circleId            圈子id
     * @param likeDiaryCreatetime 日记创建时间 yyyy-MM-dd
     * @param diaryStatus         日志状态 ！不等于
     * @return
     */
    List<UserDiary> selectAllByUserIdAndCircleIdAndDiaryCreatetimeLikeAndDiaryStatus(@Param("userId") String userId, @Param("circleId") Long circleId, @Param("likeDiaryCreatetime") String likeDiaryCreatetime, @Param("diaryStatus") Integer diaryStatus);

    /**
     * 根据主题和用户id查询当天删除日记的总数
     *
     * @param userId
     * @param circleId
     * @return
     */
    int countByUserIdAndCircleId(@Param("userId") String userId, @Param("circleId") Integer circleId);

    /**
     * 判断此主题今天用户是否发表过日记 获得总数 如果为0 则未发表，否则发表过
     *
     * @param userId
     * @param circleId
     * @param themeId
     * @return
     */
    int countByUserIdAndCircleIdAndThemeId(@Param("userId") String userId, @Param("circleId") Long circleId, @Param("themeId") Integer themeId);

    /**
     * 查看用户的日志信息总数(全部日志包括非法日志和删除日志)
     *
     * @param userId              用户id
     * @param circleId            圈子id
     * @param likeDiaryCreatetime 要查询的时间
     * @return
     */
    int countByUserIdAndCircleIdAndDiaryCreatetimeLike(@Param("userId") String userId, @Param("circleId") Long circleId, @Param("likeDiaryCreatetime") String likeDiaryCreatetime);

    /**
     * 获取圈子的某一天的打卡人数
     *
     * @param circleId 圈子id
     * @param date     日期 格式(yyyyMMdd)
     * @return
     */
    int countThemeUserNumberByDate(@Param("circleId") Long circleId, @Param("date") String date);

    /**
     * 获取圈子某一天的所有打卡用户
     *
     * @param circleId 圈子id
     * @param date     打卡日期
     * @return
     */
    List<String> selectUserSignInIdByDate(@Param("circleId") Long circleId, @Param("date") String date);

    /**
     * 查询主题id和圈子id下的全部日志总数
     *
     * @param themeId  主题id
     * @param circleId 圈子id
     * @return
     */
    int countByThemeIdAndDiaryStatus(@Param("themeId") Integer themeId, @Param("circleId") Integer circleId);

    /**
     * 根据圈子id和日记状态查询全部日志信息
     *
     * @param circleId    圈子id
     * @param diaryStatus 日志状态
     * @return
     */
    List<UserDiary> selectAllByCircleIdAndDiaryStatus(@Param("circleId") Long circleId, @Param("diaryStatusList") List<Integer> diaryStatus);

    /**
     * 查询出某位用户某一天是否在某一个主题下打卡
     *
     * @param circleId    圈子id
     * @param userId      用户id
     * @param date        哪一天（20200311）
     * @param diaryStatus 日志状态
     * @return
     */
    List<UserDiary> selectByCircleIdAndUserIdAndDiaryCreatetimeAndDiaryStatus(@Param("circleId") Long circleId, @Param("userId") String userId, @Param("diaryCreatetime") String date, @Param("diaryStatus") List<Integer> diaryStatus, @Param("themeId") Integer themeId);
}