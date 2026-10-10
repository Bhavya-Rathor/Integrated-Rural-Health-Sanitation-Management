package com.irhsm.waterservice.entity;

import com.irhsm.waterservice.enums.ActionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "corrective_action")
@Getter
@Setter
@NoArgsConstructor
public class CorrectiveAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "issue_id")
    private PublicHealthIssue issue;

    private String actionType;         // CHLORINATION, PIPE_REPAIR, ALTERNATE_SUPPLY, AWARENESS

    @Column(length = 1000)
    private String description;

    private String assignedTo;
    private LocalDate actionDate;

    @Enumerated(EnumType.STRING)
    private ActionStatus status;
}