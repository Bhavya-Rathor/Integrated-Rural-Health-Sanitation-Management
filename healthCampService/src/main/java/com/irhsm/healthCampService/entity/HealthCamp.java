package com.irhsm.healthCampService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity 
@Table(name = "health_camp")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor  

// @Getter @Setter (Lombok) generate getName(), setName() and the rest at compile time.
// @NoArgsConstructor is required by JPA, which needs an empty constructor to create objects from rows.

public class HealthCamp {
    @Id 
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Camp name is required")
    private String name;

    @NotNull (message = "Camp type is required")
    @Enumerated (EnumType.STRING)
    private CampType campType;

    @NotBlank(message = "Village is required")
    private String village;

    @NotBlank(message = "Venue is required")
    private String venue;

    @NotNull(message = "Camp data is required")
    private LocalDate campDate;

    private LocalTime startTime;
    private LocalTime endTime;

    private Long centreId;

    private Integer targetCount;

// @Enumerated(EnumType.STRING) stores "PLANNED" as text. Without it, JPA stores 0, 1, 2…, which breaks if enum is ever reordered
    @Enumerated (EnumType.STRING)
    private CampStatus status = CampStatus.PLANNED; //gives new camps a default status

}
