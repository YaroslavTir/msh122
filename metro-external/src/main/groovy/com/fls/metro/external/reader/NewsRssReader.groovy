package com.fls.metro.external.reader

import com.fls.metro.core.config.PropertiesHolder
import com.sun.syndication.feed.synd.SyndFeed
import com.sun.syndication.fetcher.FeedFetcher
import com.sun.syndication.fetcher.FetcherException
import com.sun.syndication.fetcher.FetcherListener
import com.sun.syndication.fetcher.impl.FeedFetcherCache
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher
import com.sun.syndication.io.FeedException
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.integration.Message
import org.springframework.integration.support.MessageBuilder

import javax.annotation.PostConstruct

/**
 * User: NFadin
 * Date: 28.04.14
 * Time: 11:52
 */
@Slf4j
class NewsRssReader {

    private FeedFetcherCache feedInfoCache = HashMapFeedInfoCache.instance
    private FeedFetcher feedFetcher = new HttpURLFeedFetcher(feedInfoCache)
    private FetcherListener fetcherListener
    private URL url

    @PostConstruct
    void init() {
        if (fetcherListener) {
            feedFetcher.addFetcherEventListener(fetcherListener)
        }
    }

    public Message<SyndFeed> receive() {
        def feed = obtainFeedItem()
        return MessageBuilder.withPayload(feed).build()
    }

    private SyndFeed obtainFeedItem() {
        SyndFeed feed = null
        try {
            feed = feedFetcher.retrieveFeed(url)
        } catch (IOException e) {
            log.error('IO Problem while retrieving feed', e);
        } catch (FeedException e) {
            log.error('Feed Problem while retrieving feed', e);
        } catch (FetcherException e) {
            log.error('Fetcher Problem while retrieving feed', e);
        }
        feed
    }

    void setUrl(String url) {
        this.url = new URL(url)
    }

    void setFetcherListener(FetcherListener fetcherListener) {
        this.fetcherListener = fetcherListener
    }
}
