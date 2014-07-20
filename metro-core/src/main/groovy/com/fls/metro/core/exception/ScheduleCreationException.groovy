package com.fls.metro.core.exception

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 05.06.14
 * Time: 15:01
 * To change this template use File | Settings | File Templates.
 */
public class ScheduleCreationException extends ServerException {

    ScheduleCreationException(){
        super("schedule.creation.error");
    }
}
