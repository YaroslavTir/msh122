package com.fls.metro.core.data.domain
import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Ignore
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.annotation.TimestampField
import com.fls.metro.core.data.dto.content.StationPlanInfo
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

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
@Table('station_plan_settings')
@Seq('station_plan_settings_seq')
class StationPlanSettings extends StationPlanInfo implements WithId, WithOwner {
    @Id Long id

    ObjectType ownerType
    Long ownerId
    @TimestampField
    Date updateDate

    @Ignore
    List<StationPlanSettingsItem> items

    @Ignore
    Boolean own = true

    static StationPlanSettings copy(StationPlanSettings s) {
        return new StationPlanSettings(ownerType: s.ownerType, ownerId: s.ownerId,
        rusUrl: s.rusUrl, engUrl: s.engUrl);
    }


}
