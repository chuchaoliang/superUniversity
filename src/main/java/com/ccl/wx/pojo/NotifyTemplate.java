package com.ccl.wx.pojo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * 消息通知模板
 *
 * @author 褚超亮
 * @date 2020/5/27 15:07
 */
@Data
public class NotifyTemplate {
    /**
     * 消息类型
     */
    private int messageType;
    /**
     * 消息位置
     */
    private int messageLocation;

    /**
     * 消息数量，默认为1
     */
    private int messageNumber = 1;

    /**
     * 消息内容，只有私信的时候才展示
     */
    private String messageContent;

    /**
     * 目标用户id，只有发送私信的时候（私信利用用户id来展示）
     */
    private String targetUserId;

    /**
     * 消息是否提醒（私信时才利用）
     */
    private boolean remind;

    /**
     * 发送内容的日期
     */
    private final String sendDate = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);
}
