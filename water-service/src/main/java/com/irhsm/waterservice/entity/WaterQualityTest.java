package com.irhsm.waterservice.entity;

import com.irhsm.waterservice.enums.TestResult;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "water_quality_test")
@Getter
@Setter
@NoArgsConstructor
public class WaterQualityTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_id")
    private WaterSource source;

    private LocalDate testDate;
    private String testedBy;           // e.g. "FTK - Anganwadi worker Selvi"

    private Double ph;
    private Double tds;                // mg/L
    private Double residualChlorine;   // mg/L, only for chlorinated supply
    private Double turbidity;          // NTU
    private Boolean coliformPresent;

    @Enumerated(EnumType.STRING)
    private TestResult result;         // decided by the service, never typed by the user

    private String failedParameters;   // e.g. "TDS 910.0 mg/L, Coliform present"
}