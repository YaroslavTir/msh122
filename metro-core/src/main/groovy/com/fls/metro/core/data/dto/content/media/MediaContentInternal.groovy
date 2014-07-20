package com.fls.metro.core.data.dto.content.media

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
class MediaContentInternal {
    MediaContentInternalType type
    MediaContentInternalSize size
    String infoText
    String fileUrl
    String audioUrl
    String bgColor
}
