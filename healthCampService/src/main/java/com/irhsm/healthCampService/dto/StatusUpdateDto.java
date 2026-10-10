package com.irhsm.healthCampService.dto;

import jakarta.validation.constraints.NotBlank;

// Body of PUT /api/camps/{id}/status, e.g. {"status":"REFERRED","remarks":"Sent to PHC Katpadi"}
public record StatusUpdateDto(@NotBlank String status, String remarks) { }
