package com.irhsm.healthCampService.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

// Data from the "Register resident" form on the camp detail page
@Getter
@Setter
public class BeneficiaryForm {
    @NotNull(message = "Resident ID is required")
    private Long residentId;          // ID in the Resident service

    @NotBlank(message = "Name is required")
    private String residentName;

    private String village;           // blank = same village as the camp

    @Min(value = 0, message = "Age must be 0-120")
    @Max(value = 120, message = "Age must be 0-120")
    private Integer age;

    private String gender;
}
