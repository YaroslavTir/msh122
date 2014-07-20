package com.fls.metro.core.validation.validator

import com.fls.metro.core.data.dao.UserDao
import com.fls.metro.core.data.domain.User
import com.fls.metro.core.validation.annotation.UniqueUsername
import org.springframework.beans.factory.annotation.Autowired

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * User: NFadin
 * Date: 05.06.2014
 * Time: 18:35
 */
class UserUniqueUsernameValidator implements ConstraintValidator<UniqueUsername, User> {

    @Autowired
    private UserDao userDao

    @Override
    void initialize(UniqueUsername constraintAnnotation) {
    }

    @Override
    boolean isValid(User value, ConstraintValidatorContext context) {
        if (value.username) {
            def existingUser = userDao.findByUsername(value.username)
            return !existingUser || existingUser.id == value.id
        }
        return true
    }
}
