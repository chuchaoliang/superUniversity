package com.ccl.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.dto.CommentDTO;
import com.ccl.wx.dto.ReplyDTO;
import com.ccl.wx.entity.Comment;
import com.ccl.wx.entity.Reply;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.enums.diary.EnumComment;
import com.ccl.wx.enums.common.EnumPage;
import com.ccl.wx.enums.diary.EnumReply;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.mapper.CommentMapper;
import com.ccl.wx.service.CommentService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.ReplyService;
import com.ccl.wx.service.UserInfoService;
import com.ccl.wx.util.CclDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/2/29 11:27
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ReplyService replyService;

    @Resource
    private JoinCircleService joinCircleService;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return commentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Comment record) {
        return commentMapper.insert(record);
    }

    @Override
    public int insertSelective(Comment record) {
        return commentMapper.insertSelective(record);
    }

    @Override
    public Comment selectByPrimaryKey(Long id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Comment record) {
        return commentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Comment record) {
        return commentMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Long> selectIdByDiaryId(Long diaryId) {
        return commentMapper.selectIdByDiaryId(diaryId);
    }

    @Override
    public List<CommentDTO> getDiaryComment(Long diaryId, Integer page, Boolean home) {
        int value = EnumPage.PAGE_NUMBER.getValue();
        int number = value;
        if (home) {
            number = EnumPage.CIRCLE_HOME_THEME.getValue();
        }
        List<Comment> comments = commentMapper.selectComment(diaryId, EnumComment.COMMENT_USER.getValue(), page * value, number);
        return adornComment(comments);
    }

    public List<CommentDTO> adornComment(List<Comment> comments) {
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            List<Reply> replies = replyService.selectReply(comment.getId(), 0, EnumReply.REPLY_SHOW.getValue());
            // 获取全部子评论
            List<ReplyDTO> replyDTOS = replyService.adornReply(replies, true);
            // 获取用户信息
            UserInfo userInfo = userInfoService.selectByPrimaryKey(comment.getUserId());
            userInfo.setNickname(joinCircleService.getUserJoinCircleNickname(userInfo.getId(), comment.getCircleId()));
            BeanUtils.copyProperties(comment, commentDTO);
            // 设置用户昵称
            commentDTO.setNickName(userInfo.getNickname());
            // 设置用户头像
            commentDTO.setHeadImage(userInfo.getAvatarurl());
            // 设置用户性别
            commentDTO.setGender(userInfo.getGender());
            // 设置此评论的回复
            commentDTO.setReplyList(replyDTOS);
            // 设置创建时间
            commentDTO.setCreateTime(CclDateUtil.todayDate(comment.getCommentCreatetime()));
            // 设置回复总数
            commentDTO.setReplyNumber(replyService.countByCommentId(comment.getId()));
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    @Override
    public Long getDiaryAllComment(Long diaryId) {
        Long commentNumber = commentMapper.countByDiaryId(diaryId);
        Long replyNumber = replyService.countByDiaryId(diaryId);
        return commentNumber + replyNumber;
    }

    @Override
    public List<CommentDTO> getMasterComment(Long diaryId) {
        // 获取日记的点评
        List<Comment> comments = commentMapper.selectComment(diaryId, 1, 0, EnumComment.COMMENT_MAX_NUMBER.getValue());
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            if (EnumComment.COMMENT_DELETE_STATUS.getValue().equals(comment.getCommentStatus())) {
                // 点评为删除状态
                continue;
            }
            CommentDTO commentDTO = new CommentDTO();
            UserInfo userInfo = userInfoService.selectByPrimaryKey(comment.getUserId());
            userInfo.setNickname(joinCircleService.getUserJoinCircleNickname(userInfo.getId(), comment.getCircleId()));
            BeanUtils.copyProperties(comment, commentDTO);
            // 设置用户昵称
            commentDTO.setNickName(userInfo.getNickname());
            // 设置用户头像
            commentDTO.setHeadImage(userInfo.getAvatarurl());
            // 设置用户性别
            commentDTO.setGender(userInfo.getGender());
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    @Override
    public String saveDiaryComment(Comment comment) {
        // 插入数据
        int i = commentMapper.insertSelective(comment);
        if (i == 1) {
            return JSON.toJSONString(comment);
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public String deleteCircleComment(Integer commentId) {
        Comment comment = commentMapper.selectByPrimaryKey(commentId.longValue());
        if (comment != null) {
            // 删除评论
            commentMapper.deleteByPrimaryKey(commentId.longValue());
            List<Reply> replies = replyService.selectReply(commentId.longValue(), 0, Integer.MAX_VALUE);
            for (Reply reply : replies) {
                replyService.deleteByPrimaryKey(reply.getId());
            }
            return EnumResultStatus.SUCCESS.getValue();
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public Boolean checkComment(Integer diaryId) {
        int masterCommentNumber = getMasterComment(diaryId.longValue()).size();
        if (masterCommentNumber >= EnumComment.COMMENT_MAX_NUMBER.getValue()) {
            return false;
        } else {
            return true;
        }
    }
}

