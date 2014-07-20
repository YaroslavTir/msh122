package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.TimestampField

/**
 * User: NFadin
 * Date: 08.05.14
 * Time: 13:06
 */
public interface WithNews {
    List<News> getNews()
    void setNews(List<News> news)
    Date getNewsUpdateDate()
    void setNewsUpdateDate(Date newsUpdateDate)
}