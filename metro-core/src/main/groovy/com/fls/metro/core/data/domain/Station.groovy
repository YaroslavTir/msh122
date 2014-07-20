package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Ignore
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.validation.annotation.UniqueInHierarchyLevelName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.constraints.NotNull
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 13:30
 */
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
@Table('station')
@Seq('station_seq')
@UniqueInHierarchyLevelName(message = '{station.name.must.be.unique.in.line}')
class Station extends HierarchyObject implements Comparable<Station>, WithStationPlanSettings {
    @Id Long id
    @NotNull(message = '{station.line.cannot.be.null}')
    Long lineId
    @NotEmpty(message = '{station.name.cannot.be.empty}')
    String name
    @NotEmpty(message = '{station.name.cannot.be.empty}')
    String enname

    @Ignore
    StationPlanSettings stationPlanSettings

    @Ignore
    Collection<Lobby> lobbies

    @Override
    int compareTo(Station o) {
        if (id.equals(o.id)) return 0

        if (id > o.id) return 1

        return -1
    }

    @Override
    @Ignore
    Long getParentId() {
        lineId
    }
}
