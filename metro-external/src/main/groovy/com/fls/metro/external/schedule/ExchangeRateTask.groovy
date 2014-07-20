package com.fls.metro.external.schedule

import com.fls.metro.core.data.dto.content.ExchangeRate
import com.fls.metro.core.service.ExchangeRateService
import com.fls.metro.external.service.ExchangeRateRetrieveService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 11:39
 */
@Slf4j
class ExchangeRateTask extends RefreshDataTask<List<ExchangeRate>>{
    @Autowired
    private ExchangeRateService exchangeRateService
    @Autowired
    private ExchangeRateRetrieveService exchangeRateRetrieveService

    @Override
    List<ExchangeRate> doRetrieve() {
        return exchangeRateRetrieveService.retrieve()
    }

    @Override
    void doRefresh(List<ExchangeRate> exchangeRateList) {
        exchangeRateService.refreshExchangeRate(exchangeRateList)
    }
}
