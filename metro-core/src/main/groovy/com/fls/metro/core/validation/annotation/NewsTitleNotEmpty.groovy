package com.fls.metro.core.validation.annotation

import com.fls.metro.core.validation.validator.NewsTitleNotEmptyValidator

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * User: NFadin
 * Date: 29.05.2014
 * Time: 13:35
 */
@Target([ElementType.TYPE])
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NewsTitleNotEmptyValidator)
public @interface NewsTitleNotEmpty {
    String message() default "At least one news title should be not empty";
    Class<?>[] groups() default [];
    Class<? extends Payload>[] payload() default [];
}