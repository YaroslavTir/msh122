package com.fls.metro.core.data.dto.content

import com.fls.metro.core.data.domain.GeneratedMediaContent
import com.fls.metro.core.data.dto.content.media.MediaContentInternal
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 20.05.14
 * Time: 12:02
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@ToString
@TupleConstructor
class ContentItem {

    MediaContentInternal content
    GeneratedMediaContent contentExt
}
