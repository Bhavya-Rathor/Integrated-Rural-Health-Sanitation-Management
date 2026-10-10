package com.irhsm.healthCampService.repository;          

import com.irhsm.healthCampService.entity.CampStatus;    // Enum used in a method below
import com.irhsm.healthCampService.entity.HealthCamp;    // The entity this repository manages
import org.springframework.data.jpa.repository.JpaRepository; 

import java.time.LocalDate;                              
import java.util.*;                                    

// JpaRepository  <Entity type, type of its id>
public interface HealthCampRepository extends JpaRepository<HealthCamp, Long> {

    List<HealthCamp> findByStatus(CampStatus status);    
    List<HealthCamp> findByVillage(String village);      
    long countByStatus(CampStatus status);                
    long countByCampDateAfter(LocalDate date);           
}