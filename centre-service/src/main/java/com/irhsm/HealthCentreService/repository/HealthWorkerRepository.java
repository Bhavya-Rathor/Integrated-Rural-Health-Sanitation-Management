package com.irhsm.HealthCentreService.repository;

import com.irhsm.HealthCentreService.entity.HealthWorker;
import com.irhsm.HealthCentreService.entity.WorkerRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HealthWorkerRepository extends JpaRepository<HealthWorker, Long> {
    List<HealthWorker> findByCentreId(Long centreId);
    List<HealthWorker> findByCentreIdAndActiveTrue(Long centreId);
    List<HealthWorker> findByRole(WorkerRole role);
    List<HealthWorker> findByNameContainingIgnoreCase(String name);
    long countByActiveTrue();
}