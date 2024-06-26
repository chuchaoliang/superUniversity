package com.ccl.wx.controller.circle.diary;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.Result;
import com.ccl.wx.dto.CommentDTO;
import com.ccl.wx.entity.Comment;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.global.annotation.ParamCheck;
import com.ccl.wx.service.CommentService;
import com.ccl.wx.util.ResponseMsgUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author 褚超亮
 * @date 2020/1/18 11:47
 * 圈子评论、点赞、回复功能
 */
@RestController
@RequestMapping("/circle")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 保存日志评论、点评
     *
     * @param comment      评论具体内容
     * @param userId       用户id
     * @param targetUserId 日志所属人id
     * @param result       参数校检结果
     * @return
     */
    @PostMapping("/diary/comment/add")
    public Result<String> saveCircleComment(@Validated @RequestBody(required = false) Comment comment,
                                            @RequestHeader(value = "token", required = false) String userId,
                                            @ParamCheck @RequestParam(value = "userId", required = false) String targetUserId, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseMsgUtil.fail(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        comment.setUserId(userId);
        String responseResult = commentService.saveDiaryComment(comment, targetUserId);
        if (EnumResultStatus.FAIL.getValue().equals(responseResult)) {
            return ResponseMsgUtil.fail("评论失败！");
        }
        return ResponseMsgUtil.success(responseResult);
    }

    /**
     * 检测圈子管理员是否可以点评日志
     *
     * @param diaryId 日志id
     * @return
     */
    @GetMapping("/diary/comment/check")
    public Result<String> checkComment(@RequestParam(value = "diaryId", required = false) Integer diaryId) {
        boolean result = commentService.checkComment(diaryId);
        if (result) {
            return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
        }
        return ResponseMsgUtil.fail("日志点评不能超过3条啊！");
    }

    /**
     * 获取某篇日记的评论
     *
     * @param diaryId 日记id
     * @return
     */
    @ParamCheck
    @GetMapping("/diary/comment/get")
    public Result<String> getAllCommentByDiaryId(@RequestParam(value = "diaryId", required = false) Integer diaryId,
                                                 @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        List<CommentDTO> diaryComment = commentService.getDiaryComment(diaryId.longValue(), page, false);
        return ResponseMsgUtil.success(JSON.toJSONString(diaryComment));
    }

    /**
     * 根据评论id删除评论
     *
     * @param commentId 评论id
     * @return
     */
    @GetMapping("/diary/comment/del")
    public Result<String> deleteCircleCommentById(@ParamCheck @RequestParam(value = "commentId", required = false) Integer commentId) {
        String result = commentService.deleteCircleComment(commentId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("删除失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }
}
