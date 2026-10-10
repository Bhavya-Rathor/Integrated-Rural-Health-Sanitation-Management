package com.irhsm.HealthCentreService.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "facility")
@Getter
@Setter
@NoArgsConstructor

public class Facility{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated (EnumType.STRING)
    private FacilityStatus status;

    @Enumerated (EnumType.STRING)
    private Priority priority;

    private String remarks;

    private LocalDate lastupdated;

    @ManyToOne
    @JoinColumn(name = "centre_id")  
    private HealthCentre centre;

}