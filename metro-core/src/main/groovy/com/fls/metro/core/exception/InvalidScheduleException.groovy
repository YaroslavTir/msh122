package com.fls.metro.core.exception

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 05.06.14
 * Time: 15:00
 * To change this template use File | Settings | File Templates.
 */
public class InvalidScheduleException extends ServerException {

    public InvalidScheduleException(String message) {
        super("invalid.schedule", message);
    }
}
