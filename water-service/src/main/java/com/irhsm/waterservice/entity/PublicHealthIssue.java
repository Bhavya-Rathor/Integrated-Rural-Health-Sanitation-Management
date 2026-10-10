package com.irhsm.waterservice.entity;

import com.irhsm.waterservice.enums.Priority;
import com.irhsm.waterservice.enums.WorkStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "public_health_issue")
@Getter
@Setter
@NoArgsConstructor
public class PublicHealthIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_id")
    private WaterSource source;

    @ManyToOne
    @JoinColumn(name = "test_id")      // the test that raised this issue (nullable)
    private WaterQualityTest test;

    private String title;

    @Column(length = 1000)
    private String description;

    private String village;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private WorkStatus status;

    private LocalDate reportedOn;
    private LocalDate resolvedOn;

    @Column(length = 500)
    private String officerRemarks;

    // From the Sanitation service. ID ONLY: a different database, so no @ManyToOne.
    private Long nearbyFacilityId;
    private String nearbyFacilityName; // copy of the name, so pages don't need another call
}