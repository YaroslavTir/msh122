package com.fls.metro.core.service

import com.fls.metro.core.data.domain.ObjectType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * User: NFadin
 * Date: 06.05.14
 * Time: 14:35
 */
@Slf4j
@Service
class HierarchyDataRetriever {

    @Autowired
    private HierarchyService hierarchyService

    List collectParentLevel(ObjectType objectType, def id, Closure cb) {
        collectLevels(objectType, id, cb, false, true, true)
    }

    List collectCurrentLevel(ObjectType objectType, def id, Closure cb) {
        collectLevels(objectType, id, cb, true, false, false)
    }

    List collectAllLevels(ObjectType objectType, def id, Closure cb) {
        collectLevels(objectType, id, cb, false, false, false)
    }

    List collectAllLevelsFirstOccurrence(ObjectType objectType, def id, Closure cb) {
        collectLevels(objectType, id, cb, false, true, false)
    }

    private List collectLevels(ObjectType objectType, def id, Closure cb, boolean onlyCurrent, boolean firstOccurrence, boolean onlyParent) {

        def result = []
        def o = hierarchyService.get(objectType, id)
        if (!o) {
            return result
        }
        def ot = objectType
        def oid = o.id
        def cbr
        if (!onlyParent) {
            cbr = cb.call(ot, oid, o)
            if (cbr) {
                result.addAll(cbr)
            }
            if (firstOccurrence && result) {
                return result
            }
            if (onlyCurrent) {
                return result
            }
        }

        while (ot.parent) {
            o = hierarchyService.getParent(ot, oid)
            ot = ot.parent
            oid = o.id
            cbr = cb.call(ot, oid, o)
            if (cbr) {
                result.addAll(cbr)
            }
            if (firstOccurrence && result) {
                break
            }
        }
        result
    }
}
