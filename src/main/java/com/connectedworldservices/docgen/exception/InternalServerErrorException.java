package com.connectedworldservices.docgen.exception;

import org.springframework.http.HttpStatus;


public class InternalServerErrorException extends ResponseException {

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
