package com.irhsm.HealthCentreService.entity;

import java.time.LocalTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "health_centre")
@Getter
@Setter
@NoArgsConstructor
public class HealthCentre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CentreType type;

    @NotBlank 
    private String village;

    private String district;

    @Pattern(regexp = "\\d{10}",message = "Phone number must be 10 digits")
    private String phone;

    private LocalTime openingTime;
    private LocalTime closingTime;

    private boolean open24x7;

    @Enumerated (EnumType.STRING)
    private CentreStatus status;
}
