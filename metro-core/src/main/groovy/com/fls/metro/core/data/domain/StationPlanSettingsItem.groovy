package com.fls.metro.core.data.domain

import com.fls.metro.core.data.dto.content.StationPlanInfoType
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 04.06.14
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
@EqualsAndHashCode
@ToString
@TupleConstructor
class StationPlanSettingsItem {
    StationPlanInfoType type
    String rusText
    String engText
}
