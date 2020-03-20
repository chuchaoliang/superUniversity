package com.ccl.wx.service;

/**
 * @author 褚超亮
 * @date 2020/1/30 18:40
 */
public interface CircleScheduleService {

    /**
     * 用户点赞数据持久化
     * 将redis中用户点赞数据，保存到mysql中
     */
    void saveUserLikeDataPersistence();

    /**
     * 将全部点赞数据持久化
     * 将redis中用户点赞总数，保存到mysql中
     */
    void saveUserAccountLikeDataPersistence();

    /**
     * 删除用户的日志并且评论、点赞、回复等信息
     */
    void deleteUserDiaryInfoAndComment();
}
