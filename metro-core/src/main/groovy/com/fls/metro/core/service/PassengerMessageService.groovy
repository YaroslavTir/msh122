package com.fls.metro.core.service

import com.fls.metro.core.data.dao.PassengerMessageDao
import com.fls.metro.core.data.domain.PassengerMessage
import com.fls.metro.core.data.dto.grid.data.PassengerMessageData
import com.fls.metro.core.data.filter.PassengerMessageFilter
import com.fls.metro.core.security.SecurityUtils
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: NFadin
 * Date: 23.05.14
 * Time: 17:03
 */
@Slf4j
@Service
class PassengerMessageService {
    @Autowired
    private PassengerMessageDao passengerMessageDao
    @Autowired
    private ImService imService

    @Transactional
    PassengerMessageData filter(PassengerMessageFilter filter) {
        new PassengerMessageData(
                total: passengerMessageDao.count(filter),
                data: passengerMessageDao.filter(filter)
        )
    }

    @Transactional
    void add(PassengerMessage passengerMessage) {
        def imName = SecurityUtils.username
        log.info('Create new message for im {}', imName)
        passengerMessage.imName = imName
        passengerMessageDao.create(passengerMessage)
        log.info('Message for im {} was successfully created', imName)
    }
}
