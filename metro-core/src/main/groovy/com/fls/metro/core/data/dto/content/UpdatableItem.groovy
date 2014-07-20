package com.fls.metro.core.data.dto.content

import com.fls.metro.core.annotation.Ignore
import com.fls.metro.core.data.domain.WithId
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

/**
 * User: NFadin
 * Date: 24.04.14
 * Time: 10:03
 */
@EqualsAndHashCode
@ToString
abstract class UpdatableItem {
    @Ignore Date updateDate
}
