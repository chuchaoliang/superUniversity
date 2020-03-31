package com.ccl.wx.aop;

import com.alibaba.fastjson.JSON;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.enums.EnumResultCode;
import com.ccl.wx.service.UserInfoService;
import com.ccl.wx.util.ResponseMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author 褚超亮
 * @date 2020/3/29 10:43
 */
@Slf4j
@Order(0)
@Component
@Aspect
public class TokenCheckAop {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 请求头认证字段
     */
    public static final String HEAD_TOKEN = "userId";

    @Pointcut("(@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)) && !execution(* com.ccl.wx.controller.UserInfoController.user*(..))")
    void pointcut() {
    }

    @Pointcut("execution(public * com.ccl.wx.controller..*.*(..)) && !execution(* com.ccl.wx.controller.UserInfoController.user*(..))")
    void allPointcut() {
    }

    @Around("pointcut()")
    //@Around("allPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String userId = request.getHeader(HEAD_TOKEN);
        if (StringUtils.isEmpty(userId)) {
            // token为空
            log.warn("前端传输token为空");
            return JSON.toJSONString(ResponseMsgUtil.builderResponse(EnumResultCode.USER_LOGIN_FAIL.getCode(), EnumResultCode.USER_LOGIN_FAIL.getMessage()));
        } else {
            //token不为空，检测此用户是否存在
            UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
            if (userInfo == null) {
                return JSON.toJSONString(ResponseMsgUtil.builderResponse(EnumResultCode.USER_NONENTITY.getCode(), EnumResultCode.USER_NONENTITY.getMessage()));
            }
        }
        return pjp.proceed();
    }
}
