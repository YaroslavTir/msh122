package com.fls.metro.external.transformer

import com.fls.metro.core.data.domain.News
import com.fls.metro.core.data.dto.content.Language
import com.fls.metro.external.dto.NewsBunch
import com.sun.syndication.feed.synd.SyndEntry
import com.sun.syndication.feed.synd.SyndFeed
import groovy.util.logging.Slf4j
import org.springframework.integration.Message
import org.springframework.integration.support.MessageBuilder

/**
 * User: NFadin
 * Date: 28.04.14
 * Time: 12:38
 */
@Slf4j
class SyndFeedToNewsBunchTransformer {

    private static final Map<Language, Closure<News>> newsBuilders = [
            (Language.RU) : { new News(title: it.title, updateDate: it.publishedDate)},
            (Language.EN) : { new News(titleEn: it.title, updateDate: it.publishedDate)}
    ]

    private Language lang

    public Message<NewsBunch> transform(Message<SyndFeed> syndFeedMessage) {
        def result = new NewsBunch()
        def news = [] as List<News>
        result.news = news
        SyndFeed syndFeed = syndFeedMessage.payload
        syndFeed.entries.each {
            news.add(newsBuilders[lang].call(it))
        }
        result.buildDate = syndFeed.publishedDate
        MessageBuilder.withPayload(result).build()
    }

    void setLang(Language lang) {
        this.lang = lang
    }
}
