package com.fls.metro.core.data.domain

import com.fls.metro.core.annotation.Id
import com.fls.metro.core.annotation.Seq
import com.fls.metro.core.annotation.Table
import com.fls.metro.core.annotation.TimestampField
import com.fls.metro.core.data.dto.content.Language
import com.fls.metro.core.data.dto.content.HelpMediaData
import com.fls.metro.core.data.dto.content.help.HelpMenuItemDataType
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.codehaus.jackson.annotate.JsonIgnoreProperties

/**
 * User: APermyakov
 * Date: 20.05.14
 * Time: 10:45
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
@ToString
@Table('help_info')
@Seq('help_info_seq')
class HelpInfo implements WithId {
    @Id Long id
    @TimestampField Date updateDate
    String title
    String htmlText
    String name
    Integer index

    ObjectType objectType
    Long objectId
    Language language
    HelpMenuItemDataType helpInfoType
    List<HelpMediaData> video=[]
    List<HelpMediaData> audio=[]

    List<HelpMediaData> getMedia(){
        List<HelpMediaData> media=[];
        if(video){
            media.addAll(video);
        }
        if(audio){
            media.addAll(audio);
        }
        media
    }
}
