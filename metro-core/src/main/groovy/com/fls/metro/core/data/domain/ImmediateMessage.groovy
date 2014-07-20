package com.fls.metro.core.data.domain
import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.annotation.TimestampField
import com.fls.metro.core.data.dto.content.ContentItem
import com.fls.metro.core.data.dto.messages.ImmMessageStatus
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.codehaus.jackson.annotate.JsonIgnoreProperties

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
@Table('imm_message')
@Seq('imm_message_seq')
class ImmediateMessage extends ContentItem implements WithId, WithOwner{
    @Id Long id
    @TimestampField
    Date updateDate
    @TimestampField
    Date startDate
    @TimestampField
    Date stopDate

    ObjectType ownerType
    Long ownerId

    ImmMessageStatus status=ImmMessageStatus.STOPPED



}
