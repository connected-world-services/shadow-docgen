package com.connectedworldservices.docgen.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class HttpConfig {

    @Value("${docgen.http.max.connections:100}")
    private int maxConnTotal;

    @Value("${docgen.http.max.connections.per.route}")
    private int maxConnPerRoute;

    @Value("${docgen.http.connection.timetolive.millis}")
    private long connectionTimeToLive;

    @Value("${docgen.http.request.connection.timeout.millis}")
    private int requestConnTimeOut;

    @Value("${docgen.http.connect.timeout.millis}")
    private int connectTimeOut;


    public CloseableHttpClient httpClient() {
        HttpClientBuilder builder = HttpClientBuilder.create()
                .setMaxConnTotal(maxConnTotal)
                .setMaxConnPerRoute(maxConnPerRoute)
                .setDefaultRequestConfig(requestConfig())
                .setConnectionTimeToLive(connectionTimeToLive, TimeUnit.MILLISECONDS)
                .evictExpiredConnections()
                .evictIdleConnections(connectionTimeToLive, TimeUnit.MILLISECONDS);
        return builder.build();
    }

    private RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(connectTimeOut)
                .setConnectionRequestTimeout(requestConnTimeOut).build();
    }

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate(requestFactory());
        template.setErrorHandler(new DefaultResponseErrorHandler());
        return template;
    }

    public ClientHttpRequestFactory requestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }
}
