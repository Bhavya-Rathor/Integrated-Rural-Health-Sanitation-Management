package com.irhsm.waterservice.dto;

import com.irhsm.waterservice.entity.PublicHealthIssue;

public record PendingItemDto(
        String source, Long id, String title, String village,
        String status, String priority, String date
) {
    public static PendingItemDto from(PublicHealthIssue i) {
        return new PendingItemDto(
                "water",
                i.getId(),
                i.getTitle(),
                i.getVillage(),
                i.getStatus().name(),
                i.getPriority().name(),
                i.getReportedOn() != null ? i.getReportedOn().toString() : null
        );
    }
}