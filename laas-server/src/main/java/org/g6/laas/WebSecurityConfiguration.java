package org.g6.laas;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/controllers/login").permitAll()
                .antMatchers(HttpMethod.GET, "/", "/themes/**", "/public/**", "/styles/**", "/fonts/**", "/images/**", "/scripts/**").permitAll()
                .antMatchers("/controllers/logout").authenticated()
                .antMatchers("/api/v1/jobs/**").authenticated()
                .antMatchers("/controllers/jobs/**").authenticated();
    }
}
