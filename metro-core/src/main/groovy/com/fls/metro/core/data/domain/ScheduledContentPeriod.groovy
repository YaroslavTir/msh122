package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.annotation.TimeField
import com.fls.metro.core.data.dto.content.media.MediaContentInternalSize
import com.fls.metro.core.data.dto.content.media.MediaContentInternalType
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
@Table('sch_content')
@Seq('sch_content_seq')
class ScheduledContentPeriod implements WithId{
    @Id Long id
    @TimeField
    String startTime
    @TimeField
    String endTime

    MediaContentInternalType contentType
    MediaContentInternalSize mediaSize
    String infoText
    String fileUrl
    String audioUrl
    String bgColor

    Long mediaContentId

    Long schId
}
