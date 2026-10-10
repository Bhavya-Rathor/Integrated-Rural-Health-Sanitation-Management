package com.irhsm.sanitationservice.entity;


import com.irhsm.sanitationservice.enums.FacilityCondition;
import com.irhsm.sanitationservice.enums.FacilityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity            // this class = a table
@Table(name = "sanitation_facility")
@Getter @Setter
@NoArgsConstructor
public class SanitationFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Type is Required")
    @Enumerated(EnumType.STRING)
    private FacilityType type;

    @NotBlank(message = "Village is Required")
    private String village;

    private String locationDesc;

    @NotNull(message = "Capicity is Required")
    @Min(value = 1, message = "Capicity must be al least 1")
    private Integer capacity;

    @NotNull(message = "Condition is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "facility_condition")
    private FacilityCondition condition;

    @PastOrPresent(message = "Last cleaned data cannot be in the Future")
    private LocalDate lastCleanedOn;

    private String caretakerName;
}
