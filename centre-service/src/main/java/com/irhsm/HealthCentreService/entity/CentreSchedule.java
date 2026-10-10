package com.irhsm.HealthCentreService.entity;

import java.time.LocalTime;
import java.time.DayOfWeek;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Centre_schedule")
@Getter
@Setter
@NoArgsConstructor

public class CentreSchedule{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated (EnumType.STRING)
    private DayOfWeek dayOfWeek;
    
    @NotNull
    private LocalTime StartTime;

    @NotNull
    private LocalTime EndTime;

    private String service;
    @ManyToOne 
    @JoinColumn (name = "centre_id")
    private HealthCentre centre;
    @ManyToOne 
    @JoinColumn (name = "worker_id")
    private HealthWorker worker;

}