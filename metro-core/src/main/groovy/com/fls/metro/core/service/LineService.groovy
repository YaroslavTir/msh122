package com.fls.metro.core.service

import com.fls.metro.core.data.dao.LineDao
import com.fls.metro.core.data.domain.Line
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.dto.RemoveHierarchyObjectResult
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: APermyakov
 * Date: 05.05.14
 * Time: 16:31
 */
@Service
@Slf4j
class LineService extends HierarchyObjectService<Line> {

    @Autowired
    LineDao lineDao

    @Transactional
    Line get(Long id) {
        log.info('Get line with id {}', id)
        get(ObjectType.LINE, id)
    }

    @Transactional
    Line createLine(Line line) {
        log.info('Create line with name {}', line.name)
        lineDao.create(line)
        log.info('Line with name {}. Id is {}', line.name, line.id)
        return line
    }

    @Transactional
    Line updateLine(Line line) {
        log.info('Update line with id {}', line.id)
        line = setDataForUpdate(ObjectType.LINE, line) { Line it, Line old ->
            if (it.number != old.number || it.color != old.color) {
                it.staticDataUpdateDate = new Date()
            }
            return it
        }
        lineDao.update(line)
        log.info('Line with id {} was successfully updated', line.id)
        return line
    }

    @Transactional
    RemoveHierarchyObjectResult delete(Long id) {
        if (lineDao.stationsCount(id) > 0) {
            log.info('Can\'t remove line with id {}', id)
            return RemoveHierarchyObjectResult.REMOVE_CHILD
        }
        log.info('Delete line with id {}', id)
        prepareDelete(ObjectType.LINE,id)
        lineDao.delete(id)
        log.info('Line with id {} was successfully deleted', id)
        return RemoveHierarchyObjectResult.OK
    }
}
