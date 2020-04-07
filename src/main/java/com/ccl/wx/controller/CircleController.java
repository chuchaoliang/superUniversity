package com.ccl.wx.controller;

import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.entity.UserDiary;
import com.ccl.wx.mapper.CircleInfoMapper;
import com.ccl.wx.mapper.JoinCircleMapper;
import com.ccl.wx.mapper.UserDiaryMapper;
import com.ccl.wx.service.impl.CircleServiceImpl;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.util.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 圈子相关
 *
 * @author 褚超亮
 * @date 2019/10/15 8:50
 */
@RestController
@RequestMapping("/wx")
public class CircleController {

    @Autowired
    private UserDiaryMapper userDiaryMapper;

    @Autowired
    private JoinCircleMapper joinCircleMapper;

    @Autowired
    private CircleInfoMapper circleInfoMapper;

    @Autowired
    private CircleServiceImpl circleService;

    /**
     * 更换圈子的头像
     * TODO API 待修改
     *
     * @param hImage   头像地址
     * @param circleid 圈子id
     * @return
     */
    @PostMapping("/savehimage")
    public String headImage(@RequestParam(value = "hImage", required = false) MultipartFile hImage,
                            @RequestParam(value = "circleid", required = false) String circleid,
                            @RequestParam(value = "userid", required = false) String userid) {
        System.out.println(circleid + ":" + userid);
        if (hImage == null || StringUtils.isEmpty(circleid) || StringUtils.isEmpty(userid)) {
            return "fail";
        } else {
            String fImagePath = FtpUtil.uploadFile(userid, hImage);
            // 删除之前的图片,并更新图片
            Boolean imageB = circleService.updateCircleHeadImage(circleid, fImagePath);
            if (imageB) {
                return "success";
            } else {
                return "delfail";
            }
        }
    }

    /**
     * 上传录音
     *
     * @param voice  录音文件
     * @param userid 用户id
     * @param id     日记id
     * @return
     */
    @PostMapping("/savevoice")
    public String saveVoice(@RequestParam(value = "voice", required = false) MultipartFile voice,
                            @RequestParam(value = "userid", required = false) String userid,
                            @RequestParam(value = "id", required = false) String id) {
        if (voice == null || StringUtils.isEmpty(userid) || StringUtils.isEmpty(id)) {
            return "fail";
        } else {
            UserDiary userDiary = userDiaryMapper.selectByPrimaryKey(Long.valueOf(id));
            String voicePath = FtpUtil.uploadFile(userid, voice);
            userDiary.setDiaryVoice(voicePath);
            userDiaryMapper.updateByPrimaryKeySelective(userDiary);
            return "success";
        }
    }

    /**
     * 退出圈子
     * TODO 退出圈子可以添加一个状态码，给圈主发送退出消息 待重构
     *
     * @param circleid 圈子id
     * @param userid   用户id
     * @return
     */
    @GetMapping("/exitqz")
    public String exitCircle(@RequestParam(value = "circleid", required = false) String circleid,
                             @RequestParam(value = "userid", required = false) String userid) {
        if (StringUtils.isEmpty(circleid) || StringUtils.isEmpty(userid)) {
            return "fail";
        } else {
            // 退出圈子，圈子人数-1
            circleInfoMapper.updateCircleMemberByCircleId(Long.valueOf(circleid), -1);
            JoinCircle circleInfo = joinCircleMapper.selectByPrimaryKey(Long.valueOf(circleid), userid);
            circleInfo.setExitTime(new Date());
            circleInfo.setUserStatus(2);
            System.out.println(circleInfo);
            joinCircleMapper.updateByPrimaryKeySelective(circleInfo);
            return "success";
        }
    }

    /**
     * 加入圈子
     * TODO API 未检测圈子是否为空，重复加入圈子待检测
     *
     * @param circleid 圈子id
     * @param userid   用户id
     * @return
     */
    @GetMapping("/joinqz")
    public String joinCircle(@RequestParam(value = "circleid", required = false) String circleid,
                             @RequestParam(value = "userid", required = false) String userid) {
        // 如果为圈主不需要检查密码即可加入圈子
        if (StringUtils.isEmpty(circleid) || StringUtils.isEmpty(userid)) {
            return "fail";
        } else {
            return circleService.joinCircle(circleid, userid);
        }
    }

    /**
     * TODO API
     * 根据密码加入圈子
     *
     * @param circleid  圈子id
     * @param userid    用户id
     * @param cpassword 圈子密码
     * @return
     */
    @GetMapping("/joinqzbyp")
    public String joinCircleByPassword(@RequestParam(value = "circleid", required = false) String circleid,
                                       @RequestParam(value = "userid", required = false) String userid,
                                       @RequestParam(value = "cpassword", required = false) String cpassword) {
        System.out.println("输入密码：" + cpassword);
        if (StringUtils.isEmpty(circleid) || StringUtils.isEmpty(userid) || StringUtils.isEmpty(cpassword)) {
            return "fail";
        } else {
            return circleService.joinPrivacyCircleByPassword(circleid, userid, cpassword);
        }
    }

    /**
     * TODO API
     * 检测圈子密码是否为空
     *
     * @param circleid 圈子id
     * @return
     */
    @GetMapping("/judgecp")
    public String judgeCirclePasswordExists(@RequestParam(value = "circleid") String circleid) {
        if (StringUtils.isEmpty(circleid)) {
            return "fail";
        } else {
            CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(Long.valueOf(circleid));
            if (StringUtils.isEmpty(circleInfo.getCirclePassword())) {
                return "-1";
            } else {
                return "success";
            }
        }
    }

    /**
     * 判断用户是否为圈主
     *
     * @param circleid 圈子id
     * @param userid   用户id
     * @return
     */
    @GetMapping("/judgecircle")
    public String judgeCircle(@RequestParam(value = "circleid", required = false) String circleid,
                              @RequestParam(value = "userid", required = false) String userid) {
        if (StringUtils.isEmpty(circleid) || StringUtils.isEmpty(userid)) {
            return "-1";
        } else {
            CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(Long.valueOf(circleid));
            if (circleInfo.getCircleUserid().equals(userid)) {
                // 是圈主
                return "success";
            } else {
                // 不是圈主
                return "fail";
            }
        }
    }

    /**
     * 判断是否为私密圈子
     *
     * @param circleid
     * @return
     */
    @GetMapping("/jprivacycircle")
    public String judgeCirclePrivacy(@RequestParam(value = "circleid", required = false) Long circleid) {
        if (StringUtils.isEmpty(circleid)) {
            return "fail";
        } else {
            Boolean cPrivacy = circleService.judgeCirclePrivacyStatus(circleid);
            if (cPrivacy) {
                // 是私密圈子
                return "success";
            } else {
                // 不是私密圈子
                return "-1";
            }
        }
    }

    /**
     * 判断用户是否可以直接进入私密圈子
     *
     * @param userid
     * @param circleid
     * @return
     */
    @GetMapping("/juserintoprivacy")
    public String judgeUserIntoPrivacyCircle(@RequestParam(value = "userid", required = false) String userid,
                                             @RequestParam(value = "circleid", required = false) Long circleid) {
        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(circleid)) {
            return "fail";
        } else {
            Boolean userPrivacy = circleService.judgeUserIntoPrivacyCircle(userid, circleid);
            if (userPrivacy) {
                // 可以直接进入
                return "success";
            } else {
                // 不能直接进入
                return "-1";
            }
        }
    }
}