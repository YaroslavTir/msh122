package com.fls.metro.core.validation.validator

import com.fls.metro.core.data.dao.MediaDao
import com.fls.metro.core.data.domain.MediaFile
import com.fls.metro.core.validation.annotation.UniqueMediaFileNameType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * User: NFadin
 * Date: 03.06.2014
 * Time: 11:45
 */
@Slf4j
class MediaFileUniqueNameTypeValidator implements ConstraintValidator<UniqueMediaFileNameType, MediaFile> {

    @Autowired
    private MediaDao mediaDao

    @Override
    void initialize(UniqueMediaFileNameType constraintAnnotation) {
    }

    @Override
    boolean isValid(MediaFile value, ConstraintValidatorContext context) {
        !mediaDao.findByNameAndType(value.name, value.mediaType)
    }
}
