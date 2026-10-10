package com.irhsn.dashboard.dto;

// A pending item + the service that owns it,
// so the Approve button knows WHERE to send the update.
public record PendingRow(ServiceInfo service, PendingItemDto item) {
}