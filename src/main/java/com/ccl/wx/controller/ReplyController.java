package com.ccl.wx.controller;

import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.common.EnumResultCode;
import com.ccl.wx.common.Result;
import com.ccl.wx.entity.Reply;
import com.ccl.wx.enums.EnumResultStatus;
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
@RequestMapping("/wx/circle")
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
}
