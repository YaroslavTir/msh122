package com.fls.metro.api.dto

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

/**
 * User: NFadin
 * Date: 12.05.14
 * Time: 13:10
 */
@EqualsAndHashCode
@ToString
@TupleConstructor
class Notification {
    Map<String, String> errors = [:]
}
