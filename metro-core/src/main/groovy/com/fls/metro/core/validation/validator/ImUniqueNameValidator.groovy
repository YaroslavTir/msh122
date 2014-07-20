package com.fls.metro.core.validation.validator

import com.fls.metro.core.data.dao.ImDao
import com.fls.metro.core.data.domain.Im
import com.fls.metro.core.service.ImService
import com.fls.metro.core.validation.annotation.UniqueImName
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * User: NFadin
 * Date: 16.05.14
 * Time: 18:06
 */
@Slf4j
class ImUniqueNameValidator implements ConstraintValidator<UniqueImName, Im> {

    @Autowired
    private ImDao imDao

    @Override
    void initialize(UniqueImName constraintAnnotation) {
    }

    @Override
    boolean isValid(Im value, ConstraintValidatorContext context) {
        if (value.imName) {
            def existingIm = imDao.findImByName(value.imName)
            return !existingIm || existingIm.id == value.id
        }
        return true
    }
}
