package com.connectedworldservices.docgen.config;

import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyConfig {


    @Value("${ssl.key-store}")
    private String keystoreFile;
    @Value("${ssl.key-store-password}")
    private String keystorePass;
    @Value("${ssl.key-password}")
    private String keyPassword;
    @Value("${ssl.port:0}")
    private Integer sslPort;

    @Bean
    public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory() {

        final JettyEmbeddedServletContainerFactory factory =  new JettyEmbeddedServletContainerFactory();

        if (isSslConfigured()) {
            factory.addServerCustomizers((JettyServerCustomizer) server -> {

                SslContextFactory sslContextFactory = new SslContextFactory();
                sslContextFactory.setKeyStorePassword(keystorePass);
                sslContextFactory.setKeyStorePath(keystoreFile);
                sslContextFactory.setKeyManagerPassword(keyPassword);

                final NetworkTrafficServerConnector connector = new NetworkTrafficServerConnector(server, sslContextFactory);
                connector.setPort(Integer.valueOf(sslPort));
                server.addConnector(connector);
            });
        }
        return factory;
    }

    private boolean isSslConfigured() {
        return  ! "ssl.key-store".equals(keystoreFile) &&
                ! "ssl.key-store-password".equals(keystorePass) &&
                ! "ssl.key-password".equals(keyPassword) &&
                ! sslPort.equals(0);
    }

}
