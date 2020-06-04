package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author  褚超亮
 * @date  2020/6/3 22:55
 */

@ApiModel(value="com-ccl-wx-entity-SystemUser")
@Data
public class SystemUser implements Serializable {
    /**
    * 主键
    */
    @ApiModelProperty(value="主键")
    private Integer id;

    /**
    * 头像地址
    */
    @ApiModelProperty(value="头像地址")
    private String headPortrait;

    /**
    * 昵称
    */
    @ApiModelProperty(value="昵称")
    private String nickname;

    /**
    * 权限（0超级用户权限，1其它暂时未使用）
    */
    @ApiModelProperty(value="权限（0超级用户权限，1其它暂时未使用）")
    private Byte permission;

    /**
    * 标签
    */
    @ApiModelProperty(value="标签")
    private String label;

    /**
    * 是否删除/是否有效（0未删除/有效1删除/无效）
    */
    @ApiModelProperty(value="是否删除/是否有效（0未删除/有效1删除/无效）")
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