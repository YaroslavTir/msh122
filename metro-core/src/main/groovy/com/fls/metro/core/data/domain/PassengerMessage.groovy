package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.annotation.TimestampField
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

/**
 * User: NFadin
 * Date: 23.05.14
 * Time: 16:56
 */
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode
@ToString
@Table('passenger_message')
@Seq('pass_mess_seq')
class PassengerMessage {
    @Id
    Long id
    String imName
    String name = 'anonymous'
    String contact = 'anonymous'
    String message = 'empty'
    @TimestampField
    Date createDate = new Date()
}
