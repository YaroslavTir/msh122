package com.fls.metro.core.data.dto.content

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 17:12
 */
enum Area {
    SCREEN(1080, 1920),
    BANNER(1080, 378),
    SCREENSAVER(1080,1070),
    STATION_PLAN(740, 866)

    Area(int width, int height){
        this.width=width;
        this.height=height;
    }

    int width
    int height

}
