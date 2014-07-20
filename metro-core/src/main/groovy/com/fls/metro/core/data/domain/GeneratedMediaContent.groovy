package com.fls.metro.core.data.domain
import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.data.dto.content.media.MediaContent
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 08.05.14
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
@Table('media_content')
@Seq('media_content_seq')
class GeneratedMediaContent extends MediaContent implements WithId{
    @Id Long id
    boolean  generated
}
