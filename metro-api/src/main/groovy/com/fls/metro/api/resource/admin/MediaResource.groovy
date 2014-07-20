package com.fls.metro.api.resource.admin

import com.fls.metro.api.dto.admin.FileUploadResult
import com.fls.metro.core.data.domain.MediaFile
import com.fls.metro.core.data.domain.MediaFileType
import com.fls.metro.core.data.dto.grid.data.MediaFileData
import com.fls.metro.core.data.filter.MediaFilter
import com.fls.metro.core.data.filter.Order
import com.fls.metro.core.service.MediaService
import com.sun.jersey.core.header.FormDataContentDisposition
import com.sun.jersey.multipart.FormDataParam
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 07.05.14
 * Time: 10:31
 * To change this template use File | Settings | File Templates.
 */
@Path('media')
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
@Component
class MediaResource extends AbstractResource {

    private static String HEADER_ENCODING = 'ISO8859-1'
    // from org.jvnet.mimepull.MIMEParser. God knows why they did it...

    @Autowired
    MediaService mediaService;


    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public FileUploadResult upload(@FormDataParam("mediaFileType") MediaFileType mediaType,
                                   @FormDataParam("file") InputStream uploadedInputStream,
                                   @FormDataParam("file") FormDataContentDisposition fileDetail) {
        def mf = new MediaFile(
                name: new String(fileDetail.fileName.getBytes(HEADER_ENCODING)),
                mediaType: mediaType)
        validate(mf)
        try {
            String url = mediaService.saveFile(mf, uploadedInputStream);
            return new FileUploadResult(url: url);
        } catch (Exception ignored) {
            return new FileUploadResult(url: null);
        }
    }

    @GET
    public MediaFileData list(@QueryParam('mediaType') MediaFileType mediaType,
                              @QueryParam('currentPage') Integer currentPage,
                              @QueryParam('pageSize') Integer pageSize) {
        return mediaService.list(new MediaFilter(
                mediaType: mediaType,
                currentPage: currentPage,
                pageSize: pageSize,
                orders: [
                        new Order(
                                field: 'createdDate',
                                direction: 'DESC'
                        )
                ]
        ));
    }

    @GET
    @Path('{id}')
    public MediaFile info(@PathParam('id') Long id) {
        mediaService.find(id)
    }

    @DELETE
    @Path('{id}')
    public void delete(@PathParam('id') Long id) {
        mediaService.delete(id);
    }


}
