package com.fls.metro.core.data.dao

import com.fls.metro.core.data.domain.Station
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 14:53
 */
@Slf4j
@Repository
class StationDao extends HierarchyDao<Station> {

    Integer lobbiesCount(Long id) {
        sql.rows('select count(id) as cnt from lobby where station_id = :id', [id: id])[0].cnt
    }

    List<Station> getStationByName(String stationName) {
            executeSelectRows name: stationName
    }
}
