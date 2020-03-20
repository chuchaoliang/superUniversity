package com.ccl.wx.service;

import com.ccl.wx.dto.CommentDTO;
import com.ccl.wx.dto.UserDiaryDTO;
import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.entity.UserDiary;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.entity.CircleInfo;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2019/10/30 20:47
 */
public interface CircleService {

    /**
     * 检测用户是否在加入圈子中, 过滤淘汰状态的用户
     * 加入： true
     * 未加入： false
     *
     * @param circleId 圈子id
     * @param userid   用户id
     * @return
     */
    Boolean judgeUserInCircle(String circleId, String userid);

    /**
     * 更新用户打卡数据
     *
     * @param userDiary 用户打卡数据
     */
    String updateSignInData(UserDiary userDiary);

    /**
     * 判断用户是否为圈主
     * 是圈主: true
     * 不是圈主: false
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    Boolean judgeUserCircleMaster(String circleId, String userId);

    /**
     * 圈子加强类型
     *
     * @param circles 圈子成员信息
     * @return
     */
    String selectCircleDTO(List<CircleInfo> circles);

    /**
     * 圈子后台成员
     *
     * @param circles 圈子成员信息
     * @return
     */
    String selectCircleAllMemberDTO(List<JoinCircle> circles);

    /**
     * 更新圈子头像
     *
     * @param circleid 圈子id
     * @param filePath 图片路径
     * @return
     */
    Boolean updateCircleHeadImage(String circleid, String filePath);

    /**
     * 创建圈子
     *
     * @param circleInfo 圈子信息
     * @return
     */
    String fondCircle(CircleInfo circleInfo);

    /**
     * 检测圈子名称是否重复,
     * 为空 true
     * 不为空 false
     *
     * @param circleName 圈子名称
     * @return
     */
    Boolean detectionCircleNameRepetition(String circleName);

    /**
     * 正常加入圈子
     * 自己主动退出的圈子再次加入不需要圈主同意即可加入
     * TODO 圈主淘汰的人需要圈主的同意才可加入
     *
     * @param circleid 圈子id
     * @param userid   用户id
     * @return
     */
    String joinCircle(String circleid, String userid);

    /**
     * 私密圈子
     * 根据圈子密码加入圈子
     *
     * @param circleid  圈子id
     * @param userid    用户id
     * @param cpassword 圈子密码
     * @return
     */
    String joinPrivacyCircleByPassword(String circleid, String userid, String cpassword);

    /**
     * 获取圈子内全部状态的成员
     *
     * @param circleid 圈子id
     * @return
     */
    List<String> getCircleAllMember(String circleid);

    /**
     * 获取全部的日志信息
     *
     * @param circleid 圈子id
     * @param userid   用户id
     * @param page     页数
     * @return
     */
    String getAllDiaryInfo(String circleid, String userid, Integer page);

    /**
     * 获取部分的日志信息
     *
     * @param circleid 圈子id
     * @param userid   用户id
     * @param page     页数
     * @return
     */
    String getAssignDiaryInfo(String circleid, String userid, Integer page);

    /**
     * 获取圈子中日志信息
     *
     * @param userDiaries 用户日志
     * @param loginUserid 登录的用户id
     * @param nextPage    是否还有下一页
     * @return
     */
    String getCircleDiaryInfo(List<UserDiary> userDiaries, String loginUserid, Boolean nextPage);

    /**
     * 获取圈子内的主页面加载的全部内容
     *
     * @param circleid 圈子id
     * @param userid   用户id
     * @return 圈子全部内容json字符串
     */
    String getCircleIndexAllContent(String userid, String circleid);

    /**
     * 获取全部的日志评论
     *
     * @param diaryid 日志id
     * @return
     */
    List<CommentDTO> getAllComment(Long diaryid);

    /**
     * 获取全部的日志点评
     *
     * @param diaryid 日志id
     * @return
     */
    List<CommentDTO> getMasterComment(Long diaryid);

    /**
     * 根据日记id查询全部的信息（点赞、评论、点评）
     *
     * @param diaryid 日志id
     * @return
     */
    UserDiaryDTO getDiaryInfoById(Long diaryid);

    /**
     * 获取此日记的点赞状态
     *
     * @param userid   用户id
     * @param circleid 圈子id
     * @param diaryid  日志id
     * @return
     */
    Boolean judgeDiaryLikeStatus(String userid, String circleid, Long diaryid);

    /**
     * 获取全部点赞用户昵称或者信息
     * userid::userNickname
     *
     * @param diaryid
     * @param circleid
     * @param userid
     * @return
     */
    List<UserInfo> getAllLikeUserNickName(String userid, String circleid, Long diaryid);

    /**
     * 根据点赞用户昵称拼接其昵称
     *
     * @param userInfos
     * @return
     */
    String getAllLikeUserNickName(List<UserInfo> userInfos);

    /**
     * 获取用户加入在某个圈子的信息
     *
     * @param userid   用户id
     * @param circleid 圈子id
     * @return
     */
    String getUserInCircleInfo(String userid, Long circleid);

    /**
     * 判断用户是否可以直接进入该圈子
     *
     * @param userid   用户id
     * @param circleid 圈子id
     * @return
     */
    Boolean judgeUserIntoPrivacyCircle(String userid, Long circleid);

    /**
     * 根据圈子id判断是否为私密圈子
     *
     * @param circleid 圈子id
     * @return
     */
    Boolean judgeCirclePrivacyStatus(Long circleid);
}
