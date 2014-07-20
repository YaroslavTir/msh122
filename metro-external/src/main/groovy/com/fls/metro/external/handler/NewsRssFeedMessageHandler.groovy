package com.fls.metro.external.handler

import com.fls.metro.core.data.dto.content.Language
import com.fls.metro.core.service.NewsService
import com.fls.metro.external.dto.NewsBunch
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.integration.Message
import org.springframework.integration.MessagingException
import org.springframework.integration.core.MessageHandler

/**
 * User: NFadin
 * Date: 28.04.14
 * Time: 11:49
 */
@Slf4j
class NewsRssFeedMessageHandler implements MessageHandler {

    @Autowired
    private NewsService newsService

    private Language lang = Language.RU

    @Override
    void handleMessage(Message<?> message) throws MessagingException {
        def payload = message.payload
        if (!payload instanceof NewsBunch) {
            log.warn('Wrong payload', payload)
        }
        NewsBunch bunch = (NewsBunch) payload
        newsService.refreshExternalNews(lang, bunch.buildDate, bunch.news)
    }

    void setLang(Language lang) {
        this.lang = lang
    }
}
