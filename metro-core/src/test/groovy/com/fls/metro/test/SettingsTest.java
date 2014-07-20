package com.fls.metro.test;

import com.fls.metro.core.data.domain.*;
import com.fls.metro.core.data.dto.content.Content;
import com.fls.metro.core.data.dto.schedule.ScheduleType;
import com.fls.metro.core.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 30.04.14
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */

public class SettingsTest extends CoreTest {

    @Autowired
    BannerSettingsService bannerSettingsService;

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

    private ScheduleType sType=ScheduleType.MESSAGE;
    private Im im1;
    private Im im2;
    private Station station;
    private Lobby lobby;
    private Line line;
    private Schema schema;




    @Test
    public void testSettings() throws Exception {
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

        assertNull(settingsService.get(ObjectType.IM, im1.getId()));

        Settings s=new Settings();
        s.setEmergencyNumber("123");
        s.setScreensaverTimeoutSec(60);
        s.setOwnerId(schema.getId());
        s.setOwnerType(ObjectType.SCHEMA);

        schema.setSettings(s);
        schemaService.update(schema);

        assertNotNull(settingsService.get(ObjectType.IM, im1.getId()));

        List<Content> im1Content=contentService.get(im1.getImName());
        Content im1Rus=getRusContent(im1Content);
        assertNotNull(im1Rus.getStaticContent().getScreensaverTimeoutSec());
        assertEquals(60,im1Rus.getStaticContent().getScreensaverTimeoutSec().intValue());
        assertTrue(im1Rus.getStaticContent().getScreensaverEnabled());

        schema.getSettings().setScreensaverTimeoutSec(null);
        schemaService.update(schema);

        im1Content=contentService.get(im1.getImName());
        im1Rus=getRusContent(im1Content);
        assertNotNull(im1Rus.getStaticContent().getScreensaverTimeoutSec());
        assertEquals(0,im1Rus.getStaticContent().getScreensaverTimeoutSec().intValue());
        assertFalse(im1Rus.getStaticContent().getScreensaverEnabled());



    }
}
