package com.irhsn.dashboard.service;

import com.irhsn.dashboard.client.ServiceClient;
import com.irhsn.dashboard.config.ServiceDirectory;
import com.irhsn.dashboard.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Dashboard rules: build tiles, merge + sort pending items, filter,
// and send the officer's decision to the right service.
@Service
public class DashboardService {

    private final ServiceDirectory directory;
    private final ServiceClient client;

    public DashboardService(ServiceDirectory directory, ServiceClient client) {
        this.directory = directory;
        this.client = client;
    }

    // 1. One tile per service
    public List<ServiceTile> getTiles() {
        List<ServiceTile> tiles = new ArrayList<>();
        for (ServiceInfo s : directory.all()) {
            Map<String, Object> counts = client.getSummary(s);
            tiles.add(new ServiceTile(s.key(), s.label(), counts != null,
                    counts == null ? Map.of() : counts));
        }
        return tiles;
    }

    // 2. All services' pending items in ONE list.
    //    Sorted: HIGH, then MEDIUM, then LOW; same priority -> oldest first.
    public List<PendingRow> getAllPending() {
        List<PendingRow> rows = new ArrayList<>();
        for (ServiceInfo s : directory.all()) {
            for (PendingItemDto item : client.getPending(s)) {
                rows.add(new PendingRow(s, item));
            }
        }
        rows.sort(Comparator
                .comparingInt((PendingRow r) -> priorityRank(r.item().priority()))
                .thenComparing(r -> r.item().date() == null ? "" : r.item().date()));
        return rows;
    }

    // 3. Filter by village and/or service (blank = no filter)
    public List<PendingRow> filter(List<PendingRow> rows, String village, String serviceKey) {
        return rows.stream()
                .filter(r -> village == null || village.isBlank()
                        || village.equalsIgnoreCase(r.item().village()))
                .filter(r -> serviceKey == null || serviceKey.isBlank()
                        || serviceKey.equals(r.service().key()))
                .toList();
    }

    // Village names for the filter dropdown
    public List<String> villages(List<PendingRow> rows) {
        return rows.stream().map(r -> r.item().village())
                .filter(Objects::nonNull).distinct().sorted().toList();
    }

    // 4. Officer clicked Approve/Reject. null = success, otherwise error text.
    public String decide(String serviceKey, Long id, boolean approve, String remarks) {
        ServiceInfo s = directory.find(serviceKey);
        if (s == null) {
            return "Unknown service: " + serviceKey;
        }
        String status = approve ? s.approveStatus() : s.rejectStatus();
        return client.updateStatus(s, id, new StatusUpdateDto(status, remarks));
    }

    public List<ServiceInfo> services() {
        return directory.all();
    }

    // HIGH=0, MEDIUM=1, LOW=2, anything else=3 (last)
    private int priorityRank(String priority) {
        if (priority == null) return 3;
        return switch (priority.toUpperCase()) {
            case "HIGH" -> 0;
            case "MEDIUM" -> 1;
            case "LOW" -> 2;
            default -> 3;
        };
    }
}