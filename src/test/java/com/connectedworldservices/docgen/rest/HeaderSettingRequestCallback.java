package com.connectedworldservices.docgen.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;

import java.io.IOException;


public class HeaderSettingRequestCallback implements RequestCallback {

    @Override
    public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {
        final HttpHeaders clientHeaders = clientHttpRequest.getHeaders();
        clientHeaders.add("Accept", "application/pdf");
    }
}
