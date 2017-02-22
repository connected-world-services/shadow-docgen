package com.connectedworldservices.docgen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableConfigurationProperties
@SpringBootApplication
@EnableSwagger2
public class DocumentGenerator {

    public static void main(String[] args) {
        SpringApplication.run(DocumentGenerator.class, args);
    }
}
