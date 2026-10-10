package com.irhsn.dashboard.dto;

// One pending action, exactly as every service returns it from /pending.
// date is text ("2026-10-07") so any service's date is accepted.
public record PendingItemDto(
        String source,
        Long id,
        String title,
        String village,
        String status,
        String priority,
        String date
) {
}