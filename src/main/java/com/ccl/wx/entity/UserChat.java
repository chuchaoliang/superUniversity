package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author  褚超亮
 * @date  2020/6/1 17:11
 */

@ApiModel(value="com-ccl-wx-entity-UserChat")
@Data
public class UserChat implements Serializable {
    /**
    * 主键id
    */
    @ApiModelProperty(value="主键id")
    private Long id;

    /**
    * 发送者id
    */
    @ApiModelProperty(value="发送者id")
    private String sendId;

    /**
    * 接收者id
    */
    @ApiModelProperty(value="接收者id")
    private String targetId;

    /**
    * 文本内容/图片地址
    */
    @ApiModelProperty(value="文本内容/图片地址")
    private String content;

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
    * 是否删除
    */
    @ApiModelProperty(value="是否删除")
    private Byte delete;

    /**
    * 删除时间
    */
    @ApiModelProperty(value="删除时间")
    private Date deleteTime;

    /**
    * 是否读取
    */
    @ApiModelProperty(value="是否读取")
    private Byte read;

    private static final long serialVersionUID = 1L;
}