package com.irhsm.sanitationservice.service;

import com.irhsm.sanitationservice.dto.InspectionRequest;
import com.irhsm.sanitationservice.entity.CleanlinessInspection;
import com.irhsm.sanitationservice.entity.SanitationFacility;
import com.irhsm.sanitationservice.enums.FacilityCondition;
import com.irhsm.sanitationservice.repository.CleanlinessInspectionRepository;
import com.irhsm.sanitationservice.repository.SanitationFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectionService {

    private final CleanlinessInspectionRepository inspectionRepository;
    private final SanitationFacilityRepository facilityRepository;

    public List<CleanlinessInspection> findByFacility(Long facilityId) {
        return inspectionRepository.findByFacilityIdOrderByInspectionDateDesc(facilityId);
    }

    @Transactional
    public CleanlinessInspection create(Long facilityId, InspectionRequest req) {
        SanitationFacility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Facility " + facilityId + " not found"));

        CleanlinessInspection inspection = new CleanlinessInspection();
        inspection.setFacility(facility);
        inspection.setInspectionDate(req.inspectionDate());
        inspection.setInspectorName(req.inspectorName());
        inspection.setCleanlinessScore(req.cleanlinessScore());
        inspection.setWaterAvailable(req.waterAvailable());
        inspection.setRemarks(req.remarks());

        // Rule: a poor score or no water means a working facility now needs repair
        boolean failed = req.cleanlinessScore() <= 2 || !req.waterAvailable();
        if (failed && facility.getCondition() == FacilityCondition.FUNCTIONAL) {
            facility.setCondition(FacilityCondition.NEEDS_REPAIR);
            facilityRepository.save(facility);
        }
        return inspectionRepository.save(inspection);
    }
}