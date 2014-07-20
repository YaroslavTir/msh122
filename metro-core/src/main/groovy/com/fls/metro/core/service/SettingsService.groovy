package com.fls.metro.core.service

import com.fls.metro.core.data.dao.SchemaDao
import com.fls.metro.core.data.dao.SettingsDao
import com.fls.metro.core.data.domain.HierarchyObject
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.domain.Settings
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 05.05.14
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
@Service
@Slf4j
class SettingsService{

    @Autowired
    private HierarchyDataRetriever hierarchyDataRetriever

    @Autowired
    SettingsDao settingsDao
    @Autowired
    SchemaDao schemaDao

    @Transactional
    Settings create(Settings settings){
        settings.updateDate=new Date();
        settingsDao.create(settings);
    }

    @Transactional
    void update(Settings settings){
        settings.updateDate=new Date();
        settingsDao.update(settings);
    }

    @Transactional
    void delete(Settings settings){
        settingsDao.delete(settings.id);
    }

    @Transactional
    Settings get(ObjectType objectType, def id) {
        def result = hierarchyDataRetriever.collectAllLevelsFirstOccurrence(objectType, id) { ObjectType ot, oid, HierarchyObject o ->
            Settings settings=settingsDao.get(ot, oid);
            if (settings) {
                return settings;
            }
            return null
        }
        if (result) {
            return result[0] as Settings
        }
        return null
    }

    @Transactional
    void delete(ObjectType type, Long ownerId){
        settingsDao.deleteFor(type, ownerId);
    }

    @Transactional
    void processSettingsForHierarchyObject(Settings settings){
        if(settings.id!=null){
            if(settings.own){
                update(settings)
            }else{
                delete(settings)
            }
        }else{
            if(settings.own){
                create(settings)
            }
        }
    }

    @Transactional
    Settings schemaSettings() {
        get(ObjectType.SCHEMA, schemaDao.schemaId())
    }
}
