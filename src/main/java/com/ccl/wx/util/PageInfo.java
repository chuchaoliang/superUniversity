package com.ccl.wx.util;

import lombok.Data;

import java.util.List;

/**
 * 定义分页信息类
 * @author 褚超亮
 */
@Data
public class PageInfo<T> {

    /**
     * 条数
     */
    private long pageSize;

    /**
     * 当前页
     */
    private long pageNum;

    /**
     * 总页数
     */
    private long total;

    /**
     * 下一页
     */
    private long nextPage;

    /**
     * 上一页
     */
    private long prePage;

    /**
     * 尾页
     */
    private long pages;

    private boolean isFirstPage;

    private boolean isLastPage;

    private boolean hasPreviousPage;

    private boolean hasNextPage;

    private List<T> list;
}
