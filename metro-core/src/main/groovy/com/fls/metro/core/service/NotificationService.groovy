package com.fls.metro.core.service

import com.fls.metro.core.data.dao.ImDao
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CopyOnWriteArraySet

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 11:43
 */
@Slf4j
@Service
class NotificationService {
    @Autowired
    private ImService imService
    @Autowired
    private ImDao imDao
    @Autowired(required = false)
    private List<ImNotificationServiceListener> listeners

    private Set<String> imNamesToNotify = new HashSet<>()

    @Transactional
    void notifyIm(String imName) {
        notifyIms([imName])
    }

    @Transactional
    void notifyLobby(Long id) {
        notifyIms(imDao.findImsByLobby(id))
    }

    @Transactional
    void notifyStation(Long id) {
        notifyIms(imDao.findImsByStation(id))
    }

    @Transactional
    void notifyLine(Long id) {
        notifyIms(imDao.findImsByLine(id))
    }

    @Transactional
    void notifySchema(Long id) {
        notifyIms(imDao.findImsBySchema(id))
    }

    @Transactional
    void notifyAllIms() {
        notifyIms(imService.imNamesList())
    }

    synchronized void notifyIms(List<String> imNames) {
        imNamesToNotify.addAll(imNames)
    }

    void runNotification() {
        def ims = []
        synchronized (this) {
            ims = new ArrayList(imNamesToNotify)
            imNamesToNotify.clear()
        }
        if (ims) {
            log.info("Run notification for ims: {}", ims)
            listeners.each { it.onNotification(ims) }
        }
    }
}
