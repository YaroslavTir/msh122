package com.fls.metro.core.service
import com.fls.metro.core.data.dao.StationPlanSettingsDao
import com.fls.metro.core.data.domain.HierarchyObject
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.domain.StationPlanSettings
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
class StationPlanSettingsService {

    @Autowired
    private HierarchyDataRetriever hierarchyDataRetriever

    @Autowired
    StationPlanSettingsDao planSettingsDao

    @Transactional
    StationPlanSettings create(StationPlanSettings settings){
        settings.updateDate=new Date();
        planSettingsDao.create(settings);
    }

    @Transactional
    void update(StationPlanSettings settings){
        settings.updateDate=new Date();
        planSettingsDao.update(settings);
    }

    @Transactional
    void delete(StationPlanSettings settings){
        planSettingsDao.delete(settings.id);
    }

    @Transactional
    StationPlanSettings get(ObjectType objectType, def id) {
        def result = hierarchyDataRetriever.collectAllLevelsFirstOccurrence(objectType, id) { ObjectType ot, oid, HierarchyObject o ->
            StationPlanSettings settings=planSettingsDao.get(ot, oid);
            if (settings) {
                return settings;
            }
            return null
        }
        if (result) {
            return result[0] as StationPlanSettings
        }
        return null
    }

    @Transactional
    void delete(ObjectType type, Long ownerId){
        planSettingsDao.deleteFor(type, ownerId);
    }

    @Transactional
    void processSettingsForHierarchyObject(StationPlanSettings settings){
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
}
