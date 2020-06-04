package com.ccl.wx.vo.notify;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/6/3 22:35
 */
@Data
public class SystemNotifyVO {
    /**
     * 发送者id
     */
    @NotBlank(message = "发送人id不能为空！")
    private String sendId;
    /**
     * 消息内容
     */
    @NotBlank(message = "发送消息内容不能为空")
    private String messageContent;
    /**
     * 接受者id列表，如果为空则全部成员接收
     */
    private List<String> targetIdList;
}
