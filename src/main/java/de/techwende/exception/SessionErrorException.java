package de.techwende.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SessionErrorException extends Exception {

    public SessionErrorException(String message) {
        super(message);
        LOG.error(message);
    }
}