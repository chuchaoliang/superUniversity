package com.ccl.wx.service;

import com.ccl.wx.entity.Reply;

import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/25 20:05
 */

public interface ReplyService {


    int deleteByPrimaryKey(Long id);

    int insert(Reply record);

    int insertSelective(Reply record);

    Reply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Reply record);

    int updateByPrimaryKey(Reply record);

    List<Long> selectIdByDiaryId(Long diaryId);

    /**
     * 回复评论
     *
     * @param reply
     * @return
     */
    String replyDiaryComment(Reply reply);

    /**
     * 删除回复
     *
     * @param replyId 回复id
     * @return
     */
    String deleteDiaryReply(Integer replyId);

    /**
     * 根据评论id查询回复数据
     *
     * @param commentId  评论id
     * @param start      起始
     * @param pageNumber 多少个数据
     * @return
     */
    List<Reply> selectAllByCommentId(Long commentId, int start, Integer pageNumber);

    /**
     * 根据日记id查询回复的条数
     *
     * @param diaryId 日记id
     * @return
     */
    Long countByDiaryId(Long diaryId);
}
