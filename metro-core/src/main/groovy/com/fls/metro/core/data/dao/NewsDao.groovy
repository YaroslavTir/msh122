package com.fls.metro.core.data.dao

import com.fls.metro.core.data.domain.News
import com.fls.metro.core.data.domain.ObjectType
import org.springframework.stereotype.Repository

/**
 * User: NFadin
 * Date: 06.05.14
 * Time: 10:38
 */
@Repository
class NewsDao extends AbstractDao<News> {
    List<News> findAll(ObjectType type, def id) {
        executeSelectRows objectType: type, objectId: id
    }
}
