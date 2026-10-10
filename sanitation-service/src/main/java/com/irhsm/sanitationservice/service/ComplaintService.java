package com.irhsm.sanitationservice.service;

import com.irhsm.sanitationservice.dto.ComplaintRequest;
import com.irhsm.sanitationservice.entity.Complaint;
import com.irhsm.sanitationservice.entity.SanitationFacility;
import com.irhsm.sanitationservice.enums.FacilityCondition;
import com.irhsm.sanitationservice.enums.Priority;
import com.irhsm.sanitationservice.enums.WorkStatus;
import com.irhsm.sanitationservice.repository.ComplaintRepository;
import com.irhsm.sanitationservice.repository.SanitationFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final SanitationFacilityRepository facilityRepository;

    public List<Complaint> findAll() {
        return complaintRepository.findAll();
    }

    public Complaint findById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Complaint " + id + " not found"));
    }

    public List<Complaint> findOpen() {
        return complaintRepository.findByStatusNotOrderByReportedOnDesc(WorkStatus.RESOLVED);
    }

    public long countAll() {
        return complaintRepository.count();
    }

    public long countOpen() {
        return complaintRepository.countByStatusNot(WorkStatus.RESOLVED);
    }

    @Transactional   // both saves succeed together or neither does
    public Complaint create(ComplaintRequest req) {
        SanitationFacility facility = facilityRepository.findById(req.facilityId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Facility " + req.facilityId() + " not found"));

        Complaint c = new Complaint();
        c.setFacility(facility);
        c.setTitle(req.title());
        c.setDescription(req.description());
        c.setReportedBy(req.reportedBy());
        c.setPriority(req.priority());
        c.setStatus(WorkStatus.OPEN);          // every complaint starts OPEN
        c.setReportedOn(LocalDate.now());

        // Rule: a HIGH complaint means a working facility now needs repair
        if (req.priority() == Priority.HIGH && facility.getCondition() == FacilityCondition.FUNCTIONAL) {
            facility.setCondition(FacilityCondition.NEEDS_REPAIR);
            facilityRepository.save(facility);
        }
        return complaintRepository.save(c);
    }

    @Transactional
    public Complaint updateStatus(Long id, String statusText, String remarks) {
        WorkStatus newStatus;
        try {
            newStatus = WorkStatus.valueOf(statusText.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown status: " + statusText);
        }

        Complaint c = findById(id);
        c.setStatus(newStatus);
        if (remarks != null && !remarks.isBlank()) {
            c.setOfficerRemarks(remarks);
        }
        c.setResolvedOn(newStatus == WorkStatus.RESOLVED ? LocalDate.now() : null);
        return complaintRepository.save(c);
    }

    public List<Complaint> findByFacility(Long facilityId) {
        return complaintRepository.findByFacilityId(facilityId);
    }

    public List<Complaint> search(WorkStatus status) {
        return (status == null)
                ? complaintRepository.findAllByOrderByReportedOnDesc()
                : complaintRepository.findByStatusOrderByReportedOnDesc(status);
    }
}