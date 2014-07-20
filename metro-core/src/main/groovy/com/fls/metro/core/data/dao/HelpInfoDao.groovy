package com.fls.metro.core.data.dao

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.fls.metro.core.data.domain.HelpInfo
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.dto.content.HelpMediaData
import com.fls.metro.core.data.dto.content.help.HelpMenuItemDataType
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository
/**
 * User: APermyakov
 * Date: 21.05.14
 * Time: 14:16
 */
@Slf4j
@Repository
class HelpInfoDao extends AbstractDao<HelpInfo>{

    List<HelpInfo> findAll(ObjectType type, def id) {
        List<HelpInfo> result=[];
        List<HelpMediaData> vdata=[];
        def kryo=new Kryo();
        kryo.register(HelpMediaData.class,0);


        sql.eachRow('select index, title, html_text, name, update_date, language, help_info_type, videos, audio from help_info where object_type=? and object_id=? order by index',[type.name(), id]) {
            HelpInfo hi=new HelpInfo(index:it.index, objectType: type, objectId: id, updateDate: it.update_date,
                    name:it.name, title: it.title, htmlText: it.html_text, language: it.language,
                    helpInfoType: HelpMenuItemDataType.valueOf(it.help_info_type))
            if(hi.updateDate!=null){
                hi.updateDate=new Date(hi.updateDate.getTime());
            }
            if (it.videos) {
                ByteArrayInputStream inp=new ByteArrayInputStream(it.videos);
                Input input=new Input(inp);
                hi.video = kryo.readObject(input, vdata.getClass())
            }

            if (it.audio) {
                ByteArrayInputStream inp=new ByteArrayInputStream(it.audio);
                Input input=new Input(inp);
                hi.audio = kryo.readObject(input, vdata.getClass())
            }
            result.add(hi)
        }
        result

    }

    void saveNewHelpInfoList(List<HelpInfo> helpInfos, ObjectType objectType, long objectId) {
        def kryo=new Kryo();
        kryo.register(HelpMediaData.class,0);
        sql.execute("delete from help_info where object_type = ? and object_id = ? ", objectType.name(), objectId)
        sql.withBatch(
            """INSERT INTO help_info(index, title, html_text, name, object_type,
                            object_id, update_date, language, help_info_type, videos, audio)
                    values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""", { ps ->
                helpInfos.each { s ->
                    ByteArrayOutputStream out=new ByteArrayOutputStream();
                    Output output=new Output(out);
                    if(s.video){
                        kryo.writeObject(output, s.video)
                    } else{
                        kryo.writeObject(output, [])
                    }
                    output.close();

                    ByteArrayOutputStream out2=new ByteArrayOutputStream();
                    Output output2=new Output(out2);
                    if(s.audio){
                        kryo.writeObject(output2, s.audio)
                    } else{
                        kryo.writeObject(output2, [])
                    }
                    output2.close();
                    ps.addBatch(
                            s.index, s.title, s.htmlText, s.name, s.objectType.name(), s.objectId,
                            Sql.TIMESTAMP(s.updateDate), s.language.name(), s.helpInfoType.name(),
                            Sql.BINARY(out.toByteArray()),Sql.BINARY(out2.toByteArray())
                    )
                }
            }
        )

    }

    void deleteFor(ObjectType ownerType, Long ownerId){
        sql.execute("delete from help_info where object_type = ? and object_id = ? ", ownerType.name(), ownerId)
    }
}
