package com.fls.metro.external.job

import com.fls.metro.core.job.AbstractJob
import com.fls.metro.core.job.Job
import com.fls.metro.core.job.JobPriority
import com.fls.metro.core.service.WeatherService
import com.fls.metro.external.schedule.RefreshDataTask
import com.fls.metro.external.schedule.WeatherTask
import com.fls.metro.external.service.WeatherRetrieveService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 11:11
 */
@Slf4j
@Component
class LoadExternalDataJob extends AbstractJob {

    @Autowired(required = false)
    private List<RefreshDataTask<?>> refreshDataTaskList

    @Override
    void runJob() throws Exception {
        refreshDataTaskList.each {
            it.run()
        }
    }
}
