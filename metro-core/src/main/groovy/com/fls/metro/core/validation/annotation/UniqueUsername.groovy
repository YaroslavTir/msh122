package com.fls.metro.core.validation.annotation

import com.fls.metro.core.validation.validator.MediaFileUniqueNameTypeValidator
import com.fls.metro.core.validation.validator.UserUniqueUsernameValidator

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * User: NFadin
 * Date: 05.06.2014
 * Time: 18:35
 */
@Target([ElementType.TYPE])
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserUniqueUsernameValidator)
public @interface UniqueUsername {
    String message() default "Username must be unique";
    Class<?>[] groups() default [];
    Class<? extends Payload>[] payload() default [];
}