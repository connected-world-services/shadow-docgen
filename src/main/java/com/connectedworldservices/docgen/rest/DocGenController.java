package com.connectedworldservices.docgen.rest;

import com.connectedworldservices.docgen.exception.ResponseException;
import com.connectedworldservices.docgen.generator.PDFGenerator;
import com.connectedworldservices.docgen.service.AppEngineService;
import com.connectedworldservices.docgen.template.FreeMarkerTemplateResolver;
import com.connectedworldservices.docgen.utils.LoggerUtils;
import com.connectedworldservices.docgen.utils.VersionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@Slf4j
public class DocGenController {

    @Autowired
    AppEngineService appEngine;

    @Autowired
    FreeMarkerTemplateResolver templateResolver;

    @Autowired
    PDFGenerator pdfGenerator;


    @Value("${docgen.version}")
    String docGenVersion;

    private ObjectMapper om = new ObjectMapper();

    @RequestMapping(value="/document/version",
            method = RequestMethod.GET,
            produces ="application/json")
    ResponseEntity<String> version() {
        try {
            return ResponseEntity.ok(om.writeValueAsString(VersionUtils.lookupExecutableWithVersion(DocGenController.class, docGenVersion)));
        } catch (JsonProcessingException e) {
            throw Throwables.propagate(e);
        }
    }

    @RequestMapping(value = "/document/{cwsRef}", method = RequestMethod.GET, produces = {"application/pdf"})
    public ResponseEntity<InputStreamResource> document(@PathVariable String cwsRef)  {
        try {
            LoggerUtils.auditInfo(log, "request", cwsRef, ()-> "");
            String html = templateResolver.toHtml(appEngine.getState(cwsRef));
            InputStream document = pdfGenerator.generatePdfFromHtmlWithImages(html);
            LoggerUtils.auditInfo(log, "response", cwsRef, ()-> "Document Generated");
            return ResponseEntity.ok()
                    .headers(httpHeaders())
                    .body(new InputStreamResource(document));
        } catch (ResponseException ex) {
            LoggerUtils.auditError(log, "exception", cwsRef, () -> ex.getMessage());
            return new ResponseEntity<>(ex.getHttpStatus());
        }
    }

    private HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("X-Content-Type-Options", "nosniff");
        return headers;
    }
}
