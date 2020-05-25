package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author  褚超亮
 * @date  2020/5/24 15:58
 */

@ApiModel(value="com-ccl-wx-entity-NotifyConfig")
@Data
public class NotifyConfig implements Serializable {
    /**
    * 主键用户id
    */
    @ApiModelProperty(value="主键用户id")
    private String id;

    /**
    * 消息提醒设置，默认设置为全部消息提醒，如果设置则为不提醒(例如0,1,2)则0,1,2类型的消息不提醒
    */
    @ApiModelProperty(value="消息提醒设置，默认设置为全部消息提醒，如果设置则为不提醒(例如0,1,2)则0,1,2类型的消息不提醒")
    private String notifyConfig;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
    * 更新时间
    */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    /**
    * 删除时间
    */
    @ApiModelProperty(value="删除时间")
    private Date delTime;

    private static final long serialVersionUID = 1L;
}