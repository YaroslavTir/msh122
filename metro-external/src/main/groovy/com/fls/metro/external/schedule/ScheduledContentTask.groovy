package com.fls.metro.external.schedule

import com.fls.metro.core.service.ScheduleService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 05.05.14
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
class ScheduledContentTask {

    @Autowired
    ScheduleService scheduledContentService;

    void run(){
        scheduledContentService.refreshInfoTextOnIms();
    }

}
