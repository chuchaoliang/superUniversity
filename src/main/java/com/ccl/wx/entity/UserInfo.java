package com.ccl.wx.entity;

import com.ccl.wx.annotation.UserCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author 褚超亮
 * @date 2020/3/5 12:13
 */

@Data
public class UserInfo {

    public interface Default {
    }

    /**
     * 用户的唯一表示
     */
    @NotBlank(message = "用户id不能为空！", groups = {Default.class})
    @UserCheck
    private String id;

    /**
     * 用户的昵称
     */
    @NotBlank(message = "用户昵称不能为空！", groups = {Default.class})
    private String nickname;

    /**
     * 头像地址
     */
    @NotBlank(message = "头像地址不能为空！", groups = {Default.class})
    private String avatarurl;

    /**
     * 性别（1男2女）
     */
    @NotBlank(message = "用户性别不能为空", groups = {Default.class})
    private String gender;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 国
     */
    private String country;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    /**
     * 用户另一个键
     */
    private Long userId;
}