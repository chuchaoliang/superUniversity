package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.common.EnumResultCode;
import com.ccl.wx.common.Result;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.service.CircleInfoService;
import com.ccl.wx.service.JoinCircleService;
import com.ccl.wx.service.impl.CircleServiceImpl;
import com.ccl.wx.util.ResponseMsgUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
    private CircleServiceImpl circleService;

    @Resource
    private CircleInfoService circleInfoService;

    /**
     * 创建圈子
     *
     * @param circleInfo 圈子信息数据
     * @return
     */
    @PostMapping("/found")
    public Result<String> foundCircle(@Validated @RequestBody(required = false) CircleInfo circleInfo,
                                      @RequestParam(value = "image", required = false) MultipartFile image,
                                      BindingResult result) {
        if (result.hasErrors()) {
            // 参数校检存在错误
            return ResponseMsgUtil.fail(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        } else {
            // 检测圈子名字是否重复
            circleInfoService.fondCircle(circleInfo, image);
            return ResponseMsgUtil.success("");
        }
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
            return ResponseMsgUtil.success(EnumResultCode.SUCCESS.getStatus(),"昵称可以使用！",JSON.toJSONString(circleName));
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
     * 根据圈子类型获取圈子数据
     *
     * @param tid 圈子类型
     * @return
     */
    @GetMapping("/getcircletype")
    public String getCircleByType(@RequestParam(value = "tid", required = false) String tid) {
        if (StringUtils.isEmpty(tid)) {
            return "fail";
        } else {
            List<CircleInfo> circles = circleInfoService.findAllByCircleLocation(Integer.parseInt(tid));
            String circlesDTO = circleService.selectCircleDTO(circles);
            return circlesDTO;
        }
    }

    /**
     * 根据关键词搜索圈子数据
     *
     * @param keyword 圈子关键词
     * @return
     */
    @GetMapping("/sqzkeyword")
    public String searchCircleByKeyWord(@RequestParam(value = "keyword", required = false) String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return "fail";
        } else {
            List<CircleInfo> circles = circleInfoService.selectAllLikeAndCircleName(keyword);
            String circlesDTO = circleService.selectCircleDTO(circles);
            return circlesDTO;
        }
    }

    /**
     * 根据圈子类型和关键词查询数据
     *
     * @param keyword 关键词
     * @param ctype   圈子类型
     * @return
     */
    @GetMapping("/sqzlkeyword")
    public String searchCircleByLocationKeyWord(@RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestParam(value = "ctype", required = false) String ctype) {
        if (StringUtils.isEmpty(keyword) || StringUtils.isEmpty(ctype)) {
            return "fail";
        }
        List<CircleInfo> circles = circleInfoService.selectAllByCircleNameLikeAndCircleLocation(keyword, Integer.parseInt(ctype));
        String circlesDTO = circleService.selectCircleDTO(circles);
        return circlesDTO;
    }

    /**
     * TODO API 待重构
     * 查找用户所加入的圈子
     *
     * @param userid 用户id
     * @return
     */
    @GetMapping("/sjoincircle")
    public String searchJoinCircle(@RequestParam(value = "userid", required = false) String userid) {
        if (StringUtils.isEmpty(userid)) {
            return "fail";
        } else {
            ArrayList<Long> circleids = new ArrayList<>();
            ArrayList<CircleInfo> circleInfos = new ArrayList<>();
            List<JoinCircle> circles = joinCircleService.selectAllByUserIdAndUserPermission(userid, 0);
            for (JoinCircle circle : circles) {
                circleids.add(circle.getCircleId());
            }
            for (Long circleid : circleids) {
                CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(circleid);
                circleInfos.add(circleInfo);
            }
            String circlesDTO = circleService.selectCircleDTO(circleInfos);
            return circlesDTO;
        }
    }

    /**
     * TODO API 待重构
     * 查找用户创建的圈子
     *
     * @param userid 用户id
     * @return
     */
    @GetMapping("/sfoundcircle")
    public String searchFoundCircle(@RequestParam(value = "userid", required = false) String userid) {
        if (StringUtils.isEmpty(userid)) {
            return "fail";
        } else {
            ArrayList<Long> circleids = new ArrayList<>();
            ArrayList<CircleInfo> circleInfos = new ArrayList<>();
            List<JoinCircle> circles = joinCircleService.selectAllByUserIdAndUserPermission(userid, 2);
            for (JoinCircle circle : circles) {
                circleids.add(circle.getCircleId());
            }
            for (Long circleid : circleids) {
                CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(circleid);
                circleInfos.add(circleInfo);
            }
            String circlesDTO = circleService.selectCircleDTO(circleInfos);
            return circlesDTO;
        }
    }

    /**
     * TODO API
     * 查询用户全部的圈子
     *
     * @param userid 用户id
     * @return
     */
    @GetMapping("/sallcircle")
    public String searchAllCircle(@RequestParam(value = "userid", required = false) String userid) {
        if (StringUtils.isEmpty(userid)) {
            return "fail";
        } else {
            ArrayList<Long> circleids = new ArrayList<>();
            ArrayList<CircleInfo> circleInfos = new ArrayList<>();
            List<JoinCircle> circles = joinCircleService.selectAllByUserIdAndUserPermission(userid, 0);
            List<JoinCircle> foundCircles = joinCircleService.selectAllByUserIdAndUserPermission(userid, 2);
            circles.addAll(foundCircles);
            for (JoinCircle circle : circles) {
                circleids.add(circle.getCircleId());
            }
            for (Long circleid : circleids) {
                CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(circleid);
                circleInfos.add(circleInfo);
            }
            String circlesDTO = circleService.selectCircleDTO(circleInfos);
            return circlesDTO;
        }
    }

    /**
     * 根据圈子id获取数据
     * 前端数据有的为空fail 否则返回圈子信息
     *
     * @param circleid 圈子id
     * @return
     */
    @GetMapping("/getcirclebyid")
    public String getCircleInfoById(@RequestParam(value = "circleid", required = false) String circleid) {
        if (StringUtils.isEmpty(circleid)) {
            return "fail";
        } else {
            CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(Long.valueOf(circleid));
            return JSON.toJSONString(circleInfo);
        }
    }

    /**
     * 修改或者插入圈子标签
     * 前端数据有的为空 fail 否则返回success
     *
     * @param circleid    圈子id
     * @param circlelabel 圈子标签
     * @return
     */
    @GetMapping("/editcirclelabel")
    public String editCircleLabel(@RequestParam(value = "circleid", required = false) String circleid,
                                  @RequestParam(value = "circlelabel", required = false) String circlelabel) {
        String nullStr = "undefined";
        if (StringUtils.isEmpty(circleid)) {
            return "fail";
        } else {
            CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(Long.valueOf(circleid));
            if (nullStr.equals(circlelabel) || "".equals(circlelabel) || circlelabel == null) {
                // 删除圈子标签
                circleInfo.setCircleLabel("");
                circleInfoService.updateByPrimaryKeySelective(circleInfo);
                return "success";
            } else {
                // 添加或者修改圈子标签
                circleInfo.setCircleLabel(circlelabel);
                circleInfoService.updateByPrimaryKeySelective(circleInfo);
                return "success";
            }
        }
    }
}

