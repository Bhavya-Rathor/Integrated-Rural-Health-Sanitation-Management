package com.irhsm.healthCampService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity 
@Table (
    name = "camp_beneficiary",
    uniqueConstraints = @UniqueConstraint (
        columnNames = {"camp_id", "resident_id"} 
        // this combination must be unique So the same resident cannot register twice for one camp
    )
)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 

public class CampBeneficiary {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // Many beneficiaries belong to ONE camp
    @JoinColumn(name = "camp_id", nullable=false)
    private HealthCamp camp;  // The camp this resident is registered for

    @Column(name = "resident_id", nullable = false)
    private Long residentId;  // ID of the resident in Tvisha's Resident service (plain number, no FK)

    private String residentName;  // Copied from the Resident service at registration
    private String village;
    private Integer age;
    private String gender;

    @Enumerated (EnumType.STRING)
    private Attendance attendance = Attendance.REGISTERED;

    private LocalDateTime registeredAt;

    // JPA calls this method automatically just before the first INSERT
    // Set the registration time to the current date and time
    @PrePersist 
    void onCreate(){
        registeredAt = LocalDateTime.now();
    }
}
