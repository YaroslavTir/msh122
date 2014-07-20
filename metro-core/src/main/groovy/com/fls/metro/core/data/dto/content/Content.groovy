package com.fls.metro.core.data.dto.content

import com.fls.metro.core.data.dto.content.help.HelpMenu
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
/**
 * User: NFadin
 * Date: 23.04.14
 * Time: 10:06
 */
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@ToString
@TupleConstructor
class Content {
    Language lang
    StaticContent staticContent
    StationPlan stationPlan
    ImNews news
    Weather weather
    List<ExchangeRate> exchangeRate
    HelpMenu help
    Message message
    BannerVideo bannerVideo
    BannerMessage bannerMessage
    ContentSettings contentSettings
}
