package com.irhsm.sanitationservice.entity;

import com.irhsm.sanitationservice.enums.Priority;
import com.irhsm.sanitationservice.enums.WorkStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "complaint")
@Getter
@Setter
@NoArgsConstructor
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many complaints belong to one facility.
    // Creates a facility_id foreign-key column in the complaint table.
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "facility_id")
    private SanitationFacility facility;

    @NotBlank
    private String title;               // short line shown on the dashboard

    @Column(length = 1000)
    private String description;

    @NotBlank
    private String reportedBy;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private WorkStatus status;          // set by the service, never by the user

    private LocalDate reportedOn;
    private LocalDate resolvedOn;

    @Column(length = 500)
    private String officerRemarks;      // filled by the dashboard's PUT
}