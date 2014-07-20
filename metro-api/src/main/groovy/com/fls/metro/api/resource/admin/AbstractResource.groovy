package com.fls.metro.api.resource.admin

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.ValidatorFactory

/**
 * User: NFadin
 * Date: 12.05.14
 * Time: 16:44
 */
@Component
abstract class AbstractResource {

    @Autowired
    private ValidatorFactory validatorFactory

    protected <T> T validate(T o) {
        def violations = validatorFactory.validator.validate(o)
        if (violations) {
            throw new ConstraintViolationException(violations)
        }
        o
    }
}
