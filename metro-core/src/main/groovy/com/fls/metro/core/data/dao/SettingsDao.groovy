package com.fls.metro.core.data.dao
import com.fls.metro.core.data.domain.ObjectType
import com.fls.metro.core.data.domain.Settings
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository
/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:11
 */
@Slf4j
@Repository
class SettingsDao extends AbstractDao<Settings> {

    Settings get(ObjectType type, Long ownerId) {
        Settings result=null;
        def it=sql.firstRow('select id, update_date, emergency_number, screensaver_timeout_sec, show_currency,show_weather, show_time,show_languages, show_external_news from settings where owner_type=? and owner_id=?',[type.name(),ownerId]);
        if(it){
            result=new Settings(id: it.id, updateDate: it.update_date, emergencyNumber: it.emergency_number, screensaverTimeoutSec: it.screensaver_timeout_sec,
                    showCurrency: it.show_currency, showWeather: it.show_weather,showTime: it.show_time, showLanguages: it.show_languages, showExternalNews: it.show_external_news,
                    ownerType:type, ownerId:ownerId);
        }
        result
    }


    void deleteFor(ObjectType type, Long ownerId){
        sql.execute("delete from settings where owner_type=? and owner_id=?",[type.name(), ownerId]);
    }

}
