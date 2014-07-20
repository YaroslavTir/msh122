package com.fls.metro.core.validation.annotation

import com.fls.metro.core.validation.validator.HierarchyObjectUniqueNameValidator
import com.fls.metro.core.validation.validator.ImUniqueNameValidator

import javax.validation.Constraint
import javax.validation.Payload
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * User: NFadin
 * Date: 16.05.14
 * Time: 18:05
 */
@Target([ElementType.TYPE])
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImUniqueNameValidator.class)
@interface UniqueImName {
    String message() default "Im name must be unique";
    Class<?>[] groups() default [];
    Class<? extends Payload>[] payload() default [];
}