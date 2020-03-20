package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.dto.UserDiaryDTO;
import com.ccl.wx.entity.UserDiary;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.service.CircleService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.CclUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

/**
 * 圈子日志相关
 *
 * @author 褚超亮
 * @date 2020/2/20 20:32
 */
@Slf4j
@Api(tags = {"CircleDiaryController【圈子用户日志信息】"})
@RestController
@RequestMapping("/wx")
public class UserDiaryController {

    @Autowired
    private UserDiaryService userDiaryService;

    @Autowired
    private CircleService circleService;

    /**
     * 根据日志的id查询日志的信息
     *
     * @param diaryId 日志id
     * @return （与此日志相关的全部评论、点赞、点评信息）
     */
    @GetMapping("/getonediaryinfo")
    public String getDiaryInfoByDiaryId(@RequestParam(value = "diaryid", required = false) String diaryId) {
        if (StringUtils.isEmpty(diaryId)) {
            // 前端传输的数据为空
            return "fail";
        } else {
            // 获取评论内容
            UserDiaryDTO diaryInfoById = circleService.getDiaryInfoById(Long.valueOf(diaryId));
            return JSON.toJSONStringWithDateFormat(diaryInfoById, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        }
    }

    /**
     * TODO
     * 更新圈子日志信息
     * 1. 获取日记id，日记内容，日记状态，地址信息，图片列表
     * id, diaryContent,diaryStatus,diaryAddress,images
     *
     * @param userDiaryDTO
     * @return
     */
    @PostMapping("/updatecirclediary")
    public String updateCircleDiary(@RequestBody(required = false) UserDiaryDTO userDiaryDTO) {
        if (userDiaryDTO == null) {
            return "fail";
        } else {
            return userDiaryService.updateCircleDiaryContent(userDiaryDTO);
        }
    }

    /**
     * TODO API
     *
     * @param image
     * @param userId
     * @param id
     * @return
     */
    @PostMapping("/savediaryimage")
    public String saveDiaryImage(@RequestPart(value = "image", required = false) MultipartFile image,
                                 @RequestParam(value = "userid", required = false) String userId,
                                 @RequestParam(value = "id", required = false) Long id) {
        if (image == null || StringUtils.isEmpty(id) || StringUtils.isEmpty(userId)) {
            // 前端传来的数据为空 失败
            return "fail";
        } else {
            return userDiaryService.saveDiaryImage(image, userId, id);
        }
    }

    /**
     * 删除用户的日志信息
     * TODO 删除日志信息
     *
     * @param diaryid 日志id
     * @return
     */
    @GetMapping("/deldiary")
    public String deleteCircleDiaryInfo(@ParamCheck @RequestParam(value = "diaryid", required = false) Long diaryid) {
        return userDiaryService.deleteUserDiaryInfo(diaryid);
    }

    /**
     * userId 用户id
     * diaryContent 日记内容 (可为空)
     * diaryStatus  日记状态
     * diaryAddress 用户地址（可为空）
     * circleId 圈子id
     * 如果前端传来的数据不为空则返回此日记的主键id ，否则返回fail
     *
     * @param userDiary
     * @return
     */
    @ApiOperation(value = "用户发表日志", notes = "需要参数userId、diaryContent、diaryStatus、diaryAddress、circleId、themeId", httpMethod = "POST")
    @SneakyThrows
    @PostMapping("/savediarycontent")
    public String saveContent(@RequestBody(required = false) UserDiary userDiary) {
        ArrayList<String> strs = new ArrayList<>();
        strs.add("userId");
        strs.add("circleId");
        if (CclUtil.classPropertyIsNull(userDiary, strs)) {
            return userDiaryService.publishUserDiary(userDiary);
        }
        return EnumResultStatus.FAIL.getValue();
    }

    /**
     * TODO API
     * 获取圈子全部用户的全部日志信息
     *
     * @param circleid 圈子id
     * @param userid
     * @param page
     * @return
     */
    @ApiOperation(value = "获取圈子中的全部日志信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "circleid", value = "圈子id", dataType = "String"),
            @ApiImplicitParam(name = "userid", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int", example = "1"),
    })
    @GetMapping("/getalldiaryinfo")
    public String getDiaryInfo(@ParamCheck @RequestParam(value = "circleid", required = false) String circleid,
                               @ParamCheck @RequestParam(value = "userid", required = false) String userid,
                               @ParamCheck @RequestParam(value = "page", required = false) Integer page) {
        return circleService.getAllDiaryInfo(circleid, userid, page);
    }

    /**
     * 获取某个用户的全部日志信息
     *
     * @param circleid
     * @param userid
     * @param page
     * @return
     */
    @GetMapping("/getassigndiaryinfo")
    public String getAssignDiaryInfo(@RequestParam(value = "circleid", required = false) String circleid,
                                     @RequestParam(value = "userid", required = false) String userid,
                                     @RequestParam(value = "page", required = false) Integer page) {
        if (StringUtils.isEmpty(circleid) || StringUtils.isEmpty(userid) || StringUtils.isEmpty(page)) {
            return "fail";
        } else {
            return circleService.getAssignDiaryInfo(circleid, userid, page);
        }
    }

    /**
     * 获取日志全部点赞信息
     *
     * @param userid
     * @param circleid
     * @param diaryid
     * @return
     */
    @GetMapping("/getalllike")
    public String getAllUserLike(@RequestParam(value = "userid", required = false) String userid,
                                 @RequestParam(value = "circleid", required = false) String circleid,
                                 @RequestParam(value = "diaryid", required = false) Long diaryid) {
        return JSON.toJSONStringWithDateFormat(circleService.getAllLikeUserNickName(userid, circleid, diaryid), "yyyy-MM-dd mm:ss", SerializerFeature.DisableCircularReferenceDetect);
    }


    /**
     * 根据日志id查询该日志信息
     *
     * @param diaryId
     * @return
     */
    @GetMapping("/getonediarybyid")
    public String getDiaryById(@RequestParam(value = "diaryId", required = false) Long diaryId) {
        if (StringUtils.isEmpty(diaryId)) {
            return "fail";
        }
        return userDiaryService.getCircleDiaryById(diaryId);
    }

    /**
     * 增加日志浏览量，并不仅仅日志信息的
     *
     * @return
     */
    @GetMapping("/diary/add/browse")
    public String addDiaryBrowse(@ParamCheck @RequestParam(value = "userId", required = false) String userId,
                                 @ParamCheck @RequestParam(value = "diaryId", required = false) Long diaryId) {
        userDiaryService.addDiaryBrowseNumber(userId, diaryId);
        return "success";
    }
}
