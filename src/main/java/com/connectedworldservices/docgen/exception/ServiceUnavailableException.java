package com.connectedworldservices.docgen.exception;


import org.springframework.http.HttpStatus;


public class ServiceUnavailableException extends ResponseException {

    public ServiceUnavailableException(Throwable ex) {
        super(ex);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }
}
