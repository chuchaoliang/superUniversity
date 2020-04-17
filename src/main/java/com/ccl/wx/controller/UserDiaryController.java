package com.ccl.wx.controller;

import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.common.EnumResultCode;
import com.ccl.wx.common.Result;
import com.ccl.wx.entity.UserDiary;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.ResponseMsgUtil;
import com.ccl.wx.vo.UserDiaryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 圈子日志相关
 *
 * @author 褚超亮
 * @date 2020/2/20 20:32
 */
@Slf4j
@Api(tags = {"CircleDiaryController【圈子用户日志信息】"})
@RestController
@RequestMapping("/wx/circle")
public class UserDiaryController {

    @Resource
    private UserDiaryService userDiaryService;

    @Resource
    private JoinCircleService joinCircleService;

    /**
     * 更新圈子日志信息
     * 1. 获取日记id，日记内容，日记状态，地址信息，图片列表
     * id, diaryContent,diaryStatus,diaryAddress,images
     *
     * @param userDiaryVO
     * @return
     */
    @PostMapping("/diary/update")
    public Result<String> updateCircleDiary(@Validated @RequestBody(required = false) UserDiaryVO userDiaryVO,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseMsgUtil.fail(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        String responseResult = userDiaryService.updateCircleDiaryContent(userDiaryVO);
        if (EnumResultStatus.FAIL.getValue().equals(responseResult)) {
            return ResponseMsgUtil.fail("日志不存在，或者可能被删除，或者主题不存在，被删除！");
        }
        return ResponseMsgUtil.success(responseResult);
    }

    /**
     * 根据日志的id查询日志的信息
     *
     * @param diaryId 日志id
     * @return （与此日志相关的全部评论、点赞、点评信息）
     * @deprecated 不使用
     */
    @GetMapping("/diary/get/one")
    public Result<String> getDiaryInfoByDiaryId(@ParamCheck @RequestParam(value = "diaryId", required = false) String diaryId) {
        // 获取评论内容
        String result = userDiaryService.getDiaryInfoById(Long.valueOf(diaryId));
        return ResponseMsgUtil.success(result);
    }

    /**
     * 保存日志图片
     *
     * @param image   图片文件
     * @param userId  用户id
     * @param diaryId 日志id
     * @return
     */
    @ParamCheck
    @PostMapping("/diary/save/image")
    public Result<String> saveDiaryImage(@RequestPart(value = "image", required = false) MultipartFile image,
                                         @RequestHeader(value = "token", required = false) String userId,
                                         @RequestParam(value = "diaryId", required = false) Long diaryId) {
        String result = userDiaryService.saveDiaryImage(image, userId, diaryId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("文件上传失败，或者日记不存在!");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 音频文件上传
     *
     * @param file    文件
     * @param userId  用户id
     * @param diaryId 日志id
     * @return
     */
    @ParamCheck
    @PostMapping("/diary/save/voice")
    public Result<String> saveDiaryVoice(@RequestPart(value = "file", required = false) MultipartFile file,
                                         @RequestHeader(value = "token", required = false) String userId,
                                         @RequestParam(value = "diaryId", required = false) Long diaryId) {
        String result = userDiaryService.saveDiaryVoice(file, userId, diaryId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("文件上传失败，已经存在音频信息，或者日记不存在!");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 视频文件上传
     *
     * @param file    文件
     * @param userId  用户id
     * @param diaryId 日志id
     * @return
     */
    @ParamCheck
    @PostMapping("/diary/save/video")
    public Result<String> saveDiaryVideo(@RequestPart(value = "file", required = false) MultipartFile file,
                                         @RequestHeader(value = "token", required = false) String userId,
                                         @RequestParam(value = "diaryId", required = false) Long diaryId) {
        String result = userDiaryService.saveDiaryVideo(file, userId, diaryId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("文件上传失败，已经存在视频信息，或者日记不存在!");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 删除日志
     *
     * @param diaryId 日志id
     * @return
     */
    @ApiOperation(value = "删除日志", httpMethod = "GET")
    @GetMapping("/diary/del")
    public Result<String> deleteCircleDiaryInfo(@ParamCheck @RequestParam(value = "diaryId", required = false) Long diaryId) {
        String result = userDiaryService.deleteUserDiaryInfo(diaryId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("日记不能存在，或者已经被删除了！未知错误！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * userId 用户id
     * diaryContent 日记内容 (可为空)
     * diaryStatus  日记状态
     * diaryAddress 用户地址（可为空）
     * circleId 圈子id
     * 如果前端传来的数据不为空则返回此日记的主键id ，否则返回fail
     *
     * @param userDiary 用户日志
     * @return
     */
    @ApiOperation(value = "用户发表日志", notes = "需要参数userId、diaryContent、diaryStatus、diaryAddress、circleId、themeId", httpMethod = "POST")
    @PostMapping("/diary/save/content")
    public Result<String> saveContent(@Validated @RequestBody(required = false) UserDiary userDiary, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseMsgUtil.fail(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        } else {
            String result = userDiaryService.saveUserDiary(userDiary);
            if (EnumResultStatus.FAIL.getValue().equals(result)) {
                return ResponseMsgUtil.fail("打卡失败！用户已经打卡此主题，或者该主题被删除了！");
            } else {
                return ResponseMsgUtil.success(result);
            }
        }
    }

    /**
     * 检测用户打卡状态
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @GetMapping("/diary/check/signin")
    public Result<String> checkUserSignInStatus(@RequestParam(value = "circleId", required = false) Long circleId,
                                                @RequestHeader(value = "token", required = false) String userId) {
        boolean userSignInStatus = joinCircleService.checkUserSignInStatus(circleId, userId);
        if (!userSignInStatus) {
            // 不可以打卡
            return ResponseMsgUtil.fail("用户被淘汰，或者已经完成今日全部主题打卡！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS.getStatus(), "用户可以打卡！", null);
    }

    /**
     * 获取圈子全部用户的全部日志信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param page     当前页数
     * @return
     */
    @ApiOperation(value = "获取圈子中的全部日志信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "circleId", value = "圈子id", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int", example = "1"),
    })
    @ParamCheck
    @GetMapping("/diary/get/all")
    public Result<String> getDiaryInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                       @RequestHeader(value = "token", required = false) String userId,
                                       @RequestParam(value = "page", required = false) Integer page) {
        return ResponseMsgUtil.success(userDiaryService.getAllDiaryInfo(circleId, userId, page));
    }

    /**
     * 获取某个用户的全部日志信息
     * 打卡记录
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param page     页数
     * @return
     */
    @ParamCheck
    @GetMapping("/menu/diary/get/user")
    public Result<String> getAssignDiaryInfo(@RequestParam(value = "circleId", required = false) Long circleId,
                                             @RequestHeader(value = "token", required = false) String userId,
                                             @RequestParam(value = "page", required = false) Integer page) {
        String result = userDiaryService.getAssignDiaryInfo(circleId, userId, page);
        return ResponseMsgUtil.success(result);
    }

    /**
     * 增加日志浏览量，并不仅仅日志信息的
     *
     * @return
     */
    @ParamCheck
    @GetMapping("/diary/add/browse")
    public Result<String> addDiaryBrowse(@RequestHeader(value = "token", required = false) String userId,
                                         @RequestParam(value = "diaryId", required = false) Long diaryId) {
        userDiaryService.addDiaryBrowseNumber(userId, diaryId);
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }
}
