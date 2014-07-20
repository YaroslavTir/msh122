package com.fls.metro.core.validation.validator

import com.fls.metro.core.data.dao.LineDao
import com.fls.metro.core.data.domain.Line
import com.fls.metro.core.validation.annotation.UniqueLineNumber
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * User: NFadin
 * Date: 03.06.2014
 * Time: 16:26
 */
@Slf4j
class LineUniqueNumberValidator implements ConstraintValidator<UniqueLineNumber, Line> {

    @Autowired
    private LineDao lineDao

    @Override
    void initialize(UniqueLineNumber constraintAnnotation) {
    }

    @Override
    boolean isValid(Line value, ConstraintValidatorContext context) {
        if (value.number) {
            def existingIm = lineDao.findByNumber(value.number)
            return !existingIm || existingIm.id == value.id
        }
        return true
    }
}
