package com.ccl.wx.aop;

import com.ccl.wx.annotation.ParamCheck;
import com.ccl.wx.entity.CircleInfo;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.exception.CircleIsNullException;
import com.ccl.wx.exception.ParamIsNullException;
import com.ccl.wx.exception.UserIsNullException;
import com.ccl.wx.service.CircleInfoService;
import com.ccl.wx.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 参数校检切面
 *
 * @author 褚超亮
 * @date 2020/3/2 17:04
 */
@Component
@Aspect
@Slf4j
public class ParamCheckAop {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private CircleInfoService circleInfoService;

    // && @annotation(org.springframework.web.bind.annotation.GetMapping
    @Pointcut("execution(public * com.ccl.wx.controller..*.*(..)) && !execution(* com.ccl.wx.controller.CircleScheduleController.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        // 前置通知
        long beforeTime = System.currentTimeMillis();
        // 得到拦截的方法
        Method method = signature.getMethod();
        GetMapping getMapping = signature.getMethod().getAnnotation(GetMapping.class);
        PostMapping postMapping = signature.getMethod().getAnnotation(PostMapping.class);
        String[] value = null;
        if (postMapping == null) {
            // 请求为get
            value = getMapping.value();
        } else {
            value = postMapping.value();
        }
        // 获取方法参数注解，返回二维数组是因为某些参数可能存在多个注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return pjp.proceed();
        }
        // 获取方法参数名
        String[] paramNames = signature.getParameterNames();
        // 获取参数值
        Object[] paramValues = pjp.getArgs();
        // 获取方法的参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        ParamCheck paramCheck = signature.getMethod().getAnnotation(ParamCheck.class);
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                // token 中已经检测无需检测
                //if ("userid".equals(paramNames[i]) || "userId".equals(paramNames[i])) {
                //    userIsNull((String) paramValues[i], value[0]);
                //}
                if ("circleid".equals(paramNames[i]) || "circleId".equals(paramNames[i])) {
                    circleIsNull(Long.valueOf(String.valueOf(paramValues[i])), value[0]);
                }
                if (paramCheck != null) {
                    // 说明该注解在方法上
                    paramIsNull(paramNames[i], paramValues[i], parameterTypes[i] == null ? null : parameterTypes[i].getName(), value[0]);
                    break;
                }
                if (parameterAnnotations[i][j] instanceof ParamCheck && ((ParamCheck) parameterAnnotations[i][j]).notNull()) {
                    paramIsNull(paramNames[i], paramValues[i], parameterTypes[i] == null ? null : parameterTypes[i].getName(), value[0]);
                    break;
                }
            }
        }
        result = pjp.proceed();
        // 返回通知
        long afterTime = System.currentTimeMillis();
        log.info("方法名：" + signature.getName() + "->方法执行时间：" + (afterTime - beforeTime) + "-->请求的Url：\'" + value[0] + "\'");
        // 无需后置通知、处理异常通知 paramIsNull方法处理
        return result;
    }

    private void circleIsNull(Long circleId, String url) {
        CircleInfo circleInfo = circleInfoService.selectByPrimaryKey(circleId);
        if (circleInfo == null) {
            throw new CircleIsNullException(circleId, url);
        }
    }

    private void userIsNull(String userId, String url) {
        UserInfo userInfo = userInfoService.selectByPrimaryKey(userId);
        if (userInfo == null) {
            throw new UserIsNullException(userId, url);
        }
    }

    private void paramIsNull(String paramName, Object value, String parameterType, String url) {
        if (value == null || "".equals(value.toString().trim())) {
            throw new ParamIsNullException(paramName, parameterType, url);
        }
    }
}
