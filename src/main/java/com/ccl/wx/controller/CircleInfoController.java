package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.common.EnumResultCode;
import com.ccl.wx.common.Result;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.service.CircleInfoService;
import com.ccl.wx.service.CircleIntroService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.util.ResponseMsgUtil;
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
 * 圈子信息相关
 *
 * @author 褚超亮
 * @date 2019/10/27 11:00
 */
@Api(tags = {"CircleInfoController【圈子信息相关的数据信息】"})
@Slf4j
@RestController
@RequestMapping("/wx/circle")
public class CircleInfoController {

    @Resource
    private JoinCircleService joinCircleService;

    @Resource
    private CircleInfoService circleInfoService;

    @Resource
    private CircleIntroService circleIntroService;

    /**
     * 创建圈子
     *
     * @param circleInfo 圈子信息数据
     * @return
     */
    @PostMapping("/found")
    public Result<String> foundCircle(@Validated CircleInfo circleInfo,
                                      @RequestHeader(value = "token", required = false) String userId,
                                      @RequestParam(value = "image", required = false) MultipartFile image,
                                      BindingResult result) {
        if (result.hasErrors()) {
            // 参数校检存在错误
            return ResponseMsgUtil.fail(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        circleInfo.setCircleUserid(userId);
        // 检测圈子名字是否重复
        String resultResponse = circleInfoService.fondCircle(circleInfo, image);
        if (EnumResultStatus.FAIL.getValue().equals(resultResponse)) {
            return ResponseMsgUtil.fail("创建失败！");
        }
        return ResponseMsgUtil.success(resultResponse);
    }

    /**
     * 检测圈子名称是否重复
     *
     * @param circleName 圈子名称
     * @return
     */
    @GetMapping("/check/name")
    public Result<String> checkCircleNameRepetition(@RequestParam(value = "circleName", required = false) String circleName) {
        if (circleInfoService.checkCircleName(circleName)) {
            return ResponseMsgUtil.success(EnumResultCode.SUCCESS.getStatus(), "昵称可以使用！", JSON.toJSONString(circleName));
        } else {
            return ResponseMsgUtil.fail(EnumResultCode.CIRCLE_NAME_REPETITION.getStatus(), "圈子昵称重复！-->" + circleName);
        }
    }

    /**
     * 根据圈子id查找圈子数据
     *
     * @param circleId 圈子id
     * @return
     */
    @ApiOperation(value = "根据圈子id查找圈子数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "圈子id", dataType = "String", example = "3"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", example = "o1x2q5czO_xCH9eemeEfL41_gvMk")
    })
    @ParamCheck
    @GetMapping("/home")
    public Result<String> getCircle(@RequestParam(value = "circleId", required = false) Long circleId,
                                    @RequestHeader(value = "token", required = false) String userId) {
        return ResponseMsgUtil.success(circleInfoService.getCircleIndexAllContent(userId, circleId.intValue()));
    }

    /**
     * 根据圈子类型获取圈子
     *
     * @param type   圈子类型
     * @param userId 用户id
     * @param page   第几页
     * @return
     */
    @ParamCheck
    @GetMapping("/get/type")
    public Result<String> getCircleByType(@RequestParam(value = "type", required = false) Integer type,
                                          @RequestHeader(value = "token", required = false) String userId,
                                          @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        return ResponseMsgUtil.success(circleInfoService.selectCircleByType(type, userId, page));
    }

    /**
     * 根据关键词搜索圈子数据
     *
     * @param keyword 圈子关键词
     * @param userId  用户id
     * @param page    页数
     * @return
     */
    @ParamCheck
    @GetMapping("/search/keyword")
    public Result<String> searchCircleByKeyWord(@RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestHeader(value = "token", required = false) String userId,
                                                @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String result = circleInfoService.searchCircleByKeyWord(keyword, userId, page);
        return ResponseMsgUtil.success(result);
    }


    /**
     * 根据圈子类型和关键词查询数据
     *
     * @param keyword 关键词
     * @param type    圈子类型
     * @param page    页数
     * @return
     */
    @ParamCheck
    @GetMapping("/search/keyword/type")
    public Result<String> searchCircleByTypeKeyWord(@RequestParam(value = "keyword", required = false) String keyword,
                                                    @RequestParam(value = "type", required = false) Integer type,
                                                    @RequestHeader(value = "token", required = false) String userId,
                                                    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        String result = circleInfoService.searchCircleByTypeKeyWord(keyword, type, userId, page);
        return ResponseMsgUtil.success(result);
    }


    /**
     * 我加入的圈子
     *
     * @param userId 用户id
     * @return
     */
    @ParamCheck
    @GetMapping("/get/my/join")
    public Result<String> searchJoinCircle(@RequestHeader(value = "token", required = false) String userId,
                                           @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        return ResponseMsgUtil.success(joinCircleService.selectUserJoinCircle(userId, page));
    }

    /**
     * 我创建的圈子
     *
     * @param userId 用户id
     * @return
     */
    @ParamCheck
    @GetMapping("/get/my/found")
    public Result<String> searchFoundCircle(@RequestHeader(value = "token", required = false) String userId,
                                            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        return ResponseMsgUtil.success(joinCircleService.selectUserFoundCircle(userId, page));
    }

    /**
     * 判断用户是否可以直接进入私密圈子
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @return
     */
    @ParamCheck
    @GetMapping("/check/privacy")
    public Result<String> judgeUserIntoPrivacyCircle(@RequestHeader(value = "token", required = false) String userId,
                                                     @RequestParam(value = "circleId", required = false) Long circleId) {
        String result = circleInfoService.judgeUserIntoPrivacyCircle(userId, circleId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("进入圈子失败！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }
}

