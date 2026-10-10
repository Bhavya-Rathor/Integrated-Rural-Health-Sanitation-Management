package com.irhsm.sanitationservice.controller.api;

import com.irhsm.sanitationservice.dto.InspectionRequest;
import com.irhsm.sanitationservice.entity.CleanlinessInspection;
import com.irhsm.sanitationservice.service.InspectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sanitation/facilities/{facilityId}/inspections")
@RequiredArgsConstructor
public class InspectionApiController {

    private final InspectionService inspectionService;

    @GetMapping
    public List<CleanlinessInspection> getForFacility(@PathVariable Long facilityId) {
        return inspectionService.findByFacility(facilityId);
    }

    @PostMapping
    public ResponseEntity<CleanlinessInspection> create(@PathVariable Long facilityId,
                                                        @Valid @RequestBody InspectionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inspectionService.create(facilityId, req));
    }
}