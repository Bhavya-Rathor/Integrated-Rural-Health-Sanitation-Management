package com.irhsm.healthCampService.dto;

import com.irhsm.healthCampService.entity.ScreeningResult;
import com.irhsm.healthCampService.entity.ScreeningType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// Data from the "Record screening" form on the camp detail page
@Getter
@Setter
public class ScreeningForm {
    @NotNull(message = "Choose a resident")
    private Long beneficiaryId;

    @NotNull(message = "Choose a screening type")
    private ScreeningType screeningType;

    @NotBlank(message = "Reading is required")
    private String reading;           // e.g. "152/96 mmHg"

    @NotNull(message = "Choose a result")
    private ScreeningResult result;

    private String screenedBy;
    private String remarks;
}
