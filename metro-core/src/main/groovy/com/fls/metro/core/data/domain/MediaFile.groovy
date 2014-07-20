package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.annotation.TimestampField
import com.fls.metro.core.validation.annotation.UniqueMediaFileNameType
import com.fls.metro.core.validation.annotation.WindowsMediaCenterExtension
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
@WindowsMediaCenterExtension(message = '{media.file.unsupported.file.extension}')
@UniqueMediaFileNameType(message = '{media.file.name.and.type.must.be.unique}')
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
@Table('media')
@Seq('media_seq')
class MediaFile {
    @Id Long id
    String name
    @TimestampField
    Date createdDate
    String url
    MediaFileType mediaType
    String thumbUrl
}
