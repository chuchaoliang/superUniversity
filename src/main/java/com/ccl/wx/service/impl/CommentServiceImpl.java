package com.ccl.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.dto.CommentDTO;
import com.ccl.wx.dto.ReplyDTO;
import com.ccl.wx.entity.Comment;
import com.ccl.wx.entity.Reply;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.enums.EnumComment;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.mapper.CommentMapper;
import com.ccl.wx.pojo.DiaryHideComment;
import com.ccl.wx.service.CommentService;
import com.ccl.wx.service.ReplyService;
import com.ccl.wx.service.UserInfoService;
import com.ccl.wx.vo.CircleHomeCommentVO;
import com.ccl.wx.vo.CircleHomeReplyVO;
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

    /**
     * 评论数量
     */
    public static final Integer COMMENT_NUMBER = 5;

    /**
     * 评论的回复数量
     */
    public static final Integer COMMENT_REPLY_NUMBER = 3;

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
    public List<CommentDTO> getOneDiaryCommentInfoById(Long diaryId) {
        // 获取子评论
        List<Comment> comments = commentMapper.selectAllByDiaryIdAndCommentTypeOrderByCommentCreatetimeDesc(diaryId, 0, 0, 10);
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            // 查询此评论下的子评论
            List<Reply> replies = replyService.selectAllByCommentId(comment.getId(), 0, COMMENT_REPLY_NUMBER);
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
        Long replySum = replyService.countByDiaryId(diaryId);
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
    public List<CommentDTO> getMasterComment(Long diaryId) {
        // 获取日记的点评
        List<Comment> comments = commentMapper.selectAllByDiaryIdAndCommentTypeOrderByCommentCreatetimeDesc(diaryId, 1, 0, 5);
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            if (EnumComment.COMMENT_DELETE_STATUS.getValue().equals(comment.getCommentStatus())) {
                // 点评为删除状态
                continue;
            }
            CommentDTO commentDTO = new CommentDTO();
            UserInfo userInfo = userInfoService.selectByPrimaryKey(comment.getUserId());
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
            List<Reply> replies = replyService.selectAllByCommentId(commentId.longValue(), 0, Integer.MAX_VALUE);
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

    @Override
    public List<CircleHomeCommentVO> getAllComment(Long diaryId, Integer page) {
        // 获取子评论
        List<Comment> comments = commentMapper.selectAllByDiaryIdAndCommentTypeOrderByCommentCreatetimeDesc(diaryId, 0, 0, 10);
        List<CircleHomeCommentVO> circleHomeCommentVOS = new ArrayList<>();
        for (Comment comment : comments) {
            if (EnumComment.COMMENT_DELETE_STATUS.getValue().equals(comment.getCommentStatus())) {
                // 点评为删除状态
                continue;
            }
            CircleHomeCommentVO circleHomeCommentVO = new CircleHomeCommentVO();
            // 查询此评论下的全部子评论
            List<Reply> replies = replyService.selectAllByCommentId(comment.getId(), 0, 3);
            List<CircleHomeReplyVO> circleHomeReplyVOS = new ArrayList<>();
            for (Reply reply : replies) {
                CircleHomeReplyVO circleHomeReplyVO = new CircleHomeReplyVO();
                // 回复人信息
                UserInfo userInfo = userInfoService.selectByPrimaryKey(reply.getReplyUserid());
                // 目标用户信息
                UserInfo targetUserInfo = userInfoService.selectByPrimaryKey(reply.getTargetUserid());
                BeanUtils.copyProperties(reply, circleHomeReplyVO);
                // 设置回复人昵称
                circleHomeReplyVO.setNickName(userInfo.getNickname());
                // 设置回复人性别
                circleHomeReplyVO.setGender(userInfo.getGender());
                // 设置回复人头像
                circleHomeReplyVO.setHeadImage(userInfo.getAvatarurl());
                // 设置目标人昵称
                circleHomeReplyVO.setTargetNickName(targetUserInfo.getNickname());
                circleHomeReplyVOS.add(circleHomeReplyVO);
                if (circleHomeCommentVOS.size() == 15) {
                    break;
                }
            }
            // 获取用户信息
            UserInfo userInfo = userInfoService.selectByPrimaryKey(comment.getUserId());
            BeanUtils.copyProperties(comment, circleHomeCommentVOS);
            // 设置用户昵称
            circleHomeCommentVO.setNickName(userInfo.getNickname());
            // 设置用户头像
            circleHomeCommentVO.setHeadImage(userInfo.getAvatarurl());
            // 设置用户性别
            circleHomeCommentVO.setGender(userInfo.getGender());
            // 设置此评论的回复
            circleHomeCommentVO.setReplies(circleHomeReplyVOS);
            circleHomeCommentVOS.add(circleHomeCommentVO);
        }
        return circleHomeCommentVOS;
    }
}

