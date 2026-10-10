package com.irhsm.healthCampService.repository;

import com.irhsm.healthCampService.entity.CampStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CampStaffRepository extends JpaRepository<CampStaff, Long> {

    List<CampStaff> findByCampId(Long campId);            //  staff of one camp
}
