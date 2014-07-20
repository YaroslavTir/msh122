package com.fls.metro.core.service

import com.fls.metro.core.data.dto.content.Language
import com.fls.metro.core.data.dto.content.Weather
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * User: NFadin
 * Date: 28.04.14
 * Time: 16:12
 */
@Slf4j
@Service
class WeatherService {

    @Autowired
    private NotificationService notificationService

    private Map<Language, Weather> languageWeather = new HashMap<>()

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    Weather get(Language lan) {
        lock.readLock().lock()
        def result = null
        try {
            def w = languageWeather[lan]
            if (w) {
                result = new Weather(
                        image: w.image,
                        temperature: w.temperature,
                        updateDate: w.updateDate
                )
            }
        } finally {
            lock.readLock().unlock()
        }
        result
    }

    void refreshWeather(Map<Language, Weather> weatherMap) {
        if (!weatherMap) { return }
        lock.writeLock().lock()
        try {
            if (languageWeather) {
                if (languageWeather?.every { k, v -> weatherMap[k] == v }) {
                    return
                }
            }
            log.info('Refresh weather data')
            notificationService.notifyAllIms()
            languageWeather = weatherMap
            log.info('Weather data is refreshed')
        } finally {
            lock.writeLock().unlock()
        }
    }
}
