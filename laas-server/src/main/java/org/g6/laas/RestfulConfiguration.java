package org.g6.laas;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Import(RepositoryRestMvcConfiguration.class)
public class RestfulConfiguration extends RepositoryRestMvcConfiguration {

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        try {
            config.setReturnBodyOnCreate(true);
            config.setReturnBodyOnUpdate(true);
            config.setBaseUri(new URI("/api/v1"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
