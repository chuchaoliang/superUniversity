package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author  褚超亮
 * @date  2020/6/3 22:28
 */

@ApiModel(value="com-ccl-wx-entity-SystemNotify")
@Data
public class SystemNotify implements Serializable {
    /**
    * 主键id
    */
    @ApiModelProperty(value="主键id")
    private Integer id;

    /**
    * 发送者id
    */
    @ApiModelProperty(value="发送者id")
    private String sendId;

    /**
    * 消息内容id
    */
    @ApiModelProperty(value="消息内容id")
    private Integer noticeId;

    /**
    * 接受者id（为空全部，暂时无用）
    */
    @ApiModelProperty(value="接受者id（为空全部，暂时无用）")
    private String targetId;

    /**
    * 阅读/拉取数量
    */
    @ApiModelProperty(value="阅读/拉取数量")
    private Integer readNumber;

    /**
    * 用户类型（0系统1普通用户）
    */
    @ApiModelProperty(value="用户类型（0系统1普通用户）")
    private Byte userType;

    /**
    * 通知类型（0全部1部分）暂时只有全部考虑性能
    */
    @ApiModelProperty(value="通知类型（0全部1部分）暂时只有全部考虑性能")
    private Byte notifyType;

    /**
    * 资源id 和 资源类型(这个参数是为了部分通知准备，但是可能不使用因为这样效率不高)
    */
    @ApiModelProperty(value="资源id 和 资源类型(这个参数是为了部分通知准备，但是可能不使用因为这样效率不高)")
    private Integer resourceId;

    /**
    * 资源类型（0圈子...）不使用
    */
    @ApiModelProperty(value="资源类型（0圈子...）不使用")
    private Byte resourceType;

    /**
    * 是否删除（0未删除1删除）
    */
    @ApiModelProperty(value="是否删除（0未删除1删除）")
    private Byte delete;

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
    private Date deleteTime;

    private static final long serialVersionUID = 1L;
}