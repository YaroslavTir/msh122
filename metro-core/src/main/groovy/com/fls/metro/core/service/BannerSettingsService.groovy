package com.fls.metro.core.service
import com.fls.metro.core.data.dao.BannerSettingsDao
import com.fls.metro.core.data.dao.BannerVideoDao
import com.fls.metro.core.data.domain.*
import com.fls.metro.core.data.dto.content.ContentItem
import com.fls.metro.core.data.dto.schedule.ScheduleType
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
class BannerSettingsService{

    @Autowired
    private HierarchyDataRetriever hierarchyDataRetriever

    @Autowired
    BannerSettingsDao bannerSettingsDao

    @Autowired
    BannerVideoDao bannerVideoDao;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MediaContentService mediaContentService;

    private final scheduleType=ScheduleType.BANNER;


    @Transactional
    BannerSettings create(BannerSettings settings){
        settings.updateDate=new Date();
        BannerSettings created=bannerSettingsDao.create(settings);
        settings.setId(created.id);
        if(settings.videos!=null){
            int i=0;
            for(ContentItem video:settings.videos){
                BannerVideo bv=new BannerVideo(number: i, bannerSettingsId: settings.id);
                if(video.content!=null){
                    bv.fileUrl=video.content.fileUrl;
                    bannerVideoDao.create(bv);
                }
                i++;
            }
        }
        if(settings.schedule!=null){
            scheduleService.create(settings.schedule,settings.ownerType, settings.ownerId, scheduleType);
        }
        return settings;
    }

    @Transactional
    void update(BannerSettings settings){
        settings.updateDate=new Date();
        deleteVideoList(settings.id);
        if(settings.videos!=null){
            int i=0;
            for(ContentItem video:settings.videos){
                BannerVideo bv=new BannerVideo(number: i, bannerSettingsId: settings.id);
                if(video.content!=null){
                    bv.fileUrl=video.content.fileUrl;
                    bannerVideoDao.create(bv);
                }
                i++;
            }
        }

        if(settings.schedule!=null){
            scheduleService.updateScheduleFor(settings.schedule,settings.ownerType, settings.ownerId, scheduleType);
        }else{
            scheduleService.cleanScheduleFor(settings.ownerType, settings.ownerId, scheduleType);
        }
        bannerSettingsDao.update(settings);
    }

    @Transactional
    void deleteVideoList(Long settingsId){
        bannerVideoDao.deleteFor(settingsId);
    }

    @Transactional
    BannerSettings get(ObjectType type, Long ownerId){
        def result = hierarchyDataRetriever.collectAllLevelsFirstOccurrence(type, ownerId) { ObjectType ot, oid, HierarchyObject o ->
            BannerSettings bs=bannerSettingsDao.get(ot, oid);
            if (bs) {
                bs.videos=bannerVideoDao.listVideo(bs.id);
                bs.schedule=scheduleService.getScheduleFor(ot, oid,scheduleType);
                return bs;
            }
            return null
        }
        if (result) {
            return result[0] as BannerSettings
        }
        return null

    }

    @Transactional
    void delete(ObjectType type, Long ownerId){
        bannerSettingsDao.deleteFor(type, ownerId);
    }

    @Transactional
    void delete(BannerSettings bs){
        if(bs.schedule){
            scheduleService.cleanScheduleFor(bs.ownerType, bs.ownerId, scheduleType);
        }
        bannerSettingsDao.delete(bs.id);
    }

    @Transactional
    void processBannerSettingsForHierarchyObject(BannerSettings bannerSettings){
        if(bannerSettings.id!=null){
            if(bannerSettings.own){
                update(bannerSettings)
            }else{
                delete(bannerSettings)
            }
        }else{
            if(bannerSettings.own){
                create(bannerSettings)
            }
        }
    }

}
