package com.irhsm.sanitationservice.dto;

import com.irhsm.sanitationservice.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Backs the HTML form. Mutable (getters + setters) so th:field can bind to it.
@Getter
@Setter
@NoArgsConstructor
public class ComplaintForm {

    @NotNull(message = "Choose a facility")
    private Long facilityId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Reporter name is required")
    private String reportedBy;

    @NotNull(message = "Choose a priority")
    private Priority priority;

    // Hand the same data to the service in the shape it already accepts
    public ComplaintRequest toRequest() {
        return new ComplaintRequest(facilityId, title, description, reportedBy, priority);
    }
}