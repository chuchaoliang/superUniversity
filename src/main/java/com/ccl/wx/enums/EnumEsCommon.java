package com.ccl.wx.enums;

/**
 * @author 褚超亮
 * @date 2020/4/27 9:51
 */
public enum EnumEsCommon {
    /**
     * 高亮颜色前缀
     */
    ES_HIGHLIGHT_PRE_TAGS_COLOR("<span style='color:#E4393C'>"),
    /**
     * 高亮后缀
     */
    ES_HIGHLIGHT_POST_TAGS_COLOR("</span>");

    private String value;

    EnumEsCommon(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
