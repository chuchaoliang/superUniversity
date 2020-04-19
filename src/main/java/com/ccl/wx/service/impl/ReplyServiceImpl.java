package com.ccl.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.dto.ReplyDTO;
import com.ccl.wx.entity.Comment;
import com.ccl.wx.entity.Reply;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.enums.EnumResultStatus;
import com.ccl.wx.mapper.ReplyMapper;
import com.ccl.wx.service.CommentService;
import com.ccl.wx.service.ReplyService;
import com.ccl.wx.service.UserInfoService;
import com.ccl.wx.util.CclDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/25 20:05
 */

@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class ReplyServiceImpl implements ReplyService {

    @Resource
    private ReplyMapper replyMapper;

    @Resource
    private CommentService commentService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private JoinCircleServiceImpl joinCircleService;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return replyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Reply record) {
        return replyMapper.insert(record);
    }

    @Override
    public int insertSelective(Reply record) {
        return replyMapper.insertSelective(record);
    }

    @Override
    public Reply selectByPrimaryKey(Long id) {
        return replyMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Reply record) {
        return replyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Reply record) {
        return replyMapper.updateByPrimaryKey(record);
    }

    @Override
    public String replyDiaryComment(Reply reply) {
        Comment comment = commentService.selectByPrimaryKey(reply.getCommentId());
        if (comment == null) {
            return EnumResultStatus.FAIL.getValue();
        }
        int i = replyMapper.insertSelective(reply);
        if (i == 1) {
            return JSON.toJSONString(reply);
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public String deleteDiaryReply(Integer replyId) {
        int i = replyMapper.deleteByPrimaryKey(replyId.longValue());
        if (i == 1) {
            return EnumResultStatus.SUCCESS.getValue();
        }
        return EnumResultStatus.FAIL.getValue();
    }

    @Override
    public List<Reply> selectReply(Long commentId, int start, Integer pageNumber) {
        return replyMapper.selectReply(commentId, start, pageNumber);
    }

    @Override
    public Long countByDiaryId(Long diaryId) {
        return replyMapper.countByDiaryId(diaryId);
    }

    @Override
    public List<ReplyDTO> adornReply(List<Reply> replies) {
        ArrayList<ReplyDTO> replyDTOS = new ArrayList<>();
        for (Reply reply : replies) {
            ReplyDTO replyDTO = new ReplyDTO();
            // 回复人信息
            UserInfo replyUserInfo = userInfoService.selectByPrimaryKey(reply.getReplyUserid());
            // 目标人信息
            UserInfo targetUserInfo = userInfoService.selectByPrimaryKey(reply.getTargetUserid());
            // 设置回复人、目标人昵称
            replyUserInfo.setNickname(joinCircleService.getUserJoinCircleNickname(replyUserInfo.getId(), reply.getCircleId()));
            targetUserInfo.setNickname(joinCircleService.getUserJoinCircleNickname(targetUserInfo.getId(), reply.getCircleId()));
            BeanUtils.copyProperties(reply, replyDTO);
            // 设置回复人信息
            replyDTO.setRNickName(replyUserInfo.getNickname());
            replyDTO.setRGender(replyUserInfo.getGender());
            replyDTO.setRHeadImage(replyUserInfo.getAvatarurl());
            // 设置目标人信息
            replyDTO.setTNickName(targetUserInfo.getNickname());
            replyDTO.setTGender(targetUserInfo.getGender());
            replyDTO.setTHeadImage(targetUserInfo.getAvatarurl());
            // 设置回复创建时间
            replyDTO.setCreateTime(CclDateUtil.todayDate(reply.getReplyCreatetime()));
            replyDTOS.add(replyDTO);
        }
        return replyDTOS;
    }

    @Override
    public List<Long> selectIdByDiaryId(Long diaryId) {
        return replyMapper.selectIdByDiaryId(diaryId);
    }

    @Override
    public Long countByCommentId(Long commentId) {
        return replyMapper.countByCommentId(commentId);
    }
}
