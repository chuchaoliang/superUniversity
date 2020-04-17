package com.ccl.wx.global.annotation;

import java.lang.annotation.*;

/**
 * 参数不能为空注解，作用于方法参数上
 *
 * @author 褚超亮
 * @date 2020/3/2 16:58
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamCheck {

    /**
     * 参数是否为非空，默认不能为空
     *
     * @return
     */
    boolean notNull() default true;
}
