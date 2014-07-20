package com.fls.metro.test;

import com.fls.metro.core.data.domain.*;
import com.fls.metro.core.data.dto.content.Area;
import com.fls.metro.core.data.dto.content.Content;
import com.fls.metro.core.data.dto.content.MessageType;
import com.fls.metro.core.data.dto.schedule.Schedule;
import com.fls.metro.core.data.dto.schedule.ScheduleType;
import com.fls.metro.core.data.dto.schedule.ScheduledContent;
import com.fls.metro.core.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 30.04.14
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */

public class MessagesTest extends CoreTest {

    @Autowired
    MessageService messageService;

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
    public void testMessages() throws Exception {
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

        ImmediateMessage imStation=new ImmediateMessage();
        imStation.setOwnerId(station.getId());
        imStation.setOwnerType(ObjectType.STATION);
        imStation.setContent(image("Station text"));
        //imStation.setContentExt(mediaContentService.generateMediaContent(imStation.getContent(), Area.SCREEN));

        imStation=messageService.saveMessage(imStation);

        ImmediateMessage im1Im1=new ImmediateMessage();
        im1Im1.setOwnerId(im1.getId());
        im1Im1.setOwnerType(ObjectType.IM);
        im1Im1.setContent(textOnBgcolor("IM1 text1"));
        im1Im1.setContentExt(mediaContentService.generateMediaContent(im1Im1.getContent(), Area.SCREEN));

        im1Im1=messageService.saveMessage(im1Im1);

        ImmediateMessage im1Im2=new ImmediateMessage();
        im1Im2.setOwnerId(im1.getId());
        im1Im2.setOwnerType(ObjectType.IM);
        im1Im2.setContent(video("IM1 video url"));
        //im1Im2.setContentExt(mediaContentService.generateMediaContent(im1Im2.getContent(), Area.SCREEN));
        im1Im2=messageService.saveMessage(im1Im2);

        List<ImmediateMessage> im1Messages=messageService.getImmediateMessages(ObjectType.IM, im1.getId());
        assertEquals(2, im1Messages.size());

        List<ImmediateMessage> im2Messages=messageService.getImmediateMessages(ObjectType.IM, im2.getId());
        assertEquals(0, im2Messages.size());

        List<ImmediateMessage> stationMessages=messageService.getImmediateMessages(ObjectType.STATION, station.getId());
        assertEquals(1, stationMessages.size());

        ScheduledContent text1=new ScheduledContent();
        text1.setStartTime("00:00");
        text1.setEndTime("12:00");
        text1.setContent(textOnBgcolor("Good morning"));
        text1.setContentExt(mediaContentService.generateMediaContent(text1.getContent(), Area.BANNER));

        ScheduledContent text2=new ScheduledContent();
        text2.setStartTime("12:00");
        text2.setEndTime("23:59");
        text2.setContent(video("video"));
        text2.setContentExt(mediaContentService.generateMediaContent(text2.getContent(), Area.BANNER));

        List<ScheduledContent> content=new ArrayList<>();
        content.add(text1);
        content.add(text2);

        Schedule s=new Schedule();
        s.setContent(content);
        MessageSchedule im1Schedule=new MessageSchedule();
        im1Schedule.setOwnerType(ObjectType.IM);
        im1Schedule.setOwnerId(im1.getId());
        im1Schedule.setSchedule(Collections.singletonList(s));
        messageService.createMessageSchedule(im1Schedule);

        List<Content> im1Content=contentService.get(im1.getImName());
        assertEquals(2, im1Content.size());
        Content im1Rus=getRusContent(im1Content);
        assertNotNull(im1Rus.getMessage());
        assertEquals(MessageType.SCH, im1Rus.getMessage().getType());


        boolean play=messageService.playMessage(im1Im1.getId());
        assertTrue(play);

        play=messageService.playMessage(im1Im2.getId());
        assertFalse(play);

        play=messageService.playMessage(imStation.getId());
        assertTrue(play);

        ImmediateMessage im1playing=messageService.getPlayingMessage(ObjectType.IM, im1.getId());
        assertEquals(im1Im1.getId(), im1playing.getId());

        im1Content=contentService.get(im1.getImName());
        im1Rus=getRusContent(im1Content);
        assertNotNull(im1Rus.getMessage());
        assertEquals(MessageType.IMM, im1Rus.getMessage().getType());

        ImmediateMessage im2playing=messageService.getPlayingMessage(ObjectType.IM, im2.getId());
        assertEquals(imStation.getId(), im2playing.getId());

        messageService.stopMessage(imStation.getId());
        im2playing=messageService.getPlayingMessage(ObjectType.IM, im2.getId());
        assertNull(im2playing);

        play=messageService.playMessage(imStation.getId());
        assertTrue(play);

        ImmediateMessage stationPlaying=messageService.getPlayingMessage(ObjectType.STATION, station.getId());
        assertEquals(imStation.getId(), stationPlaying.getId());

        im2playing=messageService.getPlayingMessage(ObjectType.IM, im2.getId());
        assertEquals(imStation.getId(), im2playing.getId());

        messageService.stopMessage(im1Im1.getId());
        im1playing=messageService.getPlayingMessage(ObjectType.IM, im1.getId());
        assertEquals(imStation.getId(), im1playing.getId());

        im1Content=contentService.get(im1.getImName());
        im1Rus=getRusContent(im1Content);
        assertNotNull(im1Rus.getMessage());
        assertEquals(MessageType.IMM, im1Rus.getMessage().getType());


        messageService.stopMessage(stationPlaying.getId());
        im1Content=contentService.get(im1.getImName());
        im1Rus=getRusContent(im1Content);
        assertNotNull(im1Rus.getMessage());
        assertEquals(MessageType.SCH, im1Rus.getMessage().getType());

        play=messageService.playMessage(im1Im2.getId());
        assertTrue(play);

        im1Content=contentService.get(im1.getImName());
        im1Rus=getRusContent(im1Content);
        assertNotNull(im1Rus.getMessage());
        assertEquals(MessageType.IMM, im1Rus.getMessage().getType());

        messageService.stopMessage(im1Im2.getId());
        scheduleService.cleanScheduleFor(ObjectType.IM, im1.getId(), ScheduleType.MESSAGE);

        im1Content=contentService.get(im1.getImName());
        im1Rus=getRusContent(im1Content);
        assertNotNull(im1Rus.getMessage());
        assertNull(im1Rus.getMessage().getType());







    }
}
