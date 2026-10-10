package com.irhsm.Resident.repository;

import com.irhsm.Resident.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Long> {

    List<Resident> findByActiveTrue();

    List<Resident> findByActiveTrueAndVillage(String village);

    List<Resident> findByActiveTrueAndFullNameContainingIgnoreCase(String q);

    List<Resident> findByActiveTrueAndVillageAndFullNameContainingIgnoreCase(String village, String q);

    boolean existsByAbhaNumber(String abhaNumber);

    long countByActiveTrue();
}