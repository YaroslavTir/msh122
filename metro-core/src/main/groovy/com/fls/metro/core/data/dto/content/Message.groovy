package com.fls.metro.core.data.dto.content

import com.fls.metro.core.data.dto.content.media.MediaContent
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 16.05.14
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
@EqualsAndHashCode
@ToString
class Message {
    MessageType type
    Date updateDate
    MediaContent content
}
