package com.ccl.wx.service.impl;

import com.ccl.wx.entity.NotifyContent;
import com.ccl.wx.entity.SystemNotify;
import com.ccl.wx.entity.SystemUser;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.enums.common.EnumCommon;
import com.ccl.wx.enums.common.EnumResultStatus;
import com.ccl.wx.enums.notify.EnumNotifyType;
import com.ccl.wx.enums.user.EnumUserInfo;
import com.ccl.wx.mapper.SystemNotifyMapper;
import com.ccl.wx.service.*;
import com.ccl.wx.vo.notify.SystemNotifyVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/6/3 22:28
 */

@Service
public class SystemNotifyServiceImpl implements SystemNotifyService {

    @Resource
    private SystemNotifyMapper systemNotifyMapper;
    @Resource
    private NotifyContentService notifyContentService;
    @Resource
    private SystemUserService systemUserService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserNotifyService userNotifyService;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return systemNotifyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SystemNotify record) {
        return systemNotifyMapper.insert(record);
    }

    @Override
    public int insertSelective(SystemNotify record) {
        return systemNotifyMapper.insertSelective(record);
    }

    @Override
    public SystemNotify selectByPrimaryKey(Integer id) {
        return systemNotifyMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SystemNotify record) {
        return systemNotifyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SystemNotify record) {
        return systemNotifyMapper.updateByPrimaryKey(record);
    }

    @Override
    public String sendMessageNotify(SystemNotifyVO systemNotifyVO, Integer type) {
        // 检验消息内容不能为空
        if (StringUtils.isEmpty(systemNotifyVO.getMessageContent())) {
            return EnumResultStatus.FAIL.getValue();
        }
        // 获取发送人id
        String sendId = systemNotifyVO.getSendId();
        // 判断发送者用户是否存在
        if (type == 0) {
            // 系统用户
            SystemUser systemUser = systemUserService.selectByPrimaryKey(Integer.parseInt(sendId));
            if (systemUser == null || systemUser.getDelete() != EnumCommon.NOT_DELETE.getData()) {
                return EnumResultStatus.FAIL.getValue();
            }
        } else {
            UserInfo userInfo = userInfoService.selectByPrimaryKey(sendId);
            if (userInfo == null || userInfo.getUserStatus() != EnumUserInfo.USER_NORMAL.getValue()) {
                return EnumResultStatus.FAIL.getValue();
            }
        }
        // 插入数据到消息内容表
        NotifyContent notifyContent = new NotifyContent();
        notifyContent.setContent(systemNotifyVO.getMessageContent());
        int i = notifyContentService.insertSelective(notifyContent);
        if (i != 0) {
            // 保存成功
            // 获取插入的消息内容id
            SystemNotify systemNotify = new SystemNotify();
            // 设置发送人id
            systemNotify.setSendId(sendId);
            // 设置消息内容id
            Integer notifyContentId = notifyContent.getId();
            // 设置消息内容id
            systemNotify.setNoticeId(notifyContentId);
            int i1 = insertSelective(systemNotify);
            if (i1 != 0) {
                // 保存成功
                // 获取接收人用户id列表
                List<String> targetIdList = systemNotifyVO.getTargetIdList();
                String result;
                if (type == 0) {
                    // 系统消息处理
                    result = userNotifyService.systemMessageNotify(EnumNotifyType.SYSTEM_NOTICE, sendId, targetIdList, notifyContentId);
                } else {
                    // 普通用户消息处理
                    result = userNotifyService.userMessageNotify(EnumNotifyType.COMMON_NOTICE, sendId, targetIdList, notifyContentId);
                }
                if (EnumResultStatus.FAIL.getValue().equals(result)) {
                    return EnumResultStatus.UNKNOWN.getValue();
                }
            }
        }
        return EnumResultStatus.SUCCESS.getValue();
    }
}
