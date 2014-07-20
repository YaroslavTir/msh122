package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.ArrayField
import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Ignore
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.validation.annotation.UniqueUsername
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:04
 */
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@EqualsAndHashCode
@Table('users')
@Seq('user_seq')
@UniqueUsername(message = '{user.username.must.be.unique}')
class User implements WithId{
    @Id Long id
    String username
    String password
    @ArrayField('varchar') String[] roles
    String fio
}
