package com.connectedworldservices.docgen.template;

import com.connectedworldservices.docgen.exception.InternalServerErrorException;
import com.connectedworldservices.docgen.utils.LoggerUtils;
import com.google.common.io.CharStreams;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TemplateResolver {

    @Value("${docgen.template.html.location}")
    private String templateLocation;
    private static final String PLACEHOLDER_REGEX = "(\\$\\{\\w*(\\.\\w+)*\\})";
    @Getter
    private String htmlTemplate;

    @Autowired
    protected ResourceLoader resourceLoader;

    public String toHtml(Map<String, Object> state) {
        Pattern pattern = Pattern.compile(PLACEHOLDER_REGEX);
        Matcher matcher = pattern.matcher(htmlTemplate);
        String htmlContent = htmlTemplate;
        List<String> placeHolderTokens = new ArrayList<>();
        while (matcher.find()) {
            placeHolderTokens.add(matcher.group());
        }
        for (String token : placeHolderTokens) {
            String jsonPath = token.replace("${", "").replace("}", "");
            String tokenValue = valueAtPath(state, jsonPath);
            htmlContent = htmlContent.replace(token, tokenValue);
        }
        return htmlContent;

    }

    public String valueAtPath(Object json, String jsonPath) {
        try {
            return JsonPath.read(json, jsonPath).toString();
        } catch (Exception ex) {
            return "N/A";
        }
    }

    @PostConstruct
    private void initHtmlTemplate() {
        try {
            Resource resource = resourceLoader.getResource(templateLocation);
            htmlTemplate = CharStreams.toString(new InputStreamReader(resource.getInputStream()));
        } catch (Exception ex) {
            LoggerUtils.auditError(log, "exception", "", () -> "HTML template location not configured correctly.");
            throw new InternalServerErrorException(ex);
        }
    }
}
