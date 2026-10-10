package com.irhsn.dashboard.dto;

// Body sent on Approve/Reject: { "status": "APPROVED", "remarks": "..." }
public record StatusUpdateDto(String status, String remarks) {
}