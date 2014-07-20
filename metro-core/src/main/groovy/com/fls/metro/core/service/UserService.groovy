package com.fls.metro.core.service

import com.fls.metro.core.data.dao.UserDao
import com.fls.metro.core.data.domain.Role
import com.fls.metro.core.data.domain.User
import com.fls.metro.core.data.dto.RemoveUserResult
import com.fls.metro.core.exception.UserAlreadyExistsException
import com.fls.metro.core.security.SecurityUtils
import com.fls.metro.core.security.UserDetailsUser
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:10
 */
@Slf4j
@Service
class UserService implements UserDetailsService {
    @Autowired
    @Qualifier('userPasswordEncoder')
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDao userDao

    @Value('${common.admin.name}')
    private String adminName

    @Transactional
    @Override
    UserDetailsUser loadUserByUsername(String username) throws UsernameNotFoundException {
        def user = userDao.findByUsername(username)
        if (!user) {
            log.info('User {} not found', username)
            throw new UsernameNotFoundException('user.not.found')
        }
        userDetails(user)
    }

    static UserDetailsUser userDetails(User user) {
        new UserDetailsUser(
                username: user.username,
                password: user.password,
                roles: user.roles,
                authorities: user.roles.collect {
                    new SimpleGrantedAuthority(it)
                } as Set)
    }

    @Transactional
    User create(String username, String password, List<Role> roles) throws UserAlreadyExistsException {
        create(new User(username: username, password: password, roles: roles))
    }

    @Transactional
    User create(User rawUser) {
        log.info('Start create user with name {}', rawUser.username)
        if (userDao.findByUsername(rawUser.username)) {
            throw new UserAlreadyExistsException();
        }
        def result = userDao.create(new User(username: rawUser.username, password: passwordEncoder.encode(rawUser.password), roles: rawUser.roles, fio: rawUser.fio))
        log.info('User with name {} was successfully created', rawUser.username)
        return result
    }

    @Transactional
    User update(User rawUser) {
        log.info('Update user with name {}', rawUser.username)
        User user = userDao.find(rawUser.id)
        if (!user) {
            throw new IllegalArgumentException("User with id $rawUser.id and name $rawUser.username wasn't found")
        }
        User result = mergeUser(user, rawUser)
        userDao.update(result)
        result
    }

    User mergeUser(User user, User rawUser) {
        if (rawUser.password) {
            user.password = passwordEncoder.encode(rawUser.password)
        }
        user.username = rawUser.username
        user.roles = rawUser.roles
        user.fio = rawUser.fio
        user
    }

    @Transactional
    User find(String username) {
        userDao.findByUsername(username)
    }

    @Transactional
    User find(Long id) {
        userDao.find(id)
    }

    @Transactional
    List<User> list() {
        userDao.list()
    }

    RemoveUserResult delete(Long id) {
        def user = userDao.find(id)
        switch (user.username) {
            case adminName:
                return RemoveUserResult.ERROR_REMOVE_ADMIN
            case SecurityUtils.username:
                return RemoveUserResult.ERROR_REMOVE_CURRENT_USER
            default:
                userDao.delete(id)
                log.info('User with id {} was deleted', id)
                return RemoveUserResult.OK
        }
    }
}
