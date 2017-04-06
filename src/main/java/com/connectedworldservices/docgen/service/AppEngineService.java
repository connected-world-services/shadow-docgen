package com.connectedworldservices.docgen.service;


import com.connectedworldservices.docgen.exception.ServiceUnavailableException;
import com.connectedworldservices.docgen.utils.LoggerUtils;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class AppEngineService {

    @Value("${docgen.endpoints.appengine}")
    String appengineUrl;
    private static final String APPLICATION_STATE_SERVICE_NAME = "application-state";
    private static final String CWS_REF_PLACEHOLDER = "{cws-ref}";
    private static final String SERVICE_JSON_PATH = "_links.service";
    private static final String NAME = "name";
    private static final String HREF = "href";

    @Lazy
    @Autowired
    private HttpService httpService;

    public Map<String, Object> getState(String cwsRef) {
        Map headers = Collections.emptyMap();
        Map<String, Object> applicationStateService = getApplicationStateService();
        String url = ((String) applicationStateService.get(HREF)).replace(CWS_REF_PLACEHOLDER, cwsRef);
        return httpService.executeGet(url, headers, Map.class);
    }


    public Map<String,Object> getResourceState(String cwsRef, String businessFeatureName, String resourceName) {
        String url = new StringBuilder(appengineUrl).append("/").append(cwsRef).append("/").append(businessFeatureName).append("/").append(resourceName).toString();
        return httpService.executeGet(url, Collections.emptyMap(), Map.class);
    }


    private Map<String, Object> getApplicationStateService() {
        try {
            Map headers = Collections.emptyMap();
            Map<String, Object> stateResponse = httpService.executeGet(appengineUrl, headers, Map.class);
            List<Map<String, Object>> services = JsonPath.read(stateResponse, SERVICE_JSON_PATH);
            Optional<Map<String, Object>> service = services.stream().filter(x -> x.get(NAME).equals(APPLICATION_STATE_SERVICE_NAME)).findFirst();
            return service.get();
        } catch (Exception ex) {
            LoggerUtils.auditError(log, "exception", "", () -> "Appengine service is unavailable or the model does not have the application state service defined");
            throw new ServiceUnavailableException(ex);
        }
    }

}
