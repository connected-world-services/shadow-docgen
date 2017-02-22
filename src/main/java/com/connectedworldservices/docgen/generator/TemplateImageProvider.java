package com.connectedworldservices.docgen.generator;

import com.connectedworldservices.docgen.exception.InternalServerErrorException;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class TemplateImageProvider extends AbstractImageProvider{

    @Value("${docgen.template.html.images.folder}")
    private String imagesPath;
    @Autowired
    ResourceLoader resourceLoader;

    @Override
    public String getImageRootPath() {
        try {
            Resource resource = resourceLoader.getResource(imagesPath);
            return resource.getURL().getPath();
        } catch (Exception ex) {
            throw new InternalServerErrorException(ex);
        }
    }
}
