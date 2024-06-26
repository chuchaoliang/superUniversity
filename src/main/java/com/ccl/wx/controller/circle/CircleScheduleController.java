package com.ccl.wx.controller.circle;

import cn.hutool.core.date.DatePattern;
import com.ccl.wx.enums.common.EnumEnvironmentProfile;
import com.ccl.wx.service.CircleScheduleService;
import com.ccl.wx.service.TodayContentService;
import com.ccl.wx.service.UserDiaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 与圈子相关的定时信息
 *
 * @author 褚超亮
 * @date 2019/11/20 21:48
 */
@Slf4j
@Component
public class CircleScheduleController implements ApplicationContextAware {

    @Resource
    private CircleScheduleService circleScheduleService;

    private static ApplicationContext context = null;

    /**
     * 日期格式化
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);

    /**
     * 日志输出字符
     */
    public static final String LOG_STR = "----------------------------------------------------------";

    @Resource
    private UserDiaryService userDiaryService;

    @Resource
    private TodayContentService todayContentService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 返回当前的环境
     *
     * @return
     */
    public String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

    /**
     * 判断环境是否生效
     * (定时任务在哪种环境下生效)
     *
     * @return true 生效 false 不生效
     */
    public boolean judgeEnvironment() {
        String activeProfile = getActiveProfile();
        if (activeProfile.equals(EnumEnvironmentProfile.PROD_PROFILE.getValue())) {
            return true;
        }
        return false;
    }

    /**
     * 每隔五分钟执行一次
     * 将redis中的数据持久化到mysql中
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void saveUserLikeDataPersistence() {
        if (judgeEnvironment()) {
            log.info(LOG_STR);
            log.info(dateFormat.format(new Date()) + "：将点赞数据持久化到mysql");
            log.info(LOG_STR);
            circleScheduleService.saveUserLikeDataPersistence();
        }
    }

    /**
     * 每隔五分钟执行一次
     * 保存用户的点赞数据信息
     * 持redis中的数据持久化到mysql中
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void saveUserAccountLikeDataPersistence() {
        if (judgeEnvironment()) {
            log.info(LOG_STR);
            log.info(dateFormat.format(new Date()) + "：将点赞数目总数持久化到mysql");
            log.info(LOG_STR);
            circleScheduleService.saveUserAccountLikeDataPersistence();
        }
    }

    /**
     * ，诶隔五分钟执行一次
     * 处理用户加入圈子的消息
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void disposeUserJoinCircleMessage() {
        if (judgeEnvironment()) {
            log.info(LOG_STR);
            log.info(dateFormat.format(new Date()) + "：处理用户加入圈子消息");
            log.info(LOG_STR);
            circleScheduleService.disposeUserJoinCircleMessage();
        }
    }

    /**
     * ，诶隔五分钟执行一次
     * 处理用户退出圈子的消息
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void disposeUserExitCircleMessage() {
        if (judgeEnvironment()) {
            log.info(LOG_STR);
            log.info(dateFormat.format(new Date()) + "：处理用户退出圈子消息");
            log.info(LOG_STR);
            circleScheduleService.disposeUserExitCircleMessage();
        }
    }

    /**
     * 每天晚上12:00执行
     * 删除用户的评论信息
     */
    @Scheduled(cron = "59 59 23 * * ?")
    public void deleteUserDiaryAndComment() {
        if (judgeEnvironment()) {
            log.info(LOG_STR);
            log.info(dateFormat.format(new Date()) + "：开始删除用户日志信息");
            circleScheduleService.deleteUserDiaryInfoAndComment();
            log.info(LOG_STR);
        }
    }

    /**
     * 持久化日记浏览量到数据库中
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void saveDiaryBrowse() {
        if (judgeEnvironment()) {
            log.info(LOG_STR);
            log.info(dateFormat.format(new Date()) + "：持久化日记浏览量到数据库中");
            userDiaryService.saveDiaryBrowseNumber();
            log.info(LOG_STR);
        }
    }

    /**
     * 每天早上6:00执行
     * 删除圈子中为删除状态的主题信息
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void deleteCircleTheme() {
        if (judgeEnvironment()) {
            log.info(LOG_STR);
            log.info(dateFormat.format(new Date()) + "：删除圈子中删除状态的主题");
            todayContentService.deleteCircleThemeFormDatabase();
            log.info(LOG_STR);
        }
    }
}
