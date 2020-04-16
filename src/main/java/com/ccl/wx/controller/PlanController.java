package com.ccl.wx.controller;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.dto.PlanDTO;
import com.ccl.wx.entity.Plan;
import com.ccl.wx.mapper.PlanMapper;
import com.ccl.wx.service.impl.PlanServiceImpl;
import com.ccl.wx.util.CclUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author 褚超亮
 * @date 2019/10/11 19:57
 */
@RestController
@RequestMapping("/wx")
public class PlanController {

    @Resource
    private PlanMapper planMapper;

    @Resource
    private PlanServiceImpl planService;


    /**
     * 根据计划id查找计划信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getplanbyid")
    public String getPlanById(@RequestParam(value = "id", required = false) String id) {
        if (StringUtils.isEmpty(id)) {
            return "fail";
        } else {
            Plan plan = planMapper.selectByPrimaryKey(Long.parseLong(id));
            String planstr = JSON.toJSONString(plan);
            return planstr;
        }
    }

    /**
     * 完成计划
     * 如果此计划已经完成返回1 提示：已完成计划 数据库不重复保存计划
     *
     * @param id
     * @param date
     * @return
     */
    @GetMapping("/finishplan")
    public String finishPlan(@RequestParam(value = "id", required = false) String id,
                             @RequestParam(value = "date", required = false) String date) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(date)) {
            return "fail";
        } else {
            Plan plan = planMapper.selectByPrimaryKey(Long.parseLong(id));
            if (!StringUtils.isEmpty(plan.getFinish())) {
                List<String> finishs = Arrays.asList(plan.getFinish().split(","));
                if (finishs.contains(date)) {
                    return "1";
                } else {
                    String fDate = "," + date;
                    planMapper.concatFinish(id, fDate);
                    return "success";
                }
            } else {
                plan.setFinish(date);
                planMapper.updateByPrimaryKeySelective(plan);
                return "success";
            }
        }
    }

    /**
     * 根据userid date 查询当天的所有计划,
     * 根据时间排序升序排列
     *
     * @param userid      用户的id
     * @param time        标题日期
     * @param loginstatus 登录状态
     * @return
     */
    @GetMapping("/getplan")
    public String getUserPlans(@RequestParam(value = "userid", required = false) String userid,
                               @RequestParam(value = "date", required = false) String time,
                               @RequestParam(value = "loginstatus", required = false) String loginstatus) {
        System.out.println(loginstatus);
        if (StringUtils.isEmpty(loginstatus)) {
            return "fail";
        } else {
            List<PlanDTO> plans = planService.ascSortByStartTimePlan(userid, time);
            String plan = JSON.toJSONString(plans);
            return plan;
        }
    }

    /**
     * 保存计划
     * -如果前端传送来的必要的数据为空，返回 fail
     * -如果时间冲突返回-1 (开始时间>结束时间)
     * -如果时间正常，检测时间是否与已有的时间是否冲突（是否重叠）返回 @+冲突的事件名称
     * -如果以上情况都没有正常保存，返回 success
     * -如果都未执行， 返回 fail 保存失败，请重试
     * - 无需检测时间冲突
     *
     * @return
     */
    @PostMapping("/saveplan")
    public String savePlan(@RequestBody(required = false) Plan plan) {
        System.out.println(plan);
        ArrayList<String> strs = planService.judgeListIsNull(plan);
        if (!CclUtil.classPropertyIsNull(plan, strs)) {
            // 前端传输过来的plan为空
            return "fail";
        } else {
            if (planService.judgePlanTimeClash(plan.getStartTime(), plan.getEndTime())) {
                return "-1";
            } else {
                Plan userPlan = new Plan();
                BeanUtils.copyProperties(plan, userPlan);
                userPlan.setCreateTime(new Date());
                userPlan.setSign(1);
                planMapper.insertSelective(userPlan);
                return "success";
            }
        }
    }

    /**
     * 将要删除的计划日期保存在删除字段中。。
     *
     * @param id   计划id
     * @param date 标题日期
     * @return
     */
    @GetMapping("/delplan")
    public String deletePlan(@RequestParam(value = "id", required = false) String id,
                             @RequestParam(value = "date", required = false) String date) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(date)) {
            return "fail";
        } else {
            Plan plan = planMapper.selectByPrimaryKey(Long.parseLong(id));
            if (StringUtils.isEmpty(plan.getDelTime())) {
                plan.setDelTime(date);
                planMapper.updateByPrimaryKeySelective(plan);
            } else {
                String fDate = "," + date;
                planMapper.concatDelTime(id, fDate);
            }
            return "success";
        }
    }

    /**
     * 删除全部的计划
     *
     * @param id
     * @return
     */
    @GetMapping("delallplan")
    public String delAllPlan(@RequestParam(value = "id", required = false) String id) {
        if (StringUtils.isEmpty(id)) {
            return "fail";
        } else {
            planMapper.deleteByPrimaryKey(Long.valueOf(id));
            return "success";
        }
    }

    /**
     * 更新用户的计划
     * -1.开始时间大于结束时间 返回 -1
     * -2.时间有冲突 返回 @+冲突的事件名称
     * -3.前端传送来的有的数据为空，返回 fail
     * -4.正常更新数据，返回success
     *
     * @return
     */
    @PostMapping("/updateplan")
    public String updateplan(@RequestBody(required = false) Plan plan) {
        System.out.println(plan);
        ArrayList<String> strs = new ArrayList<>();
        strs.add("id");
        strs.add("startTime");
        strs.add("endTime");
        strs.add("content");
        strs.add("titleTime");
        strs.add("titleTimeEnd");
        if (!CclUtil.classPropertyIsNull(plan, strs)) {
            return "fail";
        } else {
            if (planService.judgePlanTimeClash(plan.getStartTime(), plan.getEndTime())) {
                return "-1";
            } else if (planService.judgePlanDateClash(plan.getTitleTime(), plan.getTitleTimeEnd())) {
                return "1";
            } else {
                planMapper.updateByPrimaryKeySelective(plan);
                return "success";
            }
        }
    }
}
