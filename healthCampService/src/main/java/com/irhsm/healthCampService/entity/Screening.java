package com.irhsm.healthCampService.entity;

import jakarta.persistence.*;                             
import jakarta.validation.constraints.NotBlank;          
import jakarta.validation.constraints.NotNull;           
import lombok.AllArgsConstructor;                         
import lombok.Getter;                                     
import lombok.NoArgsConstructor;                          
import lombok.Setter;                                     

import java.time.LocalDateTime; 

@Entity 
@Table(name = "screening")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 

public class Screening {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Many screenings belong to ONE beneficiary
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private CampBeneficiary beneficiary; // Who was screened

    @NotNull (message = "Screening type is required")
    @Enumerated(EnumType.STRING)
    private ScreeningType screeningType;  // BP, BLOOD_SUGAR, BMI, EYE, ORAL, HAEMOGLOBIN

    @NotBlank(message= "Reading is required")
    private String reading;    // 152/96 mmHg,10 mg/dL

    @NotNull(message = "Result is required")
    @Enumerated (EnumType.STRING)
    private ScreeningResult result;

    private String screenedBy;
    private String remarks;

    private LocalDateTime screenedAt;

    @PrePersist 
    void onCreate(){
        screenedAt = LocalDateTime.now();
    }

}
