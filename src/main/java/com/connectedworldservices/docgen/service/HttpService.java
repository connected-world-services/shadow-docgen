package com.connectedworldservices.docgen.service;


import com.google.common.base.Throwables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpService {
    @Autowired
    RestTemplate restTemplate;
    /**
     *  Executes HTTP GET request.
     * @param url
     * @param headers
     * @param responseType
     * @param <T>
     * @param <V>
     * @return
     */
    public <T, V> T executeGet(String url, Map<String, String> headers, Class<T> responseType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "application/json");
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.setAll(headers);

        return executeRequest(url, HttpMethod.GET, new HttpEntity<V>(httpHeaders), responseType);
    }
    private <T, V> T executeRequest(String url, HttpMethod httpMethod, HttpEntity<V> entity, Class<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, httpMethod, entity, responseType);
            return response.getBody();
        } catch (Exception rethrow) {
            throw Throwables.propagate(rethrow);
        }
    }

}
