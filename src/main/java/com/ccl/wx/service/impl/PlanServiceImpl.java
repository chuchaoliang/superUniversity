package com.ccl.wx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ccl.wx.mapper.PlanMapper;
import com.ccl.wx.dto.PlanDTO;
import com.ccl.wx.entity.Plan;
import com.ccl.wx.service.PlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 褚超亮
 * @date 2019/10/12 19:14
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanMapper planMapper;

    @Override
    public List<PlanDTO> ascSortByStartTimePlan(String userid, String time) {
        List<Plan> plans = planMapper.findAllDatePlans(time, userid);
        ArrayList<PlanDTO> planDTOS = new ArrayList<>();
        Iterator<Plan> it = plans.iterator();
        while (it.hasNext()) {
            Plan plan = it.next();
            if (!StringUtils.isEmpty(plan.getDelTime())) {
                List<String> deltimes = Arrays.asList(plan.getDelTime().split(","));
                if (deltimes.contains(time)) {
                    it.remove();
                }
            }
        }
        for (Plan plan : plans) {
            PlanDTO planDTO = new PlanDTO();
            planDTO.setBriefContent(StrUtil.maxLength(plan.getContent(),7));
            if (!StringUtils.isEmpty(plan.getFinish())) {
                // 存在完成此计划
                List<String> finishs = Arrays.asList(plan.getFinish().split(","));
                if (finishs.contains(time)) {
                    planDTO.setFinishPlan(true);
                    BeanUtils.copyProperties(plan, planDTO);
                    planDTOS.add(planDTO);
                } else {
                    planDTO.setFinishPlan(false);
                    BeanUtils.copyProperties(plan, planDTO);
                    planDTOS.add(planDTO);
                }
            } else {
                // 此计划从未完成过
                planDTO.setFinishPlan(false);
                BeanUtils.copyProperties(plan, planDTO);
                planDTOS.add(planDTO);
            }
        }
        planDTOS.sort((o1, o2) -> {
            return o1.getStartTime().compareTo(o2.getStartTime());
        });
        return planDTOS;
    }

    @Override
    public boolean judgePlanTimeClash(String starttime, String endtime, String userid, String titleTime) {
        // TODO 过滤掉状态为 删除状态 的计划 未添加（未启用）
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        List<Plan> plans = planMapper.findAllByUseridAndTitleTime(userid, titleTime);
        try {
            Date start = format.parse(starttime);
            Date end = format.parse(endtime);
            for (int i = 0; i < plans.size(); i++) {
                String pStarttime = plans.get(i).getStartTime();
                String pEndtime = plans.get(i).getEndTime();
                Date pStart = format.parse(pStarttime);
                Date pEnd = format.parse(pEndtime);
                boolean judgeTime = start.getTime() < pEnd.getTime() && end.getTime() > pStart.getTime();
                if (judgeTime) {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Long judgePlanTimeClashRetuenId(String starttime, String endtime, String userid, String titleTime) {
        // TODO 过滤掉状态为 删除状态 的计划 未添加（未启用）
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        List<Plan> plans = planMapper.findAllByUseridAndTitleTime(userid, titleTime);
        try {
            Date start = format.parse(starttime);
            Date end = format.parse(endtime);
            for (int i = 0; i < plans.size(); i++) {
                String pStarttime = plans.get(i).getStartTime();
                String pEndtime = plans.get(i).getEndTime();
                Date pStart = format.parse(pStarttime);
                Date pEnd = format.parse(pEndtime);
                boolean judgeTime = start.getTime() < pEnd.getTime() && end.getTime() > pStart.getTime();
                if (judgeTime) {
                    return plans.get(i).getId();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public boolean judgePlanDateClash(String startdate, String enddate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startD = format.parse(startdate);
            Date endD = format.parse(enddate);
            if (startD.getTime() > endD.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<String> judgeListIsNull(Plan plan) {
        ArrayList<String> strs = new ArrayList<>();
        strs.add("startTime");
        strs.add("endTime");
        strs.add("userid");
        strs.add("content");
        strs.add("titleTime");
        strs.add("titleTimeEnd");
        return strs;
    }

    @Override
    public boolean judgePlanTimeClash(String starttime, String endtime) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date startT = format.parse(starttime);
            Date endT = format.parse(endtime);
            if (startT.getTime() > endT.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
