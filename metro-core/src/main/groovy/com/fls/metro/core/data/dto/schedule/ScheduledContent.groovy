package com.fls.metro.core.data.dto.schedule
import com.fls.metro.core.data.dto.content.ContentItem
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
class ScheduledContent extends ContentItem{
    String startTime //0.00<= startTime <23.59
    String endTime   //0.00 < endTime <=23.59

}
