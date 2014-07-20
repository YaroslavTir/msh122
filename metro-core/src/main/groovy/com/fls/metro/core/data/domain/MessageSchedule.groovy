package com.fls.metro.core.data.domain
import com.fls.metro.core.annotation.*
import com.fls.metro.core.data.dto.schedule.Schedule
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
@Table('message_schedule')
@Seq('message_schedule_seq')
class MessageSchedule implements WithId, WithOwner, WithSchedule{
    @Id Long id

    ObjectType ownerType
    Long ownerId

    @TimestampField
    Date updateDate

    @Ignore
    List<Schedule> schedule;

    @Ignore
    boolean own=true;


    static MessageSchedule copy(MessageSchedule bs){
        return new MessageSchedule(ownerType:bs.ownerType, ownerId:bs.ownerId, updateDate: new Date(),
                schedule: bs.schedule
        )
    }

}
