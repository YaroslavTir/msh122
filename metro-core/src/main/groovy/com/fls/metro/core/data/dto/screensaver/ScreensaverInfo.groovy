package com.fls.metro.core.data.dto.screensaver

import com.fls.metro.core.data.domain.MediaFile
import com.fls.metro.core.data.domain.ObjectType
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * User: NFadin
 * Date: 16.05.14
 * Time: 12:23
 */
@EqualsAndHashCode
@ToString
class ScreensaverInfo {
    String url
    ObjectType owner
    String parentUrl
    ObjectType parentType
}
