package com.ccl.wx.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 褚超亮
 * @date 2020/2/29 18:08
 */
@Data
public class DiaryHideComment implements Serializable {

    /**
     * 判断是否需要隐藏评论
     */
    private Boolean hideComment;

    /**
     * 评论总数
     */
    private Long commentSum;
}
