package com.irhsm.HealthCentreService.repository;

import com.irhsm.HealthCentreService.entity.Facility;
import com.irhsm.HealthCentreService.entity.FacilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByCentreId(Long centreId);
    List<Facility> findByStatus(FacilityStatus status);
    List<Facility> findByNameContainingIgnoreCase(String name);
    long countByStatus(FacilityStatus status);
    List<Facility> findByStatusNot(FacilityStatus status);
    long countByStatusNot(FacilityStatus status);
}

