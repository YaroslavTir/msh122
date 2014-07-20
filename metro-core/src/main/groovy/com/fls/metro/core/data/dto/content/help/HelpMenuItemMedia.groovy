package com.fls.metro.core.data.dto.content.help

import com.fls.metro.core.data.domain.HelpInfo
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 15:56
 */
@EqualsAndHashCode
@ToString
class HelpMenuItemMedia extends HelpMenuItem {

    HelpMenuItemDataMedia data

    @Override
    HelpMenuItemDataType getType() {
        return HelpMenuItemDataType.MEDIA
    }

    @Override
    HelpMenuItemDataMedia getData() {
        return data
    }

    HelpMenuItemMedia(HelpInfo i){
        data=new HelpMenuItemDataMedia(media: i.getMedia(), text: i.getHtmlText(), title: i.title)
        caption = i.name
        updateDate = i.updateDate
    }
}
