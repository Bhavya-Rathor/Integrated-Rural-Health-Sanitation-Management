package com.irhsn.medicine.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// Body for POST /api/medicines/requests
// e.g. { "medicineId": 1, "centreId": 2, "requestedQty": 200, "remarks": "Stock almost over" }
public record ManualRequestDto(
        @NotNull Long medicineId,
        @NotNull Long centreId,
        @NotNull @Positive Integer requestedQty,
        String remarks
) {
}