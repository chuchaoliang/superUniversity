package com.ccl.wx.controller.circle;

import com.ccl.wx.global.annotation.ParamCheck;
import com.ccl.wx.common.api.EnumResultCode;
import com.ccl.wx.common.api.Result;
import com.ccl.wx.dto.CircleIntroDTO;
import com.ccl.wx.entity.CircleIntro;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.service.CircleIntroService;
import com.ccl.wx.util.ResponseMsgUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author 褚超亮
 * @date 2020/4/16 20:50
 */
@Api(tags = {"CircleIntroController【圈子简介相关的数据信息】"})
@Slf4j
@RestController
@RequestMapping("/wx/circle")
public class CircleIntroController {

    @Resource
    private CircleIntroService circleIntroService;

    /**
     * 保存圈子简介内容
     *
     * @param circleIntro 圈子信息
     * @param result
     * @return
     */
    @PostMapping("/intro/content")
    public Result<String> saveCircleIntroContent(@Validated @RequestBody CircleIntro circleIntro,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            return ResponseMsgUtil.fail(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        String resultResponse = circleIntroService.saveCircleIntroContent(circleIntro);
        if (EnumResultStatus.FAIL.getValue().equals(resultResponse)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 保存圈子简介图片
     *
     * @param circleId 圈子id
     * @param file     要保存的文件
     * @param userId
     * @return
     */
    @ParamCheck
    @PostMapping("/intro/image")
    public Result<String> saveCircleIntroImage(@RequestParam(value = "circleId", required = false) Long circleId,
                                               @RequestPart(value = "image", required = false) MultipartFile file,
                                               @RequestHeader(value = "token", required = false) String userId) {
        String result = circleIntroService.saveCircleIntroImage(circleId, file, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 保存圈子简介音频
     *
     * @param circleId 圈子id
     * @param file     文件
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @PostMapping("/intro/voice")
    public Result<String> saveCircleIntroVoice(@RequestParam(value = "circleId", required = false) Long circleId,
                                               @RequestPart(value = "voice", required = false) MultipartFile file,
                                               @RequestHeader(value = "token", required = false) String userId) {
        String result = circleIntroService.saveCircleIntroVoice(circleId, file, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 保存圈主简介内容
     *
     * @param circleIntro 圈子信息
     * @param result
     * @return
     */
    @PostMapping("/intro/master/content")
    public Result<String> saveCircleMasterIntroContent(@Validated @RequestBody(required = false) CircleIntro circleIntro,
                                                       BindingResult result) {
        if (result.hasErrors()) {
            return ResponseMsgUtil.fail(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        String resultResponse = circleIntroService.saveCircleIntroContent(circleIntro);
        if (EnumResultStatus.FAIL.getValue().equals(resultResponse)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * 保存圈主简介图片
     *
     * @param circleId 圈子id
     * @param file     要保存的文件
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @PostMapping("/intro/master/image")
    public Result<String> saveCircleMasterIntroImage(@RequestParam(value = "circleId", required = false) Long circleId,
                                                     @RequestPart(value = "image", required = false) MultipartFile file,
                                                     @RequestHeader(value = "token", required = false) String userId) {
        String result = circleIntroService.saveCircleMasterIntroImage(circleId, file, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 保存圈主简介音频
     *
     * @param circleId 圈子id
     * @param file     文件
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @PostMapping("/intro/master/voice")
    public Result<String> saveCircleMasterIntroVoice(@RequestParam(value = "circleId", required = false) Long circleId,
                                                     @RequestPart(value = "voice", required = false) MultipartFile file,
                                                     @RequestHeader(value = "token", required = false) String userId) {
        String result = circleIntroService.saveCircleMasterIntroVoice(circleId, file, userId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 更新圈子简介
     *
     * @param circleIntroDTO
     * @return
     */
    @PostMapping("/intro/update")
    public Result<String> updateCircleIntro(@RequestBody(required = false) CircleIntroDTO circleIntroDTO,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseMsgUtil.fail(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        String resultResponse = circleIntroService.updateCircleIntro(circleIntroDTO);
        if (EnumResultStatus.FAIL.getValue().equals(resultResponse)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(resultResponse);
    }

    /**
     * 更新圈主简介
     *
     * @param circleIntroDTO
     * @return
     */
    @PostMapping("/intro/master/update")
    public Result<String> updateCircleMasterIntro(@RequestBody(required = false) CircleIntroDTO circleIntroDTO,
                                                  BindingResult result) {
        if (result.hasErrors()) {
            return ResponseMsgUtil.fail(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        String resultResponse = circleIntroService.updateCircleMasterIntro(circleIntroDTO);
        if (EnumResultStatus.FAIL.getValue().equals(resultResponse)) {
            return ResponseMsgUtil.fail("操作失败！");
        }
        return ResponseMsgUtil.success(resultResponse);
    }

    /**
     * 获取圈子简介信息
     *
     * @param circleId 圈子id
     * @return
     */
    @GetMapping("/intro/get")
    public Result<String> getCircleIntro(@ParamCheck @RequestParam(value = "circleId", required = false) Integer circleId) {
        String result = circleIntroService.getCircleIntro(circleId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("不存在圈子简介信息！");
        }
        return ResponseMsgUtil.success(result);
    }
}
