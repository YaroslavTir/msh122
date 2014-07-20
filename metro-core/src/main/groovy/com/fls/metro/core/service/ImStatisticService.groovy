package com.fls.metro.core.service

import com.fls.metro.core.data.dao.statistic.ImEventDao
import com.fls.metro.core.data.dao.statistic.ImStatisticDao
import com.fls.metro.core.data.domain.statistic.ImEvent
import com.fls.metro.core.data.domain.statistic.ImStatistic
import com.fls.metro.core.data.dto.grid.data.ImStatisticData
import com.fls.metro.core.data.filter.ImStatisticFilter
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: NFadin
 * Date: 02.06.2014
 * Time: 13:15
 */
@Service
@Slf4j
class ImStatisticService {

    @Autowired
    private ImEventDao eventDao
    @Autowired
    private ImStatisticDao statisticDao

    private static final Closure<Boolean> eventChecker = { ImEvent it ->
        it.event.informative && it.fires && it.fires != 0
    }

    @Transactional
    void add(ImStatistic statistic) {
        log.info('Add statistic {}', statistic)
        if (!statistic.events.any(eventChecker)) {
            log.info('Nothing to add')
        } else {
            statisticDao.create(statistic)
            statistic.events.each {
                if (eventChecker.call(it)) {
                    it.statisticId = statistic.id
                    eventDao.create(it)
                }
            }
            log.info('Statistic for im {} was successfully added', statistic.imName)
        }
    }

    @Transactional
    ImStatisticData filter(ImStatisticFilter filter) {
        new ImStatisticData(data: statisticDao.filter(filter))
    }

    @Transactional
    ImStatistic get(Long id) {
        def result = statisticDao.find(id)
        if (result) {
            result.events = eventDao.findByStatisticId(id)
        }
        result
    }
}
