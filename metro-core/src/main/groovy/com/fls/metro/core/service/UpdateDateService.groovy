package com.fls.metro.core.service

import com.fls.metro.core.data.domain.HierarchyObject
import com.fls.metro.core.data.domain.ObjectType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: NFadin
 * Date: 16.05.14
 * Time: 13:36
 */
@Service
@Slf4j
class UpdateDateService {
    @Autowired
    private HierarchyDataRetriever hierarchyDataRetriever
    @Autowired
    private ImService imService

    @Transactional
    Date getStaticDataUpdateDate(ObjectType objectType, def id) {
        getMaxDate(objectType, id, 'staticDataUpdateDate')
    }

    @Transactional
    Date getNewsUpdateDate(ObjectType objectType, def id) {
        getMaxDate(objectType, id, 'newsUpdateDate')
    }

    private getMaxDate(ObjectType objectType, def id, String property) {
        Date currentDate = null;
        hierarchyDataRetriever.collectAllLevels(objectType, id) { ObjectType ot, oid, HierarchyObject o ->
            Date date = o.getProperty(property) as Date
            if (!currentDate) {
                currentDate = date
            }
            if (date && currentDate.before(date)) {
                currentDate = date
            }
            return currentDate
        }
        return currentDate
    }
}
