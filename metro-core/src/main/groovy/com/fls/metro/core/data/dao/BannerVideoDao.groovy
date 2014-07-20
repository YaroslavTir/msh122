package com.fls.metro.core.data.dao
import com.fls.metro.core.data.domain.BannerVideo
import com.fls.metro.core.data.domain.GeneratedMediaContent
import com.fls.metro.core.data.dto.content.ContentItem
import com.fls.metro.core.data.dto.content.media.MediaContentInternal
import com.fls.metro.core.data.dto.content.media.MediaContentInternalType
import com.fls.metro.core.data.dto.content.media.MediaContentType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:11
 */
@Slf4j
@Repository
class BannerVideoDao extends AbstractDao<BannerVideo> {

    @Autowired
    GeneratedMediaContentDao generatedMediaDao;

    List<ContentItem> listVideo(Long settingsId) {
        List<ContentItem> result = []
        sql.eachRow('select file_url from banner_video where banner_settings_id=? order by number',[settingsId]) {
            if(it.file_url!=null){
                result.add(new ContentItem(content:new MediaContentInternal(type: MediaContentInternalType.VIDEO, fileUrl: it.file_url), contentExt: new GeneratedMediaContent(type:MediaContentType.VIDEO, fileUrl: it.file_url)));
            }
        }
        result
    }


    void deleteFor(Long settingsId){
        sql.execute("delete from banner_video where banner_settings_id=?",[settingsId]);
    }

}
