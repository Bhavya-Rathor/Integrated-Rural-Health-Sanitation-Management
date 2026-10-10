package com.irhsn.dashboard.client;

import com.irhsn.dashboard.dto.PendingItemDto;
import com.irhsn.dashboard.dto.ServiceInfo;
import com.irhsn.dashboard.dto.StatusUpdateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// Talks to the other services over HTTP.
// If a service is down, we never crash: we return null / empty list
// and the page shows "Unavailable".
@Component
public class ServiceClient {

    // Logger = prints messages in the IntelliJ console
    private static final Logger log = LoggerFactory.getLogger(ServiceClient.class);

    private final RestTemplate restTemplate;

    public ServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // GET {baseUrl}{apiPath}/summary  e.g. http://localhost:8084/api/medicines/summary
    // Returns the JSON as a Map, or null if the service is down.
    public Map<String, Object> getSummary(ServiceInfo service) {
        String url = service.baseUrl() + service.apiPath() + "/summary";
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = restTemplate.getForObject(url, Map.class);
            return result;
        } catch (RestClientException e) {
            log.warn("Summary failed for {} -> {} : {}", service.key(), url, e.getMessage());
            return null;
        }
    }

    // GET {baseUrl}{apiPath}/pending  e.g. http://localhost:8084/api/medicines/pending
    // Returns the list of pending items, or an empty list if the service is down.
    public List<PendingItemDto> getPending(ServiceInfo service) {
        String url = service.baseUrl() + service.apiPath() + "/pending";
        try {
            PendingItemDto[] items = restTemplate.getForObject(url, PendingItemDto[].class);
            return items == null ? List.of() : Arrays.asList(items);
        } catch (RestClientException e) {
            log.warn("Pending failed for {} -> {} : {}", service.key(), url, e.getMessage());
            return List.of();
        }
    }

    // PUT {baseUrl}{apiPath}/{id}/status  with body {"status":"APPROVED","remarks":"..."}
    // Returns null if it worked, or an error message to show on the page.
    public String updateStatus(ServiceInfo service, Long id, StatusUpdateDto body) {
        String url = service.baseUrl() + service.apiPath() + "/" + id + "/status";
        try {
            restTemplate.put(url, body);
            return null; // success
        } catch (HttpStatusCodeException e) {
            // The service answered, but with an error (400, 404, 409...)
            log.warn("Status update failed for {} -> {} : {} {}", service.key(), url,
                    e.getStatusCode(), e.getResponseBodyAsString());
            return service.label() + " refused: " + e.getResponseBodyAsString();
        } catch (RestClientException e) {
            // The service did not answer at all (down / timeout)
            log.warn("Status update failed for {} -> {} : {}", service.key(), url, e.getMessage());
            return service.label() + " is not reachable right now.";
        }
    }
}