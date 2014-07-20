package com.fls.metro.core.data.dto.content

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

/**
 * User: NFadin
 * Date: 06.05.14
 * Time: 13:12
 */
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@ToString
class ImNews extends UpdatableItem {
    List<ImNewsItem> items
}
