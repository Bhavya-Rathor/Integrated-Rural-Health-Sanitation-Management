package com.irhsm.waterservice.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

// What a field tester submits from a Field Test Kit
public record WaterTestRequest(
        @NotNull Long sourceId,
        @NotNull @PastOrPresent LocalDate testDate,
        @NotBlank String testedBy,
        @NotNull @DecimalMin("0.0") @DecimalMax("14.0") Double ph,
        @NotNull @PositiveOrZero Double tds,
        @PositiveOrZero Double residualChlorine,      // optional: groundwater is often not chlorinated
        @NotNull @PositiveOrZero Double turbidity,
        @NotNull Boolean coliformPresent
) {}