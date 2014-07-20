package com.fls.metro.external.service

import com.fls.metro.core.data.dto.content.Language
import com.fls.metro.core.data.dto.content.Weather
import com.fls.metro.core.util.Constants
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import java.text.SimpleDateFormat

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 11:12
 */
@Slf4j
@Service
class WeatherRetrieveService extends RetrieveXmlDataService<Map<Language, Weather>> {

    @Value('${content.weather.url}')
    private String url

    @Override
    String url() {
        url
    }

    Map<Language, Weather> retrieve(def forecast) {
        def fact = forecast.fact
        def temp = fact.temperature.text()
        def image = fact.image.text()
        def updateDate = new SimpleDateFormat(Constants.WEATHER_DATE_FORMAT).parse(fact.observation_time.text())
        [
                (Language.EN): new Weather(temperature: temp, image: image, updateDate: updateDate),
                (Language.RU): new Weather(temperature: temp, image: image, updateDate: updateDate)
        ]
    }
}
