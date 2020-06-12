package com.ccl.wx.common.list;

import com.ccl.wx.enums.notify.EnumNotifyType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/6/11 22:00
 */
public class NotifyTypeList {
    private NotifyTypeList() {
    }

    public static List<Integer> getNotifyTypeList() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(EnumNotifyType.SYSTEM_NOTICE.getNotifyType());
        list.add(EnumNotifyType.DIARY_LIKE.getNotifyType());
        list.add(EnumNotifyType.DIARY_COMMON_COMMENT.getNotifyType());
        list.add(EnumNotifyType.DIARY_COMMENT.getNotifyType());
        list.add(EnumNotifyType.DIARY_REPLY.getNotifyType());
        list.add(EnumNotifyType.CIRCLE_APPLY.getNotifyType());
        list.add(EnumNotifyType.CIRCLE_REFUSE.getNotifyType());
        list.add(EnumNotifyType.CIRCLE_AGREE.getNotifyType());
        list.add(EnumNotifyType.CIRCLE_OUT.getNotifyType());
        list.add(EnumNotifyType.CIRCLE_JOIN.getNotifyType());
        list.add(EnumNotifyType.CIRCLE_EXIT.getNotifyType());
        list.add(EnumNotifyType.USER_ATTENTION.getNotifyType());
        list.add(EnumNotifyType.USER_CALL_ATTENTION.getNotifyType());
        list.add(EnumNotifyType.USER_CHAT.getNotifyType());
        list.add(EnumNotifyType.COMMON_NOTICE.getNotifyType());
        return list;
    }
}
