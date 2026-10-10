package com.irhsm.waterservice.dto;

// The part of Sanitation's facility JSON that Water needs. Extra JSON fields are ignored.
public record FacilityDto(Long id, String name, String village, String condition) {}