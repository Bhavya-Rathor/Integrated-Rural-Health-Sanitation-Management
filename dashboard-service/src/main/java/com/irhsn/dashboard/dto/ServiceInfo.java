package com.irhsn.dashboard.dto;

// One service the dashboard talks to.
// key     = short name used in forms, e.g. "medicines"
// label   = name shown on screen
// baseUrl = e.g. http://localhost:8084
// apiPath = e.g. /api/medicines
// approveStatus / rejectStatus = the status words THIS service accepts
public record ServiceInfo(
        String key,
        String label,
        String baseUrl,
        String apiPath,
        String approveStatus,
        String rejectStatus
) {
}