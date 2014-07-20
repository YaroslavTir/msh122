package com.fls.metro.core.data.domain
import com.fls.metro.core.annotation.*
import com.fls.metro.core.data.dto.content.ContentItem
import com.fls.metro.core.data.dto.schedule.Schedule
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
@Table('banner_settings')
@Seq('banner_settings_seq')
class BannerSettings implements WithId, WithOwner, WithSchedule{
    @Id Long id

    ObjectType ownerType
    Long ownerId

    @TimestampField
    Date updateDate

    String imageUrl

    @Ignore
    List<Schedule> schedule;

    @Ignore
    List<ContentItem> videos;

    @Ignore
    boolean own =true;


    static BannerSettings copy(BannerSettings bs){
        return new BannerSettings(ownerType:bs.ownerType, ownerId:bs.ownerId, updateDate: new Date(),
                schedule: bs.schedule, videos: bs.videos, imageUrl: bs.imageUrl
        )
    }

}
