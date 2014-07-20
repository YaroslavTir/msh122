package com.fls.metro.core.service

import com.fls.metro.core.data.dao.LobbyDao
import com.fls.metro.core.data.domain.Lobby
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.dto.RemoveHierarchyObjectResult
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: NFadin
 * Date: 08.05.14
 * Time: 12:36
 */
@Service
@Slf4j
class LobbyService extends HierarchyObjectService<Lobby> {

    @Autowired
    private LobbyDao lobbyDao

    @Transactional
    Lobby get(Long id) {
        log.info('Get lobby with id {}', id)
        get(ObjectType.LOBBY, id)
    }

    @Transactional
    Lobby create(Lobby lobby) {
        log.info('Create lobby with name {}', lobby.name)
        lobby = lobbyDao.create(lobby)
        log.info('Lobby with name {}. Id is {}', lobby.name, lobby.id)
        return lobby
    }

    @Transactional
    Lobby update(Lobby lobby) {
        log.info('Update lobby with id {}', lobby.id)
        lobby = setDataForUpdate(ObjectType.LOBBY, lobby)
        lobbyDao.update(lobby)
        log.info('Lobby with id {} was successfully updated', lobby.id)
        return lobby
    }

    @Transactional
    RemoveHierarchyObjectResult delete(Long id) {
        if (lobbyDao.imsCount(id) > 0) {
            log.info('Can\'t remove lobby with id {}', id)
            return RemoveHierarchyObjectResult.REMOVE_CHILD
        }
        log.info('Delete lobby with id {}', id)
        prepareDelete(ObjectType.LOBBY,id)
        lobbyDao.delete(id)
        log.info('Lobby with id {} was successfully deleted', id)
        return RemoveHierarchyObjectResult.OK
    }
}
