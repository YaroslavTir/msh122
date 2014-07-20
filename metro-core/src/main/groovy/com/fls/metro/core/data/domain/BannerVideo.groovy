package com.fls.metro.core.data.domain
import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
@Table('banner_video')
@Seq('banner_video_seq')
class BannerVideo implements WithId{
    @Id Long id
    Integer number
    Long bannerSettingsId

    String fileUrl

}
