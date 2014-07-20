package com.fls.metro.core.job

import com.fls.metro.core.data.dao.LineDao
import com.fls.metro.core.data.dao.LobbyDao
import com.fls.metro.core.data.dao.StationDao
import com.fls.metro.core.data.domain.Line
import com.fls.metro.core.data.domain.Lobby
import com.fls.metro.core.data.domain.Role
import com.fls.metro.core.data.domain.Schema
import com.fls.metro.core.data.domain.Station
import com.fls.metro.core.service.SchemaService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * User: APermyakov
 * Date: 28.04.14
 * Time: 16:01
 */

@Slf4j
@Component
class DefaultDataJob extends AbstractJob{

    @Autowired
    SchemaService schemaService

    @Autowired
    LineDao lineDao

    @Autowired
    StationDao stationDao

    @Autowired
    LobbyDao lobbyDao

    @Override
    @Transactional
    void runJob() throws Exception {
//
//        if (!schemaService.hasSchema()) {
//            Node sch = new XmlParser().parseText(file("schema1.xml").getText("utf-8"))
//
//            Schema schema = new Schema(name: sch.'@name')
//            schemaService.create(schema)
//            sch.line.each {
//                Line line = new Line(
//                        schemaId: schema.id,
//                        name: it.'@name',
//                        number : Integer.parseInt(it.'@number'),
//                        picLink: it.'@picLink',
//                        color: it.'@color')
//                lineDao.create(line)
//                it.station.each {
//                    Station station = new Station(lineId: line.id, name : it.'@name', enname: it.'@enname')
//                    stationDao.create(station)
//                    it.lobby.each {
//                        lobbyDao.create(new Lobby(stationId: station.id, name: it.'@name'))
//                    }
//                }
//            }
//        } else {
//            Node sch = new XmlParser().parseText(file("schema1.xml").getText("utf-8"))
//            sch.line.each {
//                Line l = lineDao.findUniqueByName(it.'@name')
//                if (l != null && l.enname != null && l.enname.trim().isEmpty()) {
//                    l.enname = it.'@enname'
//                    lineDao.update(l)
//                }
//
//                it.station.each {
//                    List<Station> stations = stationDao.getStationByName(it.'@name')
//
//                    if (stations.isEmpty()) {
//                        return
//                    }
//                    if (stations.get(0).enname != null && !stations.get(0).enname.trim().isEmpty()) {
//                        return
//                    }
//
//                    for (Station s : stations) {
//                        s.enname = it.'@enname'
//                        stationDao.update(s)
//                    }
//                }
//            }
//        }
    }

    private InputStream file(name) {
        return DefaultDataJob.class.getResourceAsStream("/data/${name}")
    }
}
