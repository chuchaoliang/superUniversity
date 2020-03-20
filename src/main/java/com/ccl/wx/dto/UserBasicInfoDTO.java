package com.ccl.wx.dto;

import lombok.Data;

/**
 * @author 褚超亮
 * @date 2019/12/5 15:55
 */
@Data
public class UserBasicInfoDTO {

    /**
     * 全部打卡次数
     */
    private Integer sumClockNum;

    /**
     * 加入的圈子数
     */
    private Integer sumJoinCircle;

    /**
     * 我创建的圈子数
     */
    private Integer sumFoundCircle;

    /**
     * 用户的全部积分能量球
     */
    private Integer sumIntegral;

    /**
     * 第几个用户
     */
    private Long userRank;
}
