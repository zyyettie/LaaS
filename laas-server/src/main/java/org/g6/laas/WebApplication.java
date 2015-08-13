package org.g6.laas;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.profiles("prod").sources(WebApplication.class);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder().profiles("dev")
                .sources(WebApplication.class).run(args);
    }

}
