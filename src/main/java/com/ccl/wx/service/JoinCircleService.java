package com.ccl.wx.service;

import com.ccl.wx.entity.JoinCircle;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 褚超亮
 * @date 2020/3/7 17:19
 */

public interface JoinCircleService {

    int deleteByPrimaryKey(Long circleId, String userId);

    int insert(JoinCircle record);

    int insertSelective(JoinCircle record);

    JoinCircle selectByPrimaryKey(Long circleId, String userId);

    int updateByPrimaryKeySelective(JoinCircle record);

    int updateByPrimaryKey(JoinCircle record);

    /**
     * 获取用户加入圈子昵称
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @return
     */
    String getUserJoinCircleNickname(String userId, Long circleId);

    /**
     * 拼接圈子id
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param themeId  主题id
     * @param flag
     * @return
     */
    int concatCircleTheme(Long circleId, String userId, String themeId, Boolean flag);

    /**
     * 创建主题后，那些全部打卡的主题设置为部分打卡
     * 更新某一日用户全部打卡的用户状态
     *
     * @param circleId       圈子id
     * @param userSignStatus 用户状态
     * @return
     */
    int updateByCircleIdAndUserSignStatus(Long circleId, Integer userSignStatus);

    /**
     * 根据圈子用户状态获取总人数
     *
     * @param circleId
     * @param userStatus
     * @return
     */
    int countByCircleIdAndUserStatus(Long circleId, Integer userStatus);

    /**
     * 获取积分排行榜
     *
     * @param circleId
     * @param page
     * @return
     */
    String getUserVitalityRankingData(Long circleId, Integer page);

    /**
     * 获取连续打卡排行榜
     *
     * @param circleId
     * @param page
     * @return
     */
    String getUserSignInRankingData(Long circleId, Integer page);

    /**
     * 用户活跃排行榜
     *
     * @param circleId   圈子id
     * @param start      开始页数
     * @param pageNumber 每页数量
     * @return
     */
    List<Map> getUserVitalityRanking(Long circleId, Integer start, Integer pageNumber);

    /**
     * 用户连续打卡排行榜
     *
     * @param circleId
     * @param start
     * @param pageNumber
     * @return
     */
    List<Map> getUserSignInRanking(Long circleId, Integer start, Integer pageNumber);

    /**
     * 获取某人的活跃度排名信息
     *
     * @param circleId
     * @param userId
     * @param start
     * @param pageNumber
     * @return
     */
    List<Map> getUserVitalityRankingInfo(Long circleId, String userId, Integer start, Integer pageNumber);

    /**
     * 获取某人的连续打卡排名信息
     *
     * @param circleId   圈子id
     * @param userId     用户id
     * @param start      开始数目
     * @param pageNumber 页数
     * @return
     */
    List<Map> getUserSignInRankingInfo(Long circleId, String userId, Integer start, Integer pageNumber);

    /**
     * 获取用户活跃度排名信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    List<Map> getUserVitalityInfo(Long circleId, String userId);

    /**
     * 获取用户连续打卡排名信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    List<Map> getUserSignInInfo(Long circleId, String userId);

    /**
     * 获取圈子某一天的打卡信息
     *
     * @param circleId 圈子id
     * @param date     日期
     * @return
     */
    String getCircleSignInInfo(Long circleId, Date date);

    /**
     * 根据日期获取某一天打卡用户信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param date     日期
     * @param page     页数
     * @return
     */
    String getUserSignStatisticsSuccessInfo(Long circleId, String userId, Date date, Integer page);

    /**
     * 根据日期获取某一天未打卡的用户信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param date     日期
     * @param page     页数
     * @return
     */
    String getUserSignStatisticsFailInfo(Long circleId, String userId, Date date, Integer page);

    /**
     * 判断某位用户是否为圈子管理人员
     *
     * @param circleId       圈子id
     * @param userPermission 用户权限
     * @param userId         用户id
     * @return true是 false不是
     */
    Boolean judgeUserIsCircleManage(Integer circleId, List<Integer> userPermission, String userId);

    /**
     * 判断用户是否为圈子成员
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return true(是) false(不是)
     */
    Boolean judgeUserInCircle(Integer circleId, String userId);

    /**
     * 根据圈子id和用户状态得到用户圈子总活跃度
     *
     * @param circleId   圈子id
     * @param userStatus 用户状态
     * @return
     */
    Long sumUserVitalityByCircleIdAndUserStatus(Long circleId, Integer userStatus);

    /**
     * 检测用户是否完成全部主题打卡（用户是否可以打卡）
     * false 完成全部主题 不可以在打卡
     * true 未完成 可以打卡
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    Boolean checkUserSignInStatus(Long circleId, String userId);

    /**
     * 加入圈子
     *
     * @param circleId    圈子id
     * @param userId      用户id
     * @param applyReason 申请理由
     * @return
     */
    String joinCircle(Long circleId, String userId, String applyReason);

    /**
     * 根据密码加入圈子
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param password 密码
     * @return
     */
    String joinCircleByPassword(Long circleId, String userId, String password);

    /**
     * 退出圈子
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    String exitCircle(Long circleId, String userId);

    /**
     * 我加入的圈子
     *
     * @param userId 用户id
     * @param page   页数
     * @return
     */
    String selectUserJoinCircle(String userId, Integer page);

    /**
     * 我创建的圈子
     *
     * @param userId 用户id
     * @param page   页数
     * @return
     */
    String selectUserFoundCircle(String userId, Integer page);

    /**
     * 获取圈子正常用户信息
     *
     * @param circleId 圈子id
     * @param page     页数
     * @return
     */
    String getCircleNormalUser(Long circleId, Integer page);

    /**
     * 加入圈子装饰
     *
     * @param joinCircles 所要加强的数据
     * @param number      总数
     * @param page        页数
     * @return
     */
    String selectAdornNormalJoinCircle(List<JoinCircle> joinCircles, Integer number, Integer page);

    /**
     * 待审核和拒绝用户装饰
     *
     * @param joinCircles 所要加强的数据
     * @param number      总数
     * @param page        页数
     * @return
     */
    String selectAdornJoinCircle(List<JoinCircle> joinCircles, Integer number, Integer page);

    /**
     * 获取圈子被拒绝用户信息
     *
     * @param circleId 圈子id
     * @param page     页数
     * @return
     */
    String getCircleRefuseUser(Long circleId, Integer page);

    /**
     * 获取圈子待加入用户信息
     *
     * @param circleId 圈子id
     * @param page     页数
     * @return
     */
    String getCircleAuditUser(Long circleId, Integer page);

    /**
     * 同意用户加入圈子申请
     *
     * @param applyUserId 申请人用户id
     * @param circleId    圈子id
     * @param userId      操作人用户id
     * @return
     */
    String agreeJoinApply(String applyUserId, Long circleId, String userId);

    /**
     * 拒绝用户加入圈子申请
     *
     * @param circleId     圈子id
     * @param applyUserId  申请人用户id
     * @param refuseReason 拒绝理由
     * @param userId       处理人id
     * @return
     */
    String refuseJoinCircle(Long circleId, String applyUserId, String refuseReason, String userId);

    /**
     * 淘汰圈子中用户
     *
     * @param circleId  圈子id
     * @param outUserId 被淘汰用户人id
     * @param userId    用户id
     * @return
     */
    String outCircleUser(Long circleId, String outUserId, String userId);

    /**
     * 保存用户所在圈子昵称
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @param nickname 用户昵称
     * @return
     */
    String saveCircleNickname(String userId, Long circleId, String nickname);

    /**
     * 更新用户所在圈子昵称为默认值
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @return
     */
    String updateCircleNicknameDefault(String userId, Long circleId);

    /**
     * 检测用户是否设置了圈子昵称
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    String checkUserCircleNickname(Long circleId, String userId);

    /**
     * 更新用户圈子昵称
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param nickname
     * @return
     */
    String updateUserCircleNickname(Long circleId, String userId, String nickname);

    /**
     * 获取圈子中管理员信息
     *
     * @param circleId 圈子id
     * @return
     */
    String getCircleAdminInfo(Long circleId);

    /**
     * 获取圈子普通用户信息
     *
     * @param circleId 圈子id
     * @param page     当前页数
     * @return
     */
    String getCircleGeneralUserInfo(Long circleId, Integer page);

    /**
     * 检测是否可以添加管理员
     *
     * @param circleId 圈子id
     * @return
     */
    String checkAddCircleAdmin(Long circleId);

    /**
     * 添加圈子管理员
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    String addCircleAdmin(Long circleId, String userId);

    /**
     * 移除管理员
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    String outCircleAdminInfo(Long circleId, String userId);

    /**
     * 圈子转让
     *
     * @param circleId 圈子id
     * @param tUserId  转让给目标用户的id
     * @param userId   用户id
     * @return
     */
    String transferCircle(Long circleId, String tUserId, String userId);

    /**
     * 获取某个用户的所在圈子相关信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param tUserId  要查询的目标用户id
     * @return
     */
    String getRecordUserInfo(Long circleId, String userId, String tUserId);

    /**
     * 获取圈子中所有的管理人员信息
     *
     * @param circleId       圈子id
     * @param userPermission 用户权限列表
     * @param start          起始位置
     * @param page           数据数量
     * @return
     */
    List<JoinCircle> selectUserIdByUserPermission(Integer circleId, List<Integer> userPermission, Integer start, Integer page);
}


