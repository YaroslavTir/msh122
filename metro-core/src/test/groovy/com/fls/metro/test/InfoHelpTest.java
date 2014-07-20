package com.fls.metro.test;

import com.fls.metro.core.data.domain.*;
import com.fls.metro.core.data.dto.content.Content;
import com.fls.metro.core.data.dto.content.HelpMediaData;
import com.fls.metro.core.data.dto.content.Language;
import com.fls.metro.core.data.dto.content.help.HelpMenuItemDataType;
import com.fls.metro.core.data.dto.content.help.HelpMenuItemMedia;
import com.fls.metro.core.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 30.04.14
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */

public class InfoHelpTest extends CoreTest {

    @Autowired
    HelpInfoService helpInfoService;

    @Autowired
    ImService imService;

    @Autowired
    LobbyService lobbyService;

    @Autowired
    StationService stationService;

    @Autowired
    LineService lineService;

    @Autowired
    SchemaService schemaService;

    @Autowired
    MediaContentService mediaContentService;

    @Autowired
    SettingsService settingsService;

    @Autowired
    ContentService contentService;

    @Override
    public void clean(){
        if(im1!=null){
            imService.delete(im1.getId());
            im1=null;
        }
        if(im2!=null){
            imService.delete(im2.getId());
            im2=null;
        }
        if(lobby!=null){
            lobbyService.delete(lobby.getId());
            lobby=null;
        }
        if(station!=null){
            stationService.delete(station.getId());
            station=null;
        }
        if(line!=null){
            lineService.delete(line.getId());
            line=null;
        }
    }

    private Im im1;
    private Im im2;
    private Station station;
    private Lobby lobby;
    private Line line;
    private Schema schema;




    @Test
    public void testInfoHelp() throws Exception {
        schema=new Schema();
        schema.setName(genUnique("schema"));
        schema=schemaService.create(schema);

        line=new Line();
        line.setName(genUnique("line"));
        line.setNumber((int)System.currentTimeMillis()%1000);
        line.setSchemaId(schema.getId());
        line=lineService.createLine(line);


        station=new Station();
        station.setName(genUnique("Glavnaya"));
        station.setEnname(station.getName());
        station.setLineId(line.getId());
        station=stationService.create(station);

        lobby=new Lobby();
        lobby.setName(genUnique("Osnovnoy"));
        lobby.setStationId(station.getId());
        lobby=lobbyService.create(lobby);

        im1=new Im();
        im1.setName(genUnique("im1"));
        im1.setImName(im1.getName());
        im1.setIp("0.0.0.1");
        im1.setPosition("left corner");
        im1.setLobbyId(lobby.getId());
        im1=imService.create(im1);
        assertNotNull(im1);
        assertNotNull(im1.getId());

        im2=new Im();
        im2.setName(genUnique("im2"));
        im2.setImName(im2.getName());
        im2.setIp("0.0.0.2");
        im2.setPosition("right corner");
        im2.setLobbyId(lobby.getId());
        im2=imService.create(im2);

        im1=imService.get(im1.getId());
        im2=imService.get(im2.getId());
        lobby=lobbyService.get(lobby.getId());
        station=stationService.get(station.getId());

        HelpInfo hi=new HelpInfo();
        hi.setName("name");
        hi.setTitle("title");
        hi.setObjectType(ObjectType.IM);
        hi.setLanguage(Language.RU);
        hi.setHelpInfoType(HelpMenuItemDataType.MEDIA);
        hi.setUpdateDate(new Date());
        hi.setVideo(new ArrayList<HelpMediaData>());
        HelpMediaData vd1=new HelpMediaData();
        vd1.setTitle("video 1");
        vd1.setLink("url 1");
        hi.getAudio().add(vd1);

        helpInfoService.processAndSave(Collections.singletonList(hi), ObjectType.IM, im1.getId());

        List<HelpInfo> helpIm1=helpInfoService.getTotal(ObjectType.IM, im1.getId());
        assertTrue(helpIm1.size()==1);
        assertNotNull(helpIm1.get(0).getMedia());
        assertEquals(1, helpIm1.get(0).getMedia().size());
        assertEquals(vd1.getLink(), helpIm1.get(0).getMedia().get(0).getLink());
        assertEquals(vd1.getTitle(), helpIm1.get(0).getMedia().get(0).getTitle());



        List<Content> im1Content=contentService.get(im1.getImName());
        Content im1Rus=getRusContent(im1Content);
        assertNotNull(im1Rus.getHelp());
        assertEquals(1,im1Rus.getHelp().getItems().size());
        assertTrue(im1Rus.getHelp().getItems().get(0) instanceof HelpMenuItemMedia);
        HelpMenuItemMedia hm1=(HelpMenuItemMedia) im1Rus.getHelp().getItems().get(0);
        assertEquals(1, hm1.getData().getMedia().size());
        assertEquals(vd1.getLink(), hm1.getData().getMedia().get(0).getLink());
        assertEquals(vd1.getTitle(), hm1.getData().getMedia().get(0).getTitle());



        List<HelpInfo> empty=Collections.emptyList();
        helpInfoService.processAndSave(empty, ObjectType.IM, im1.getId());






    }
}
