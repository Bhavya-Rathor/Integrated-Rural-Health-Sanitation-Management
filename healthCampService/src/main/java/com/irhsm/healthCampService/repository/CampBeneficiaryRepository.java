package com.irhsm.healthCampService.repository;

import com.irhsm.healthCampService.entity.CampBeneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CampBeneficiaryRepository extends JpaRepository<CampBeneficiary, Long> {

    List<CampBeneficiary> findByCampId(Long campId);                    
    boolean existsByCampIdAndResidentId(Long campId, Long residentId);  
    long countByCampId(Long campId);                                    
}
