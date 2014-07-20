package com.fls.metro.core.data.dao
import com.fls.metro.core.data.domain.BannerSettings
import com.fls.metro.core.data.domain.ObjectType
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository
/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:11
 */
@Slf4j
@Repository
class BannerSettingsDao extends AbstractDao<BannerSettings> {

    BannerSettings get(ObjectType type, Long ownerId) {
        BannerSettings result=null;
        def it=sql.firstRow('select id, update_date, image_url from banner_settings where owner_type=? and owner_id=?',[type.name(),ownerId]);
        if(it){
            result=new BannerSettings(id: it.id, updateDate: it.update_date,ownerType:type, ownerId:ownerId, imageUrl: it.image_url);
        }
        result
    }


    void deleteFor(ObjectType type, Long ownerId){
        sql.execute("delete from banner_settings where owner_type=? and owner_id=?",[type.name(), ownerId]);
    }

}
