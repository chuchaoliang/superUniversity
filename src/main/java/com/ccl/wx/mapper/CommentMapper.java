package com.ccl.wx.mapper;

import com.ccl.wx.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/1/18 12:37
 */

public interface CommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    /**
     * 根据日记id查找日记评论、点评
     *
     * @param diaryId 日志id
     * @return
     */
    List<Comment> selectAllByDiaryId(@Param("diaryId") Long diaryId);

    /**
     * 查找日记评论、点评
     *
     * @param diaryId     日记id
     * @param commentType 评论类型
     * @param start       页面开始
     * @param end         页面结束
     * @return
     */
    List<Comment> selectAllByDiaryIdAndCommentTypeOrderByCommentCreatetimeDesc(@Param("diaryId") Long diaryId, @Param("commentType") Integer commentType, @Param("start") Integer start, @Param("end") Integer end);

    /**
     * 根据圈子id查询全部评论信息
     *
     * @param circleId
     * @return
     */
    List<Comment> selectAllByCircleId(@Param("circleId") Long circleId);

    /**
     * 根据日志id查询全部评论id列表
     *
     * @param diaryId
     * @return
     */
    List<Long> selectIdByDiaryId(@Param("diaryId") Long diaryId);

    /**
     * 根据日记id查询存在多少评论
     * @param diaryId
     * @return
     */
    Long countByDiaryId(@Param("diaryId")Long diaryId);
}