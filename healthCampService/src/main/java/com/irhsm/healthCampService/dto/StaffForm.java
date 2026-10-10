package com.irhsm.healthCampService.dto;

import com.irhsm.healthCampService.entity.StaffRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// Data from the "Add staff" form on the camp detail page
@Getter
@Setter
public class StaffForm {
    @NotNull(message = "Worker ID is required")
    private Long workerId;            // ID in the Health Centre service

    @NotBlank(message = "Worker name is required")
    private String workerName;

    @NotNull(message = "Role is required")
    private StaffRole role;

    private String duty;
}
