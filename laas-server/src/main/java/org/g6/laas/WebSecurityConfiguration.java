package org.g6.laas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public EvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/controllers/login").permitAll()
                .antMatchers("/controllers/register").permitAll()
                .antMatchers(HttpMethod.GET, "/", "/themes/**", "/public/**", "/styles/**", "/fonts/**", "/images/**", "/scripts/**").permitAll()
                .antMatchers("/controllers/logout").permitAll()
                .antMatchers("/api/v1/jobs/**").authenticated()
                .antMatchers("/api/v1/notifications/**").authenticated()
                .antMatchers("/controllers/jobs/**").authenticated();
    }
}
