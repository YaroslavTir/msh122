package com.fls.metro.core.data.domain.statistic

import com.fls.metro.core.annotation.Column
import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Ignore
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.annotation.TimestampField
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

/**
 * User: NFadin
 * Date: 02.06.2014
 * Time: 12:50
 */
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@ToString
@Table('im_statistic')
@Seq('im_stat_seq')
class ImStatistic {
    @Id Long id
    String imName
    @TimestampField @Column('date_from') Date from
    @TimestampField @Column('date_to') Date to
    @Ignore List<ImEvent> events
}
