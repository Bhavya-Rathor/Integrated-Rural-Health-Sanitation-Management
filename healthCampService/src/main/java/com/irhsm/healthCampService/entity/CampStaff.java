package com.irhsm.healthCampService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "camp_staff")
@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 

public class CampStaff {  // One object of this class = one row in camp_staff
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    // Many staff rows belong to one camp, creates the camp_id foreign key column
    @ManyToOne 
    @JoinColumn (name = "camp_id", nullable = false)
    private HealthCamp camp;  // In Java we hold the whole camp object; the DB stores only its id

    // Form validation: a worker must be chosen
    // ID of the health worker in Bhavya's Health Centre service (no foreign key)
    @NotNull (message = "Worker is required")
    private Long workerId;

    // Worker's name copied from the Centre service, so pages work if it is down
    private String workerName;

    @NotNull (message = "Role is required")
    @Enumerated(EnumType.STRING)  // Store the enum as text ("DOCTOR"), not as a number
    private StaffRole role;

    private String duty;
}
