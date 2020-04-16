package com.ccl.wx.common.comparator;

import com.ccl.wx.entity.CircleInfo;

import java.util.Comparator;

/**
 * @author 褚超亮
 * @date 2020/4/10 19:26
 */
public class CircleInfoComparator implements Comparator<CircleInfo> {
    @Override
    public int compare(CircleInfo o1, CircleInfo o2) {
        int num1 = o2.getDiarySum() / o2.getCircleMember() - o1.getDiarySum() / o1.getCircleMember();
        return num1 == 0 ? o2.getCircleCreatetime().compareTo(o1.getCircleCreatetime()) : num1;
    }
}
