package com.adena.edhukanuserservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ControllerSecurityConfig {

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> {
            try {
                requests.anyRequest().permitAll()
                        .and().cors().disable()
                        .csrf().disable();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return http.build();
    }
}