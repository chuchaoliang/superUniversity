package com.ccl.wx.service;

import com.ccl.wx.dto.PlanDTO;
import com.ccl.wx.entity.Plan;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2019/10/12 19:48
 */
public interface PlanService {

    /**
     * 根据时间降序，查询用户的所有计划
     *
     * @param userid 用户的openid
     * @param date   用户的计划日期
     * @return
     */
    List<PlanDTO> ascSortByStartTimePlan(String userid, String date);

    /**
     * 检测时间段是否有冲突，没有冲突返回true，否则返回fals;
     *
     * @param starttime 开始的时间
     * @param endtime   结束的时间
     * @param userid    用户的openid
     * @param titleTime 当前的时间
     * @return
     */
    boolean judgePlanTimeClash(String starttime, String endtime, String userid, String titleTime);

    /**
     * 检测时间段是否冲突，没有冲突返回0L，否则返回冲突的id
     *
     * @param starttime 开始时间
     * @param endtime   结束时间
     * @param userid    用于openid
     * @param titleTime 当前的时间
     * @return
     */
    Long judgePlanTimeClashRetuenId(String starttime, String endtime, String userid, String titleTime);

    /**
     * 检测时间段是否冲突（开始时间大于结束时间）
     * 冲突返回 true 否则false
     *
     * @param starttime 开始时间
     * @param endtime   结束时间
     * @return
     */
    boolean judgePlanTimeClash(String starttime, String endtime);

    /**
     * 检测日期时间段是否冲突(开始日期大于结束日期)
     * 冲突返回true 否则false
     *
     * @param startdate 开始日期
     * @param enddate   结束日期
     * @return
     */
    boolean judgePlanDateClash(String startdate, String enddate);

    /**
     * 将需要判断的属性值，加入列表中
     *
     * @param plan 需要检测侧的计划
     * @return
     */
    ArrayList<String> judgeListIsNull(Plan plan);

}
