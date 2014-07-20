package com.fls.metro.core.service

import com.fls.metro.core.data.domain.HierarchyObject
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.dto.screensaver.ScreensaverInfo
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: NFadin
 * Date: 15.05.14
 * Time: 17:04
 */
@Slf4j
@Service
class ScreensaverService {

    @Autowired
    private HierarchyDataRetriever hierarchyDataRetriever
    @Autowired
    private ImService imService
    @Autowired
    private MediaService mediaService

    @Transactional
    ScreensaverInfo getFormIm(String imName) {
        get(ObjectType.IM, imService.getByImName(imName)?.id)
    }

    @Transactional
    ScreensaverInfo get(ObjectType objectType, def id) {
        Closure<ScreensaverInfo> cb = { ObjectType ot, oid, HierarchyObject o ->
            if (o.screensaverUrl) {
                return new ScreensaverInfo(
                        url: o.screensaverUrl,
                        owner: ot)
            }
            return null
        }
        def result = null
        def currentLevelInfo = hierarchyDataRetriever.collectCurrentLevel(objectType, id, cb)
        if (currentLevelInfo) {
            result = currentLevelInfo[0] as ScreensaverInfo
        }
        def parentLevelInfo = hierarchyDataRetriever.collectParentLevel(objectType, id, cb)
        if (parentLevelInfo) {
            parentLevelInfo = parentLevelInfo[0] as ScreensaverInfo
            if (!result) {
                result = parentLevelInfo
            } else {
                result.parentType = parentLevelInfo.owner
                result.parentUrl = parentLevelInfo.url
            }
        }
        return result
    }
}
