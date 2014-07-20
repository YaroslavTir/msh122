package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Ignore
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.validation.annotation.UniqueInHierarchyLevelName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

/**
 * User: NFadin
 * Date: 25.04.14
 * Time: 13:22
 */
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
@Table('schema')
@Seq('schema_seq')
class Schema extends HierarchyObject implements WithSettings {
    @Id Long id
    String name

    @Ignore Settings settings

    @Ignore
    Collection<Line> lines;

    @Override
    @Ignore
    Long getParentId() {
        null
    }
}
