package com.fls.metro.core.data.dto.content

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 17:21
 */
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@ToString
class InfoZoneText extends UpdatableItem {
    String text
    List<InfoZoneTextInterval> intervals
}
