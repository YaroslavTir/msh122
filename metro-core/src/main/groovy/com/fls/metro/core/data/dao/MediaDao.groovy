package com.fls.metro.core.data.dao
import com.fls.metro.core.data.domain.MediaFile
import com.fls.metro.core.data.domain.MediaFileType
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Repository
/**
 * User: NFadin
 * Date: 16.04.14
 * Time: 13:11
 */
@Slf4j
@Repository
class MediaDao extends AbstractDao<MediaFile> {

    List<MediaFile> findAll() {
        List<MediaFile> result = []
        sql.eachRow('select id, name, media_type, created_date, url, thumb_url from media order by name') {
            result.add(new MediaFile(id: it.id, name: it.name, mediaType: it.media_type, createdDate: it.created_date, url:it.url, thumbUrl: it.thumb_url ))
        }
        result
    }

    List<MediaFile> findByMediaFileType(MediaFileType mediaFileType) {
        List<MediaFile> result = []
        sql.eachRow('select id, name, media_type, created_date, url, thumb_url from media ' +
                'where media_type = :mediaType order by name', ['mediaType': mediaFileType.name()]) {
            result.add(new MediaFile(id: it.id, name: it.name, mediaType: it.media_type, createdDate: it.created_date, url:it.url, thumbUrl: it.thumb_url ))
        }
        result
    }

    List<MediaFile> findByNameAndType(String name, MediaFileType type) {
        executeSelectRows name: name, mediaType: type
    }
}
