package com.fls.metro.core.service

import com.fls.metro.core.data.dto.content.ExchangeRate
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.util.concurrent.CopyOnWriteArrayList

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 11:40
 */
@Service
@Slf4j
class ExchangeRateService {
    private List<ExchangeRate> exchangeRateList = new CopyOnWriteArrayList<>()

    @Autowired
    private NotificationService notificationService

    List<ExchangeRate> get() {
        exchangeRateList.collect {
            new ExchangeRate(
                    currencyName: it.currencyName,
                    value: it.value,
                    updateDate: it.updateDate
            )
        }
    }

    synchronized def refreshExchangeRate(List<ExchangeRate> newExchangeRateList) {
        if (!exchangeRateList) {
            exchangeRateList.addAll(newExchangeRateList)
            return
        }

        if (checkRefresh(newExchangeRateList)) {
            log.info('Need to refresh exchange rate data')
            exchangeRateList.clear()
            exchangeRateList.addAll(newExchangeRateList)
            notificationService.notifyAllIms()
            log.info('Refresh exchange rate data is done')
        }
    }

    private boolean checkRefresh(List<ExchangeRate> newExchangeRateList) {
        newExchangeRateList.any { ner ->
            def er = exchangeRateList.find {
                it.currencyName == ner.currencyName
            }
            er?.value != ner.value
        }
    }
}
