package com.fls.metro.core.data.dto.content

import groovy.transform.AutoClone
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import static groovy.transform.AutoCloneStyle.COPY_CONSTRUCTOR

/**
 * User: NFadin
 * Date: 28.04.14
 * Time: 16:21
 */
@EqualsAndHashCode
@ToString
class Weather extends UpdatableItem {
    String temperature
    String image
}
