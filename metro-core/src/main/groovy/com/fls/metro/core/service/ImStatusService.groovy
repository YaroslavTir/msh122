package com.fls.metro.core.service

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 05.05.14
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
@Service
@Slf4j
class ImStatusService {

    @Autowired(required = false)
    ImStatusRequestService imStatusRequestService;

    ImStatus check(String name){
        return imStatusRequestService.get(name);
    }
}
