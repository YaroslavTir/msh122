package com.fls.metro.core.validation.annotation

import com.fls.metro.core.validation.validator.WindowsMediaCenterExtensionValidator

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * User: NFadin
 * Date: 06.06.2014
 * Time: 16:31
 */
@Target([ElementType.TYPE])
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WindowsMediaCenterExtensionValidator)
public @interface WindowsMediaCenterExtension {
    String message() default 'Unsupported file extension'
    Class<?>[] groups() default [];
    Class<? extends Payload>[] payload() default [];
}