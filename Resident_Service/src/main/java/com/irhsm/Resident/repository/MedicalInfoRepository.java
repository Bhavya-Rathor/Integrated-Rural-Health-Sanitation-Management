package com.irhsm.Resident.repository;

import com.irhsm.Resident.entity.MedicalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MedicalInfoRepository extends JpaRepository<MedicalInfo, Long> {

    Optional<MedicalInfo> findByResidentId(Long residentId);

    @Query("select count(m) from MedicalInfo m " +
           "where m.resident.active = true " +
           "and m.chronicConditions is not null " +
           "and m.chronicConditions <> '' " +
           "and lower(m.chronicConditions) <> 'none'")
    long countWithChronicConditions();
}