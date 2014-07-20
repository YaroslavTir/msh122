package com.fls.metro.test;

import com.fls.metro.core.data.dto.content.Content;
import com.fls.metro.core.data.dto.content.Language;
import com.fls.metro.core.data.dto.content.media.MediaContentInternal;
import com.fls.metro.core.data.dto.content.media.MediaContentInternalSize;
import com.fls.metro.core.data.dto.content.media.MediaContentInternalType;
import groovy.util.GroovyTestCase;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 30.04.14
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/coreContext.xml"})
public abstract class CoreTest extends GroovyTestCase {

    @After
    public abstract void clean();

    protected String genUnique(String prefix){
        return prefix+System.currentTimeMillis();
    }

    protected MediaContentInternal textOnBgcolor(String text){
        MediaContentInternal m=new MediaContentInternal();
        m.setInfoText(text);
        m.setBgColor("#AAFFAA");
        m.setType(MediaContentInternalType.IMAGE);
        return m;
    }

    protected MediaContentInternal fileText(String url, String text){
        MediaContentInternal m=new MediaContentInternal();
        m.setInfoText(text);
        m.setFileUrl(url);
        m.setType(MediaContentInternalType.IMAGE);
        m.setSize(MediaContentInternalSize.MAXSIZE);
        return m;
    }

    protected MediaContentInternal video(String url){
        MediaContentInternal m=new MediaContentInternal();
        m.setFileUrl(url);
        m.setType(MediaContentInternalType.VIDEO);
        return m;
    }

    protected MediaContentInternal image(String url){
        MediaContentInternal m=new MediaContentInternal();
        m.setFileUrl(url);
        m.setType(MediaContentInternalType.IMAGE);
        m.setSize(MediaContentInternalSize.FULLSIZE);
        return m;
    }

    protected Content getRusContent(List<Content> list){
        for(Content c:list){
            if(c.getLang()== Language.RU){
                return c;
            }
        }
        return null;
    }

    protected Content getEngContent(List<Content> list){
        for(Content c:list){
            if(c.getLang()== Language.EN){
                return c;
            }
        }
        return null;
    }

    protected CoreTest(){
        System.setProperty("env","test"); // profile
    }
}
