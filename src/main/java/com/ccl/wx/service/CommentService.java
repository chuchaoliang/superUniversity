package com.ccl.wx.service;

import com.ccl.wx.dto.CommentDTO;
import com.ccl.wx.entity.Comment;
import com.ccl.wx.pojo.DiaryHideComment;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/2/29 11:27
 */
public interface CommentService {

    /**
     * 根据用户的日志信息获取日志的全部评论信息系（其中包括回复）
     *
     * @param diaryId 日志id
     * @return
     */
    List<CommentDTO> getOneDiaryCommentInfoById(Long diaryId);

    /**
     * 根据日志id获取此日志的全部评论和回复总条数
     *
     * @param diaryId
     * @return
     */
    Integer getUserCommentSumById(Long diaryId);

    /**
     * 根据日志id判断是否隐藏评论
     * 是否显示 查看全部**条评论和回复
     *
     * @param diaryId
     * @return
     */
    DiaryHideComment judgeHideCommentById(Long diaryId);

    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Long> selectIdByDiaryId(Long diaryId);

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
     * @param comment 评论内容
     * @return
     */
    String saveDiaryComment(Comment comment);

    /**
     * 删除圈子评论
     *
     * @param commentId 评论id
     * @return
     */
    String deleteCircleComment(Integer commentId);
}

