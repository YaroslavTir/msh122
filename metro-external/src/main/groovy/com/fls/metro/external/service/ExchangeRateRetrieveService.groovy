package com.fls.metro.external.service

import com.fls.metro.core.data.dto.content.ExchangeRate
import com.fls.metro.core.util.Constants
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import java.text.SimpleDateFormat

/**
 * User: NFadin
 * Date: 29.04.14
 * Time: 11:32
 */
@Slf4j
@Service
class ExchangeRateRetrieveService extends RetrieveXmlDataService<List<ExchangeRate>> {

    @Value('${content.exchangeRate.url}')
    private String url
    private List<String> currencies

    @Override
    protected String url() {
        url
    }

    @Override
    protected List<ExchangeRate> retrieve(def valCurs) {
        def updateDate = new SimpleDateFormat(Constants.SHORT_DATE_FORMAT).parse(valCurs['@Date'].text())
        def currenciesData = valCurs.Valute
        currencies.collect { currency ->
            new ExchangeRate(
                    currencyName: currency,
                    value: currenciesData.find { it.CharCode == currency }?.Value?.text(),
                    updateDate: updateDate
            )
        }
    }

    @Value('${content.exchangeRate.currencies}')
    void setCurrencies(String currencies) {
        this.currencies = currencies.split(',').collect { it.trim() }
    }
}
