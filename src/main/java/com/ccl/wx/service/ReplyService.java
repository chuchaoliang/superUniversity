package com.ccl.wx.service;

import com.ccl.wx.dto.ReplyDTO;
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
    List<Reply> selectReply(Long commentId, int start, Integer pageNumber);

    /**
     * 根据日记id查询回复的条数
     *
     * @param diaryId 日记id
     * @return
     */
    Long countByDiaryId(Long diaryId);

    /**
     * 装饰回复
     *
     * @param replies 回复列表
     * @param index   是否展示回复中评论对应目标人信息（true展示false不展示）
     * @return
     */
    List<ReplyDTO> adornReply(List<Reply> replies, boolean index);

    /**
     * 根据日记id查询全部的回复
     *
     * @param diaryId
     * @return
     */
    List<Long> selectIdByDiaryId(Long diaryId);

    /**
     * 根据评论id获取全部回复总数
     *
     * @param commentId 评论id
     * @return
     */
    Long countByCommentId(Long commentId);

    /**
     * 获取评论下的子评论信息
     *
     * @param commentId 评论id
     * @param page
     * @return
     */
    String getReply(Integer commentId, Integer page);
}
