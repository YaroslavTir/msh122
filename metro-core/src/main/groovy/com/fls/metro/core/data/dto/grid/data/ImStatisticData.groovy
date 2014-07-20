package com.fls.metro.core.data.dto.grid.data

import com.fls.metro.core.data.dao.statistic.ImStatisticDao
import com.fls.metro.core.data.domain.statistic.ImStatistic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * User: NFadin
 * Date: 02.06.2014
 * Time: 16:07
 */
@EqualsAndHashCode
@ToString
class ImStatisticData  {
    List<ImStatisticDao.GroupStatisticData> data
}
