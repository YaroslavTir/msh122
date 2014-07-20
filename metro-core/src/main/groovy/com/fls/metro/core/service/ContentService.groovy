package com.fls.metro.core.service
import com.fls.metro.core.data.domain.*
import com.fls.metro.core.data.dto.content.*
import com.fls.metro.core.data.dto.content.help.HelpMenu
import com.fls.metro.core.data.dto.content.media.MediaContent
import com.fls.metro.core.data.dto.schedule.ScheduleType
import com.fls.metro.core.data.dto.schedule.ScheduledContent
import com.fls.metro.core.data.dto.schedule.ScheduledContentWithLocalTime
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
/**
 * User: NFadin
 * Date: 23.04.14
 * Time: 10:10
 */
@Slf4j
@Service
class ContentService {

    @Autowired
    private NewsService newsService
    @Autowired
    private WeatherService weatherService
    @Autowired
    private ExchangeRateService exchangeRateService
    @Autowired
    private ImService imService
    @Autowired
    BannerSettingsService bannerSettingsService
    @Autowired
    ScheduleService scheduleService
    @Autowired
    MediaContentService mediaContentService
    @Autowired
    MessageService messageService
    @Autowired
    SettingsService settingsService
    @Autowired
    HelpInfoService helpInfoService
    @Autowired
    StationPlanSettingsService stationPlanSettingsService

    @Value('${common.lineDefaultColor}')
    private String lineDefaultColor


    // todo this is a dummy partly
    @Transactional
    List<Content> get(String imName) {
        Im im = imService.getByImNameWithHierarchyData(imName)
        Line line = im.hierarchyInfo[ObjectType.LINE] as Line
        Station station = im.hierarchyInfo[ObjectType.STATION] as Station
        Date currentDate = new Date();
        Settings settings = settingsService.get(ObjectType.IM, im.id);
        Map<Language, HelpMenu> helpMenuMap = helpInfoService.getInfoContent(ObjectType.IM, im.id)

        return [
                new Content(
                        Language.EN,
                        new StaticContent(
                                updateDate: im.staticDataUpdateDate,
                                stationName: station.enname,
                                lineName:  line.enname,
                                inactiveDefault: im.screensaverInfo?.url,
                                position: new Position(
                                        lat: im.latitude,
                                        lon: im.longitude
                                ),
                                color: line.color ?: lineDefaultColor,
                                number: line.number,
                                emergencyNumber: settings?settings.emergencyNumber:null,
                                screensaverEnabled: (settings!=null && settings.screensaverTimeoutSec!=null),
                                screensaverTimeoutSec: (settings&&settings.screensaverTimeoutSec)?settings.screensaverTimeoutSec:0
                        ),
                        getStationPlan(Language.EN,im),
                        newsService.getForIm(imName, Language.EN),
                        weatherService.get(Language.EN),
                        exchangeRateService.get(),
                        helpMenuMap.get(Language.EN),
                        getCurrentMessage(im, currentDate),
                        getBannerVideoList(im),
                        getCurrentBannerMessage(im, currentDate),
                        getContentSettings(settings)
                ),
                new Content(
                        Language.RU,
                        new StaticContent(
                                updateDate: im.staticDataUpdateDate,
                                stationName: im.hierarchyInfo[ObjectType.STATION].name,
                                lineName: line.name,
                                inactiveDefault: im.screensaverInfo?.url,
                                position: new Position(
                                        lat: im.latitude,
                                        lon: im.longitude
                                ),
                                color: line.color ?: lineDefaultColor,
                                number: line.number,
                                emergencyNumber: settings?settings.emergencyNumber:null,
                                screensaverEnabled: (settings!=null && settings.screensaverTimeoutSec!=null),
                                screensaverTimeoutSec: (settings&&settings.screensaverTimeoutSec)?settings.screensaverTimeoutSec:0
                        ),
                        getStationPlan(Language.RU, im),
                        newsService.getForIm(imName, Language.RU),
                        weatherService.get(Language.RU),
                        exchangeRateService.get(),
                        helpMenuMap.get(Language.RU),
                        getCurrentMessage(im, currentDate),
                        getBannerVideoList(im),
                        getCurrentBannerMessage(im, currentDate),
                        getContentSettings(settings)
                )
        ]
    }

    public StationPlan getStationPlan(Language lang, Im im){
        StationPlanSettings sps=stationPlanSettingsService.get(ObjectType.IM, im.id);
        if(sps==null){
            return new StationPlan(planLink: null, info:null, items: [], updateDate: new Date())
        }
        StationPlan plan=new StationPlan();
        List<StationPlanInfoItem> items=[];
        plan.planLink=(lang==Language.RU)?sps.rusUrl:sps.engUrl;
        if(sps.items){
            for(StationPlanSettingsItem item:sps.items){
                if(item.type==StationPlanInfoType.INFO){
                    plan.info=(lang==Language.RU)?item.rusText:item.engText;
                }else{
                    def text=(lang==Language.RU)?item.rusText:item.engText;
                    if(text){
                        items.add(new StationPlanInfoItem(type:item.type, text: text));
                    }
                }
            }
        }
        plan.items=items;
        Date recentDate=getMostRecentDate(sps.updateDate, im.updateDate)
        plan.updateDate=recentDate;
        plan
    }

    public ContentSettings getContentSettings(Settings settings){
        if(settings==null){
            return new ContentSettings();
        }
        new ContentSettings(
                updateDate: settings.updateDate,
                showWeather: settings.showWeather, showTime: settings.showTime, showCurrency: settings.showCurrency,
                showLanguages: settings.showLanguages, showExternalNews: settings.showExternalNews)
    }

    public com.fls.metro.core.data.dto.content.BannerVideo getBannerVideoList(Im im){
        BannerSettings bs=bannerSettingsService.get(ObjectType.IM, im.id);
        if(bs){
            Date recentDate=getMostRecentDate(bs.updateDate, im.updateDate);
            com.fls.metro.core.data.dto.content.BannerVideo video=new com.fls.metro.core.data.dto.content.BannerVideo(updateDate: recentDate, defaultImageUrl: bs.imageUrl);
            List<MediaContent> videos=[];
            for(ContentItem ci:bs.videos){
                videos.add(ci.contentExt);
            }
            video.content=videos;
            video.defaultImageUrl=bs.imageUrl
            return video;
        }else{
            return new com.fls.metro.core.data.dto.content.BannerVideo(updateDate: new Date(), content:[]);
        }
    }

    public BannerMessage getCurrentBannerMessage(Im im, Date date){
       ScheduledContent currContent=scheduleService.getScheduledContent(im.id, ScheduleType.BANNER, date);
       if(currContent!=null){
           DateTime now=DateTime.now();
           LocalTime startTime=ScheduledContentWithLocalTime.toLocalTime(currContent.startTime);
           DateTime curr=new DateTime().withDate(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth()).withTime(startTime.getHourOfDay(), startTime.getMinuteOfHour(), 0,0);
           Date recentDate=curr.toDate();
           if(im.bannerSettings){
               recentDate=getMostRecentDate(recentDate,im.bannerSettings.updateDate);
           }else{
               recentDate=getMostRecentDate(recentDate,im.updateDate);
           }
           BannerMessage bm=new BannerMessage(updateDate: recentDate);
           if(currContent.contentExt!=null){
               bm.content=currContent.contentExt;
           }else if(currContent.content!=null){
               bm.content=mediaContentService.convertMediaContent(currContent.content, Area.BANNER);
           }else{
               return new BannerMessage(updateDate: date, content:null);
           }
           return bm;
       }else{
           return new BannerMessage(updateDate: date, content:null);
       }
    }

    public static getMostRecentDate(Date date1, Date date2){

        if(date1){
            if(date2){
                return date1.after(date2)?date1:date2;
            }else{
                return date1;
            }
        }
        return date2;
    }

    public Message getCurrentMessage(Im im, Date date){
        ImmediateMessage imm=messageService.getPlayingMessage(ObjectType.IM, im.id);
        if(imm!=null){
            Message message=new Message(type: MessageType.IMM, updateDate: imm.startDate);
            if(imm.contentExt!=null){
                message.content=imm.contentExt;
            }else if(imm.content!=null){
                message.content=mediaContentService.convertMediaContent(imm.content, Area.SCREEN);
            }
            return message;
        }else{
            ScheduledContent currContent=scheduleService.getScheduledContent(im.id, ScheduleType.MESSAGE, date);
            if(currContent!=null){
                DateTime now=DateTime.now();
                LocalTime startTime=ScheduledContentWithLocalTime.toLocalTime(currContent.startTime);
                DateTime curr=new DateTime().withDate(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth()).withTime(startTime.getHourOfDay(), startTime.getMinuteOfHour(), 0,0);\
                Date recentDate=curr.toDate();
                if(im.messageSchedule){
                    recentDate=getMostRecentDate(recentDate,im.messageSchedule.updateDate);
                }else{
                    recentDate=getMostRecentDate(recentDate,im.updateDate);
                }
                Message message=new Message(type: MessageType.SCH, updateDate: recentDate);
                if(currContent.contentExt!=null){
                    message.content=currContent.contentExt;
                }else if(currContent.content!=null){
                    message.content=mediaContentService.convertMediaContent(currContent.content, Area.SCREEN);
                }else{
                    return new Message(updateDate: date, type:null, content:null);
                }
                return message;
            }else{
                return new Message(updateDate: date, type:null, content:null);
            }
        }
    }



}
