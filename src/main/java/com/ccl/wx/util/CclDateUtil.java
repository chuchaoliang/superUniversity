package com.ccl.wx.util;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @author 褚超亮
 * @date 2019/12/12 19:51
 */
public class CclDateUtil {

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    private static final String ONE_TODAY_AGO = "今天";
    private static final String ONE_YESTERDAY_AGO = "昨天";
    private static final String ONE_DAY_BEFORE_YESTERDAY_AGO = "前天";

    public static String todayDate(Date date) {
        long day = DateUtil.betweenDay(date, new Date(), true);
        // 获取小时数
        int hour = DateUtil.hour(date, true);
        // 获取分钟数
        int minute = DateUtil.minute(date);
        StringBuilder time = new StringBuilder();
        if (hour < 10) {
            time.append(0).append(hour);
        } else {
            time.append(hour);
        }
        if (minute < 10) {
            time.append(":").append(0).append(minute);
        } else {
            time.append(":").append(minute);
        }
        //String time = hour + ":" + minute;
        if (day == 0) {
            // 今天
            return ONE_TODAY_AGO + time;
        } else if (day == 1) {
            // 昨天
            return ONE_YESTERDAY_AGO + time;
        } else if (day == 2) {
            // 前天
            return ONE_DAY_BEFORE_YESTERDAY_AGO + time;
        } else {
            return DateUtil.format(date, "MM-dd HH:mm");
        }
    }

    public static String format(Date date) {
        long delta = System.currentTimeMillis() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }
}
