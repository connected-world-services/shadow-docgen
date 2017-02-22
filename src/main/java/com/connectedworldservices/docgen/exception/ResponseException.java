package com.connectedworldservices.docgen.exception;


import org.springframework.http.HttpStatus;

public abstract class ResponseException extends RuntimeException{

    public ResponseException(Throwable cause) {
        super(cause);
    }
    public abstract HttpStatus getHttpStatus();
}
