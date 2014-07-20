package com.fls.metro.core.data.dao.statistic

import com.fls.metro.core.data.dao.AbstractDao
import com.fls.metro.core.data.domain.statistic.ImStatistic
import com.fls.metro.core.data.dto.GroupPeriod
import com.fls.metro.core.data.filter.ImStatisticFilter
import groovy.sql.Sql
import groovy.transform.TupleConstructor
import org.joda.time.DateTime
import org.springframework.stereotype.Repository

/**
 * User: NFadin
 * Date: 02.06.2014
 * Time: 13:30
 */
@Repository
class ImStatisticDao extends AbstractDao<ImStatistic> {

    @TupleConstructor
    static class GroupStatisticData {
        Integer inum
        Integer year
        Date start
        Date end
        Map<String, Long> eventData = [:]
    }

    List<GroupStatisticData> filter(ImStatisticFilter filter) {
        def qb = new StringBuilder('select e.event as eventName, sum(e.fires) as fireTimes, ' +
                'extract(year from s.date_from) as year');
        def groupPeriod = filter.groupPeriod ?: GroupPeriod.DAY
        def periodGroupPart = "extract(${groupPeriod.sqlPart} from s.date_from)".toString()
        qb.append(", $periodGroupPart as inum".toString())

        qb.append(' from im_statistic s join im_event e on (s.id = e.statistic_id) ')
        def parameters = []
        if (!filter.empty) {
            def ands = []
            if (filter.imName) {
                ands << 's.im_name = ?'
                parameters << filter.imName
            }
            if (filter.periodDateFrom) {
                ands << 's.date_from >= ?'
                parameters << Sql.DATE(filter.periodDateFrom)
            }
            if (filter.periodDateTo) {
                ands << 's.date_to <= ?'
                parameters << Sql.DATE(filter.periodDateTo)
            }
            qb.append("where ${ands.join(' and ')}".toString())
        }
        qb.append(" group by extract(year from s.date_from), $periodGroupPart, e.event".toString())
        qb.append(" order by extract(year from s.date_from), $periodGroupPart".toString())
        def result = []
        GroupStatisticData last = null
        sql.eachRow(qb.toString(), parameters) {
            if (!last || it.year != last.year || it.inum != last.inum) {
                last = extractData(groupPeriod, it)
                result << last
            }
            def eventVal = last.eventData[it.eventName]
            if (eventVal) {
                last.eventData[it.eventName] = eventVal + it.fireTimes
            } else {
                last.eventData << [(it.eventName): it.fireTimes]
            }
        }
        return result
    }

    private static GroupStatisticData extractData(GroupPeriod gp, def it) {
        def inum = it.inum as Integer
        def year = it.year as Integer
        def dt = new DateTime().withYear(year)
        switch (gp) {
            case GroupPeriod.DAY:
                dt = dt.withDayOfYear(inum)
                return new GroupStatisticData(inum, year, dt.toDate())
            case GroupPeriod.WEEK:
                dt = dt.withWeekOfWeekyear(inum).withDayOfWeek(1)
                return new GroupStatisticData(inum, year, dt.toDate(), dt.plusWeeks(1).minusDays(1).toDate())
            case GroupPeriod.MONTH:
                dt = dt.withMonthOfYear(inum).withDayOfMonth(1)
                return new GroupStatisticData(inum, year, dt.toDate(), dt.plusMonths(1).minusDays(1).toDate())
            case GroupPeriod.YEAR:
                dt = dt.withDayOfYear(1)
                return new GroupStatisticData(inum, year, dt.toDate(), dt.plusYears(1).minusDays(1).toDate())
        }
    }
}
