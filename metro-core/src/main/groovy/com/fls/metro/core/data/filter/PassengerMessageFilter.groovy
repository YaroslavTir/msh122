package com.fls.metro.core.data.filter

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * User: NFadin
 * Date: 23.05.14
 * Time: 17:03
 */
@EqualsAndHashCode
@ToString
class PassengerMessageFilter extends AbstractFilter {
    String imName
    Date createDate
}
