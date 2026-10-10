package com.irhsm.Resident.dto;

public record PendingItemDto(String source, Long id, String title, String village,
                             String status, String priority, String date) {
}