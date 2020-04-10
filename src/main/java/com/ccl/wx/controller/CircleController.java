package com.ccl.wx.controller;

import com.ccl.wx.entity.UserDiary;
import com.ccl.wx.mapper.CircleInfoMapper;
import com.ccl.wx.mapper.JoinCircleMapper;
import com.ccl.wx.mapper.UserDiaryMapper;
import com.ccl.wx.service.impl.CircleServiceImpl;
import com.ccl.wx.util.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}