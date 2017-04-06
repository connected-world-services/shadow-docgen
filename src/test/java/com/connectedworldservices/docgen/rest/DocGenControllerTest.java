package com.connectedworldservices.docgen.rest;

import com.connectedworldservices.docgen.template.FreeMarkerTemplateResolver;
import com.connectedworldservices.docgen.template.TemplateResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class DocGenControllerTest{

    @Value("${local.server.port}")
    protected int port;
    protected final static String DOCUMENT_MAPPING = "/document";
    protected final static RestTemplate REST_TEMPLATE = new RestTemplate();
    @Autowired
    TemplateResolver templateResolver;
    @Autowired
    FreeMarkerTemplateResolver freeMarkerTemplateResolver;

    private static final WireMockServer wireMockServer =
            new WireMockServer(wireMockConfig().port(8089).bindAddress("localhost"));


    @BeforeClass
    public static void startServer() {
        wireMockServer.start();
        configureFor("localhost", 8089);
    }

    @AfterClass
    public static void stopServer() {
        wireMockServer.stop();
    }

    protected String uriBuilder(String... paths) {
        String uri = "http://localhost:" + port + DOCUMENT_MAPPING;
        for (String path : paths) {
            uri += "/" + path;
        }
        return uri;
    }

    private static RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy= new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
        requestFactory.setProxy(proxy);
        return new RestTemplate(requestFactory);
    }

    @Test
    public void testGetDocumentWhenAppEngineNotAvailable() {
        stopServer();
        HttpServerErrorException ex = null;
        try {
            HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback();
            REST_TEMPLATE.execute(uriBuilder("123"), HttpMethod.GET, requestCallback, response -> response);
        } catch (HttpServerErrorException e) {
            ex = e;
        }
        assertNotNull(ex);
        assertEquals("503 Service Unavailable", ex.getMessage());
        startServer();
    }

    @Test
    public void testGetDocumentWhenStateServiceNotAvailable() {
        givenThat(get(urlEqualTo("/api"))
                .willReturn(
                        aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(MockResponses.WITHOUT_STATE_SERVICE)));
        HttpServerErrorException ex = null;
        try {
            HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback();
            REST_TEMPLATE.execute(uriBuilder("123"), HttpMethod.GET, requestCallback, response -> response);
        } catch (HttpServerErrorException e) {
            ex = e;
        }
        assertNotNull(ex);
        assertEquals("503 Service Unavailable", ex.getMessage());
    }

    @Test
    public void testGetDocumentPositiveScenario() {
        givenThat(get(urlEqualTo("/api"))
                .willReturn(
                        aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(MockResponses.WITH_STATE_SERVICE)));

        givenThat(get(urlEqualTo("/api/state?cws-ref=123"))
                .willReturn(
                        aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(MockResponses.STATE_DATA)));
        try {
            HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback();
            ClientHttpResponse res = REST_TEMPLATE.execute(uriBuilder("123"), HttpMethod.GET, requestCallback, response -> response);
            File targetFile = new File("target/response.pdf");
            FileUtils.copyInputStreamToFile(res.getBody(), targetFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetDocumentForResourcePositiveScenario() {
        givenThat(get(urlEqualTo("/api"))
                .willReturn(
                        aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(MockResponses.WITH_STATE_SERVICE)));

        givenThat(get(urlEqualTo("/api/123/new-connection/docgen"))
                .willReturn(
                        aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(MockResponses.STATE_DATA)));
        try {
            HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback();
            ClientHttpResponse res = REST_TEMPLATE.execute(uriBuilder("123/new-connection/docgen"), HttpMethod.GET, requestCallback, response -> response);
            File targetFile = new File("target/responseUsingResource.pdf");
            FileUtils.copyInputStreamToFile(res.getBody(), targetFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testToHtml() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> state = mapper.readValue(MockResponses.STATE_DATA, new TypeReference<Map<String, Object>>(){});
        String value = templateResolver.toHtml(state);
        assertTrue(value.contains("Amount : 32"));
        System.out.println(value);
    }

    @Test
    public void testFreeMarkerTemplateToHtml() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> state = mapper.readValue(MockResponses.STATE_DATA, new TypeReference<Map<String, Object>>(){});
        String value = freeMarkerTemplateResolver.toHtml(state);
        assertTrue(value.contains("Amount : 32"));
        assertTrue(value.contains("Double Amount : 64"));
        assertTrue(value.contains("Checking if value exists: N/A"));
        System.out.println(value);
    }
}
