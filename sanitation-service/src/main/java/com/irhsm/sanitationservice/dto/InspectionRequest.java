package com.irhsm.sanitationservice.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record InspectionRequest(
        @NotNull @PastOrPresent LocalDate inspectionDate,
        @NotBlank String inspectorName,
        @NotNull @Min(1) @Max(5) Integer cleanlinessScore,
        @NotNull Boolean waterAvailable,
        String remarks
) {}