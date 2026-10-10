package com.irhsm.sanitationservice.controller.api;


import com.irhsm.sanitationservice.entity.SanitationFacility;
import com.irhsm.sanitationservice.service.FacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sanitation/facilities")
@RequiredArgsConstructor
public class FacilityApiController {

    private final FacilityService facilityService;

    // GET /api/sanitation/facilities  -> all
    // GET /api/sanitation/facilities?village = X -> one village (the Water service will call this)
    @GetMapping
    public List<SanitationFacility> getAll(@RequestParam(required = false) String village) {
        return facilityService.findAll(village);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanitationFacility> getOne(@PathVariable Long id){
        return facilityService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SanitationFacility> create(@Valid @RequestBody SanitationFacility facility){
        return ResponseEntity.status(HttpStatus.CREATED).body(facilityService.create(facility));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanitationFacility> update(@PathVariable Long id, @Valid @RequestBody SanitationFacility facility){
        return facilityService.update(id, facility)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        return facilityService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
