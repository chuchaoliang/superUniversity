package com.ccl.wx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 圈子中打卡统计成功用户信息
 *
 * @author 褚超亮
 * @date 2020/3/22 19:34
 */
@ApiModel(value = "用户打卡成功DTO")
@Data
public class UserSignSuccessDTO {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像地址")
    private String avatarurl;

    @ApiModelProperty(value = "日志创建时间")
    private Date diaryCreatetime;

    @ApiModelProperty(value = "格式化的日志创建时间")
    private String formatDiaryCreate;

    @ApiModelProperty(value = "打卡的主题总数")
    private Integer themeNumber;

    @ApiModelProperty(value = "用户打卡天数")
    private Integer userSigninDay;
}
