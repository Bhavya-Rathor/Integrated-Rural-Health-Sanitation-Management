package com.irhsm.waterservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CorrectiveActionRequest(
        @NotBlank String actionType,
        String description,
        @NotBlank String assignedTo,
        @NotNull LocalDate actionDate
) {}