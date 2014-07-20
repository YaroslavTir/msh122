package com.fls.metro.core.data.dto.content.help

import com.fls.metro.core.data.dto.content.UpdatableItem
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 14:32
 */
@EqualsAndHashCode
@ToString
class HelpMenu extends UpdatableItem {
    List<HelpMenuItem> items
}
