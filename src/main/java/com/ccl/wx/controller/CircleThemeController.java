package com.ccl.wx.controller;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.common.EnumResultCode;
import com.ccl.wx.common.Result;
import com.ccl.wx.dto.CircleTodayContentDTO;
import com.ccl.wx.entity.TodayContent;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.service.TodayContentService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.ResponseMsgUtil;
import com.ccl.wx.vo.CircleThemeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 圈子今日内容相关
 *
 * @author 褚超亮
 * @date 2020/1/12 10:16
 */
@Api(tags = {"CircleThemeController【圈子主题相关操作信息】"})
@RestController
@RequestMapping("/wx/circle")
public class CircleThemeController {

    @Resource
    private UserDiaryService circleDiaryService;

    @Resource
    private TodayContentService todayContentService;

    /**
     * TODO
     * 根据每日内容id获取今天的内容
     *
     * @param themeId 每日内容id
     * @return
     */
    @ApiOperation(value = "根据主题id获取主题相关信息")
    @ParamCheck
    @GetMapping("/theme/get/one")
    public Result<String> getCircleTodayContentById(@RequestParam(value = "themeId", required = false) Long themeId,
                                                    @RequestParam(value = "circleId", required = false) Long circleId) {
        String result = todayContentService.selectCircleThemeInfoById(themeId, circleId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("此主题不存在，可能被删除了！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 用户打卡获取主题相关信息
     *
     * @param userId   用户id
     * @param circleId 圈子id
     * @param page     第几页
     * @return
     */
    @ParamCheck
    @ApiOperation(value = "用户打卡获取主题相关信息")
    @GetMapping("/theme/user/signin")
    public Result<String> getThemeUserSignInInfo(@RequestHeader(value = "token", required = false) String userId,
                                                 @RequestParam(value = "circleId", required = false) Long circleId,
                                                 @RequestParam(value = "page", required = false) Integer page,
                                                 @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        String result = todayContentService.selectAllThemeByCircleIdPage(circleId, userId, page, true, date);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("此用户未加入此圈子，或者被淘汰，状态异常！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * 获取圈子首页的主题
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @return
     */
    @ParamCheck
    @ApiOperation(value = "获取圈子首页的主题")
    @GetMapping("/theme/get/home")
    public Result<String> getAllCircleHomeTheme(@RequestParam(value = "circleId", required = false) Long circleId,
                                                @RequestHeader(value = "token", required = false) String userId) {
        List<CircleThemeVO> circleThemeVOS = todayContentService.selectAllThemeByCircleHome(userId, circleId);
        return ResponseMsgUtil.success(JSON.toJSONStringWithDateFormat(circleThemeVOS, DatePattern.CHINESE_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat));
    }

    /**
     * 根据圈子id获取主题信息
     *
     * @param circleId 圈子id
     * @param userId   用户id
     * @param page     第几页
     * @return
     */
    @ApiOperation(value = "根据圈子id获取主题信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "circleId", value = "圈子Id", dataType = "long", example = "5"),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int", example = "0"),
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", example = "o1x2q5czO_xCH9eemeEfL41_gvMk")
    })
    @ParamCheck
    @GetMapping("/theme/get/all/page")
    public Result<String> getAllThemeByCircleIdPage(@RequestParam(value = "circleId", required = false) Long circleId,
                                                    @RequestHeader(value = "token", required = false) String userId,
                                                    @RequestParam(value = "page", required = false) Integer page) {
        String result = todayContentService.selectAllThemeByCircleIdPage(circleId, userId, page, false, null);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("此用户未加入此圈子，或者被淘汰，状态异常！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * TODO
     * 删除每日的内容，将内容状态设置为2
     *
     * @param contentId 删除内容的id
     * @return
     */
    @ApiOperation(value = "根据id删除主题")
    @ParamCheck
    @ApiImplicitParam(name = "contentId", value = "圈子主题Id", dataType = "Long", example = "1")
    @GetMapping("/theme/del")
    public Result<String> deleteTodayContentById(@RequestParam(value = "circleId", required = false) Long circleId,
                                                 @RequestParam(value = "themeId", required = false) Long contentId) {
        String result = todayContentService.deleteCircleTheme(circleId, contentId);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("该主题可能已经被删除了！");
        }
        return ResponseMsgUtil.success(EnumResultCode.SUCCESS);
    }

    /**
     * TODO
     * 保存圈子每日内容
     * 1. 设置文本内容可以为空、设置创建时间、设置圈子id、设置状态为0
     * 2. 将这个圈子以前为0的那个状态设置为1
     * 3. 将圈子的每日内容id更改
     *
     * @param todayContent 每日内容对象
     * @return
     */
    @ApiOperation(value = "添加圈子主题内容和标题")
    @PostMapping("/theme/add/content")
    public Result<String> saveTodayContent(TodayContent todayContent,
                                           @RequestHeader(value = "token", required = false) String userId,
                                           @RequestPart(value = "image", required = false) MultipartFile image) {
        String result = todayContentService.saveCircleThemeContent(todayContent, userId, image);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("添加主题失败！");
        } else {
            return ResponseMsgUtil.success(result);
        }
    }

    /**
     * @param userId 用户id
     * @param id     圈子主题id
     * @param voice  音频文件
     * @return
     */
    @ParamCheck
    @ApiOperation(value = "保存圈子主题的音频")
    @PostMapping("/theme/add/voice")
    public Result<String> saveCircleThemeVoice(@RequestHeader(value = "token", required = false) String userId,
                                               @RequestParam(value = "id", required = false) Integer id,
                                               @RequestPart(value = "voice", required = false) MultipartFile voice) {
        String result = todayContentService.saveCircleThemeVoice(userId, id, voice);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("音频上传失败，重复上传，或者对应的主题被删除了！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * @param userId 用户id
     * @param id     主题id
     * @param video  视频文件
     * @return
     */
    @ParamCheck
    @ApiOperation(value = "保存圈子主题的视频")
    @PostMapping("/theme/add/video")
    public Result<String> saveCircleThemeVideo(@RequestHeader(value = "token", required = false) String userId,
                                               @RequestParam(value = "id", required = false) Integer id,
                                               @RequestPart(value = "video", required = false) MultipartFile video) {
        String result = todayContentService.saveCircleThemeVideo(userId, id, video);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("视频上传失败，重复上传，或者对应的主题被删除了！");
        }
        return ResponseMsgUtil.success(result);
    }

    /**
     * TODO
     * 上传每日内容的图片
     *
     * @param image  图片
     * @param userId 用户id
     * @param id     每日内容的id主键
     * @return success fail
     */
    @ApiOperation(value = "保存圈子主题图片")
    @ParamCheck
    @PostMapping("/theme/add/image")
    public Result<String> saveTodayImage(@RequestPart(value = "image", required = false) MultipartFile image,
                                         @RequestHeader(value = "token", required = false) String userId,
                                         @RequestParam(value = "id", required = false) Long id) {
        String result = todayContentService.saveEverydayImage(image, userId, id);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("图片上传失败！或者主题被删除了哦！");
        } else {
            return ResponseMsgUtil.success(result);
        }
    }

    /**
     * 保存主题头像照片
     *
     * @param image  图片文件
     * @param userId 用户id
     * @param id     主题id
     * @return
     */
    @ParamCheck
    @PostMapping("/theme/add/image/head")
    public Result<String> saveThemeHeadImage(@RequestPart(value = "image", required = false) MultipartFile image,
                                             @RequestHeader(value = "token", required = false) String userId,
                                             @RequestParam(value = "id", required = false) Long id) {
        String result = todayContentService.saveThemeHeadImage(image, userId, id);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("图片上传失败！或者主题被删除了哦！");
        } else {
            return ResponseMsgUtil.success(result);
        }
    }

    /**
     * 更新每日内容
     * 1. 获取每日内容的文本内容
     * 2. 获取图片地址，得到以前的图片地址，并且根据图片的网络地址删除图片地址
     * 若以前没有图片，不删除直接插入图片，并更新数据
     * 3. 删除图片后，重新上传之前更新的地址
     *
     * @param circleTodayContentDTO 需要获取的对象 ： 今日内容ID（*） 今日内容（可以为空）
     * @return
     */
    @ApiOperation(value = "更新圈子主题信息")
    @SneakyThrows
    @PostMapping("/theme/update")
    public Result<String> updateTodayContent(@RequestBody(required = false) CircleTodayContentDTO circleTodayContentDTO) {
        String result = todayContentService.updateCircleTodayContent(circleTodayContentDTO);
        if (EnumResultStatus.FAIL.getValue().equals(result)) {
            return ResponseMsgUtil.fail("该主题不存在，可能被删除了！");
        }
        return ResponseMsgUtil.success(result);
    }
}
