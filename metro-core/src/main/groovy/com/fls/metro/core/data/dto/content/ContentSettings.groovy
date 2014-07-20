package com.fls.metro.core.data.dto.content

import com.fls.metro.core.annotation.TimestampField
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 27.05.14
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
class ContentSettings {

    @TimestampField
    Date updateDate

    Boolean showCurrency = Boolean.TRUE
    Boolean showWeather = Boolean.TRUE
    Boolean showTime = Boolean.TRUE
    Boolean showLanguages = Boolean.TRUE
    Boolean showExternalNews = Boolean.TRUE

}
