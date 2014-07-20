package com.fls.metro.core.data.filter

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * User: NFadin
 * Date: 23.05.14
 * Time: 17:04
 */
@EqualsAndHashCode
@ToString
abstract class AbstractFilter {
    Integer currentPage
    Integer pageSize
    List<Order> orders = []
}
