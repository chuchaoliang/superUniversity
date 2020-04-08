package com.ccl.wx.controller;

import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.common.EnumResultCode;
import com.ccl.wx.common.Result;
import com.ccl.wx.entity.Comment;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.service.CommentService;
import com.ccl.wx.util.ResponseMsgUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author 褚超亮
 * @date 2020/1/18 11:47
 * 圈子评论、点赞、回复功能
 */
@RestController
@RequestMapping("/wx/circle")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 保存评论
     *
     * @param comment 评论内容状态
     * @return
     */
    @PostMapping("/diary/comment/add")
    public Result<String> saveCircleComment(@Validated @RequestBody(required = false) Comment comment,
                                            @RequestHeader(value = "token", required = false) String userId, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseMsgUtil.fail(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        comment.setUserId(userId);
        String responseResult = commentService.saveDiaryComment(comment);
        if (EnumResultStatus.FAIL.getValue().equals(responseResult)) {
            return ResponseMsgUtil.fail("评论失败！");
        }
        return ResponseMsgUtil.success(responseResult);
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
