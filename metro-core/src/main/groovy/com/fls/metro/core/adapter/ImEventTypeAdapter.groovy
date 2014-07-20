package com.fls.metro.core.adapter

import com.fls.metro.core.data.domain.ImEventType

import javax.xml.bind.annotation.adapters.XmlAdapter

/**
 * User: NFadin
 * Date: 02.06.2014
 * Time: 13:08
 */
class ImEventTypeAdapter extends XmlAdapter<String, ImEventType> {
    @Override
    ImEventType unmarshal(String event) throws Exception {
        ImEventType.getByEvent(event) ?: ImEventType.valueOf(event)
    }

    @Override
    String marshal(ImEventType eventType) throws Exception {
        eventType.name()
    }
}
