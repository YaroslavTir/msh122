package com.fls.metro.core.data.dao

import com.fls.metro.core.data.domain.Line
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 14:52
 */
@Slf4j
@Repository
class LineDao extends HierarchyDao<Line> {

    Integer stationsCount(Long id) {
        sql.rows('select count(id) as cnt from station where line_id = :id', [id: id])[0].cnt
    }

    Line findByNumber(Integer number) {
        executeSelect number : number
    }
}
