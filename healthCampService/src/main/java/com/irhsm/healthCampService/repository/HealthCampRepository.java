package com.irhsm.healthCampService.repository;          

import com.irhsm.healthCampService.entity.CampStatus;    // Enum used in a method below
import com.irhsm.healthCampService.entity.HealthCamp;    // The entity this repository manages
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;                              
import java.util.*;                                    

// JpaRepository  <Entity type, type of its id>
public interface HealthCampRepository extends JpaRepository<HealthCamp, Long> {

    List<HealthCamp> findByStatus(CampStatus status);    
    List<HealthCamp> findByVillage(String village);      
    long countByStatus(CampStatus status);                
    long countByCampDateAfter(LocalDate date);           

    // For the list page filters (newest camp first)
    List<HealthCamp> findAllByOrderByCampDateDesc();
    List<HealthCamp> findByVillageIgnoreCaseOrderByCampDateDesc(String village);
    List<HealthCamp> findByStatusOrderByCampDateDesc(CampStatus status);
    List<HealthCamp> findByVillageIgnoreCaseAndStatusOrderByCampDateDesc(String village, CampStatus status);

    // Upcoming camps for the dashboard tile
    long countByStatusAndCampDateGreaterThanEqual(CampStatus status, LocalDate date);

    // Village names for the filter dropdown
    @Query("SELECT DISTINCT c.village FROM HealthCamp c ORDER BY c.village")
    List<String> findVillages();
}
