package com.irhsm.healthCampService.repository;

import com.irhsm.healthCampService.entity.Screening;
import com.irhsm.healthCampService.entity.ScreeningResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByBeneficiaryId(Long beneficiaryId);
    List<Screening> findByBeneficiaryResidentId(Long residentId);
    long countByResultNot(ScreeningResult result);

    // All screenings of one camp (beneficiary -> camp -> id), newest first
    List<Screening> findByBeneficiaryCampIdOrderByScreenedAtDesc(Long campId);
}
