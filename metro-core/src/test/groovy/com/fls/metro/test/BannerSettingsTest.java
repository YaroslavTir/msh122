package com.fls.metro.test;

import com.fls.metro.core.data.domain.*;
import com.fls.metro.core.data.dto.content.Area;
import com.fls.metro.core.data.dto.content.BannerMessage;
import com.fls.metro.core.data.dto.content.ContentItem;
import com.fls.metro.core.data.dto.schedule.Schedule;
import com.fls.metro.core.data.dto.schedule.ScheduleType;
import com.fls.metro.core.data.dto.schedule.ScheduledContent;
import com.fls.metro.core.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 30.04.14
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */

public class BannerSettingsTest extends CoreTest {

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
    ScheduleService scheduleService;

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
    public void testBannerSettings() throws Exception {
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


        ScheduledContent text1=new ScheduledContent();
        text1.setStartTime("10:00");
        text1.setEndTime("12:00");
        text1.setContent(textOnBgcolor("Good morning"));
        text1.setContentExt(mediaContentService.generateMediaContent(text1.getContent(), Area.BANNER));

        ScheduledContent text2=new ScheduledContent();
        text2.setStartTime("12:00");
        text2.setEndTime("14:00");
        text2.setContent(image("Good afternoon!"));
        //text2.setContentExt(mediaContentService.generateMediaContent(text2.getContent(), Area.SCREEN));

        List<ScheduledContent> content=new ArrayList<>();
        content.add(text1);
        content.add(text2);

        Schedule s=new Schedule();
        s.setContent(content);

        BannerSettings bs1=im1.getBannerSettings();
        bs1.setSchedule(Collections.singletonList(s));

        List<ContentItem> videos1=new ArrayList<ContentItem>();
        ContentItem v1=new ContentItem();
        v1.setContent(video("http://imv1"));
        videos1.add(v1);
        bs1.setVideos(videos1);
        bs1.setOwn(true);

        im1.setBannerSettings(bs1);
        imService.update(im1);

        ScheduledContent stationText=new ScheduledContent();
        stationText.setStartTime("8:00");
        stationText.setEndTime("11:00");
        stationText.setContent(fileText("http://file1", "Station closed!"));
        stationText.setContentExt(mediaContentService.generateMediaContent(stationText.getContent(), Area.BANNER));
        s=new Schedule();
        s.setContent(Collections.singletonList(stationText));

        BannerSettings bsStation=station.getBannerSettings();
        bsStation.setSchedule(Collections.singletonList(s));

        List<ContentItem> stationVideos=new ArrayList<ContentItem>();
        ContentItem sv1=new ContentItem();
        sv1.setContent(video("http://sv1"));
        stationVideos.add(sv1);

        ContentItem sv2=new ContentItem();
        sv2.setContent(video("http://sv2"));
        stationVideos.add(sv2);
        bsStation.setVideos(stationVideos);
        bsStation.setOwn(true);

        station.setBannerSettings(bsStation);
        stationService.update(station);



        im1=imService.get(im1.getId());
        assertNotNull(im1.getBannerSettings());
        assertEquals(1, im1.getBannerSettings().getVideos().size());
        List<Schedule> schIm1= im1.getBannerSettings().getSchedule();
        assertNotNull(schIm1);
        assertEquals(1, schIm1.size());
        assertEquals(2, schIm1.get(0).getContent().size());

        station=stationService.get(station.getId());
        assertNotNull(station.getBannerSettings());
        assertEquals(2, station.getBannerSettings().getVideos().size());

        List<Schedule> schStation= station.getBannerSettings().getSchedule();
        assertNotNull(schStation);
        assertEquals(1, schStation.size());
        assertEquals(1, schStation.get(0).getContent().size());

        SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yy HH:mm");
        Date dt=sdf.parse("30.04.2014 8:00");
        Collection<String> imsToUpdate= scheduleService.getImNamesToRefresh(dt);
        assertEquals(2, imsToUpdate.size());

        com.fls.metro.core.data.dto.content.BannerVideo im1bv=contentService.getBannerVideoList(im1);
        assertEquals(im1bv.getContent().size(), 1);

        com.fls.metro.core.data.dto.content.BannerVideo im2bv=contentService.getBannerVideoList(im2);
        assertEquals(im2bv.getContent().size(), 2);

        BannerMessage bm=contentService.getCurrentBannerMessage(im1, dt);
        assertNull(bm.getContent());

        bm=contentService.getCurrentBannerMessage(im2,dt);
        assertNotNull(bm.getContent());
        assertEquals(stationText.getContentExt().getFileUrl(), bm.getContent().getFileUrl());

        dt=sdf.parse("30.04.2014 9:00");
        imsToUpdate= scheduleService.getImNamesToRefresh(dt);
        assertEquals(0, imsToUpdate.size());

        bm=contentService.getCurrentBannerMessage(im1, dt);
        assertNull(bm.getContent());


        bm=contentService.getCurrentBannerMessage(im2, dt);
        assertNotNull(bm.getContent());
        assertEquals(stationText.getContentExt().getFileUrl(), bm.getContent().getFileUrl());


        dt=sdf.parse("30.04.2004 10:11");
        bm=contentService.getCurrentBannerMessage(im1, dt);
        assertNotNull(bm.getContent());
        assertEquals(text1.getContentExt().getFileUrl(), bm.getContent().getFileUrl());

        bm=contentService.getCurrentBannerMessage(im2, dt);
        assertEquals(stationText.getContentExt().getFileUrl(), bm.getContent().getFileUrl());

        dt=sdf.parse("30.04.2004 12:00");
        imsToUpdate= scheduleService.getImNamesToRefresh(dt);
        assertEquals(1, imsToUpdate.size());
        assertTrue(imsToUpdate.contains(im1.getImName()));

        bm=contentService.getCurrentBannerMessage(im1, dt);
        assertEquals(text2.getContent().getFileUrl(), bm.getContent().getFileUrl());

        bm=contentService.getCurrentBannerMessage(im2, dt);
        assertNull(bm.getContent());

        im1.getBannerSettings().setOwn(false);
        imService.update(im1);

        schIm1= scheduleService.getScheduleFor(ObjectType.IM, im1.getId(), ScheduleType.BANNER);
        assertNotNull(schIm1);
        assertEquals(0, schIm1.size());

        im1=imService.get(im1.getId());
        assertEquals(2, im1.getBannerSettings().getVideos().size());
        assertEquals(1, im1.getBannerSettings().getSchedule().size());



    }
}
