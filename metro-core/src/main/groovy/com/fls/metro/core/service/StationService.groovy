package com.fls.metro.core.service

import com.fls.metro.core.data.dao.StationDao
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.domain.Station
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
class StationService extends HierarchyObjectService<Station> {

    @Autowired
    private StationDao stationDao

    @Transactional
    Station get(Long id) {
        log.info('Get station with id {}', id)
        get(ObjectType.STATION, id)
    }

    @Transactional
    Station create(Station station) {
        log.info('Create station with name {}', station.name)
        station = stationDao.create(station)
        log.info('Station with name {}. Id is {}', station.name, station.id)
        return station
    }

    @Transactional
    Station update(Station station) {
        log.info('Update station with id {}', station.id)
        station = setDataForUpdate(ObjectType.STATION, station) { Station it, Station old ->
            if (it.name != old.name) {
                it.staticDataUpdateDate = new Date()
            }
            return it
        }
        stationDao.update(station)
        log.info('Station with id {} was successfully updated', station.id)
        return station
    }

    @Transactional
    RemoveHierarchyObjectResult delete(Long id) {
        if (stationDao.lobbiesCount(id) > 0) {
            log.info('Can\'t remove station with id {}', id)
            return RemoveHierarchyObjectResult.REMOVE_CHILD
        }
        log.info('Delete station with id {}', id)
        prepareDelete(ObjectType.STATION,id)
        stationDao.delete(id)
        log.info('Station with id {} was successfully deleted', id)
        return RemoveHierarchyObjectResult.OK
    }
}
