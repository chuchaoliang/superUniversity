package com.ccl.wx.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 褚超亮
 * @date 2020/6/8 16:37
 */
@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
@Data
public class Notify implements Serializable {
    private static final long serialVersionUID = -2433244612486526654L;
    // 头像
    private String portrait;
    // 昵称
    private String nickname;
    // 创建时间
    private String createTime;
    // 是否可以查看
    private boolean look;
    // 消息内容
    private String content;
    // 内容中资源昵称
    private String userNickname;
    // 内容中用户id
    private String userId;
    // 内容中圈子id
    private Integer circleId;
    // 资源id
    private Integer resourceId;
    // 资源类型
    private Integer resourceType;
    // 标题
    private String title;
    // 标签
    private String label;
}
