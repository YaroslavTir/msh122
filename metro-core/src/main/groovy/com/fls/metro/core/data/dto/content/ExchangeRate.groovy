package com.fls.metro.core.data.dto.content

import groovy.transform.AutoClone
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import static groovy.transform.AutoCloneStyle.COPY_CONSTRUCTOR

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 17:18
 */
@EqualsAndHashCode
@ToString
class ExchangeRate extends UpdatableItem {
    String currencyName
    String value
}