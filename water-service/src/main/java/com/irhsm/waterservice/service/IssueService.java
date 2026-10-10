package com.irhsm.waterservice.service;

import com.irhsm.waterservice.client.SanitationClient;
import com.irhsm.waterservice.dto.CorrectiveActionRequest;
import com.irhsm.waterservice.entity.CorrectiveAction;
import com.irhsm.waterservice.entity.PublicHealthIssue;
import com.irhsm.waterservice.entity.WaterQualityTest;
import com.irhsm.waterservice.entity.WaterSource;
import com.irhsm.waterservice.enums.ActionStatus;
import com.irhsm.waterservice.enums.Priority;
import com.irhsm.waterservice.enums.WorkStatus;
import com.irhsm.waterservice.repository.CorrectiveActionRepository;
import com.irhsm.waterservice.repository.PublicHealthIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final PublicHealthIssueRepository issueRepository;
    private final CorrectiveActionRepository actionRepository;
    private final SanitationClient sanitationClient;

    public List<PublicHealthIssue> findAll() {
        return issueRepository.findAllByOrderByReportedOnDesc();
    }

    public PublicHealthIssue findById(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Issue " + id + " not found"));
    }

    public List<PublicHealthIssue> findOpen() {
        return issueRepository.findByStatusNotOrderByReportedOnDesc(WorkStatus.RESOLVED);
    }

    public long countAll()  { return issueRepository.count(); }
    public long countOpen() { return issueRepository.countByStatusNot(WorkStatus.RESOLVED); }

    public List<CorrectiveAction> findActions(Long issueId) {
        return actionRepository.findByIssueIdOrderByActionDateDesc(issueId);
    }

    // Called automatically when a test comes back UNSAFE
    @Transactional
    public PublicHealthIssue openFromUnsafeTest(WaterQualityTest test) {
        WaterSource source = test.getSource();

        PublicHealthIssue issue = new PublicHealthIssue();
        issue.setSource(source);
        issue.setTest(test);
        issue.setTitle("Unsafe water at " + source.getName());
        issue.setDescription("Failed: " + test.getFailedParameters());
        issue.setVillage(source.getVillage());
        // Coliform means faecal contamination: the most dangerous case
        issue.setPriority(Boolean.TRUE.equals(test.getColiformPresent()) ? Priority.HIGH : Priority.MEDIUM);
        issue.setStatus(WorkStatus.OPEN);
        issue.setReportedOn(LocalDate.now());

        // REST call to the Sanitation service: link a broken facility in the same village
        sanitationClient.findFacilityNeedingRepair(source.getVillage()).ifPresent(f -> {
            issue.setNearbyFacilityId(f.id());
            issue.setNearbyFacilityName(f.name());
        });

        return issueRepository.save(issue);
    }

    @Transactional
    public PublicHealthIssue updateStatus(Long id, String statusText, String remarks) {
        WorkStatus newStatus;
        try {
            newStatus = WorkStatus.valueOf(statusText.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown status: " + statusText);
        }
        PublicHealthIssue issue = findById(id);
        issue.setStatus(newStatus);
        if (remarks != null && !remarks.isBlank()) {
            issue.setOfficerRemarks(remarks);
        }
        issue.setResolvedOn(newStatus == WorkStatus.RESOLVED ? LocalDate.now() : null);
        return issueRepository.save(issue);
    }

    @Transactional
    public CorrectiveAction addAction(Long issueId, CorrectiveActionRequest req) {
        PublicHealthIssue issue = findById(issueId);

        CorrectiveAction action = new CorrectiveAction();
        action.setIssue(issue);
        action.setActionType(req.actionType());
        action.setDescription(req.description());
        action.setAssignedTo(req.assignedTo());
        action.setActionDate(req.actionDate());
        action.setStatus(ActionStatus.PLANNED);

        // Rule: once action is planned, the issue is being worked on
        if (issue.getStatus() == WorkStatus.OPEN || issue.getStatus() == WorkStatus.ASSIGNED) {
            issue.setStatus(WorkStatus.IN_PROGRESS);
            issueRepository.save(issue);
        }
        return actionRepository.save(action);
    }

    public List<PublicHealthIssue> search(WorkStatus status) {
        return (status == null) ? findAll() : issueRepository.findByStatusOrderByReportedOnDesc(status);
    }
}