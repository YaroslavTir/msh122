package com.fls.metro.core.validation.validator

import com.fls.metro.core.data.domain.HierarchyObject
import com.fls.metro.core.data.domain.Im
import com.fls.metro.core.validation.annotation.NewsTitleNotEmpty
import groovy.util.logging.Slf4j

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * User: NFadin
 * Date: 29.05.2014
 * Time: 13:35
 */
@Slf4j
class NewsTitleNotEmptyValidator implements ConstraintValidator<NewsTitleNotEmpty, HierarchyObject> {
    @Override
    void initialize(NewsTitleNotEmpty constraintAnnotation) {
    }

    @Override
    boolean isValid(HierarchyObject o, ConstraintValidatorContext context) {
        !o.news.any {
            !it.title && !it.titleEn
        }
    }
}
