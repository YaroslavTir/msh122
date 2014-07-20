package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Ignore
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.validation.annotation.UniqueImName
import com.fls.metro.core.validation.annotation.UniqueInHierarchyLevelName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 10:18
 */
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
@Table('im')
@Seq('im_seq')
@UniqueImName(message = '{im.im.name.must.be.unique}')
@UniqueInHierarchyLevelName(message = '{im.name.must.be.unique.in.lobby}')
class Im extends HierarchyObject implements Comparable<Im>, WithStationPlanSettings {
    @Id
    Long id
    @NotNull(message = '{im.lobby.cannot.be.null}')
    Long lobbyId
    @NotEmpty(message = '{im.name.cannot.be.empty}')
    String name
    String ip
    Integer port
    @NotEmpty(message = '{im.im.name.cannot.be.empty}')
    @Pattern(regexp = '[A-Za-z0-9]+', message = '{im.im.name.should.be.latin}')
    String imName
    String position
    String latitude
    String longitude

    @Ignore
    StationPlanSettings stationPlanSettings

    @Override
    int compareTo(Im o) {
        if (id.equals(o.id)) return 0

        if (id > o.id) return 1

        return -1
    }

    @Override
    @Ignore
    Long getParentId() {
        lobbyId
    }
}
