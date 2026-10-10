package com.irhsm.sanitationservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cleanliness_inspection")
@Getter
@Setter
@NoArgsConstructor
public class CleanlinessInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "facility_id")
    private SanitationFacility facility;

    @NotNull
    @PastOrPresent
    private LocalDate inspectionDate;

    @NotBlank
    private String inspectorName;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer cleanlinessScore;   // 1 = very dirty, 5 = very clean

    @NotNull
    private Boolean waterAvailable;

    @Column(length = 500)
    private String remarks;
}