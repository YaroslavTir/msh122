package com.fls.metro.core.data.dao.statistic

import com.fls.metro.core.data.dao.AbstractDao
import com.fls.metro.core.data.domain.statistic.ImEvent
import org.springframework.stereotype.Repository

/**
 * User: NFadin
 * Date: 02.06.2014
 * Time: 13:29
 */
@Repository
class ImEventDao extends AbstractDao<ImEvent> {
    List<ImEvent> findByStatisticId(Long statisticId) {
        executeSelectRows statisticId : statisticId
    }
}
