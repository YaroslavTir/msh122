package com.fls.metro.core.data.dao

import com.fls.metro.core.data.domain.Im
import com.fls.metro.core.data.domain.Line
import com.fls.metro.core.data.domain.Lobby
import com.fls.metro.core.data.domain.Schema
import com.fls.metro.core.data.domain.Station

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository

/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:11
 */
@Slf4j
@Repository
class SchemaDao extends HierarchyDao<Schema> {

    Schema getSchema() {
        Schema schema
        Set<Line> lines = new TreeSet<Line>();
        Map<Long, Set<Station>> lineStations = new HashMap<>();

        Set<Station> stations = new TreeSet<Station>();
        Map<Long, Set<Lobby>> stationLobby = new HashMap<>();

        Set<Lobby> lobbies = new TreeSet<Lobby>();
        Map<Long, Set<Im>> lobbyIm = new HashMap<>();


        Line curLine
        Station curStation
        Lobby curLobby
        Im curIm

        sql.eachRow(' select ' +
                ' s.id schemaId, s.name schemaName, ' +
                ' l.id lineId, l.name lineName, l.number lineNumber, l.pic_link linePicLink, ' +
                ' st.id stationId, st.name stationName, ' +
                ' lob.id lobbyId, lob.name lobbyName, ' +
                ' i.name imName, i.id imId, i.im_name imImName, i.position imPosition, i.ip imIp, i.port imPort' +
                ' FROM ' +
                ' schema s ' +
                ' LEFT JOIN line l on (l.schema_id = s.id) ' +
                ' LEFT JOIN station st on (st.line_id = l.id) ' +
                ' LEFT JOIN lobby lob on (lob.station_id = st.id) ' +
                ' LEFT JOIN im i on i.lobby_id = lob.id ') {

            if (schema == null) {
                schema = new Schema(id: it.schemaId,  name: it.schemaName)
            }

            curLine = (it.lineId != null) ?
                new Line(schemaId: it.schemaId, id: it.lineId, name: it.lineName, number: it.lineNumber, picLink: it.linePicLink)
            : null

            curStation = (it.stationId != null) ?
                new Station(id : it.stationId, lineId: it.lineId, name: it.stationName)
            : null

            curLobby = (it.lobbyId != null) ?
                new Lobby(id: it.lobbyId, stationId: it.stationId, name: it.lobbyName)
            : null
            curIm = (it.imId != null) ?
                new Im(id: it.imId, lobbyId: it.lobbyId, name: it.imName, ip: it.imIp, imName : it.imImName, position : it.imPosition, port: it.imPort)
            : null


            if (curLine) {
                lines.add(curLine)
            }
            if (curStation) {
                stations.add(curStation)
            }
            if (curLobby) {
                lobbies.add(curLobby)
            }

            if (curStation) {
                if (lineStations.containsKey(curStation.lineId)) {
                    lineStations.get(curStation.lineId).add(curStation)
                } else {
                    Set<Station> setStations = new TreeSet<Station>()
                    setStations.add(curStation);
                    lineStations.put(curStation.lineId, setStations)
                }
            }

            if (curLobby) {
                if (stationLobby.containsKey(curLobby.stationId)) {
                    stationLobby.get(curLobby.stationId).add(curLobby)
                } else {
                    Set<Lobby> setLobby = new TreeSet<Lobby>()
                    setLobby.add(curLobby)
                    stationLobby.put(curLobby.stationId, setLobby)
                }
            }

            if (curIm) {
                if (lobbyIm.containsKey(curIm.lobbyId)) {
                    lobbyIm.get(curIm.lobbyId).add(curIm)
                } else {
                    Set<Im> setIms = new TreeSet<Im>()
                    setIms.add(curIm)
                    lobbyIm.put(curIm.lobbyId, setIms)
                }
            }
        }


        if (schema != null) {
            for (Lobby lo :lobbies) {
                lo.ims = lobbyIm.get(lo.id)
            }

            for (Station s :stations) {
                s.lobbies = stationLobby.get(s.id)
            }


            for (Line l : lines) {
                l.stations = lineStations.get(l.id)
            }

            schema.lines = lines
        }

        return schema;
    }

    boolean hasSchema() {
        sql.firstRow('select count(*) from schema').count > 0;
    }

    Long schemaId() {
        executeSelect()?.id
    }
}
