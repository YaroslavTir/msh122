package com.fls.metro.external.dto

import com.fls.metro.core.data.domain.News
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * User: NFadin
 * Date: 28.04.14
 * Time: 13:21
 */
@EqualsAndHashCode
@ToString
class NewsBunch {
    Date buildDate
    List<News> news
}
