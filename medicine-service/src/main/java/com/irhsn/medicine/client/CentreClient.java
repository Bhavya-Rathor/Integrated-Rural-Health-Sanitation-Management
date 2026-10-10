package com.irhsn.medicine.client;

import com.irhsn.medicine.dto.CentreDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

// @Component = "Spring, create one of these at startup
// and give it to any class that needs it."
@Component
public class CentreClient {

    // A logger prints messages to the console (like System.out, but better).
    private static final Logger log = LoggerFactory.getLogger(CentreClient.class);

    private final RestTemplate restTemplate; // the HTTP tool from AppConfig
    private final String baseUrl;            // http://localhost:8082

    // Spring calls this constructor and passes in:
    // 1) the RestTemplate created in AppConfig
    // 2) centre.service.url read from application.properties (@Value)
    public CentreClient(RestTemplate restTemplate,
                        @Value("${centre.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    // ===== JOB 1: get one centre =====
    // Calls GET http://localhost:8082/api/centres/2
    // and turns the JSON answer into a CentreDto.
    public CentreDto getCentre(Long centreId) {
        try {
            CentreDto centre = restTemplate.getForObject(
                    baseUrl + "/api/centres/" + centreId, CentreDto.class);
            if (centre != null) {
                return centre;
            }
        } catch (RestClientException e) {
            // Bhavya's service is down or returned an error.
            // Write a warning, but DON'T crash.
            log.warn("Health Centre service unavailable: {}", e.getMessage());
        }
        // Fallback: a placeholder centre, so my service keeps working.
        return new CentreDto(centreId, "Centre " + centreId, "Unknown");
    }

    // ===== JOB 2: get all centres =====
    // Calls GET http://localhost:8082/api/centres
    // The answer is a JSON list, so I read it as an array, then turn it into a List.
    public List<CentreDto> getAllCentres() {
        try {
            CentreDto[] centres = restTemplate.getForObject(
                    baseUrl + "/api/centres", CentreDto[].class);
            if (centres != null) {
                return Arrays.asList(centres);
            }
        } catch (RestClientException e) {
            log.warn("Health Centre service unavailable: {}", e.getMessage());
        }
        // Fallback: an empty list (the dropdown will just be empty).
        return List.of();
    }
}