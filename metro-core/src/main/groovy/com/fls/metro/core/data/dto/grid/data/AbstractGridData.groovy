package com.fls.metro.core.data.dto.grid.data

/**
 * User: NFadin
 * Date: 23.05.14
 * Time: 17:05
 */
abstract class AbstractGridData<T> {
    Integer total
    Collection<T> data
}
