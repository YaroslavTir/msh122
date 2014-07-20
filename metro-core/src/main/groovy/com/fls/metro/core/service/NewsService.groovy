package com.fls.metro.core.service

import com.fls.metro.core.data.dao.NewsDao
import com.fls.metro.core.data.domain.News
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.domain.Settings
import com.fls.metro.core.data.dto.content.ImNews
import com.fls.metro.core.data.dto.content.ImNewsItem
import com.fls.metro.core.data.dto.content.Language
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * User: NFadin
 * Date: 28.04.14
 * Time: 13:19
 */
@Service
@Slf4j
class NewsService {
    @Autowired
    private NotificationService notificationService
    @Autowired
    private NewsDao newsDao
    @Autowired
    private ImService imService
    @Autowired
    private HierarchyDataRetriever hierarchyDataRetriever
    @Autowired
    private SettingsService settingsService
    @Autowired
    private UpdateDateService updateDateService

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private static class ExternalNews {
        Date buildDate
        List<News> news
    }

    Map<Language, ExternalNews> externalNewsData = new HashMap<>()

    Map<Language, Closure<String>> titleRetrievers = [
            (Language.EN) : { it.titleEn },
            (Language.RU) : { it.title }
    ]

    Map<Language, Closure<String>> newsFilters = [
            (Language.EN) : { it.titleEn },
            (Language.RU) : { it.title }
    ]

    void refreshExternalNews(Language lang, Date buildDate, List<News> news) {
        if (!news) { return }
        lock.writeLock().lock()
        try {
            ExternalNews externalNews = externalNewsData[lang]
            if (!externalNews) {
                externalNews = new ExternalNews(
                        buildDate: buildDate,
                        news: news
                )
                externalNewsData.put(lang, externalNews)
                return
            }

            if (!externalNews.buildDate || externalNews.buildDate.before(buildDate)) {
                externalNews.buildDate = buildDate
                externalNews.news = news
                Settings schemaSettings
                if ((schemaSettings = settingsService.schemaSettings()) == null || schemaSettings.showExternalNews) {
                    notificationService.notifyAllIms()
                }

            }
        } finally {
            lock.writeLock().unlock()
        }
    }

    @Transactional
    ImNews getForIm(String imName, Language lang) {
        Date updateDate = externalNewsData[lang]?.buildDate
        def im = imService.getByImName(imName)
        def imNewsUpdateDate = updateDateService.getNewsUpdateDate(ObjectType.IM, im.id)
        if (!updateDate || imNewsUpdateDate.after(updateDate)) {
            updateDate = imNewsUpdateDate
        }
        new ImNews(
                updateDate: updateDate,
                items: get(im.id, ObjectType.IM, true, true, false, lang).grep(newsFilters[lang]).collect {
                    new ImNewsItem(
                            title: titleRetrievers[lang].call(it),
                            updateDate: it.updateDate
                    )
                }
        )
    }

    @Transactional
    List<News> getLocal(ObjectType objectType, def id) {
        get(id, objectType, true, false, false, null)
    }

    List<News> getLocalCurrentLevel(ObjectType objectType, def id) {
        get(id, objectType, true, false, true, null)
    }

    @Transactional
    void save(News news) {
        if (news.id) {
            log.info('Update news with id {}', news.id)
            newsDao.update(news)
        } else {
            log.info('Create new news for object with type {} and id {}', news.objectType, news.objectId)
            newsDao.create(news)
        }
    }

    private List<News> get(def id, ObjectType objectType, boolean local, boolean external, boolean currentLevel, Language lang) {
        lock.readLock().lock()
        def result = []
        try {
            Settings schemaSettings
            if (external && ((schemaSettings = settingsService.schemaSettings()) == null || schemaSettings.showExternalNews)) {
                if (externalNewsData[lang]) {
                    result.addAll(externalNewsData[lang].news)
                }
            }
            if (local) {
                if (currentLevel) {
                    result.addAll(hierarchyDataRetriever.collectCurrentLevel(objectType, id) { ObjectType ot, oid, o ->
                        newsDao.findAll(ot, oid)
                    })
                } else {
                    result.addAll(hierarchyDataRetriever.collectAllLevels(objectType, id) { ObjectType ot, oid, o ->
                        newsDao.findAll(ot, oid)
                    })
                }
            }
        } finally {
            lock.readLock().unlock()
        }

        result
    }

    def delete(News news) {
        newsDao.delete(news.id)
    }
}
