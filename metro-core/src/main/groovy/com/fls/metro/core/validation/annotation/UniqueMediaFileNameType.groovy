package com.fls.metro.core.validation.annotation

import com.fls.metro.core.validation.validator.MediaFileUniqueNameTypeValidator

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * User: NFadin
 * Date: 03.06.2014
 * Time: 11:45
 */
@Target([ElementType.TYPE])
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MediaFileUniqueNameTypeValidator)
public @interface UniqueMediaFileNameType {
    String message() default "Type and name must be unique";
    Class<?>[] groups() default [];
    Class<? extends Payload>[] payload() default [];
}