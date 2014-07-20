package com.fls.metro.core.data.dto.schedule
import com.fls.metro.core.annotation.DateField
import com.fls.metro.core.annotation.Ignore
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import org.codehaus.jackson.annotate.JsonIgnoreProperties

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
/**
 * User: NFadin
 * Date: 23.04.14
 * Time: 10:06
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@ToString
@TupleConstructor
class Schedule {
    @DateField
    Date startDate
    @DateField
    Date endDate

    @Ignore
    List<ScheduledContent> content

}
