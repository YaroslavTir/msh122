package com.fls.metro.core.service
import com.fls.metro.core.data.dao.MediaDao
import com.fls.metro.core.data.domain.MediaFile
import com.fls.metro.core.data.domain.MediaFileType
import com.fls.metro.core.data.dto.content.Area
import com.fls.metro.core.data.dto.grid.data.MediaFileData
import com.fls.metro.core.data.filter.MediaFilter
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage
/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 06.05.14
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
@Service
@Slf4j
public class MediaService extends FileStoreService {


    @Value('${images.pathToStore}')
    private String pathToStore;

    @Value('${images.baseUrl}')
    private String baseUrl;

    @Value('${images.thumbwidth}')
    private int thumbWidth = 300;

    @Autowired
    MediaDao mediaDao;


    private static final String MEDIA_PREFIX = "file";
    private static final String IMG_THUMB_POSTFIX = "thumb.png";
    private static final String IMG_MAXSIZE_POSTFIX = "max.png";
    private static final String IMG_FULLSIZE_POSTFIX = "full.png";


    private static String getThumbnailPath(String imagePath) {
        return String.format("%s_%s", imagePath, IMG_THUMB_POSTFIX);
    }

    public static String getThumbnailUrl(String imageUrl) {
        return String.format("%s_%s", imageUrl, IMG_THUMB_POSTFIX);
    }

    private static String getMaxSizePath(String imagePath) {
        return String.format("%s_%s", imagePath, IMG_MAXSIZE_POSTFIX);
    }

    public static String getMaxSizeUrl(String imageUrl) {
        return String.format("%s_%s", imageUrl, IMG_MAXSIZE_POSTFIX);
    }

    private static String getFullSizePath(String imagePath) {
        return String.format("%s_%s", imagePath, IMG_FULLSIZE_POSTFIX);
    }

    public static String getFullSizeUrl(String imageUrl) {
        return String.format("%s_%s", imageUrl, IMG_FULLSIZE_POSTFIX);
    }


    private static String parseFileName(String url) {
        int pos = url.lastIndexOf('/');
        if (pos < 0) {
            return url;
        }
        return url.substring(pos + 1);
    }

    private void generateThumb(String imgPath) {
        File file = new File(imgPath);
        if (!file.exists()) {
            return;
        }
        File thumbFile = new File(getThumbnailPath(imgPath));
        if (thumbFile.exists()) {
            thumbFile.delete();
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedImage bim = ImageIO.read(fis);
            fis.close();
            float ratio = ((float) bim.getHeight() / (float) bim.getWidth());
            int thumbHeight = (int) (ratio * thumbWidth);
            Image resizedImage = bim.getScaledInstance(thumbWidth, thumbHeight, Image.SCALE_DEFAULT);
            BufferedImage rBimg = new BufferedImage(thumbWidth, thumbHeight, bim.getType());
            // Create Graphics object
            Graphics2D g = rBimg.createGraphics();

            // Draw the resizedImg from 0,0 with no ImageObserver
            g.drawImage(resizedImage, 0, 0, null);

            // Dispose the Graphics object, we no longer need it
            g.dispose();
            ImageIO.write(rBimg, "png", thumbFile);
        } catch (Exception e) {
            log.error("Error generating thumbnail for {}", imgPath, e);
        }
    }


    public String resizeFullSize(String imgUrl, Area area){
        String imgPath=getFilePathByUrl(imgUrl,pathToStore);
        String genPath=getFullSizePath(imgPath);
        File genFile = new File(genPath);
        if(genFile.exists()){
            return baseUrl+genFile.name;
        }

        File file = new File(imgPath);
        if (!file.exists()) {
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedImage bim = ImageIO.read(fis);
            fis.close();
            Image resizedImage = bim.getScaledInstance(area.width, area.height, Image.SCALE_SMOOTH);
            BufferedImage rBimg = new BufferedImage(area.width, area.height, bim.getType());
            // Create Graphics object
            Graphics2D g = rBimg.createGraphics();

            // Draw the resizedImg from 0,0 with no ImageObserver
            g.drawImage(resizedImage, 0, 0, null);

            // Dispose the Graphics object, we no longer need it
            g.dispose();

            ImageIO.write(rBimg, "png", genFile);
            return baseUrl+genFile.name;
        } catch (Exception e) {
            log.error("Error resizing image {}", imgPath, e);
        }
    }

    public String resizeMaxSize(String imgUrl, Area area){
        String imgPath=getFilePathByUrl(imgUrl, pathToStore);
        String genPath=getMaxSizePath(imgPath);
        File genFile = new File(genPath);
        if(genFile.exists()){
            return baseUrl+genFile.name;
        }
        File file = new File(imgPath);
        if (!file.exists()) {
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedImage bim = ImageIO.read(fis);
            fis.close();
            float aRatio = ((float) area.getHeight() / (float) area.getWidth());
            float iRatio = ((float) bim.getHeight() / (float) bim.getWidth());
            float width=area.width;
            float height=area.height;

            if(aRatio>=iRatio){
                height=width*iRatio;
            }else{
                width=height/iRatio;
            }

            Image resizedImage = bim.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
            BufferedImage rBimg = new BufferedImage((int)width, (int)height, bim.getType());
            // Create Graphics object
            Graphics2D g = rBimg.createGraphics();

            // Draw the resizedImg from 0,0 with no ImageObserver
            g.drawImage(resizedImage, 0, 0, null);

            // Dispose the Graphics object, we no longer need it
            g.dispose();

            ImageIO.write(rBimg, "png", genFile);
            return baseUrl+genFile.name;
        } catch (Exception e) {
            log.error("Error resizing image {}", imgPath, e);
        }
    }

    private Area getResizeArea(MediaFileType type){
        switch(type){
            case MediaFileType.IMAGE_BANNER: return Area.BANNER;
            case MediaFileType.IMAGE_SCREENSAVER: return Area.SCREENSAVER;
            case MediaFileType.IMAGE_MESSAGE: return Area.SCREEN;
            default: return null;
        }
    }

    private boolean isImage(MediaFile mf){
        switch (mf.mediaType) {
            case MediaFileType.IMAGE_BANNER:
            case MediaFileType.IMAGE_SCREENSAVER:
            case MediaFileType.IMAGE_PLAN:
            case MediaFileType.IMAGE_MESSAGE:
            case MediaFileType.IMAGE_INSIDE_TEXT:
                return true;
        }
        return false;
    }

    public String saveFile(MediaFile mf, InputStream uploadedInputStream) {
        try {

            String filePath = generateUniqueFileName(MEDIA_PREFIX, pathToStore, mf.name);
            File file = new File(filePath);

            int read = 0;
            byte[] bytes = new byte[1024];
            BufferedOutputStream out =
                    new BufferedOutputStream(new FileOutputStream(file));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

            /*Area area=getResizeArea(mf.getMediaType());
            if(area){
                resize(filePath, area);
            }*/

            String fileUrl = baseUrl + file.name;
            mf.with {
                createdDate = new Date()
                url = fileUrl
            }

           if(isImage(mf)){
                    generateThumb(filePath);
                    mf.thumbUrl = getThumbnailUrl(fileUrl);
            }
            mediaDao.create(mf);
            return fileUrl;

        } catch (Exception e) {
            log.error("Error saving file {}", mf.name, e);
        }
        return null
    }

    public MediaFileData list(MediaFilter filter) {
        return new MediaFileData(
                total: mediaDao.count(filter),
                data: mediaDao.filter(filter)
        )
    }

    private void deleteFile(String path){
        File file=new File(path);
        if(file.exists()){
            file.delete();
        }
    }

    public void delete(Long id) {
        MediaFile mf=mediaDao.find(id);
        if(mf){
            String filePath=getFilePathByUrl(mf.url, pathToStore);

            if(isImage(mf)){
                deleteFile(getFilePathByUrl(mf.thumbUrl, pathToStore));
                deleteFile(getFullSizePath(filePath));
                deleteFile(getMaxSizePath(filePath));
            }
            deleteFile(filePath);
        }
        mediaDao.delete(id);
    }

    public void update(MediaFile mf) {
        mediaDao.update(mf);
    }

    public MediaFile find(Long id) {
        mediaDao.find(id)
    }
}
