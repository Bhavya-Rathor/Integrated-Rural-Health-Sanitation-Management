package com.irhsm.waterservice.client;

import com.irhsm.waterservice.dto.FacilityDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class SanitationClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    // Written by hand (not @RequiredArgsConstructor) because of the @Value parameter
    public SanitationClient(RestTemplate restTemplate,
                            @Value("${sanitation.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    // Asks Sanitation for facilities in the village and returns the first one that is not FUNCTIONAL
    public Optional<FacilityDto> findFacilityNeedingRepair(String village) {
        String url = baseUrl + "/api/sanitation/facilities?village={village}";
        try {
            FacilityDto[] facilities = restTemplate.getForObject(url, FacilityDto[].class, village);
            if (facilities == null) {
                return Optional.empty();
            }
            return Arrays.stream(facilities)
                    .filter(f -> !"FUNCTIONAL".equals(f.condition()))
                    .findFirst();
        } catch (RestClientException e) {
            // Sanitation down or unreachable: log it and carry on without the link
            log.warn("Sanitation service unavailable: {}", e.getMessage());
            return Optional.empty();
        }
    }
}