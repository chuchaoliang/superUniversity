package com.ccl.wx.validator;

import com.ccl.wx.annotation.UserCheck;
import com.ccl.wx.entity.UserInfo;
import com.ccl.wx.exception.UserIsNullException;
import com.ccl.wx.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 用户是否为空校检
 *
 * @author 褚超亮
 * @date 2020/3/13 11:48
 */
public class UserIsNullValidator implements ConstraintValidator<UserCheck, String> {

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        UserInfo userInfo = userInfoService.selectByPrimaryKey(value);
        if (userInfo == null) {
            throw new UserIsNullException(value, "未知url");
        }
        return true;
    }

    @Override
    public void initialize(UserCheck constraintAnnotation) {
    }
}
