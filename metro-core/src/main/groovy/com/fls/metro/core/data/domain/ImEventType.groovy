package com.fls.metro.core.data.domain

/**
 * User: NFadin
 * Date: 02.06.2014
 * Time: 12:55
 */
public enum ImEventType {
    MAP_ACTIVATED('MapActivated', true),
    MAP_SEARCH('MapSearch', false),
    MAP_USING('MapUsing', false),
    METRO_MAP_ACTIVATED('MetroMapActivated', true),
    METRO_MAP_USING('MetroMapUsing', false),
    STATION_SCHEME_ACTIVATED('StationSchemeActivated', true),
    SCREEN_SAVER_TAKEN_OFF('ScreenSaverTakenOff', true),
    HELP_ACTIVATED('HelpActivated', true),
    FEEDBACK_ACTIVATED('FeedbackActivated', true)

    String event
    boolean informative

    private ImEventType(String event, boolean informative) {
        this.event = event
        this.informative = informative
    }

    static getByEvent(String event) {
        values().find {
            it.event == event
        }
    }
}