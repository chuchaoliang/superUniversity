package com.ccl.wx.common.notify;

/**
 * @author 褚超亮
 * @date 2020/5/27 21:49
 */
public interface IUserNotify {
    /**
     * 获取消息类型
     *
     * @return
     */
    Integer getNotifyType();

    /**
     * 获取消息位置
     *
     * @return
     */
    Integer getNotifyLocation();

    /**
     * 获取资源类型
     *
     * @return
     */
    Integer getResourceType();

    /**
     * 获取队列
     *
     * @return
     */
    String getQueue();
}
