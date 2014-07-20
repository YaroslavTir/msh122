package com.fls.metro.test;

import com.fls.metro.core.data.dao.ImDao;
import com.fls.metro.core.data.dao.LobbyDao;
import com.fls.metro.core.data.dao.StationDao;
import com.fls.metro.core.data.domain.Im;
import com.fls.metro.core.data.domain.Lobby;
import com.fls.metro.core.data.domain.ObjectType;
import com.fls.metro.core.data.domain.Station;
import com.fls.metro.core.data.dto.content.Area;
import com.fls.metro.core.data.dto.content.media.MediaContentInternalType;
import com.fls.metro.core.data.dto.schedule.Schedule;
import com.fls.metro.core.data.dto.schedule.ScheduleType;
import com.fls.metro.core.data.dto.schedule.ScheduledContent;
import com.fls.metro.core.exception.InvalidScheduleException;
import com.fls.metro.core.service.MediaContentService;
import com.fls.metro.core.service.ScheduleService;
import org.joda.time.DateTime;
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

public class ScheduleTest extends CoreTest {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    ImDao imDao;

    @Autowired
    LobbyDao lobbyDao;

    @Autowired
    StationDao stationDao;

    @Autowired
    MediaContentService mediaContentService;

    @Override
    public void clean(){
        if(im1!=null){
            imDao.delete(im1.getId());
            scheduleService.cleanScheduleFor(ObjectType.IM, im1.getId(), sType);
            im1=null;
        }
        if(im2!=null){
            imDao.delete(im2.getId());
            scheduleService.cleanScheduleFor(ObjectType.IM, im2.getId(), sType);
            im2=null;
        }
        if(lobby!=null){
            lobbyDao.delete(lobby.getId());
            scheduleService.cleanScheduleFor(ObjectType.LOBBY, lobby.getId(), sType);
            lobby=null;
        }
        if(station!=null){
            stationDao.delete(station.getId());
            scheduleService.cleanScheduleFor(ObjectType.STATION, station.getId(), sType);
            station=null;
        }
    }

    private ScheduleType sType=ScheduleType.MESSAGE;
    private Im im1;
    private Im im2;
    private Station station;
    private Lobby lobby;


    private void createInfra(){

    }

    @Test
    public void testScheduleCreate() throws Exception {

        station=new Station();
        station.setName(genUnique("Glavnaya"));
        station.setEnname(station.getName());
        station=stationDao.create(station);

        lobby=new Lobby();
        lobby.setName(genUnique("Osnovnoy"));
        lobby.setStationId(station.getId());
        lobby=lobbyDao.create(lobby);

        im1=new Im();
        im1.setName(genUnique("im1"));
        im1.setImName(im1.getName());
        im1.setIp("0.0.0.1");
        im1.setPosition("left corner");
        im1.setLobbyId(lobby.getId());
        im1=imDao.create(im1);
        assertNotNull(im1);
        assertNotNull(im1.getId());

        im2=new Im();
        im2.setName(genUnique("im2"));
        im2.setImName(im2.getName());
        im2.setIp("0.0.0.2");
        im2.setPosition("right corner");
        im2.setLobbyId(lobby.getId());
        im2=imDao.create(im2);


        Schedule s1=new Schedule();
        s1.setContent(new ArrayList<ScheduledContent>());
        ScheduledContent sc1=new ScheduledContent("00:00","12:00");
        sc1.setContent(image("url"));
        s1.getContent().add(sc1);
        Schedule s2=new Schedule();
        s2.setContent(new ArrayList<ScheduledContent>());
        ScheduledContent sc2=new ScheduledContent("00:00","12:00");
        sc2.setContent(video("url2"));
        s2.getContent().add(sc2);
        List s=new ArrayList();
        s.add(s1);
        s.add(s2);

        try{
            scheduleService.create(s, ObjectType.IM, im1.getId(), sType);
            fail();
        }catch(InvalidScheduleException e){
            System.out.println(e.getMessage());
        }

        s2.setStartDate(DateTime.parse("2014-05-01").toDate());

        try{
            scheduleService.create(s, ObjectType.IM, im1.getId(), sType);
            fail();
        }catch(InvalidScheduleException e){
            System.out.println(e.getMessage());
        }

        s2.setEndDate(DateTime.parse("2014-04-30").toDate());

        try{
            scheduleService.create(s, ObjectType.IM, im1.getId(), sType);
            fail();
        }catch(InvalidScheduleException e){
            System.out.println(e.getMessage());
        }

        s2.setEndDate(DateTime.parse("2014-06-01").toDate());

        try{
            scheduleService.create(s, ObjectType.IM, im1.getId(), sType);
            fail();
        }catch(InvalidScheduleException e){
            System.out.println(e.getMessage());
        }

        s1.setEndDate(DateTime.parse("2014-06-01").toDate());
        s2.setStartDate(DateTime.parse("2014-07-01").toDate());
        s2.setEndDate(null);

        scheduleService.create(s, ObjectType.IM, im1.getId(), sType);

        List<Schedule> im1S=scheduleService.getScheduleFor(ObjectType.IM, im1.getId(), sType);
        assertEquals(2, im1S.size());

        scheduleService.cleanScheduleFor(ObjectType.IM, im1.getId(), sType);


        s1.setStartDate(DateTime.parse("2014-04-01").toDate());
        scheduleService.create(s, ObjectType.IM, im1.getId(), sType);

        im1S=scheduleService.getScheduleFor(ObjectType.IM, im1.getId(), sType);
        assertEquals(2, im1S.size());

        s1.setStartDate(DateTime.parse("2014-04-01").toDate());
        s1.setEndDate(DateTime.parse("2014-04-01").toDate());

        s2.setStartDate(DateTime.parse("2014-04-01").toDate());
        s2.setEndDate(DateTime.parse("2014-04-06").toDate());

        try{
            scheduleService.create(s, ObjectType.IM, im1.getId(), sType);
            fail();
        }catch(InvalidScheduleException e){
            System.out.println(e.getMessage());
        }

        s1.setStartDate(DateTime.parse("2014-04-01").toDate());
        s1.setEndDate(DateTime.parse("2014-04-01").toDate());

        s2.setStartDate(DateTime.parse("2014-04-02").toDate());
        s2.setEndDate(DateTime.parse("2014-04-06").toDate());
        scheduleService.create(s, ObjectType.IM, im1.getId(), sType);



        scheduleService.cleanScheduleFor(ObjectType.IM, im1.getId(), sType);

    }

    @Test
    public void testContentCreate() throws Exception {

        station=new Station();
        station.setName(genUnique("Glavnaya"));
        station.setEnname(station.getName());
        station=stationDao.create(station);

        lobby=new Lobby();
        lobby.setName(genUnique("Osnovnoy"));
        lobby.setStationId(station.getId());
        lobby=lobbyDao.create(lobby);

        im1=new Im();
        im1.setName(genUnique("im1"));
        im1.setImName(im1.getName());
        im1.setIp("0.0.0.1");
        im1.setPosition("left corner");
        im1.setLobbyId(lobby.getId());
        im1=imDao.create(im1);
        assertNotNull(im1);
        assertNotNull(im1.getId());

        im2=new Im();
        im2.setName(genUnique("im2"));
        im2.setImName(im2.getName());
        im2.setIp("0.0.0.2");
        im2.setPosition("right corner");
        im2.setLobbyId(lobby.getId());
        im2=imDao.create(im2);

        List<Long> imIds=new ArrayList<>();
        imIds.add(im1.getId());
        imIds.add(im2.getId());
        List<String> imNames=imDao.findImNames(imIds);
        assertEquals(2, imNames.size());



        ScheduledContent text1=new ScheduledContent();
        text1.setStartTime("10:00");
        text1.setEndTime("12:00");
        text1.setContent(textOnBgcolor("Good morning"));
        text1.setContentExt(mediaContentService.generateMediaContent(text1.getContent(), Area.BANNER));

        ScheduledContent text2=new ScheduledContent();
        text2.setStartTime("12:00");
        text2.setEndTime("14:00");
        text2.setContent(textOnBgcolor("Good afternoon!"));
        text2.setContentExt(mediaContentService.generateMediaContent(text2.getContent(), Area.BANNER));

        List<ScheduledContent> content=new ArrayList<>();
        content.add(text1);
        content.add(text2);

        Schedule s=new Schedule();
        s.setContent(content);



        scheduleService.create(Collections.singletonList(s), ObjectType.IM, im1.getId(), sType);

        ScheduledContent stationText=new ScheduledContent();
        stationText.setStartTime("8:00");
        stationText.setEndTime("11:00");
        stationText.setContent(fileText("http://file1", "Station closed!"));
        stationText.setContentExt(mediaContentService.generateMediaContent(stationText.getContent(), null));
        s=new Schedule();
        s.setContent(Collections.singletonList(stationText));

        scheduleService.create(Collections.singletonList(s), ObjectType.STATION, station.getId(), sType);

        List<Schedule> schIm1= scheduleService.getScheduleFor(ObjectType.IM,im1.getId(), sType);
        assertNotNull(schIm1);
        assertEquals(1, schIm1.size());
        assertEquals(2, schIm1.get(0).getContent().size());

        List<Schedule> schStation= scheduleService.getScheduleFor(ObjectType.STATION, station.getId(), sType);
        assertNotNull(schStation);
        assertEquals(1, schStation.size());
        assertEquals(1, schStation.get(0).getContent().size());

        SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yy HH:mm");
        Date dt=sdf.parse("30.04.2014 8:00");
        Collection<String> imsToUpdate= scheduleService.getImNamesToRefresh(dt);
        assertEquals(2, imsToUpdate.size());

        ScheduledContent media= scheduleService.getScheduledContent(im1.getId(), sType, dt);
        assertNull(media.getContentExt());

        media= scheduleService.getScheduledContent(im2.getId(), sType, dt);
        assertNotNull(media.getContentExt());
        assertEquals(stationText.getContent().getInfoText(), media.getContent().getInfoText());


        dt=sdf.parse("30.04.2014 9:00");
        imsToUpdate= scheduleService.getImNamesToRefresh(dt);
        assertEquals(0, imsToUpdate.size());

        media= scheduleService.getScheduledContent(im1.getId(), sType, dt);
        assertNull(media.getContentExt());

        media= scheduleService.getScheduledContent(im2.getId(),sType, dt);
        assertEquals(stationText.getContent().getInfoText(), media.getContent().getInfoText());
        assertNotNull(stationText.getContentExt());

        dt=sdf.parse("30.04.2004 10:11");
        media= scheduleService.getScheduledContent(im1.getId(), sType, dt);
        assertEquals(text1.getContent().getInfoText(), media.getContent().getInfoText());
        assertNotNull(text1.getContentExt());

        media= scheduleService.getScheduledContent(im2.getId(), sType, dt);
        assertEquals(stationText.getContent().getInfoText(), media.getContent().getInfoText());

        dt=sdf.parse("30.04.2004 12:00");
        imsToUpdate= scheduleService.getImNamesToRefresh(dt);
        assertEquals(1, imsToUpdate.size());
        assertTrue(imsToUpdate.contains(im1.getImName()));

        media= scheduleService.getScheduledContent(im1.getId(), sType, dt);
        assertEquals(text2.getContent().getInfoText(), media.getContent().getInfoText());

        media= scheduleService.getScheduledContent(im2.getId(),sType, dt);
        assertNull(media.getContentExt());

        scheduleService.cleanScheduleFor(ObjectType.IM, im1.getId(), sType);
        schIm1= scheduleService.getScheduleFor(ObjectType.IM, im1.getId(), sType);
        assertNotNull(schIm1);
        assertEquals(0, schIm1.size());

        scheduleService.updateScheduleFor(schStation,ObjectType.IM, im2.getId(), sType);

    }

    @Test
    public void testFullSchedule() throws Exception {

        station=new Station();
        station.setName(genUnique("Glavnaya"));
        station.setEnname(station.getName());
        station=stationDao.create(station);

        lobby=new Lobby();
        lobby.setName(genUnique("Osnovnoy"));
        lobby.setStationId(station.getId());
        lobby=lobbyDao.create(lobby);

        im1=new Im();
        im1.setName(genUnique("im1"));
        im1.setImName(im1.getName());
        im1.setIp("0.0.0.1");
        im1.setPosition("left corner");
        im1.setLobbyId(lobby.getId());
        im1=imDao.create(im1);
        assertNotNull(im1);
        assertNotNull(im1.getId());

        im2=new Im();
        im2.setName(genUnique("im2"));
        im2.setImName(im2.getName());
        im2.setIp("0.0.0.2");
        im2.setPosition("right corner");
        im2.setLobbyId(lobby.getId());
        im2=imDao.create(im2);




        ScheduledContent text1=new ScheduledContent();
        text1.setStartTime("10:00");
        text1.setEndTime("12:00");
        text1.setContent(textOnBgcolor("Good morning"));
        text1.setContentExt(mediaContentService.generateMediaContent(text1.getContent(), Area.BANNER));

        ScheduledContent text2=new ScheduledContent();
        text2.setStartTime("12:00");
        text2.setEndTime("14:00");
        text2.setContent(textOnBgcolor("Good afternoon!"));

        List<ScheduledContent> content1=new ArrayList<>();
        content1.add(text1);
        content1.add(text2);

        SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");

        Schedule s1=new Schedule();
        s1.setContent(content1);
        s1.setStartDate(sdf.parse("04.06.2014"));
        s1.setEndDate(sdf.parse("04.06.2014"));

        ScheduledContent text3=new ScheduledContent();
        text3.setStartTime("10:00");
        text3.setEndTime("12:00");
        text3.setContent(image("url"));

        List<ScheduledContent> content2=new ArrayList<>();
        content2.add(text3);

        Schedule s2=new Schedule();
        s2.setContent(content2);
        s2.setStartDate(sdf.parse("05.06.2014"));
        s2.setEndDate(sdf.parse("06.06.2014"));

        List<Schedule> schedule=new ArrayList<>();
        schedule.add(s1);
        schedule.add(s2);




        scheduleService.create(schedule, ObjectType.IM, im1.getId(), sType);


        SimpleDateFormat sdfTime=new SimpleDateFormat("dd.MM.yy HH:mm");
        Date dt=sdfTime.parse("04.06.2014 10:00");
        Collection<String> imsToUpdate= scheduleService.getImNamesToRefresh(dt);
        assertEquals(1, imsToUpdate.size());

        ScheduledContent media= scheduleService.getScheduledContent(im1.getId(), sType, dt);
        assertNotNull(media.getContentExt());
        assertEquals(MediaContentInternalType.IMAGE, media.getContent().getType());
        assertEquals(text1.getContent().getInfoText(), media.getContent().getInfoText());


        dt=sdfTime.parse("04.06.2014 12:05");

        media= scheduleService.getScheduledContent(im1.getId(), sType, dt);
        assertNotNull(media.getContentExt());
        assertEquals(MediaContentInternalType.IMAGE, media.getContent().getType());
        assertEquals(text2.getContent().getInfoText(), media.getContent().getInfoText());


        dt=sdfTime.parse("05.06.2014 10:45");
        media= scheduleService.getScheduledContent(im1.getId(), sType, dt);
        assertNull(media.getContentExt());
        assertEquals(MediaContentInternalType.IMAGE, media.getContent().getType());
        assertEquals(text3.getContent().getFileUrl(), media.getContent().getFileUrl());

        dt=sdfTime.parse("05.06.2014 22:00");
        media= scheduleService.getScheduledContent(im1.getId(), sType, dt);
        assertNull(media);

        dt=sdfTime.parse("06.06.2014 10:45");
        media= scheduleService.getScheduledContent(im1.getId(), sType, dt);
        assertNull(media.getContentExt());
        assertEquals(MediaContentInternalType.IMAGE, media.getContent().getType());
        assertEquals(text3.getContent().getFileUrl(), media.getContent().getFileUrl());

        dt=sdfTime.parse("07.06.2014 22:00");
        media= scheduleService.getScheduledContent(im1.getId(), sType, dt);
        assertNull(media);


    }


}
