package com.fls.metro.core.data.dao
import com.fls.metro.core.data.domain.GeneratedMediaContent
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.domain.ScheduledContentPeriod
import com.fls.metro.core.data.dto.content.media.MediaContentInternal
import com.fls.metro.core.data.dto.content.media.MediaContentInternalSize
import com.fls.metro.core.data.dto.content.media.MediaContentInternalType
import com.fls.metro.core.data.dto.schedule.ScheduleType
import com.fls.metro.core.data.dto.schedule.ScheduledContent
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:11
 */
@Slf4j
@Repository
class ScheduledContentPeriodDao extends AbstractDao<ScheduledContentPeriod> {

    @Value('${schedule.thresholdSec}')
    int thresholdSec;

    @Autowired
    GeneratedMediaContentDao generatedMediaDao;

    List<ScheduledContent> listContent(Long scheduleId) {
        List<ScheduledContent> result = []
        sql.eachRow('select start_time, end_time, content_type, media_size, file_url, info_text, audio_url, bg_color,media_content_id from sch_content where sch_id=? order by start_time asc',[scheduleId]) {
            ScheduledContent content=new ScheduledContent(startTime: it.start_time, endTime: it.end_time, content:(it.content_type==null?null:new MediaContentInternal(type:MediaContentInternalType.valueOf(it.content_type),size:it.media_size?MediaContentInternalSize.valueOf(it.media_size):MediaContentInternalSize.DEFAULT, infoText:it.info_text, fileUrl:it.file_url, audioUrl: it.audio_url, bgColor: it.bg_color)));
            if(it.media_content_id!=null){
                content.contentExt=generatedMediaDao.find(it.media_content_id);
            }
            result.add(content)
        }
        result
    }

    List<ScheduledContent> listNotEmpty(Long scheduleId) {
        List<ScheduledContent> result = []
        sql.eachRow('select start_time, end_time, info_text, content_type, media_size, file_url,audio_url, bg_color, media_content_id from sch_content where sch_id=? and content_type is not null order by start_time asc',[scheduleId]) {
            ScheduledContent content=new ScheduledContent(startTime: it.start_time, endTime: it.end_time, content:(it.content_type==null?null:new MediaContentInternal(type:MediaContentInternalType.valueOf(it.content_type),size:it.media_size?MediaContentInternalSize.valueOf(it.media_size):MediaContentInternalSize.DEFAULT,  infoText:it.info_text, fileUrl:it.file_url, audioUrl: it.audio_url, bgColor: it.bg_color)));
            if(it.media_content_id!=null){
                content.contentExt=generatedMediaDao.find(it.media_content_id);
            } else {
                content.contentExt = new GeneratedMediaContent()
            }
            result.add(content);
        }
        result
    }

    public static class ScheduleOwner{
        ObjectType type
        Long id

        ScheduleOwner(ObjectType type,Long id) {
            this.id = id
            this.type = type
        }
    }

    Iterable<ScheduleOwner> allScheduleChangesOn(Date date){
        DateTime dt=new DateTime(date);
        LocalTime time=LocalTime.fromDateFields(date);
        Set<Long> result = new HashSet<Long>();
        sql.eachRow('select distinct s.owner_type, s.owner_id from sch_content c join schedule s on c.sch_id=s.id  where ' +
                '((? between (c.start_time - interval \''+thresholdSec+'\' second) and (c.start_time + interval \''+thresholdSec+'\' second)) ' +
                ' or (? between (c.end_time - interval \''+thresholdSec+'\' second) and (c.end_time + interval \''+thresholdSec+'\' second))) ' +
                ' and (s.start_date is null or s.start_date<=?) and (s.end_date is null or s.end_date>?)',[Sql.TIME(time),Sql.TIME(time), Sql.DATE(date), Sql.DATE(date)]) {
            result.add(new ScheduleOwner(ObjectType.valueOf(it.owner_type), it.owner_id));
        }
        result
    }

    public String toTimeString(int hour) throws IllegalArgumentException{
        if(hour<0 || hour>23){
            throw new IllegalArgumentException("Invalid arguments for time");
        }
        return String.format("%s:00:00",hour);
    }

    private String dateToTimeString(Date d){
        LocalTime lt=LocalTime.fromDateFields(d);
        return lt.toString();
    }

    private Date truncToDate(Date d){
        DateTime dt=new DateTime(d);
        return dt.minusMillis(dt.millisOfDay().get()).toDate();
    }

    ScheduledContent getCurrentContent(ObjectType ownerType, Long ownerId, ScheduleType scheduleType,Date datetime){
        ScheduledContent result
        String timeString=dateToTimeString(datetime);
        Date date=truncToDate(datetime);
        def it=sql.firstRow('select start_time, end_time, content_type, media_size, info_text, file_url, audio_url, bg_color,media_content_id ' +
                'from sch_content c right join schedule s on c.sch_id=s.id ' +
                'where ?>=start_time and ?<end_time and (s.start_date is null or s.start_date<=?) ' +
                'and (s.end_date is null or s.end_date>?) ' +
                'and s.owner_type=? and s.owner_id=? and s.type=? order by start_time',
                [Sql.TIME(timeString),Sql.TIME(timeString), Sql.DATE(date), Sql.DATE(date), ownerType.name(), ownerId, scheduleType.name()])

        if(it!=null){
            result=new ScheduledContent(startTime: it.start_time, endTime: it.end_time, content:(it.content_type==null?null:new MediaContentInternal(type:MediaContentInternalType.valueOf(it.content_type), size:it.media_size?MediaContentInternalSize.valueOf(it.media_size):MediaContentInternalSize.DEFAULT, infoText:it.info_text, fileUrl:it.file_url, audioUrl: it.audio_url, bgColor: it.bg_color)));
            if(it.media_content_id!=null){
                result.contentExt=generatedMediaDao.find(it.media_content_id);
            }
        }
        result
    }


    void deleteFor(ObjectType type, Long ownerId){
        sql.execute("delete from sch_content where owner_type=? and owner_id=?",[type.name(), ownerId]);
    }

}
