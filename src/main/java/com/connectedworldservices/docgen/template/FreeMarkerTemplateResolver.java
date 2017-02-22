package com.connectedworldservices.docgen.template;


import com.connectedworldservices.docgen.exception.InternalServerErrorException;
import com.connectedworldservices.docgen.utils.LoggerUtils;
import com.google.common.io.CharStreams;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@Component
public class FreeMarkerTemplateResolver {
    @Value("${docgen.template.html.location}")
    private String templateLocation;

    private Template template;
    @Autowired
    protected ResourceLoader resourceLoader;

    public String toHtml(Map<String, Object> state) {
        try {
            StringWriter stringWriter = new StringWriter();
            template.process(state, stringWriter);
            return stringWriter.toString();
        } catch (TemplateException | IOException e) {
            LoggerUtils.auditError(log, "resolveTemplate", "", ()->e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    @PostConstruct
    public void initFreeMarkerTemplate() {
        try {
            Resource resource = resourceLoader.getResource(templateLocation);
            String htmlTemplate = CharStreams.toString(new InputStreamReader(resource.getInputStream()));
            template = new Template("htmlTemplate", new StringReader(htmlTemplate), new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
        } catch (IOException e) {
            LoggerUtils.auditError(log, "exception", "", () -> "HTML template location not configured correctly.");
            throw new InternalServerErrorException(e);
        }
    }
}
