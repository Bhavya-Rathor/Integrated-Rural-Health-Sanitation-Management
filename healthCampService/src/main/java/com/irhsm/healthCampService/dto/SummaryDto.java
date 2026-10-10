package com.irhsm.healthCampService.dto;

// Counts for the dashboard tile. "total" and "pending" are the two every service sends.
public record SummaryDto(long total, long pending, long upcoming, long screened, long abnormal) { }
