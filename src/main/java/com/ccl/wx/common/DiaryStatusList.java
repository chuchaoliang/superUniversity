package com.ccl.wx.common;

import com.ccl.wx.enums.EnumUserDiary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2020/3/30 9:09
 */
public class DiaryStatusList {

    private DiaryStatusList() {
    }

    /**
     * 常规日志状态日志状态0，1
     * 用户是圈子成员
     *
     * @return
     */
    public static List<Integer> joinCircleDiaryStatusList() {
        List<Integer> diaryStatusList = new ArrayList<>();
        diaryStatusList.add(EnumUserDiary.USER_DIARY_NORMAL.getValue());
        diaryStatusList.add(EnumUserDiary.USER_DIARY_PERMISSION.getValue());
        return diaryStatusList;
    }

    /**
     * 用户不是圈子成员
     */
    public static List<Integer> outCircleDiaryStatusList() {
        List<Integer> diaryStatusList = new ArrayList<>();
        diaryStatusList.add(EnumUserDiary.USER_DIARY_NORMAL.getValue());
        return diaryStatusList;
    }

    /**
     * 用户查看自己的所在圈子的打卡日记
     *
     * @return
     */
    public static List<Integer> userCircleDiaryStatusList() {
        List<Integer> diaryStatusList = new ArrayList<>();
        diaryStatusList.add(EnumUserDiary.USER_DIARY_NORMAL.getValue());
        diaryStatusList.add(EnumUserDiary.USER_DIARY_PERMISSION.getValue());
        diaryStatusList.add(EnumUserDiary.USER_DIARY_THEME_DELETE.getValue());
        diaryStatusList.add(EnumUserDiary.USER_DIARY_VIOLATION.getValue());
        return diaryStatusList;
    }
}
