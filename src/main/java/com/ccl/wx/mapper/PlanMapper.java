package com.ccl.wx.mapper;

import com.ccl.wx.entity.Plan;import org.apache.ibatis.annotations.Param;import java.util.List;

/**
 * @author 褚超亮
 * @date 2019/10/23 10:05
 */

public interface PlanMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Plan record);

    int insertSelective(Plan record);

    Plan selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Plan record);

    int updateByPrimaryKey(Plan record);

    /**
     * 根据userid查询全部数据
     *
     * @param userid
     * @return
     */
    List<Plan> findAllByUserid(@Param("userid") String userid);

    /**
     * 根据userid和当前页面的日期查询数据
     *
     * @param userid
     * @param titleTime
     * @return
     */
    List<Plan> findAllByUseridAndTitleTime(@Param("userid") String userid, @Param("titleTime") String titleTime);

    /**
     * 根据userid获取全部的starttime
     *
     * @param userid
     * @return
     */
    List<String> findStartTimeByUserid(@Param("userid") String userid);

    /**
     * 根据userid 查询在指定范围内的计划
     *
     * @param time
     * @param userid
     * @return
     */
    List<Plan> findAllDatePlans(@Param("time") String time, @Param("userid") String userid);

    /**
     * 拼接要删除的字符串
     * @param id
     * @param time
     * @return
     */
    int concatDelTime(@Param("id") String id, @Param("time") String time);

    /**
     * 拼接要完成的字符串
     * @param id
     * @param time
     * @return
     */
    int concatFinish(@Param("id") String id, @Param("time") String time);
}