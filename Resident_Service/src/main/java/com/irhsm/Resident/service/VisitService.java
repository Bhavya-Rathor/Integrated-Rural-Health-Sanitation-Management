package com.irhsm.Resident.service;

import com.irhsm.Resident.dto.PendingItemDto;
import com.irhsm.Resident.entity.FollowUpStatus;
import com.irhsm.Resident.entity.Priority;
import com.irhsm.Resident.entity.Resident;
import com.irhsm.Resident.entity.VisitRecord;
import com.irhsm.Resident.exception.InvalidStatusException;
import com.irhsm.Resident.exception.VisitNotFoundException;
import com.irhsm.Resident.repository.MedicalInfoRepository;
import com.irhsm.Resident.repository.ResidentRepository;
import com.irhsm.Resident.repository.VisitRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisitService {

    private final VisitRecordRepository visitRepository;
    private final ResidentRepository residentRepository;
    private final MedicalInfoRepository medicalInfoRepository;
    private final ResidentService residentService;

    public VisitService(VisitRecordRepository visitRepository,
                        ResidentRepository residentRepository,
                        MedicalInfoRepository medicalInfoRepository,
                        ResidentService residentService) {
        this.visitRepository = visitRepository;
        this.residentRepository = residentRepository;
        this.medicalInfoRepository = medicalInfoRepository;
        this.residentService = residentService;
    }

    @Transactional
    public VisitRecord addVisit(Long residentId, VisitRecord v) {
        Resident r = residentService.getById(residentId); // 404 if the resident doesn't exist
        v.setId(null);
        v.setResident(r);

        if (v.isFollowUpRequired()) {
            if (v.getFollowUpDate() == null) {
                throw new IllegalArgumentException("Follow-up date is required when a follow-up is needed");
            }
            v.setFollowUpStatus(FollowUpStatus.PENDING);
        } else {
            v.setFollowUpDate(null);
            v.setFollowUpStatus(FollowUpStatus.NONE);
        }
        if (v.getPriority() == null) {
            v.setPriority(Priority.MEDIUM);
        }
        return visitRepository.save(v);
    }

    public List<VisitRecord> getHistory(Long residentId) {
        residentService.getById(residentId);
        return visitRepository.findByResidentIdOrderByVisitDateDesc(residentId);
    }

    // GET /api/residents/summary
    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total", residentRepository.countByActiveTrue());
        summary.put("pending", getPendingFollowUps().size());
        summary.put("chronic", medicalInfoRepository.countWithChronicConditions());
        summary.put("visitsThisMonth",
                visitRepository.countByVisitDateGreaterThanEqual(LocalDate.now().withDayOfMonth(1)));
        return summary;
    }

    // GET /api/residents/pending : follow-ups due today or earlier
    public List<PendingItemDto> getPendingFollowUps() {
        List<VisitRecord> due = visitRepository.findByFollowUpStatusInAndFollowUpDateLessThanEqual(
                List.of(FollowUpStatus.PENDING, FollowUpStatus.ASSIGNED), LocalDate.now());

        return due.stream()
                .filter(v -> v.getResident().isActive())
                // enum order is HIGH, MEDIUM, LOW, so HIGH comes first; then oldest date first
                .sorted(Comparator.comparing(VisitRecord::getPriority)
                        .thenComparing(VisitRecord::getFollowUpDate))
                .map(this::toPendingItem)
                .toList();
    }

    // PUT /api/residents/{visitId}/status
    @Transactional
    public PendingItemDto updateFollowUpStatus(Long visitId, String status) {
        VisitRecord v = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException(visitId));

        if (v.getFollowUpStatus() == FollowUpStatus.NONE) {
            throw new InvalidStatusException("Visit " + visitId + " has no follow-up");
        }
        if (v.getFollowUpStatus() == FollowUpStatus.DONE) {
            throw new InvalidStatusException("Follow-up " + visitId + " is already DONE");
        }

        FollowUpStatus newStatus;
        try {
            newStatus = FollowUpStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidStatusException("Status must be ASSIGNED or DONE");
        }
        if (newStatus != FollowUpStatus.ASSIGNED && newStatus != FollowUpStatus.DONE) {
            throw new InvalidStatusException("Status must be ASSIGNED or DONE");
        }

        v.setFollowUpStatus(newStatus);
        visitRepository.save(v);
        return toPendingItem(v);
    }

    private PendingItemDto toPendingItem(VisitRecord v) {
        return new PendingItemDto(
                "resident",
                v.getId(),
                "Follow-up: " + v.getResident().getFullName() + " (" + v.getComplaint() + ")",
                v.getResident().getVillage(),
                v.getFollowUpStatus().name(),
                v.getPriority().name(),
                v.getFollowUpDate().toString());
    }
}