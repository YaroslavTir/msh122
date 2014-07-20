package com.fls.metro.core.data.dto.content.help

import com.fls.metro.core.data.dto.content.HtmlDataFileToLinkMapper
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 15:18
 */
@EqualsAndHashCode
@ToString
class HelpMenuItemDataHtml extends HelpMenuItemData {
    List<HtmlDataFileToLinkMapper> images
}
