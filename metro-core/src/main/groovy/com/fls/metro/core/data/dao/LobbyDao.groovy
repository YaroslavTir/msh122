package com.fls.metro.core.data.dao

import com.fls.metro.core.data.domain.Lobby
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 14:52
 */
@Slf4j
@Repository
class LobbyDao extends HierarchyDao<Lobby> {

    Integer imsCount(Long id) {
        sql.rows('select count(id) as cnt from im where lobby_id = :id', [id: id])[0].cnt
    }
}
