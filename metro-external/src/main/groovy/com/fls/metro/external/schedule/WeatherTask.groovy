package com.fls.metro.external.schedule

import com.fls.metro.core.data.dto.content.Language
import com.fls.metro.core.data.dto.content.Weather
import com.fls.metro.core.service.WeatherService
import com.fls.metro.external.service.WeatherRetrieveService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired

/**
 * User: NFadin
 * Date: 28.04.14
 * Time: 16:17
 */
@Slf4j
class WeatherTask extends RefreshDataTask<Map<Language, Weather>> {
    @Autowired
    private WeatherService weatherService
    @Autowired
    private WeatherRetrieveService weatherRetrieveService

    @Override
    Map<Language, Weather> doRetrieve() {
        weatherRetrieveService.retrieve()
    }

    @Override
    void doRefresh(Map<Language, Weather> languageWeatherMap) {
        weatherService.refreshWeather(languageWeatherMap)
    }
}
