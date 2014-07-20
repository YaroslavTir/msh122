package com.fls.metro.core.data.dao

import com.fls.metro.core.data.domain.User
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository

/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:11
 */
@Slf4j
@Repository
class UserDao extends AbstractDao<User> {
    User findByUsername(String username) {
        executeSelect username : username
    }

    List<User> list() {
        List<User> result = []
        sql.eachRow('select id, username, password, roles, fio from users order by username') {
            result.add(new User(id: it.id, username: it.username, password: it.password, roles: it.roles.array, fio: it.fio))
        }
        result
    }
}
