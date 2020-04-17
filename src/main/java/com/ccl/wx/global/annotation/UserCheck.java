package com.ccl.wx.global.annotation;

import com.ccl.wx.global.validator.UserIsNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author 褚超亮
 * @date 2020/3/13 11:45
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {UserIsNullValidator.class})
public @interface UserCheck {

    String message() default "此用户不存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
