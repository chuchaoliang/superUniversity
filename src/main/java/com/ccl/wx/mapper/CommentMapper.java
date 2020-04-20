package com.ccl.wx.mapper;

import com.ccl.wx.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/25 19:51
 */

@Mapper
public interface CommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    /**
     * 查找日记评论、点评
     *
     * @param diaryId     日记id
     * @param commentType 评论类型
     * @param start       起始页
     * @param number      查找个数
     * @return
     */
    List<Comment> selectComment(@Param("diaryId") Long diaryId, @Param("commentType") Integer commentType, @Param("start") Integer start, @Param("number") Integer number);

    /**
     * 根据日记id查询全部评论信息
     *
     * @param diaryId 日志id
     * @return
     */
    List<Comment> selectAllByDiaryId(@Param("diaryId") Long diaryId);

    /**
     * 获取评论总数
     *
     * @param diaryId 日记id
     * @return
     */
    Long countByDiaryId(Long diaryId);

    /**
     * 根据日记id查询所有的评论id
     *
     * @param diaryId 日记id
     * @return
     */
    List<Long> selectIdByDiaryId(Long diaryId);
}