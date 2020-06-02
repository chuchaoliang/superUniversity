package com.ccl.wx.enums.system;

/**
 * @author 褚超亮
 * @date 2020/6/2 20:46
 */
public enum EnumMessageType {
    // 用户聊天消息
    USER_CHAT_TYPE("chat"),
    // 测试用户是否连接成功
    TEST_CONNECTION("test");

    private String value;

    EnumMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
