package com.ccl.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.dto.CircleMemberDTO;
import com.ccl.wx.dto.JoinCircleDTO;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.entity.JoinCircle;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.mapper.*;
import com.ccl.wx.service.CircleService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO 常量值待封装
 *
 * @author 褚超亮
 * @date 2019/10/30 20:47
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CircleServiceImpl implements CircleService {

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private JoinCircleMapper joinCircleMapper;

    @Autowired
    private CircleInfoMapper circleInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserDiaryService userDiaryService;

    /**
     * 文件上传路径
     */
    private static String FILE_LOCAL_PATH;

    /**
     * 常见的图片类型
     */
    private final static String IMAGE_TYPE = "jpeg,png,gif,bmp,jpg";

    /**
     * 常见的音频类型
     */
    private final static String VOICE_TYPE = "m4a,wav,mp3,aac";

    @Value("${file.upload.local.path}")
    private void setFILE_LOCAL_PATH(String FILE_LOCAL_PATH) {
        CircleServiceImpl.FILE_LOCAL_PATH = FILE_LOCAL_PATH;
    }

    /**
     * 圈子默认首页图片路径
     */
    private static String CIRCLE_HEAD_IMAGE;

    @Value("${circle.head.image}")
    private void setCIRCLE_HEAD_IMAGE(String CIRCLE_HEAD_IMAGE) {
        CircleServiceImpl.CIRCLE_HEAD_IMAGE = CIRCLE_HEAD_IMAGE;
    }

    /**
     * 文件保存默认前缀文件夹名称
     */
    private static String FILE_USELESS_PREFIX;

    @Value("${file.upload.useless.local.path}")
    private void setFILE_USELESS_PREFIX(String FILE_USELESS_PREFIX) {
        CircleServiceImpl.FILE_USELESS_PREFIX = FILE_USELESS_PREFIX;
    }

    /**
     * 存放文件前缀
     */
    @Value("${easy.server.path}")
    private String CIRCLE_HEAD_IMAGE_PREFIX;

    /**
     * 评论删除状态
     */
    private final static Integer COMMENT_DELETE_STATUS = 1;

    /**
     * 成功状态
     */
    private final static String SUCCESS = "success";

    /**
     * redis存储的点赞前缀
     */
    private final static String LIKE_PREFIX = "like::";

    /**
     * redis存储的点赞和前缀
     */
    private final static String LIKE_SUM_PREFIX = "account::";

    /**
     * redis中连接符号
     */
    private final static String CONNECT_VALUE = "::";

    /**
     * redis中私圈子私密状态前缀
     */
    private final static String CIRCLE_PRIVACY_PREFIX = "privacy";

    /**
     * 点赞状态
     */
    private final static Integer LIKE_STATUS = 1;

    /**
     * 取消点赞状态
     */
    private final static Integer UNLIKE_STATUS = 0;

    /**
     * 每页总数
     */
    private final static Integer PAGE_AMOUNT = 10;

    /**
     * 私密圈子状态
     */
    private final static Integer CIRCLE_PASSWORD_STATUS = 2;

    @Autowired
    private CircleService circleService;

    @Override
    public Boolean updateCircleHeadImage(String circleid, String filePath) {
        CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(Long.valueOf(circleid.trim()));
        if (circleInfo.getCircleHimage().equals(CIRCLE_HEAD_IMAGE)) {
            // 圈子头像为默认头像直接更新
            circleInfo.setCircleHimage(filePath);
            circleInfoMapper.updateByPrimaryKeySelective(circleInfo);
            return true;
        } else {
            // 删除原来圈子头像，并进行更新
            String filepath = circleInfo.getCircleHimage();
            String str = FtpUtil.delFile(filepath);
            if (SUCCESS.equals(str)) {
                // 删除成功
                circleInfo.setCircleHimage(filePath);
                circleInfoMapper.updateByPrimaryKeySelective(circleInfo);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public String selectCircleAllMemberDTO(List<JoinCircle> circles) {
        ArrayList<CircleMemberDTO> circleMemberDTOS = new ArrayList<>();
        for (JoinCircle circle : circles) {
            String masterUser = circleInfoMapper.selectByPrimaryKey(circle.getCircleId()).getCircleUserid();
            if (masterUser.equals(circle.getUserId())) {
                continue;
            }
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(circle.getUserId());
            CircleMemberDTO circleMemberDTO = new CircleMemberDTO();
            BeanUtils.copyProperties(circle, circleMemberDTO);
            circleMemberDTO.setNickName(userInfo.getNickname());
            circleMemberDTO.setImageUrl(userInfo.getAvatarurl());
            circleMemberDTO.setGender(userInfo.getGender());
            circleMemberDTOS.add(circleMemberDTO);
        }
        return JSON.toJSONStringWithDateFormat(circleMemberDTOS, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public List<String> getCircleAllMember(String circleid) {
        List<JoinCircle> circles1 = joinCircleMapper.selectAllByCircleIdAndUserStatus(Long.valueOf(circleid), 0);
        List<JoinCircle> circles2 = joinCircleMapper.selectAllByCircleIdAndUserStatus(Long.valueOf(circleid), 1);
        List<JoinCircle> circles3 = joinCircleMapper.selectAllByCircleIdAndUserStatus(Long.valueOf(circleid), 3);
        String str1 = circleService.selectCircleAllMemberDTO(circles1);
        String str2 = circleService.selectCircleAllMemberDTO(circles2);
        String str3 = circleService.selectCircleAllMemberDTO(circles3);
        ArrayList<String> circleAllMember = new ArrayList<>();
        circleAllMember.add(str1);
        circleAllMember.add(str2);
        circleAllMember.add(str3);
        return circleAllMember;
    }

    @Override
    public String getUserInCircleInfo(String userid, Long circleid) {
        JoinCircle joinCircle = joinCircleMapper.selectByPrimaryKey(circleid, userid);
        if (joinCircle != null) {
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
            CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(circleid);
            JoinCircleDTO joinCircleDTO = new JoinCircleDTO();
            BeanUtils.copyProperties(joinCircle, joinCircleDTO);
            joinCircleDTO.setUserNickName(userInfo.getNickname());
            joinCircleDTO.setAvatarurl(userInfo.getAvatarurl());
            joinCircleDTO.setGender(userInfo.getGender());
            joinCircleDTO.setCircleName(circleInfo.getCircleName());
            return JSON.toJSONStringWithDateFormat(joinCircleDTO, "yyyy-MM-dd", SerializerFeature.WriteDateUseDateFormat);
        } else {
            return "fail";
        }
    }
}
