package com.irhsm.HealthCentreService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "health_worker")
@Getter
@Setter
@NoArgsConstructor

public class HealthWorker{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank 
    private String name;

    @Enumerated (EnumType.STRING)
    private WorkerRole role;

    private String qualification;

    private String phone;

    private boolean active;

    @ManyToOne 
    @JoinColumn (name = "centre_id")
    private HealthCentre centre;
    
}