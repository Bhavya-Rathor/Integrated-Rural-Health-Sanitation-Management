package com.irhsm.waterservice.entity;

import com.irhsm.waterservice.enums.SourceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "water_source")
@Getter
@Setter
@NoArgsConstructor
public class WaterSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;                       // e.g. "Gudiyatham Handpump HP-03"

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    private SourceType type;

    @NotBlank(message = "Village is required")
    private String village;

    private String locationDesc;

    @Min(value = 0, message = "Households cannot be negative")
    private Integer householdsServed;

    @NotNull(message = "Say whether it is working")
    private Boolean functional;
}