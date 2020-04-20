package com.ccl.wx.mapper;

import com.ccl.wx.entity.JoinCircle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;import java.util.List;import java.util.Map;

/**
 * @author 褚超亮
 * @date 2020/4/20 10:20
 */

@Mapper
public interface JoinCircleMapper {
    int deleteByPrimaryKey(@Param("circleId") Long circleId, @Param("userId") String userId);

    int insert(JoinCircle record);

    int insertSelective(JoinCircle record);

    JoinCircle selectByPrimaryKey(@Param("circleId") Long circleId, @Param("userId") String userId);

    int updateByPrimaryKeySelective(JoinCircle record);

    int updateByPrimaryKey(JoinCircle record);

    /**
     * 根据条件查询信息
     *
     * @param joinCircle
     * @return
     */
    List<JoinCircle> selectByAll(JoinCircle joinCircle);

    /**
     * 根据圈子id 和用户状态来查询圈子中人数
     *
     * @param circleId
     * @param userStatus
     * @return
     */
    Integer countByCircleIdAndUserStatus(@Param("circleId") Long circleId, @Param("userStatus") Integer userStatus);

    /**
     * 根据圈子id获取全部的圈子用户id
     *
     * @param circleId 圈子的id
     * @return
     */
    List<String> selectUserIdByCircleId(@Param("circleId") Long circleId);

    /**
     * 更新圈子状态
     *
     * @param circleId       圈子id
     * @param userSignStatus 用户部分打卡状态
     * @return
     */
    int updateByCircleIdAndUserSignStatus(@Param("circleId") Long circleId, @Param("userSignStatus") Integer userSignStatus);

    /**
     * 拼接用户签到日历
     *
     * @param circleid 圈子id
     * @param calendar 用户打卡日历
     * @param userid   用户id
     * @param flag
     * @return
     */
    int concatClockInCalendar(@Param("circleid") String circleid, @Param("userid") String userid, @Param("calendar") String calendar, @Param("flag") Boolean flag);

    /**
     * 拼接圈子主题字符串
     *
     * @param circleId
     * @param userId
     * @param themeId
     * @param flag
     * @return
     */
    int concatCircleTheme(@Param("circleId") Long circleId, @Param("userId") String userId, @Param("themeId") String themeId, @Param("flag") Boolean flag);

    /**
     * 获取用户的所有打卡天数
     *
     * @param userid 用户id
     * @return
     */
    int sumUserSigninDayByUserid(@Param("userid") String userid);

    /**
     * 获取用户的能量球（积分）
     *
     * @param userid 用户id
     * @return
     */
    int sumUserVitalityByUserid(@Param("userid") String userid);

    /**
     * 查找用户加入的圈子或者创建的圈子
     *
     * @param userId         用户id
     * @param userPermission 用户加入的圈子权限
     * @return
     */
    List<JoinCircle> selectAllByUserIdAndUserPermission(@Param("userId") String userId, @Param("userPermission") List<Integer> userPermission);

    /**
     * 用户活跃度排行榜
     *
     * @param circleId   圈子id
     * @param start      起始值
     * @param pageNumber 每页个数
     * @return
     */
    List<Map> getUserVitalityRanking(@Param("circleId") Long circleId, @Param("start") Integer start, @Param("pageNumber") Integer pageNumber);

    /**
     * 用户连续打卡排行榜
     *
     * @param circleId   圈子id
     * @param start      起始值
     * @param pageNumber 每页个数
     * @return
     */
    List<Map> getUserSignInRanking(@Param("circleId") Long circleId, @Param("start") Integer start, @Param("pageNumber") Integer pageNumber);

    /**
     * 获取活跃度排行榜中的某个用户的信息
     *
     * @param circleId   圈子id
     * @param userId     用户id
     * @param start      从0开始
     * @param pageNumber 结束 200
     * @return
     */
    List<Map> getUserVitalityRankingInfo(@Param("circleId") Long circleId, @Param("userId") String userId, @Param("start") Integer start, @Param("pageNumber") Integer pageNumber);

    /**
     * 获取用户连续打卡排行榜中的某个用户信息
     *
     * @param circleId   圈子id
     * @param userId     用户id
     * @param start      从0开始
     * @param pageNumber 结束200
     * @return
     */
    List<Map> getUserSignInRankingInfo(@Param("circleId") Long circleId, @Param("userId") String userId, @Param("start") Integer start, @Param("pageNumber") Integer pageNumber);

    /**
     * 获取用户在圈子中的活跃度信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    List<Map> getUserVitalityInfo(@Param("circleId") Long circleId, @Param("userId") String userId);

    /**
     * 获取用户所在圈子中连续打卡信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    List<Map> getUserSignInInfo(@Param("circleId") Long circleId, @Param("userId") String userId);

    /**
     * 打卡统计：获取用户某一天成功打卡用户信息
     *
     * @param circleId 圈子id
     * @param date     日期
     * @param start    开始页
     * @param page     第几页
     * @return
     */
    List<Map> getUserSuccessSignInByDate(@Param("circleId") Long circleId, @Param("date") String date, @Param("start") Integer start, @Param("page") Integer page);

    /**
     * 打卡统计：获取用户某一天未打卡用户信息
     *
     * @param circleId 圈子id
     * @param date     日期
     * @param start    开始页
     * @param page     第几页
     * @return
     */
    List<Map> getUserFailSignInByDate(@Param("circleId") Long circleId, @Param("date") String date, @Param("start") Integer start, @Param("page") Integer page);

    /**
     * 获取圈子中所有的管理人员信息
     *
     * @param circleId       圈子id
     * @param userPermission 用户权限列表
     * @return
     */
    List<JoinCircle> selectUserIdByUserPermission(@Param("circleId") Integer circleId, @Param("userPermission") List<Integer> userPermission);

    /**
     * 根据圈子id和用户状态得到用户圈子总活跃度
     *
     * @param circleId   圈子id
     * @param userStatus 用户状态
     * @return
     */
    Long sumUserVitalityByCircleIdAndUserStatus(@Param("circleId") Long circleId, @Param("userStatus") Integer userStatus);
}