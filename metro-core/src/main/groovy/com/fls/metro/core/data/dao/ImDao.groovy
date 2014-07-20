package com.fls.metro.core.data.dao

import com.fls.metro.core.data.domain.Im
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 10:20
 */
@Slf4j
@Repository
class ImDao extends HierarchyDao<Im> {
    Im findImByName(String imName) {
        executeSelect imName: imName
    }

    List<String> ips(List<String> imNames) {
        sql.rows('select ip from im where im_name in (:names)',
                [('names'): imNames.join(',')])*.ip
    }

    List<String> imNamesList() {
        sql.rows('select im_name from im')*.im_name
    }

    List<String> findImNames(Collection<Long> imIds) {
        if (imIds.isEmpty()) {
            return Collections.emptyList();
        }
        sql.rows(String.format('select im_name from im where id in (%s)', imIds.join(',')))*.im_name
    }

    List<String> findImsByLobby(Long lobbyId) {
        sql.rows('select im.im_name as im_name from im where lobby_id=?', [lobbyId])*.im_name;
    }

    List<String> findImsByStation(Long stationId) {
        sql.rows('select im.im_name as im_name from im left join lobby l on im.lobby_id=l.id where l.station_id=?', [stationId])*.im_name;
    }

    List<String> findImsByLine(Long lineId) {
        sql.rows('select im.im_name as im_name from im left join lobby l on im.lobby_id=l.id left join station s on l.station_id=s.id where s.line_id=?', [lineId])*.im_name;
    }

    List<String> findImsBySchema(Long schemaId) {
        sql.rows('select im.im_name as im_name from im left join lobby l on im.lobby_id=l.id left join station s on l.station_id=s.id left join line on s.line_id=line.id where line.schema_id=?', [schemaId])*.im_name;
    }

    String stationName(Long imId) {
        sql.rows('select s.name stationName ' +
                'from im i ' +
                'join lobby l on (i.lobby_id = l.id) ' +
                'join station s on (l.station_id = s.id) ' +
                'where i.id = :imId', [imId: imId]).stationName
    }

    List<Im> nameStartsWith(String imName) {
        sql.rows('select * from im where im_name like :imName', [imName : imName + '%']).collect {
            toDomain(it)
        }
    }
}
