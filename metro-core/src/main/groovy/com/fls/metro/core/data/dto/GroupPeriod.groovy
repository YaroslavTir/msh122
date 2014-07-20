package com.fls.metro.core.data.dto

/**
 * User: NFadin
 * Date: 09.06.2014
 * Time: 13:53
 */
public enum GroupPeriod {
    WEEK('week'), MONTH('month'), YEAR('year'), DAY('doy')
    String sqlPart

    GroupPeriod(String sqlPart) {
        this.sqlPart = sqlPart
    }
}