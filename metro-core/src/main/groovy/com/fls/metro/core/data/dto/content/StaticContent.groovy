package com.fls.metro.core.data.dto.content
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 17:13
 */
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@ToString
class StaticContent extends UpdatableItem {
    String stationName
    String lineName
    String inactiveDefault
    Position position
    Integer number
    String color
    String emergencyNumber
    boolean screensaverEnabled
    Integer screensaverTimeoutSec
}
