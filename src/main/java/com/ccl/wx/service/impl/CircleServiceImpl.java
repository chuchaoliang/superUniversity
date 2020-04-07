package com.ccl.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccl.wx.dto.*;
import com.ccl.wx.entity.*;
import com.ccl.wx.enums.EnumUserCircle;
import com.ccl.wx.enums.EnumUserDiary;
import com.ccl.wx.mapper.*;
import com.ccl.wx.service.CircleRedisService;
import com.ccl.wx.service.CircleService;
import com.ccl.wx.service.CommentService;
import com.ccl.wx.service.UserDiaryService;
import com.ccl.wx.util.FtpUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


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
    private UserDiaryMapper userDiaryMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private TodayContentMapper todayContentMapper;

    @Autowired
    private UserLikeMapper userLikeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CircleRedisService circleRedisService;

    @Autowired
    private CommentService commentService;

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
    public String joinCircle(String circleid, String userid) {
        String status = "success";
        JoinCircle joinCircle = new JoinCircle();
        // 设置圈子id
        joinCircle.setCircleId(Long.valueOf(circleid));
        // 设置用户id
        joinCircle.setUserId(userid);
        JoinCircle circleUser = joinCircleMapper.selectByPrimaryKey(Long.valueOf(circleid), userid);
        // 圈子中人数 +1
        circleInfoMapper.updateCircleMemberByCircleId(Long.valueOf(circleid), 1);
        if (circleUser != null) {
            if (circleUser.getUserStatus().equals(EnumUserCircle.USER_OUT_STATUS.getValue())) {
                joinCircle.setUserStatus(EnumUserCircle.USER_NORMAL_STATUS.getValue());
                joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
                return status;
            } else {
                return "1";
            }
        } else {
            // 设置加入时间
            joinCircle.setJoinTime(new Date());
            // 根据圈子id查询圈子数据
            CircleInfo circle = circleInfoMapper.selectByPrimaryKey(Long.valueOf(circleid));
            // 查看是否为圈主或者圈子成员
            if (circle.getCircleUserid().equals(userid)) {
                // 圈主
                joinCircle.setUserPermission(2);
                joinCircle.setUserStatus(0);
            } else {
                // 普通成员
                joinCircle.setUserPermission(0);
                // 查看圈子状态
                if (circle.getCircleSet().equals(0)) {
                    joinCircle.setUserStatus(0);
                } else if (circle.getCircleSet().equals(1)) {
                    joinCircle.setUserStatus(1);
                    status = "fail";
                }
            }
            joinCircleMapper.insertSelective(joinCircle);
            return status;
        }
    }

    @Override
    public String joinPrivacyCircleByPassword(String circleid, String userid, String cpassword) {
        CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(Long.valueOf(circleid));
        // 圈子密码不存在
        if (StringUtils.isEmpty(circleInfo.getCirclePassword())) {
            return "-1";
        } else {
            // 判断密码是否正确
            if (cpassword.equals(circleInfo.getCirclePassword())) {
                // 圈子总人数 +1
                circleInfoMapper.updateCircleMemberByCircleId(Long.valueOf(circleid), 1);
                // 密码正确，加入圈子
                JoinCircle joinCircle = new JoinCircle();
                // 设置圈子id
                joinCircle.setCircleId(Long.valueOf(circleid));
                // 设置用户id
                joinCircle.setUserId(userid);
                // 查询数据库该用户是否加入过该圈子
                JoinCircle circleUser = joinCircleMapper.selectByPrimaryKey(Long.valueOf(circleid), userid);
                if (circleUser != null) {
                    // 说明圈子中存在此用户，将该圈子用户设置为正常状态
                    joinCircle.setUserStatus((EnumUserCircle.USER_NORMAL_STATUS.getValue()));
                    // 更新数据
                    joinCircleMapper.updateByPrimaryKeySelective(joinCircle);
                    return "success";
                } else {
                    // 用户未曾加入过该圈子
                    // 设置加入时间
                    joinCircle.setJoinTime(new Date());
                    // 设置用户状态
                    joinCircle.setUserStatus(EnumUserCircle.USER_NORMAL_STATUS.getValue());
                    // 设置用户权限
                    joinCircle.setUserPermission(0);
                    // 插入数据
                    joinCircleMapper.insertSelective(joinCircle);
                    return "success";
                }
            } else {
                // 密码错误
                return "-1";
            }
        }
    }

    @Override
    public String selectCircleDTO(List<CircleInfo> circles) {
        // 正常查询 热门查询 最新查询
        ArrayList<List<CirclesDTO>> str = new ArrayList<>();
        ArrayList<CirclesDTO> circlesDTOS = new ArrayList<>();
        for (CircleInfo circle : circles) {
            CirclesDTO circlesDTO = new CirclesDTO();
            // 查询圈子中正常状态的用户
            Integer sumperson = joinCircleMapper.countByCircleIdAndUserStatus(circle.getCircleId(), EnumUserCircle.USER_NORMAL_STATUS.getValue());
            // 根据日志状态查询日志信息
            ArrayList<Integer> diaryStatus = new ArrayList<>();
            diaryStatus.add(EnumUserDiary.USER_DIARY_NORMAL.getValue());
            diaryStatus.add(EnumUserDiary.USER_DIARY_PERMISSION.getValue());
            Long sumdiary = userDiaryService.countByCircleIdAndDiaryStatus(circle.getCircleId(), diaryStatus);
            BeanUtils.copyProperties(circle, circlesDTO);
            circlesDTO.setJoinPerson(sumperson);
            circlesDTO.setSumDiary(sumdiary);
            circlesDTOS.add(circlesDTO);
        }
        // 根据热度降序排列
        List<CirclesDTO> circlesDTOSByVitality = circlesDTOS.stream().
                sorted(Comparator.comparingLong(CirclesDTO::getCircleVitality).reversed().
                        thenComparing(CirclesDTO::getJoinPerson)).collect(Collectors.toList());
        // 根据创建时间降序排列
        List<CirclesDTO> circlesDTOSByCreateTime = circlesDTOS.stream().
                sorted(Comparator.comparing(CirclesDTO::getCircleCreatetime).reversed().
                        thenComparing(CirclesDTO::getJoinPerson)).collect(Collectors.toList());
        str.add(circlesDTOS);
        str.add(circlesDTOSByVitality);
        str.add(circlesDTOSByCreateTime);
        return JSON.toJSON(str).toString();
    }

    @Override
    public Boolean judgeUserCircleMaster(String circleId, String userId) {
        CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(Long.valueOf(circleId));
        if (circleInfo.getCircleUserid().equals(userId)) {
            return true;
        } else {
            return false;
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
    public List<UserInfo> getAllLikeUserNickName(String userid, String circleid, Long diaryid) {
        // 先判断缓存中是否存在
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        UserLike userLike = userLikeMapper.selectByTypeId(diaryid);
        Integer userLikeStatus = circleRedisService.getUserLikeStatus(userid, circleid, diaryid);
        if (userLike != null && !StringUtils.isEmpty(userLike.getLikeUserid())) {
            // 数据库中存在此数据
            List<String> userids = new ArrayList<>(Arrays.asList(userLike.getLikeUserid().split(",")));
            // 用户id列表去重
            List<String> fuserids = userids.stream().distinct().collect(Collectors.toList());
            boolean loginUserLikeCondition = (userLikeStatus != null && userLikeStatus.equals(LIKE_STATUS)) || (userLikeStatus == null && fuserids.contains(userid));
            // 删除本用户
            fuserids.removeIf(s -> s.equals(userid));
            // 判断删除本用户之后是否为空
            if (fuserids.size() > 0) {
                for (String likeUserId : fuserids) {
                    UserInfo userInfo = userInfoMapper.selectByPrimaryKey(likeUserId);
                    userInfos.add(userInfo);
                }
            }
            // 查找本用户（访问程序的用户）
            UserInfo loginUserInfo = userInfoMapper.selectByPrimaryKey(userid);
            // 添加本用户点赞条件
            if (loginUserLikeCondition) {
                userInfos.add(0, loginUserInfo);
            }
        } else {
            // 数据库中不存在 查看缓存中是否存在
            if (userLikeStatus != null && userLikeStatus.equals(LIKE_STATUS)) {
                userInfos.add(0, userInfoMapper.selectByPrimaryKey(userid));
            }
        }
        return userInfos;
    }

    @Override
    public String getAllLikeUserNickName(List<UserInfo> userInfos) {
        StringBuffer stringBuffer = new StringBuffer();
        if (userInfos.size() != 0) {
            for (UserInfo userInfo : userInfos) {
                stringBuffer.append(userInfo.getNickname()).append(",");
            }
            return String.valueOf(stringBuffer.deleteCharAt(stringBuffer.length() - 1));
        } else {
            return "";
        }
    }

    @Override
    public Boolean judgeDiaryLikeStatus(String userid, String circleid, Long diaryid) {
        String hash = LIKE_PREFIX + userid;
        String key = circleid + CONNECT_VALUE + diaryid;
        // 先判断缓存中是否存在
        if (redisTemplate.opsForHash().hasKey(hash, key)) {
            // 缓存中存在
            if (redisTemplate.opsForHash().get(hash, key).equals(LIKE_STATUS)) {
                // 状态为点赞状态1
                return true;
            } else {
                // 状态为取消点赞状态0
                return false;
            }
        } else {
            // 缓存为空
            UserLike userLike = userLikeMapper.selectByTypeId(diaryid);
            if (userLike != null) {
                // 数据库不为空
                if (userLike.getLikeStatus().equals(LIKE_STATUS)) {
                    // 为生效状态
                    List<String> userids = Arrays.asList(userLike.getLikeUserid().split(","));
                    if (userids.contains(userid)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                // 数据库为空
                return false;
            }
        }
    }

    @SneakyThrows
    @Override
    public List<CommentDTO> getAllComment(Long diaryId) {
        // 获取子评论
        List<Comment> comments = commentMapper.selectAllByDiaryIdAndCommentTypeOrderByCommentCreatetimeDesc(diaryId, 0, 0, 10);
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            if (COMMENT_DELETE_STATUS.equals(comment.getCommentStatus())) {
                // 点评为删除状态
                continue;
            }
            CommentDTO commentDTO = new CommentDTO();
            // 查询此评论下的全部子评论 TODO 可以设置分页 只查询10个
            List<Reply> replies = replyMapper.selectAllByCommentId(comment.getId(), 0, 3);
            ArrayList<ReplyDTO> replyDTOS = new ArrayList<>();
            for (Reply reply : replies) {
                ReplyDTO replyDTO = new ReplyDTO();
                // 回复人信息
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(reply.getReplyUserid());
                // 目标用户信息
                UserInfo targetUserInfo = userInfoMapper.selectByPrimaryKey(reply.getTargetUserid());
                BeanUtils.copyProperties(reply, replyDTO);
                // 设置回复人昵称
                replyDTO.setNickName(userInfo.getNickname());
                // 设置回复人性别
                replyDTO.setGender(userInfo.getGender());
                // 设置回复人头像
                replyDTO.setHeadImage(userInfo.getAvatarurl());
                // 设置目标人昵称
                replyDTO.setTargetNickName(targetUserInfo.getNickname());
                // 设置目标人性别
                replyDTO.setTargetGender(targetUserInfo.getGender());
                // 设置目标人头像
                replyDTO.setTargetHeadImage(targetUserInfo.getAvatarurl());
                replyDTOS.add(replyDTO);
                if (commentDTOS.size() == 15) {
                    break;
                }
            }
            // 获取用户信息
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(comment.getUserId());
            BeanUtils.copyProperties(comment, commentDTO);
            // 设置用户昵称
            commentDTO.setNickName(userInfo.getNickname());
            // 设置用户头像
            commentDTO.setHeadImage(userInfo.getAvatarurl());
            // 设置用户性别
            commentDTO.setGender(userInfo.getGender());
            // 设置此评论的回复
            commentDTO.setReplyDTOS(replyDTOS);
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    @SneakyThrows
    @Override
    public List<CommentDTO> getMasterComment(Long diaryId) {
        // 获取日记的点评
        List<Comment> comments = commentMapper.selectAllByDiaryIdAndCommentTypeOrderByCommentCreatetimeDesc(diaryId, 1, 0, 5);
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            if (COMMENT_DELETE_STATUS.equals(comment.getCommentStatus())) {
                // 点评为删除状态
                continue;
            }
            CommentDTO commentDTO = new CommentDTO();
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(comment.getUserId());
            BeanUtils.copyProperties(comment, commentDTO);
            // 设置用户昵称
            commentDTO.setNickName(userInfo.getNickname());
            // 设置用户头像
            commentDTO.setHeadImage(userInfo.getAvatarurl());
            // 设置用户性别
            commentDTO.setGender(userInfo.getGender());
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    @Override
    public UserDiaryDTO getDiaryInfoById(Long diaryId) {
        // 全部点评
        List<CommentDTO> masterComment = circleService.getMasterComment(diaryId);
        // 全部评论
        List<CommentDTO> allComment = circleService.getAllComment(diaryId);
        UserDiaryDTO userDiaryDTO = new UserDiaryDTO();
        userDiaryDTO.setMasterComments(masterComment);
        userDiaryDTO.setComments(allComment);
        return userDiaryDTO;
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

    @Override
    public Boolean judgeCirclePrivacyStatus(Long circleId) {
        CircleInfo circleInfo = circleInfoMapper.selectByPrimaryKey(circleId);
        // 判断是否为私密圈子
        if (circleInfo.getCircleSet().equals(CIRCLE_PASSWORD_STATUS) && !StringUtils.isEmpty(circleInfo.getCirclePassword())) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean judgeUserIntoPrivacyCircle(String userid, Long circleid) {
        // 判断用户是否为圈主
        Boolean circleMaster = circleService.judgeUserCircleMaster(String.valueOf(circleid), userid);
        if (circleMaster) {
            // 是圈主
            return true;
        } else {
            // 不是圈主
            List<String> userIds = joinCircleMapper.selectUserIdByCircleIdAndUserStatus(circleid, EnumUserCircle.USER_NORMAL_STATUS.getValue());
            if (userIds.contains(userid)) {
                // 是圈子成员
                return true;
            } else {
                // 不是圈子成员
                return false;
            }
        }
    }
}
