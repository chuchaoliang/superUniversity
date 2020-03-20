package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.dto.CommentDTO;
import com.ccl.wx.entity.Comment;
import com.ccl.wx.entity.Reply;
import com.ccl.wx.mapper.CommentMapper;
import com.ccl.wx.mapper.ReplyMapper;
import com.ccl.wx.service.CircleRedisService;
import com.ccl.wx.service.CommentService;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/1/18 11:47
 * 圈子评论、点赞、回复功能
 */
@RestController
@RequestMapping("/wx")
public class CircleCommentController {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private CircleRedisService circleRedisService;

    @Autowired
    private CommentService commentService;

    /**
     * 评论正常状态
     */
    private final static Integer COMMENT_STATUS_NORMAL = 0;

    /**
     * 评论删除状态
     */
    private final static Integer COMMENT_STATUS_DELETE = 1;

    /**
     * 不是点评状态
     */
    private final static Integer JUDGE_REMARK_NO = 0;

    /**
     * 是点评状态
     */
    private final static Integer JUDGE_REMARK_YES = 1;


    /**
     * TODO
     * 保存圈子评论
     *
     * @param commentDTO 评论内容状态
     *                   获取 评论内容commentContent 评论人userid 圈子circleid 日志id diary_id
     * @return
     */
    @SneakyThrows
    @PostMapping("/savecomment")
    public String saveCircleComment(@RequestBody(required = false) CommentDTO commentDTO) {
        // 设置评论创建时间
        commentDTO.setCommentCreatetime(new Date());
        // 设置评论状态
        commentDTO.setCommentStatus(COMMENT_STATUS_NORMAL);
        // 设置评论类型,是否是点评
        if (commentDTO.getRemarkStatus()) {
            // 是点评状态
            commentDTO.setCommentType(JUDGE_REMARK_YES);
        } else {
            // 不是点评状态
            commentDTO.setCommentType(JUDGE_REMARK_NO);
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        // 插入数据
        commentMapper.insertSelective(comment);
        return "success";
    }


    /**
     * TODO
     * 根据评论id删除评论
     *
     * @param id 评论id
     * @return
     */
    @GetMapping("/delcomment")
    public String deleteCircleCommentById(@RequestParam(value = "id", required = false) String id) {
        if (StringUtils.isEmpty(id)) {
            return "fail";
        } else {
            // 删除评论、并且删除子评论
            commentMapper.deleteByPrimaryKey(Long.valueOf(id));
            List<Reply> replies = replyMapper.selectAllByCommentId(Long.valueOf(id), 0, 3);
            if (replies.size() == 0) {
                // 不存在子评论
                return "success";
            } else {
                // 存在子评论 TODO 根据子评论删除 待改进 若子评论过多不能全部删除
                for (Reply reply : replies) {
                    // 全部删除
                    replyMapper.deleteByPrimaryKey(reply.getId());
                }
                return "success";
            }
        }
    }

    /**
     * 回复评论
     *
     * @param reply 评论
     *              commentId 评论id、replyContent 回复内容、replyUserid 回复用户id、targetUserid 目标用户id、replyStatus 回复的状态
     *              replyCreatetime 回复的创建时间、circleId 圈子id
     * @return
     */
    @PostMapping("/replycomment")
    public String replyDiaryComment(@RequestBody(required = false) Reply reply) {
        // 设置创建时间
        reply.setReplyCreatetime(new Date());
        // 插入数据
        replyMapper.insertSelective(reply);
        return "success";
    }

    /**
     * 删除回复
     *
     * @param id
     * @return
     */
    @GetMapping("/delreply")
    public String deleteCircleReply(@RequestParam(value = "id", required = false) String id) {
        if (StringUtils.isEmpty(id)) {
            return "fail";
        } else {
            replyMapper.deleteByPrimaryKey(Long.valueOf(id));
            return "success";
        }
    }

    /**
     * redis点赞状态
     *
     * @param userId
     * @param circleId
     * @param diaryId
     * @return
     */
    @GetMapping("/savelike")
    public String saveLikeDiary(@RequestParam(value = "userid", required = false) String userId,
                                @RequestParam(value = "circleid", required = false) String circleId,
                                @RequestParam(value = "diaryid", required = false) String diaryId) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(circleId) || StringUtils.isEmpty(diaryId)) {
            return "fail";
        } else {
            String hint = circleRedisService.saveLikeRedis(userId, circleId, diaryId);
            return hint;
        }
    }

    /**
     * redis取消用户点赞
     *
     * @param userId
     * @param circleId
     * @param diaryId
     * @return
     */
    @GetMapping("/unlike")
    public String unLikeDiary(@RequestParam(value = "userid", required = false) String userId,
                              @RequestParam(value = "circleid", required = false) String circleId,
                              @RequestParam(value = "diaryid", required = false) String diaryId) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(circleId) || StringUtils.isEmpty(diaryId)) {
            return "fail";
        } else {
            String hint = circleRedisService.unLikeFromRedis(userId, circleId, diaryId);
            return hint;
        }
    }

    /**
     * redis删除用户点赞
     *
     * @param userId
     * @param circleId
     * @param diaryId
     * @return
     */
    @GetMapping("/dellike")
    public String deleteLikeDiary(@RequestParam(value = "userid", required = false) String userId,
                                  @RequestParam(value = "circleid", required = false) String circleId,
                                  @RequestParam(value = "diaryid", required = false) String diaryId) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(circleId) || StringUtils.isEmpty(diaryId)) {
            return "fail";
        } else {
            String hint = circleRedisService.deleteLikeFromRedis(userId, circleId, diaryId);
            return hint;
        }
    }

    /**
     * redis判断用户状态（是否可以进行点赞）
     *
     * @param userId
     * @return
     */
    @GetMapping("/statuslike")
    public Boolean judgeUserLikeStatus(@RequestParam(value = "userid", required = false) String userId) {
        return circleRedisService.judgeLikeStatus(userId);
    }

    /**
     * 根据日记id获取评论的信息
     *
     * @param diaryId
     * @return
     */
    @GetMapping("/get/comment/{diaryId}")
    public String getDiaryCommentById(@PathVariable(value = "diaryId") Long diaryId) {
        return JSON.toJSONString(commentService.getOneDiaryCommentInfoById(diaryId));
    }

    /**
     * 根据日记id判断是否隐藏评论
     *
     * @param diaryId
     * @return
     */
    @GetMapping("/judge/comment/{diaryId}")
    public String judgeDiaryHideComment(@PathVariable(value = "diaryId") Long diaryId) {
        return JSON.toJSONString(commentService.judgeHideCommentById(diaryId));
    }
}
