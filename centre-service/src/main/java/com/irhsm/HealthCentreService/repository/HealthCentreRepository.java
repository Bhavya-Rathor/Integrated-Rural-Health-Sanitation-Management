package com.irhsm.HealthCentreService.repository;

import com.irhsm.HealthCentreService.entity.HealthCentre;
import org.springframework.data.jpa.repository.JpaRepository;
import  java.util.List;
import com.irhsm.HealthCentreService.entity.CentreType;
import com.irhsm.HealthCentreService.entity.CentreStatus;

public interface HealthCentreRepository extends JpaRepository<HealthCentre, Long> {
    List<HealthCentre> findByVillage(String village);
    List<HealthCentre> findByType(CentreType type);
    List<HealthCentre> findByNameContainingIgnoreCase(String name);
    long countByStatus(CentreStatus status);
}

