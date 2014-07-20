package com.fls.metro.core.exception;

/**
 * Created with IntelliJ IDEA.
 * User: PGrebenyuk
 * Date: 30.04.14
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
public class MediaContentGenerationException extends ServerException {

    public MediaContentGenerationException(String message) {
        super("media.content.generation", message);
    }
}
