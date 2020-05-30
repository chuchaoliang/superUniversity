package com.ccl.wx.service;

/**
 * @author 褚超亮
 * @date 2020/5/12 13:36
 */
public interface RedisService {
    /**
     * 判断用户是否操作频繁
     *
     * @param userId 用户id
     * @param isHigh 是否是高频点击按钮 （true是false不是）
     * @return true 可以操作 false 不可以操作
     */
    boolean judgeButtonClick(String userId, boolean isHigh);

    /**
     * redis中添加值
     *
     * @param key   要添加的key
     * @param value 对应的值
     */
    void stringSetValue(Object key, Object value);
}
