package com.ccl.wx.service;

import com.ccl.wx.dto.CommentDTO;
import com.ccl.wx.entity.Comment;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/2/29 11:27
 */
public interface CommentService {

    /**
     * 获取日志评论和回复
     *
     * @param diaryId 日志id
     * @param page    第几页
     * @param home    是否为圈子主页
     * @return
     */
    List<CommentDTO> getDiaryComment(Long diaryId, Integer page, Boolean home);

    /**
     * 根据日志id获取此日志的全部评论和回复总条数
     *
     * @param diaryId 日志id
     * @return
     */
    Long getDiaryAllComment(Long diaryId);

    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    /**
     * 获取全部的日志点评
     *
     * @param diaryId 日志id
     * @return
     */
    List<CommentDTO> getMasterComment(Long diaryId);

    /**
     * 保存日志评论
     *
     * @param comment      评论内容
     * @param targetUserId 日志所属人的id
     * @return
     */
    String saveDiaryComment(Comment comment, String targetUserId);

    /**
     * 删除圈子评论
     *
     * @param commentId 评论id
     * @return
     */
    String deleteCircleComment(Integer commentId);

    /**
     * 检测圈子管理员是否可以点评日志
     *
     * @param diaryId 日志id
     * @return
     */
    Boolean checkComment(Integer diaryId);

    /**
     * 根据日记id查询评论的全部id
     *
     * @param diaryId 日记id
     * @return
     */
    List<Long> selectIdByDiaryId(Long diaryId);
}

