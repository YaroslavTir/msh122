package com.fls.metro.core.data.filter

import com.fls.metro.core.data.dto.GroupPeriod
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * User: NFadin
 * Date: 23.05.14
 * Time: 17:03
 */
@EqualsAndHashCode
@ToString
class ImStatisticFilter {
    String imName
    Date periodDateFrom
    Date periodDateTo
    GroupPeriod groupPeriod = GroupPeriod.DAY

    boolean isEmpty() {
        return !imName && !periodDateFrom && !periodDateTo
    }
}
