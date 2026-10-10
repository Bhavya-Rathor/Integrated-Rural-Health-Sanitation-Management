package com.irhsm.sanitationservice.dto;

import jakarta.validation.constraints.NotBlank;

public record StatusUpdateRequest(@NotBlank String status, String remarks) {}