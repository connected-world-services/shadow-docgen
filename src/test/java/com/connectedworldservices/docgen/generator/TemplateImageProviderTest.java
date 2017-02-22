package com.connectedworldservices.docgen.generator;


import com.connectedworldservices.docgen.exception.InternalServerErrorException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ResourceLoader;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class TemplateImageProviderTest {

    @Mock
    ResourceLoader resourceLoader;

    @InjectMocks
    TemplateImageProvider templateImageProvider;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        org.springframework.test.util.ReflectionTestUtils.setField(templateImageProvider, "imagesPath", "classpath:html/images");
    }

    @Test(expected = InternalServerErrorException.class)
    public void testNegative() {
        when(resourceLoader.getResource(any())).thenThrow(new NullPointerException());
        templateImageProvider.getImageRootPath();
    }
}
