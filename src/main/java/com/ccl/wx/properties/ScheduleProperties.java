package com.ccl.wx.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 褚超亮
 * @date 2020/2/13 20:41
 */
@Data
@Component
@ConfigurationProperties(prefix = "task.schedule")
public class ScheduleProperties {
    public Integer circleClockInStatusFlag;
    public Integer circleLikeDataRedisFlag;
    public Integer circleAccountLikeDataRedisFlag;
    public Integer deleteUserDiaryAndCommentFlag;
}
