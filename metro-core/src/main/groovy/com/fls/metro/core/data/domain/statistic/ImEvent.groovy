package com.fls.metro.core.data.domain.statistic

import com.fls.metro.core.adapter.ImEventTypeAdapter
import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.data.domain.ImEventType
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

/**
 * User: NFadin
 * Date: 02.06.2014
 * Time: 12:53
 */
@EqualsAndHashCode
@ToString
@Table('im_event')
@Seq('im_event_seq')
class ImEvent {
    @Id Long id
    Long statisticId
    @XmlJavaTypeAdapter(ImEventTypeAdapter)
    ImEventType event
    Integer fires = 0
}
