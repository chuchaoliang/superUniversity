package com.ccl.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.entity.Comment;
import com.ccl.wx.entity.Reply;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.mapper.ReplyMapper;
import com.ccl.wx.service.CommentService;
import com.ccl.wx.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/25 20:05
 */

@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class ReplyServiceImpl implements ReplyService {

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private CommentService commentService;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return replyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Reply record) {
        return replyMapper.insert(record);
    }

    @Override
    public int insertSelective(Reply record) {
        return replyMapper.insertSelective(record);
    }

    @Override
    public Reply selectByPrimaryKey(Long id) {
        return replyMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Reply record) {
        return replyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Reply record) {
        return replyMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Long> selectIdByDiaryId(Long diaryId) {
        return replyMapper.selectIdByDiaryId(diaryId);
    }

    @Override
    public String replyDiaryComment(Reply reply) {
        Comment comment = commentService.selectByPrimaryKey(reply.getCommentId());
        if (comment == null) {
            return EnumResultStatus.FAIL.getValue();
        }
        int i = replyMapper.insertSelective(reply);
        if (i == 1) {
            return JSON.toJSONString(reply);
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public String deleteDiaryReply(Integer replyId) {
        int i = replyMapper.deleteByPrimaryKey(replyId.longValue());
        if (i == 1) {
            return EnumResultStatus.SUCCESS.getValue();
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public List<Reply> selectAllByCommentId(Long commentId, int start, Integer pageNumber) {
        return replyMapper.selectAllByCommentId(commentId, start, pageNumber);
    }

    @Override
    public Long countByDiaryId(Long diaryId) {
        return replyMapper.countByDiaryId(diaryId);
    }
}
