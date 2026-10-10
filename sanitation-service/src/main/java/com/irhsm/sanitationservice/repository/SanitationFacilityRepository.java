package com.irhsm.sanitationservice.repository;

import com.irhsm.sanitationservice.entity.SanitationFacility;
import com.irhsm.sanitationservice.enums.FacilityCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SanitationFacilityRepository extends JpaRepository<SanitationFacility, Long> {

    // spring builds the sql from the method name:
    // select * from sanitation_facility where lower(village) = LOWER(?)
    List<SanitationFacility> findByVillageIgnoreCase(String village);

    // used later by /summary to count FUNCTIONAL, NEEDS_REPAIR, ...
    long countByCondition(FacilityCondition condition);
}
