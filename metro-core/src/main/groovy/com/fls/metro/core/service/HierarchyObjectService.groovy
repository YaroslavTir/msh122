package com.fls.metro.core.service

import com.fls.metro.core.data.domain.*
import com.fls.metro.core.data.dto.content.StationPlanInfoType
import com.fls.metro.core.data.dto.screensaver.ScreensaverInfo
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired

/**
 * User: NFadin
 * Date: 08.05.14
 * Time: 13:03
 */
@Slf4j
abstract class HierarchyObjectService<T extends HierarchyObject> {

    @Autowired
    protected HierarchyService hierarchyService
    @Autowired
    protected NewsService newsService
    @Autowired
    protected ScreensaverService screensaverService
    @Autowired
    private UpdateDateService updateDateService
    @Autowired
    protected BannerSettingsService bannerSettingsService
    @Autowired
    protected HelpInfoService helpInfoService
    @Autowired
    protected MessageService messageService
    @Autowired
    protected StationPlanSettingsService stationPlanSettingsService


    protected T get(ObjectType objectType, Long id, boolean copySettings, Closure<T> additionalSet) {
        T o = hierarchyService.getWithHierarchyInfo(objectType, id, T)

        o.news = newsService.getLocal(objectType, o.id)
        o.newsUpdateDate = updateDateService.getNewsUpdateDate(objectType, o.id)

        o.helpInfos = helpInfoService.getTotal(objectType, o.id)

        def screensaverInfo = screensaverService.get(objectType, o.id)
        o.screensaverInfo = screensaverInfo ?: new ScreensaverInfo(owner: objectType);

        o.staticDataUpdateDate = updateDateService.getStaticDataUpdateDate(objectType, o.id)

        BannerSettings bs = bannerSettingsService.get(objectType, o.id)
        if (bs == null) {
            o.bannerSettings = new BannerSettings(ownerType: objectType, ownerId: id, own:(objectType==ObjectType.SCHEMA), videos: []);
        } else {
            if (bs.ownerType.equals(objectType) && bs.ownerId.equals(id)) {
                o.bannerSettings = bs;
            } else {
                if(copySettings){
                    o.bannerSettings = BannerSettings.copy(bs);
/*                    o.bannerSettings.ownerType = objectType;
                    o.bannerSettings.ownerId = id;*/
                    o.bannerSettings.own = false;
                }else{
                    o.bannerSettings = new BannerSettings(ownerType: bs.ownerType, ownerId: bs.ownerId, own:false, videos: []);
                }
            }
        }

        MessageSchedule ms = messageService.getMessageSchedule(objectType, o.id)
        if (ms == null) {
            o.messageSchedule = new MessageSchedule(ownerType: objectType, ownerId: id, own:(objectType==ObjectType.SCHEMA));
        } else {
            if (ms.ownerType.equals(objectType) && ms.ownerId.equals(id)) {
                o.messageSchedule = ms;
            } else {
                if(copySettings){
                    o.messageSchedule = MessageSchedule.copy(ms);
/*                    o.messageSchedule.ownerType = objectType;
                    o.messageSchedule.ownerId = id;*/
                    o.messageSchedule.own = false;
                }else{
                    o.messageSchedule = new MessageSchedule(ownerType: ms.ownerType, ownerId: ms.ownerId,own:false, schedule: []);
                }

            }
        }

        if(o instanceof WithStationPlanSettings){
            StationPlanSettings planSettings=stationPlanSettingsService.get(objectType, o.id);
            if (planSettings == null) {
                o.stationPlanSettings = new StationPlanSettings(ownerType: objectType, ownerId: id,own:(objectType==ObjectType.STATION));
            } else {
                if (planSettings.ownerType.equals(objectType) && planSettings.ownerId.equals(id)) {
                    o.stationPlanSettings = planSettings;
                } else {
                    if(copySettings){
                        o.stationPlanSettings = StationPlanSettings.copy(planSettings);
    /*                    o.stationPlanSettings.ownerType = objectType;
                        o.stationPlanSettings.ownerId = id;*/
                        o.stationPlanSettings.own = false;
                        } else{
                            o.stationPlanSettings = new StationPlanSettings(ownerType: planSettings.ownerType, ownerId: planSettings.ownerId,own:false);
                        }
                }
            }
            def stationPlanItems = o?.stationPlanSettings?.items ?: []
            if(stationPlanItems.isEmpty()){
                def itemsToSet = []
                StationPlanInfoType.values().each { type ->
                    itemsToSet << (new StationPlanSettingsItem(type))
                }
                o?.stationPlanSettings?.items = itemsToSet
            }
        }

        additionalSet ? additionalSet.call(o) : o
    }

    protected T get(ObjectType objectType, def id) {
        get(objectType, id, false, null)
    }

    protected T setDataForUpdate(ObjectType objectType, T o, Closure<T> additionalSet) {
        def old = hierarchyService.get(objectType, o.id)
        correctStaticDataUpdateDate(o, old)
        o.updateDate=new Date();
        // news
        o.newsUpdateDate = new Date()
        def oldNews = newsService.getLocalCurrentLevel(objectType, o.id)
        oldNews.each { oldN ->
            if (!o.news.find {
                it.id == oldN.id
            }) {
                newsService.delete(oldN)
            }
        }
        o.news.each {
            it.objectType = objectType
            it.objectId = o.id
            it.updateDate = new Date()
            newsService.save(it)
        }

        if (o.bannerSettings) {
            bannerSettingsService.processBannerSettingsForHierarchyObject(o.bannerSettings);
        }
        if(o.messageSchedule){
            messageService.processMessageScheduleForHierarchyObject(o.messageSchedule);
        }

        if(o instanceof WithStationPlanSettings && o.stationPlanSettings){
            stationPlanSettingsService.processSettingsForHierarchyObject(o.stationPlanSettings);
        }

        helpInfoService.processAndSave(o.helpInfos, objectType, o.id)

        additionalSet ? additionalSet.call(o, old) : o
    }

    protected void prepareDelete(ObjectType objectType, Long id) {
        prepareDelete(objectType, id, null)
    }

    protected void prepareDelete(ObjectType objectType, Long id, Closure additionalDelete) {
        def o = hierarchyService.get(objectType, id)
        if(o.bannerSettings && o.bannerSettings.id){
            bannerSettingsService.delete(o.bannerSettings);
        }
        if(o.messageSchedule && o.messageSchedule.id){
            messageService.deleteMessageSchedule(o.messageSchedule);
        }
        messageService.deleteImmediateMessages(objectType, id);

        if(o.helpInfos){
            helpInfoService.delete(objectType, id);
        }
        if( o instanceof WithStationPlanSettings){
            if(o.stationPlanSettings){
                stationPlanSettingsService.delete(o.stationPlanSettings);
            }
        }
        if (additionalDelete) {
            additionalDelete.call(o)
        }
    }

    private static void correctStaticDataUpdateDate(HierarchyObject newObject, HierarchyObject oldObject) {
        if (newObject.screensaverUrl != oldObject.screensaverUrl) {
            newObject.staticDataUpdateDate = new Date()
        }
    }

    protected T setDataForUpdate(ObjectType objectType, T o) {
        setDataForUpdate(objectType, o, null)
    }
}
