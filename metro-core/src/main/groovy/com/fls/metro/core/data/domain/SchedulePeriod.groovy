package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.data.dto.schedule.Schedule
import com.fls.metro.core.data.dto.schedule.ScheduleType
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
@Table('schedule')
@Seq('schedule_seq')
class SchedulePeriod extends Schedule implements WithId, WithOwner{
    @Id
    Long id
    ScheduleType type
    ObjectType ownerType
    Long ownerId
}
