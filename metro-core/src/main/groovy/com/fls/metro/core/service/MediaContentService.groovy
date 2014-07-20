package com.fls.metro.core.service

import com.fls.metro.core.data.dao.GeneratedMediaContentDao
import com.fls.metro.core.data.domain.GeneratedMediaContent
import com.fls.metro.core.data.dto.content.Area
import com.fls.metro.core.data.dto.content.media.MediaContent
import com.fls.metro.core.data.dto.content.media.MediaContentInternal
import com.fls.metro.core.data.dto.content.media.MediaContentInternalSize
import com.fls.metro.core.data.dto.content.media.MediaContentInternalType
import com.fls.metro.core.data.dto.content.media.MediaContentType
import com.fls.metro.core.exception.MediaContentGenerationException
import com.fls.metro.core.util.graphics.HtmlImageGenerator
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.imageio.ImageIO
import java.awt.*
/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 06.05.14
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
@Service
@Slf4j
public class MediaContentService extends FileStoreService{

    @Value('${images.pathToStoreGenerated}')
    private String pathToStore;

    @Value('${images.baseUrlGenerated}')
    private String baseUrl;

    private static String STYLES ="<style>H1 {font-size:96px} H2 {font-size:72px} H3 {font-size:60px} " +
            "H4 {font-size:48px} H5 {font-size:36px} H6 {font-size:33px} " +
            "p {font-size:36px}</style>";


    @Autowired
    private GeneratedMediaContentDao mediaContentDao

    @Autowired
    private MediaService mediaService

    private static final String GEN_PREFIX ="gen";

    public boolean isContentGenerated(MediaContentInternal mc){
        if(mc.type!=MediaContentInternalType.IMAGE){
            return false;
        }
        if(mc.infoText){
            return true;
        }
        if(mc.size==MediaContentInternalSize.FULLSIZE || mc.size==MediaContentInternalSize.MAXSIZE){
            return true;
        }
        return false;
    }

    @Transactional
    GeneratedMediaContent generateMediaContent(MediaContentInternal mc, Area area){
        MediaContent m=convertMediaContent(mc, area);
        GeneratedMediaContent gm=new GeneratedMediaContent(generated: isContentGenerated(mc), updateDate: m.updateDate, type: m.type, fileUrl: m.fileUrl, audioUrl: m.audioUrl);
        return mediaContentDao.create(gm);
    }

    @Transactional
    void deleteMediaContent(GeneratedMediaContent mc){
        if(mc!=null){
            if(mc.fileUrl!=null){
                deleteFileOnUrl(mc.fileUrl);
            }
            if(mc.id!=null){
                mediaContentDao.delete(mc.id);
            }
        }
    }

    @Transactional
    void deleteMediaContent(Long id){
        GeneratedMediaContent mc=mediaContentDao.find(id);
        if(mc!=null){
            deleteMediaContent(mc);
        }
    }

    private void deleteFileOnUrl(String url){
        String path=getFilePathByUrl(url,pathToStore);
        File file=new File(path);
        if(file.exists()){
            file.delete();
        }
    }

    GeneratedMediaContent getById(Long id){
        return mediaContentDao.find(id);
    }

    MediaContent convertMediaContent(MediaContentInternal mc, Area area) throws MediaContentGenerationException{
        switch (mc.type){
            case MediaContentInternalType.IMAGE:
                return generateImage(mc, area);
            case MediaContentInternalType.VIDEO:
                return simpleUrl(mc.fileUrl, MediaContentType.VIDEO);
            case MediaContentInternalType.AUDIO:
                return simpleUrl(mc.fileUrl, MediaContentType.AUDIO);
        }
    }

    MediaContent generateImage(MediaContentInternal mc, Area area){
        MediaContent res=new MediaContent(type:MediaContentType.IMAGE, updateDate: new Date());
        if(mc.audioUrl!=null){
            res.type=MediaContentType.IMAGE_AUDIO;
            res.audioUrl=mc.audioUrl;
        }

        def fileUrl=mc.fileUrl;
        if(fileUrl){
            if(mc.size==MediaContentInternalSize.FULLSIZE){
                fileUrl=mediaService.resizeFullSize(fileUrl, area);
                if(mc.infoText){
                    res.fileUrl=generateTextOnImage(mc.infoText, fileUrl, area);
                }else{
                    res.fileUrl=fileUrl;
                }
            }else if(mc.size==MediaContentInternalSize.MAXSIZE){
                fileUrl=mediaService.resizeMaxSize(fileUrl, area);
                if(mc.infoText){
                    res.fileUrl=generateTextOnImage(mc.infoText, fileUrl, null);
                }else{
                    res.fileUrl=fileUrl;
                }
            }else{
                if(mc.infoText){
                    res.fileUrl=generateTextOnImage(mc.infoText, fileUrl, null);
                }else{
                    res.fileUrl=fileUrl;
                }
            }
            return res;
        }
        // generate image
        if(mc.bgColor!=null){
            res.fileUrl=generateTextOnColor(mc.infoText, mc.bgColor, area);
        }else{
            res.fileUrl=generateTextOnColor(mc.infoText, "#FFFFFF", area);
        }
        return res;
    }

    MediaContent simpleUrl(String url, MediaContentType type){
        return new MediaContent(type:type, fileUrl: url, updateDate: new Date());
    }

    MediaContent simple2Url(String url, String audioUrl, MediaContentType type){
        return new MediaContent(type:type, fileUrl: url, audioUrl:audioUrl, updateDate: new Date());
    }

    public String generateTextOnColor(String text, String color, Area area) throws MediaContentGenerationException{
        HtmlImageGenerator imageGenerator=new HtmlImageGenerator();
        if(area==null){
            area=Area.SCREEN;
        }
        int width=area.width;
        int height=area.height;
        imageGenerator.setSize(new Dimension(width, height));
        String html=String.format("<html><head>%s</head><body margin=\"0\" padding=\"0\" bgcolor=\"%s\" style=\"font-size:36px\"><div style=\"width:%s;height:%s;\">%s<div></body></html>", STYLES, color, width, height, text);
        imageGenerator.loadHtml(html);
        String path=generateUniqueFileName(GEN_PREFIX, pathToStore,"file.png");
        String fileName=imageGenerator.saveAsImage(path);
        return baseUrl+fileName;
    }

    public String generateTextOnImage(String text, String imageUrl, Area area) throws MediaContentGenerationException{
        HtmlImageGenerator imageGenerator=new HtmlImageGenerator();
        int width=0;
        int height=0;
        if(area!=null){
            width=area.width;
            height=area.height;
        }else{
            URL url = new URL(imageUrl);
            java.awt.Image image = ImageIO.read(url);//Toolkit.getDefaultToolkit().createImage(url);
            if(!image){
                return null;
            }
            width=image.getWidth(null);
            height=image.getHeight(null);
        }
        if(width<=0 || height<=0){
            width=Area.SCREEN.width;
            height=Area.SCREEN.height;
        }
        imageGenerator.setSize(new Dimension(width, height));
        String html=String.format("<html><head>%s</head><body style=\"background-image: url(%s); background-repeat:no-repeat no-repeat; background-size:cover; background-position:center; font-size:36px\"><div style=\"width:%s;height:%s\">%s<div></body></html>", STYLES, imageUrl, width, height, text)
        imageGenerator.loadHtml(html);
        String path=generateUniqueFileName(GEN_PREFIX, pathToStore,"file.png");
        String fileName=imageGenerator.saveAsImage(path);
        return baseUrl+fileName;
    }


}
