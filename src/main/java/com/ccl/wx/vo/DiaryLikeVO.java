package com.ccl.wx.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 褚超亮
 * @date 2020/3/30 14:29
 */
@Data
public class DiaryLikeVO implements Serializable {
    /**
     * 用户的id
     */
    private String id;

    /**
     * 用户的昵称
     */
    private String nickname;

    /**
     * 头像地址
     */
    private String avatarurl;

    /**
     * 性别（0未知,1男,2女）
     */
    private String gender;
}
