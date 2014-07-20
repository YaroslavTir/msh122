package com.fls.metro.core.data.dto.content.media

import com.fls.metro.core.data.dto.content.Area
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * User: NFadin
 * Date: 21.05.14
 * Time: 16:00
 */
@EqualsAndHashCode
@ToString
class MediaContentGenerateRequest {
    MediaContentInternal content
    Area area
}
