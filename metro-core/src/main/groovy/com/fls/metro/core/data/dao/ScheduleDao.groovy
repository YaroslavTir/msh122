package com.fls.metro.core.data.dao
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.domain.SchedulePeriod
import com.fls.metro.core.data.dto.schedule.Schedule
import com.fls.metro.core.data.dto.schedule.ScheduleType
import groovy.util.logging.Slf4j
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:11
 */
@Slf4j
@Repository
class ScheduleDao extends AbstractDao<SchedulePeriod> {

    @Autowired
    ScheduledContentPeriodDao contentPeriodDao

    List<Schedule> list(ObjectType ownerType, Long ownerId, ScheduleType type) {
        List<Schedule> result = []
        sql.eachRow('select id, start_date, end_date from schedule where type=? and owner_type=? and owner_id=? order by coalesce(start_date,date \'1500-1-1\') asc',[type.name(), ownerType.name(), ownerId]) {
            def sch=new Schedule(startDate: it.start_date, endDate: it.end_date)
            if(sch.endDate!=null){
                DateTime endDate=new DateTime(sch.endDate).minusDays(1);
                sch.endDate=endDate.toDate();
            }
            if(sch.startDate!=null){
                sch.startDate=new Date(sch.startDate.getTime())
            }
            sch.content=contentPeriodDao.listNotEmpty(it.id)
            if(!sch.content.isEmpty() || !sch.startDate && !sch.endDate){
                result.add(sch);
            }else{
                if(!sch.startDate && !sch.endDate){
                    result.add(sch);
                }
            }
        }
        result
    }

    void deleteFor(ObjectType ownerType, Long ownerId, ScheduleType type){
        sql.execute("delete from schedule where owner_type=? and owner_id=? and type=?",[ownerType.name(), ownerId, type.name()]);
    }

}
