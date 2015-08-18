package org.g6.laas;

import org.g6.laas.server.database.entity.*;
import org.g6.laas.server.database.entity.user.Notification;
import org.g6.laas.server.database.entity.user.Role;
import org.g6.laas.server.database.entity.user.User;
import org.g6.laas.server.database.entity.user.Inbox;
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
        config.exposeIdsFor(User.class,Role.class, Notification.class,Inbox.class, Category.class, File.class, FileType.class,
                            Product.class, Scenario.class,ScenarioHistory.class,Task.class, TaskHistory.class, Workflow.class);
        try {
            config.setReturnBodyOnCreate(true);
            config.setReturnBodyOnUpdate(true);
            config.setBaseUri(new URI("/api/v1"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
