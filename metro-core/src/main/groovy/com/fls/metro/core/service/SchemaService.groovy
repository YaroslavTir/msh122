package com.fls.metro.core.service

import com.fls.metro.core.data.dao.SchemaDao
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.domain.Schema
import com.fls.metro.core.data.domain.Settings
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User: APermyakov
 * Date: 28.04.14
 * Time: 15:01
 */
@Slf4j
@Service
class SchemaService extends HierarchyObjectService<Schema> {

    @Autowired
    private SettingsService settingsService

    @Autowired
    private SchemaDao schemaDao

    @Transactional
    Schema getSchemaDTO() {
        def schema = schemaDao.getSchema()
        get(ObjectType.SCHEMA, schema.id, false) {
            it.settings = settingsService.get(ObjectType.SCHEMA, it.id)
            if (!it.settings) {
                it.settings = new Settings(ownerType: ObjectType.SCHEMA, ownerId: it.id);
            }
            it.lines = schema.lines
            it
        }
    }

    @Transactional
    Schema create(Schema schema) {
        schemaDao.create(schema)
    }

    @Transactional
    boolean hasSchema() {
        schemaDao.hasSchema()
    }

    @Transactional
    Schema update(Schema schema) {
        log.info('Update schema with id {}', schema.id)
        schemaDao.update(schema = setDataForUpdate(ObjectType.SCHEMA, schema) { Schema it, Schema oldSchema ->
            if (it.settings) {
                settingsService.processSettingsForHierarchyObject(it.settings)
            }
            return it
        })
        log.info('Schema with id {} was successfully updated', schema.id)
        return schema
    }

    @Transactional
    void delete(Long id) {
        prepareDelete(ObjectType.IM, id) { Schema it ->
            if (it.settings && it.settings.id) {
                settingsService.delete(it.settings)
            }
        }
    }
}
