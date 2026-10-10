package com.irhsn.medicine.dto;

import com.irhsn.medicine.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;

// Body for PUT /api/medicines/{id}/status
// e.g. { "status": "APPROVED", "remarks": "Dispatch from district store" }
public record StatusUpdateDto(
        @NotNull RequestStatus status, // must be one of the enum values
        String remarks                 // optional note from the officer
) {
}