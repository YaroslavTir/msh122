package com.fls.metro.core.data.dto.content.help

import com.fls.metro.core.data.dto.content.HelpMediaData
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 16:00
 */
@EqualsAndHashCode
@ToString
class HelpMenuItemDataMedia extends HelpMenuItemData {
    List<HelpMediaData> media
}
