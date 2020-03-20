package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.mapper.CircleInfoMapper;
import com.ccl.wx.mapper.JoinCircleMapper;
import com.ccl.wx.service.impl.CircleServiceImpl;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.util.CclUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 圈子信息相关
 *
 * @author 褚超亮
 * @date 2019/10/27 11:00
 */
@Api(tags = {"CircleInfoController【圈子信息相关的数据信息】"})
@RestController
@RequestMapping("/wx")
public class CircleInfoController {

    @Autowired
    private CircleInfoMapper circleInfoMapper;

    @Autowired
    private JoinCircleMapper joinCircleMapper;

    @Autowired
    private CircleServiceImpl circleService;

    /**
     * 创建圈子 将圈子活力设置为0 等等。。。
     *
     * @param circleInfo 圈子信息数据
     * @return
     */
    @PostMapping("/foundqz")
    public String foundCircle(@RequestBody(required = false) CircleInfo circleInfo) {
        ArrayList<String> strs = new ArrayList<>();
        // 圈主id
        strs.add("circleUserid");
        // 圈子名称
        strs.add("circleName");
        // 圈子位置
        strs.add("circleLocation");
        // 圈子设置
        strs.add("circleSet");
        if (CclUtil.classPropertyIsNull(circleInfo, strs)) {
            // 检测圈子名字是否重复
            return circleService.fondCircle(circleInfo);
        } else {
            // 前端数据有的为空
            return "fail";
        }
    }

    /**
     * 检测圈子名称是否重复
     * 圈子名字重复fail 否则success
     * TODO API
     *
     * @param circlename 圈子名称
     * @return
     */
    @GetMapping("/checkqzname")
    public String checkCircleNameRepetition(@RequestParam(value = "circlename", required = false) String circlename) {
        List<String> circleNames = circleInfoMapper.selectAllCircleName();
        if (circleNames.contains(circlename)) {
            return "fail";
        } else {
            return "success";
        }
    }

    /**
     * 根据圈子id查找圈子数据
     *
     * @param id 圈子id
     * @return
     */
    @ApiOperation(value = "根据圈子id查找圈子数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "圈子id", dataType = "String", example = "3"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", example = "o1x2q5czO_xCH9eemeEfL41_gvMk")
    })
    @GetMapping("/getcircle")
    public String getCircle(@RequestParam(value = "id", required = false) String id,
                            @RequestParam(value = "userId", required = false) String userId) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(userId)) {
            return "fail";
        } else {
            return circleService.getCircleIndexAllContent(userId, id);
        }
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
            List<CircleInfo> circles = circleInfoMapper.findAllByCircleLocation(Integer.parseInt(tid));
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
            List<CircleInfo> circles = circleInfoMapper.selectAllLikeAndCircleName(keyword);
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
        List<CircleInfo> circles = circleInfoMapper.selectAllByCircleNameLikeAndCircleLocation(keyword, Integer.parseInt(ctype));
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
            List<JoinCircle> circles = joinCircleMapper.selectAllByUserIdAndUserPermission(userid, 0);
            for (JoinCircle circle : circles) {
                circleids.add(circle.getCircleId());
            }
            for (Long circleid : circleids) {
                CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(circleid);
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
            List<JoinCircle> circles = joinCircleMapper.selectAllByUserIdAndUserPermission(userid, 2);
            for (JoinCircle circle : circles) {
                circleids.add(circle.getCircleId());
            }
            for (Long circleid : circleids) {
                CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(circleid);
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
            List<JoinCircle> circles = joinCircleMapper.selectAllByUserIdAndUserPermission(userid, 0);
            List<JoinCircle> foundCircles = joinCircleMapper.selectAllByUserIdAndUserPermission(userid, 2);
            circles.addAll(foundCircles);
            for (JoinCircle circle : circles) {
                circleids.add(circle.getCircleId());
            }
            for (Long circleid : circleids) {
                CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(circleid);
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
            CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(Long.valueOf(circleid));
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
            CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(Long.valueOf(circleid));
            if (nullStr.equals(circlelabel) || "".equals(circlelabel) || circlelabel == null) {
                // 删除圈子标签
                circleInfo.setCircleLabel("");
                circleInfoMapper.updateByPrimaryKeySelective(circleInfo);
                return "success";
            } else {
                // 添加或者修改圈子标签
                circleInfo.setCircleLabel(circlelabel);
                circleInfoMapper.updateByPrimaryKeySelective(circleInfo);
                return "success";
            }
        }
    }
}

