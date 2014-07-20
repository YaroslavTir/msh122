package com.fls.metro.api.dto.admin

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

/**
 * User: NFadin
 * Date: 18.04.14
 * Time: 11:19
 */
@EqualsAndHashCode
@ToString
@TupleConstructor
class UserAuth {
    String username
    List<String> roles
}
