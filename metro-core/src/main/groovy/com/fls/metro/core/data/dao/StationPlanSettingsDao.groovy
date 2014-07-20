package com.fls.metro.core.data.dao
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.domain.StationPlanSettings
import com.fls.metro.core.data.domain.StationPlanSettingsItem
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository
/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:11
 */
@Slf4j
@Repository
class StationPlanSettingsDao extends AbstractDao<StationPlanSettings> {

    StationPlanSettings get(ObjectType type, Long ownerId) {
        StationPlanSettings result=null;
        def it=sql.firstRow('select id, update_date, rus_url, eng_url, items from station_plan_settings where owner_type=? and owner_id=?',[type.name(),ownerId]);
        if(it){
            result=new StationPlanSettings(id: it.id, updateDate: it.update_date, rusUrl: it.rus_url, engUrl: it.eng_url,
                    ownerType:type, ownerId:ownerId);
            if (it.items) {
                def kryo=new Kryo();
                kryo.register(StationPlanSettingsItem.class,1);
                ByteArrayInputStream inp=new ByteArrayInputStream(it.items);
                Input input=new Input(inp);
                List<StationPlanSettingsItem> items=[];
                result.items = kryo.readObject(input, items.getClass())
            }
        }
        result
    }

    @Override
    StationPlanSettings create(StationPlanSettings t) {
        def kryo=new Kryo();
        kryo.register(StationPlanSettingsItem.class,1);
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Output output=new Output(out);
        if(t.items){
            kryo.writeObject(output, t.items)
        } else{
            kryo.writeObject(output, [])
        }
        output.close();
        def result=sql.executeInsert("insert into station_plan_settings (owner_type, owner_id, rus_url, eng_url, items, update_date) values (?,?,?,?,?,?)",
                t.ownerType.name(), t.ownerId, t.rusUrl, t.engUrl,Sql.BINARY(out.toByteArray()), Sql.TIMESTAMP(t.updateDate))
        Long id=result[0][0];
        t.id=id;
        t
    }

    @Override
    def update(StationPlanSettings t) {
        def kryo=new Kryo();
        kryo.register(StationPlanSettingsItem.class,1);
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Output output=new Output(out);
        if(t.items){
            kryo.writeObject(output, t.items)
        } else{
            kryo.writeObject(output, [])
        }
        output.close();
        sql.execute("update station_plan_settings set rus_url=?, eng_url=?, items=?, update_date=? where owner_type=? and owner_id=?",
                t.rusUrl, t.engUrl,Sql.BINARY(out.toByteArray()), Sql.TIMESTAMP(t.updateDate), t.ownerType.name(), t.ownerId)
    }

    void deleteFor(ObjectType type, Long ownerId){
        sql.execute("delete from station_plan_settings where owner_type=? and owner_id=?",[type.name(), ownerId]);
    }

}
