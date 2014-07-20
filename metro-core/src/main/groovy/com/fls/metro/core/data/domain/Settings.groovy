package com.fls.metro.core.data.domain
import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Ignore
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.data.dto.content.ContentSettings
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
@Table('settings')
@Seq('settings_seq')
class Settings extends ContentSettings implements WithId {
    @Id Long id

    ObjectType ownerType
    Long ownerId

    String emergencyNumber
    Integer screensaverTimeoutSec

    @Ignore
    Boolean own = Boolean.TRUE

    static Settings copy(Settings s) {
        return new Settings(ownerType: s.ownerType, ownerId: s.ownerId, updateDate: s.updateDate, emergencyNumber: s.emergencyNumber,
                showCurrency: s.showCurrency, showWeather: s.showWeather, showTime: s.showTime,
                showLanguages: s.showLanguages, showExternalNews: s.showExternalNews,
                screensaverTimeoutSec: s.screensaverTimeoutSec);
    }


}
