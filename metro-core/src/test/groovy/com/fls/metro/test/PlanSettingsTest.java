package com.fls.metro.test;

import com.fls.metro.core.data.domain.*;
import com.fls.metro.core.data.dto.content.Content;
import com.fls.metro.core.data.dto.content.StationPlanInfoType;
import com.fls.metro.core.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 30.04.14
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */

public class PlanSettingsTest extends CoreTest {

    @Autowired
    StationPlanSettingsService planSettingsService;

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
    public void testPlanSettings() throws Exception {
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

        StationPlanSettings stationPs=new StationPlanSettings();
        stationPs.setOwnerType(ObjectType.STATION);
        stationPs.setOwnerId(station.getId());
        stationPs.setItems(new ArrayList<StationPlanSettingsItem>());

        StationPlanSettingsItem sTransportItem=new StationPlanSettingsItem();
        sTransportItem.setType(StationPlanInfoType.TRANSPORT);
        sTransportItem.setRusText("Транспорт: автобус 1,2\nТроллейбус 5");
        sTransportItem.setEngText("Bus 1,2\n");

        StationPlanSettingsItem sInfoItem=new StationPlanSettingsItem();
        sInfoItem.setType(StationPlanInfoType.INFO);
        sInfoItem.setRusText("Переход закрывается в 0:45");
        sInfoItem.setEngText("Passage closes at 0:45");

        stationPs.getItems().add(sTransportItem);
        stationPs.getItems().add(sInfoItem);
        stationPs=planSettingsService.create(stationPs);

        StationPlanSettings sps=planSettingsService.get(ObjectType.STATION, station.getId());
        assertNotNull(sps);
        assertNotNull(sps.getItems());
        assertEquals(2, sps.getItems().size());

        stationPs.setEngUrl("eng url");
        stationPs.setRusUrl("rus url");

        planSettingsService.update(stationPs);

        sps=planSettingsService.get(ObjectType.STATION, station.getId());
        assertNotNull(sps);
        assertEquals("rus url", sps.getRusUrl());

        List<Content> im1Content=contentService.get(im1.getImName());
        Content im1Rus=getRusContent(im1Content);
        assertNotNull(im1Rus.getStationPlan());
        assertEquals(1,im1Rus.getStationPlan().getItems().size());
        assertEquals(sInfoItem.getRusText(),im1Rus.getStationPlan().getInfo());

        assertEquals(sTransportItem.getRusText(),im1Rus.getStationPlan().getItems().get(0).getText());


        Content im1En=getEngContent(im1Content);
        assertNotNull(im1En.getStationPlan());
        assertEquals(1,im1En.getStationPlan().getItems().size());
        assertEquals(sInfoItem.getEngText(),im1En.getStationPlan().getInfo());

        assertEquals(sTransportItem.getEngText(),im1En.getStationPlan().getItems().get(0).getText());


        planSettingsService.delete(ObjectType.STATION, station.getId());





    }
}
