package com.fls.metro.api.dto

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response

/**
 * User: NFadin
 * Date: 09.06.2014
 * Time: 14:13
 */
class SimpleDateParam {
    private final DateTime date;

    public SimpleDateParam(String timestamp) throws WebApplicationException {
        try {
            this.date = new DateTime(Long.valueOf(timestamp))
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException(
                    Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity("Couldn't parse date: " + date + " (" + e.getMessage() + ")")
                            .build()
            );
        }
    }

    public DateTime getDate() {
        return date;
    }
}
