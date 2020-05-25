package com.ccl.wx.controller.circle.diary;

import com.ccl.wx.global.annotation.ParamCheck;
import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.Result;
import com.ccl.wx.entity.Reply;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.service.ReplyService;
import com.ccl.wx.util.ResponseMsgUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author 褚超亮
 * @date 2020/4/7 20:20
 */
@RestController
@RequestMapping("/circle")
public class ReplyController {

    @Resource
    private ReplyService replyService;

    /**
     * 回复评论
     *
     * @param reply 评论
     * @return
     */
    @PostMapping("/diary/comment/reply")
    public Result<String> replyDiaryComment(@Validated @RequestBody(required = false) Reply reply,
                                            @RequestHeader(value = "token", required = false) String userId, BindingResult request) {
        if (request.hasErrors()) {
            return ResponseMsgUtil.fail(Objects.requireNonNull(request.getFieldError()).getDefaultMessage());
        }
        reply.setReplyUserid(userId);
        String responseResult = replyService.replyDiaryComment(reply);
        if (EnumResultStatus.FAIL.getValue().equals(responseResult)) {
            return ResponseMsgUtil.fail("回复失败！");
        }
        // 插入数据
        return ResponseMsgUtil.success(responseResult);
    }

    /**
     * 删除回复
     *
     * @param replyId
     * @return
     */
    @GetMapping("/diary/reply/del")
    public Result<String> deleteCircleReply(@ParamCheck @RequestParam(value = "replyId", required = false) Integer replyId) {
        String result = replyService.deleteDiaryReply(replyId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            ResponseMsgUtil.fail("删除失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 获取评论下的子评论信息
     *
     * @param commentId 评论id
     * @return
     */
    @GetMapping("/diary/reply/get")
    public Result<String> getDiaryReply(@RequestParam(value = "commentId", required = false) Integer commentId,
                                        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String result = replyService.getReply(commentId, page);
        return ResponseMsgUtil.success(result);
    }
}
