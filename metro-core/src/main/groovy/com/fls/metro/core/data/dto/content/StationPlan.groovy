package com.fls.metro.core.data.dto.content

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
/**
 * User: NFadin
 * Date: 16.05.14
 * Time: 12:23
 */
@EqualsAndHashCode
@ToString
class StationPlan {
    String planLink
    List<StationPlanInfoItem> items
    String info
    Date updateDate
}
