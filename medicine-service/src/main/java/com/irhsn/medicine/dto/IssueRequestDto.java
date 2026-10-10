package com.irhsn.medicine.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// The JSON body for POST /api/medicines/issue.
// A "record" = a small data-only class. Java auto-creates the
// constructor and getters: medicineId(), centreId(), quantity()...
// (note: record getters have NO "get" in front)
public record IssueRequestDto(
        @NotNull Long medicineId,          // which medicine
        @NotNull Long centreId,            // at which centre
        @NotNull @Positive Integer quantity, // how many units (> 0)
        Long residentId,                   // which patient (Tvisha's ID)
        Long appointmentId,                // which appointment (Bhavya's ID), may be null
        String issuedBy                    // pharmacist / health worker name
) {
}