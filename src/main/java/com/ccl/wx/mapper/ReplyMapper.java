package com.ccl.wx.mapper;

import com.ccl.wx.entity.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/25 20:05
 */

@Mapper
public interface ReplyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Reply record);

    int insertSelective(Reply record);

    Reply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Reply record);

    int updateByPrimaryKey(Reply record);

    /**
     * 根据评论id查找回复
     *
     * @param commentId 评论的id
     * @param start     起始位置
     * @param number    总数
     * @return
     */
    List<Reply> selectReply(@Param("commentId") Long commentId, @Param("start") Integer start, @Param("number") Integer number);

    /**
     * 根据圈子id查询全部回复数据
     *
     * @param circleId 圈子id
     * @return
     */
    List<Reply> selectAllByCircleId(@Param("circleId") Long circleId);

    /**
     * 根据日记id查询回复的条数
     *
     * @param diaryId 日志id
     * @return
     */
    Long countByDiaryId(@Param("diaryId") Long diaryId);

    /**
     * 根据日记id查询全部回复id
     *
     * @param diaryId 日志id
     * @return
     */
    List<Long> selectIdByDiaryId(Long diaryId);

    /**
     * 根据评论id获取全部回复总数
     *
     * @param commentId 评论id
     * @return
     */
    Long countByCommentId(@Param("commentId") Long commentId);
}