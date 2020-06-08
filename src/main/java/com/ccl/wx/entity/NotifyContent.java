package com.ccl.wx.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 褚超亮
 * @date 2020/6/8 16:57
 */

@ApiModel(value = "com-ccl-wx-entity-NotifyContent")
@Data
public class NotifyContent implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Integer id;

    /**
     * 通知内容
     */
    @ApiModelProperty(value = "通知内容")
    private String content;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String image;

    /**
     * 音频地址
     */
    @ApiModelProperty(value = "音频地址")
    private String voice;

    /**
     * 视频地址
     */
    @ApiModelProperty(value = "视频地址")
    private String video;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Byte delete;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    private Date deleteTime;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String title;

    /**
     * 文章标签
     */
    @ApiModelProperty(value = "文章标签")
    private String label;

    private static final long serialVersionUID = 1L;
}