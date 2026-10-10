package com.irhsm.sanitationservice.dto;

import com.irhsm.sanitationservice.entity.Complaint;

// The shared team format for /pending. The 7 field names must match exactly.
public record PendingItemDto(
        String source,
        Long id,
        String title,
        String village,
        String status,
        String priority,
        String date
) {
    public static PendingItemDto from(Complaint c) {
        return new PendingItemDto(
                "sanitation",
                c.getId(),
                c.getTitle(),
                c.getFacility().getVillage(),
                c.getStatus().name(),
                c.getPriority().name(),
                c.getReportedOn() != null ? c.getReportedOn().toString() : null   // "2026-10-10"
        );
    }
}