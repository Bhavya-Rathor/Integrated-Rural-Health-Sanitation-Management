package com.irhsm.sanitationservice.repository;

import com.irhsm.sanitationservice.entity.CleanlinessInspection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CleanlinessInspectionRepository extends JpaRepository<CleanlinessInspection, Long> {

    // Newest inspection first, for the facility detail page
    List<CleanlinessInspection> findByFacilityIdOrderByInspectionDateDesc(Long facilityId);
}