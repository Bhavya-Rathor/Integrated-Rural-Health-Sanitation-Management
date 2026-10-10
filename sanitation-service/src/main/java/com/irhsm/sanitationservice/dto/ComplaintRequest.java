package com.irhsm.sanitationservice.dto;

import com.irhsm.sanitationservice.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// What the client sends to create a complaint: just the facility's id, not the whole facility object
public record ComplaintRequest(
        @NotNull Long facilityId,
        @NotBlank String title,
        String description,
        @NotBlank String reportedBy,
        @NotNull Priority priority
) {}