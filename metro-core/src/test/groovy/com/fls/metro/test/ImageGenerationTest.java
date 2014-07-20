package com.fls.metro.test;

import com.fls.metro.core.data.dto.content.Area;
import com.fls.metro.core.exception.MediaContentGenerationException;
import com.fls.metro.core.service.MediaContentService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 16.05.14
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
public class ImageGenerationTest extends CoreTest {

    @Autowired
    MediaContentService mediaContentService;

    @Override
    public void clean() {

    }

    @Test
    public void generateImage() throws MediaContentGenerationException{
        mediaContentService.generateTextOnColor("<p>Formatted</p> text, <H1><font color=\"FFAA00\">Color text</font></H1>!","green", Area.SCREEN);
        mediaContentService.generateTextOnImage("<p style=\"text-align: center;\">textAngular is a super cool <span style=\"color: blue;\">WYSIWYG Text</span> Editor directive for AngularJS! textAngular is a super cool <span style=\"color: green;\">WYSIWYG Text</span> Editor directive for AngularJS! textAngular is a super cool <span style=\"color: red;\">WYSIWYG Text</span> Editor directive for AngularJS!</p>!","http://localhost:81/images/file_sq9p2vcggae9crtv0vlbimgm91.jpg",Area.SCREEN);
    }
}
