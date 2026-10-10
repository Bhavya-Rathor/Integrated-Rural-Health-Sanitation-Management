package com.irhsn.medicine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// @Configuration = this class sets things up for Spring.
@Configuration
public class AppConfig {

    // @Bean = "Spring, create one RestTemplate and give it to
    // any class that asks for it." Made once, shared everywhere.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}