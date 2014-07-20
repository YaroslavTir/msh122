package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Ignore
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.annotation.TimestampField
import com.fls.metro.core.data.dto.content.UpdatableItem
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlTransient

/**
 * User: NFadin
 * Date: 23.04.14
 * Time: 10:07
 */
@EqualsAndHashCode
@ToString
@Table('news')
@Seq('news_seq')
class News implements WithId {
    @Id Long id
    @TimestampField Date updateDate
    String title = ''
    String titleEn = ''
    ObjectType objectType
    Long objectId
}
