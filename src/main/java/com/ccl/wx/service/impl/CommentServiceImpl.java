package com.ccl.wx.service.impl;

import com.ccl.wx.dto.CommentDTO;
import com.ccl.wx.dto.ReplyDTO;
import com.ccl.wx.entity.Comment;
import com.ccl.wx.entity.Reply;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.mapper.CommentMapper;
import com.ccl.wx.mapper.ReplyMapper;
import com.ccl.wx.pojo.DiaryHideComment;
import com.ccl.wx.service.CommentService;
import com.ccl.wx.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 评论数量
     */
    public static final Integer COMMENT_NUMBER = 5;

    /**
     * 评论的回复数量
     */
    public static final Integer COMMENT_REPLY_NUMBER = 3;

    @Override
    public List<CommentDTO> getOneDiaryCommentInfoById(Long diaryId) {
        // 获取子评论
        List<Comment> comments = commentMapper.selectAllByDiaryIdAndCommentTypeOrderByCommentCreatetimeDesc(diaryId, 0, 0, 10);
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            // 查询此评论下的全部子评论 TODO 可以设置分页 只查询10个
            List<Reply> replies = replyMapper.selectAllByCommentId(comment.getId(), 0, COMMENT_REPLY_NUMBER);
            ArrayList<ReplyDTO> replyDTOS = new ArrayList<>();
            for (Reply reply : replies) {
                ReplyDTO replyDTO = new ReplyDTO();
                // 回复人信息
                UserInfo userInfo = userInfoService.selectByPrimaryKey(reply.getReplyUserid());
                // 目标用户信息
                UserInfo targetUserInfo = userInfoService.selectByPrimaryKey(reply.getTargetUserid());
                BeanUtils.copyProperties(reply, replyDTO);
                // 设置回复人昵称
                replyDTO.setNickName(userInfo.getNickname());
                // 设置回复人性别
                replyDTO.setGender(userInfo.getGender());
                // 设置回复人头像
                replyDTO.setHeadImage(userInfo.getAvatarurl());
                // 设置目标人昵称
                replyDTO.setTargetNickName(targetUserInfo.getNickname());
                // 设置目标人性别
                replyDTO.setTargetGender(targetUserInfo.getGender());
                // 设置目标人头像
                replyDTO.setTargetHeadImage(targetUserInfo.getAvatarurl());
                replyDTOS.add(replyDTO);
            }
            // 获取用户信息
            UserInfo userInfo = userInfoService.selectByPrimaryKey(comment.getUserId());
            BeanUtils.copyProperties(comment, commentDTO);
            // 设置用户昵称
            commentDTO.setNickName(userInfo.getNickname());
            // 设置用户头像
            commentDTO.setHeadImage(userInfo.getAvatarurl());
            // 设置用户性别
            commentDTO.setGender(userInfo.getGender());
            // 设置此评论的回复
            commentDTO.setReplyDTOS(replyDTOS);
            commentDTOS.add(commentDTO);
            if (commentDTOS.size() == COMMENT_NUMBER) {
                break;
            }
        }
        return commentDTOS;
    }

    @Override
    public Integer getUserCommentSumById(Long id) {
        return null;
    }

    @Override
    public DiaryHideComment judgeHideCommentById(Long diaryId) {
        // 日志数
        Long commentSum = commentMapper.countByDiaryId(diaryId);
        Long replySum = replyMapper.countByDiaryId(diaryId);
        DiaryHideComment diaryHideComment = new DiaryHideComment();
        long sum = commentSum + replySum;
        diaryHideComment.setCommentSum(sum);
        if (sum > COMMENT_NUMBER * COMMENT_REPLY_NUMBER) {
            diaryHideComment.setHideComment(true);
        } else {
            diaryHideComment.setHideComment(false);
        }
        return diaryHideComment;
    }

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
}

