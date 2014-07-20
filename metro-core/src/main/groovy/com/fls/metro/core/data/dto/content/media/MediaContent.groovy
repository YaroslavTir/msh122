package com.fls.metro.core.data.dto.content.media

import com.fls.metro.core.annotation.TimestampField
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 13.05.14
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
@EqualsAndHashCode
@ToString
class MediaContent {
    MediaContentType type
    String fileUrl
    String audioUrl
    @TimestampField
    Date updateDate
}
