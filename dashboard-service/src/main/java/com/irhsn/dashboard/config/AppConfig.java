package com.irhsn.dashboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

// Setup class: creates ONE RestTemplate (the HTTP "phone") for the whole app.
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(@Value("${services.timeout-ms}") long timeoutMs) {
        // Give up if a service does not connect or answer within timeoutMs,
        // so one stuck service cannot freeze the dashboard.
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(timeoutMs));
        factory.setReadTimeout(Duration.ofMillis(timeoutMs));
        return new RestTemplate(factory);
    }
}