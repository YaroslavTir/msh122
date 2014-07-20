package com.fls.metro.core.data.dto.schedule

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.joda.time.LocalTime

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 29.05.14
 * Time: 14:00
 * To change this template use File | Settings | File Templates.
 */
@EqualsAndHashCode
@ToString
class ScheduledContentWithLocalTime extends ScheduledContent{
    LocalTime localStartTime
    LocalTime localEndTime

    static LocalTime toLocalTime(String timeString){
        if(timeString!=null){
            LocalTime.parse(timeString);
        }
    }
}
