package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Ignore
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.validation.annotation.UniqueInHierarchyLevelName
import com.fls.metro.core.validation.annotation.UniqueLineNumber
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
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
@Table('line')
@Seq('line_seq')
@UniqueLineNumber(message = '{line.number.must.be.unique}')
@UniqueInHierarchyLevelName(message = '{line.name.must.be.unique.in.schema}')
class Line extends HierarchyObject implements Comparable<Line> {
    @Id Long id
    @NotNull(message = '{line.schema.cannot.be.null}')
    Long schemaId
    @NotEmpty(message = '{line.name.cannot.be.empty}')
    String name
    @NotEmpty(message = '{line.enname.cannot.be.empty}')
    String enname
    @NotEmpty(message = '{line.number.cannot.be.empty}')
    @Pattern(regexp = '[0-9]+', message = '{line.number.should.be.number}')
    private String number
    @NotEmpty(message = '{line.color.cannot.be.empty}')
    @Pattern(regexp = '#[0-9A-Fa-f]{6}', message = '{line.color.mask.validation}')
    String color = '#000000'
    String picLink

    @Ignore
    Collection<Station> stations;

    @Override
    int compareTo(Line o) {
        if (id.equals(o.id)) return 0

        if (getNumber() > o.getNumber()) return 1

        return -1
    }

    @Override
    @Ignore
    Long getParentId() {
        schemaId
    }

    Integer getNumber() {
        return this.number ? Integer.parseInt(this.number) : null
    }

    void setNumber(Integer number) {
        this.number = String.valueOf(number)
    }
}
