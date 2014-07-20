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
 * Time: 13:31
 */
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
@Table('lobby')
@Seq('lobby_seq')
@UniqueInHierarchyLevelName(message = '{lobby.name.must.be.unique.in.station}')
class Lobby extends HierarchyObject implements Comparable<Lobby>, WithStationPlanSettings{
    @Id Long id
    @NotNull(message = '{lobby.station.cannot.be.null}')
    Long stationId
    @NotEmpty(message = '{lobby.name.cannot.be.empty}')
    String name

    @Ignore
    StationPlanSettings stationPlanSettings

    @Ignore
    Collection<Im> ims

    @Override
    int compareTo(Lobby o) {
        if (id.equals(o.id)) return 0

        if (id > o.id) return 1

        return -1
    }

    @Override
    @Ignore
    Long getParentId() {
        stationId
    }
}
