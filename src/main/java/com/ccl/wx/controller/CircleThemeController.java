package com.ccl.wx.controller;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.common.Result;
import com.ccl.wx.dto.CircleTodayContentDTO;
import com.ccl.wx.entity.TodayContent;
import com.ccl.wx.service.TodayContentService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.ResponseMsgUtil;
import com.ccl.wx.vo.CircleThemeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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

    /**
     * TODO
     * 根据每日内容id获取今天的内容
     *
     * @param themeId 每日内容id
     * @return
     */
    @ApiOperation(value = "根据主题id获取主题相关信息")
    @SneakyThrows
    @ParamCheck
    @GetMapping("/theme/get/one")
    public Result<String> getCircleTodayContentById(@RequestParam(value = "themeId", required = false) Long themeId,
                                            @RequestParam(value = "circleId", required = false) Long circleId) {
        return ResponseMsgUtil.success(todayContentService.selectCircleThemeInfoById(themeId, circleId));
    }

    @Resource
    private TodayContentService todayContentService;

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
    public String getThemeUserSignInInfo(@RequestParam(value = "userId", required = false) String userId,
                                         @RequestParam(value = "circleId", required = false) Long circleId,
                                         @RequestParam(value = "page", required = false) Integer page) {
        return todayContentService.selectAllThemeByCircleIdPage(circleId, userId, page, true);
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
    public String getAllCircleHomeTheme(@RequestParam(value = "circleId", required = false) Long circleId,
                                        @RequestParam(value = "userId", required = false) String userId) {
        List<CircleThemeVO> circleThemeVOS = todayContentService.selectAllThemeByCircleHome(userId, circleId);
        return JSON.toJSONStringWithDateFormat(circleThemeVOS, DatePattern.CHINESE_DATE_PATTERN, SerializerFeature.WriteDateUseDateFormat);
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
    public String getAllThemeByCircleIdPage(@RequestParam(value = "circleId", required = false) Long circleId,
                                            @RequestParam(value = "userId", required = false) String userId,
                                            @RequestParam(value = "page", required = false) Integer page) {
        return todayContentService.selectAllThemeByCircleIdPage(circleId, userId, page, false);
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
    public String deleteTodayContentById(@RequestParam(value = "circleId", required = false) Long circleId,
                                         @RequestParam(value = "contentId", required = false) Long contentId) {
        return todayContentService.deleteCircleTheme(circleId, contentId);
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
    public String saveTodayContent(TodayContent todayContent,
                                   @RequestParam(value = "userId", required = false) String userId,
                                   @RequestPart(value = "image", required = false) MultipartFile image) {
        return todayContentService.saveCircleThemeContent(todayContent, userId, image);
    }

    /**
     * @param userId 用户id
     * @param id     圈子主题id
     * @param voice  音频文件
     * @return
     */
    @ApiOperation(value = "保存圈子主题的音频")
    @PostMapping("/theme/add/voice")
    public String saveCircleThemeVoice(@RequestParam(value = "userId", required = false) String userId,
                                       @RequestParam(value = "id", required = false) Integer id,
                                       @RequestPart(value = "voice", required = false) MultipartFile voice) {
        return todayContentService.saveCircleThemeVoice(userId, id, voice);
    }

    /**
     * @param userId 用户id
     * @param id     主题id
     * @param video  视频文件
     * @return
     */
    @ApiOperation(value = "保存圈子主题的视频")
    @PostMapping("/theme/add/video")
    public String saveCircleThemeVideo(@RequestParam(value = "userId", required = false) String userId,
                                       @RequestParam(value = "id", required = false) Integer id,
                                       @RequestPart(value = "video", required = false) MultipartFile video) {
        return todayContentService.saveCircleThemeVideo(userId, id, video);
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
    @PostMapping("/theme/add/image")
    public String saveTodayImage(@RequestPart(value = "image", required = false) MultipartFile image,
                                 @ParamCheck @RequestParam(value = "userId", required = false) String userId,
                                 @ParamCheck @RequestParam(value = "id", required = false) Long id) {
        return todayContentService.saveEverydayImage(image, userId, id);
    }

    /**
     * TODO
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
    public String updateTodayContent(@RequestBody(required = false) CircleTodayContentDTO circleTodayContentDTO) {
        // id 图片列表：TodayImages 每日内容： TodayContent
        if (StringUtils.isEmpty(circleTodayContentDTO.getId())) {
            // 如果前端传输来的id为空,失败！
            return "fail";
        } else {
            return todayContentService.updateCircleTodayContent(circleTodayContentDTO);
        }
    }
}
