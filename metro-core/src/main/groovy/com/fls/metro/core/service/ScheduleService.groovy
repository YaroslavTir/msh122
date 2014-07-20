package com.fls.metro.core.service
import com.fls.metro.core.data.dao.*
import com.fls.metro.core.data.domain.*
import com.fls.metro.core.data.dto.content.Area
import com.fls.metro.core.data.dto.schedule.Schedule
import com.fls.metro.core.data.dto.schedule.ScheduleType
import com.fls.metro.core.data.dto.schedule.ScheduledContent
import com.fls.metro.core.data.dto.schedule.ScheduledContentWithLocalTime
import com.fls.metro.core.exception.InvalidScheduleException
import com.fls.metro.core.exception.ScheduleCreationException
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Slf4j
@Service
class ScheduleService extends TraverseHierarchyService<ScheduledContent>{

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private ScheduledContentPeriodDao scheduledContentPeriodDao;

    @Autowired
    private ImDao imDao;

    @Autowired
    private NotificationService imNotificationService;

    @Autowired
    private MediaContentService mediaContentService;

    final LocalTime midnight=LocalTime.parse("00:00");
    final LocalTime minuteToMidnight=LocalTime.parse("23:59");



    private List<ScheduledContent> checkScheduleContent(List<ScheduledContent> schedule) throws InvalidScheduleException, ScheduleCreationException{
        if(schedule==null || schedule.isEmpty()){
            return schedule;
        }
        List<ScheduledContentWithLocalTime> sortedSchedule=[];
        for(ScheduledContent c:schedule){
            ScheduledContentWithLocalTime cit=new ScheduledContentWithLocalTime(
                    startTime: c.startTime, endTime: c.endTime, content: c.content, contentExt: c.contentExt);
            cit.localStartTime=ScheduledContentWithLocalTime.toLocalTime(c.startTime);
            cit.localEndTime=ScheduledContentWithLocalTime.toLocalTime(c.endTime);
            isValid(cit);
            sortedSchedule.add(cit);
        }

        if(schedule.size()>1){
            sortedSchedule=sortedSchedule.sort(new Comparator<ScheduledContentWithLocalTime>() {
                @Override
                int compare(ScheduledContentWithLocalTime o1, ScheduledContentWithLocalTime o2) {
                    if(o1==null && o2==null){
                        return 0;
                    }
                    if(o1==null){
                        return -1;
                    }
                    if(o2==null){
                        return 1;
                    }
                    return o1.localStartTime.compareTo(o2.localStartTime);
                }
            });
            // fill in blanks
            List<ScheduledContent> result=[];

            ScheduledContent first=sortedSchedule[0];
            if(first.localStartTime.compareTo(midnight)>0){
                result.add(new ScheduledContent(startTime: "00:00", endTime: first.startTime));
            }
            for(int i=0; i<sortedSchedule.size()-1; i++){
                ScheduledContent current=sortedSchedule[i];
                int diff=sortedSchedule[i+1].localStartTime.compareTo(current.localEndTime);
                if(diff<0){
                    throw new InvalidScheduleException();
                }else if(diff>0){
                    result.add(current);
                    result.add(new ScheduledContent(startTime: current.endTime, endTime: sortedSchedule[i+1].startTime))
                }else { //diff=0
                    result.add(schedule[i]);
                }
            }
            ScheduledContent last=sortedSchedule[sortedSchedule.size()-1];
            result.add(last);
            if(last.localEndTime.compareTo(minuteToMidnight)<0){
                result.add(new ScheduledContent(startTime: last.endTime, endTime: "23:59"));
            }
            return result;
        }else{
            // fill blanks in schedule
            List<ScheduledContent> result=[];
            ScheduledContentWithLocalTime t=sortedSchedule[0];
            if(t.localStartTime.compareTo(midnight)>0){
                result.add(new ScheduledContent(startTime: 0, endTime: t.startTime));
            }
            result.add(t);
            if(t.localEndTime.compareTo(minuteToMidnight)<0){
                result.add(new ScheduledContent(startTime: t.endTime, endTime: "23:59"));
            }
            return result;
        }

    }

    private List<Schedule> checkSchedule(List<Schedule> schedule) throws InvalidScheduleException, ScheduleCreationException{
        if(schedule==null || schedule.isEmpty()){
            return schedule;
        }
        if(schedule.size()>1){
            List<Schedule> sortedSchedule=schedule.sort(new Comparator<Schedule>() {
                @Override
                int compare(Schedule o1, Schedule o2) {
                    if(o1==null && o2==null){
                        return 0;
                    }
                    if(o1==null){
                        return -1;
                    }
                    if(o2==null){
                        return 1;
                    }
                    if(o1.startDate==null && o2.startDate==null){
                        return 0;
                    }
                    if(o1.startDate==null){
                        return -1;
                    }
                    if(o2.startDate==null){
                        return 1;
                    }
                    return o1.startDate.compareTo(o2.startDate);
                }
            });
            // fill in blanks
            List<Schedule> result=[];

            Schedule first=sortedSchedule[0];
            if(first.startDate!=null){
                result.add(new Schedule(startDate: null, endDate: first.startDate));
            }

            for(int i=0; i<sortedSchedule.size()-1; i++){
                Schedule current=sortedSchedule[i];
                if(current.endDate!=null){
                    current.endDate=new DateTime(current.endDate).plusDays(1).toDate();
                }
                if(current.startDate!=null && current.endDate!=null && current.startDate.compareTo(current.endDate)>=0){
                    throw new InvalidScheduleException("End date earlier than start date");
                }
                current.content=checkScheduleContent(current.content)

                Date nextStartDate=sortedSchedule[i+1].startDate;
                if(nextStartDate==null){
                    nextStartDate=DateTime.now().minusYears(200).toDate();
                }
                Date currentEndDate=current.endDate;
                if(currentEndDate==null){
                    currentEndDate=DateTime.now().plusYears(200).toDate();
                }
                int diff=nextStartDate.compareTo(currentEndDate);
                if(diff<0){
                    throw new InvalidScheduleException("Overlapping period");
                }else if(diff>0){
                    result.add(current);
                    result.add(new Schedule(startDate: currentEndDate, endDate: nextStartDate))
                }else { //diff=0
                    result.add(schedule[i]);
                }
            }
            Schedule last=sortedSchedule[sortedSchedule.size()-1];
            if(last.endDate!=null){
                last.endDate=new DateTime(last.endDate).plusDays(1).toDate();
            }
            result.add(last);
            if(last.endDate!=null){
                result.add(new Schedule(startDate: last.endDate, endDate: null));
            }
            return result;
        }else{
            List<Schedule> result=[];
            // fill blanks in schedule
            Schedule current=schedule[0];
            if(current.endDate!=null){
                current.endDate=new DateTime(current.endDate).plusDays(1).toDate();
            }
            if(current.startDate!=null && current.endDate!=null && current.startDate.compareTo(current.endDate)>=0){
                throw new InvalidScheduleException("End date earlier than start date");
            }
            current.content=checkScheduleContent(current.content)
            if(current.startDate!=null){
                result.add(new Schedule(startDate: null, endDate: current.startDate));
            }
            result.add(current);
            if(current.endDate!=null){
                result.add(new Schedule(startDate: current.endDate, endDate: null));
            }
            return result;
        }
    }

    private ScheduledContentPeriod fromDto(ScheduledContent st, SchedulePeriod parent, ScheduleType type){
        ScheduledContentPeriod t=new ScheduledContentPeriod(startTime: st.startTime, endTime: st.endTime, schId: parent.id);

        t.setStartTime(ScheduledContentWithLocalTime.toLocalTime(t.startTime).toString());
        t.setEndTime(ScheduledContentWithLocalTime.toLocalTime(t.endTime).toString());
        if(st.content!=null){
            t.contentType=st.content.type;
            t.mediaSize=st.content.size;
            t.fileUrl=st.content.fileUrl;
            t.infoText=st.content.infoText;
            t.bgColor=st.content.bgColor;
            t.audioUrl=st.content.audioUrl;

        }
        if(st.content!=null){
            if(mediaContentService.isContentGenerated(st.content)){
                if(st.contentExt==null){
                    GeneratedMediaContent gen=mediaContentService.generateMediaContent(st.content, Area.SCREEN);
                    t.mediaContentId=gen.id;
                }else{
                    t.mediaContentId=st.contentExt.id;
                }
            }
            if(!mediaContentService.isContentGenerated(st.content) && st.contentExt!=null){
                mediaContentService.deleteMediaContent(st.contentExt);
                t.mediaContentId=null;
            }

        }else{
            if(st.contentExt!=null){
                mediaContentService.deleteMediaContent(st.contentExt);
                t.mediaContentId=null;
            }
        }
        return t;
    }

    private boolean isValid(ScheduledContentWithLocalTime t) throws InvalidScheduleException{
        if(t.localStartTime==null){
            t.setStartTime(t.startTime);
        }
        if(t.localEndTime==null){
            t.setEndTime(t.endTime);
        }
        if(t.localStartTime.compareTo(minuteToMidnight)>=0){
            throw new InvalidScheduleException();
        }
        if(t.localEndTime.compareTo(midnight)<=0){
            throw new InvalidScheduleException();
        }
        if(t.localStartTime.compareTo(t.localEndTime)>=0){
            throw new InvalidScheduleException();
        }
    }

    @Transactional
    void create(List<Schedule> schedule, ObjectType ownerType, Long ownerId, ScheduleType type) throws InvalidScheduleException{
        List<Schedule> newSchedule=checkSchedule(schedule);
        try{
            newSchedule.each {
                SchedulePeriod s=scheduleDao.create(new SchedulePeriod(startDate: it.startDate, endDate: it.endDate, type:type, ownerType: ownerType, ownerId: ownerId))
                it.content.each {
                    scheduledContentPeriodDao.create(fromDto(it, s, type))
                }

            }
        }catch(Exception e){
            log.error("Error creating schedule", e);
            throw new ScheduleCreationException();
        }
    }


    @Transactional
    List<Schedule> getScheduleFor(ObjectType ownerType, Long ownerId, ScheduleType type){
        return scheduleDao.list(ownerType, ownerId, type);
    }

    @Transactional
    Collection<String> getImNamesToRefresh(Date d){
        Set<String> result=[]
        Set<Long> imIds=[]

        Iterable<ScheduledContentPeriodDao.ScheduleOwner> allChanges=scheduledContentPeriodDao.allScheduleChangesOn(d)
        for(ScheduledContentPeriodDao.ScheduleOwner o:allChanges){
            if(o.type==ObjectType.IM){
                imIds.add(o.id);
            }else if(o.type==ObjectType.LOBBY){
                result.addAll(imDao.findImsByLobby(o.id));
            }else if(o.type==ObjectType.STATION){
                result.addAll(imDao.findImsByStation(o.id));
            }else if(o.type==ObjectType.LINE){
                result.addAll(imDao.findImsByLine(o.id));
            }else if(o.type==ObjectType.SCHEMA){
                result.addAll(imDao.findImsBySchema(o.id));
            }
        }
        result.addAll(imDao.findImNames(imIds));

      result
    }

    private Area getAreaByScheduleType(ScheduleType type){
       switch(type){
           case ScheduleType.BANNER: return Area.BANNER;
           case ScheduleType.MESSAGE: return Area.SCREEN;
           default:return Area.SCREEN;
       }
    }


    @Transactional
    ScheduledContent getScheduledContent(Long imId,ScheduleType type, Date moment){
       def closure={ObjectType otype, Long oid -> scheduledContentPeriodDao.getCurrentContent(otype,oid, type, moment)}
       return retrieveResult(ObjectType.IM, imId, closure)
    }


    @Transactional
    public void cleanScheduleFor(ObjectType ownerType, Long ownerId, ScheduleType type){
        deleteScheduleFor(ownerType, ownerId, type, true);
    }

    private void deleteScheduleFor(ObjectType ownerType, Long ownerId, ScheduleType type, boolean withContent){
        if(withContent){
            List<Schedule> schedule=scheduleDao.list(ownerType, ownerId, type);
            scheduleDao.deleteFor(ownerType, ownerId, type);
            for(Schedule sch:schedule){
                for(ScheduledContent c:sch.content){
                    if(c.contentExt!=null){
                        mediaContentService.deleteMediaContent(c.contentExt);
                    }
                }
            }
        }else{
            scheduleDao.deleteFor(ownerType, ownerId, type);
        }
    }

    @Transactional
    void updateScheduleFor(List<Schedule> schedule, ObjectType ownerType, Long ownerId, ScheduleType type){
        deleteScheduleFor(ownerType, ownerId, type, false);
        create(schedule, ownerType, ownerId, type);
    }

    public void refreshInfoTextOnIms(){
        List<String> imNames= new ArrayList<>(getImNamesToRefresh(new Date()));
        imNotificationService.notifyIms(imNames);
    }
}
